package rapizz.model;

public class PizzaMenu {
    private int idPizza;
    private String nomPizza, nomIngredient;
    private double prixDeBase;
    private int quantite;

    public PizzaMenu(int idPizza, String nomPizza, double prixDeBase, String nomIngredient, int quantite) {
        this.idPizza = idPizza;
        this.nomPizza = nomPizza;
        this.prixDeBase = prixDeBase;
        this.nomIngredient = nomIngredient;
        this.quantite = quantite;
    }

    public int getIdPizza() { return idPizza; }
    public String getNomPizza() { return nomPizza; }
    public double getPrixDeBase() { return prixDeBase; }
    public String getNomIngredient() { return nomIngredient; }
    public int getQuantite() { return quantite; }
}
