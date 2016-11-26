package ibm.kogbanking.logic;

import android.app.Activity;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.util.Log;

import com.ibm.watson.developer_cloud.visual_recognition.v3.VisualRecognition;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.ClassifyImagesOptions;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.VisualClassification;

import java.io.File;

/**
 * Created by Yannick on 26.11.2016.
 */

public class VisualRecognitionTest extends AsyncTask<Void, Void, String> {

    Activity callingActivity;
    File file;

    public VisualRecognitionTest(Activity callingActivity, File file){
        this.callingActivity = callingActivity;
        this.file = file;
    }

    @Override
    protected String doInBackground(Void... params) {
        VisualRecognition service = new VisualRecognition(VisualRecognition.VERSION_DATE_2016_05_20);
        service.setApiKey("{fb4a8bba6e9887fb22430cdcca8d181a3ac55711}");


        Resources resources = callingActivity.getResources();
        ClassifyImagesOptions options = new ClassifyImagesOptions.Builder()
                .images(file)
                .build();
        VisualClassification result = service.classify(options).execute();

        return result.toString();
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        Log.e("result", s);
    }
}
