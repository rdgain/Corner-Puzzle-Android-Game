package com.example.lab2;

import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;
import android.webkit.WebView;

/**
 * Created by Raluca on 16-Feb-16.
 * Parts of code from CE881 Lab, Spyros Samothrakis
 */

public class HelpActivity extends Activity {
    static String filePath = "file:///android_asset/html/Help.html";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WebView wb = new WebView(this);

        //root directory is "assets"
        wb.loadUrl(filePath);
        setContentView(wb);
        getActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                //assuming parent activity is on the back stack, enough to finish current activity to return to it
                //alternative way commented out
                //this.startActivity(upIntent);
                //NavUtils.navigateUpTo(this, upIntent);
                finish();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}