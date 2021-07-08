/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DBAccess;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.City;
import utils.DBConnection;

/**
 *
 * @author lisakim
 */
public class DBCity {
    
    public static ObservableList<City> getAllCities(){
        ObservableList <City> cityList = FXCollections.observableArrayList();
        
        try {
            String sql = "SELECT cityId, city, country FROM city, country WHERE city.countryId = country.countryId";
            
            PreparedStatement ps = DBConnection.startConnection().prepareStatement(sql);
            
            ResultSet rs = ps.executeQuery();
            
            while(rs.next()){
                int cityId = rs.getInt("cityId");
                String city = rs.getString("city");
                String country = rs.getString("country");
                
                City c = new City(cityId, city, country);
                cityList.add(c);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        
        return cityList;
    }
}
