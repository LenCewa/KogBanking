package ibm.kogbanking.logic;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.ibm.watson.developer_cloud.visual_recognition.v3.VisualRecognition;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.ClassifyImagesOptions;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.VisualClassification;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.StreamCorruptedException;

import ibm.kogbanking.GUI.Home;

/**
 * Created by Yannick on 26.11.2016.
 */

public class VisualRecognitionTest extends AsyncTask<Void, Void, String> {

    Activity callingActivity;
    File file;
    String classifier;

    public VisualRecognitionTest(Activity callingActivity, File file, String classifier){
        this.callingActivity = callingActivity;
        this.file = file;
        this.classifier = classifier;
    }

    @Override
    protected String doInBackground(Void... params) {
        VisualRecognition service = new VisualRecognition(VisualRecognition.VERSION_DATE_2016_05_20);
        service.setApiKey("fb4a8bba6e9887fb22430cdcca8d181a3ac55711");


        ClassifyImagesOptions options = new ClassifyImagesOptions.Builder()
                .images(file)
                .threshold(0.0)
                .classifierIds(classifier)
                .build();
        VisualClassification result = service.classify(options).execute();

        return result.toString();
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        Log.e("result", s);

        Double d = 0d;
        try {
            JSONObject obj = new JSONObject(s);
            JSONArray arr = obj.getJSONArray("images");
            obj = arr.getJSONObject(0);
            arr = obj.getJSONArray("classifiers");
            obj = arr.getJSONObject(0);
            arr = obj.getJSONArray("classes");
            obj = arr.getJSONObject(0);
            d = obj.getDouble("score");
            Log.e("double", String.valueOf(d));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Intent i = new Intent(callingActivity, Home.class);
        Toast.makeText(callingActivity, String.valueOf(d), Toast.LENGTH_LONG).show();
        if(d >= 0.75)
            callingActivity.startActivity(i);
    }
}
