/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import DBAccess.DBCustomer;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Customer;
import utils.DBConnection;
import utils.DBQuery;

/**
 * FXML Controller class
 *
 * @author lisakim
 */
public class MainPageController implements Initializable {
    
    Stage stage;
    Parent scene;
    
    @FXML
    private TableView<Customer> customerTableView; 
    
    @FXML
    private TableColumn<Customer, Integer> customerIdCol;

    @FXML
    private TableColumn<Customer, String> customerNameCol;

    @FXML
    private TableColumn<Customer, String> customerAddressCol;
    
    @FXML
    private TableColumn<Customer, String> customerAddress2Col;

    @FXML
    private TableColumn<Customer, String> customerCityCol;

    @FXML
    private TableColumn<Customer, String> customerPhoneNumberCol;
    

    
    @FXML
    void onActionAddCustomer(ActionEvent event) throws IOException {
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/AddCustomerPage.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    @FXML
    void onActionUpdateCustomerf(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/view/UpdateCustomerPage.fxml"));
        loader.load();
        
        UpdateCustomerPageController customerController = loader.getController();
        if (customerTableView.getSelectionModel().getSelectedItem() != null){
            Customer sendingCustomer = customerTableView.getSelectionModel().getSelectedItem();
            
            customerController.sendCustomerData(sendingCustomer);
            
            stage = (Stage)((Button)event.getSource()).getScene().getWindow();
            Parent scene = loader.getRoot();
            stage.setScene(new Scene(scene));
            stage.show();
        }
    }
    

    
    @FXML
    void onActionDeleteCustomer(ActionEvent event) {
        Customer customer = customerTableView.getSelectionModel().getSelectedItem();
        
        if (customer == null) {
            return;
        }
        
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "This will delete "
                + "the selected customer.  Click okay to continue");
        Optional<ButtonType> result = alert.showAndWait();
        
        if (result.isPresent() && result.get().equals(ButtonType.OK)){
            DBCustomer.deleteCustomer(customer.getCustomerId(), customer.getAddressId());
        }
        
        customerTableView.setItems(DBCustomer.getAllCustomers());
        
    }



    @FXML
    void onActionViewAppointments(ActionEvent event) throws IOException {
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/ViewAppointments.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }
    
    @FXML
    void onActionExit(ActionEvent event) {
        DBConnection.closeConnection();
        System.exit(0);
    }

    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        customerTableView.setItems(DBCustomer.getAllCustomers());
        customerIdCol.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        customerNameCol.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        customerAddressCol.setCellValueFactory(new PropertyValueFactory<>("address"));
        customerAddress2Col.setCellValueFactory(new PropertyValueFactory<>("address2"));
        customerCityCol.setCellValueFactory(new PropertyValueFactory<>("city"));
        customerPhoneNumberCol.setCellValueFactory(new PropertyValueFactory<>("phone"));

    }    
    
}
