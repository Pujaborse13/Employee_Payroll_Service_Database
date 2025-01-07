package com.EmployePayrollServiceDatabase;


import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

public class EmployeePayrollServiceTest {

    @Test
    public void testUpdateEmployeeSalary() throws CustomDatabaseException {
        EmployeePayrollService service = new EmployeePayrollService();
        String employeeName = "Terissa";
        double updatedSalary = 3000000.00;

        service.updateEmployeeSalary(employeeName, updatedSalary);

        EmployeePayroll employeePayroll = service.getEmployeePayrollByName(employeeName);

        assertNotNull(employeePayroll);
        assertEquals(employeeName, employeePayroll.name);
        assertEquals(updatedSalary, employeePayroll.salary);
    }
}
