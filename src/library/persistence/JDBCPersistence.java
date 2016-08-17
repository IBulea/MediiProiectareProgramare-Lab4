package library.persistence;

import library.persistence.repository.book.ProductRepository;
import library.persistence.repository.book.jdbc.ProductRepositoryJDBC;
import library.persistence.repository.user.UserRepository;
import library.persistence.repository.user.jdbc.UserRepositoryJDBC;

public class JDBCPersistence extends Persistence {

  public UserRepository createUserRepository() {
    System.out.println("JDBC user repository created ");
    return new UserRepositoryJDBC();
  }

  @Override
  public ProductRepository createProductRepository() {
    System.out.println("JDBC product repository created ");
    return new ProductRepositoryJDBC();
  }
}
