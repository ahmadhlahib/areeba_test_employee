package com.example.areebaemployeetest.presenter;

import android.app.Activity;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.areebaemployeetest.model.data.Employee;
import com.example.areebaemployeetest.model.model.AddEditEmployeeModel;
import com.example.areebaemployeetest.utils.HelperUtils;
import com.example.areebaemployeetest.view.IAddEditEmployee;
import com.example.areebaemployeetest.view.IEmployee;

import java.util.ArrayList;
import java.util.List;

public class AddEditEmployeePresenter implements IAddEditEmployee.IPresenter {
    private IAddEditEmployee.IView view;
    private Employee employee; // Your list of results
    private IAddEditEmployee.IModel model;
    private HelperUtils helper;
    private String uuid;
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public AddEditEmployeePresenter(IAddEditEmployee.IView view, Activity activity,String uuid) {
        this.view = view;
        this.model=new AddEditEmployeeModel(activity);
        this.uuid=uuid;
        helper=new HelperUtils(activity);
        SetupEmployee();
    }

    private void SetupEmployee() {
        Employee employee= model.getEmployee(uuid);
        if(employee!=null)
        {
            view.setEmployee(employee);
        }
    }

    @Override
    public void AddEditEmployee(Employee employee) {
        model.AddEditEmployee(employee);
        view.goToMainPage();
    }
}
