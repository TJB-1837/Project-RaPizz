package rapizz.model;

public class Ingredient {
    private int id;
    private String nom;

    public Ingredient(int id, String nom) {
        this.id = id;
        this.nom = nom;
    }

    public int getId() { return id; }
    public String getNom() { return nom; }
}
