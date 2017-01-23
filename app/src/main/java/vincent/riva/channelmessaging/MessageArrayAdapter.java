package vincent.riva.channelmessaging;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class MessageArrayAdapter extends ArrayAdapter<ResponseMessage> {

    private final Context context;

    public MessageArrayAdapter(Context context, List<ResponseMessage> objects) {
        super(context, R.layout.message_layout, objects);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ResponseMessage message = getItem(position);
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.message_layout, parent, false);
        TextView textViewUser = (TextView)rowView.findViewById(R.id.textViewUser);
        TextView textViewDate = (TextView)rowView.findViewById(R.id.textViewDate);
        TextView textViewMessage = (TextView)rowView.findViewById(R.id.textViewMessage);
        textViewUser.setText(message.getUserID()+"");
        textViewDate.setText(message.getData());
        textViewMessage.setText(message.getMessage());
        return rowView;
    }
}
