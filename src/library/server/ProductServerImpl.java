package library.server;

import library.model.Product;
import library.model.User;
import library.persistence.Persistence;
import library.persistence.repository.book.ProductRepository;
import library.persistence.repository.user.UserRepository;
import library.services.IProductClient;
import library.services.IProductServer;
import library.services.ProductException;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ProductServerImpl implements IProductServer {
  private UserRepository userRepository;
  private ProductRepository productRepository;
  private Map<Integer, IProductClient> loggedClients;

  public ProductServerImpl() {
    userRepository= Persistence.getInstance().createUserRepository();
    productRepository= Persistence.getInstance().createProductRepository();
    loggedClients=new ConcurrentHashMap<>();
  }

  public synchronized User login(String userName, String password, IProductClient client) throws ProductException {
    User user = userRepository.verifyUser(userName, password);
    if (user != null){
      if(loggedClients.get(user.getId()) != null)
        throw new ProductException("User already logged in.");
      loggedClients.put(user.getId(), client);
    } else
      throw new ProductException("Authentication failed.");
    return user;
  }

  public synchronized void logout(int userId, IProductClient client) throws ProductException {
    IProductClient localClient = loggedClients.remove(userId);
    if (localClient == null)
      throw new ProductException("User "+userId+" is not logged in.");
  }

  @Override
  public List<Product> getAvailableProducts() throws ProductException {
    return productRepository.getAvailableProducts();
  }

  @Override
  public List<Product> getUserProducts(int userId) throws ProductException {
    return productRepository.getUserProducts(userId);
  }

  @Override
  public List<Product> searchProducts(String key) throws ProductException {
    return productRepository.searchProducts(key);
  }

  @Override
  public void borrowProduct(int userId, int productId,int q) throws ProductException {
    int newQuantity = productRepository.borrowProduct(userId, productId,q);
    for (int keyUserId : loggedClients.keySet()) {
      if (keyUserId != userId) {
        loggedClients.get(keyUserId).productBorrowed(productId, newQuantity, false);
      } else {
        loggedClients.get(keyUserId).productBorrowed(productId, newQuantity, true);
      }

    }
  }
}
