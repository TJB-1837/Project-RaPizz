package ra.pizz.model;

import java.time.LocalDateTime;

public class FicheLivraison {
    private String nomLivreur, prenomLivreur, typeVehicule, nomClient, prenomClient;
    private String nomPizza;
    private LocalDateTime date;
    private int retard;
    private double prixFacture;

    public FicheLivraison(String nomLivreur, String prenomLivreur, String typeVehicule, 
                          String nomClient, String prenomClient, String nomPizza, 
                          LocalDateTime date, int retard, double prixFacture) {
        this.nomLivreur = nomLivreur;
        this.prenomLivreur = prenomLivreur;
        this.typeVehicule = typeVehicule;
        this.nomClient = nomClient;
        this.prenomClient = prenomClient;
        this.nomPizza = nomPizza;
        this.date = date;
        this.retard = retard;
        this.prixFacture = prixFacture;
    }

    public String getNomLivreur() { return nomLivreur; }
    public String getPrenomLivreur() { return prenomLivreur; }
    public String getTypeVehicule() { return typeVehicule; }
    public String getNomClient() { return nomClient; }
    public String getPrenomClient() { return prenomClient; }
    public String getNomPizza() { return nomPizza; }
    public LocalDateTime getDate() { return date; }
    public int getRetard() { return retard; }
    public double getPrixFacture() { return prixFacture; }
}
