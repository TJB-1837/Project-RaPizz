package rapizz.ui;

import java.awt.*;
import java.awt.event.*;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import rapizz.dao.*;
import rapizz.model.*;

public class MainFrame extends JFrame {

    // ── Palette pastel ────────────────────────────────────────────────────────
    private static final Color BG          = new Color(0xFAF8F5);
    private static final Color PANEL_BG    = new Color(0xFFFFFF);
    private static final Color ACCENT      = new Color(0xE8735A); // terracotta doux
    private static final Color ACCENT_SOFT = new Color(0xFDE8E3);
    private static final Color SAGE        = new Color(0x8BAF8E); // vert sauge
    private static final Color SAGE_SOFT   = new Color(0xE4EEE5);
    private static final Color SAND        = new Color(0xD4B896); // sable
    private static final Color TEXT_DARK   = new Color(0x2D2A26);
    private static final Color TEXT_MID    = new Color(0x6B6460);
    private static final Color TEXT_LIGHT  = new Color(0xA09890);
    private static final Color BORDER_CLR  = new Color(0xE8E2DC);
    private static final Color ROW_ALT     = new Color(0xFDF5F0);

    private static final Font FONT_TITLE   = new Font("Segoe UI", Font.BOLD,  22);
    private static final Font FONT_SECTION = new Font("Segoe UI", Font.BOLD,  14);
    private static final Font FONT_LABEL   = new Font("Segoe UI", Font.PLAIN, 13);
    private static final Font FONT_SMALL   = new Font("Segoe UI", Font.PLAIN, 11);
    private static final Font FONT_MONO    = new Font("Consolas",  Font.PLAIN, 12);

    // ── DAOs ──────────────────────────────────────────────────────────────────
    private final FicheLivraisonDAO ficheLivraisonDAO = new JDBCFicheLivraisonDAO();
    private final PizzaMenuDAO      pizzaMenuDAO      = new JDBCPizzaMenuDAO();
    private final StatisticsDAO     statsDAO          = new StatisticsDAO();
    private final CommandeDAO       commandeDAO       = new JDBCCommandeDAO();

    // ── Onglets ───────────────────────────────────────────────────────────────
    private JTabbedPane tabs;

    // ── Tables ────────────────────────────────────────────────────────────────
    private JTable tblMenu, tblLivraisons, tblVehicules,
                   tblOrdersPerClient, tblAboveAvg,
                   tblBestClient, tblWorstDeliverer,
                   tblMostPizza, tblLeastPizza, tblFavoriteIngredient;
    private JLabel lblAvgOrders;
    private JLabel lblRevenueTotal;
    private JLabel lblRevenueMonth;
    private JLabel lblStatus;

    // ── Nouvelle commande ────────────────────────────────────────────────────
    private JComboBox<ComboItem<Client>> cmbClient;
    private JComboBox<ComboItem<Pizza>> cmbPizza;
    private JComboBox<ComboItem<Livreur>> cmbLivreur;
    private JComboBox<ComboItem<Vehicule>> cmbVehicule;
    private JComboBox<String> cmbSize;
    private JLabel lblClientBalance;
    private JLabel lblPrice;
    private JLabel lblBalanceAfter;

    // ─────────────────────────────────────────────────────────────────────────

    public MainFrame() {
        super("RaPizz — Gestion");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1100, 720);
        setMinimumSize(new Dimension(900, 600));
        setLocationRelativeTo(null);
        getContentPane().setBackground(BG);

        buildUI();
        refreshAll();
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  Construction de l'UI
    // ══════════════════════════════════════════════════════════════════════════

    private void buildUI() {
        setLayout(new BorderLayout(0, 0));

        add(buildHeader(),  BorderLayout.NORTH);
        add(buildTabs(),    BorderLayout.CENTER);
        add(buildStatusBar(), BorderLayout.SOUTH);
    }

    // ── En-tête ───────────────────────────────────────────────────────────────

    private JPanel buildHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(ACCENT);
        header.setBorder(new EmptyBorder(16, 28, 16, 28));

        JLabel logo = new JLabel("🍕  RaPizz");
        logo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        logo.setForeground(Color.WHITE);

        JLabel subtitle = new JLabel("Système de gestion interne");
        subtitle.setFont(FONT_SMALL);
        subtitle.setForeground(new Color(0xFFFFFF, true));
        subtitle.setForeground(new Color(255, 255, 255, 180));

        JPanel left = new JPanel();
        left.setOpaque(false);
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
        left.add(logo);
        left.add(Box.createVerticalStrut(2));
        left.add(subtitle);

        JButton btnRefresh = buildIconButton("↻  Actualiser", ACCENT_SOFT, ACCENT);
        btnRefresh.addActionListener(e -> refreshAll());

