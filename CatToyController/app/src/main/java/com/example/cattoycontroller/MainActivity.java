package com.example.cattoycontroller;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

public class MainActivity extends Activity implements CompoundButton.OnCheckedChangeListener, View.OnClickListener {
    private int mLedNumber;

    final int LED1 = 0;
    final String webViewUrl = "http://192.168.43.62:8081";

    private WebView mWebView;
    private ToggleButton mButtonMotion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ToggleButton ToggleButton1 = (ToggleButton) findViewById(R.id.toggleButton_motor);
        ToggleButton1.setOnCheckedChangeListener(this);

        Button TakeButton = (Button) findViewById(R.id.button_take_camera);
        TakeButton.setOnClickListener(this);

        final
        WebView webView = (WebView) findViewById(R.id.webView);
        mWebView = webView;
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(webViewUrl);

        Button reloadButton = (Button) findViewById(R.id.button_reload);
        reloadButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                webView.reload();
            }
        });

        ToggleButton buttonMottion = (ToggleButton) findViewById(R.id.button_motion);
        mButtonMotion = buttonMottion;
        buttonMottion.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            private Activity activity;
            public CompoundButton.OnCheckedChangeListener setAct(Activity act) {
                this.activity = act;
                return this;
            }
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                int stat;
                if (isChecked) {
                    stat = 1;
                } else{
                    stat = 0;
                }

                HttpGetTaskMotionButton task = new HttpGetTaskMotionButton(this.activity);
                task.execute(stat);
            }
        }.setAct(this));

        Button DropBoxButton = (Button) findViewById(R.id.dropbox_button);
        DropBoxButton.setOnClickListener(new View.OnClickListener(){
            Intent intent = null;
            @Override
            public void onClick(View view) {
                intent = new Intent(Intent.ACTION_MAIN);
                intent.setAction("android.intent.category.LAUNCHER");
                intent.setClassName("com.dropbox.android", "com.dropbox.android.activity.DbxMainActivity");
                intent.setFlags(0x4000000);
                startActivity(intent);
            }
        });
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        int stat;
        if (isChecked) {
            stat = 1;
        } else{
            stat = 0;
        }

        HttpGetTask task = new HttpGetTask(this);
        task.execute(0, stat);
    }

    @Override
    public void onClick(View v) {
        boolean motionChecked = mButtonMotion.isChecked();
        if(motionChecked){
            HttpGetTaskMotionButton motionTask = new HttpGetTaskMotionButton(this);
            motionTask.execute(0);
        }
        HttpGetTaskTakeCamera task = new HttpGetTaskTakeCamera(this);
        task.execute();
        if(motionChecked){
            HttpGetTaskMotionButton motionTask = new HttpGetTaskMotionButton(this);
            motionTask.execute(1);
//            mWebView.reload();
        }
    }
}