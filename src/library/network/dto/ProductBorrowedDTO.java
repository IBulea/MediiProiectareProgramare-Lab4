package library.network.dto;

import java.io.Serializable;

/**
 * Created by Sergiu on 04.04.2016.
 */
public class ProductBorrowedDTO implements Serializable {
  private int productId;
  private int newQuantity;
  private boolean byThisUser;

  public ProductBorrowedDTO(int productId, int newQuantity, boolean byThisUser) {
    this.productId = productId;
    this.newQuantity = newQuantity;
    this.byThisUser = byThisUser;
  }

  public int getProductId() {
    return productId;
  }

  public void setProductId(int productId) {
    this.productId = productId;
  }

  public int getNewQuantity() {
    return newQuantity;
  }

  public void setNewQuantity(int newQuantity) {
    this.newQuantity = newQuantity;
  }

  public boolean isByThisUser() {
    return byThisUser;
  }

  public void setByThisUser(boolean byThisUser) {
    this.byThisUser = byThisUser;
  }
}
