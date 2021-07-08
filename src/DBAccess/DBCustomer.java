/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DBAccess;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Customer;
import utils.DBConnection;

/**
 *
 * @author lisakim
 */
public class DBCustomer {
   
    public static ObservableList<Customer> getAllCustomers() {
        ObservableList <Customer> customerList = FXCollections.observableArrayList();
        
        try {
            String sql = "SELECT customerId, customerName, customer.addressId, address, address2, address.cityId, city, phone FROM customer, address, city " +
                    "WHERE customer.addressId = address.addressId AND address.cityId = city.cityId";
            
            PreparedStatement ps = DBConnection.startConnection().prepareStatement(sql);
            
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()){
                int customerId = rs.getInt("customerId");
                String customerName = rs.getString("customerName");
                int addressId = rs.getInt("addressId");
                String address = rs.getString("address");
                String address2 = rs.getString("address2");
                int cityId = rs.getInt("cityId");
                String city = rs.getString("city");
                String phone = rs.getString("phone");
                
                Customer customer = new Customer(customerId, customerName, addressId, address, address2, cityId, city, phone);
                customerList.add(customer);
            }
        
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        
        return customerList;
    }
    
    public static void createCustomer(String customerName, String address, String address2, int cityId, String phone){
        try {
            // creating the address id first
            String sqlai = "INSERT INTO address VALUES (NULL, ?, ?, ?, '76543', ?, now(), 'admin', now(), 'Bob')";
            PreparedStatement psai = DBConnection.startConnection().prepareStatement(sqlai, Statement.RETURN_GENERATED_KEYS);
            
            psai.setString(1, address);
            psai.setString(2, address2);
            psai.setInt(3, cityId);
            psai.setString(4, phone);
            
            psai.execute();
            
            ResultSet rs = psai.getGeneratedKeys();
            rs.next();
            int addressId = rs.getInt(1);
            
            // sql statement for creating the customer
            String sqlc = "INSERT INTO customer VALUES (NULL, ?, ?,  1, now(), 'admin', now(), 'Bob')";
            
            PreparedStatement psc = DBConnection.startConnection().prepareStatement(sqlc);
            
            psc.setString(1, customerName);
            psc.setInt(2, addressId);
            
            psc.execute();

        } catch (SQLException ex){
            ex.printStackTrace();
        }
    }
    
    public static void updateCustomer(int customerId, int addressId, String customerName, String address, String address2, int cityId, String phone){
        try {
            // updating the customer name first
            String sqlc = "UPDATE customer set customerName = ? where customerId = ?";
            
            PreparedStatement psc = DBConnection.startConnection().prepareStatement(sqlc);
            
            psc.setString(1, customerName);
            psc.setInt(2, customerId);
            psc.execute();
            
            String sqlai = "UPDATE address set address = ?, address2 = ?, cityId = ?, phone =? where addressId = ?";
           
            PreparedStatement psa = DBConnection.startConnection().prepareStatement(sqlai);
            psa.setString(1, address);
            psa.setString(2, address2);
            psa.setInt(3, cityId);
            psa.setString(4, phone);
            psa.setInt(5, addressId);
            psa.execute();
            
            
        } catch (SQLException ex){
            ex.printStackTrace();
        }
        
    }
    
    public static void deleteCustomer(int customerId, int addressId){
        try {
            String sqlc = "DELETE from customer WHERE addressId = ?";
            
            PreparedStatement pst = DBConnection.startConnection().prepareStatement(sqlc);
            
            pst.setInt(1, addressId);
            
            pst.execute();
            
            String sqla = "DELETE from address WHERE customerId = ?";
            
            PreparedStatement pst1 = DBConnection.startConnection().prepareStatement(sqla);
            
            pst1.setInt(1, customerId);
            
            pst.execute();
            
        } catch (SQLException ex){
            ex.printStackTrace();
        }
    }
}
