package com.EmployePayrollServiceDatabase;

import java.sql.*;
import java.util.Date;

public class EmployeePayrollService {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/payroll_service";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "1321";

    public static void main(String[] args) {
        EmployeePayrollService service = new EmployeePayrollService();
        try {
            String employeeName = "Terissa";
            double updatedSalary = 3000000.00;

            service.updateEmployeeSalary(employeeName, updatedSalary);
            EmployeePayroll employeePayroll = service.getEmployeePayrollByName(employeeName);

            if(employeePayroll.getSalary() == updatedSalary)
            {
                System.out.println("Updated employee payroll object is in syn with the database");
            }
            else {
                System.out.println("Discrepancy between Employee Payroll Object and Database.");
            }

            System.out.println("Updated Employee Data: " + employeePayroll);
        } catch (CustomDatabaseException e) {
            System.err.println(e.getMessage());
        }
    }

    public void updateEmployeeSalary(String name, double newSalary) throws CustomDatabaseException {
        String sql = "UPDATE employee_payroll SET salary = ? WHERE name = ?";

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setDouble(1, newSalary);
            preparedStatement.setString(2, name);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected == 0) {
                throw new CustomDatabaseException("No employee found with name: " + name, null);
            }

            System.out.println("Salary updated successfully for employee: " + name);
        } catch (SQLException e) {
            throw new CustomDatabaseException("Error updating employee salary.", e);
        }
    }

    public EmployeePayroll getEmployeePayrollByName(String name) throws CustomDatabaseException {
        String selectSql = "SELECT id, name, salary, start_date FROM employee_payroll WHERE name = ?";

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement preparedStatement = connection.prepareStatement(selectSql)) {

            preparedStatement.setString(1, name);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String employeeName = resultSet.getString("name");
                    double salary = resultSet.getDouble("salary");
                    Date startDate = resultSet.getDate("start_date");
                    return new EmployeePayroll(id, employeeName, salary, startDate);
                } else {
                    throw new CustomDatabaseException("Employee with name " + name + " not found.", null);
                }
            }
        } catch (SQLException e) {
            throw new CustomDatabaseException("Error retrieving employee payroll data.", e);
        }
    }
}