package com.example.demo;

public class SalaryCalculation {
    private static final double MANAGER_BASE_SALARY = 10000.0;
    private static final double SELLER_BASE_SALARY = 5000.0;

    private static final double MANAGER_BONUS = 50.0;
    private static final double SELLER_BONUS = 20.0;

    private String employeeType;
    private int extraHoursWorked;
    private int productsSold;

    public SalaryCalculation(String employeeType, int extraHoursWorked, int productsSold) {
        this.employeeType = employeeType;
        this.extraHoursWorked = extraHoursWorked;
        this.productsSold = productsSold;
    }

    public String getEmployeeType() {
        return employeeType;
    }

    public void setEmployeeType(String employeeType) {
        this.employeeType = employeeType;
    }

    public int getExtraHoursWorked() {
        return extraHoursWorked;
    }

    public void setExtraHoursWorked(int extraHoursWorked) {
        this.extraHoursWorked = extraHoursWorked;
    }

    public int getProductsSold() {
        return productsSold;
    }

    public void setProductsSold(int productsSold) {
        this.productsSold = productsSold;
    }

    public double PreTaxSalary() {
        double baseSalary;
        double bonusPerHour;
    
        if (employeeType.equalsIgnoreCase("manager")) {
            baseSalary = MANAGER_BASE_SALARY;
            bonusPerHour = MANAGER_BONUS;
        } else if (employeeType.equalsIgnoreCase("seller")) {
            baseSalary = SELLER_BASE_SALARY;
            bonusPerHour = SELLER_BONUS;
        } else {
            throw new IllegalArgumentException("Invalid employee type: " + employeeType);
        }
    
        double productBonus = 0.0;
        if (productsSold >= 1000 && productsSold <= 1500) {
            productBonus = 100.0;
        } else if (productsSold > 1500) {
            productBonus = 200.0;
        }

        double extraHoursBonus = extraHoursWorked * bonusPerHour;
    
        double preTaxSalary = baseSalary + productBonus + extraHoursBonus;
    
        return preTaxSalary;
    }
    
    public double posttaxSalary() {
        double preTaxSalary = PreTaxSalary();
        if (preTaxSalary <= 1000) {
            return preTaxSalary;
        } else if (preTaxSalary <= 1500) {
            return preTaxSalary * 0.84; 
        } else {
            return preTaxSalary * 0.82; 
        }
    }
}
