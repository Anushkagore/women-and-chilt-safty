package com.example.womenchildsafetyapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class ShowTablesActivity extends AppCompatActivity {

    private DBHelper dbHelper;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_tables); // Make sure to create this layout

        listView = findViewById(R.id.listView); // Assuming you have a ListView in your layout
        dbHelper = new DBHelper(this);

        // Get all table names
        List<String> tableNames = dbHelper.getAllTableNames();

        // Set up the adapter to display the table names
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, tableNames);
        listView.setAdapter(adapter);

        // Set an item click listener
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedTable = tableNames.get(position);
                Intent intent = new Intent(ShowTablesActivity.this, ShowTableRecordsActivity.class);
                intent.putExtra("TABLE_NAME", selectedTable);
                startActivity(intent);
            }
        });
    }
}
