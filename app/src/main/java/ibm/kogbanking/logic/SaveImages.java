package ibm.kogbanking.logic;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;

import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import ibm.kogbanking.R;

/**
 * Created by Yannick on 26.11.2016.
 */

public class SaveImages extends AsyncTask<Uri, Object, Object> {

    Activity callingActivity;
    ProgressDialog loading;
    String[] files = new String[10];

    public SaveImages(Activity callingActivity){
        this.callingActivity = callingActivity;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        loading = ProgressDialog.show(callingActivity, "Processing Images...", null,true,true);
        loading.setCancelable(false);
    }

    @Override
    protected Object doInBackground(Uri... params) {

        Uri uri = params[0];

        File sdcard = Environment.getExternalStorageDirectory();

        MediaMetadataRetriever retriever = new MediaMetadataRetriever();

        try {
            retriever.setDataSource(callingActivity, uri);

            for (int i = 0; i < 10; i++)
                files[i] = faceDetector(retriever.getFrameAtTime(i * 250000, MediaMetadataRetriever.OPTION_CLOSEST),i);

        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
        } catch (RuntimeException ex) {
            ex.printStackTrace();
        } finally {
            try {
                retriever.release();
            } catch (RuntimeException ex) {
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(Object aVoid) {
        super.onPostExecute(aVoid);
        loading.dismiss();
        new Compress(callingActivity, files, "positives.zip").zip();
        new WatsonAsync(callingActivity).execute();
    }

    public String faceDetector(Bitmap bmp, int i){
        InputStream is = callingActivity.getResources().openRawResource(R.raw.haarcascade_frontalface_alt);
        File cascadeDir = callingActivity.getDir("cascade", Context.MODE_PRIVATE);
        File mCascadeFile = new File(cascadeDir, "haarcascade_frontalface_alt.xml");
        try {
            FileOutputStream os = new FileOutputStream(mCascadeFile);

            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = is.read(buffer)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            is.close();
            os.close();
        }
        catch (IOException e){
            e.printStackTrace();
        }

        CascadeClassifier faceDetector = new CascadeClassifier(mCascadeFile.getAbsolutePath());
        Mat image = new Mat();
        Utils.bitmapToMat(bmp, image);
        Imgproc.cvtColor(image, image, Imgproc.COLOR_BGR2RGB);

        MatOfRect faceDetections = new MatOfRect();
        faceDetector.detectMultiScale(image, faceDetections);

        Rect rect = new Rect();
        for (Rect r : faceDetections.toArray()) {
            rect = r;
        }

        String file_path = Environment.getExternalStorageDirectory().getAbsolutePath();
        String filename = file_path + "/" + "output" + i + ".png";
        Imgcodecs.imwrite(filename, new Mat(image, rect));

        return filename;
    }
}
