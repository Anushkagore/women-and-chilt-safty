package com.example.womenchildsafetyapp;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log; // Import Log class

import java.util.ArrayList;
import java.util.List;

public class ShowTableRecordsActivity extends AppCompatActivity {

    private DBHelper dbHelper;
    private ListView listView;
    private String tableName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_table_records); // Create this layout

        listView = findViewById(R.id.listView); // Assuming you have a ListView in your layout
        dbHelper = new DBHelper(this);

        // Get the table name from the intent
        tableName = getIntent().getStringExtra("TABLE_NAME");

        if (tableName == null) {
            Log.e("ShowTableRecordsActivity", "Table name is null");
            Toast.makeText(this, "No table name provided", Toast.LENGTH_SHORT).show();
            finish(); // Close the activity because no table name was passed
            return;
        }

        // Get records from the selected table
        List<String> records = getTableRecords(tableName);
        Log.d("ShowTableRecordsActivity", "Records fetched for " + tableName + ": " + records.size());

        if (records.isEmpty()) {
            Toast.makeText(this, "No records found in " + tableName, Toast.LENGTH_SHORT).show();
        }

        // Set up the adapter to display the records
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, records);
        listView.setAdapter(adapter);
    }

    private List<String> getTableRecords(String tableName) {
        List<String> records = new ArrayList<>();
        // Fetch records based on the table name
        if (tableName.equals(DBHelper.TABLE_USERS)) {
            // Fetch user records
            records = dbHelper.getAllUsers(); // Implement this method in DBHelper
        } else if (tableName.equals(DBHelper.TABLE_EMERGENCY_CONTACTS)) {
            // Fetch emergency contact records
            records = dbHelper.getAllEmergencyContacts(); // Implement this method in DBHelper
        } else if (tableName.equals(DBHelper.TABLE_COMPLAINTS)) {
            // Fetch complaint records
            records = dbHelper.getAllComplaints(); // Implement this method in DBHelper
        }
        return records;
    }
}
