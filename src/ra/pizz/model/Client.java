package ra.pizz.model;

public class Client {
    private int id;
    private String nom, prenom, telephone;
    private double solde;

    public Client(int id, String nom, String prenom, String telephone, double solde) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.telephone = telephone;
        this.solde = solde;
    }

    public int getId() { return id; }
    public String getNom() { return nom; }
    public String getPrenom() { return prenom; }
    public String getTelephone() { return telephone; }
    public double getSolde() { return solde; }
    public void setSolde(double solde) { this.solde = solde; }
}
