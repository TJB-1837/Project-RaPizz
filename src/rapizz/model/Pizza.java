package rapizz.model;

public class Pizza {
    private int id;
    private String nom;
    private double prixDeBase;

    public Pizza(int id, String nom, double prixDeBase) {
        this.id = id;
        this.nom = nom;
        this.prixDeBase = prixDeBase;
    }

    public int getId() { return id; }
    public String getNom() { return nom; }
    public double getPrixDeBase() { return prixDeBase; }
    
    public double getPrix(String taille) {
        switch(taille) {
            case "naine": return prixDeBase * 2 / 3;
            case "ogresse": return prixDeBase * 4 / 3;
            default: return prixDeBase;
        }
    }
}
