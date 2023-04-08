package com.example.majorassignment_2_studentinfo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mobsandgeeks.saripaar.Rule;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.*;

import java.util.List;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity implements Validator.ValidationListener {

    DatabaseHelper databaseHelper;

    @NotEmpty
    @Pattern(regex = "^[a-zA-Z\\s]*$", message = "Only alphabets are allowed")
    EditText et_name;

    @NotEmpty
    @Pattern(regex = "^\\d+$", message = "Only digits are allowed")
    EditText et_number;

    @NotEmpty
    @Email
    EditText et_email;

    @NotEmpty
    @Pattern(regex = "^[A-Z]{4}$", message = "Write Course Code of 4 letters and in Capital")
    EditText et_course;

    Button btn_add_student, btn_list_of_student;
    Validator validator;

    @Override
    public void onValidationSucceeded() {
        // Toast.makeText(this, "All validations are clear", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(this);
            // Display error messages
            if (view instanceof EditText) {
                ((EditText) view).setError(message);
            } else {
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Validation Setup
        validator = new Validator(this);
        validator.setValidationListener(this);

        // DatabaseHelper File
        databaseHelper = new DatabaseHelper(this);

        // Variables
        et_name = (EditText) findViewById(R.id.et_name);
        et_number = (EditText) findViewById(R.id.et_number);
        et_email = (EditText) findViewById(R.id.et_email);
        et_course = (EditText) findViewById(R.id.et_course);

        btn_add_student = (Button) findViewById(R.id.add_student);
        btn_list_of_student = (Button) findViewById(R.id.list_of_student);

        // Add Student on Button Click.
        btn_add_student.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                // Validating the data before adding.
                validator.validate();

                String name = et_name.getText().toString();
                String number = et_number.getText().toString();
                String email = et_email.getText().toString();
                String course = et_course.getText().toString();

                if(name.equals("") || email.equals("") || course.equals("") || number.equals("")) {
                    Toast.makeText(getApplicationContext(), "Fields Require", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    // Checking if email or student number already exists before adding.
                    Boolean checkEmailExist = databaseHelper.CheckEmailExist(email);
                    Boolean checkNumberExist = databaseHelper.CheckNumberExist(number);

                    if(!checkEmailExist && !checkNumberExist) {
                        Boolean insert = databaseHelper.Insert(name, number, email, course);
                        if(insert) {
                            Toast.makeText(getApplicationContext(), "Registered ", Toast.LENGTH_SHORT).show();
                            et_name.setText("");
                            et_number.setText("");
                            et_email.setText("");
                            et_course.setText("");
                        } else {
                            Toast.makeText(getApplicationContext(), "Error Inserting Data :(", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Email or Student Number Already Exists", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        btn_list_of_student.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, studentList.class);
                startActivity(intent);
            }
        });
    }
}