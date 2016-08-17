package library.network.utils;

import library.network.rpcprotocol.ProductClientRpcWorker;
import library.services.IProductServer;

import java.net.Socket;

/**
 * Created by grigo on 2/25/16.
 */
public class ProductRpcConcurrentServer extends AbsConcurrentServer {

  private IProductServer chatServer;

  public ProductRpcConcurrentServer(int port, IProductServer chatServer) {
    super(port);
    this.chatServer = chatServer;
    System.out.println("Chat- ProductRpcConcurrentServer");
  }

  @Override
  protected Thread createWorker(Socket client) {
    ProductClientRpcWorker worker=new ProductClientRpcWorker(chatServer, client);
    Thread tw=new Thread(worker);
    return tw;
  }
}