        header.add(left,       BorderLayout.WEST);
        header.add(btnRefresh, BorderLayout.EAST);
        return header;
    }

    // ── Barre de statut ───────────────────────────────────────────────────────

    private JPanel buildStatusBar() {
        JPanel bar = new JPanel(new BorderLayout());
        bar.setBackground(new Color(0xF0EBE5));
        bar.setBorder(new CompoundBorder(
            new MatteBorder(1, 0, 0, 0, BORDER_CLR),
            new EmptyBorder(5, 16, 5, 16)));

        lblStatus = new JLabel("Prêt.");
        lblStatus.setFont(FONT_SMALL);
        lblStatus.setForeground(TEXT_MID);

        bar.add(lblStatus, BorderLayout.WEST);
        return bar;
    }

    // ── Onglets principaux ────────────────────────────────────────────────────

    private JTabbedPane buildTabs() {
        tabs = new JTabbedPane(JTabbedPane.LEFT);
        tabs.setBackground(BG);
        tabs.setFont(FONT_LABEL);
        tabs.setForeground(TEXT_DARK);

        UIManager.put("TabbedPane.selected",         ACCENT_SOFT);
        UIManager.put("TabbedPane.contentAreaColor", PANEL_BG);
        UIManager.put("TabbedPane.focus",            ACCENT);

        tabs.addTab("  Carte / Menu  ",    buildMenuPanel());
        tabs.addTab("  Fiches livraison ", buildLivraisonsPanel());
        tabs.addTab("  Statistiques     ", buildStatsPanel());
        tabs.addTab("  Nouvelle commande ", buildOrderPanel());

        return tabs;
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  Onglet 1 — Carte / Menu
    // ══════════════════════════════════════════════════════════════════════════

    private JPanel buildMenuPanel() {
        JPanel panel = new JPanel(new BorderLayout(12, 12));
        panel.setBackground(BG);
        panel.setBorder(new EmptyBorder(20, 24, 20, 24));

        panel.add(buildSectionHeader("Carte des pizzas",
                "Nom, ingrédients et tarifs par taille", ACCENT), BorderLayout.NORTH);

        String[] cols = {"Pizza", "Ingrédients", "Naine (×0.67)", "Humaine (prix base)", "Ogresse (×1.33)"};
        tblMenu = buildStyledTable(cols);
        tblMenu.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        JScrollPane scroll = styledScroll(tblMenu);

        // Largeurs colonnes
        tblMenu.getColumnModel().getColumn(0).setPreferredWidth(130);
        tblMenu.getColumnModel().getColumn(1).setPreferredWidth(350);
        tblMenu.getColumnModel().getColumn(2).setPreferredWidth(110);
        tblMenu.getColumnModel().getColumn(3).setPreferredWidth(130);
        tblMenu.getColumnModel().getColumn(4).setPreferredWidth(110);

        panel.add(scroll, BorderLayout.CENTER);

        JPanel footer = buildRefreshFooter("Rafraîchir la carte", e -> loadMenu());
        panel.add(footer, BorderLayout.SOUTH);

        return panel;
    }

    private void loadMenu() {
        setStatus("Chargement de la carte…");
        DefaultTableModel model = (DefaultTableModel) tblMenu.getModel();
        model.setRowCount(0);

        SwingWorker<List<PizzaMenu>, Void> worker = new SwingWorker<>() {
            @Override
            protected List<PizzaMenu> doInBackground() throws Exception {
                return pizzaMenuDAO.findMenu();
            }

            @Override
            protected void done() {
                try {
                    List<PizzaMenu> items = get();
                    for (PizzaMenu pm : items) {
                        double base = pm.getPrixDeBase();
                        double naine = base * 2.0 / 3.0;
                        double ogresse = base * 4.0 / 3.0;

                        String ingredientsText;
                        java.util.Map<String, Integer> ingredients = pm.getIngredients();
                        if (ingredients == null || ingredients.isEmpty()) {
                            ingredientsText = "—";
                        } else {
                            StringBuilder sb = new StringBuilder();
                            int i = 0;
                            for (java.util.Map.Entry<String, Integer> entry : ingredients.entrySet()) {
                                if (i > 0) sb.append(", ");
                                sb.append(entry.getKey());
                                if (entry.getValue() != null && entry.getValue() > 0) {
                                    sb.append(" x").append(entry.getValue());
                                }
                                i++;
                            }
                            ingredientsText = sb.toString();
                        }

                        model.addRow(new Object[]{
                            pm.getNomPizza(),
                            ingredientsText,
                            String.format("%.2f €", naine),
                            String.format("%.2f €", base),
                            String.format("%.2f €", ogresse)
                        });
                    }

                    fitColumnToContent(tblMenu, 1, 520);

                    setStatus(items.size() + " pizza(s) chargée(s).");
                } catch (Exception ex) {
                    showError("Erreur chargement menu", ex);
                }
            }
        };
        worker.execute();
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  Onglet 2 — Fiches de livraison
    // ══════════════════════════════════════════════════════════════════════════

    private JPanel buildLivraisonsPanel() {
        JPanel panel = new JPanel(new BorderLayout(12, 12));
        panel.setBackground(BG);
        panel.setBorder(new EmptyBorder(20, 24, 20, 24));

        panel.add(buildSectionHeader("Fiches de livraison",
                "Livreur, véhicule, client, date et retard éventuel", SAGE), BorderLayout.NORTH);

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        String[] cols = {"Livreur", "Véhicule", "Client", "Pizza", "Date", "Retard (min)", "Prix facturé"};
        tblLivraisons = buildStyledTable(cols);

        // Rendu conditionnel : retard en rouge
        tblLivraisons.getColumnModel().getColumn(5).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object val,
                    boolean sel, boolean foc, int row, int col) {
                Component c = super.getTableCellRendererComponent(t, val, sel, foc, row, col);
                try {
                    int retard = Integer.parseInt(val.toString());
                    if (retard > 0) {
                        c.setForeground(new Color(0xC0392B));
                        setFont(getFont().deriveFont(Font.BOLD));
                    } else {
                        c.setForeground(SAGE);
                        setFont(getFont().deriveFont(Font.PLAIN));
                    }
                } catch (Exception ignored) {}
                return c;
            }
        });

        tblLivraisons.getColumnModel().getColumn(0).setPreferredWidth(120);
        tblLivraisons.getColumnModel().getColumn(1).setPreferredWidth(90);
        tblLivraisons.getColumnModel().getColumn(2).setPreferredWidth(120);
        tblLivraisons.getColumnModel().getColumn(3).setPreferredWidth(130);
        tblLivraisons.getColumnModel().getColumn(4).setPreferredWidth(120);
        tblLivraisons.getColumnModel().getColumn(5).setPreferredWidth(90);
        tblLivraisons.getColumnModel().getColumn(6).setPreferredWidth(100);

        JScrollPane scroll = styledScroll(tblLivraisons);
        panel.add(scroll, BorderLayout.CENTER);

        JPanel footer = buildRefreshFooter("Rafraîchir les fiches", e -> loadLivraisons());
        panel.add(footer, BorderLayout.SOUTH);
        return panel;
    }

    private void loadLivraisons() {
        setStatus("Chargement des fiches de livraison…");
        DefaultTableModel model = (DefaultTableModel) tblLivraisons.getModel();
        model.setRowCount(0);
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        SwingWorker<List<FicheLivraison>, Void> worker = new SwingWorker<>() {
            @Override
            protected List<FicheLivraison> doInBackground() throws Exception {
                return ficheLivraisonDAO.findAll();
            }

            @Override
            protected void done() {
                try {
                    List<FicheLivraison> fiches = get();
                    for (FicheLivraison f : fiches) {
                        model.addRow(new Object[]{
                            f.getNomLivreur() + " " + f.getPrenomLivreur(),
                            f.getTypeVehicule(),
                            f.getNomClient() + " " + f.getPrenomClient(),
                            f.getNomPizza(),
                            f.getDate() != null ? f.getDate().format(fmt) : "—",
                            f.getRetard(),
                            String.format("%.2f €", f.getPrixFacture())
                        });
                    }
                    setStatus(fiches.size() + " livraison(s) chargée(s).");
                } catch (Exception ex) {
                    showError("Erreur chargement livraisons", ex);
                }
            }
        };
        worker.execute();
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  Onglet 3 — Statistiques
    // ══════════════════════════════════════════════════════════════════════════

    private JPanel buildStatsPanel() {
        JPanel panel = new JPanel(new BorderLayout(12, 16));
        panel.setBackground(BG);
        panel.setBorder(new EmptyBorder(20, 24, 20, 24));

        JPanel top = new JPanel(new BorderLayout(0, 12));
        top.setOpaque(false);
        top.add(buildSectionHeader("Statistiques",
            "Clients, livreurs, pizzas et ingrédients", SAND), BorderLayout.NORTH);
        top.add(buildRevenueBand(), BorderLayout.SOUTH);
        panel.add(top, BorderLayout.NORTH);

        // Zone centrale — deux colonnes
        JPanel center = new JPanel(new GridLayout(1, 2, 16, 0));
        center.setOpaque(false);

        // ── Colonne gauche ─────────────────────────────────────────────────
        JPanel left = new JPanel();
        left.setOpaque(false);
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));

        // Bloc véhicules
        JPanel vehiculesCard = buildCard("Véhicules n'ayant jamais servi");
        tblVehicules = buildStyledTable(new String[]{"Véhicule"});
        vehiculesCard.add(styledScroll(tblVehicules), BorderLayout.CENTER);
        addCardToColumn(left, vehiculesCard);

        // Bloc moyenne
        JPanel avgCard = buildCard("Moyenne de commandes par client");
        avgCard.setPreferredSize(new Dimension(0, 40));
        lblAvgOrders = new JLabel("—");
        lblAvgOrders.setFont(new Font("Segoe UI", Font.BOLD, 32));
        lblAvgOrders.setForeground(ACCENT);
        lblAvgOrders.setHorizontalAlignment(SwingConstants.CENTER);
        JLabel avgLabel = new JLabel("commandes / client");
        avgLabel.setFont(FONT_SMALL);
        avgLabel.setForeground(TEXT_MID);
        avgLabel.setHorizontalAlignment(SwingConstants.CENTER);
        JPanel avgInner = new JPanel(new GridLayout(2, 1, 0, 4));
        avgInner.setOpaque(false);
        avgInner.add(lblAvgOrders);
        avgInner.add(avgLabel);
        avgCard.add(avgInner, BorderLayout.CENTER);
        addCardToColumn(left, avgCard);

        // Bloc meilleur client
        JPanel bestClientCard = buildCard("Meilleur client");
        tblBestClient = buildStyledTable(new String[]{"Nom", "Prénom", "Commandes"});
        bestClientCard.add(styledScroll(tblBestClient), BorderLayout.CENTER);
        addCardToColumn(left, bestClientCard);

        // Bloc ingrédient favori
        JPanel ingredientCard = buildCard("Ingrédient favori");
        tblFavoriteIngredient = buildStyledTable(new String[]{"Ingrédient", "Quantité"});
        ingredientCard.add(styledScroll(tblFavoriteIngredient), BorderLayout.CENTER);
        addCardToColumn(left, ingredientCard);

        center.add(left);

        // ── Colonne droite ─────────────────────────────────────────────────
        JPanel right = new JPanel();
        right.setOpaque(false);
        right.setLayout(new BoxLayout(right, BoxLayout.Y_AXIS));

        // Commandes par client
        JPanel ordersCard = buildCard("Commandes par client");
        tblOrdersPerClient = buildStyledTable(new String[]{"Nom", "Prénom", "Commandes"});
        ordersCard.add(styledScroll(tblOrdersPerClient), BorderLayout.CENTER);
        addCardToColumn(right, ordersCard);

        // Clients > moyenne
        JPanel aboveCard = buildCard("Clients au-dessus de la moyenne");
        tblAboveAvg = buildStyledTable(new String[]{"Nom", "Prénom", "Commandes"});
        aboveCard.add(styledScroll(tblAboveAvg), BorderLayout.CENTER);
        addCardToColumn(right, aboveCard);

        // Livreur le plus en retard
        JPanel worstCard = buildCard("Livreur le plus en retard");
        tblWorstDeliverer = buildStyledTable(new String[]{"Livreur", "Véhicule", "Retards"});
        worstCard.add(styledScroll(tblWorstDeliverer), BorderLayout.CENTER);
        addCardToColumn(right, worstCard);

        // Pizza la plus demandée
        JPanel mostPizzaCard = buildCard("Pizza la plus demandée");
        tblMostPizza = buildStyledTable(new String[]{"Pizza", "Commandes"});
        mostPizzaCard.add(styledScroll(tblMostPizza), BorderLayout.CENTER);
        addCardToColumn(right, mostPizzaCard);

        // Pizza la moins demandée
        JPanel leastPizzaCard = buildCard("Pizza la moins demandée");
        tblLeastPizza = buildStyledTable(new String[]{"Pizza", "Commandes"});
        leastPizzaCard.add(styledScroll(tblLeastPizza), BorderLayout.CENTER);
        addCardToColumn(right, leastPizzaCard);

        center.add(right);
        JScrollPane centerScroll = new JScrollPane(center,
            ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
            ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        centerScroll.setBorder(null);
        centerScroll.getViewport().setBackground(BG);
        panel.add(centerScroll, BorderLayout.CENTER);

        JPanel footer = buildRefreshFooter("Rafraîchir les statistiques", e -> loadStats());
        panel.add(footer, BorderLayout.SOUTH);
        return panel;
    }

    private void loadStats() {
        setStatus("Calcul des statistiques…");

        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            List<String[]> unusedVehicles, ordersPerClient, aboveAvg;
            List<String[]> bestClient, worstDeliverer, mostPizza, leastPizza, favoriteIngredient;
            double avg;
            double totalRevenue;

            @Override
            protected Void doInBackground() throws Exception {
                unusedVehicles  = statsDAO.getUnusedVehicles();
                ordersPerClient = statsDAO.getOrdersPerClient();
                avg             = statsDAO.getAverageOrders();
                aboveAvg        = statsDAO.getClientsAboveAverage();
                bestClient       = statsDAO.getBestClient();
                worstDeliverer   = statsDAO.getWorstDeliverer();
                mostPizza        = statsDAO.getMostOrderedPizza();
                leastPizza       = statsDAO.getLeastOrderedPizza();
                favoriteIngredient = statsDAO.getFavoriteIngredient();
                totalRevenue    = statsDAO.getTotalRevenue();
                return null;
            }

            @Override
            protected void done() {
                try {
                    get(); // re-throw exception si besoin

                    fillTable(tblVehicules,       unusedVehicles);
                    fillTable(tblOrdersPerClient, ordersPerClient);
                    fillTable(tblAboveAvg,        aboveAvg);
                    fillTable(tblBestClient,       bestClient);
                    fillTable(tblWorstDeliverer,   worstDeliverer);
                    fillTable(tblMostPizza,        mostPizza);
                    fillTable(tblLeastPizza,       leastPizza);
                    fillTable(tblFavoriteIngredient, favoriteIngredient);
                    lblAvgOrders.setText(String.format("%.1f", avg));
                    lblRevenueTotal.setText(String.format("%.2f €", totalRevenue));
                    setStatus("Statistiques mises à jour.");
                } catch (Exception ex) {
                    showError("Erreur chargement statistiques", ex);
                }
            }
        };
        worker.execute();
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  Onglet 4 — Nouvelle commande
    // ══════════════════════════════════════════════════════════════════════════

    private JPanel buildOrderPanel() {
        JPanel panel = new JPanel(new BorderLayout(12, 12));
        panel.setBackground(BG);
        panel.setBorder(new EmptyBorder(20, 24, 20, 24));

        panel.add(buildSectionHeader("Nouvelle commande",
                "Saisie de commande, solde client et facturation", ACCENT), BorderLayout.NORTH);

        JPanel formCard = buildCard("Détails de la commande");
        formCard.add(buildOrderForm(), BorderLayout.CENTER);
        panel.add(formCard, BorderLayout.CENTER);

        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        footer.setOpaque(false);
        footer.setBorder(new EmptyBorder(10, 0, 0, 0));
        JButton btnReload = buildIconButton("↻  Recharger les listes", SAGE, Color.WHITE);
        btnReload.addActionListener(e -> loadOrderData());
        JButton btnCreate = buildIconButton("✔  Créer la commande", ACCENT, Color.WHITE);
        btnCreate.addActionListener(e -> submitOrder());
        footer.add(btnReload);
        footer.add(btnCreate);
        panel.add(footer, BorderLayout.SOUTH);

        loadOrderData();
        return panel;
    }

    private JPanel buildOrderForm() {
        JPanel form = new JPanel(new GridBagLayout());
        form.setOpaque(false);

        cmbClient = new JComboBox<>();
        cmbPizza = new JComboBox<>();
        cmbLivreur = new JComboBox<>();
        cmbVehicule = new JComboBox<>();
        cmbSize = new JComboBox<>(new String[]{"naine", "humaine", "ogresse"});
        lblClientBalance = new JLabel("—");
        lblClientBalance.setFont(FONT_LABEL);
        lblClientBalance.setForeground(TEXT_DARK);
        lblPrice = new JLabel("—");
        lblPrice.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblPrice.setForeground(ACCENT);
        lblBalanceAfter = new JLabel("—");
        lblBalanceAfter.setFont(FONT_LABEL);
        lblBalanceAfter.setForeground(TEXT_DARK);

        ActionListener update = e -> updateOrderPricing();
        cmbClient.addActionListener(update);
        cmbPizza.addActionListener(update);
        cmbSize.addActionListener(update);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;

        int row = 0;
        addFormRow(form, gbc, row++, "Client", cmbClient);
        addFormRow(form, gbc, row++, "Solde client", lblClientBalance);
        addFormRow(form, gbc, row++, "Pizza", cmbPizza);
        addFormRow(form, gbc, row++, "Taille", cmbSize);
        addFormRow(form, gbc, row++, "Livreur", cmbLivreur);
        addFormRow(form, gbc, row++, "Véhicule", cmbVehicule);
        addFormRow(form, gbc, row++, "Prix facturé", lblPrice);
        addFormRow(form, gbc, row++, "Solde après", lblBalanceAfter);

        return form;
    }

    private void addFormRow(JPanel form, GridBagConstraints gbc, int row, String label, JComponent field) {
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 1;
        gbc.weightx = 0.25;
        JLabel lbl = new JLabel(label);
        lbl.setFont(FONT_LABEL);
        lbl.setForeground(TEXT_MID);
        form.add(lbl, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.75;
        form.add(field, gbc);
    }

    private void loadOrderData() {
        setStatus("Chargement des données de commande…");

        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            List<Client> clients;
            List<Pizza> pizzas;
            List<Livreur> livreurs;
            List<Vehicule> vehicules;

            @Override
            protected Void doInBackground() throws Exception {
                clients = commandeDAO.getClients();
                pizzas = commandeDAO.getPizzas();
                livreurs = commandeDAO.getLivreurs();
                vehicules = commandeDAO.getVehicules();
                return null;
            }

            @Override
            protected void done() {
                try {
                    get();

                    cmbClient.setModel(buildClientModel(clients));
                    cmbPizza.setModel(buildPizzaModel(pizzas));
                    cmbLivreur.setModel(buildLivreurModel(livreurs));
                    cmbVehicule.setModel(buildVehiculeModel(vehicules));

                    updateOrderPricing();
                    setStatus("Données de commande chargées.");
                } catch (Exception ex) {
                    showError("Erreur chargement commande", ex);
                }
            }
        };
        worker.execute();
    }

    private void submitOrder() {
        Client client = getSelectedClient();
        Pizza pizza = getSelectedPizza();
        Livreur livreur = getSelectedLivreur();
        Vehicule vehicule = getSelectedVehicule();

        if (client == null || pizza == null || livreur == null || vehicule == null) {
            JOptionPane.showMessageDialog(this,
                "Merci de sélectionner un client, une pizza, un livreur et un véhicule.",
                "Commande incomplète", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String taille = (String) cmbSize.getSelectedItem();
        double multiplier = getTailleMultiplier(taille);

        try {
            commandeDAO.createCommande(client.getId(), pizza.getId(), livreur.getId(),
                vehicule.getId(), multiplier);

            setStatus("Commande enregistrée.");
            loadLivraisons();
            loadStats();
            loadOrderData();
            JOptionPane.showMessageDialog(this,
                "Commande enregistrée avec succès.",
                "Succès", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            showError("Erreur création commande", ex);
        }
    }

    private void updateOrderPricing() {
        Client client = getSelectedClient();
        Pizza pizza = getSelectedPizza();
        String taille = (String) cmbSize.getSelectedItem();

        if (client != null) {
            lblClientBalance.setText(String.format("%.2f €", client.getSolde()));
        } else {
            lblClientBalance.setText("—");
        }

        double prix = 0;
        if (pizza != null) {
            prix = pizza.getPrix(taille != null ? taille : "humaine");
        }

        lblPrice.setText(String.format("%.2f €", prix));

        if (client != null) {
            double after = client.getSolde() - prix;
            lblBalanceAfter.setText(String.format("%.2f €", after));
            if (after < 0) {
                lblBalanceAfter.setForeground(new Color(0xC0392B));
            } else {
                lblBalanceAfter.setForeground(TEXT_DARK);
            }
        } else {
            lblBalanceAfter.setText("—");
            lblBalanceAfter.setForeground(TEXT_DARK);
        }
    }

    private double getTailleMultiplier(String taille) {
        if (taille == null) {
            return 1.0;
        }
        switch (taille) {
            case "naine": return 2.0 / 3.0;
            case "ogresse": return 4.0 / 3.0;
            default: return 1.0;
        }
    }

    private DefaultComboBoxModel<ComboItem<Client>> buildClientModel(List<Client> clients) {
        DefaultComboBoxModel<ComboItem<Client>> model = new DefaultComboBoxModel<>();
        for (Client c : clients) {
            String label = c.getNom() + " " + c.getPrenom() + " (" + String.format("%.2f €", c.getSolde()) + ")";
            model.addElement(new ComboItem<>(label, c));
        }
        return model;
    }

    private DefaultComboBoxModel<ComboItem<Pizza>> buildPizzaModel(List<Pizza> pizzas) {
        DefaultComboBoxModel<ComboItem<Pizza>> model = new DefaultComboBoxModel<>();
        for (Pizza p : pizzas) {
            String label = p.getNom() + " (" + String.format("%.2f €", p.getPrixDeBase()) + ")";
            model.addElement(new ComboItem<>(label, p));
        }
        return model;
    }

    private DefaultComboBoxModel<ComboItem<Livreur>> buildLivreurModel(List<Livreur> livreurs) {
        DefaultComboBoxModel<ComboItem<Livreur>> model = new DefaultComboBoxModel<>();
        for (Livreur l : livreurs) {
            String label = l.getNom() + " " + l.getPrenom();
            model.addElement(new ComboItem<>(label, l));
        }
        return model;
    }

    private DefaultComboBoxModel<ComboItem<Vehicule>> buildVehiculeModel(List<Vehicule> vehicules) {
        DefaultComboBoxModel<ComboItem<Vehicule>> model = new DefaultComboBoxModel<>();
        for (Vehicule v : vehicules) {
            String label = v.getNom() + " (" + v.getType() + ")";
            model.addElement(new ComboItem<>(label, v));
        }
        return model;
    }

    private Client getSelectedClient() {
        ComboItem<Client> item = (ComboItem<Client>) cmbClient.getSelectedItem();
        return item != null ? item.getValue() : null;
    }

    private Pizza getSelectedPizza() {
        ComboItem<Pizza> item = (ComboItem<Pizza>) cmbPizza.getSelectedItem();
        return item != null ? item.getValue() : null;
    }

    private Livreur getSelectedLivreur() {
        ComboItem<Livreur> item = (ComboItem<Livreur>) cmbLivreur.getSelectedItem();
        return item != null ? item.getValue() : null;
    }

    private Vehicule getSelectedVehicule() {
        ComboItem<Vehicule> item = (ComboItem<Vehicule>) cmbVehicule.getSelectedItem();
        return item != null ? item.getValue() : null;
    }

    private JPanel buildRevenueBand() {
        JPanel band = new JPanel(new GridLayout(1, 2, 16, 0));
        band.setOpaque(false);

        JPanel totalCard = buildCard("CA total");
        totalCard.setPreferredSize(new Dimension(0, 180));
        lblRevenueTotal = new JLabel("—");
        lblRevenueTotal.setFont(new Font("Segoe UI", Font.BOLD, 34));
        lblRevenueTotal.setForeground(SAGE);
        lblRevenueTotal.setHorizontalAlignment(SwingConstants.CENTER);
        JLabel totalLabel = new JLabel("toutes périodes");
        totalLabel.setFont(FONT_SMALL);
        totalLabel.setForeground(TEXT_MID);
        totalLabel.setHorizontalAlignment(SwingConstants.CENTER);
        JPanel totalInner = new JPanel(new GridLayout(2, 1, 0, 4));
        totalInner.setOpaque(false);
        totalInner.add(lblRevenueTotal);
        totalInner.add(totalLabel);
        totalCard.add(totalInner, BorderLayout.CENTER);


        band.add(totalCard);
        return band;
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  Helpers UI
    // ══════════════════════════════════════════════════════════════════════════

    /** En-tête de section avec titre + sous-titre + barre colorée */
    private JPanel buildSectionHeader(String title, String subtitle, Color accent) {
        JPanel p = new JPanel(new BorderLayout(0, 4));
        p.setOpaque(false);
        p.setBorder(new EmptyBorder(0, 0, 14, 0));

        JLabel lTitle = new JLabel(title);
        lTitle.setFont(FONT_TITLE);
        lTitle.setForeground(TEXT_DARK);

        JLabel lSub = new JLabel(subtitle);
        lSub.setFont(FONT_LABEL);
        lSub.setForeground(TEXT_MID);

        JPanel bar = new JPanel();
        bar.setBackground(accent);
        bar.setPreferredSize(new Dimension(0, 3));
        bar.setBorder(new EmptyBorder(0, 0, 0, 0));

        JPanel texts = new JPanel();
        texts.setOpaque(false);
        texts.setLayout(new BoxLayout(texts, BoxLayout.Y_AXIS));
        texts.add(lTitle);
        texts.add(Box.createVerticalStrut(2));
        texts.add(lSub);

        p.add(texts, BorderLayout.CENTER);
        p.add(bar,   BorderLayout.SOUTH);
        return p;
    }

    /** Carte avec titre intégré et fond blanc arrondi */
    private JPanel buildCard(String title) {
        JPanel card = new JPanel(new BorderLayout(0, 8));
        card.setBackground(PANEL_BG);
        card.setBorder(new CompoundBorder(
            new LineBorder(BORDER_CLR, 1, true),
            new EmptyBorder(14, 14, 14, 14)));

        JLabel lbl = new JLabel(title);
        lbl.setFont(FONT_SECTION);
        lbl.setForeground(TEXT_DARK);
        lbl.setBorder(new EmptyBorder(0, 0, 6, 0));
        card.add(lbl, BorderLayout.NORTH);
        return card;
    }

    /** JTable avec style pastel cohérent */
    private JTable buildStyledTable(String[] columns) {
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        JTable table = new JTable(model);
        table.setFont(FONT_LABEL);
        table.setRowHeight(28);
        table.setShowHorizontalLines(true);
        table.setShowVerticalLines(false);
        table.setGridColor(BORDER_CLR);
        table.setBackground(PANEL_BG);
        table.setSelectionBackground(ACCENT_SOFT);
        table.setSelectionForeground(TEXT_DARK);
        table.setIntercellSpacing(new Dimension(12, 0));

        // En-tête
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 12));
        header.setBackground(new Color(0xF5F0EB));
        header.setForeground(TEXT_MID);
        header.setBorder(new MatteBorder(0, 0, 1, 0, BORDER_CLR));
        header.setPreferredSize(new Dimension(0, 34));

        // Lignes alternées
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object val,
                    boolean sel, boolean foc, int row, int col) {
                Component c = super.getTableCellRendererComponent(t, val, sel, foc, row, col);
                if (!sel) {
                    c.setBackground(row % 2 == 0 ? PANEL_BG : ROW_ALT);
                    c.setForeground(TEXT_DARK);
                }
                setBorder(new EmptyBorder(0, 8, 0, 8));
                return c;
            }
        });

        table.setAutoCreateRowSorter(true);
        return table;
    }

    private JScrollPane styledScroll(JTable table) {
        JScrollPane sp = new JScrollPane(table);
        sp.setBorder(new LineBorder(BORDER_CLR, 1));
        sp.getViewport().setBackground(PANEL_BG);
        return sp;
    }

    private void fitColumnToContent(JTable table, int colIndex, int maxWidth) {
        TableColumn column = table.getColumnModel().getColumn(colIndex);
        int width = 40;

        TableCellRenderer headerRenderer = table.getTableHeader().getDefaultRenderer();
        Component headerComp = headerRenderer.getTableCellRendererComponent(
            table, column.getHeaderValue(), false, false, 0, colIndex);
        width = Math.max(width, headerComp.getPreferredSize().width);

        for (int row = 0; row < table.getRowCount(); row++) {
            TableCellRenderer renderer = table.getCellRenderer(row, colIndex);
            Component comp = table.prepareRenderer(renderer, row, colIndex);
            width = Math.max(width, comp.getPreferredSize().width + 16);
        }

        if (maxWidth > 0 && width > maxWidth) {
            width = maxWidth;
        }
        column.setPreferredWidth(width);
    }

    private void addCardToColumn(JPanel column, JPanel card) {
        card.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
        column.add(card);
        column.add(Box.createVerticalStrut(12));
    }

    /** Bouton de rafraîchissement en bas de panneau */
    private JPanel buildRefreshFooter(String label, ActionListener action) {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        p.setOpaque(false);
        p.setBorder(new EmptyBorder(10, 0, 0, 0));
        JButton btn = buildIconButton("↻  " + label, ACCENT, Color.WHITE);
        btn.addActionListener(action);
        p.add(btn);
        return p;
    }

    /** Bouton stylisé */
    private JButton buildIconButton(String text, Color bg, Color fg) {
        JButton btn = new JButton(text) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setBorder(new EmptyBorder(8, 18, 8, 18));
        btn.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) {
                btn.setBackground(bg.darker());
            }
            @Override public void mouseExited(MouseEvent e) {
                btn.setBackground(bg);
            }
        });
        return btn;
    }

    /** Remplit une JTable depuis une liste de String[] */
    private void fillTable(JTable table, List<String[]> data) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        model.setRowCount(0);
        for (String[] row : data) model.addRow(row);
    }

    private void setStatus(String msg) {
        if (lblStatus != null) lblStatus.setText(msg);
    }

    private void showError(String context, Exception ex) {
        setStatus("Erreur : " + ex.getMessage());
        JOptionPane.showMessageDialog(this,
            context + " :\n" + ex.getMessage(),
            "Erreur", JOptionPane.ERROR_MESSAGE);
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  Chargement global
    // ══════════════════════════════════════════════════════════════════════════

    private void refreshAll() {
        loadMenu();
        loadLivraisons();
        loadStats();
        loadOrderData();
    }

    private static class ComboItem<T> {
        private final String label;
        private final T value;

        private ComboItem(String label, T value) {
            this.label = label;
            this.value = value;
        }

        public T getValue() { return value; }

        @Override
        public String toString() { return label; }
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  Point d'entrée (si lancé directement)
    // ══════════════════════════════════════════════════════════════════════════

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        SwingUtilities.invokeLater(() -> new MainFrame().setVisible(true));
    }
}