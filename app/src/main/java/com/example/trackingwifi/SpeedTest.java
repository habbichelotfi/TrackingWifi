package com.example.trackingwifi;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import fr.bmartel.speedtest.SpeedTestReport;
import fr.bmartel.speedtest.SpeedTestSocket;
import fr.bmartel.speedtest.inter.ISpeedTestListener;
import fr.bmartel.speedtest.model.SpeedTestError;
import me.itangqi.waveloadingview.WaveLoadingView;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

public class SpeedTest extends AppCompatActivity {
SpeedTestAdapter speedTestAdapter;
   private WaveLoadingView Download,Upload,mWaveLoadingView;
   private TextView countdown,dk,uk,backtomenu,tryagain;
   private Set<String > data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speed_test);
        Toolbar toolbar = findViewById(R.id.toolbar);
        backtomenu=findViewById(R.id.backtomenu);
        tryagain=findViewById(R.id.tryagain);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_action_back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //What to do on back clicked
                finish();
            }
        });
        countdown=findViewById(R.id.countdown);
toolbar.setTitle(getIntent().getExtras().getString("name_wifi"));

        dk=findViewById(R.id.dk);
        uk=findViewById(R.id.uk);
        new CountDownTimer(3000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                Toast.makeText(SpeedTest.this,""+millisUntilFinished,Toast.LENGTH_LONG).show();
countdown.setText(String.valueOf(millisUntilFinished/1000)+"s..");
            }

            @Override
            public void onFinish() {
                new SpeedTestTaskD().execute();
            }
        }.start();

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

finish();
            }
        });
tryagain.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        new SpeedTestTaskD().execute();
    }
});
        backtomenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        mWaveLoadingView = (WaveLoadingView) findViewById(R.id.waveLoadingView);
        mWaveLoadingView.setShapeType(WaveLoadingView.ShapeType.CIRCLE);
        mWaveLoadingView.setTopTitle("Download");
        mWaveLoadingView.setCenterTitleColor(Color.WHITE);
        mWaveLoadingView.setBottomTitleSize(18);
        mWaveLoadingView.setProgressValue(0);
        mWaveLoadingView.setBorderWidth(10);
        mWaveLoadingView.setAmplitudeRatio(100);
        //mWaveLoadingView.setWaveColor(Color.GRAY);
        mWaveLoadingView.setBorderColor(Color.WHITE);
        mWaveLoadingView.setTopTitleStrokeWidth(3);
        mWaveLoadingView.setAnimDuration(3000);
        mWaveLoadingView.pauseAnimation();
        mWaveLoadingView.resumeAnimation();
        mWaveLoadingView.cancelAnimation();
        mWaveLoadingView.startAnimation();
        //Download = findViewById(R.id.Download);
