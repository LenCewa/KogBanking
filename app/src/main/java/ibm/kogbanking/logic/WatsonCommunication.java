package ibm.kogbanking.logic;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ibm.watson.developer_cloud.conversation.v1.ConversationService;
import com.ibm.watson.developer_cloud.conversation.v1.model.MessageRequest;
import com.ibm.watson.developer_cloud.conversation.v1.model.MessageResponse;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

import ibm.kogbanking.GUI.VoiceInterfaceActivity;

/**
 * Created by len13 on 26.11.2016.
 */

public class WatsonCommunication extends AsyncTask<Void, String, MessageResponse> {

    VoiceInterfaceActivity context;
    String msg;

    ConversationService service;
    String workspaceId = "2054b5c4-cbb0-4e15-a7fc-b3d40d231a38";
    MessageRequest newMessage;
    MessageResponse response;
    Map<String, Object> map;

    public WatsonCommunication(VoiceInterfaceActivity context, String msg) {
        this.context = context;
        this.msg = msg;
    }

    @Override
    public MessageResponse doInBackground (Void... aVoid) {

        service = new ConversationService("2016-09-20");
        service.setUsernameAndPassword("778fd675-2f27-422c-8599-8528a8766890", "ORqPn3GxwWDu");
        newMessage = new MessageRequest.Builder().inputText(msg).build();
        response = service.message(workspaceId, newMessage).execute();

        return  response;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(MessageResponse messageResponse) {
        super.onPostExecute(messageResponse);
        //String watsonAnswer = messageResponse.getText().get(0);
        context.conversation.add("watsonAnswer");
        context.adapter.notifyDataSetChanged();
    }

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
    }
}
