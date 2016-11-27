package ibm.kogbanking.GUI;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import ibm.kogbanking.CustomAdapter;
import ibm.kogbanking.R;
import ibm.kogbanking.logic.WatsonSpeech;

import android.speech.tts.TextToSpeech;

import com.ibm.watson.developer_cloud.http.ServiceCall;
import com.ibm.watson.developer_cloud.text_to_speech.v1.model.Voice;
import com.ibm.watson.developer_cloud.text_to_speech.v1.util.WaveUtils;

/**
 * Created by Ludwig on 26.11.2016.
 */

public class Home extends Activity implements android.speech.tts.TextToSpeech.OnInitListener{
    ArrayList<String[]> account;
    ImageView profilePic;
    TextView balance;
    Button transactionButton;
    ListView olderTransactions;
    Button voiceUI;
    android.speech.tts.TextToSpeech tts;
    CustomAdapter adapter;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        tts = new android.speech.tts.TextToSpeech(this, this);

        account = new ArrayList<String[]>();
        String[] test1 = {"Dr. Watson","Medical Bill", "234", "11/25/16"};
        String[] test2= {"Wal-Mart","Shopping", "43","11/22/16"};
        String[] test3 = {"Bank of America","Ensurance", "590","11/19/16"};
        String[] test4 = {"John Dowe","Rent", "1034","11/19/16"};
        String[] test5 = {"Amazon","Kindle Reader", "989","11/16/16"};
        String[] test6 = {"Expedia","Vacation", "13200","11/13/16"};

        account.add(0, test1);
        account.add(0, test2);
        account.add(0, test3);
        account.add(0, test4);
        account.add(0, test5);
        account.add(0, test6);

        profilePic = (ImageView) findViewById(R.id.profilePic);
        balance = (TextView) findViewById(R.id.balance);
        transactionButton = (Button) findViewById(R.id.transactionButton);
        olderTransactions = (ListView) findViewById(R.id.olderTransactions);
        voiceUI = (Button) findViewById(R.id.voiceRecogBtn);

        balance.setText("20000$");

        transactionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home.this, Transaction.class);
                startActivity(intent);
            }
        });

        voiceUI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Home.this, VoiceInterfaceActivity.class);
                startActivity(i);
            }
        });

        adapter = new CustomAdapter(this, account);
        olderTransactions.setAdapter(adapter);

        olderTransactions.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String transferedTo = adapter.getItem(position)[0];
                String becauseOf = adapter.getItem(position)[1];
                String amount = adapter.getItem(position)[2];
                String date = adapter.getItem(position)[3];

                String text = amount + "Dollar transfered to " + transferedTo +  " because of " + becauseOf + " on " + date;
                new WatsonSpeech(Home.this, text, true).execute();
            }

        });
    }

    @Override
    public void onInit(int status) {
        if (status == android.speech.tts.TextToSpeech.SUCCESS) {

            int result = tts.setLanguage(Locale.GERMAN);

            if (result == android.speech.tts.TextToSpeech.LANG_MISSING_DATA
                    || result == android.speech.tts.TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "This Language is not supported");
            } else {
                //speakOut("Willkommen");
            }

        } else {
            Log.e("TTS", "Initilization Failed!");
        }
    }

    private void speakOut(String text) {

        //tts.speak(text, android.speech.tts.TextToSpeech.QUEUE_FLUSH, null);
    }
}
