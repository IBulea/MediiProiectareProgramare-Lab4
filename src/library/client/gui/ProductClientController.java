package library.client.gui;

import library.model.Product;
import library.model.User;
import library.services.IProductClient;
import library.services.IProductServer;
import library.services.ProductException;

import javax.swing.table.TableModel;
import java.util.List;

public class ProductClientController implements IProductClient {

  private ProductsTableModel availableProductsTableModel;
  private ProductsTableModel yourProductsTableModel;
  private IProductServer libraryServer;
  private User user;

  public ProductClientController(IProductServer libraryServer) {
    this.libraryServer = libraryServer;
    availableProductsTableModel = new ProductsTableModel(true);
    yourProductsTableModel = new ProductsTableModel(false);
  }

  public void login(String userName, String password) throws ProductException {
    User user = libraryServer.login(userName, password, this);
    this.user = user;
    loadAvailableProducts();
    List<Product> userProducts = libraryServer.getUserProducts(user.getId());
    yourProductsTableModel.setProducts(userProducts);
  }

  public void logout() {
    try {
      libraryServer.logout(user.getId(), this);
    } catch (ProductException e) {
      System.out.println("Logout error "+e);
    }
  }

  public TableModel getAvailableProductsTableModel(){
    return availableProductsTableModel;
  }

  public void loadAvailableProducts() throws ProductException {
    List<Product> availableProducts = libraryServer.getAvailableProducts();
    availableProductsTableModel.setProducts(availableProducts);
  }

  public ProductsTableModel getYourProductsTableModel() {
    return yourProductsTableModel;
  }

  public void borrowProduct(int productId,int quantity) throws ProductException {
    libraryServer.borrowProduct(user.getId(), productId,quantity);
  }

  public void searchProducts(String key) throws ProductException {
    List<Product> foundProducts = libraryServer.searchProducts(key);
    availableProductsTableModel.setProducts(foundProducts);
  }

  @Override
  public void productBorrowed(int productId, int newQuantity, boolean byThisUser) throws ProductException {
    if (byThisUser) {
      Product borrowed = availableProductsTableModel.getById(productId);
      yourProductsTableModel.addProduct(borrowed);
    }
    if (newQuantity == 0) {
      availableProductsTableModel.removeById(productId);
    } else {
      availableProductsTableModel.getById(productId).setQuantity(newQuantity);
      availableProductsTableModel.fireTableDataChanged();
    }
  }
}
