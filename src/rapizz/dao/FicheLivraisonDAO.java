package rapizz.dao;

import java.util.List;
import rapizz.model.*;

public interface FicheLivraisonDAO {
    List<FicheLivraison> findAll() throws Exception;
}
