package library.persistence.repository.book.mock;

import library.model.Product;
import library.persistence.repository.book.ProductRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sergiu on 03.04.2016.
 */
public class ProductRepositoryMock implements ProductRepository {

  private List<Product> allProducts;

  public ProductRepositoryMock() {
    allProducts = new ArrayList<>();
    populateProducts();
  }

  private void populateProducts() {
    Product product1 = new Product(1,"Cosmos",250,2);
    Product product2 = new Product(2,"Orange",900,1);
    Product product3 = new Product(3,"Ham",800,1);
    Product product4 = new Product(4,"Picture",700,4);
    Product product5 = new Product(5,"Mal",100,2);

    allProducts.add(product1);
    allProducts.add(product2);
    allProducts.add(product3);
    allProducts.add(product4);
    allProducts.add(product5);
  }

  @Override
  public List<Product> getAvailableProducts() {
    return allProducts;
  }

  @Override
  public List<Product> getUserProducts(int userId) {
    List<Product> userProducts = new ArrayList<>();
    userProducts.add(allProducts.get(1));
    userProducts.add(allProducts.get(3));
    return userProducts;
  }

  @Override
  public List<Product> searchProducts(String key) {
    return null;
  }

  @Override
  public int borrowProduct(int userId, int productId,int q) {
    return 0;
  }
}
