package ibm.kogbanking.GUI;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.ibm.watson.developer_cloud.conversation.v1.ConversationService;
import com.ibm.watson.developer_cloud.conversation.v1.model.MessageRequest;
import com.ibm.watson.developer_cloud.conversation.v1.model.MessageResponse;
import com.ibm.watson.developer_cloud.speech_to_text.v1.SpeechToText;


import java.util.ArrayList;

import ibm.kogbanking.R;
import ibm.kogbanking.logic.WatsonCommunication;
import okhttp3.MultipartBody;

/**
 * Created by len13 on 26.11.2016.
 */

public class VoiceInterfaceActivity extends Activity {

    ListView messages;
    EditText aMsg;
    Button send;
    String username = "778fd675-2f27-422c-8599-8528a8766890";
    String password = "ORqPn3GxwWDu";

    SpeechToText service;

    public ArrayList<String> conversation;
    public ArrayAdapter<String> adapter;


    MessageResponse response;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice_interface);

        send = (Button) findViewById(R.id.sendBtn);
        aMsg = (EditText) findViewById(R.id.messageTxt);
        messages = (ListView) findViewById(R.id.exchangedMsgs);
        conversation = new ArrayList<>();
        conversation.add("Ignore.");
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, conversation);
        messages.setAdapter(adapter);

        //[SPEECH TO TEXT BEGIN]
        service = new SpeechToText();
        service.setUsernameAndPassword(username,password);

        //[SPEECH TO TEXT end]

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = aMsg.getText().toString();
                conversation.add(msg);
                aMsg.setText("");
                adapter.notifyDataSetChanged();

                new WatsonCommunication(VoiceInterfaceActivity.this, msg).execute();
            }
        });

    }
}
