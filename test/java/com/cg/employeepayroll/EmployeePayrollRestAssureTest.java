package com.cg.employeepayroll;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.logging.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import com.cg.employeepayroll.EmployeePayrollService.IOService;
import com.google.gson.Gson;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class EmployeePayrollRestAssureTest {
	Logger log = Logger.getLogger(EmployeePayrollRestAssureTest.class.getName());

	@Before
	public void setUp() {
		RestAssured.baseURI = "http://localhost";
		RestAssured.port = 3000;
	}

	public EmployeePayrollData[] getEmployeeList() {
		Response response = RestAssured.get("/employees");
		log.info("Employee payroll entries in JSON Server :\n" + response.asString());
		EmployeePayrollData[] arrayOfEmployees = new Gson().fromJson(response.asString(), EmployeePayrollData[].class);
		return arrayOfEmployees;
	}

	public Response addEmployeeToJsonServer(EmployeePayrollData employeePayrollData) {
		String empJson = new Gson().toJson(employeePayrollData);
		RequestSpecification request = RestAssured.given();
		request.header("Content-Type", "application/json");
		request.body(empJson);
		return request.post("/employees");
	}

	@Test
	public void givenNewEmployee_WhenAdded__ShouldMatch() throws PayrollSystemException {
		EmployeePayrollService employeePayrollService;
		EmployeePayrollData[] arrayOfEmployees = getEmployeeList();
		employeePayrollService = new EmployeePayrollService(Arrays.asList(arrayOfEmployees));
		EmployeePayrollData employeePayrollData = null;
		employeePayrollData = new EmployeePayrollData(0, "Manish", 3000000.00, LocalDate.now(), 'M');
		Response response = addEmployeeToJsonServer(employeePayrollData);
		int statusCode = response.getStatusCode();
		Assert.assertEquals(201, statusCode);
		employeePayrollData = new Gson().fromJson(response.asString(), EmployeePayrollData.class);
		employeePayrollService.addEmployeePayroll(employeePayrollData, IOService.REST_IO);
		long entries = employeePayrollService.countEntries(IOService.REST_IO);
		Assert.assertEquals(6, entries);
	}
	
	@Test
	public void givenListOfEmployees_WhenAdded_ShouldMatch201ResponseCode() throws PayrollSystemException {
		EmployeePayrollService employeePayrollService;
		EmployeePayrollData[] arrayOfEmployees = getEmployeeList();
		employeePayrollService = new EmployeePayrollService(Arrays.asList(arrayOfEmployees));
		EmployeePayrollData[] arrayOfEmpPayrolls = {
				new EmployeePayrollData(0, "Sunder", 6000000.00, LocalDate.now(), 'M'),
				new EmployeePayrollData(0, "Mukesh", 4000000.00, LocalDate.now(), 'M'),
				new EmployeePayrollData(0, "Anil", 5000000.00, LocalDate.now(), 'M') };
		for (EmployeePayrollData employeePayrollData : arrayOfEmpPayrolls) {
			Response response = addEmployeeToJsonServer(employeePayrollData);
			int statusCode = response.getStatusCode();
			Assert.assertEquals(201, statusCode);
			employeePayrollData = new Gson().fromJson(response.asString(), EmployeePayrollData.class);
			employeePayrollService.addEmployeePayroll(employeePayrollData, IOService.REST_IO);
		}
		long entries = employeePayrollService.countEntries(IOService.REST_IO);
		Assert.assertEquals(5, entries);
	}
	
	@Test
	public void givenNewSalaryForEmployee_WhenUpdated_ShouldMatch200Response() {
		EmployeePayrollService employeePayrollService;
		EmployeePayrollData[] arrayOfEmployees = getEmployeeList();
		employeePayrollService = new EmployeePayrollService(Arrays.asList(arrayOfEmployees));
		employeePayrollService.updateEmployeeSalary("Jeff Bezos", 6000000.0, IOService.REST_IO);
		EmployeePayrollData employeePayrollData = employeePayrollService.getEmployeePayrollData("Jeff Bezos");
		String empJson = new Gson().toJson(employeePayrollData);
		RequestSpecification request = RestAssured.given();
		request.header("Content-Type", "application/json");
		request.body(empJson);
		Response response = request.put("/employees/" + employeePayrollData.id);
		int statusCode = response.getStatusCode();
		Assert.assertEquals(200, statusCode);
	}
}
