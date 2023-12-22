package com.example.areebaemployeetest.view;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import com.example.areebaemployeetest.R;
import com.example.areebaemployeetest.model.data.Coordinates;
import com.example.areebaemployeetest.model.data.Dob;
import com.example.areebaemployeetest.model.data.Employee;
import com.example.areebaemployeetest.model.data.Location;
import com.example.areebaemployeetest.model.data.Login;
import com.example.areebaemployeetest.model.data.Name;
import com.example.areebaemployeetest.model.data.Street;
import com.example.areebaemployeetest.model.data.ValidationResult;
import com.example.areebaemployeetest.presenter.AddEditEmployeePresenter;
import com.example.areebaemployeetest.utils.EncryptionUtils;
import com.example.areebaemployeetest.utils.HelperUtils;
import com.example.areebaemployeetest.validator.FormValidator;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

public class AddEditEmployeeActivity extends AppCompatActivity implements IAddEditEmployee.IView {
    private IAddEditEmployee.IPresenter presenter;
    private Activity activity;
    private EditText et_title, et_first, et_last, et_username, et_password, et_email, et_dob, et_lattitude, et_longitude,
            et_country, et_street_name,et_street_number,et_city,et_state,et_phone,et_cell;
    private Button btn_save;
    private Spinner sp_gender;
    private DatePicker dob_date_picker;
    private String employee_id;
    private Employee employee;
    private FusedLocationProviderClient fusedLocationClient;
    private HelperUtils helper;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.activity = this;
        helper=new HelperUtils(activity);
        Intent intent = getIntent();
        this.employee_id = intent.getStringExtra("UUID");
        setContentView(R.layout.activity_add_edit_employee);
        SetupView();
    }

    private void SetupView() {
        et_title = findViewById(R.id.et_title);
        et_first = findViewById(R.id.et_first);
        et_last = findViewById(R.id.et_last);
        et_username = findViewById(R.id.et_username);
        et_password = findViewById(R.id.et_password);
        et_email = findViewById(R.id.et_email);
        et_dob = findViewById(R.id.et_dob);
        et_country = findViewById(R.id.et_country);
        et_street_name = findViewById(R.id.et_street_name);
        et_street_number = findViewById(R.id.et_street_number);
        et_city = findViewById(R.id.et_city);
        et_state = findViewById(R.id.et_state);
        et_phone = findViewById(R.id.et_phone);
        et_cell = findViewById(R.id.et_cell);
        sp_gender = findViewById(R.id.sp_gender);
        et_lattitude = findViewById(R.id.et_lattitude);
        et_longitude = findViewById(R.id.et_longitude);
        dob_date_picker = findViewById(R.id.dob_date_picker);
        btn_save = findViewById(R.id.btn_save);
        Calendar calendar = Calendar.getInstance();
        dob_date_picker.init(
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH),
                null
        );
        String[] spinnerData = {"male", "female"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.custom_spinner_item, spinnerData) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                return createCustomView(position, convertView, parent);
            }

            @Override
            public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                return createCustomView(position, convertView, parent);
            }
            private View createCustomView(int position, View convertView, ViewGroup parent) {
                // Inflate the custom layout for the spinner item
                LayoutInflater inflater = getLayoutInflater();
                View customView = inflater.inflate(R.layout.custom_spinner_item, parent, false);

                // Customize the view elements based on your data
                LinearLayout llBase = customView.findViewById(R.id.ll_base);
                ImageView icon = customView.findViewById(R.id.icon);
                TextView text = customView.findViewById(R.id.text);
                if(position==0)
                {
                    icon.setImageResource(R.drawable.male);
                    llBase.setBackgroundResource(R.drawable.spinner_shape_border);
                }
                else
                {
                    icon.setImageResource(R.drawable.female);
                    llBase.setBackgroundResource(R.drawable.spinner_female_shape_border);
                }
                text.setText(spinnerData[position]);
                return customView;
            }
        };
        adapter.setDropDownViewResource(R.layout.custom_spinner_item);
        sp_gender.setAdapter(adapter);

        et_dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog(view);
            }
        });
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddEditEmployee();
            }
        });
        presenter = new AddEditEmployeePresenter(this, this, employee_id);
        if(employee==null)
            getCoordinate();
        else
        {
            et_lattitude.setText(employee.getLocation().getCoordinates().getLatitude());
            et_longitude.setText(employee.getLocation().getCoordinates().getLongitude());
        }
    }

    private void AddEditEmployee() {
        Employee newEmployee = new Employee();
        String uuid = UUID.randomUUID().toString();
        if (employee != null) {
            newEmployee = employee;
            uuid = employee.getLogin().getUuid();
        }
        Name name = new Name(et_title.getText().toString(), et_first.getText().toString(), et_last.getText().toString());
        Login login = new Login(uuid, et_username.getText().toString(), EncryptionUtils.encrypt(et_password.getText().toString()), null, null, null, null);
        newEmployee.setName(name);
        newEmployee.setLogin(login);
        String gender=null;
        if(sp_gender.getSelectedItem()!=null)
            gender=sp_gender.getSelectedItem().toString();
        newEmployee.setGender(gender);
        newEmployee.setEmail(EncryptionUtils.encrypt(et_email.getText().toString()));
        Integer streetNumber=0;
        try {
            streetNumber=Integer.parseInt(et_street_number.getText().toString());
        }catch (Exception exception)
        {
            exception.printStackTrace();
        }
        newEmployee.setLocation(new Location(new Street( streetNumber, et_street_name.getText().toString()),
                et_city.getText().toString(), et_state.getText().toString(), et_country.getText().toString(), ""
                , new Coordinates(et_lattitude.getText().toString(),et_longitude.getText().toString()), null));
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        try {
            newEmployee.setPhone(et_phone.getText().toString());
            newEmployee.setCell(et_cell.getText().toString());
//            newEmployee.setPicture(new Picture("", "", ""));
            newEmployee.setDob(new Dob(et_dob.getText().toString(),0));
            boolean isUserNameValidate=employee == null;
            FormValidator formValidator = new FormValidator(isUserNameValidate);
            ValidationResult result = formValidator.validateForm(activity,newEmployee);
            if (result.isFormValid()) {
                Date dob = dateFormat.parse(et_dob.getText().toString());
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    newEmployee.setDob(new Dob(et_dob.getText().toString(), calculateAge(convertToLocalDate(dob), LocalDate.now())));
                }
                // Proceed with adding the employee
                presenter.AddEditEmployee(newEmployee);
            } else {
                // Show an error message or handle the validation failure
                helper.showAlertDialog(result.getErrorMessage());
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void getCoordinate() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        // Check if the app has permission to access the device's location
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED) {

            // Permission is granted, get the last known location
            getLastLocation();
        } else {
            // Request location permissions
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    123);
        }
    }

    @SuppressLint("MissingPermission")
    private void getLastLocation() {
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<android.location.Location>() {
                    @Override
                    public void onSuccess(android.location.Location location) {
                        if (location != null) {
                            double latitude = location.getLatitude();
                            double longitude = location.getLongitude();
                            et_lattitude.setText(latitude+"");
                            et_longitude.setText(longitude+"");
                        } else {
                            et_lattitude.setText("0");
                            et_longitude.setText("0");
                        }
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(activity, "Error getting location", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // Handle the result of the location permission request
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 123) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, get the last known location
                getLastLocation();
            } else {
                Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private static LocalDate convertToLocalDate(Date date) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        }
        return null;
    }
    private static int calculateAge(LocalDate birthdate, LocalDate currentDate) {
        if ((birthdate != null) && (currentDate != null)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                return Period.between(birthdate, currentDate).getYears();
            }
        } else {
            return 0; // Handle null inputs as needed
        }
        return 0;
    }
    public void showDatePickerDialog(View view) {
        // Create a DatePickerDialog with the current date as the default date
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String selectedDate =  year + "-" + (month + 1) + "-" + dayOfMonth;
                        et_dob.setText(selectedDate);
                    }
                },
                dob_date_picker.getYear(),
                dob_date_picker.getMonth(),
                dob_date_picker.getDayOfMonth()
        );
        // Show the DatePickerDialog
        datePickerDialog.show();
    }

    @Override
    public void setEmployee(Employee employee) {
        this.employee=employee;
        et_title.setText(employee.getName().getTitle());
        et_first.setText(employee.getName().getFirst());
        et_last.setText(employee.getName().getLast());
        et_username.setText(employee.getLogin().getUsername());
        et_email.setText(employee.getEmail());
        try {
            et_password.setText(EncryptionUtils.decrypt(employee.getLogin().getPassword()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        et_dob.setText(employee.getDob().getDate());
        et_country.setText(employee.getLocation().getCountry());
        et_city.setText(employee.getLocation().getCity());
        et_state.setText(employee.getLocation().getState());
        et_street_name.setText(employee.getLocation().getStreet().getName());
        et_street_number.setText(employee.getLocation().getStreet().getNumber()+"");
        et_phone.setText(employee.getPhone());
        et_cell.setText(employee.getCell());
        int position=0;
        if(employee.getGender().equalsIgnoreCase("male"))
            position=0;
        else
            position=1;
        sp_gender.setSelection(position);
    }
    @Override
    public void goToMainPage() {
        finish(); // Finish the current activity to remove it from the back stack
    }
}
