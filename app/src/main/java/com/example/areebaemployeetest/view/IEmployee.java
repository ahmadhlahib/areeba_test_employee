package com.example.areebaemployeetest.view;

import com.example.areebaemployeetest.model.data.Employee;

import java.util.List;

public interface IEmployee {
    interface IView {
        void showList(List<Employee> employees);
        void filter(String key);
    }
    interface IModel {
        List<Employee> getEmployeeList();
        void callApiCallToGetData(String url, IOnGetResponseListener iOnGetResponseListener);
        void removeEmployees(List<Employee> selectedArray);
    }

    interface IPresenter {
        void search(String query);
        void showMore(int currentListSize);
        void removeEmployees(List<Employee> selectedArray);
    }

    interface IOnGetResponseListener {
        void onFinish(List<Employee> employeeList);
    }
    interface IOnSelectedEmployeeListener {
        void OnEmployeeSelected(boolean selected);
        void OnGettingDataComplete();
    }
}
