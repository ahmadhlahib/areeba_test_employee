package com.example.areebaemployeetest.validator;

import android.app.Activity;

import com.example.areebaemployeetest.dbServices.EmployeeDbService;
import com.example.areebaemployeetest.dbServices.IEmployeeDbService;
import com.example.areebaemployeetest.model.data.Employee;
import com.example.areebaemployeetest.model.data.ValidationResult;

import java.util.List;

public class UserNameValidationHandler implements IValidationHandler {
    private IValidationHandler nextHandler;

    @Override
    public ValidationResult validate(Activity activity,Employee employee) {
        ValidationResult validationResult=new ValidationResult();
        IEmployeeDbService employeeDbService=new EmployeeDbService(activity);
        List<Employee> existingEmployeeList=employeeDbService.getEmployeeFromDb("Where Username='"+employee.getLogin().getUsername()+"'");
        if (existingEmployeeList.size()>0) {
            // Handle validation failure
            validationResult.setFormValid(false);
            validationResult.setErrorMessage("Username Exist");
            return validationResult;
        } else if (nextHandler != null) {
            // Delegate to the next handler in the chain
            return nextHandler.validate(activity,employee);
        } else {
            // All validations passed
            validationResult.setFormValid(true);
            return validationResult;
        }
    }

    @Override
    public void setNextHandler(IValidationHandler nextHandler) {
        this.nextHandler = nextHandler;
    }
}