/*        Download.setShapeType(WaveLoadingView.ShapeType.CIRCLE);
        Download.setTopTitle("Download");

        Download.setBottomTitleSize(18);
        Download.setProgressValue(0);
        Download.setBorderWidth(10);
        Download.setAmplitudeRatio(0);
        Download.setWaveColor(Color.GRAY);

        Download.setTopTitleStrokeColor(Color.BLUE);
        Download.setTopTitleStrokeWidth(3);
        Download.setAnimDuration(3000);
        Download.pauseAnimation();
        Download.resumeAnimation();
        Download.cancelAnimation();
        Download.startAnimation();
        `
 */
        Upload = findViewById(R.id.upload);
        Upload.setShapeType(WaveLoadingView.ShapeType.CIRCLE);
        Upload.setTopTitle("Upload");
        Upload.setBottomTitleSize(18);
        Upload.setProgressValue(0);
        Upload.setBorderWidth(10);
        Upload.setAmplitudeRatio(50);

        Upload.setTopTitleStrokeWidth(3);
        Upload.setAnimDuration(3000);


        Upload.startAnimation();
       // new SpeedTestTaskU().execute();
         }
  private   class SpeedTestTaskD extends AsyncTask<Void, Void, Void> {

      @Override
      protected Void doInBackground(Void... params) {

          SpeedTestSocket speedTestSocket = new SpeedTestSocket();

          // add a listener to wait for speedtest completion and progress
          speedTestSocket.addSpeedTestListener(new ISpeedTestListener() {

              @Override
              public void onCompletion(final SpeedTestReport report) {
                  // called when download/upload is finished
                  runOnUiThread(new Runnable() {
                      @Override
                      public void run() {

                          Toast.makeText(SpeedTest.this, "on complet", Toast.LENGTH_SHORT).show();
new SpeedTestTaskU().execute();
                      }
                  });
              }

              @Override
              public void onError(SpeedTestError speedTestError, final String errorMessage) {
                  runOnUiThread(new Runnable() {
                      @Override
                      public void run() {
                          Toast.makeText(SpeedTest.this, errorMessage, Toast.LENGTH_SHORT).show();

                      }
                  });
              }

              @Override
              public void onProgress(final float percent, final SpeedTestReport report) {
                  // called to notify download/upload progress
                  runOnUiThread(new Runnable() {
                      @Override
                      public void run() {
                          Toast.makeText(SpeedTest.this, report.getSpeedTestMode().toString(), Toast.LENGTH_SHORT).show();
                       mWaveLoadingView.setProgressValue( Math.round(percent));
                          mWaveLoadingView.setCenterTitle(String.valueOf(percent)+"%");
                          dk.setText("Ko/s :"+Math.round(report.getTransferRateOctet().intValue()/1024));
                      }
                  });
              }
          });

          speedTestSocket.startDownload("http://ipv4.ikoula.testdebit.info/5M.iso");
          return null;
      }
  }
  private class SpeedTestTaskU  extends AsyncTask<Void, Void, Void> {


      @Override
      protected Void doInBackground(Void... voids) {
          SpeedTestSocket speedTestSocket = new SpeedTestSocket();


          speedTestSocket.addSpeedTestListener(new ISpeedTestListener() {

              @Override
              public void onCompletion ( final SpeedTestReport report){
                  // called when download/upload is finished
                  runOnUiThread(new Runnable() {
                      @Override
                      public void run() {
tryagain.setVisibility(View.VISIBLE);
backtomenu.setVisibility(View.VISIBLE);
                          Toast.makeText(SpeedTest.this, "on complet", Toast.LENGTH_SHORT).show();
                      }
                  });
              }

              @Override
              public void onError (SpeedTestError speedTestError,final String errorMessage){
                  runOnUiThread(new Runnable() {
                      @Override
                      public void run() {
                          Toast.makeText(SpeedTest.this, errorMessage, Toast.LENGTH_SHORT).show();

                      }
                  });
              }

              @Override
              public void onProgress ( final float percent, final SpeedTestReport report){
                  // called to notify download/upload progress
                  runOnUiThread(new Runnable() {
                      @Override
                      public void run() {
                          Toast.makeText(SpeedTest.this, report.getSpeedTestMode().toString(), Toast.LENGTH_SHORT).show();
                          Upload.setProgressValue( Math.round(percent));
                          Upload.setCenterTitle(String.valueOf(percent)+"%");

                          uk.setText(Math.round(report.getTransferRateOctet().intValue()/1024)+" Ko/s");

                      }
                  });
              }
          });
                  speedTestSocket.startUpload("http://ipv4.ikoula.testdebit.info/",4000000);

          return null;
      }

  }
    private void saveToDB(String name,float d,float u,Date date) {
        SQLiteDatabase database = new SampleSQLiteDBHelper(this).getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SampleSQLiteDBHelper.PERSON_COLUMN_NAME, name);
        values.put(SampleSQLiteDBHelper.PERSON_COLUMN_D, d);
        values.put(SampleSQLiteDBHelper.PERSON_COLUMN_U, u);
        values.put(SampleSQLiteDBHelper.PERSON_COLUMN_DATE, String.valueOf(date));

        long newRowId = database.insert(SampleSQLiteDBHelper.PERSON_TABLE_NAME, null, values);

        Toast.makeText(this, "The new Row Id is " + newRowId, Toast.LENGTH_LONG).show();
    }


}
