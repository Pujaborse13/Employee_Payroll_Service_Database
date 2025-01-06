package com.EmployePayrollServiceDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EmployeePayrollService {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/payroll_service";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "1321";

    public static void main(String[] args) {
        EmployeePayrollService service = new EmployeePayrollService();
        try {
            List<EmployeePayroll> employeePayrollList = service.getEmployeePayrollData();
            System.out.println("Employee Payroll Data Retrieved:");
            employeePayrollList.forEach(System.out::println);
        } catch (CustomDatabaseException e) {
            System.err.println(e.getMessage());
        }
    }

    public List<EmployeePayroll> getEmployeePayrollData() throws CustomDatabaseException {
        List<EmployeePayroll> employeePayrollList = new ArrayList<>();
        String sql = "SELECT id, name, salary, start_date FROM employee_payroll";

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                double salary = resultSet.getDouble("salary");
                Date startDate = resultSet.getDate("start_date");
                employeePayrollList.add(new EmployeePayroll(id, name, salary, startDate));
            }
        } catch (SQLException e) {
            throw new CustomDatabaseException("Error retrieving employee payroll data.", e);
        }
        return employeePayrollList;
    }
}

class CustomDatabaseException extends Exception {
    public CustomDatabaseException(String message, Throwable cause) {
        super(message, cause);
    }
}
