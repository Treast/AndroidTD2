package vincent.riva.channelmessaging;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class ChannelArrayAdapter extends ArrayAdapter<ResponseChannel> {

    private final Context context;

    public ChannelArrayAdapter(Context context, List<ResponseChannel> objects) {
        super(context, R.layout.channel_layout, objects);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ResponseChannel channel = getItem(position);
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.channel_layout, parent, false);
        TextView textViewName = (TextView)rowView.findViewById(R.id.textViewName);
        TextView textViewConnectedUsers = (TextView)rowView.findViewById(R.id.textViewConnectedUsers);
        textViewName.setText(channel.getName());
        textViewConnectedUsers.setText(channel.getConnectedusers()+" utilisateurs connectés");
        return rowView;
    }
}
