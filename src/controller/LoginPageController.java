/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;


import DBAccess.DBAppointment;
import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import utils.DBConnection;
import utils.DBQuery;

/**
 * FXML Controller class
 *
 * @author lisakim
 */
public class LoginPageController implements Initializable {
    
    Stage stage;
    Parent scene;
    
    public static int userId;
    public static String userName;
    
    @FXML
    private TextField usernameTextBox;

    @FXML
    private TextField passwordTextBox;
    
    @FXML
    private Label loginMessageLabel;
    
    @FXML
    private Label usernameLabel;

    @FXML
    private Label passwordLabel;
    
    @FXML
    private Button loginButtonText;

    @FXML
    private Button cancelButtonText;

    
    @FXML
    void onActionLogin(ActionEvent event) throws IOException, SQLException {
        // changes message "the username or password ...." to spanish if default Locale
        ResourceBundle bundle = ResourceBundle.getBundle("properties/Nat", Locale.getDefault());
        if(Locale.getDefault().getLanguage().equals("es")){
            loginMessageLabel.setText(
                    bundle.getString("the") + " " +
                    bundle.getString("username") + " " +
                    bundle.getString("or") + " " +
                    bundle.getString("password") + " " +        
                    bundle.getString("does") + " " +
                    bundle.getString("not") + " " +
                    bundle.getString("match") + " " +
                    bundle.getString("our") + " " +
                    bundle.getString("records") + " ");
        }
        

       String enteredUsername = usernameTextBox.getText();
        String enteredPassword = passwordTextBox.getText();
        
        String selectStatement = "SELECT userId, username, password FROM user WHERE username =? and password =?";
        DBQuery.setPreparedStatement(DBConnection.startConnection(), selectStatement);
        
        PreparedStatement ps = DBQuery.getPreparedStatement();
        
        ps.setString(1, enteredUsername);
        ps.setString(2, enteredPassword);
        ps.execute(); 
        
        ResultSet rs = ps.getResultSet();
        
        if(rs.next()){
            String dbUsername = rs.getString("userName");
            String dbPassword = rs.getString("password");
            userId = rs.getInt("userId");
            userName = dbUsername;
            System.out.println("DB username: " + dbUsername);
            System.out.println("DB password: " + dbPassword);
            System.out.println("DB userId: " + userId);
            System.out.println("Entered password: " + enteredPassword);
            System.out.println("got to next screen");
            DBAppointment.appointmentAlert();
            DBAppointment.loginTimestamps();
            stage = (Stage)((Button)event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("/view/MainPage.fxml"));
            stage.setScene(new Scene(scene));
            stage.show();
        } else {
            System.out.println("do not go to next screen");
            loginMessageLabel.setOpacity(1);
            usernameTextBox.setText("");
            passwordTextBox.setText("");              
        }
        
    }
    
    @FXML
    void passwordSetOpacity(MouseEvent event) {
        //loginMessageLabel.setOpacity(0);
    }

    @FXML
    void usernameSetOpacity(MouseEvent event) {
        //loginMessageLabel.setOpacity(0);
    }

    @FXML
    void onActionCancel(ActionEvent event) {
        DBConnection.closeConnection();
        System.exit(0);
    }


    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // changes entire login form to spanish if default locale
        ResourceBundle bundle = ResourceBundle.getBundle("properties/Nat", Locale.getDefault());
        if(Locale.getDefault().getLanguage().equals("es")){
            usernameLabel.setText(bundle.getString("username"));
            passwordLabel.setText(bundle.getString("password"));
            loginButtonText.setText(bundle.getString("login"));
            cancelButtonText.setText(bundle.getString("cancel"));
        }
        
    }    
    
}















//        while (rs.next()){
//            int userId = rs.getInt("userId");
//            String userName = rs.getString("userName");
//            String password = rs.getString("password");
//            int active = rs.getInt("active");
//            LocalDate date = rs.getDate("createDate").toLocalDate();
//            LocalTime time = rs.getTime("createDate").toLocalTime();
//            String createdBy = rs.getString("createdBy");
//            LocalDateTime lastUpdate = rs.getTimestamp("lastUpdate").toLocalDateTime();
//            
//            System.out.println("user id: " + userId + "username: " + userName +
//                    "password: " + password + "active: " + active + "create date: " +
//                    date + time + "created by: " + createdBy + "last update: " 
//                    + lastUpdate);
//


            // date and time have probably been deprecated
            //LocalDate date = rs.getDate("createDate").toLocalDate();
            //LocalTime time = rs.getTime("createDate").toLocalTime();
            //String createdBy = rs.getString("createdBy");
            //LocalDateTime lastUpdate = rs.getTimestamp("lastUpdate").toLocalDateTime();

            //System.out.println(countryId + " | " + country + " | " + date + 
            //        " " + time + " | " + createdBy + " | " + lastUpdate);
            //}   

//************ failed try catch with select statement  -there is not way to throw an error since the item is
// not being the database is okay
// try { 
//            String enteredUsername = usernameTextBox.getText();
//            String enteredPassword = passwordTextBox.getText();
//
//            String selectStatement = "SELECT username, password FROM user WHERE username =? and password =?";
//            DBQuery.setPreparedStatement(DBConnection.startConnection(), selectStatement);
//
//            PreparedStatement ps = DBQuery.getPreparedStatement();
//
//            ps.setString(1, enteredUsername);
//            ps.setString(2, enteredPassword);
//            ps.execute(); 
//
//            ResultSet rs = ps.getResultSet();
//
//            if(rs.next()){
//                String dbUsername = rs.getString("userName");
//                String dbPassword = rs.getString("password");
//                System.out.println("DB username: " + dbUsername);
//                System.out.println("DB password: " + dbPassword);
//                System.out.println("Entered password: " + enteredPassword);
//                    if (enteredPassword.equals(dbPassword)){
//                        System.out.println("got to next screen");
//                        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
//                        // TODO
//                        // make logged in page
//                        scene = FXMLLoader.load(getClass().getResource("/view/LoginPage.fxml"));
//                        stage.setScene(new Scene(scene));
//                        stage.show();
//                    }
//            }        
//        }catch (SQLException ex) {
//                    System.out.println("do not go to next screen");
//                    loginMessageLabel.setOpacity(1);
//                    usernameTextBox.setText("");
//                    passwordTextBox.setText("");              
//        }
//        