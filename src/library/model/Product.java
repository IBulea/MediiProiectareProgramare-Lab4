package library.model;

import java.io.Serializable;

public class Product implements Serializable {

  private String name;
  private int id,quantity, price;

  public Product(int id, String name, int quantity, int price) {
    this.id = id;
    this.name = name;
    this.quantity = quantity;
    this.price = price;
  }

  public String getName() {
    return name;
  }

  public int getQuantity() {
    return quantity;
  }
  public void setQuantity(int q) {
	    this.quantity=q;
	  }

  public int getId() {
    return id;
  }

  public int getPrice() {
    return price;
  }

  @Override
  public String toString() {
    return "Book [name=" + name + ", quantity=" + quantity + ", id=" + id
        + ", price=" + price + "]";
  }
}
