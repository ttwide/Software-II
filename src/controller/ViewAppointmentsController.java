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
import java.time.LocalDateTime;
import java.util.Optional;
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
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Appointment;
import model.Customer;

/**
 * FXML Controller class
 *
 * @author lisakim
 */
public class ViewAppointmentsController implements Initializable {
    
    Stage stage;
    Parent scene;
    
    @FXML
    private TableView<Appointment> appointmentTableView;
       
    @FXML
    private TableColumn<Appointment, Integer> appointmentIdCol;
    
    @FXML
    private TableColumn<Appointment, Integer> customerIdCol;

    @FXML
    private TableColumn<Appointment, String> customerNameCol;

    @FXML
    private TableColumn<Appointment, String> typeCol;

    @FXML
    private TableColumn<Appointment, LocalDateTime> startTimeCol;

    @FXML
    private TableColumn<Appointment, LocalDateTime> endTimeCol;
    
    @FXML
    private ComboBox<String> viewComboBox;
    
    
    ObservableList<String> options = FXCollections.observableArrayList(
        "All", "Current Month", "Current Week");
    
    @FXML
    void onActionApptsByUser(ActionEvent event) throws IOException{
        
        DBAppointment.apptsByUser();
        
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/view/ScheduleForEachUser.fxml"));
        loader.load();
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        Parent scene = loader.getRoot();
        stage.setScene(new Scene(scene));
        stage.show();
        
    }
    
    
    @FXML
    void onActionTotalApptsByUser(ActionEvent event) throws IOException{
        DBAppointment.totalApptsByUser();
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/TotalApptsByUser.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }
    
    @FXML
    void onActionTypeByMonthAppt(ActionEvent event) throws IOException{
        DBAppointment.numberOfApptsTypesByMonth();
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/AppointmentTypesByMonth.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }
    
    @FXML
    void onActionComboBox(ActionEvent event) {
        if (viewComboBox.getValue().equals("All")){
            appointmentTableView.setItems(DBAppointment.getAllAppointments());
        } 
        if (viewComboBox.getValue().equals("Current Month")){
            appointmentTableView.setItems(DBAppointment.monthView());
        } 
        if (viewComboBox.getValue().equals("Current Week")){
            appointmentTableView.setItems(DBAppointment.weekView());
        }   
    }
    
    
    @FXML
    void onActionAddButton(ActionEvent event) throws IOException {
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/AddAppointmentPage.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    @FXML
    void onActionBackButton(ActionEvent event) throws IOException {
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/MainPage.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    @FXML
    void onActionDeleteButton(ActionEvent event) {
        Appointment appointment = appointmentTableView.getSelectionModel().getSelectedItem();
        if (appointment == null){
            return;
        }
        
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "This will delete" +
                "the selected appointment.  Click okay to continue.");
        Optional<ButtonType> result = alert.showAndWait();
        
        if (result.isPresent() && result.get().equals(ButtonType.OK)){
            DBAppointment.deleteAppointment(appointment.getAppointmentId());
        }
        
        appointmentTableView.setItems(DBAppointment.getAllAppointments());
    }

    @FXML
    void onActionUpdateButton(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/view/UpdateAppointmentPage.fxml"));
        loader.load();
        
        UpdateAppointmentPageController appointmentController = loader.getController();
        if (appointmentTableView.getSelectionModel().getSelectedItem() != null){
            Appointment sendingAppointment = appointmentTableView.getSelectionModel().getSelectedItem();
            
            appointmentController.sendAppointmentData(sendingAppointment);
            stage = (Stage)((Button)event.getSource()).getScene().getWindow();
            Parent scene = loader.getRoot();
            stage.setScene(new Scene(scene));
            stage.show();
        }
        

    }


    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        appointmentTableView.setItems(DBAppointment.getAllAppointments());
        appointmentIdCol.setCellValueFactory(new PropertyValueFactory<>("appointmentId"));
        customerIdCol.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        customerNameCol.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        startTimeCol.setCellValueFactory(new PropertyValueFactory<>("startTime"));
        endTimeCol.setCellValueFactory(new PropertyValueFactory<>("endTime"));          
        viewComboBox.setItems(options);
        viewComboBox.setValue(options.get(0));
    }    
    
}
