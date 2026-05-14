package ra.pizz.ui;

import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import ra.pizz.dao.*;
import ra.pizz.model.*;

public class MainFrame extends JFrame {
    private JTabbedPane tabbedPane;

    public MainFrame() {
        super("RaPizz - Gestion des Pizzas");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 600);
        setLocationRelativeTo(null);
        initUI();
    }

    private void initUI() {
        tabbedPane = new JTabbedPane();
        
        tabbedPane.addTab("Menu", createMenuPanel());
        tabbedPane.addTab("Fiche Livraison", createDeliveryPanel());
        tabbedPane.addTab("Statistiques", createStatsPanel());
        
        add(tabbedPane);
    }

    private JPanel createMenuPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JTable table = new JTable();
        
        JButton btnLoad = new JButton("Charger Menu");
        btnLoad.addActionListener(e -> loadMenu(table));
        
        JPanel btnPanel = new JPanel();
        btnPanel.add(btnLoad);
        
        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        panel.add(btnPanel, BorderLayout.SOUTH);
        
        return panel;
    }

    private JPanel createDeliveryPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JTable table = new JTable();
        
        JButton btnLoad = new JButton("Charger Fiches");
        btnLoad.addActionListener(e -> loadDeliveries(table));
        
        JPanel btnPanel = new JPanel();
        btnPanel.add(btnLoad);
        
        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        panel.add(btnPanel, BorderLayout.SOUTH);
        
        return panel;
    }

    private JPanel createStatsPanel() {
        JPanel panel = new JPanel(new GridLayout(4, 1, 5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Véhicules non utilisés
        JPanel p1 = new JPanel(new BorderLayout());
        p1.setBorder(BorderFactory.createTitledBorder("Véhicules non utilisés"));
        JTable t1 = new JTable();
        JButton b1 = new JButton("Charger");
        b1.addActionListener(e -> loadUnusedVehicles(t1));
        p1.add(new JScrollPane(t1), BorderLayout.CENTER);
        p1.add(b1, BorderLayout.SOUTH);
        panel.add(p1);
        
        // Commandes par client
        JPanel p2 = new JPanel(new BorderLayout());
        p2.setBorder(BorderFactory.createTitledBorder("Commandes par client"));
        JTable t2 = new JTable();
        JButton b2 = new JButton("Charger");
        b2.addActionListener(e -> loadOrdersPerClient(t2));
        p2.add(new JScrollPane(t2), BorderLayout.CENTER);
        p2.add(b2, BorderLayout.SOUTH);
        panel.add(p2);
        
        // Moyenne
        JPanel p3 = new JPanel();
        JLabel lblAvg = new JLabel("Moyenne: ");
        JButton b3 = new JButton("Calculer");
        b3.addActionListener(e -> calcAverage(lblAvg));
        p3.add(lblAvg);
        p3.add(b3);
        panel.add(p3);
        
        // Clients au-dessus moyenne
        JPanel p4 = new JPanel(new BorderLayout());
        p4.setBorder(BorderFactory.createTitledBorder("Clients > moyenne"));
        JTable t4 = new JTable();
        JButton b4 = new JButton("Charger");
        b4.addActionListener(e -> loadClientsAboveAvg(t4));
        p4.add(new JScrollPane(t4), BorderLayout.CENTER);
        p4.add(b4, BorderLayout.SOUTH);
        panel.add(p4);
        
        return panel;
    }

    private void loadMenu(JTable table) {
        try {
            PizzaMenuDAO dao = new JDBCPizzaMenuDAO();
            List<PizzaMenu> list = dao.findMenu();
            DefaultTableModel model = new DefaultTableModel(
                new String[]{"Pizza ID", "Pizza", "Prix Base", "Ingrédient", "Quantité"}, 0
            );
            for (PizzaMenu pm : list) {
                model.addRow(new Object[]{
                    pm.getIdPizza(), pm.getNomPizza(), pm.getPrixDeBase(), 
                    pm.getNomIngredient(), pm.getQuantite()
                });
            }
            table.setModel(model);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erreur: " + ex.getMessage());
        }
    }

    private void loadDeliveries(JTable table) {
        try {
            FicheLivraisonDAO dao = new JDBCFicheLivraisonDAO();
            List<FicheLivraison> list = dao.findAll();
            DefaultTableModel model = new DefaultTableModel(
                new String[]{"Livreur", "Véhicule", "Client", "Date", "Retard (min)", "Pizza", "Prix"}, 0
            );
            for (FicheLivraison fl : list) {
                model.addRow(new Object[]{
                    fl.getNomLivreur() + " " + fl.getPrenomLivreur(),
                    fl.getTypeVehicule(),
                    fl.getNomClient() + " " + fl.getPrenomClient(),
                    fl.getDate(),
                    fl.getRetard(),
                    fl.getNomPizza(),
                    fl.getPrixDeBase()
                });
            }
            table.setModel(model);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erreur: " + ex.getMessage());
        }
    }

    private void loadUnusedVehicles(JTable table) {
        try {
            StatisticsDAO dao = new StatisticsDAO();
            List<String[]> list = dao.getUnusedVehicles();
            DefaultTableModel model = new DefaultTableModel(new String[]{"Véhicule"}, 0);
            for (String[] row : list) {
                model.addRow(row);
            }
            table.setModel(model);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erreur: " + ex.getMessage());
        }
    }

    private void loadOrdersPerClient(JTable table) {
        try {
            StatisticsDAO dao = new StatisticsDAO();
            List<String[]> list = dao.getOrdersPerClient();
            DefaultTableModel model = new DefaultTableModel(new String[]{"Nom", "Prénom", "Commandes"}, 0);
            for (String[] row : list) {
                model.addRow(row);
            }
            table.setModel(model);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erreur: " + ex.getMessage());
        }
    }

    private void calcAverage(JLabel lbl) {
        try {
            StatisticsDAO dao = new StatisticsDAO();
            double avg = dao.getAverageOrders();
            lbl.setText(String.format("Moyenne: %.2f pizzas/client", avg));
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erreur: " + ex.getMessage());
        }
    }

    private void loadClientsAboveAvg(JTable table) {
        try {
            StatisticsDAO dao = new StatisticsDAO();
            List<String[]> list = dao.getClientsAboveAverage();
            DefaultTableModel model = new DefaultTableModel(new String[]{"Nom", "Prénom", "Commandes"}, 0);
            for (String[] row : list) {
                model.addRow(row);
            }
            table.setModel(model);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erreur: " + ex.getMessage());
        }
    }
}
