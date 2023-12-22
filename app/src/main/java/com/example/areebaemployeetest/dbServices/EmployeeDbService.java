package com.example.areebaemployeetest.dbServices;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.areebaemployeetest.model.data.Coordinates;
import com.example.areebaemployeetest.model.data.Dob;
import com.example.areebaemployeetest.model.data.Employee;
import com.example.areebaemployeetest.model.data.Location;
import com.example.areebaemployeetest.model.data.Login;
import com.example.areebaemployeetest.model.data.Name;
import com.example.areebaemployeetest.model.data.Picture;
import com.example.areebaemployeetest.model.data.Street;
import com.example.areebaemployeetest.utils.DatabaseHelper;
import com.example.areebaemployeetest.utils.EncryptionUtils;
import com.example.areebaemployeetest.utils.HelperUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class EmployeeDbService implements IEmployeeDbService{

    private DatabaseHelper dbHelper;
    private SQLiteDatabase db;
    private HelperUtils helper;

    public EmployeeDbService(Activity activity) {
        dbHelper = new DatabaseHelper(activity);
        db =dbHelper.getWritableDatabase();
        db =dbHelper.getReadableDatabase();
        helper=new HelperUtils(activity);
    }

    @Override @SuppressLint("Range")
    public List<Employee> getEmployeeFromDb(String Where) {
        List<Employee> employeeList=new ArrayList<>();
        String query="Select * from employee "+Where;
        Cursor cursor =db.rawQuery(query, null);
        // Process the cursor and populate the employee list
        if (cursor != null && cursor.moveToFirst()) {
            do {
                String uuid= cursor.getString(cursor.getColumnIndex("uuid"));
                String title = cursor.getString(cursor.getColumnIndex("title"));
                String firstName= cursor.getString(cursor.getColumnIndex("firstName"));
                String lastName= cursor.getString(cursor.getColumnIndex("lastName"));
                String userName= cursor.getString(cursor.getColumnIndex("userName"));
                String password= cursor.getString(cursor.getColumnIndex("password"));
                String email = cursor.getString(cursor.getColumnIndex("email"));
                String dob = cursor.getString(cursor.getColumnIndex("dob"));
                int age = cursor.getInt(cursor.getColumnIndex("age"));
                String phone = cursor.getString(cursor.getColumnIndex("phone"));
                String cell = cursor.getString(cursor.getColumnIndex("cell"));
                String gender= cursor.getString(cursor.getColumnIndex("gender"));
                String picture= cursor.getString(cursor.getColumnIndex("picture"));
                String country= cursor.getString(cursor.getColumnIndex("country"));
                String city= cursor.getString(cursor.getColumnIndex("city"));
                String state= cursor.getString(cursor.getColumnIndex("state"));
                String streetName= cursor.getString(cursor.getColumnIndex("streetName"));
                Integer streetNumber= cursor.getInt(cursor.getColumnIndex("streetNumber"));
                String longitude= cursor.getString(cursor.getColumnIndex("longitude"));
                String lattitude= cursor.getString(cursor.getColumnIndex("lattitude"));
                Employee employee=new Employee();
                Name name=new Name(title,firstName,lastName);
                Street street=new Street(streetNumber,streetName);
                Coordinates coordinates=new Coordinates(lattitude,longitude);
                Location location=new Location(street,city,state,country,null,coordinates,null);
                employee.setLocation(location);
                employee.setName(name);
                employee.setLogin(new Login(uuid,userName,password,null,null,null,null));
                try {
                    employee.setEmail(EncryptionUtils.decrypt(email));
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                employee.setPhone(phone);
                employee.setCell(cell);
                employee.setDob(new Dob(dob,age));
                employee.setGender(gender);
                employee.setPicture(new Picture(picture,picture,picture));
                employeeList.add(employee);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return employeeList;
    }
    @Override
    public void deleteEmployeeFromDb(String employeeUidInString)
    {
        try {
            String query="delete  from employee Where UUID in ("+employeeUidInString+")";
            db.execSQL(query);
        }catch (Exception exception)
        {
            exception.printStackTrace();
        }
    }
    @Override
    public void insertEmployeeList(List<Employee> employeeList) {
        for (Employee employee : employeeList) {
            try {
                List<Employee> existingEmployee=getEmployeeFromDb("Where UUID='"+employee.getLogin().getUuid()+"'");
                if(existingEmployee.size()==0)
                {
                    ContentValues contentValues=new ContentValues();
                    contentValues.put("uuid",employee.getLogin().getUuid());
                    contentValues.put("title",employee.getName().getTitle());
                    contentValues.put("firstName",employee.getName().getFirst());
                    contentValues.put("lastName",employee.getName().getLast());
                    contentValues.put("userName",employee.getLogin().getUsername());
                    contentValues.put("password",employee.getLogin().getPassword());
                    contentValues.put("email",employee.getEmail());
                    contentValues.put("dob",employee.getDob().getDate());
                    contentValues.put("age",employee.getDob().getAge());
                    contentValues.put("phone",employee.getPhone());
                    contentValues.put("cell",employee.getCell());
                    contentValues.put("gender",employee.getGender());
                    if(employee.getPicture()!=null && employee.getPicture().getMedium()!=null)
                        contentValues.put("picture",employee.getPicture().getMedium());
                    else
                        contentValues.put("picture","");
                    contentValues.put("country",employee.getLocation().getCountry());
                    contentValues.put("state",employee.getLocation().getState());
                    contentValues.put("city",employee.getLocation().getCity());
                    contentValues.put("streetName",employee.getLocation().getStreet().getName());
                    contentValues.put("streetNumber",employee.getLocation().getStreet().getNumber());
                    contentValues.put("longitude",employee.getLocation().getCoordinates().getLongitude());
                    contentValues.put("lattitude",employee.getLocation().getCoordinates().getLatitude());
                    db.insertOrThrow(Employee.TableName,null,contentValues);
                }
            }catch (Exception exception)
            {
                helper.showAlertDialog(exception.getMessage());
                exception.printStackTrace();
            }
        }
    }
    @Override
    public void updateEployeeList(List<Employee> employeeList) {
        for (Employee employee : employeeList) {
            try {
                String WhereCondition=" UUID='"+employee.getLogin().getUuid()+"'";
                List<Employee> existingEmployee=getEmployeeFromDb("Where"+WhereCondition);
                if(existingEmployee.size()>0)
                {
                    ContentValues contentValues=new ContentValues();
                    contentValues.put("uuid",employee.getLogin().getUuid());
                    contentValues.put("title",employee.getName().getTitle());
                    contentValues.put("firstName",employee.getName().getFirst());
                    contentValues.put("lastName",employee.getName().getLast());
                    contentValues.put("userName",employee.getLogin().getUsername());
                    contentValues.put("password",employee.getLogin().getPassword());
                    contentValues.put("email",employee.getEmail());
                    contentValues.put("dob",employee.getDob().getDate());
                    contentValues.put("age",employee.getDob().getAge());
                    contentValues.put("phone",employee.getPhone());
                    contentValues.put("cell",employee.getCell());
                    contentValues.put("gender",employee.getGender());
                    contentValues.put("picture",employee.getPicture().getMedium());
                    contentValues.put("country",employee.getLocation().getCountry());
                    contentValues.put("state",employee.getLocation().getState());
                    contentValues.put("city",employee.getLocation().getCity());
                    contentValues.put("streetName",employee.getLocation().getStreet().getName());
                    contentValues.put("streetNumber",employee.getLocation().getStreet().getNumber());
                    contentValues.put("longitude",employee.getLocation().getCoordinates().getLongitude());
                    contentValues.put("lattitude",employee.getLocation().getCoordinates().getLatitude());
                    db.update(Employee.TableName, contentValues, "UUID" + " = ?", new String[]{employee.getLogin().getUuid()});

                }
            }catch (Exception exception)
            {
                helper.showAlertDialog(exception.getMessage());
                exception.printStackTrace();
            }
        }
    }
}
