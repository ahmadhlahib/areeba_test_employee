package com.example.areebaemployeetest.dbServices;

import com.example.areebaemployeetest.model.data.Employee;

import java.util.List;

public interface IEmployeeDbService {
    List<Employee> getEmployeeFromDb(String Where);
    void insertEmployeeList(List<Employee> employeeList);
    void updateEployeeList(List<Employee> employeeList);

    void deleteEmployeeFromDb(String employeeUidInString);
}
