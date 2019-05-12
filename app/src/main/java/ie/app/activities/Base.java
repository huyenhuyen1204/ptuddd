package ie.app.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ie.app.models.Donation;

public abstract class Base extends AppCompatActivity {
        public final int target = 1000;
    public int totalDonated = 0;
    public static List<Donation> donations = new ArrayList<>();

    public boolean newDonation(Donation donation) {
        boolean targetAchieved = totalDonated > target;
        if (!targetAchieved) {
            donations.add(donation);
            totalDonated += donation.amount;
        } else {
            Toast toast = Toast.makeText(this, "Target Exceeded!", Toast.LENGTH_SHORT);
            toast.show();
        }
        return targetAchieved;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        MenuItem report = menu.findItem(R.id.menuReport);
        MenuItem donate = menu.findItem(R.id.menuDonate);
        if (donations.isEmpty())
            report.setEnabled(false);
        else
            report.setEnabled(true);
        if (this instanceof MainActivity) {
            donate.setVisible(false);
            if (!donations.isEmpty())
                report.setVisible(true);
        } else {
            report.setVisible(false);
            donate.setVisible(true);
        }
        return true;
    }


    public void report(MenuItem item) {
        startActivity(new Intent(this, Report.class));
    }

    public void donate(MenuItem item) {
        startActivity(new Intent(this, MainActivity.class));
    }
    public void reset(MenuItem item) {}
}

