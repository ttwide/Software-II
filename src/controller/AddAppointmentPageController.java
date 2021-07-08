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
import java.util.Locale;
import java.util.ResourceBundle;
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
import model.Customer;

/**
 * FXML Controller class
 *
 * @author lisakim
 */
public class AddAppointmentPageController implements Initializable {

    Stage stage;
    Parent scene;

    @FXML
    private ComboBox<Customer> customerNameComboBox;

    @FXML
    private TextField typeTF;

    @FXML
    private TextField startDateTF;

    @FXML
    private TextField startTimeTF;

    @FXML
    private TextField endTimeTF;

    @FXML
    void onActionAddBtn(ActionEvent event) throws IOException {
        int customerId = customerNameComboBox.getValue().getCustomerId();
        String type = typeTF.getText();
        String startDate = startDateTF.getText();
        String startTime = startTimeTF.getText();
        String endDate = startDate;
        String endTime = endTimeTF.getText();
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime start = LocalDateTime.parse(startDate + " " + startTime, formatter);
        LocalDateTime end = LocalDateTime.parse(endDate + " " + endTime, formatter);
        
        DBAppointment.addAppointment(customerId, type, start, end);
        
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/ViewAppointments.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
        
    }

    @FXML
    void onActionCancelBtn(ActionEvent event) throws IOException {
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/MainPage.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
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
