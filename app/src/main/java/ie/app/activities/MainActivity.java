package ie.app.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
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

import java.util.List;

import ie.app.api.DonationApi;
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


    public void donateButtonPressed (View view) {
        String method = paymentMethod.getCheckedRadioButtonId() == R.id.PayPal ? "PayPal" : "Direct";
        int donatedAmount = amountPicker.getValue();
        if (donatedAmount == 0)
        {
            String text = amountText.getText().toString();
            if (!text.equals(""))
                donatedAmount = Integer.parseInt(text);
        }
        if (donatedAmount > 0)
        {
            Donation _donation =  new Donation(donatedAmount, method, 0);
            newDonation(_donation);
            progressBar.setProgress(totalDonated);
            String totalDonatedStr = "$" + totalDonated;
            amountTotal.setText(totalDonatedStr);
            //------add
            Log.v("donation", String.valueOf(this));
            new InsertTask(this, _donation ).execute("/donations");
        }
    }

    @Override
    public void reset(MenuItem item)
    {
        //------------Box thong bao----------
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete ALL Donation?");
        builder.setIcon(android.R.drawable.ic_delete);
        builder.setMessage("Are you sure you want to Delete ALL the Donations ?");
        builder.setCancelable(false);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                new ResetTask(MainActivity.this).execute("/donations");
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public void onResume() {
        super.onResume();
        new GetAllTask(this).execute("/donations");
    }


    private class GetAllTask extends AsyncTask<String, Void, List<Donation>> {

        protected ProgressDialog dialog;
        protected Context context;
        SwipeRefreshLayout mSwipeRefreshLayout;


        public GetAllTask(Context context)
        {
            this.context = context;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            this.dialog = new ProgressDialog(context, 1);
            this.dialog.setMessage("Retrieving Donations List");
            this.dialog.show();
        }

        @Override
        protected List<Donation> doInBackground(String... params) {
            try {
                Log.v("donate", "Donation App Getting All Donations");
                return (List<Donation>) DonationApi.getAll((String) params[0]);
            }
            catch (Exception e) {
                Log.v("donate", "ERROR : " + e);
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(List<Donation> result) {
            super.onPostExecute(result);
        //use result to calculate the totalDonated amount here
            donations = result;
            int total = 0;
            for (int i = 0; i < result.size(); i++){
                total += result.get(i).amount;

            }
            totalDonated = total;
            progressBar.setProgress(totalDonated);
            amountTotal.setText("$" + totalDonated);

            if (dialog.isShowing())
                dialog.dismiss();
        }
    }

    private class InsertTask extends AsyncTask<Object, Void, String> {
        protected ProgressDialog dialog;
        protected Context context;
        protected Donation donation;
        public InsertTask(Context context, Donation donation)
        {
            this.context = context;
            this.donation = donation;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            this.dialog = new ProgressDialog(context, 1);
            this.dialog.setMessage("Saving Donation....");
            this.dialog.show();
        }
        @Override
        protected String doInBackground(Object... params) {
            String res = null;
            try {
                // Add donation into Server
                DonationApi.insert((String) params[0], this.donation);
                Log.v("donate", "Donation App Inserting");
            }
            catch(Exception e)
            {
                Log.v("donate","ERROR : " + e);
                e.printStackTrace();
            }
            return res;
        }
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if (dialog.isShowing())
                dialog.dismiss();

        }
    }

    private class ResetTask extends AsyncTask<Object, Void, String> {
        protected ProgressDialog  dialog;
        protected Context  context;
        public ResetTask(Context context)
        {
            this.context = context;
        }@Override
        protected void onPreExecute() {
            super.onPreExecute();
            this.dialog = new ProgressDialog(context, 1);
            this.dialog.setMessage("Deleting Donations....");
            this.dialog.show();
        }
        @Override
        protected String doInBackground(Object... params) {
            String res = null;
            try {
                Log.v("donation", "into doInBackground");
                res = DonationApi.deleteAll((String)params[0]);

            }
            catch(Exception e)
            {
                Log.v("donate"," RESET ERROR : " + e);
                e.printStackTrace();
            }
            return res;
        }
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            totalDonated = 0;
            progressBar.setProgress(totalDonated);
            amountTotal.setText("$" + totalDonated);
            if (dialog.isShowing())
                dialog.dismiss();
        }
    }

}
