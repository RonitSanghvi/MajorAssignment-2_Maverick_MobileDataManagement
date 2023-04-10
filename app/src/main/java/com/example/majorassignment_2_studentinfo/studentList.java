package com.example.majorassignment_2_studentinfo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class studentList extends AppCompatActivity {

     DatabaseHelper databaseHelper;
     ListView studentList;
     Button btn_fakeDataFetch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_list);

        databaseHelper = new DatabaseHelper(this);
        studentList = (ListView)findViewById(R.id.student_list);
        btn_fakeDataFetch = (Button)findViewById(R.id.btn_fakeDataFetch);

        ShowStudentsOnListView(databaseHelper);

        // To delete student info.
        studentList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                StudentModel clickedStudent = (StudentModel) adapterView.getItemAtPosition(position);
                String email = clickedStudent.getEmail();

                Boolean success = databaseHelper.deleteOne(email);
                ShowStudentsOnListView(databaseHelper);
                if(success) {
                    Toast.makeText(studentList.this, "Deleted" , Toast.LENGTH_SHORT).show();
                } else {Toast.makeText(studentList.this, "Not Deleted" , Toast.LENGTH_SHORT).show();}
            }
        });
    }

    // JSON data fetching from created using MockApi and added to the student info list.
    public void onClick(View view) {

        String url = "https://643099bcd4518cfb0e528c96.mockapi.io/studentList/";
        new AsyncHttpClient().get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String str = new String(responseBody);

                try {
                    JSONArray jsonArray = new JSONArray(str);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String name = jsonObject.getString("name");
                        String number = jsonObject.getString("number");
                        String email = jsonObject.getString("email");
                        String course = jsonObject.getString("course");
                        Boolean inserted = databaseHelper.Insert(name, number, email, course);
                        ShowStudentsOnListView(databaseHelper);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(studentList.this, "Problem in Adding Data", Toast.LENGTH_LONG).show();
            }
        });
    }

    // Renders the data from SQLite and shows it in the View.
    private void  ShowStudentsOnListView(DatabaseHelper dataBaseHelper) {
        ArrayAdapter studentArrayAdapter = new ArrayAdapter<StudentModel>(com.example.majorassignment_2_studentinfo.studentList.this, android.R.layout.simple_list_item_1, databaseHelper.getStudentList());
        studentList.setAdapter(studentArrayAdapter);
    }
}