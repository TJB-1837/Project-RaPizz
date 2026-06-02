package rapizz.ui;

import rapizz.dao.*;
import rapizz.model.*;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

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

    // ── Onglets ───────────────────────────────────────────────────────────────
    private JTabbedPane tabs;

    // ── Tables ────────────────────────────────────────────────────────────────
    private JTable tblMenu, tblLivraisons, tblVehicules,
                   tblOrdersPerClient, tblAboveAvg;
    private JLabel lblAvgOrders;
    private JLabel lblStatus;

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

                    // findMenu() retourne une ligne par ingrédient — on regroupe par idPizza
                    // LinkedHashMap pour conserver l'ordre de la requête SQL (ORDER BY id_pizza)
                    java.util.LinkedHashMap<Integer, Object[]> grouped = new java.util.LinkedHashMap<>();

                    for (PizzaMenu pm : items) {
                        int id = pm.getIdPizza();
                        if (!grouped.containsKey(id)) {
                            // Calcul des prix via la logique de Pizza (×2/3, ×1, ×4/3)
                            double base  = pm.getPrixDeBase();
                            double naine    = base * 2.0 / 3.0;
                            double ogresse  = base * 4.0 / 3.0;
                            grouped.put(id, new Object[]{
                                pm.getNomPizza(),
                                new StringBuilder(),   // ingrédients accumulés
                                String.format("%.2f €", naine),
                                String.format("%.2f €", base),
                                String.format("%.2f €", ogresse)
                            });
                        }
                        // Ajoute l'ingrédient dans le StringBuilder
                        StringBuilder sb = (StringBuilder) grouped.get(id)[1];
                        if (pm.getNomIngredient() != null) {
                            if (sb.length() > 0) sb.append(", ");
                            sb.append(pm.getNomIngredient());
                        }
                    }

                    // Ajoute une ligne par pizza avec les ingrédients concaténés
                    for (Object[] row : grouped.values()) {
                        row[1] = row[1].toString().isEmpty() ? "—" : row[1].toString();
                        model.addRow(row);
                    }

                    setStatus(grouped.size() + " pizza(s) chargée(s).");
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

        panel.add(buildSectionHeader("Statistiques",
                "Véhicules inutilisés, commandes par client, moyenne", SAND), BorderLayout.NORTH);

        // Zone centrale — deux colonnes
        JPanel center = new JPanel(new GridLayout(1, 2, 16, 0));
        center.setOpaque(false);

        // ── Colonne gauche ─────────────────────────────────────────────────
        JPanel left = new JPanel(new BorderLayout(0, 12));
        left.setOpaque(false);

        // Bloc véhicules
        JPanel vehiculesCard = buildCard("Véhicules n'ayant jamais servi");
        tblVehicules = buildStyledTable(new String[]{"Véhicule"});
        vehiculesCard.add(styledScroll(tblVehicules), BorderLayout.CENTER);
        left.add(vehiculesCard, BorderLayout.NORTH);

        // Bloc moyenne
        JPanel avgCard = buildCard("Moyenne de commandes par client");
        avgCard.setPreferredSize(new Dimension(0, 80));
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
        left.add(avgCard, BorderLayout.CENTER);

        center.add(left);

        // ── Colonne droite ─────────────────────────────────────────────────
        JPanel right = new JPanel(new BorderLayout(0, 12));
        right.setOpaque(false);

        // Commandes par client
        JPanel ordersCard = buildCard("Commandes par client");
        tblOrdersPerClient = buildStyledTable(new String[]{"Nom", "Prénom", "Commandes"});
        ordersCard.add(styledScroll(tblOrdersPerClient), BorderLayout.CENTER);
        right.add(ordersCard, BorderLayout.CENTER);

        // Clients > moyenne
        JPanel aboveCard = buildCard("Clients au-dessus de la moyenne");
        tblAboveAvg = buildStyledTable(new String[]{"Nom", "Prénom", "Commandes"});
        aboveCard.add(styledScroll(tblAboveAvg), BorderLayout.CENTER);
        right.add(aboveCard, BorderLayout.SOUTH);

        center.add(right);
        panel.add(center, BorderLayout.CENTER);

        JPanel footer = buildRefreshFooter("Rafraîchir les statistiques", e -> loadStats());
        panel.add(footer, BorderLayout.SOUTH);
        return panel;
    }

    private void loadStats() {
        setStatus("Calcul des statistiques…");

        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            List<String[]> unusedVehicles, ordersPerClient, aboveAvg;
            double avg;

            @Override
            protected Void doInBackground() throws Exception {
                unusedVehicles  = statsDAO.getUnusedVehicles();
                ordersPerClient = statsDAO.getOrdersPerClient();
                avg             = statsDAO.getAverageOrders();
                aboveAvg        = statsDAO.getClientsAboveAverage();
                return null;
            }

            @Override
            protected void done() {
                try {
                    get(); // re-throw exception si besoin

                    fillTable(tblVehicules,       unusedVehicles);
                    fillTable(tblOrdersPerClient, ordersPerClient);
                    fillTable(tblAboveAvg,        aboveAvg);
                    lblAvgOrders.setText(String.format("%.1f", avg));
                    setStatus("Statistiques mises à jour.");
                } catch (Exception ex) {
                    showError("Erreur chargement statistiques", ex);
                }
            }
        };
        worker.execute();
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