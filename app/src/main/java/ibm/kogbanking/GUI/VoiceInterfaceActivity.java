package ibm.kogbanking.GUI;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.ibm.watson.developer_cloud.conversation.v1.ConversationService;


import java.util.ArrayList;

import ibm.kogbanking.R;

/**
 * Created by len13 on 26.11.2016.
 */

public class VoiceInterfaceActivity extends Activity {
    ConversationService service;
    ListView messages;
    EditText aMsg;
    Button send;
    ArrayList<String> conversation;
    ArrayAdapter<String> adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice_interface);

        service = new ConversationService("2016-09-20");
        service.setUsernameAndPassword("{ORqPn3GxwWDu}", "{778fd675-2f27-422c-8599-8528a8766890}");

        send = (Button) findViewById(R.id.sendBtn);
        aMsg = (EditText) findViewById(R.id.messageTxt);
        messages = (ListView) findViewById(R.id.exchangedMsgs);
        conversation = new ArrayList<>();

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = aMsg.getText().toString();
                conversation.add(msg);
            }
        });

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, conversation);
        


    }
}
