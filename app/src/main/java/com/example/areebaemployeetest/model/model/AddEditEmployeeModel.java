package com.example.areebaemployeetest.model.model;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Build;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.areebaemployeetest.dbServices.EmployeeDbService;
import com.example.areebaemployeetest.dbServices.IEmployeeDbService;
import com.example.areebaemployeetest.model.data.Employee;
import com.example.areebaemployeetest.model.data.Login;
import com.example.areebaemployeetest.model.data.Root;
import com.example.areebaemployeetest.utils.EncryptionUtils;
import com.example.areebaemployeetest.utils.HelperUtils;
import com.example.areebaemployeetest.view.IAddEditEmployee;
import com.example.areebaemployeetest.view.IEmployee;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class AddEditEmployeeModel implements IAddEditEmployee.IModel {
    public HelperUtils helper;
    public Activity activity;
    private IEmployeeDbService employeeDbService;
    public AddEditEmployeeModel(Activity activity) {
        this.activity=activity;
        helper=new HelperUtils(activity);
        employeeDbService=new EmployeeDbService(activity);
    }

    @Override
    public Employee getEmployee(String uuid) {
        List<Employee> employeeList= employeeDbService.getEmployeeFromDb("Where UUID='"+uuid+"'");
        if(employeeList.size()>0)
            return employeeList.get(0);
        return null;
    }

    @Override
    public void AddEditEmployee(Employee employee) {
        List<Employee> employeeList=new ArrayList<>();
        employeeList.add(employee);
        if(getEmployee(employee.getLogin().getUuid())==null)
            employeeDbService.insertEmployeeList(employeeList);
        else
        {
            //Update Employee
            employeeDbService.updateEployeeList(employeeList);
        }
    }
}
