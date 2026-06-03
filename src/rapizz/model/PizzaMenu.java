package rapizz.model;

import java.util.Map;

public class PizzaMenu {
    private int idPizza;
    private String nomPizza;
    private double prixDeBase;
    private Map<String, Integer> ingredients;

    public PizzaMenu(int idPizza, String nomPizza, double prixDeBase,
                     Map<String, Integer> ingredients) {
        this.idPizza = idPizza;
        this.nomPizza = nomPizza;
        this.prixDeBase = prixDeBase;
        this.ingredients = ingredients;
    }

    public int getIdPizza() { return idPizza; }
    public String getNomPizza() { return nomPizza; }
    public double getPrixDeBase() { return prixDeBase; }
    public Map<String, Integer> getIngredients() { return ingredients; }
}
