package library.persistence.repository.book;

import library.model.Product;

import java.util.List;

/**
 * Created by Sergiu on 03.04.2016.
 */
public interface ProductRepository {
  List<Product> getAvailableProducts();
  List<Product> getUserProducts(int userId);
  List<Product> searchProducts(String key);
  int borrowProduct(int userId, int productId, int quantity);
}
