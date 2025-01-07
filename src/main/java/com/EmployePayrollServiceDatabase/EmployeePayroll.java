package com.EmployePayrollServiceDatabase;


import java.util.Date;

class EmployeePayroll {
    int id;
    String name;
    double salary;
    Date startDate;

    public EmployeePayroll(int id, String name, double salary, Date startDate) {
        this.id = id;
        this.name = name;
        this.salary = salary;
        this.startDate = startDate;
    }

    @Override
    public String toString() {
        return "EmployeePayroll{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", salary=" + salary +
                ", startDate=" + startDate +
                '}';
    }



    public double getSalary() {
        return salary;
    }
}


class CustomDatabaseException extends Exception {
    public CustomDatabaseException(String message, Throwable cause) {
        super(message, cause);
    }
}

