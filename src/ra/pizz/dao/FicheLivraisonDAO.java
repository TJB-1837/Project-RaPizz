package ra.pizz.dao;

import java.util.List;
import ra.pizz.model.*;

public interface FicheLivraisonDAO {
    List<FicheLivraison> findAll() throws Exception;
}
