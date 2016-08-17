package library.persistence.repository.book.jdbc;

import library.model.Product;
import library.persistence.JDBCUtils;
import library.persistence.repository.book.ProductRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sergiu on 03.04.2016.
 */
public class ProductRepositoryJDBC implements ProductRepository {
  @Override
  public List<Product> getAvailableProducts() {
    System.out.println("Load available products");
    Connection connection = JDBCUtils.getConnection();
    List<Product> availableProducts = new ArrayList<>();
    try {
      PreparedStatement preparedStatement = connection.prepareStatement("select * from products where");
      ResultSet result=preparedStatement.executeQuery();
      while (result.next()) {
        availableProducts.add(new Product(result.getInt(1), result.getString(2), result.getInt(3), result.getInt(4)));
      }
    } catch (SQLException e) {
      System.out.println("Error DB "+e);
    }
    return availableProducts;
  }

  @Override
  public List<Product> getUserProducts(int userId) {
    System.out.println("Load user's products");
    Connection connection = JDBCUtils.getConnection();
    List<Product> usersProducts = new ArrayList<>();
    try {
      String sql = "select * from products b " +
          "inner join user_product ub on ub.product_id=b.id " +
          "inner join users u on u.id=ub.user_id " +
          "where u.id="+userId;
      PreparedStatement preparedStatement = connection.prepareStatement(sql);
      ResultSet result=preparedStatement.executeQuery();
      while (result.next()) {
        usersProducts.add(new Product(result.getInt(1), result.getString(2), result.getInt(3), result.getInt(4)));
      }
    } catch (SQLException e) {
      System.out.println("Error DB "+e);
    }
    return usersProducts;
  }

  @Override
  public List<Product> searchProducts(String key) {
    System.out.println("Search products");
    Connection connection = JDBCUtils.getConnection();
    List<Product> foundProducts = new ArrayList<>();
    try {
      PreparedStatement preparedStatement = connection.prepareStatement("select * from products where name like '%"+key+"%'");
      ResultSet result=preparedStatement.executeQuery();
      while (result.next()) {
        foundProducts.add(new Product(result.getInt(1), result.getString(2), result.getInt(3), result.getInt(4)));
      }
    } catch (SQLException e) {
      System.out.println("Error DB "+e);
    }
    return foundProducts;
  }

  @Override
  public int borrowProduct(int userId, int productId,int q) {
    System.out.println("Borrow product");
    Connection connection = JDBCUtils.getConnection();
    int quantity = 0;
    try {
      String changeQuantity = "UPDATE `products` SET `quantity`=`quantity`-? where id=?";
      PreparedStatement changeQuantityStatement = connection.prepareStatement(changeQuantity);
      changeQuantityStatement.setInt(1, q);
      changeQuantityStatement.setInt(2, productId);
      changeQuantityStatement.executeUpdate();
      String borrow = "insert into user_product values(?,?)";
      PreparedStatement borrowStatement = connection.prepareStatement(borrow);
      borrowStatement.setInt(1, userId);
      borrowStatement.setInt(2, productId);
      borrowStatement.executeUpdate();
      PreparedStatement availableQuantityStatement = connection.prepareStatement("select quantity from products where id=?");
      availableQuantityStatement.setInt(1, productId);
      ResultSet resultSet = availableQuantityStatement.executeQuery();
      if (resultSet.next()) {
        quantity = resultSet.getInt(1);
      }
    } catch (SQLException e) {
      System.out.println("Error DB "+e);
    }
    return quantity;
  }
}
