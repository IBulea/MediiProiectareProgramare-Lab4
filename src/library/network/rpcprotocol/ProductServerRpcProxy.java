package library.network.rpcprotocol;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import library.model.Product;
import library.model.User;
import library.network.dto.ProductBorrowedDTO;
import library.network.dto.UserProductDTO;
import library.network.dto.UserDTO;
import library.network.json.ResponseDeserializer;
import library.services.IProductClient;
import library.services.IProductServer;
import library.services.ProductException;

import java.io.*;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ProductServerRpcProxy implements IProductServer {
  private String host;
  private int port;

  private IProductClient client;

  private BufferedReader input;
  private PrintWriter output;
  private Socket connection;

  private BlockingQueue<Response> responseBlockingQueue;
  private volatile boolean finished;

  public ProductServerRpcProxy(String host, int port) {
    this.host = host;
    this.port = port;
    this.responseBlockingQueue = new LinkedBlockingQueue<>();
  }

  public User login(String userName, String password, IProductClient client) throws ProductException {
    initializeConnection();
    UserDTO userDTO = new UserDTO(userName, password);
    Request request = new Request.Builder().type(RequestType.LOGIN).data(userDTO).build();
    sendRequest(request);
    Response response = readResponse();
    if (response.type() == ResponseType.ERROR){
      String errorMessage = response.data().toString();
      closeConnection();
      throw new ProductException(errorMessage);
    }
    User user = (User)response.data();
    if (user != null && response.type() == ResponseType.LOGIN_SUCCESSFULLY) {
      this.client=client;
    }
    return user;
  }

  public void logout(int userId, IProductClient client) throws ProductException {
    Request request = new Request.Builder().type(RequestType.LOGOUT).data(userId).build();
    sendRequest(request);
    Response response = readResponse();
    closeConnection();
    if (response.type() == ResponseType.ERROR){
      String errorMessage = response.data().toString();
      throw new ProductException(errorMessage);
    }
  }

  @Override
  public List<Product> getAvailableProducts() throws ProductException {
    Request request = new Request.Builder().type(RequestType.GET_AVAILABLE_PRODUCTS).build();
    sendRequest(request);
    Response response = readResponse();
    if (response.type()==ResponseType.ERROR){
      String errorMessage = response.data().toString();
      closeConnection();
      throw new ProductException(errorMessage);
    }
    List<Product> allProducts = (List<Product>)response.data();
    return allProducts;
  }

  @Override
  public List<Product> getUserProducts(int userId) throws ProductException {
    Request request = new Request.Builder().type(RequestType.GET_USER_PRODUCTS).data(userId).build();
    sendRequest(request);
    Response response = readResponse();
    if (response.type() == ResponseType.ERROR){
      String errorMessage = response.data().toString();
      closeConnection();
      throw new ProductException(errorMessage);
    }
    return (List<Product>)response.data();
  }

  @Override
  public List<Product> searchProducts(String key) throws ProductException {
    Request request = new Request.Builder().type(RequestType.SEARCH_PRODUCTS).data(key).build();
    sendRequest(request);
    Response response = readResponse();
    if (response.type() == ResponseType.ERROR){
      String errorMessage = response.data().toString();
      closeConnection();
      throw new ProductException(errorMessage);
    }
    return (List<Product>)response.data();
  }

  @Override
  public void borrowProduct(int userId, int productId,int q) throws ProductException {
    UserProductDTO userProductDTO = new UserProductDTO(userId, productId,q);
    Request request = new Request.Builder().type(RequestType.BUY_PRODUCT).data(userProductDTO).build();
    sendRequest(request);
    Response response = readResponse();
    if (response.type() == ResponseType.ERROR){
      String errorMessage = response.data().toString();
      throw new ProductException(errorMessage);
    }
  }


  private void closeConnection() {
    finished=true;
    try {
      input.close();
      output.close();
      connection.close();
      client=null;
    } catch (IOException e) {
      e.printStackTrace();
    }

  }

  private void sendRequest(Request request)throws ProductException {
      Gson gson = new Gson();
      String json = gson.toJson(request);
      System.out.println("Request json " + json);
      output.println(json);
  }

  private Response readResponse() throws ProductException {
    Response response=null;
    try{
      response = responseBlockingQueue.take();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    return response;
  }
  private void initializeConnection() throws ProductException {
    try {
      connection=new Socket(host,port);
      output = new PrintWriter(connection.getOutputStream(), true);
      output.flush();
      input = new BufferedReader(new InputStreamReader(connection.getInputStream()));
      finished = false;
      startReader();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void startReader() {
    Thread readerThread = new Thread(new ReaderThread());
    readerThread.start();
  }


  private void handleUpdate(Response response){
    if (response.type() == ResponseType.BUY_PRODUCT) {
      try {
        ProductBorrowedDTO productBorrowedDTO = (ProductBorrowedDTO)response.data();
        client.productBorrowed(productBorrowedDTO.getProductId(), productBorrowedDTO.getNewQuantity(), productBorrowedDTO.isByThisUser());
      } catch (ProductException exception) {

      }
    }
  }

  private boolean isUpdate(Response response){
    return response.type() == ResponseType.BUY_PRODUCT;
  }

  private class ReaderThread implements Runnable {
    public void run() {
      while(!finished){
        try {
          String responseJson = input.readLine();
          if (responseJson != null) {
            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.registerTypeAdapter(Response.class, new ResponseDeserializer());
            Gson gson = gsonBuilder.create();
            Response response = gson.fromJson(responseJson, Response.class);
            System.out.println("Response received " + response);
            if (isUpdate(response)) {
              handleUpdate(response);
            } else {
              try {
                responseBlockingQueue.put(response);
              } catch (InterruptedException e) {
                e.printStackTrace();
              }
            }
          }
        } catch (IOException e) {
          System.out.println("Reading error "+e);
        }
      }
    }
  }
}
