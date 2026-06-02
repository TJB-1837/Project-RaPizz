package rapizz.model;

import java.time.LocalDateTime;

public class Livraison {
    private int id, temps, idVehicule, idLivreur, idPizza, idClient;
    private LocalDateTime date;
    private double prixFacture, taille;
    private boolean estGratuite;

    public Livraison(int id, LocalDateTime date, int temps, double prixFacture, 
                     boolean estGratuite, double taille, int idVehicule, int idLivreur, 
                     int idPizza, int idClient) {
        this.id = id;
        this.date = date;
        this.temps = temps;
        this.prixFacture = prixFacture;
        this.estGratuite = estGratuite;
        this.taille = taille;
        this.idVehicule = idVehicule;
        this.idLivreur = idLivreur;
        this.idPizza = idPizza;
        this.idClient = idClient;
    }

    public int getId() { return id; }
    public LocalDateTime getDate() { return date; }
    public int getTemps() { return temps; }
    public double getPrixFacture() { return prixFacture; }
    public boolean isEstGratuite() { return estGratuite; }
    public double getTaille() { return taille; }
    public int getIdVehicule() { return idVehicule; }
    public int getIdLivreur() { return idLivreur; }
    public int getIdPizza() { return idPizza; }
    public int getIdClient() { return idClient; }
}
