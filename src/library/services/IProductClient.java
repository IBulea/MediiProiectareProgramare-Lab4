package library.services;

public interface IProductClient {
  void productBorrowed(int productId, int newQuantity, boolean byThisUser) throws ProductException;
}
