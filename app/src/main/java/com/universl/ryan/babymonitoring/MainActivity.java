package com.universl.ryan.babymonitoring;

import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class MainActivity extends AppCompatActivity { // connect between cloud MQTT and Mobile app.

    private static final String TAG = "TAG";
    static String MQTTHOST = "tcp://postman.cloudmqtt.com:12149";
    static String USERNAME = "ymafotft";
    static String PASSWORD = "v8UqctOTWwT5";
    static String TOPIC = "V-Care";

    MqttAndroidClient client;

    Vibrator vibrator;
    Ringtone ringtone;
    LinearLayout feed,predict;
    VideoView videoView;
    MediaController mediaController;
    TextView angry,happy,sad,surprise,contempt,disgust,fear,neutral;
    String messageString;
    RelativeLayout main;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        main = findViewById(R.id.main);
        angry = findViewById(R.id.angry);
        happy = findViewById(R.id.happy);
        sad = findViewById(R.id.sad);
        surprise = findViewById(R.id.surprise);
        contempt = findViewById(R.id.contempt);
        disgust = findViewById(R.id.disgust);
        fear = findViewById(R.id.fear);
        neutral = findViewById(R.id.neutral);
        mediaController = new MediaController(this);
        feed = findViewById(R.id.feed);
        videoView = findViewById(R.id.videoView);
        predict = findViewById(R.id.prediction);
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        ringtone = RingtoneManager.getRingtone(getApplicationContext(),uri);
        // create the mqtt Client id
        String clientId = MqttClient.generateClientId();
        client = new MqttAndroidClient(this.getApplicationContext(), MQTTHOST, clientId);
        MqttConnectOptions options = new MqttConnectOptions();
        options.setUserName(USERNAME);
        options.setPassword(PASSWORD.toCharArray());

        try {
            IMqttToken token = client.connect(options);// connected to the mqtt
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) { //pop-up window for successful message.
                    // We are connected
                    Snackbar.make(main, "Connected is Successful", Snackbar.LENGTH_LONG).show();
                    Log.d(TAG, "onSuccess");
                    setSubscription();
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) { //pop-up window for unsuccessful message.
                    // Something went wrong e.g. connection timeout or firewall problems
                    Snackbar.make(main, "Connected is not Successful", Snackbar.LENGTH_LONG).show();
                    Log.d(TAG, "onFailure");

                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
        client.setCallback(new MqttCallback() { // recive the data, data type is String
            @Override
            public void connectionLost(Throwable cause) {

            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception { //check the details
//                messageView.setText(new String(message.getPayload()));
                messageString = new String(message.getPayload()); // save data from string
                vibrator.vibrate(500);
                ringtone.play();
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }
        });
        feed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { // is the button play recive video.
                mediaController.setAnchorView(videoView);
                Uri uri = Uri.parse("android.resource://com.universl.ryan.babymonitoring/" + R.raw.baby_crying);
                videoView.setMediaController(mediaController);
                videoView.setVideoURI(uri);
                videoView.requestFocus();
                videoView.start();
            }
        });
        predict.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { // is the button display recive predict data.
                angry.setVisibility(View.VISIBLE);
                happy.setVisibility(View.VISIBLE);
                sad.setVisibility(View.VISIBLE);
                surprise.setVisibility(View.VISIBLE);
                contempt.setVisibility(View.GONE);
                disgust.setVisibility(View.GONE);
                fear.setVisibility(View.VISIBLE);
                neutral.setVisibility(View.VISIBLE);

                String[] messageParts = messageString.split("-"); //split the string and store the data in array.
                angry.setText(messageParts[0]);
                happy.setText(messageParts[1]);
                sad.setText(messageParts[2]);
                surprise.setText(messageParts[3]);
                //contempt.setText(messageParts[4]);
                //disgust.setText(messageParts[5]);
                fear.setText(messageParts[4]);
                neutral.setText(messageParts[5]);
            }
        });
    }
    private void setSubscription(){ //
        try {
            client.subscribe(TOPIC,0);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }
}
