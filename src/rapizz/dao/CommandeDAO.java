package rapizz.dao;

import java.util.List;
import rapizz.model.Client;
import rapizz.model.Livreur;
import rapizz.model.Pizza;
import rapizz.model.Vehicule;

public interface CommandeDAO {
    List<Client> getClients() throws Exception;
    List<Pizza> getPizzas() throws Exception;
    List<Livreur> getLivreurs() throws Exception;
    List<Vehicule> getVehicules() throws Exception;

    void createCommande(int idClient, int idPizza, int idLivreur, int idVehicule,
                        double taille) throws Exception;
}
