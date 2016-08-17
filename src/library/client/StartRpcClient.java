package library.client;

import library.client.gui.ProductClientController;
import library.client.gui.LoginWindow;
import library.network.rpcprotocol.ProductServerRpcProxy;
import library.services.IProductServer;

public class StartRpcClient {
  public static void main(String[] args) {
    IProductServer server=new ProductServerRpcProxy("localhost", 55555);
    ProductClientController libraryClientController=new ProductClientController(server);

    LoginWindow loginWindow=new LoginWindow("Product", libraryClientController);
    loginWindow.setSize(200,200);
    loginWindow.setLocation(600,300);
    loginWindow.setVisible(true);
  }
}
