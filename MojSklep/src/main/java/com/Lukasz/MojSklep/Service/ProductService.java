package com.Lukasz.MojSklep.Service;
import com.Lukasz.MojSklep.Model.Product;
import org.springframework.stereotype.Service;
import java.util.Map;

@Service
public class ProductService {
    private final Map<String, Product> productCatalog = Map.of(
            "zeszyt", new Product("Zeszyt", "https://cdn.pixabay.com/photo/2017/04/04/07/06/address-book-2200584_1280.png", "Pomieści całą wiedzę na maturę", 5.99),
            "olowek", new Product("Ołówek", "https://cdn.pixabay.com/photo/2014/04/02/10/47/pencil-304559_1280.png", "Narysujesz nim co tylko zechcesz", 1.49),
            "dlugopis", new Product("Długopis", "https://cdn.pixabay.com/photo/2012/04/13/18/44/pen-33237_1280.png", "Idealny kandydat na starter pack do szkoły", 2.99)
    );

    public Map<String, Product> getAllProducts() {
        return productCatalog;
    }

    public Product getProductById(String id) {
        return productCatalog.get(id);
    }
}
