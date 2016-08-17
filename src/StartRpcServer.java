import library.network.utils.AbstractServer;
import library.network.utils.ProductRpcConcurrentServer;
import library.network.utils.ServerException;
import library.server.ProductServerImpl;
import library.services.IProductServer;

import org.w3c.dom.*;

import javax.xml.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class StartRpcServer {
  public static void main(String[] args) {
    try {
      Properties serverProps = new Properties(System.getProperties());

      DocumentBuilderFactory factory =
          DocumentBuilderFactory.newInstance();
      DocumentBuilder builder = factory.newDocumentBuilder();
      InputStream inputStream = new FileInputStream("config.xml");
      Document document = builder.parse(inputStream);
      document.getDocumentElement().normalize();
      NodeList nodeList = document.getElementsByTagName("config");
      Node node = nodeList.item(0);
      Element element = (Element) node;
      NodeList childNodes = element.getChildNodes();
      for (int i = 0; i < childNodes.getLength(); i++) {
        if (childNodes.item(i).getNodeType() == Node.ELEMENT_NODE) {
          Element childElement = (Element) childNodes.item(i);
          String tagName = childElement.getTagName();
          String tagValue = childElement.getChildNodes().item(0).getTextContent();
          serverProps.setProperty(tagName, tagValue);
        }
      }

      System.setProperties(serverProps);
    } catch (Exception e) {
      ((Throwable) e).printStackTrace();
    }

    IProductServer libraryServer=new ProductServerImpl();
    AbstractServer server=new ProductRpcConcurrentServer(55555, libraryServer);
    try {
      server.start();
    } catch (ServerException e) {
      System.out.println(e.getMessage());
    }
  }
}
