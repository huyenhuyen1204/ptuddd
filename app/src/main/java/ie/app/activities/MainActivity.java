package ie.app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import ie.app.models.Donation;

public class MainActivity extends Base {
    private Button donateButton;
    private RadioGroup paymentMethod;
    private ProgressBar progressBar;
    private NumberPicker amountPicker;
    private EditText amountText;
    private TextView amountTotal;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        donateButton = (Button) findViewById(R.id.donateButton);

        if (donateButton != null)
        {
            Log.v("Donate", "Really got the donate button");
        }

        paymentMethod = (RadioGroup)   findViewById(R.id.paymentMethod);
        progressBar   = (ProgressBar)  findViewById(R.id.progressBar);
        amountPicker  = (NumberPicker) findViewById(R.id.amountPicker);
        amountText    = (EditText)     findViewById(R.id.paymentAmount);
        amountTotal   = (TextView)     findViewById(R.id.totalSoFar);

        amountPicker.setMinValue(0);
        amountPicker.setMaxValue(1000);
        progressBar.setMax(10000);
        amountTotal.setText("$0");
    }


    public void donateButtonPressed (View view)
    {
//        String method = paymentMethod.getCheckedRadioButtonId() == R.id.PayPal ? "PayPal" : "Direct";
//
//        int donatedAmount =  amountPicker.getValue();
//        if (donatedAmount == 0)
//        {
//            String text = amountText.getText().toString();
//            if (!text.equals(""))
//                donatedAmount = Integer.parseInt(text);
//        }
//
//        if (!targetAchieved)
//        {
//            totalDonated  = totalDonated + donatedAmount;
//            targetAchieved = totalDonated >= 10000;
//            progressBar.setProgress(totalDonated);
//            String totalDonatedStr = "$" + totalDonated;
//            amountTotal.setText(totalDonatedStr);
//        }
//        else
//        {
//            Toast toast = Toast.makeText(this, "Target Exceeded!", Toast.LENGTH_SHORT);
//            toast.show();
//        }
//
//        Log.v("Donate", amountPicker.getValue() + " donated by " +  method + "\nCurrent total " + totalDonated);
        String method = paymentMethod.getCheckedRadioButtonId() == R.id.PayPal ?
                "PayPal" : "Direct";
        int donatedAmount = amountPicker.getValue();
//
//        if (!targetAchieved)
//        {
//            totalDonated  = totalDonated + donatedAmount;
//            targetAchieved = totalDonated >= 10000;
//            progressBar.setProgress(totalDonated);
//            String totalDonatedStr = "$" + totalDonated;
//            amountTotal.setText(totalDonatedStr);
//        }
//        else
//        {
//            Toast toast = Toast.makeText(this, "Target Exceeded!", Toast.LENGTH_SHORT);
//            toast.show();
//        }
        if (donatedAmount == 0)
        {
            String text = amountText.getText().toString();
            if (!text.equals(""))
                donatedAmount = Integer.parseInt(text);
        }
        if (donatedAmount > 0)
        {
            app.newDonation(new Donation(donatedAmount, method));
            progressBar.setProgress(app.totalDonated);
            String totalDonatedStr = "$" + app.totalDonated;
            amountTotal.setText(totalDonatedStr);

        }
    }
    @Override
    public void reset(MenuItem item) {
        app.dbManager.reset();
        progressBar.setProgress(0);
        amountPicker.setValue(0);
        paymentMethod.clearCheck();
        amountText.setText(null);
        amountTotal.setText("$0");
    }
}
