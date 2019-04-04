package ie.app.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import ie.app.models.Donation;

public class Report extends Base {

    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_report);
//        listView = findViewById(R.id.reportList);
//        DonationAdapter donationAdapter = new DonationAdapter(this, (List<Donation>) listView);
//        listView.setAdapter((ListAdapter) donationAdapter);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        listView = (ListView) findViewById(R.id.reportList);
        DonationAdapter adapter = new DonationAdapter(this, app.dbManager.getAll());
        listView.setAdapter(adapter);
    }

}
