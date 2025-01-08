package com.EmployePayrollServiceDatabase;


import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class EmployeePayrollService {

    public static void main(String[] args) {
        try {
            PayrollDBService payrollDBService = PayrollDBService.getInstance();

            Map<String, GenderSalaryStats> genderStats = payrollDBService.getSalaryAnalysisByGender();

            System.out.println("Salary Analysis by Gender:");
            genderStats.forEach((gender, stats) -> {
                System.out.println("Gender: " + gender);
                System.out.println("  Sum: " + stats.getSum());
                System.out.println("  Average: " + stats.getAvg());
                System.out.println("  Min: " + stats.getMin());
                System.out.println("  Max: " + stats.getMax());
                System.out.println("  Count: " + stats.getCount());
            });

        } catch (CustomDatabaseException e) {
            System.err.println(e.getMessage());
        }
    }
}

class PayrollDBService {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/payroll_service";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "1321";

    private static PayrollDBService instance;
    private Connection connection;

    private PayrollDBService() throws CustomDatabaseException {
        try {
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        } catch (SQLException e) {
            throw new CustomDatabaseException("Unable to establish database connection.", e);
        }
    }

    public static PayrollDBService getInstance() throws CustomDatabaseException {
        if (instance == null) {
            synchronized (PayrollDBService.class) {
                if (instance == null) {
                    instance = new PayrollDBService();
                }
            }
        }
        return instance;
    }

    public Map<String, GenderSalaryStats> getSalaryAnalysisByGender() throws CustomDatabaseException {
        Map<String, GenderSalaryStats> genderStatsMap = new HashMap<>();
        String sql = "SELECT gender,SUM(salary) AS total_salary, AVG(salary) AS avg_salary, MIN(salary) AS min_salary, MAX(salary) AS max_salary, COUNT(*) AS employee_count FROM employee_payroll GROUP BY gender ";

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            while (resultSet.next()) {
                String gender = resultSet.getString("gender");
                double totalSalary = resultSet.getDouble("total_salary");
                double avgSalary = resultSet.getDouble("avg_salary");
                double minSalary = resultSet.getDouble("min_salary");
                double maxSalary = resultSet.getDouble("max_salary");
                int employeeCount = resultSet.getInt("employee_count");

                GenderSalaryStats stats = new GenderSalaryStats(totalSalary, avgSalary, minSalary, maxSalary, employeeCount);
                genderStatsMap.put(gender, stats);
            }
        } catch (SQLException e) {
            throw new CustomDatabaseException("Error performing salary analysis by gender.", e);
        }

        return genderStatsMap;
    }
}

class GenderSalaryStats {
    private final double sum;
    private final double avg;
    private final double min;
    private final double max;
    private final int count;

    public GenderSalaryStats(double sum, double avg, double min, double max, int count) {
        this.sum = sum;
        this.avg = avg;
        this.min = min;
        this.max = max;
        this.count = count;
    }

    public double getSum() {
        return sum;
    }

    public double getAvg() {
        return avg;
    }

    public double getMin() {
        return min;
    }

    public double getMax() {
        return max;
    }

    public int getCount() {
        return count;
    }
}
