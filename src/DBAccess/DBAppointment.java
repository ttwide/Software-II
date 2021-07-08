/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DBAccess;

import controller.LoginPageController;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.TimeZone;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import model.Appointment;
import model.TotalAppointments;
import model.Type;
import utils.DBConnection;

/**
 *
 * @author lisakim
 */
public class DBAppointment {
    
    
    public static ObservableList<Appointment> getAllAppointments() {
        ObservableList <Appointment> appointmentList = FXCollections.observableArrayList();
        try {
//            String sql = "select appointmentId, customer.customerId, customerName, appointment.userId, userName, type, start, end from customer, user, appointment where "
//                          + "customer.customerId = appointment.customerId and appointment.userId = user.userId";
            
            String sql = "select appointmentId, customer.customerId, customerName, appointment.userId, userName, type, start, end from customer, user, appointment where "
                          + "customer.customerId = appointment.customerId and appointment.userId = ? and user.userId = ?";
            
            PreparedStatement ps = DBConnection.startConnection().prepareStatement(sql);
            
            ps.setInt(1, LoginPageController.userId);
            ps.setInt(2, LoginPageController.userId);
            
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()){
                int appointmentId = rs.getInt("appointmentId");
                int customerId = rs.getInt("customerId");
                String customerName = rs.getString("customerName");
                int userId = rs.getInt("userId");
                String userName = rs.getString("userName");
                String type = rs.getString("type");
                LocalDateTime start = rs.getTimestamp("start").toLocalDateTime();//"start" is the column name in the database not the name of variable in the class
                LocalDateTime end = rs.getTimestamp("end").toLocalDateTime();
                
                // start new code
                ZoneId myComputerZoneId = ZoneId.of("America/Chicago");
                ZonedDateTime myZonedStart = start.atZone(myComputerZoneId);
                ZonedDateTime myZonedEnd = end.atZone(myComputerZoneId);
                
                TimeZone systemTimeZone = TimeZone.getDefault();
                ZoneId systemZoneId = systemTimeZone.toZoneId();
                
                ZonedDateTime systemStartTime = myZonedStart.withZoneSameInstant(systemZoneId);
                ZonedDateTime systemStartEnd = myZonedEnd.withZoneSameInstant(systemZoneId);
                
                LocalDateTime s = systemStartTime.toLocalDateTime();
                LocalDateTime e = systemStartEnd.toLocalDateTime();
                
                Appointment a = new Appointment(appointmentId, customerId, customerName, userId, userName, type, s, e);
                // end new code
                
                
                //Appointment a = new Appointment(appointmentId, customerId, customerName, userId, userName, type, start, end);
                appointmentList.add(a);
            }
            
         } catch (SQLException ex) {
            ex.printStackTrace();
        }
        
