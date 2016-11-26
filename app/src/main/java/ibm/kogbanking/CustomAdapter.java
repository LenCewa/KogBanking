package ibm.kogbanking;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;


public class CustomAdapter extends BaseAdapter {
    ArrayList<String[]> data;
    private static LayoutInflater inflater = null;
    Context context;

    public CustomAdapter(Context context, ArrayList<String[]> data) {
        this.context = context;
        this.data = data;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = inflater.inflate(R.layout.custom_adapter, null);

        TextView transID = (TextView) convertView.findViewById(R.id.transID);
        TextView topic = (TextView) convertView.findViewById(R.id.topic);
        TextView amount = (TextView) convertView.findViewById(R.id.amount);

        transID.setText(data.get(position)[0]);
        topic.setText(data.get(position)[1]);
        amount.setText(data.get(position)[2]);

        return convertView;
    }
}