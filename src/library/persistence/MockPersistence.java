package library.persistence;

import library.persistence.repository.book.ProductRepository;
import library.persistence.repository.book.mock.ProductRepositoryMock;
import library.persistence.repository.user.UserRepository;
import library.persistence.repository.user.mock.UserRepositoryMock;

public class MockPersistence extends Persistence {

  public UserRepository createUserRepository() {
    System.out.println("Mock user repository created ");
    return new UserRepositoryMock();
  }

  @Override
  public ProductRepository createProductRepository() {
    System.out.println("Mock book repository created ");
    return new ProductRepositoryMock();
  }
}
