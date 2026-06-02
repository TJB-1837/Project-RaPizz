package rapizz.model;

public class Livreur {
    private int id;
    private String nom, prenom;

    public Livreur(int id, String nom, String prenom) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
    }

    public int getId() { return id; }
    public String getNom() { return nom; }
    public String getPrenom() { return prenom; }
}