        return appointmentList;
    }
    
    public static void addAppointment(int customerId, String type, LocalDateTime start, LocalDateTime end){
        try {
            String sql = "insert into appointment (appointmentId, customerId, userId, title, description,location, contact, type, url, start, end, createDate, createdBy,lastUpdate,lastUpdateBy) "
                    + " values (null, ?, 1, 'Planning 101', 'Planning', 'going of different retirement plans', 'meeting room', ?, 'http:ljk;lj', ?, ?, now(), 'Rob', now(), 'Rob')";
            
            LocalTime open = LocalTime.of(8, 0);
            LocalTime close = LocalTime.of(17, 0);
            // DO NOT ERASE
            if (start.toLocalTime().isBefore(open) || end.toLocalTime().isAfter(close)){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Dialog");
                alert.setContentText("This appointment is not within the scheduled business hours of 08:00 to 17:00.");
                alert.showAndWait();  
                return;
            } 

            
            
            PreparedStatement ps = DBConnection.startConnection().prepareStatement(sql);
            
            // start new code
            TimeZone systemTimeZone = TimeZone.getDefault();
            ZoneId systemZoneId = systemTimeZone.toZoneId();

            ZonedDateTime DBZonedStart = start.atZone(systemZoneId);
            ZonedDateTime DBZonedEnd = end.atZone(systemZoneId);
              
              
            ZoneId myComputerZoneId = ZoneId.of("America/Chicago");
              

              

            ZonedDateTime DBStartTime = DBZonedStart.withZoneSameInstant(myComputerZoneId);
            ZonedDateTime DBEndTime = DBZonedEnd.withZoneSameInstant(myComputerZoneId);

            LocalDateTime s = DBStartTime.toLocalDateTime();
            LocalDateTime e = DBEndTime.toLocalDateTime();
            
            System.out.println("Checking s.toLocalTime: " + s.toLocalTime());
            // end new code
            
            // new code 
            try{
                String sql1 = "select * from appointment where userId =?";
                
                PreparedStatement ps1 = DBConnection.startConnection().prepareStatement(sql1);
                
                ps1.setInt(1, LoginPageController.userId);
                
                ResultSet rs1 = ps1.executeQuery();
                
                while(rs1.next()){
                    LocalDateTime dbStart = rs1.getTimestamp("start").toLocalDateTime();
                    LocalDateTime dbEnd = rs1.getTimestamp("end").toLocalDateTime();
                    System.out.println("dbs" +dbStart);
                    System.out.println("dbs" +dbEnd);

                    
                    if(s.isAfter(dbStart) && e.isBefore(dbEnd)){
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error Dialog");
                        alert.setContentText("This appointment is overlaps with a current appointment1");
                        alert.showAndWait();  
                        return;
                    } else if (s.equals(dbStart) || s.equals(dbEnd)|| e.equals(dbStart) || e.equals(dbEnd)) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error Dialog");
                        alert.setContentText("This appointment is overlaps with a current appointment2");
                        alert.showAndWait();  
                        return;
                    } else if (s.isBefore(dbStart) && e.isAfter(dbEnd)){
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error Dialog");
                        alert.setContentText("This appointment is overlaps with a current appointment3");
                        alert.showAndWait();  
                        return;
                    
                    } else if (s.isBefore(dbStart) && e.isAfter(dbStart)){
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error Dialog");
                        alert.setContentText("This appointment is overlaps with a current appointment4");
                        alert.showAndWait();  
                    }
                }
                
            }catch (SQLException ex) {
                ex.printStackTrace();
            }
            // end new code  
            
            //        LocalDateTime startDT = LocalDateTime.of(2020, 8, 26, 8,0);
//        LocalDateTime endDT = LocalDateTime.of(2020, 8, 26, 17,30);
//        LocalDateTime myDT = LocalDateTime.of(2020, 8, 25, 17, 15);
//        LocalTime open = LocalTime.of(8, 0);
//        LocalTime close = LocalTime.of(17, 0);
//        LocalDateTime time = LocalDateTime.now();
//        System.out.println(time);
//        
//        if (startDT.toLocalTime().isBefore(open)){
//            System.out.println("Scheduled appointment is before opening time.");
//        }
//        if (myDT.isAfter(startDT) && myDT.isBefore(endDT)){
//            System.out.println(myDT + " is between " + startDT + " and " + endDT);
//        } else if (myDT.equals(startDT) || myDT.equals(endDT)){
//            System.out.println("Your date and time matches " + startDT + " or " + endDT);
//        } else {
//            System.out.println("You date and time does not overlap.");
//        }
            
            Timestamp startTimestamp = Timestamp.valueOf(s);
            Timestamp endTimestamp = Timestamp.valueOf(e);
            
            ps.setInt(1, customerId);
            ps.setString(2, type);
            ps.setTimestamp(3, startTimestamp);
            ps.setTimestamp(4, endTimestamp);
            
            ps.execute();
            
        } catch (SQLException ex){
            ex.printStackTrace();
        }
    }
    
    public static void updateAppointment(int customerId, String type, LocalDateTime start, LocalDateTime end, int appointmentId){
        try {
            String sql = "UPDATE appointment set customerId = ? where appointmentId = ?";
            
            LocalTime open = LocalTime.of(8, 0);
            LocalTime close = LocalTime.of(17, 0);
 
            //DO NOT ERASE 
            if (start.toLocalTime().isBefore(open) || end.toLocalTime().isAfter(close)){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Dialog");
                alert.setContentText("This appointment is not within the scheduled business hours of 08:00 to 17:00.");
                alert.showAndWait();  
                return;
            }
            
            PreparedStatement ps1 = DBConnection.startConnection().prepareStatement(sql);
            
            ps1.setInt(1, customerId);
            ps1.setInt(2, appointmentId);
            
            ps1.execute();
            
            // new code
            TimeZone systemTimeZone = TimeZone.getDefault();
            ZoneId systemZoneId = systemTimeZone.toZoneId();

            ZonedDateTime DBZonedStart = start.atZone(systemZoneId);
            ZonedDateTime DBZonedEnd = end.atZone(systemZoneId);
              
              
            ZoneId myComputerZoneId = ZoneId.of("America/Chicago");
              

            ZonedDateTime DBStartTime = DBZonedStart.withZoneSameInstant(myComputerZoneId);
            ZonedDateTime DBEndTime = DBZonedEnd.withZoneSameInstant(myComputerZoneId);

            LocalDateTime s = DBStartTime.toLocalDateTime();
            LocalDateTime e = DBEndTime.toLocalDateTime();
            // end new code
            
            // new code 
            try{
                String sql2 = "select * from appointment where userId =?";
                
                PreparedStatement ps2 = DBConnection.startConnection().prepareStatement(sql2);
                
                ps2.setInt(1, LoginPageController.userId);
                
                ResultSet rs2 = ps2.executeQuery();
                
                while(rs2.next()){
                    LocalDateTime dbStart = rs2.getTimestamp("start").toLocalDateTime();
                    LocalDateTime dbEnd = rs2.getTimestamp("end").toLocalDateTime();
                    System.out.println("dbs" +dbStart);
                    System.out.println("dbs" +dbEnd);

                    
                    if(s.isAfter(dbStart) && e.isBefore(dbEnd)){
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error Dialog");
                        alert.setContentText("This appointment is overlaps with a current appointment1");
                        alert.showAndWait();  
                        return;
                    } else if (s.equals(dbStart) || s.equals(dbEnd)|| e.equals(dbStart) || e.equals(dbEnd)) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error Dialog");
                        alert.setContentText("This appointment is overlaps with a current appointment2");
                        alert.showAndWait();  
                        return;
                    } else if (s.isBefore(dbStart) && e.isAfter(dbEnd)){
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error Dialog");
                        alert.setContentText("This appointment is overlaps with a current appointment3");
                        alert.showAndWait();  
                        return;
                    
                    } else if (s.isBefore(dbStart) && e.isAfter(dbStart)){
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error Dialog");
                        alert.setContentText("This appointment is overlaps with a current appointment4");
                        alert.showAndWait();  
                    }
                }
                
            }catch (SQLException ex) {
                ex.printStackTrace();
            }
            // end new code  
            
            
            
            String sqla = "UPDATE appointment set type = ?, start = ?, end = ? where appointmentId = ?";
            Timestamp startTimestamp = Timestamp.valueOf(s);
            Timestamp endTimestamp = Timestamp.valueOf(e);
            
            PreparedStatement ps = DBConnection.startConnection().prepareStatement(sqla);
            
            ps.setString(1, type);
            ps.setTimestamp(2, startTimestamp);
            ps.setTimestamp(3, endTimestamp);
            ps.setInt(4, appointmentId);
            
            ps.execute();
            
        } catch (SQLException ex){
            ex.printStackTrace();
        }
    }
    
    
    public static void deleteAppointment(int appointmentId){
        
        try {
            
            String sql = "DELETE FROM appointment WHERE appointmentId =?";
        
            PreparedStatement ps = DBConnection.startConnection().prepareStatement(sql);
        
            ps.setInt(1, appointmentId);
            
            ps.execute();
            
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    public static ObservableList<Appointment> monthView() {
        ObservableList <Appointment> appointmentList = FXCollections.observableArrayList();

        LocalDateTime localDateTime = LocalDateTime.now();
        System.out.println(localDateTime);
        
        // start new code
        ZoneId myComputerZoneId = ZoneId.of("America/Chicago");
        ZonedDateTime myZonedLocalDateTime = localDateTime.atZone(myComputerZoneId);

        TimeZone systemTimeZone = TimeZone.getDefault();
        ZoneId systemZoneId = systemTimeZone.toZoneId();

        ZonedDateTime systemZonedTime = myZonedLocalDateTime.withZoneSameInstant(systemZoneId);

        LocalDateTime newTime = systemZonedTime.toLocalDateTime();
        // convert from chicago to system default
        // grab the month and year from the system default

        int year = newTime.getYear();
        int month = newTime.getMonthValue();
        String convertedMonth;
        if (month < 10) {
            convertedMonth = "0"+ String.valueOf(month);
        } else {
            convertedMonth = String.valueOf(month);
        }
        String yearAndMonth = String.valueOf(year) + "-" + convertedMonth + "%";
//        System.out.println(year);
//        System.out.println(month);
//        System.out.println(yearAndMonth);
//
        try {
            
            String sql = "select appointmentId, customer.customerId, customerName, appointment.userId, userName, type, start, end from customer, user, appointment where\n" +
                "start like ? and customer.customerId = appointment.customerId and appointment.userId = ? and user.userId = ?;";
        
            PreparedStatement ps = DBConnection.startConnection().prepareStatement(sql);
        
            ps.setString(1, yearAndMonth);
            ps.setInt(2, LoginPageController.userId);
            ps.setInt(3, LoginPageController.userId);
            
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()){
                int appointmentId = rs.getInt("appointmentId");
                int customerId = rs.getInt("customerId");
                String customerName = rs.getString("customerName");
                int userId = rs.getInt("userId");
                String userName = rs.getString("userName");
                String type = rs.getString("type");
                LocalDateTime start = rs.getTimestamp("start").toLocalDateTime();//"start" is the column name in the database not the name of variable in the class
                LocalDateTime end = rs.getTimestamp("end").toLocalDateTime();
                
                //ZoneId myComputerZoneId = ZoneId.of("America/Chicago");
                ZonedDateTime myZonedStart = start.atZone(myComputerZoneId);
                ZonedDateTime myZonedEnd = end.atZone(myComputerZoneId);
                
                //TimeZone systemTimeZone = TimeZone.getDefault();
                //ZoneId systemZoneId = systemTimeZone.toZoneId();
                
                ZonedDateTime systemStartTime = myZonedStart.withZoneSameInstant(systemZoneId);
                ZonedDateTime systemStartEnd = myZonedEnd.withZoneSameInstant(systemZoneId);
                
                LocalDateTime s = systemStartTime.toLocalDateTime();
                LocalDateTime e = systemStartEnd.toLocalDateTime();
                
                
                
                Appointment a = new Appointment(appointmentId, customerId, customerName, userId, userName, type, s, e);
                appointmentList.add(a);
            }
            
            
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return appointmentList;
    } 
    
    public static ObservableList<Appointment> weekView() {
        ObservableList <Appointment> appointmentList = FXCollections.observableArrayList();
        
        try {
            
            String sql = "select appointmentId, customer.customerId, customerName, appointment.userId, userName, type, start, end from customer, user, appointment where\n" +
                "yearweek(start) = yearweek(curdate())  and customer.customerId = appointment.customerId and appointment.userId = ? and user.userId = ?;";
        
            PreparedStatement ps = DBConnection.startConnection().prepareStatement(sql);
        
            ps.setInt(1, LoginPageController.userId);
            ps.setInt(2, LoginPageController.userId);
            
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()){
                int appointmentId = rs.getInt("appointmentId");
                int customerId = rs.getInt("customerId");
                String customerName = rs.getString("customerName");
                int userId = rs.getInt("userId");
                String userName = rs.getString("userName");
                String type = rs.getString("type");
                LocalDateTime start = rs.getTimestamp("start").toLocalDateTime();//"start" is the column name in the database not the name of variable in the class
                LocalDateTime end = rs.getTimestamp("end").toLocalDateTime();
                
                ZoneId myComputerZoneId = ZoneId.of("America/Chicago");
                ZonedDateTime myZonedStart = start.atZone(myComputerZoneId);
                ZonedDateTime myZonedEnd = end.atZone(myComputerZoneId);
                
                TimeZone systemTimeZone = TimeZone.getDefault();
                ZoneId systemZoneId = systemTimeZone.toZoneId();
                
                ZonedDateTime systemStartTime = myZonedStart.withZoneSameInstant(systemZoneId);
                ZonedDateTime systemStartEnd = myZonedEnd.withZoneSameInstant(systemZoneId);
                
                LocalDateTime s = systemStartTime.toLocalDateTime();
                LocalDateTime e = systemStartEnd.toLocalDateTime();
                
                Appointment a = new Appointment(appointmentId, customerId, customerName, userId, userName, type, s, e);
                appointmentList.add(a);
            }
            
            
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return appointmentList;
    }
    
    public static void appointmentAlert() {
        
        ObservableList <Appointment> appointmentList = FXCollections.observableArrayList();
        
        LocalDateTime ldt = LocalDateTime.now();
        
        TimeZone systemTimeZone = TimeZone.getDefault();
        ZoneId systemZoneId = systemTimeZone.toZoneId();

        ZonedDateTime DBZonedldt = ldt.atZone(systemZoneId);              
              
        ZoneId myComputerZoneId = ZoneId.of("America/Chicago");

        ZonedDateTime DBldt = DBZonedldt.withZoneSameInstant(myComputerZoneId);

        LocalDateTime time = DBldt.toLocalDateTime();
            
        LocalDate currentDate = time.toLocalDate();
        
        String currentDateString = currentDate.toString();
        
        System.out.println(currentDateString + " is current Date String");
        
        try {
            String sql = "select appointmentId, customer.customerId, customerName, appointment.userId, userName, type, start, end from customer, user, appointment where "
                          + "date(start) = ? and customer.customerId = appointment.customerId and appointment.userId = ? and user.userId = ?";
            
                // cur date returns the date from system default
                // but you have to convert the times when they come out the DB
                
            PreparedStatement ps = DBConnection.startConnection().prepareStatement(sql);
            
            ps.setString(1, currentDateString);
            ps.setInt(2, LoginPageController.userId);
            ps.setInt(3, LoginPageController.userId);

            
            ResultSet rs = ps.executeQuery();
            
//            ZoneId myComputerZoneId = ZoneId.of("America/Chicago");
//            TimeZone systemTimeZone = TimeZone.getDefault();
//            ZoneId systemZoneId = systemTimeZone.toZoneId();
            
            while (rs.next()){
                int appointmentId = rs.getInt("appointmentId");
                int customerId = rs.getInt("customerId");
                String customerName = rs.getString("customerName");
                int userId = rs.getInt("userId");
                String userName = rs.getString("userName");
                String type = rs.getString("type");
                LocalDateTime start = rs.getTimestamp("start").toLocalDateTime();//"start" is the column name in the database not the name of variable in the class
                LocalDateTime end = rs.getTimestamp("end").toLocalDateTime();
                
                ZonedDateTime myZonedStart = start.atZone(myComputerZoneId);
                ZonedDateTime myZonedEnd = end.atZone(myComputerZoneId);

                
                ZonedDateTime systemStartTime = myZonedStart.withZoneSameInstant(systemZoneId);
                ZonedDateTime systemStartEnd = myZonedEnd.withZoneSameInstant(systemZoneId);

                
                LocalDateTime s = systemStartTime.toLocalDateTime();
                LocalDateTime e = systemStartEnd.toLocalDateTime();
                
                Appointment a = new Appointment(appointmentId, customerId, customerName, userId, userName, type, s, e);
                appointmentList.add(a);
                System.out.println("appointment added");
            }
            
            LocalTime currentTime = LocalTime.now();
            
            for (Appointment appointment : appointmentList){
                System.out.println(appointment.getAppointmentId());
                long timeDifference = ChronoUnit.MINUTES.between(currentTime, appointment.getStartTime().toLocalTime());
                if (timeDifference > 0 && timeDifference <=15){
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Appointment Alet");
                    alert.setContentText("You have an appointment with " + appointment.getCustomerName() +
                            " in " + timeDifference + " minutes." );
                    alert.showAndWait();  
                    return;
                }
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        
        
    }
    
    public static ObservableList<Type> numberOfApptsTypesByMonth () {
       
        ObservableList <Type> typeList = FXCollections.observableArrayList();

        LocalDateTime localDateTime = LocalDateTime.now();
        
        // start new code
        ZoneId myComputerZoneId = ZoneId.of("America/Chicago");
        ZonedDateTime myZonedLocalDateTime = localDateTime.atZone(myComputerZoneId);

        TimeZone systemTimeZone = TimeZone.getDefault();
        ZoneId systemZoneId = systemTimeZone.toZoneId();

        ZonedDateTime systemZonedTime = myZonedLocalDateTime.withZoneSameInstant(systemZoneId);

        LocalDateTime newTime = systemZonedTime.toLocalDateTime();
        // convert from chicago to system default
        // grab the month and year from the system default
        
        
        int year = newTime.getYear();
        int month = newTime.getMonthValue();
        String convertedMonth;
        if (month < 10) {
            convertedMonth = "0"+ String.valueOf(month);
        } else {
            convertedMonth = String.valueOf(month);
        }
        String yearAndMonth = String.valueOf(year) + "-" + convertedMonth + "%";
        
                
        try {
            String sql = "select type, count(type) as TypesOfAppointments from appointment where start like ? group by type;";
            
            PreparedStatement ps = DBConnection.startConnection().prepareStatement(sql);
            
            ps.setString(1, yearAndMonth);
            
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()){
                String type = rs.getString("type");
                int quantity = rs.getInt("TypesOfAppointments");
                
                Type t = new Type(type, quantity);
                typeList.add(t);
            }
            

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return typeList;
    }
    
    public static ObservableList<Appointment> apptsByUser() {
        ObservableList <Appointment> appointmentList = FXCollections.observableArrayList();

        try {
            
            String sql = "select appointment.userId, userName, customer.customerId,"
                    + " customerName, appointmentId, type, start, end from appointment, "
                    + "customer, user where customer.customerId = appointment.customerId and appointment.userId = user.userId order by userId;";
            
            PreparedStatement ps = DBConnection.startConnection().prepareStatement(sql);
            
            ResultSet rs = ps.executeQuery();
           
            while (rs.next()){
                int userId = rs.getInt("userId");
                String userName = rs.getString("userName");
                int customerId = rs.getInt("customerId");
                String customerName = rs.getString("customerName");
                int appointmentId = rs.getInt("appointmentId");
                String type = rs.getString("type");
                LocalDateTime start = rs.getTimestamp("start").toLocalDateTime();
                LocalDateTime end = rs.getTimestamp("end").toLocalDateTime();

                ZoneId myComputerZoneId = ZoneId.of("America/Chicago");
                ZonedDateTime myZonedStart = start.atZone(myComputerZoneId);
                ZonedDateTime myZonedEnd = end.atZone(myComputerZoneId);

                TimeZone systemTimeZone = TimeZone.getDefault();
                ZoneId systemZoneId = systemTimeZone.toZoneId();

                ZonedDateTime systemStartTime = myZonedStart.withZoneSameInstant(systemZoneId);
                ZonedDateTime systemStartEnd = myZonedEnd.withZoneSameInstant(systemZoneId);

                LocalDateTime s = systemStartTime.toLocalDateTime();
                LocalDateTime e = systemStartEnd.toLocalDateTime();
                
                Appointment a = new Appointment(appointmentId, customerId, customerName, userId, userName, type, s, e);
                appointmentList.add(a);
            }
            
         } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return appointmentList;
    }
    
    public static ObservableList<TotalAppointments> totalApptsByUser() {        
        
        ObservableList <TotalAppointments> appointmentList = FXCollections.observableArrayList();

        
        String sql = "select userName, count(appointmentId) as TotalNumberOfAppointments from appointment, user where appointment.userId = user.userId group by userName;";
        
        try {
            PreparedStatement ps = DBConnection.startConnection().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            
            while(rs.next()){
                String userName = rs.getString("userName");
                int totalNumberOfAppt = rs.getInt("TotalNumberOfAppointments");
                
                TotalAppointments ta = new TotalAppointments(userName, totalNumberOfAppt);
                appointmentList.add(ta);
            }
                    

            
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return appointmentList;
        
    }
    
    public static void loginTimestamps() throws IOException {
        
        LocalDateTime ldt = LocalDateTime.now();
        
        String filename = "src/files/logins.txt";
        
        File file = new File(filename);
        
        if(!file.exists()){
            System.out.println("File not found.");
            System.exit(0);
        }
        
        FileWriter fwriter = new FileWriter(filename, true);

        fwriter.write(LoginPageController.userName + " logged in at " + ldt.toString() + "\n");
        fwriter.close();


    }
}
