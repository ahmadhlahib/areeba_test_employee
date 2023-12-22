package com.example.areebaemployeetest.validator;

import android.app.Activity;

import com.example.areebaemployeetest.model.data.Employee;
import com.example.areebaemployeetest.model.data.ValidationResult;
import com.example.areebaemployeetest.utils.EncryptionUtils;

public class EmptyValidationHandler implements IValidationHandler {
    private IValidationHandler nextHandler;

    @Override
    public ValidationResult validate(Activity activity,Employee employee) {
        ValidationResult validationResult=new ValidationResult();
        try {
            if (employee.getName().getTitle().isEmpty()
                || employee.getName().getFirst().isEmpty()
                     || employee.getName().getLast().isEmpty()
                    || employee.getLogin().getUsername().isEmpty()
                    || employee.getLogin().getPassword().isEmpty()
                    || employee.getGender().isEmpty()
                    || EncryptionUtils.decrypt(employee.getEmail()).isEmpty()) {
                // Handle validation failure
                validationResult.setFormValid(false);
                validationResult.setErrorMessage("Fill All Mandatory Field!");
                return validationResult;
            } else if (nextHandler != null) {
                // Delegate to the next handler in the chain
                return nextHandler.validate(activity,employee);
            } else {
                // All validations passed
                validationResult.setFormValid(true);
                return validationResult;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void setNextHandler(IValidationHandler nextHandler) {
        this.nextHandler = nextHandler;
    }
}
