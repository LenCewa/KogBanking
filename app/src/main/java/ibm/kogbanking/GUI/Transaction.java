package ibm.kogbanking.GUI;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.googlecode.tesseract.android.TessBaseAPI;
import com.ibm.watson.developer_cloud.text_to_speech.v1.TextToSpeech;
import com.ibm.watson.developer_cloud.text_to_speech.v1.model.AudioFormat;
import com.ibm.watson.developer_cloud.text_to_speech.v1.model.Voice;
import com.ibm.watson.developer_cloud.text_to_speech.v1.util.WaveUtils;

import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import ibm.kogbanking.R;


/**
 * Created by Ludwig on 26.11.2016.
 */

public class Transaction extends Activity {
    Button takePhoto;
    Button scanIBAN;
    ImageView ibanPic;
    TessBaseAPI mTess;
    TextView resultText;
    String datapath;



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);

        resultText = (TextView) findViewById(R.id.resultText);
        ibanPic = (ImageView)  findViewById(R.id.ibanPic);
        takePhoto = (Button) findViewById(R.id.takePhoto);

        takePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, 1);
            }
        });

        scanIBAN = (Button) findViewById(R.id.scanIBAN);

        scanIBAN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap image = ((BitmapDrawable)ibanPic.getDrawable()).getBitmap();
                filterBitMap(image);
                datapath = getFilesDir()+ "/tesseract/";
                String result = getImageText(datapath, image);
                String ibText = getIban(result);
                if(ibText != null)
                    resultText.setText(ibText);
                else
                    resultText.setText(result);
            }
        });

    }

    private void copyFile() {
        try {
            //location we want the file to be at
            String filepath = datapath + "/tessdata/eng.traineddata";
            //get access to AssetManager
            AssetManager assetManager = getAssets();
            //open byte streams for reading/writing
            InputStream instream = assetManager.open("tessdata/eng.traineddata");
            OutputStream outstream = new FileOutputStream(filepath);
            //copy the file to the location specified by filepath
            byte[] buffer = new byte[1024];
            int read;
            while ((read = instream.read(buffer)) != -1) {
                outstream.write(buffer, 0, read);
            }
            outstream.flush();
            outstream.close();
            instream.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void checkFile(File dir) {
        //directory does not exist, but we can successfully create it
        if (!dir.exists()&& dir.mkdirs()){
            copyFile();
        }
        //The directory exists, but there is no data file in it
        if(dir.exists()) {
            String datafilepath = datapath+ "/tessdata/eng.traineddata";
            File datafile = new File(datafilepath);
            if (!datafile.exists()) {
                copyFile();
            }
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            ibanPic.setImageBitmap(photo);
        }
    }

    public Bitmap filterBitMap(Bitmap bm){
        Bitmap bitmap = bm;
        Mat imageMat = new Mat();
        Utils.bitmapToMat(bitmap, imageMat);
        Imgproc.cvtColor(imageMat, imageMat, Imgproc.COLOR_BGR2GRAY);
        Imgproc.GaussianBlur(imageMat, imageMat, new Size(3, 3), 0);
        Imgproc.adaptiveThreshold(imageMat, imageMat, 255, Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY, 5, 4);
        Utils.matToBitmap(imageMat, bitmap);
        BitmapDrawable bd = new BitmapDrawable(getResources(),bitmap);
        ibanPic.setImageDrawable(bd);
        imageMat = null;
        return bitmap;
    }

    public String getImageText(String path, Bitmap bm){
        String result;
        checkFile(new File(datapath + "tessdata/"));
        String lang = "eng";
        mTess = new TessBaseAPI();
        mTess.init(datapath, lang);
        mTess.setImage(bm);
        result = mTess.getUTF8Text();
        mTess.end();

        return result;
    }

    public String getIban(String input){
        String result = null;

        for(int i = 0; i < input.length(); i++){
            Character c = input.charAt(i);
            if(i > 0 && input.charAt(i - 1) == 'D' && input.charAt(i) == 'E')
                if(i + 26 < input.length())
                    result = input.substring(i - 1, i + 26);
        }
        return result;
    }

}
