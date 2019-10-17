package uk.ac.brookes.alexrussell.pepperdashboardproto;

import android.app.DialogFragment;
import android.arch.lifecycle.Observer;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;

import uk.ac.brookes.alexrussell.pepperdashboard.DashboardView;
import uk.ac.brookes.alexrussell.pepperdashboard.NoticeDialogFragment;
import uk.ac.brookes.alexrussell.pepperdashboard.UpdateType;

/**
 * DashboardActivity mimics the Activity or Fragment that will display the PEPPER dashboard visualization.
 *
 */
 @RequiresApi(api = Build.VERSION_CODES.KITKAT)
public class DashboardActivity extends AppCompatActivity implements NoticeDialogFragment.NoticeDialogListener {

    DashboardDataRep dboardDataModel;
    WebView webView;
    DashboardView dashboardView;

    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        webView = (WebView) findViewById(R.id.webView);
        dboardDataModel = new DashboardDataRep(this);
        dashboardView = new DashboardView(this, webView, dboardDataModel);
        dboardDataModel.getUpdateType().observe(this, new Observer<UpdateType>() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onChanged(@Nullable UpdateType updateType) {
                try {
                    dashboardView.generalUpdate(true);
                }
                catch (Exception e) {
                    Log.v("tag", e.getMessage());
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_bar, menu);
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void onDialogPositiveClick(DialogFragment dialog, int selectedNumber) {
        dashboardView.setTimeRange(selectedNumber);
    }

    public void onDialogNegativeClick(DialogFragment dialog) {}

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_item_update) {
            dashboardView.takeUpdateCall();
        }
        return super.onOptionsItemSelected(item);
    }

}
