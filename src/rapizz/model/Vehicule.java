package rapizz.model;

public class Vehicule {
    private int id;
    private String nom, type;

    public Vehicule(int id, String nom, String type) {
        this.id = id;
        this.nom = nom;
        this.type = type;
    }

    public int getId() { return id; }
    public String getNom() { return nom; }
    public String getType() { return type; }
}
