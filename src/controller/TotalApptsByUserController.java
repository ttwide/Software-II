/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import DBAccess.DBAppointment;
import java.io.IOException;
import java.net.URL;
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
import model.TotalAppointments;

/**
 * FXML Controller class
 *
 * @author lisakim
 */
public class TotalApptsByUserController implements Initializable {
    
    Stage stage;
    Parent scene;

    @FXML
    private TableView<TotalAppointments> totalTableView;

    @FXML
    private TableColumn<TotalAppointments, String> userNameCol;

    @FXML
    private TableColumn<TotalAppointments, Integer> quantityCol;

    @FXML
    void onActionBackButton(ActionEvent event) throws IOException {
        
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/ViewAppointments.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }
    
    
    

    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        
        totalTableView.setItems(DBAppointment.totalApptsByUser());
        userNameCol.setCellValueFactory(new PropertyValueFactory<>("userName"));
        quantityCol.setCellValueFactory(new PropertyValueFactory<>("total"));
       
    }    
    
}


