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
import com.example.areebaemployeetest.view.IEmployee;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class EmployeeModel implements IEmployee.IModel {
    public HelperUtils helper;
    public Activity activity;
    private IEmployeeDbService employeeDbService;
    public EmployeeModel(Activity activity) {
        this.activity=activity;
        helper=new HelperUtils(activity);
        employeeDbService=new EmployeeDbService(activity);
    }

    @Override
    public List<Employee> getEmployeeList() {
        List<Employee> employeeList= employeeDbService.getEmployeeFromDb("");
        return employeeList;
    }
    @Override
    public   void removeEmployees(List<Employee> selectedEmployee)
    {
        String employeeUidInString="";
        for (Employee employee : selectedEmployee) {
            employeeUidInString=employeeUidInString+"'"+employee.getLogin().getUuid()+"',";
        }
        employeeUidInString=employeeUidInString.substring(0,employeeUidInString.length()-1);
        employeeDbService.deleteEmployeeFromDb(employeeUidInString);
    }
    @Override
    public void callApiCallToGetData(String url, IEmployee.IOnGetResponseListener listener)
    {
        ProgressDialog progressDialog = null;
        progressDialog = helper.GetLoadingDialog(activity, "Fetching user data..");
        ProgressDialog finalProgressDialog = progressDialog;
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                finalProgressDialog.setCancelable(false);
                finalProgressDialog.show();
            }
        });

        StringRequest request = new StringRequest(url, new Response.Listener<String>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onResponse(String response) {
                if(finalProgressDialog!=null)
                    finalProgressDialog.dismiss();
                Root root=new Gson().fromJson(response, Root.class);
                if(root==null)
                    return;
                listener.onFinish(root.getResults());
                new AsyncTask<Void, Void, String>() {

                    @Override
                    protected void onPreExecute() {
                        super.onPreExecute();
                        // Runs on the UI thread before doInBackground
                    }

                    @Override
                    protected String doInBackground(Void... voids) {
                        // Runs in the background thread
                        // Perform background computation here
                        try {
                            List<Employee> employeeList=new ArrayList<>();
                            for (Employee employee : root.getResults()) {
                                Employee obj=new Employee(employee);
                                try {
                                    obj.setEmail(EncryptionUtils.encrypt(employee.getEmail()));
                                    Login login=obj.getLogin();
                                    login.setPassword(EncryptionUtils.encrypt(employee.getLogin().getPassword()));
                                    obj.setLogin(login);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                employeeList.add(obj);
                            }
                            Root rootEncrypted=new Root(employeeList,root.getInfo());
                            employeeDbService.insertEmployeeList(rootEncrypted.getResults());
                        }catch (Exception ex)
                        {
//                            Toast.makeText(activity, "Failed to cash data!!", Toast.LENGTH_LONG).show();
                            ex.printStackTrace();
                        }
                        return "AsyncTask Completed";
                    }

                    @Override
                    protected void onPostExecute(String result) {
                        super.onPostExecute(result);
                        // Runs on the UI thread after doInBackground
                    }
                }.execute();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if(finalProgressDialog!=null)
                    finalProgressDialog.dismiss();
                Toast.makeText(activity, "Connection error occurred!!", Toast.LENGTH_LONG).show();
                helper.showAlertDialog("Connection error occurred!!");
            }
        });
        RequestQueue rQueue = Volley.newRequestQueue(activity);
        rQueue.add(request);
    }
}
