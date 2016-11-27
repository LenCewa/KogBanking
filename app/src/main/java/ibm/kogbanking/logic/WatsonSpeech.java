package ibm.kogbanking.logic;

import android.app.Activity;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.media.MediaPlayer;
import android.net.rtp.AudioStream;
import android.os.AsyncTask;
import android.os.Environment;

import com.ibm.watson.developer_cloud.http.ServiceCall;
import com.ibm.watson.developer_cloud.text_to_speech.v1.model.AudioFormat;
import com.ibm.watson.developer_cloud.text_to_speech.v1.model.Voice;
import com.ibm.watson.developer_cloud.text_to_speech.v1.util.WaveUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Yannick on 27.11.2016.
 */

public class WatsonSpeech extends AsyncTask<Void, Void, Void> {

    Activity callingActivity;
    String text;

    public WatsonSpeech(Activity callingActivity, String text){
        this.callingActivity = callingActivity;
        this.text = text;
    }
    @Override
    protected Void doInBackground(Void... params) {
            com.ibm.watson.developer_cloud.text_to_speech.v1.TextToSpeech service = new com.ibm.watson.developer_cloud.text_to_speech.v1.TextToSpeech();
            service.setUsernameAndPassword("00096487-b7f8-4e2d-a101-9d1f01af5254", "wRDRSiROgjbQ");

            try {
                ServiceCall<InputStream> stream = service.synthesize(text, Voice.EN_ALLISON);

                InputStream in = WaveUtils.reWriteWaveHeader(stream.execute());

                File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "helloWorld.wav");
                OutputStream out = new FileOutputStream(file);
                byte[] buffer = new byte[1024];
                try
                {
                    int length;
                    while ((length = in.read(buffer)) > 0) {
                        out.write(buffer, 0, length);
                    }
                }
                catch (IOException ex)
                {
                    String s = ex.toString();
                    ex.printStackTrace();
                }
                out.close();

                int i = 0;
                int size = AudioTrack.getMinBufferSize(44100, android.media.AudioFormat.CHANNEL_CONFIGURATION_MONO, android.media.AudioFormat.ENCODING_PCM_16BIT);

                AudioTrack a = new AudioTrack(AudioManager.STREAM_MUSIC, 44100,
                        android.media.AudioFormat.CHANNEL_CONFIGURATION_MONO, android.media.AudioFormat.ENCODING_PCM_16BIT, size, AudioTrack.MODE_STREAM);
                byte[] music = new byte[512];
                a.play();
                while((i = in.read(music)) != -1)
                    a.write(music, 0, i);

                in.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        return null;
    }
}
