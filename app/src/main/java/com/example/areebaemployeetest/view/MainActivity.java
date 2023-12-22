package com.example.areebaemployeetest.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.areebaemployeetest.R;
import com.example.areebaemployeetest.model.model.EmployeeModel;
import com.example.areebaemployeetest.view.adapters.RecyclerViewAdapter;
import com.example.areebaemployeetest.model.data.Employee;
import com.example.areebaemployeetest.presenter.EmployeePresenter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements IEmployee.IView {

    private IEmployee.IPresenter presenter;
    private RecyclerView recyclerView;
    private RecyclerViewAdapter adapter;
    private EditText searchEditText;
    private LinearLayout ll_baseLayout;
    private ImageView iv_delete;
    private TextView tv_add_employee,tv_delete_all;
    private Activity activity;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.activity=this;
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recyclerView);
        searchEditText = findViewById(R.id.search);
        ll_baseLayout=findViewById(R.id.ll_baseLayout);
        iv_delete=findViewById(R.id.iv_delete);
        tv_add_employee=findViewById(R.id.tv_add_employee);
        tv_delete_all=findViewById(R.id.tv_delete_all);
        // Initialize and set up the ListView and adapter here
        adapter=new RecyclerViewAdapter(this,new ArrayList<>());
        recyclerView.setLayoutManager(new LinearLayoutManager(this)); // Choose a layout manager
        recyclerView.setAdapter(adapter);
        presenter = new EmployeePresenter(this,this);
        if(adapter.getItemCount()>0)
            tv_delete_all.setText("Delete All");
        else
            tv_delete_all.setText("Get Data");
        iv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteSelectedEmployee();
            }
        });
        tv_delete_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(adapter.getItemCount()!=0)
                    deleteAllEmployee();
                else {
                    presenter.showMore(0);
                    if(adapter.getItemCount()>0)
                        tv_delete_all.setText("Delete All");
                }
            }
        });
        tv_add_employee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, AddEditEmployeeActivity.class);
                startActivity(intent);
            }
        });
        //Set up show more
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                // Load more data when the user reaches the end of the list
                if (!recyclerView.canScrollVertically(1) && searchEditText.getText().toString().isEmpty() && adapter.getItemCount()!=0) {
                    int currentListSize=adapter.getItemCount();
                    presenter.showMore(currentListSize);
                }
            }
        });
        // Set up a TextWatcher for the search EditText to trigger presenter.search(query) when text changes.
        searchEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                presenter.search(s.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        searchEditText.setText("");
        adapter=new RecyclerViewAdapter(this,new ArrayList<>());
        recyclerView.setLayoutManager(new LinearLayoutManager(this)); // Choose a layout manager
        recyclerView.setAdapter(adapter);
        IEmployee.IModel model=new EmployeeModel(this);
        presenter = new EmployeePresenter(this,this);
    }

    private void deleteSelectedEmployee() {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        builder.setTitle("Confirmation");
        builder.setMessage("Are you sure you want to delete selected employee?");

        // Add the Yes button
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                presenter.removeEmployees(adapter.selectedEmployees);
                adapter.removeSelectedEmployee();
                iv_delete.setVisibility(View.GONE);
            }
        });

        // Add the No button
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        // Create and show the alert dialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    private void deleteAllEmployee() {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        builder.setTitle("Confirmation");
        builder.setMessage("Are you sure you want to delete all employees?");

        // Add the Yes button
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                presenter.removeEmployees(adapter.getItems());
                adapter.removeAllEmployee();
                tv_delete_all.setText("Get Data");
            }
        });

        // Add the No button
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        // Create and show the alert dialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    @Override
    public void showList(List<Employee> list) {
        // Update the ListView adapter with the new list
        adapter.setList(list, new IEmployee.IOnSelectedEmployeeListener() {
            @Override
            public void OnEmployeeSelected(boolean selected) {
                if(selected)
                    iv_delete.setVisibility(View.VISIBLE);
                else
                    iv_delete.setVisibility(View.GONE);
            }

            @Override
            public void OnGettingDataComplete() {
                if(adapter.getItemCount()>0)
                    tv_delete_all.setText("Delete All");
                else
                    tv_delete_all.setText("Get Data");
            }
        });
    }

    @Override
    public void filter(String key) {
        adapter.filter(key);
    }
}