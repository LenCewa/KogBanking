package Logic;

import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import com.ibm.watson.developer_cloud.visual_recognition.v3.VisualRecognition;

import java.io.File;

/**
 * Created by andrastuzko on 2016. 11. 26..
 */

public class WatsonAsync extends AsyncTask<Void, Void, String> {

    @Override
    protected String doInBackground(Void... voids) {
        WatsonPost w = new WatsonPost(VisualRecognition.VERSION_DATE_2016_05_20);
        return w.executeTask();
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        Log.e("result", "result: " + s);

        String filePath = Environment.getExternalStorageDirectory().getAbsolutePath();
        String filename = "0.jpg";
        File f = new File(filePath, filename);

        new VisualRecognitionTest(f).execute();
    }
}
