package com.example.areebaemployeetest.validator;

import android.app.Activity;

import com.example.areebaemployeetest.dbServices.EmployeeDbService;
import com.example.areebaemployeetest.dbServices.IEmployeeDbService;
import com.example.areebaemployeetest.model.data.Employee;
import com.example.areebaemployeetest.model.data.ValidationResult;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class DateValidationHandler implements IValidationHandler {
    private IValidationHandler nextHandler;

    @Override
    public ValidationResult validate(Activity activity, Employee employee) {
        ValidationResult validationResult=new ValidationResult();
        String dateFormat = "yyyy-MM-dd";

        if (!isDateValid(employee.getDob().getDate(), dateFormat)) {
            // Handle validation failure
            validationResult.setFormValid(false);
            validationResult.setErrorMessage("Please Choose Date");
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

    public static boolean isDateValid(String dateString, String dateFormat) {
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        sdf.setLenient(false); // Disable lenient parsing

        try {
            // Parse the input string as a date
            Date date = sdf.parse(dateString);
            return date != null;
        } catch (ParseException e) {
            // The input string is not a valid date
            return false;
        }
    }

    @Override
    public void setNextHandler(IValidationHandler nextHandler) {
        this.nextHandler = nextHandler;
    }
}
