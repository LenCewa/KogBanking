package Logic;

import android.os.AsyncTask;
import android.os.Environment;

import com.ibm.watson.developer_cloud.http.HttpMediaType;
import com.ibm.watson.developer_cloud.http.RequestBuilder;
import com.ibm.watson.developer_cloud.http.ServiceCall;
import com.ibm.watson.developer_cloud.service.WatsonService;
import com.ibm.watson.developer_cloud.util.ResponseConverterUtils;
import com.ibm.watson.developer_cloud.util.Validator;
import com.ibm.watson.developer_cloud.http.ServiceCall;

import java.io.File;
import java.util.HashMap;

import okhttp3.MultipartBody;


/**
 * Created by Yannick on 26.11.2016.
 */

import com.ibm.watson.developer_cloud.visual_recognition.v3.VisualRecognition;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.ClassifierOptions;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.ClassifyImagesOptions;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.VisualClassification;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.VisualClassifier;

import okhttp3.MultipartBody;
import okhttp3.MultipartBody.Builder;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

public class WatsonPost extends VisualRecognition {

    ServiceCall s;
    /**
     * Instantiates a new Watson service.
     *
     * @param name the service name
     */
    public WatsonPost(String name) {
        super(name);

        String filePath = Environment.getExternalStorageDirectory().getAbsolutePath();
        String filename = "positives.zip";

        File positives = new File(filePath,filename);

        String filePath2 = Environment.getExternalStorageDirectory().getAbsolutePath();
        String filename2 = "negatives.zip";

        File negatives = new File(filePath2,filename2);

        setApiKey("fb4a8bba6e9887fb22430cdcca8d181a3ac55711");
        ClassifierOptions options = new ClassifierOptions.Builder()
                .classifierName("person")
                .addClass("user", positives)
                .negativeExamples(negatives)
                .build();
        s = createClassifier(options);
    }

    public String executeTask() {

        VisualClassifier v = (VisualClassifier) s.execute();
        return v.toString();
    }


    public ServiceCall createClassifier(ClassifierOptions options) {
        Validator.notNull(options, " options cannot be null");

        Builder bodyBuilder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        bodyBuilder.addFormDataPart("name", options.classifierName());

        // Classes
            String dataName = "user" + "_" + "positive_examples";
            RequestBody requestBody2 =
                    RequestBody.create(HttpMediaType.BINARY_FILE, options.positiveExamplesByClassName("user"));
            bodyBuilder.addFormDataPart(dataName, options.positiveExamplesByClassName("user").getName(), requestBody2);

        if (options.negativeExamples() != null) {
            RequestBody requestBody = RequestBody.create(HttpMediaType.BINARY_FILE, options.negativeExamples());
            bodyBuilder.addFormDataPart("negative_examples", options.negativeExamples().getName(), requestBody);
        }

        RequestBuilder requestBuilder = RequestBuilder.post("https://gateway-a.watsonplatform.net/visual-recognition/api/v3/classifiers");
        requestBuilder.query("api_key", "fb4a8bba6e9887fb22430cdcca8d181a3ac55711").body(bodyBuilder.build());
        requestBuilder.query("version", "2016-05-20").body(bodyBuilder.build());

        return createServiceCall(requestBuilder.build(), ResponseConverterUtils.getObject(VisualClassifier.class));
    }
}
