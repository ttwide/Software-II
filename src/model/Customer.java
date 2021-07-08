/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 *
 * @author lisakim
 */
public class Customer {
    
    private int customerId;
    private String customerName;
    private int addressId;
    
    // to combine customer table and address table
    private String address;
    private String address2;
    private int cityId;
    private String city;
    private String phone;

    public Customer(int customerId, String customerName, int addressId, String address, String address2, int cityId, String city, String phone) {
        this.customerId = customerId;
        this.customerName = customerName;
        this.addressId = addressId;
        this.address = address;
        this.address2 = address2;
        this.cityId = cityId;
        this.city = city;
        this.phone = phone;
    }

    public int getCustomerId() {
        return customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public int getAddressId() {
        return addressId;
    }

    public String getAddress() {
        return address;
    }

    public String getAddress2() {
        return address2;
    }

    public int getCityId() {
        return cityId;
    }

    public String getCity() {
        return city;
    }

    public String getPhone() {
        return phone;
    }

    @Override
    public String toString() {
        return customerName;
    }


    

    
}
