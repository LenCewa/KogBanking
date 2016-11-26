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
        String[] test1 = {"DE43 123456789","Arztrechnung", "234$", "25.11.16"};
        String[] test2= {"DE43 432094324","Einkauf", "43$","22.11.16"};
        String[] test3 = {"DE43 324989234","Versicherung", "590$","19.11.16"};
        String[] test4 = {"DE43 342789849","Miete", "1034$","19.11.16"};
        String[] test5 = {"DE43 989827498","Test Topic", "989$","16.11.16"};
        String[] test6 = {"DE43 238974832","Urlaub", "1320$","13.11.16"};

        account.add(0, test1);
        account.add(0, test2);
        account.add(0, test3);
        account.add(0, test4);
        account.add(0, test5);
        account.add(0, test6);

        profilePic = (ImageView) findViewById(R.id.profilePic);
        balance = (TextView) findViewById(R.id.balance);
        transactionButton = (Button) findViewById(R.id.transactionButton);
        olderTransactions = (ListView) findViewById(R.id.olderTransactions);

        balance.setText("20000$");
        olderTransactions.setAdapter(new CustomAdapter(this, account));
    }
}
