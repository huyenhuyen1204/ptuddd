package ie.app.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ie.app.api.DonationApi;
import ie.app.models.Donation;

public class Report extends Base implements View.OnClickListener, AdapterView.OnItemClickListener {

    ListView listView;
    SwipeRefreshLayout mSwipeRefreshLayout;
    DonationAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        listView = (ListView) findViewById(R.id.reportList);
        mSwipeRefreshLayout = (SwipeRefreshLayout)
                findViewById(R.id.report_swipe_refresh_layout);
        new GetAllTask(this).execute("/donations");
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
             public void onRefresh() {
                 new GetAllTask(Report.this).execute("/donations");
             }
         });
    }


    @Override
    public void onClick(View view) {
        if (view.getTag() instanceof Donation) {
            onDonationDelete((Donation) view.getTag());
        }
    }

    public void onDonationDelete(final Donation donation) {
        String stringId = donation._id;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete Donation?");
        builder.setIcon(android.R.drawable.ic_delete);
        builder.setMessage("Are you sure you want to Delete the \'Donation with ID\' \n [ " + stringId + " ] ?");
        builder.setCancelable(false);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                new DeleteTask(Report.this).execute("/donations", donation._id);
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
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View row, int position, long id) {
        new GetTask(this).execute("/donations", row.getTag().toString());
    }


    public class GetAllTask extends AsyncTask<String, Void, List<Donation>> implements AdapterView.OnItemClickListener {
        protected ProgressDialog dialog;
        protected Context context;

        public GetAllTask(Context context) {
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
                return (List<Donation>) DonationApi.getAll((String) params[0]);
            } catch (Exception e) {
                Log.v("ASYNC", "ERROR : " + e);
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<Donation> result) {
            super.onPostExecute(result);
            donations = result;
            adapter = new DonationAdapter(context, donations);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(this);
            mSwipeRefreshLayout.setRefreshing(false);
            if (dialog.isShowing())
                dialog.dismiss();
        }


        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        }
    }
    @Override
    public void reset(MenuItem item) {
        //------------Box thong bao----------
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete ALL Donation?");
        builder.setIcon(android.R.drawable.ic_delete);
        builder.setMessage("Are you sure you want to Delete ALL the Donations ?");
        builder.setCancelable(false);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                new ResetTask(Report.this).execute("/donations");
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private class GetTask extends AsyncTask<String, Void, Donation> {
        protected ProgressDialog dialog;
        protected Context context;
        public GetTask(Context context) {
            this.context = context;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            this.dialog = new ProgressDialog(context, 1);
            this.dialog.setMessage("Retrieving Donation Details");
            this.dialog.show();
        }
        @Override
        protected Donation doInBackground(String... params) {
            try {
                return (Donation) DonationApi.get((String) params[0], (String)
                        params[1]);
            } catch (Exception e) {
                Log.v("donate", "ERROR : " + e);
                e.printStackTrace();
            }return null;
        }
        @Override
        protected void onPostExecute(Donation result) {
            super.onPostExecute(result);
            Donation donation = result;
            Toast.makeText(Report.this, "Donation Data [ " + donation.upvotes +
                    "]\n " +
                    "With ID of [" + donation._id + "]", Toast.LENGTH_LONG).show();
            if (dialog.isShowing())
                dialog.dismiss();
        }
    }
    // ----------- Delete task
    private class DeleteTask extends AsyncTask<String, Void, String> {
        protected ProgressDialog dialog;
        protected Context context;
        public DeleteTask(Context context) {
            this.context = context;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            this.dialog = new ProgressDialog(context, 1);
            this.dialog.setMessage("Deleting Donation");
            this.dialog.show();
        }
        @Override
        protected String doInBackground(String... params) {try {
            return (String) DonationApi.delete((String) params[0], (String)
                    params[1]);
        } catch (Exception e) {
            Log.v("donate", "ERROR : " + e);
            e.printStackTrace();
        }
            return null;
        }
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            String s = result;
            Log.v("donate", "DELETE REQUEST : " + s);
            new GetAllTask(Report.this).execute("/donations");
            if (dialog.isShowing())
                dialog.dismiss();
        }
    }

    class DonationAdapter extends ArrayAdapter<Donation> {
        private Context context;
        public List<Donation> donations;
        public DonationAdapter(Context context, List<Donation> donations) {
            super(context, R.layout.row_donate, donations);
            this.context = context;
            this.donations = donations;
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.row_donate, parent, false);
            Donation donation = donations.get(position);
            ImageView imgDelete = (ImageView) view.findViewById(R.id.imgDelete);
            imgDelete.setTag(donation);
            imgDelete.setOnClickListener(Report.this);
            TextView amountView = (TextView)
                    view.findViewById(R.id.row_amount);
            TextView methodView = (TextView)
                    view.findViewById(R.id.row_method);
            TextView upvotesView = (TextView)
                    view.findViewById(R.id.row_upvotes);
            amountView.setText("" + donation.amount);
            methodView.setText(donation.paymenttype);
            upvotesView.setText("" + donation.upvotes);
            view.setTag(donation._id); // setting the 'row' id to the id of the donation
            return view;
        }@Override
        public int getCount() {
            return donations.size();
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
            new GetAllTask(Report.this).execute("/donations");
            if (dialog.isShowing())
                dialog.dismiss();
        }
    }

}
