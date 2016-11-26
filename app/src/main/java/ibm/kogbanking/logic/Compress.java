package ibm.kogbanking.logic;

/**
 * Created by Yannick on 26.11.2016.
 */

import android.app.Activity;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;


public class Compress {
    private static final int BUFFER = 2048;

    Activity callingActivity;
    private String[] _files;
    private String _zipFile;

    public Compress(Activity call, String[]files, String zipFile) {
        callingActivity = call;
        _files = files;
        _zipFile = zipFile;
    }

    public void zip() {
        try  {
            BufferedInputStream origin = null;
            String file_path = Environment.getExternalStorageDirectory().getAbsolutePath()+ "/" +  _zipFile;
            FileOutputStream dest = new FileOutputStream(file_path);

            ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(dest));

            byte data[] = new byte[BUFFER];

            for(int i=0; i < _files.length; i++) {
                Log.v("Compress", "Adding: " + _files[i]);
                FileInputStream fi = new FileInputStream(_files[i]);
                origin = new BufferedInputStream(fi, BUFFER);
                ZipEntry entry = new ZipEntry(_files[i].substring(_files[i].lastIndexOf("/") + 1));
                out.putNextEntry(entry);
                int count;
                while ((count = origin.read(data, 0, BUFFER)) != -1) {
                    out.write(data, 0, count);
                }
                origin.close();
                new File(_files[i]).delete();
            }

            out.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

}
