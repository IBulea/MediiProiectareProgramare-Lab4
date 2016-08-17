package library.client.gui;

import library.model.Product;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class ProductsTableModel extends AbstractTableModel {

  private static final long serialVersionUID = 1L;
  private List<Product> products;
  private String[] columns;

  public ProductsTableModel(boolean showAvailable) {
    this.products = new ArrayList<>();
    if (showAvailable) {
      columns = new String[]{"ID","Name", "Quantity", "Price"};
    } else {
      columns = new String[]{"ID","Name", "Quantity"};
    }
  }

  @Override
  public String getColumnName(int column) {
    return columns[column];
  }

  public int getRowCount() {
    return products.size();
  }

  public int getColumnCount() {
    return columns.length;
  }

  public Object getValueAt(int rowIndex, int columnIndex) {
    switch (columnIndex){
      case 0: return products.get(rowIndex).getId();
      case 1: return products.get(rowIndex).getName();
      case 2:
    	  //System.out.println("******\n\n\n\n*****" + products.get(rowIndex).getQuantity() + "*****\n\n\n****\n\n");
    	  return products.get(rowIndex).getQuantity();
      case 3: return products.get(rowIndex).getPrice();
    }
    return null;
  }

  public void setProducts(List<Product> products) {
    this.products = products;
    fireTableDataChanged();
  }

  public void addProduct(Product product) {
    this.products.add(product);
    fireTableDataChanged();
  }

  public Product get(int index) {
    return products.get(index);
  }

  public Product getById(int productId) {
    for (Product product : products) {
      if (product.getId() == productId) {
        return product;
      }
    }
    return null;
  }

  public void removeById(int productId) {
    Product toRemove = null;
    for (Product product : products) {
      if (product.getId() == productId) {
        toRemove = product;
        break;
      }
    }
    if (toRemove != null) {
      products.remove(toRemove);
    }
    fireTableDataChanged();
  }

}
