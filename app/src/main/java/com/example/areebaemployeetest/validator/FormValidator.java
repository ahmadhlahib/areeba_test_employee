package com.example.areebaemployeetest.validator;

import android.app.Activity;

import com.example.areebaemployeetest.model.data.Employee;
import com.example.areebaemployeetest.model.data.ValidationResult;

public class FormValidator {
    private IValidationHandler emptyValidationChain;
    private IValidationHandler usernameValidationChain;

    public FormValidator(boolean isUserNameValidate) {
        // Create the chain of responsibility
        emptyValidationChain = new EmptyValidationHandler();
        usernameValidationChain=new UserNameValidationHandler();
        if(isUserNameValidate)
        {
            emptyValidationChain.setNextHandler(usernameValidationChain);
            usernameValidationChain.setNextHandler(new DateValidationHandler());
        }
        else {
            emptyValidationChain.setNextHandler(new DateValidationHandler());
        }
    }

    public ValidationResult validateForm(Activity activity,Employee employee) {
        // Start the validation chain
        return emptyValidationChain.validate(activity,employee);
    }
}
