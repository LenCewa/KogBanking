package ibm.kogbanking.logic;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import com.ibm.watson.developer_cloud.visual_recognition.v3.VisualRecognition;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import ibm.kogbanking.GUI.LoginActivity;

/**
 * Created by andrastuzko on 2016. 11. 26..
 */

public class WatsonAsync extends AsyncTask<Void, Void, String> {

    Activity callingActivity;

    public WatsonAsync(Activity callingActivity){
        this.callingActivity = callingActivity;
    }
    @Override
    protected String doInBackground(Void... voids) {

        WatsonPost w = new WatsonPost(VisualRecognition.VERSION_DATE_2016_05_20);
        return w.executeTask();
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        Log.e("hallo", s);
        try {
            JSONObject obj = new JSONObject(s);
            String classifier = obj.getString("classifier_id");
            ((LoginActivity)callingActivity).accountSet = true;
            SharedPreferences sp = callingActivity.getSharedPreferences("classifier", Activity.MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();

            editor.putString("classifier", classifier);
            editor.commit();

   } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
