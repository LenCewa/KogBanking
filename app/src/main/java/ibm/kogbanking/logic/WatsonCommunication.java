package ibm.kogbanking.logic;

import android.os.AsyncTask;

import com.ibm.watson.developer_cloud.conversation.v1.ConversationService;
import com.ibm.watson.developer_cloud.conversation.v1.model.MessageRequest;
import com.ibm.watson.developer_cloud.conversation.v1.model.MessageResponse;

/**
 * Created by len13 on 26.11.2016.
 */

public class WatsonCommunication extends AsyncTask<Void, Void, String> {

    ConversationService service;
    String workspaceId = "25dfa8a0-0263-471b-8980-317e68c30488";



    @Override
    protected String doInBackground(Void... params) {
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);


    }
}
