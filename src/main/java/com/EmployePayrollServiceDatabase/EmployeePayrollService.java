package com.EmployePayrollServiceDatabase;

import java.sql.*;
import java.util.Date;

public class EmployeePayrollService {

    public static void main(String[] args) {


        try {
            PayrollService payrollService = PayrollService.getInstance();
            String employeeName = "Terissa";
            EmployeePayroll employeePayroll = payrollService.getEmployeePayrollByName(employeeName);

            if (employeePayroll != null) {
                System.out.println("Employee Payroll Data Retrieved: " + employeePayroll);
            } else {
                System.out.println("Employee not found with name: " + employeeName);
            }
        } catch (CustomDatabaseException e) {
            System.err.println(e.getMessage());
        }


    }

}
class PayrollService {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/payroll_service";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "1321";

    private static PayrollService instance;
    private Connection connection;
    private PreparedStatement selectByNamePreparedStatement;

    private PayrollService() throws CustomDatabaseException {
        try {
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            prepareStatements();


        } catch (SQLException e){
            throw new CustomDatabaseException("Unable to Establish database connection", e);

        }
    }

    public static PayrollService getInstance() throws CustomDatabaseException {
        if(instance == null) {
            synchronized (PayrollService.class) {
                if (instance == null) {
                    instance = new PayrollService();

                }
            }
        }
        return instance;
    }


    private void prepareStatements() throws SQLException {
        String selectByNameSQL = "SELECT id, name, salary, start_date FROM employee_payroll WHERE name = ?";
        selectByNamePreparedStatement = connection.prepareStatement(selectByNameSQL);
    }
    public EmployeePayroll getEmployeePayrollByName(String name) throws CustomDatabaseException {
        try {
            selectByNamePreparedStatement.setString(1, name);

            try (ResultSet resultSet = selectByNamePreparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return mapToEmployeePayroll(resultSet);
                } else {
                    return null; // No employee found
                }
            }
        } catch (SQLException e) {
            throw new CustomDatabaseException("Error retrieving employee payroll data.", e);
        }
    }

    private EmployeePayroll mapToEmployeePayroll(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("id");
        String name = resultSet.getString("name");
        double salary = resultSet.getDouble("salary");
        Date startDate = resultSet.getDate("start_date");
        return new EmployeePayroll(id, name, salary, startDate);
    }

}