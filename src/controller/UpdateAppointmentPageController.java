/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import DBAccess.DBAppointment;
import DBAccess.DBCustomer;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Appointment;
import model.Customer;

/**
 * FXML Controller class
 *
 * @author lisakim
 */
public class UpdateAppointmentPageController implements Initializable {
    
    Stage stage;
    Parent scene;
    
    @FXML
    private TextField startDateTF;

    @FXML
    private TextField startTimeTF;

    @FXML
    private TextField endTimeTF;

    @FXML
    private ComboBox<Customer> customerNameComboBox;

    @FXML
    private TextField typeTF;
    
    private int appointmentId;
    

    @FXML
    void onActionCancelBtn(ActionEvent event) throws IOException {
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/ViewAppointments.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    @FXML
    void onActionSubmitBtn(ActionEvent event) throws IOException {
        
        int customerId = customerNameComboBox.getValue().getCustomerId();
        String type = typeTF.getText();
        String startDate = startDateTF.getText();
        String startTime = startTimeTF.getText();
        String endDate = startDate;
        String endTime = endTimeTF.getText();
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime start = LocalDateTime.parse(startDate + " " + startTime, formatter);
        LocalDateTime end = LocalDateTime.parse(endDate + " " + endTime, formatter);
        
        DBAppointment.updateAppointment(customerId, type, start, end, appointmentId);
        
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/ViewAppointments.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }
    
    public void sendAppointmentData(Appointment appointment){
                
        appointmentId = appointment.getAppointmentId();
        typeTF.setText(appointment.getType());
        startDateTF.setText(appointment.getStartTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        startTimeTF.setText(appointment.getStartTime().format(DateTimeFormatter.ofPattern("HH:mm")));
        endTimeTF.setText(appointment.getEndTime().format(DateTimeFormatter.ofPattern("HH:mm")));
        
        int id = appointment.getCustomerId();
        
        ObservableList<Customer> allCustomers = DBCustomer.getAllCustomers();
        
        // used lambda instead of enhanced for loop since more concise and easier to read
        ObservableList<Customer> returnedCustomer = allCustomers.filtered(c ->
                c.getCustomerId() == id);
        
        customerNameComboBox.setValue(returnedCustomer.get(0));
        
        
//        ObservableList<Customer> returnedCustomer = FXCollections.observableArrayList();
//        for (Customer customer: DBCustomer.getAllCustomers()){
//            if (customer.getCustomerId() == appointment.getCustomerId()){
//                returnedCustomer.add(customer);
//            }
//        }
//
//        customerNameComboBox.setValue(returnedCustomer.get(0));

    }
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        customerNameComboBox.setItems(DBCustomer.getAllCustomers());
    }    
    
}
