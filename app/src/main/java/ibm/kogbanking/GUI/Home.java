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

        watsonSpeech();

        account = new ArrayList<String[]>();
        String[] test1 = {"Dr. Koch","Arztrechnung", "234", "25.11.16"};
        String[] test2= {"Edeka","Einkauf", "43","22.11.16"};
        String[] test3 = {"Barmer","Versicherung", "590","19.11.16"};
        String[] test4 = {"Markus Müller","Miete", "1034","19.11.16"};
        String[] test5 = {"Amazon","Kindle Reader", "989","16.11.16"};
        String[] test6 = {"Expedia","Urlaub", "13200","13.11.16"};

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

                String von = adapter.getItem(position)[0];
                String was = adapter.getItem(position)[1];
                String wann = adapter.getItem(position)[2];
                String wieviel = adapter.getItem(position)[3];

                String text = wieviel + "Euro eingezogen von " + von +  " " + was + "am " + wann;
                speakOut(text);
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

    public void watsonSpeech(){
        com.ibm.watson.developer_cloud.text_to_speech.v1.TextToSpeech service = new com.ibm.watson.developer_cloud.text_to_speech.v1.TextToSpeech();
        service.setUsernameAndPassword("{username}", "{password}");

        try {
            String text = "Hello world.";
            ServiceCall<InputStream> stream = service.synthesize(text, Voice.EN_ALLISON);

            InputStream in = WaveUtils.reWriteWaveHeader(stream.execute());
            OutputStream out = new FileOutputStream("hello_world.wav");
            byte[] buffer = new byte[1024];


                try
                {

                    File path=new File(getCacheDir()+"/musicfile.3gp");

                    FileOutputStream fos = new FileOutputStream(path);
                    fos.write(buffer);
                    fos.close();

                    MediaPlayer mediaPlayer = new MediaPlayer();

                    FileInputStream fis = new FileInputStream(path);
                    mediaPlayer.setDataSource(getCacheDir()+"/musicfile.3gp");

                    mediaPlayer.prepare();
                    mediaPlayer.start();
                }
                catch (IOException ex)
                {
                    String s = ex.toString();
                    ex.printStackTrace();
                }


            int length;
            while ((length = in.read(buffer)) > 0) {
                out.write(buffer, 0, length);
            }
            out.close();
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
