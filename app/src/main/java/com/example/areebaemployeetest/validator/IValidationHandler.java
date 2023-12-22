package com.example.areebaemployeetest.validator;

import android.app.Activity;

import com.example.areebaemployeetest.model.data.Employee;
import com.example.areebaemployeetest.model.data.ValidationResult;

public interface IValidationHandler {
    ValidationResult validate(Activity activity,Employee employee);
    void setNextHandler(IValidationHandler nextHandler);
}