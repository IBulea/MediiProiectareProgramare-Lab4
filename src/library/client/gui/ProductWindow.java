package library.client.gui;

import library.model.Product;
import library.services.ProductException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class ProductWindow extends JFrame{
  private JTable availableProducts;
  private JTable yourProducts;
  private TextField searchTextField;
  private ProductClientController libraryClientController;
  private JTextField textField;

  public ProductWindow(String title, ProductClientController libraryClientController){
    super(title);
    this.libraryClientController = libraryClientController;
    JPanel panel=new JPanel(new GridLayout(1,2));
    panel.add(createAllProducts());
    getContentPane().add(panel);
    addWindowListener(new WindowAdapter(){
      public void windowClosing(WindowEvent e){
        close();
      }
    });
  }

  private void close(){
    libraryClientController.logout();
  }

  private JPanel createAllProducts(){
    JPanel jPanel=new JPanel(new BorderLayout());
    jPanel.setBorder(BorderFactory.createTitledBorder("Available products"));

    availableProducts=new JTable(libraryClientController.getAvailableProductsTableModel());
    JScrollPane scroll=new JScrollPane(availableProducts);
    jPanel.add(scroll, BorderLayout.CENTER);

    JPanel southPanel = new JPanel(new GridLayout(2,1));
    jPanel.add(southPanel, BorderLayout.SOUTH);

    JPanel searchPanel = new JPanel(new GridLayout(1,3));
    southPanel.add(searchPanel);

    searchTextField = new TextField();
    searchPanel.add(searchTextField);

    Button searchButton = new Button("Search");
    searchButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        try {
          libraryClientController.searchProducts(searchTextField.getText());
        } catch (ProductException exception) {
          JOptionPane.showMessageDialog(ProductWindow.this, exception.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
        }
      }
    });
    searchPanel.add(searchButton);

    Button clearButton = new Button("Clear");
    clearButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        try {
          clearSearch();
        } catch (ProductException exception) {
          JOptionPane.showMessageDialog(ProductWindow.this, exception.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
        }
      }
    });
    searchPanel.add(clearButton);

    Button borrowButton = new Button("Buy product");
    borrowButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        int row = availableProducts.getSelectedRow();
        if (row >= 0) {
            ProductsTableModel availableProductsTableModel = (ProductsTableModel) availableProducts.getModel();
            Product selectedProduct = availableProductsTableModel.get(availableProducts.convertRowIndexToModel(row));
            //ProductsTableModel yourProductsTableModel = (ProductsTableModel) yourProducts.getModel();
            //if (yourProductsTableModel.getById(selectedProduct.getId()) != null) {
            //  JOptionPane.showMessageDialog(ProductWindow.this, "You already have this product!", "ERROR", JOptionPane.ERROR_MESSAGE);
           // } else {
              try {
                libraryClientController.borrowProduct(selectedProduct.getId(),Integer.parseInt(textField.getText()));
                clearSearch();
              } catch (ProductException exception) {
                JOptionPane.showMessageDialog(ProductWindow.this, exception.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
              }
            
          }
      }
    });
    southPanel.add(borrowButton);
    
    textField = new JTextField();
    southPanel.add(textField);
    textField.setColumns(10);

    return jPanel;
  }

  private void clearSearch() throws ProductException {
    searchTextField.setText("");
    libraryClientController.loadAvailableProducts();
  }

}
