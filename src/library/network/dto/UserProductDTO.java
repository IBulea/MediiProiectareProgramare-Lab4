package library.network.dto;

import java.io.Serializable;


public class UserProductDTO implements Serializable {
  private int userId;
  private int productId;
  private int quantity;
  public UserProductDTO(int userId, int productId,int q) {
    this.userId = userId;
    this.productId = productId;
    this.quantity=q;
  }

  public int getUserId() {
    return userId;
  }

  public void setUserId(int userId) {
    this.userId = userId;
  }

  public int getProductId() {
    return productId;
  }

  public void setProductId(int productId) {
    this.productId = productId;
  }
  public int getQuantity() {
	    return quantity;
	  }

  public void setPrQuantity(int productId) {
	    this.quantity = productId;
	  }
}
