package ibm.kogbanking.GUI;

import android.app.Activity;
import android.os.Bundle;

import com.ibm.watson.developer_cloud.conversation.v1.ConversationService;

import ibm.kogbanking.R;

/**
 * Created by len13 on 26.11.2016.
 */

public class VoiceInterfaceActivity extends Activity {
    ConversationService service;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice_interface);

        service = new ConversationService("2016-09-20");
        service.setUsernameAndPassword("{ORqPn3GxwWDu}", "{778fd675-2f27-422c-8599-8528a8766890}");

    }
}
