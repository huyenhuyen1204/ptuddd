package ie.app.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class Report extends AppCompatActivity {

    ListView listView;
    static final String[] numbers = new String[] {
            "Amount, Pay method",
            "10, Direct",
            "100, PayPal",
            "1000, Direct",
            "10, PayPal",
            "5000, PayPal"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        listView = findViewById(R.id.reportList);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, numbers);
        listView.setAdapter(adapter);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id){
            case R.id.menuReport :
                startActivity(new Intent(this, Report.class));
                break;
            case R.id.action_settings:
                return true;
            case R.id.menuDonate:
                startActivity(new Intent(this, MainActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }
}
