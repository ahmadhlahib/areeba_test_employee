package com.example.areebaemployeetest.presenter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.areebaemployeetest.dbServices.EmployeeDbService;
import com.example.areebaemployeetest.dbServices.IEmployeeDbService;
import com.example.areebaemployeetest.model.model.EmployeeModel;
import com.example.areebaemployeetest.utils.HelperUtils;
import com.example.areebaemployeetest.view.IEmployee;
import com.example.areebaemployeetest.model.data.Employee;

import java.util.ArrayList;
import java.util.List;

public class EmployeePresenter implements IEmployee.IPresenter {
    private IEmployee.IView view;
    private List<Employee> list; // Your list of results
    private IEmployee.IModel model;
    private HelperUtils helper;
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public EmployeePresenter(IEmployee.IView view, Activity activity) {
        this.view = view;
        this.model=new EmployeeModel(activity);
        helper=new HelperUtils(activity);
        SetupList();
    }

    private void SetupList() {
        List<Employee> employeeList=new ArrayList<>();
        employeeList= model.getEmployeeList();
        //IF first install and We dont have data in sqlite get from api
        if(employeeList.size()>0)
        {
            view.showList(employeeList);
        }
        else
        {
            String url="https://randomuser.me/api/?page=1&results=20";
            model.callApiCallToGetData(url, new IEmployee.IOnGetResponseListener() {
                @Override
                public void onFinish(List<Employee> employeeList) {
                    view.showList(employeeList);
                }
            });
        }
    }

    @Override
    public void search(String key) {
        view.filter(key);
    }

    @Override
    public void showMore(int currentListSize) {
        int dataToGetCount=currentListSize+20;
        String url="https://randomuser.me/api/?page=1&results="+dataToGetCount;
        model.callApiCallToGetData(url, new IEmployee.IOnGetResponseListener() {
            @Override
            public void onFinish(List<Employee> employeeList) {
                view.showList(employeeList);
            }
        });
    }
    @Override
    public void removeEmployees(List<Employee> selectedArray) {
        model.removeEmployees(selectedArray);
    }
}
