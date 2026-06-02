package rapizz.dao;

import java.util.List;
import rapizz.model.*;

public interface PizzaMenuDAO {
    List<PizzaMenu> findMenu() throws Exception;
}
