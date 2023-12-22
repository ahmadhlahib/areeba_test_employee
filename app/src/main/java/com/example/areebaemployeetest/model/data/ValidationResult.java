package com.example.areebaemployeetest.model.data;

public class ValidationResult {
    private String errorMessage;
    private boolean isFormValid;

    public ValidationResult(String errorMessage, boolean isFormValid) {
        this.errorMessage = errorMessage;
        this.isFormValid = isFormValid;
    }

    public ValidationResult() {

    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public boolean isFormValid() {
        return isFormValid;
    }

    public void setFormValid(boolean formValid) {
        isFormValid = formValid;
    }
}
