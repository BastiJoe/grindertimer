package com.grindertimer.grindertimer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.net.wifi.WifiNetworkSpecifier;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

public class Home extends AppCompatActivity {

    WiFiSocketTask   socketTask = null;
    Button           btnConnection, btnSingleShotPlus, btnSingleShotMinus,
                     btnDoubleShotPlus,btnDoubleShotMinus;
    TextView         txtSingleShotValue, txtDoubleShotValue, txtPower, txtStatus;
    ImageView        imgStatus;
    ProgressBar      progressBar;
    ConstraintLayout doubleLayout;
    RadioGroup       rdgMode;
    RadioButton      rdbSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        imgStatus          = findViewById(R.id.imgStatus);
        txtStatus          = findViewById(R.id.txtStatus);
        btnConnection      = findViewById(R.id.btnConnect);
        doubleLayout       = findViewById(R.id.doubleShotLayout);
        Toolbar toolbar    = findViewById(R.id.toolbar);
        btnSingleShotPlus  = findViewById(R.id.btnSinglePlus);
        btnSingleShotMinus = findViewById(R.id.btnSingleMinus);
        btnDoubleShotPlus  = findViewById(R.id.btnDoublePlus);
        btnDoubleShotMinus = findViewById(R.id.btnDoubleMinus);
        txtSingleShotValue = findViewById(R.id.txtSingleShotValue);
        txtDoubleShotValue = findViewById(R.id.txtDoubleShotValue);
        txtPower           = findViewById(R.id.txtPower);
        progressBar        = findViewById(R.id.progressBar);
        rdgMode            = findViewById(R.id.rdgMode);

        progressBar.setVisibility(View.GONE);
        updateValues();

        btnSingleShotPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Constant.singleShot += 100;
                txtSingleShotValue.setText(Double.toString(new Double(Constant.singleShot)/1000));
            }
        });
        btnSingleShotMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Constant.singleShot > 1000)
                    Constant.singleShot -= 100;
                txtSingleShotValue.setText(Double.toString(new Double(Constant.singleShot)/1000));
            }
        });
        btnDoubleShotPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Constant.doubleShot += 100;
                txtDoubleShotValue.setText(Double.toString(new Double(Constant.doubleShot)/1000));
            }
        });
        btnDoubleShotMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Constant.doubleShot > 1000)
                    Constant.doubleShot -= 100;
                txtDoubleShotValue.setText(Double.toString(new Double(Constant.doubleShot)/1000));
            }
        });
        connectToNetwork(getString(R.string.SSID));
        setSupportActionBar(toolbar);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);
        btnConnection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!Constant.isConnected)
                    connectToNetwork(getString(R.string.SSID));
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    protected void onStart() {
        if(Constant.showPower)
            txtPower.setVisibility(View.VISIBLE);
        else
            txtPower.setVisibility(View.GONE);
        super.onStart();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_cart) {
            Intent send = new Intent(getApplicationContext(),Setting.class);
            startActivity(send);
            overridePendingTransition(R.anim.fragment_enter_pop, R.anim.fragment_exit_pop);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void modeChanged(View v){
        rdbSelected = rdgMode.findViewById(rdgMode.getCheckedRadioButtonId());
        if (rdbSelected.getText() == getString(R.string.rdbDouble))
            Constant.shotMode = true;
        else
            Constant.shotMode = false;
        updateShotModeView(Constant.shotMode);
    }

    public void updateShotModeView(boolean newMode){
        if (newMode)
            doubleLayout.setVisibility(View.VISIBLE);
        else
            doubleLayout.setVisibility(View.GONE);
    }

    private void validateCheckedRadioButton(){
        rdbSelected = rdgMode.findViewById((rdgMode.getCheckedRadioButtonId()));
        if ((rdbSelected.getText() == getString(R.string.rdbDouble)) && !Constant.shotMode)
            rdgMode.check(R.id.rdbSingle);
    }

    private void updateValues(){
        txtSingleShotValue.setText(Double.toString(new Double(Constant.singleShot)/1000));
        txtDoubleShotValue.setText(Double.toString(new Double(Constant.doubleShot)/1000));
    }

    private void connectToNetwork(String SSID){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            WifiNetworkSpecifier.Builder builder = new WifiNetworkSpecifier.Builder();
            builder.setSsid(SSID);
            WifiNetworkSpecifier wifiNetworkSpecifier = builder.build();
            NetworkRequest.Builder networkRequestBuilder = new NetworkRequest.Builder();
            networkRequestBuilder.addTransportType(NetworkCapabilities.TRANSPORT_WIFI);
            networkRequestBuilder.setNetworkSpecifier(wifiNetworkSpecifier);
            NetworkRequest networkRequest = networkRequestBuilder.build();
            final ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            if (cm != null) {
               cm.requestNetwork(networkRequest, new ConnectivityManager.NetworkCallback() {
                   @Override
                    public void onAvailable(@NonNull Network network) {
                        // Use this network object to Send request.
                        // eg - Using OkHttp library to create a service request
                        super.onAvailable(network);
                        cm.bindProcessToNetwork(network);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                openSocketConnection();
                            }
                        });
                   }
                });
            }
        } else {
            openSocketConnection();
        }
    }

    public void openSocketConnection() {
        if (this.socketTask != null) {
            return;
        }
        String host = "192.168.4.1";
        try {
            this.txtStatus.setTextColor(getResources().getColor(R.color.colorAccent));
            this.txtStatus.setText(getString(R.string.textAttemptingToConnect));
            this.socketTask = new WiFiSocketTask(host, 80);
            this.socketTask.execute(new Void[0]);
        } catch (Exception e) {
            e.printStackTrace();
            this.txtStatus.setText(getString(R.string.textInvalidPort));
        }
    }

    public void deviceConnected() {
        Constant.isConnected = true;
        this.btnConnection.setVisibility(View.GONE);
        this.txtStatus.setTextColor(getResources().getColor(R.color.colorAccent));
        this.txtStatus.setText(getString((R.string.textConnected)));
        this.imgStatus.setImageResource(R.drawable.ic_logo_wi_fi_connected);
    }

    public void deviceDisconnected() {
        Constant.isConnected = false;
        this.btnConnection.setVisibility(View.VISIBLE);
        this.txtStatus.setTextColor(Color.RED);
        this.txtStatus.setText(getString(R.string.textDisconnected));
        this.imgStatus.setImageResource(R.drawable.ic_logo_wi_fi_slash);
        this.socketTask = null;
    }

    public void parsePacket(String packet) {
        try {
            JSONObject jsonObject = new JSONObject(packet);
            String messageFor = jsonObject.getString("messageFor");

            if(messageFor.equalsIgnoreCase("connected")) {
                String singleShot      = jsonObject.getString("single");
                String doubleShot      = jsonObject.getString("double");
                String power           = jsonObject.getString("power");
                String waiting         = jsonObject.getString("waiting");
                String pauseDuration   = jsonObject.getString("pause");
                String shotMode        = jsonObject.getString("shot");

                Constant.singleShot    = Integer.parseInt(singleShot);
                Constant.doubleShot    = Integer.parseInt(doubleShot);
                Constant.power         = Integer.parseInt(power);
                Constant.waiting       = Integer.parseInt(waiting);
                Constant.pauseDuration = Integer.parseInt(pauseDuration);
                Constant.shotMode      = Boolean.parseBoolean(shotMode);
                updateValues();
                validateCheckedRadioButton();
                updateShotModeView(Constant.shotMode);
            }
            else
            {
                String message = jsonObject.getString("Message");
                if (message.equalsIgnoreCase("normal")) {
                    String Status = jsonObject.getString("Status");
                    String Progress = jsonObject.getString("Progress");
                    String strPowerValue = jsonObject.getString("Power");
                    txtPower.setText(getString(R.string.textPower)+ strPowerValue);
                    progressBar.setProgress(Integer.parseInt(Progress));

                    if(Status.equalsIgnoreCase("0"))
                    {
                        this.txtStatus.setText(getString(R.string.textReady));
                        this.imgStatus.setImageResource(R.drawable.ic_logo_ready);
                        this.progressBar.setVisibility(View.GONE);
                    }
                    else if(Status.equalsIgnoreCase("1"))
                    {
                        this.progressBar.setVisibility(View.VISIBLE);
                        this.txtStatus.setText(getString(R.string.textRunningSingle));
                        this.imgStatus.setImageResource(R.drawable.ic_logo_running_single);
                    }
                    else if(Status.equalsIgnoreCase("2"))
                    {
                        this.progressBar.setVisibility(View.GONE);
                        this.txtStatus.setText(getString(R.string.textPaused));
                        this.imgStatus.setImageResource(R.drawable.ic_logo_paused);
                    }
                    else if(Status.equalsIgnoreCase("3"))
                    {
                        this.progressBar.setVisibility(View.GONE);
                        this.txtStatus.setText(getString(R.string.textWatchdog));
                        this.imgStatus.setImageResource(R.drawable.ic_logo_watchdog);
                    }
                    else if(Status.equalsIgnoreCase("4"))
                    {
                        this.progressBar.setVisibility(View.VISIBLE);
                        this.txtStatus.setText(getString(R.string.textRunningDouble));
                        this.imgStatus.setImageResource(R.drawable.ic_logo_running_double);
                    }
                    else if(Status.equalsIgnoreCase("5"))
                    {
                        this.progressBar.setVisibility(View.GONE);
                        this.txtStatus.setText(getString(R.string.textPaused));
                        this.imgStatus.setImageResource(R.drawable.ic_logo_paused);
                    }
                } else {
                    try {
                        JSONObject postData = new JSONObject();
                        try {
                            postData.put("singleshot", String.valueOf(Constant.singleShot));
                            postData.put("doubleshot", String.valueOf(Constant.doubleShot));
                            postData.put("pause",      String.valueOf(Constant.pauseDuration));
                            postData.put("wait",       Constant.waiting);
                            postData.put("trash",      Constant.power);
                            postData.put("mode",       Constant.shotMode);
                            socketTask.sendMessage(postData.toString() + "\n");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } catch (Exception e2) {
                        Toast.makeText(Home.this, "[TX] Command Failed.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public class WiFiSocketTask extends AsyncTask<Void, String, Void> {
        private static final String CONNECTED_MSG    = "SOCKET_CONNECTED";
        private static final String DISCONNECTED_MSG = "SOCKET_DISCONNECTED";
        private static final String PING_MSG         = "SOCKET_PING";
        String address;
        private boolean disconnectSignal = false;
        BufferedReader inStream = null;
        OutputStream outStream = null;
        int port;
        Socket socket = null;

        WiFiSocketTask(String address, int port) {
            this.address = address;
            this.port = port;
        }

        public Void doInBackground(Void... arg) {
            try {
                this.socket = new Socket();
                this.socket.connect(new InetSocketAddress(this.address, this.port), 10000);
                this.inStream = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
                this.outStream = this.socket.getOutputStream();
                if (this.socket.isConnected()) {
                    long start = System.currentTimeMillis();
                    while (true) {
                        if (this.inStream.ready()) {
                            break;
                        } else if (System.currentTimeMillis() - start > ((long) 10000)) {
                            this.disconnectSignal = true;
                            break;
                        }
                    }
                } else {
                    this.disconnectSignal = true;
                }
                while (!this.disconnectSignal) {
                    publishProgress(new String[]{this.inStream.readLine()});
                }
            } catch (Exception e) {
                //Log.e(MainActivity.this.TAG, "Error in socket thread!");
            }
            publishProgress(new String[]{DISCONNECTED_MSG});
            try {
                if (this.socket != null) {
                    this.socket.close();
                }
                if (this.inStream != null) {
                    this.inStream.close();
                }
                if (this.outStream != null) {
                    this.outStream.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
            return null;
        }

        public void onProgressUpdate(String... values) {
            String packet = values[0];
            if (packet != null) {
                Log.e("packet:", packet);
                if (packet.equals(CONNECTED_MSG)) {
                    Home.this.deviceConnected();
                } else if (packet.equals(DISCONNECTED_MSG)) {
                    Home.this.deviceDisconnected();
                } else if (!packet.equals(PING_MSG)) {
                    Home.this.parsePacket(packet);
                }
                super.onProgressUpdate(values);
            }
        }

        void sendMessage(String data) {
            try {
                this.outStream.write(data.getBytes());
                //this.outStream.write(10);
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(Home.this, "hi", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
