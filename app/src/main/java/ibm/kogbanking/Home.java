package ibm.kogbanking;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Ludwig on 26.11.2016.
 */

public class Home extends Activity {
    ArrayList<String[]> account;
    ImageView profilePic;
    TextView balance;
    Button transactionButton;
    ListView olderTransactions;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        account = new ArrayList<String[]>();
        String[] test = {"DE43 123456789","Test Topic", "100$"};
        account.add(0, test);

        profilePic = (ImageView) findViewById(R.id.profilePic);
        balance = (TextView) findViewById(R.id.balance);
        transactionButton = (Button) findViewById(R.id.transactionButton);
        olderTransactions = (ListView) findViewById(R.id.olderTransactions);

        olderTransactions.setAdapter(new CustomAdapter(this, account));
    }
}
