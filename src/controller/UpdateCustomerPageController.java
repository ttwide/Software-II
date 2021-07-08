/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import DBAccess.DBCity;
import DBAccess.DBCustomer;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.City;
import model.Customer;
import utils.DBConnection;

/**
 * FXML Controller class
 *
 * @author lisakim
 */
public class UpdateCustomerPageController implements Initializable {
    
    Stage stage;
    Parent scene;
    
    @FXML
    private TextField customerNameTF;

    @FXML
    private TextField addressTF;

    @FXML
    private TextField address2TF;

    @FXML
    private ComboBox<City> citiComboBox;

    @FXML
    private TextField phoneTF;
    
    private int customerId;
    private int addressId;

    
    @FXML
    void onCancelBTN(ActionEvent event) throws IOException {
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/MainPage.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }
    
   
    @FXML
    void onSubmitBTN(ActionEvent event) throws IOException {
        try {
            String customerName = customerNameTF.getText();
            String address = addressTF.getText();
            String address2 = address2TF.getText();
            int cityId = citiComboBox.getValue().getCityId();
            String phone = phoneTF.getText();
            if (customerName.trim().length() == 0 || address.trim().length() == 0 || 
                    address2.trim().length() == 0 || phone.trim().length()== 0){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Dialog");
                alert.setContentText("Please enter a valid value for each field.");
                alert.showAndWait();
                return;
            }
            if (phone.length() < 10){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Dialog");
                alert.setContentText("Phone number must a least be 10 characters long.");
                alert.showAndWait();
                return;
            }
            DBCustomer.updateCustomer(customerId, addressId, customerName, address, address2, cityId, phone);

            stage = (Stage)((Button)event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("/view/MainPage.fxml"));
            stage.setScene(new Scene(scene));
            stage.show();  
        } catch (NullPointerException e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setContentText("Please enter a valid value for each city.");
            alert.showAndWait();
        }
        
    }
    
    public void sendCustomerData(Customer customer){
        customerNameTF.setText(customer.getCustomerName());
        addressTF.setText(customer.getAddress());
        address2TF.setText(customer.getAddress2());
        phoneTF.setText(customer.getPhone());
        customerId = customer.getCustomerId();
        addressId = customer.getAddressId();
        
        int id = customer.getCityId();
        
        ObservableList<City> allCities = DBCity.getAllCities();
        
        // used lambda instead of enhanced for loop since more concise and easier to read
        ObservableList<City> selectedCity = allCities.filtered(c -> c.getCityId() == id);
        citiComboBox.setValue(selectedCity.get(0));
        
        
//        ObservableList<City> selectedCity = FXCollections.observableArrayList();
//        for (City city: DBCity.getAllCities()){
//            if (customer.getCityId() == city.getCityId()){
//                selectedCity.add(city);
//            }
//        }
//        citiComboBox.setValue(selectedCity.get(0));
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {     
       citiComboBox.setItems(DBCity.getAllCities());
    }    
    
}
