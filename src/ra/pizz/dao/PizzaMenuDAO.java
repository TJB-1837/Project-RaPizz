package ra.pizz.dao;

import java.util.List;
import ra.pizz.model.*;

public interface PizzaMenuDAO {
    List<PizzaMenu> findMenu() throws Exception;
}
