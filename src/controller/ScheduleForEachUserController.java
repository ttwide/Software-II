/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import DBAccess.DBAppointment;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Appointment;

/**
 * FXML Controller class
 *
 * @author lisakim
 */
public class ScheduleForEachUserController implements Initializable {
    
    Stage stage;
    Parent scene;

    @FXML
    private TableView<Appointment> appointmentTableView;
    
    @FXML
    private TableColumn<Appointment, Integer> userIdCol;
    
    @FXML
    private TableColumn<Appointment, String> usernameCol;
    
    @FXML
    private TableColumn<Appointment, Integer> appIdCol;
    
    @FXML
    private TableColumn<Appointment, String> customerNameCol;
    
    @FXML
    private TableColumn<Appointment, Integer> customerIdCol;
    
    @FXML
    private TableColumn<Appointment, String> typeCol;
    
    @FXML
    private TableColumn<Appointment, LocalDateTime> startCol;
    
    @FXML
    private TableColumn<Appointment, LocalDateTime> endCol;

    /**
     * Initializes the controller class.
     */
    
    @FXML
    private void onActionBackButton(ActionEvent event) throws IOException {
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/ViewAppointments.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        appointmentTableView.setItems(DBAppointment.apptsByUser());
        userIdCol.setCellValueFactory(new PropertyValueFactory<>("userId"));
        usernameCol.setCellValueFactory(new PropertyValueFactory<>("userName"));
        appIdCol.setCellValueFactory(new PropertyValueFactory<>("appointmentId"));
        customerIdCol.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        customerNameCol.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        startCol.setCellValueFactory(new PropertyValueFactory<>("startTime"));
        endCol.setCellValueFactory(new PropertyValueFactory<>("endTime"));
    }    


    
}
