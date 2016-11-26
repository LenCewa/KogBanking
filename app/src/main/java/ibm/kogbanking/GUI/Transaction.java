package ibm.kogbanking.GUI;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.googlecode.tesseract.android.TessBaseAPI;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import ibm.kogbanking.R;


/**
 * Created by Ludwig on 26.11.2016.
 */

public class Transaction extends Activity {
    Button scanIBAN;
    Button takePhoto;
    ImageView ibanPic;
    String datapath = "";
    Bitmap image;
    TessBaseAPI mTess;
    TextView resultText;



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
                image = BitmapFactory.decodeResource(getResources(), R.drawable.bank);
                datapath = getFilesDir()+ "/tesseract/";
                checkFile(new File(datapath + "tessdata/"));
                String lang = "eng";
                mTess = new TessBaseAPI();
                mTess.init(datapath, lang);
                mTess.setImage(image);
                String result = mTess.getUTF8Text();
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
}
