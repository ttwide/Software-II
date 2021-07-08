/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package terrencewidemansoftwareii;


import DBAccess.DBCustomer;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import utils.DBConnection;
import utils.DBQuery;

/**
 *
 * @author lisakim
 */
public class TerrenceWidemanSoftwareII extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/view/LoginPage.fxml"));
        Scene scene = new Scene(root);
        
        stage.setScene(scene);
        stage.show();

    }

    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws SQLException {
        Connection conn = DBConnection.startConnection();       
        launch(args);
        // any code written after launch will only be called on you close all 
        // the windows in the application
        DBConnection.closeConnection();
    }
    
}

// A. languages are English and Spanish
// C. Dates are in 'yyyy-mm-dd' and time is in military time
// F. exceptions are 1. scheduling an appointment outside business hours- 08:00-17:00
//    2. entering an incorrect username and password
// G.  Lambdas are in UpdateAppointmentPageController and UpdateCustomerPageController
// I.  Extra report is total number of appointments by user

