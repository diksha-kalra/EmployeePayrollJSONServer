package com.cg.employeepayroll;

import java.time.LocalDate;
import java.util.Objects;

public class EmployeePayrollData {
	public int id;
	public String name;
	public double salary;
	public LocalDate startDate;
	public char gender;
	public String[] dept_name;

	public EmployeePayrollData(int id, String name, double salary) {
		super();
		this.id = id;
		this.name = name;
		this.salary = salary;
	}

	public EmployeePayrollData(int id, String name, double salary, LocalDate startDate) {
		this(id, name, salary);
		this.startDate = startDate;
	}

	public EmployeePayrollData(int id, String name, double salary, LocalDate startDate, char gender) {
		this(id, name, salary, startDate);
		this.gender = gender;
	}
	
	public EmployeePayrollData(int id, String name, double salary, LocalDate startDate, char gender, String[] dept_name) {
		this(id, name, salary, startDate,gender);
		this.dept_name = dept_name;
	}

	@Override
	public String toString() {
		return "Employee Id: " + id + " Employee Name: " + name + " Employee Salary: " + salary+" startDate: "+startDate+" gender: "+gender;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(name,salary,startDate,gender);
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		EmployeePayrollData that = (EmployeePayrollData) o;
		return id == that.id && Double.compare(that.salary, salary) == 0 && name.equals(that.name);
	}
}
