package library.network.rpcprotocol;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import library.model.Product;
import library.model.User;
import library.network.dto.ProductBorrowedDTO;
import library.network.dto.UserProductDTO;
import library.network.dto.UserDTO;
import library.network.json.RequestDeserializer;
import library.services.IProductClient;
import library.services.IProductServer;
import library.services.ProductException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;

/**
 * Created by grigo on 12/15/15.
 */
public class ProductClientRpcWorker implements Runnable, IProductClient {
  private IProductServer server;
  private Socket connection;

  private BufferedReader input;
  private PrintWriter output;
  private volatile boolean connected;

  public ProductClientRpcWorker(IProductServer server, Socket connection) {
    this.server = server;
    this.connection = connection;
    try{
      output = new PrintWriter(connection.getOutputStream(), true);
      output.flush();
      input = new BufferedReader(new InputStreamReader(connection.getInputStream()));
      connected = true;
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void run() {
    while(connected){
      try {
        String requestJson = input.readLine();
        if (requestJson != null) {
          GsonBuilder gsonBuilder = new GsonBuilder();
          gsonBuilder.registerTypeAdapter(Request.class, new RequestDeserializer());
          Gson gson = gsonBuilder.create();
          Request request = gson.fromJson(requestJson, Request.class);
          System.out.println("Request received " + request);
          Response response = handleRequest(request);
          if (response != null) {
            sendResponse(response);
          }
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
      try {
        Thread.sleep(1000);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
    try {
      input.close();
      output.close();
      connection.close();
    } catch (IOException e) {
      System.out.println("Error "+e);
    }
  }

  @Override
  public void productBorrowed(int productId, int newQuantity, boolean byThisUser) throws ProductException {
    ProductBorrowedDTO productBorrowedDTO = new ProductBorrowedDTO(productId, newQuantity, byThisUser);
    Response response = new Response.Builder().type(ResponseType.BUY_PRODUCT).data(productBorrowedDTO).build();
    try {
      sendResponse(response);
    } catch (IOException e) {
      throw new ProductException("Sending error: "+e);
    }
  }

  private Response handleRequest(Request request){
    Response response = null;
    if (request.type() == RequestType.LOGIN){
      System.out.println("Login request ...");
      UserDTO userDTO=(UserDTO)request.data();
      try {
        User user = server.login(userDTO.getUserName(), userDTO.getPassword(), this);
        return new Response.Builder().type(ResponseType.LOGIN_SUCCESSFULLY).data(user).build();
      } catch (ProductException e) {
        connected = false;
        return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
      }
    }
    if (request.type() == RequestType.LOGOUT){
      System.out.println("Logout request");
      int userId=(int)request.data();
      try {
        server.logout(userId, this);
        connected = false;
        return new Response.Builder().type(ResponseType.LOGOUT_SUCCESSFULLY).build();
      } catch (ProductException e) {
        return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
      }
    }
    if (request.type() == RequestType.GET_AVAILABLE_PRODUCTS) {
      try {
        List<Product> allProducts = server.getAvailableProducts();
        return new Response.Builder().type(ResponseType.GET_AVAILABLE_PRODUCTS).data(allProducts).build();
      } catch (ProductException e) {
        return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
      }
    }
    if (request.type() == RequestType.GET_USER_PRODUCTS) {
      try {
        int userId = (int)request.data();
        List<Product> userProducts = server.getUserProducts(userId);
        return new Response.Builder().type(ResponseType.GET_USER_PRODUCTS).data(userProducts).build();
      } catch (ProductException e) {
        return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
      }
    }
    if (request.type() == RequestType.SEARCH_PRODUCTS) {
      try {
        String key = (String)request.data();
        List<Product> foundProducts = server.searchProducts(key);
        return new Response.Builder().type(ResponseType.SEARCH_PRODUCTS).data(foundProducts).build();
      } catch (ProductException e) {
        return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
      }
    }
    if (request.type() == RequestType.BUY_PRODUCT) {
      try {
        UserProductDTO userProductDTO = (UserProductDTO)request.data();
        server.borrowProduct(userProductDTO.getUserId(), userProductDTO.getProductId(),userProductDTO.getQuantity());
        return new Response.Builder().type(ResponseType.OK).build();
      } catch (ProductException e) {
        return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
      }
    }
    return response;
  }

  private void sendResponse(Response response) throws IOException{
    Gson gson = new Gson();
    String json = gson.toJson(response);
    System.out.println("Response json "+json);
    output.println(json);
  }
}
