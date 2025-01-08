package com.EmployePayrollServiceDatabase;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EmployeePayrollService {

    public static void main(String[] args) {

        try {
            PayrollService payrollService = PayrollService.getInstance();

            String startDate = "2023-01-01";
            String endDate = "2023-12-31";


            List<EmployeePayroll> employeeInRange = payrollService.getEmployeePayrollByJoinDateRange(startDate,endDate);

            System.out.println("Employees who joined between"+startDate+ "and"+endDate);
            employeeInRange.forEach(System.out::println);

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
        String selectByNameSQL = "SELECT id, name, salary, start_date FROM employee_payroll WHERE start_date BETWEEN ? AND ?";
        selectByNamePreparedStatement = connection.prepareStatement(selectByNameSQL);
    }
    public List<EmployeePayroll> getEmployeePayrollByJoinDateRange(String startDate, String endDate) throws CustomDatabaseException {
        List<EmployeePayroll> employeeList = new ArrayList<>();

        try {
            selectByNamePreparedStatement.setString(1, startDate);
            selectByNamePreparedStatement.setString(2, endDate);


            try (ResultSet resultSet = selectByNamePreparedStatement.executeQuery()) {
                while(resultSet.next()) {
                    employeeList.add(mapToEmployeePayroll(resultSet));

                }
            }
        } catch (SQLException e) {
            throw new CustomDatabaseException("Error retrieving employee payroll data.", e);
        }
        return employeeList;
    }
        private EmployeePayroll mapToEmployeePayroll(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("id");
        String name = resultSet.getString("name");
        double salary = resultSet.getDouble("salary");
        Date startDate = resultSet.getDate("start_date");
        return new EmployeePayroll(id, name, salary, startDate);
    }

}
