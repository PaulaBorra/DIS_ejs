package com.example.demo;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
public class SalaryController {
  
  @PostMapping("preTax")
  public Double preTax(@RequestBody SalaryCalculation entity) {
    return entity.PreTaxSalary();
  }

  @PostMapping("postTax")
  public Double postTax(@RequestBody SalaryCalculation entity) {
    return entity.posttaxSalary();
  }
}