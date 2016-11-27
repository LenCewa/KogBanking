package ibm.kogbanking.GUI;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Environment;
import android.provider.MediaStore;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

import ibm.kogbanking.logic.SaveImages;
import ibm.kogbanking.logic.VisualRecognitionTest;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import ibm.kogbanking.R;
import ibm.kogbanking.logic.WatsonSpeech;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity{

    public boolean accountSet = false;
    String classifier;
    public static boolean login = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (!OpenCVLoader.initDebug()) {
            // Handle initialization error
        }
        SharedPreferences sp = getSharedPreferences("classifier", Activity.MODE_PRIVATE);
        int count = sp.getInt("count", 0);
        if(count == 0) {

            SharedPreferences.Editor e = sp.edit();
            count++;
            e.putInt("count", count);
            e.commit();

            classifier = sp.getString("classifier", "");
            if (!classifier.equals("")) {
                accountSet = true;
            }

            if (accountSet) {
                new WatsonSpeech(this, "Please hold your phone in front of your face and take a photo of yourself", accountSet).execute();

            } else {
                new WatsonSpeech(this, "Please hold your phone in front of your face and take a video of yourself so you can log in with your face next time", accountSet).execute();

            }
        }
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK) {
            Uri uri = data.getData();

            if(!accountSet)
                new SaveImages(this).execute(uri);
            else {
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                faceDetector(imageBitmap);
                String file_path = Environment.getExternalStorageDirectory().getAbsolutePath();
                String filename = "login.png";
                File f = new File(file_path, filename);
                new VisualRecognitionTest(this, f, classifier).execute();
            }
        }
    }

    public String faceDetector(Bitmap bmp){
        InputStream is = getResources().openRawResource(R.raw.haarcascade_frontalface_alt);
        File cascadeDir = getDir("cascade", Context.MODE_PRIVATE);
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
        String filename = file_path + "/" + "login" + ".png";
        Imgcodecs.imwrite(filename, new Mat(image, rect));

        return filename;
    }
}

