package com.EmployePayrollServiceDatabase;


import java.sql.*;
import java.util.Enumeration;

public class DemoConnection
{

    public static void main(String[] a) {

        String url = "jdbc:mysql://localhost:3306/payroll_service";
        String username = "root";
        String password = "1321";

        Connection connection;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("Driver Loaded ");
        }
        catch (ClassNotFoundException e)
        {
            throw new IllegalStateException("Cannot find the driver in the classpath !",e);

        }


        listDrivers();

        try{
            System.out.println("Connecting to the database :"+url);
            connection = DriverManager.getConnection(url,username,password);
            System.out.println("Connection is Successful"+connection);

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        public static void listDrivers()
        {

        Enumeration<Driver> drivers = DriverManager.getDrivers();
        while (drivers.hasMoreElements()) {
            Driver driver = drivers.nextElement();
            System.out.println(driver.getClass().getName());
        }
    }

}
