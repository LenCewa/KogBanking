package ibm.kogbanking.GUI;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.ibm.watson.developer_cloud.http.ServiceCall;
import com.ibm.watson.developer_cloud.text_to_speech.v1.TextToSpeech;
import com.ibm.watson.developer_cloud.text_to_speech.v1.model.AudioFormat;
import com.ibm.watson.developer_cloud.text_to_speech.v1.model.Voice;
import com.ibm.watson.developer_cloud.text_to_speech.v1.util.WaveUtils;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import ibm.kogbanking.CustomAdapter;
import ibm.kogbanking.R;

/**
 * Created by Ludwig on 26.11.2016.
 */

public class Home extends Activity {
    ArrayList<String[]> account;
    ImageView profilePic;
    TextView balance;
    Button transactionButton;
    ListView olderTransactions;
    Button voiceUI;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        account = new ArrayList<String[]>();
        String[] test1 = {"DE43 123456789","Arztrechnung", "234$", "25.11.16"};
        String[] test2= {"DE43 432094324","Einkauf", "43$","22.11.16"};
        String[] test3 = {"DE43 324989234","Versicherung", "590$","19.11.16"};
        String[] test4 = {"DE43 342789849","Miete", "1034$","19.11.16"};
        String[] test5 = {"DE43 989827498","Amazon", "989$","16.11.16"};
        String[] test6 = {"DE43 238974832","Urlaub", "13200$","13.11.16"};

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

        olderTransactions.setAdapter(new CustomAdapter(this, account));

        olderTransactions.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextToSpeech service = new TextToSpeech();
                service.setUsernameAndPassword("778fd675-2f27-422c-8599-8528a8766890", "ORqPn3GxwWDu");
                ServiceCall<List<Voice>> voices = service.getVoices();

                String text = "Hallo";
                service.synthesize(text, Voice.DE_BIRGIT, AudioFormat.WAV).execute();

            }

        });
    }

}
