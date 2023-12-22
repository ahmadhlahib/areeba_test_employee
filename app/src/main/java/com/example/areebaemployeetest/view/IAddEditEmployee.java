package com.example.areebaemployeetest.view;

import com.example.areebaemployeetest.model.data.Employee;

import java.util.List;

public interface IAddEditEmployee {
    interface IView {
        void setEmployee(Employee employee);
        void goToMainPage();
    }
    interface IModel {
        Employee getEmployee(String uuid);
        void AddEditEmployee(Employee employee);
    }

    interface IPresenter {
        void AddEditEmployee(Employee employee);
    }
}
