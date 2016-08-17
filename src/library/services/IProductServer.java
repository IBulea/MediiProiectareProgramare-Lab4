package library.services;

import library.model.Product;
import library.model.User;

import java.util.List;

public interface IProductServer {
  User login(String userName, String password, IProductClient client) throws ProductException;
  void logout(int userId, IProductClient client) throws ProductException;
  List<Product> getAvailableProducts() throws ProductException;
  List<Product> getUserProducts(int userId) throws ProductException;
  List<Product> searchProducts(String key) throws ProductException;	
  void borrowProduct(int userId, int productId, int q) throws ProductException;
}
