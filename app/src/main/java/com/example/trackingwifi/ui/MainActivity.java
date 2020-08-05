package com.example.trackingwifi.ui;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.*;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.trackingwifi.DBManager;
import com.example.trackingwifi.SampleSQLiteDBHelper;
import com.example.trackingwifi.SpeedTestAdapter;
import com.example.trackingwifi.core.Speedtest;
import com.example.trackingwifi.core.config.SpeedtestConfig;
import com.example.trackingwifi.core.config.TelemetryConfig;
import com.example.trackingwifi.core.serverSelector.TestPoint;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import com.example.trackingwifi.R;

import static com.google.android.material.internal.ContextUtils.getActivity;

public class MainActivity extends Activity {
private Toolbar toolbar;
private TextView dow,upl;private RecyclerView recyclerView1;
private SQLiteDatabase database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main1);
        toolbar=findViewById(R.id.toolbar);
toolbar.setTitle(getIntent().getExtras().getString("name_wifi"));
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_action_back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //What to do on back clicked
                finish();
            }
        });
        //transition(R.id.page_splash,0);
        new Thread(){
            public void run(){
                try{sleep(1500);}catch (Throwable t){}
                try {
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    final ImageView v = (ImageView) findViewById(R.id.testBackground);
                    options.inJustDecodeBounds = true;
                    BitmapFactory.decodeResource(getResources(), R.drawable.map, options);
                    int ih = options.outHeight, iw = options.outWidth;
                    if(4*ih*iw>16*1024*1024) throw new Exception("Too big");
                    options.inJustDecodeBounds = false;
                    DisplayMetrics displayMetrics = new DisplayMetrics();
                    getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                    int vh = displayMetrics.heightPixels, vw = displayMetrics.widthPixels;
                    double desired=Math.max(vw,vh) * 0.7;
                    double scale=desired/Math.max(iw,ih);
                    final Bitmap b = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.map, options),(int)(iw*scale), (int)(ih*scale), true);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            v.setImageBitmap(b);
                        }
                    });
                }catch (Throwable t){
                    System.err.println("Failed to load testbackground ("+t.getMessage()+")");
                }
                page_init();
            }
        }.start();
    }

    private static Speedtest st=null;

    private void page_init(){
        new Thread(){
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        transition(R.id.page_init,TRANSITION_LENGTH);
                    }
                });
                final TextView t=((TextView)findViewById(R.id.init_text));
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        t.setText(R.string.init_init);
                    }
                });
                SpeedtestConfig config=null;
                TelemetryConfig telemetryConfig=null;
                TestPoint[] servers=null;
                try{
                    String c=readFileFromAssets("SpeedtestConfig.json");
                    JSONObject o=new JSONObject(c);
                    config=new SpeedtestConfig(o);
                    c=readFileFromAssets("TelemetryConfig.json");
                    o=new JSONObject(c);
                    telemetryConfig=new TelemetryConfig(o);
                    if(telemetryConfig.getTelemetryLevel().equals(TelemetryConfig.LEVEL_DISABLED)){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                hideView(R.id.privacy_open);
                            }
                        });
                    }
                    if(st!=null){
                        try{st.abort();}catch (Throwable e){}
                    }
                    st=new Speedtest();
                    st.setSpeedtestConfig(config);
                    st.setTelemetryConfig(telemetryConfig);
                    c=readFileFromAssets("ServerList.json");
                    if(c.startsWith("\"")||c.startsWith("'")){ //fetch server list from URL
                        if(!st.loadServerList(c.subSequence(1,c.length()-1).toString())){
                            throw new Exception("Failed to load server list");
                        }
                    }else{ //use provided server list
                        JSONArray a=new JSONArray(c);
                        if(a.length()==0) throw new Exception("No test points");
                        ArrayList<TestPoint> s=new ArrayList<>();
                        for(int i=0;i<a.length();i++) s.add(new TestPoint(a.getJSONObject(i)));
                        servers=s.toArray(new TestPoint[0]);
                        st.addTestPoints(servers);
                    }
                    final String testOrder=config.getTest_order();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(!testOrder.contains("D")){
                                hideView(R.id.dlArea);
                            }
                            if(!testOrder.contains("U")){
                                hideView(R.id.ulArea);
                            }
                            if(!testOrder.contains("P")){
                                hideView(R.id.pingArea);
                            }
                            if(!testOrder.contains("I")){
                                hideView(R.id.ipInfo);
                            }
                        }
                    });
                }catch (final Throwable e){
                    System.err.println(e);
                    st=null;
                    transition(R.id.page_fail,TRANSITION_LENGTH);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ((TextView)findViewById(R.id.fail_text)).setText(getString(R.string.initFail_configError)+": "+e.getMessage());
                            final Button b=(Button)findViewById(R.id.fail_button);
                            b.setText(R.string.initFail_retry);
                            b.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    page_init();
                                    b.setOnClickListener(null);
                                }
                            });
                        }
                    });
                    return;
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        t.setText(R.string.init_selecting);
                    }
                });
                st.selectServer(new Speedtest.ServerSelectedHandler() {
                    @Override
                    public void onServerSelected(final TestPoint server) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if(server==null){
                                    transition(R.id.page_fail,TRANSITION_LENGTH);
                                    ((TextView)findViewById(R.id.fail_text)).setText(getString(R.string.initFail_noServers));
                                    final Button b=(Button)findViewById(R.id.fail_button);
                                    b.setText(R.string.initFail_retry);
                                    b.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            page_init();
                                            b.setOnClickListener(null);
                                        }
                                    });
                                }else{
                                    page_serverSelect(server,st.getTestPoints());
                                }
                            }
                        });
                    }
                });
            }
        }.start();
    }

    private void page_serverSelect(TestPoint selected, TestPoint[] servers){
        transition(R.id.page_serverSelect,TRANSITION_LENGTH);
        reinitOnResume=true;
        final ArrayList<TestPoint> availableServers=new ArrayList<>();
        for(TestPoint t:servers) {
            if (t.getPing() != -1) availableServers.add(t);
        }
        int selectedId=availableServers.indexOf(selected);
        final Spinner spinner=(Spinner)findViewById(R.id.serverList);
        ArrayList<String> options=new ArrayList<String>();
        for(TestPoint t:availableServers){
            options.add(t.getName());
        }
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,options.toArray(new String[0]));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setSelection(selectedId);
        final Button b=(Button)findViewById(R.id.start);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reinitOnResume=false;
                page_test(availableServers.get(spinner.getSelectedItemPosition()));
                b.setOnClickListener(null);
            }
        });
        TextView t=(TextView)findViewById(R.id.privacy_open);
        t.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                page_privacy();
            }
        });
    }

    private void page_privacy(){
        transition(R.id.page_privacy,TRANSITION_LENGTH);
        reinitOnResume=false;
        ((WebView)findViewById(R.id.privacy_policy)).loadUrl(getString(R.string.privacy_policy));
        TextView t=(TextView)findViewById(R.id.privacy_close);
        t.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                transition(R.id.page_serverSelect,TRANSITION_LENGTH);
                reinitOnResume=true;
            }
        });
    }

    private void page_test(final TestPoint selected){
     toolbar.setVisibility(View.VISIBLE);
        transition(R.id.page_test,TRANSITION_LENGTH);
        st.setSelectedServer(selected);
        ((TextView)findViewById(R.id.serverName)).setText(selected.getName());
        ((TextView)findViewById(R.id.dlText)).setText(format(0));
        dow=findViewById(R.id.dlText);
        ((TextView)findViewById(R.id.ulText)).setText(format(0));
        upl=findViewById(R.id.ulText);

        ((TextView)findViewById(R.id.pingText)).setText(format(0));
        ((TextView)findViewById(R.id.jitterText)).setText(format(0));
        ((ProgressBar)findViewById(R.id.dlProgress)).setProgress(0);
        ((ProgressBar)findViewById(R.id.ulProgress)).setProgress(0);
        ((GaugeView)findViewById(R.id.dlGauge)).setValue(0);
        ((GaugeView)findViewById(R.id.ulGauge)).setValue(0);
        ((TextView)findViewById(R.id.ipInfo)).setText("");
         recyclerView1=findViewById(R.id.lis);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        recyclerView1.setLayoutManager(linearLayoutManager);
         database = new SampleSQLiteDBHelper(this).getReadableDatabase();


        ((ImageView)findViewById(R.id.logo_inapp)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url=getString(R.string.logo_inapp_link);
                if(url.isEmpty()) return;
                Intent i=new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });
        final View endTestArea=findViewById(R.id.endTestArea);
        final int endTestAreaHeight=endTestArea.getHeight();
        ViewGroup.LayoutParams p=endTestArea.getLayoutParams();
        p.height=0;
        endTestArea.setLayoutParams(p);
        findViewById(R.id.shareButton).setVisibility(View.GONE);
        st.start(new Speedtest.SpeedtestHandler() {
            @Override
            public void onDownloadUpdate(final double dl, final double progress) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ((TextView)findViewById(R.id.dlText)).setText(progress==0?"...": format(dl));
                        ((GaugeView)findViewById(R.id.dlGauge)).setValue(progress==0?0:mbpsToGauge(dl));
                        ((ProgressBar)findViewById(R.id.dlProgress)).setProgress((int)(100*progress));
                    }
                });
            }

            @Override
            public void onUploadUpdate(final double ul, final double progress) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ((TextView)findViewById(R.id.ulText)).setText(progress==0?"...": format(ul));
                        ((GaugeView)findViewById(R.id.ulGauge)).setValue(progress==0?0:mbpsToGauge(ul));
                        ((ProgressBar)findViewById(R.id.ulProgress)).setProgress((int)(100*progress));
                    }
                });

            }

            @Override
            public void onPingJitterUpdate(final double ping, final double jitter, final double progress) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ((TextView)findViewById(R.id.pingText)).setText(progress==0?"...": format(ping));
                        ((TextView)findViewById(R.id.jitterText)).setText(progress==0?"...": format(jitter));
                    }
                });
            }

            @Override
            public void onIPInfoUpdate(final String ipInfo) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ((TextView)findViewById(R.id.ipInfo)).setText(ipInfo);
                    }
                });
            }

            @Override
            public void onTestIDReceived(final String id, final String shareURL) {
                if(shareURL==null||shareURL.isEmpty()||id==null||id.isEmpty()) return;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Button shareButton=(Button)findViewById(R.id.shareButton);
                        shareButton.setVisibility(View.VISIBLE);
                        shareButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent share = new Intent(Intent.ACTION_SEND);
                                share.setType("text/plain");
                                share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
                                share.putExtra(Intent.EXTRA_TEXT, shareURL);
                                startActivity(Intent.createChooser(share, getString(R.string.test_share)));
                            }
                        });
                    }
                });
            }

            @Override
            public void onEnd() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        final Button restartButton=(Button)findViewById(R.id.restartButton);
                        restartButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                page_init();
                                restartButton.setOnClickListener(null);
                            }
                        });
                        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
                        LocalDateTime now = LocalDateTime.now();
                        saveToDB(getIntent().getExtras().getString("name_wifi"),dow.getText().toString(),upl.getText().toString(),new Date());
                        DBManager dbManager = new DBManager(getApplicationContext());
                        dbManager.open();

                        Cursor cursor=fetch();
                        Toast.makeText(MainActivity.this, "The total cursor count is " + cursor.getCount(),Toast.LENGTH_LONG).show();
                        SpeedTestAdapter speedTestAdapter= new SpeedTestAdapter(MainActivity.this, cursor);
                        recyclerView1.setAdapter(speedTestAdapter);
                    }
                });
                final long startT=System.currentTimeMillis(), endT=startT+TRANSITION_LENGTH;
                new Thread(){
                    public void run(){
                        while(System.currentTimeMillis()<endT){
                            final double f=(double)(System.currentTimeMillis()-startT)/(double)(endT-startT);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ViewGroup.LayoutParams p=endTestArea.getLayoutParams();
                                    p.height=(int)(endTestAreaHeight*f);
                                    endTestArea.setLayoutParams(p);
                                }
                            });
                            try{sleep(10);}catch (Throwable t){}
                        }
                    }
                }.start();
            }

            @Override
            public void onCriticalFailure(String err) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        transition(R.id.page_fail,TRANSITION_LENGTH);
                        ((TextView)findViewById(R.id.fail_text)).setText(getString(R.string.testFail_err));
                        final Button b=(Button)findViewById(R.id.fail_button);
                        b.setText(R.string.testFail_retry);
                        b.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                page_init();
                                b.setOnClickListener(null);
                            }
                        });
                    }
                });
            }
        });
    }

    private String format(double d){
        Locale l=null;
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.N) {
            l = getResources().getConfiguration().getLocales().get(0);
        }else{
            l=getResources().getConfiguration().locale;
        }
        if(d<10) return String.format(l,"%.2f",d);
        if(d<100) return String.format(l,"%.1f",d);
        return ""+Math.round(d);
    }

    private int mbpsToGauge(double s){
        return (int)(1000*(1-(1/(Math.pow(1.3,Math.sqrt(s))))));
    }

    private String readFileFromAssets(String name) throws Exception{
        BufferedReader b=new BufferedReader(new InputStreamReader(getAssets().open(name)));
        String ret="";
        try{
            for(;;){
                String s=b.readLine();
                if(s==null) break;
                ret+=s;
            }
        }catch(EOFException e){}
        return ret;
    }

    private void hideView(int id){
        View v=findViewById(id);
        if(v!=null) v.setVisibility(View.GONE);
    }

    private boolean reinitOnResume=false;
    @Override
    protected void onResume() {
        super.onResume();
        if(reinitOnResume){
            reinitOnResume=false;
            page_init();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try{st.abort();}catch (Throwable t){}
    }

    @Override
    public void onBackPressed() {
        if(currentPage==R.id.page_privacy)
            transition(R.id.page_serverSelect,TRANSITION_LENGTH);
        else super.onBackPressed();
    }

    //PAGE TRANSITION SYSTEM

    private int currentPage=-1;
    private boolean transitionBusy=false; //TODO: improve mutex
    private int TRANSITION_LENGTH=300;

    private void transition(final int page, final int duration){
        if(transitionBusy){
            new Thread(){
                public void run(){
                    try{sleep(10);}catch (Throwable t){}
                    transition(page,duration);
                }
            }.start();
        }else transitionBusy=true;
        if(page==currentPage) return;
        final ViewGroup oldPage=currentPage==-1?null:(ViewGroup)findViewById(currentPage),
                newPage=page==-1?null:(ViewGroup)findViewById(page);
        new Thread(){
            public void run(){
                long t=System.currentTimeMillis(), endT=t+duration;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(newPage!=null){
                            newPage.setAlpha(0);
                            newPage.setVisibility(View.VISIBLE);
                        }
                        if(oldPage!=null){
                            oldPage.setAlpha(1);
                        }
                    }
                });
                while(t<endT){
                    t=System.currentTimeMillis();
                    final float f=(float)(endT-t)/(float)duration;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(newPage!=null) newPage.setAlpha(1-f);
                            if(oldPage!=null) oldPage.setAlpha(f);
                        }
                    });
                    try{sleep(10);}catch (Throwable e){}
                }
                currentPage=page;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(oldPage!=null){
                            oldPage.setAlpha(0);
                            oldPage.setVisibility(View.INVISIBLE);
                        }
                        if(newPage!=null){
                            newPage.setAlpha(1);
                        }
                        transitionBusy=false;
                    }
                });
            }
        }.start();
    }
    private void saveToDB(String name, String d, String u, Date date) {
        SQLiteDatabase database = new SampleSQLiteDBHelper(this).getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SampleSQLiteDBHelper.PERSON_COLUMN_NAME, name);
        values.put(SampleSQLiteDBHelper.PERSON_COLUMN_D, d);
        values.put(SampleSQLiteDBHelper.PERSON_COLUMN_U, u);
        values.put(SampleSQLiteDBHelper.PERSON_COLUMN_DATE, String.valueOf(date));
        database.execSQL("CREATE TABLE IF NOT EXISTS "+SampleSQLiteDBHelper.PERSON_TABLE_NAME+"("+SampleSQLiteDBHelper.PERSON_COLUMN_NAME+" VARCHAR,"+SampleSQLiteDBHelper.PERSON_COLUMN_D+" VARCHAR,Upload VARCHAR,"+SampleSQLiteDBHelper.PERSON_COLUMN_DATE+" VARCHAR);");
     try{
         SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd/HH:mm:ss");

         database.execSQL("INSERT INTO "+SampleSQLiteDBHelper.PERSON_TABLE_NAME+" VALUES("+name+","+d.replaceAll(",",".")+","+u.replaceAll(",",".")+","+(dateFormat.format(date)).replaceAll(":","-")+");");

     }catch (SQLException e){
         Toast.makeText(this,"assasasa"+e.getMessage(),Toast.LENGTH_LONG).show();
     }
        //long newRowId = database.insert(SampleSQLiteDBHelper.PERSON_TABLE_NAME, null, values);

        Toast.makeText(this, "The new Row Id is " , Toast.LENGTH_LONG).show();

    }
    public Cursor fetch() {
        Cursor cursor = this.database.query(SampleSQLiteDBHelper.PERSON_TABLE_NAME, new String[]{SampleSQLiteDBHelper.PERSON_COLUMN_DATE, SampleSQLiteDBHelper.PERSON_COLUMN_D, SampleSQLiteDBHelper.PERSON_COLUMN_NAME}, null, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

}
