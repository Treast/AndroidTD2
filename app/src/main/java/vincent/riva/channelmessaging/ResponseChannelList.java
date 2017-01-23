package vincent.riva.channelmessaging;

import java.util.ArrayList;

/**
 * Created by rivav on 20/01/2017.
 */
public class ResponseChannelList {
    @Override
    public String toString() {
        return "ResponseChannelList{" +
                "channels=" + channels +
                '}';
    }

    public ArrayList<ResponseChannel> getChannels() {
        return channels;
    }

    public void setChannels(ArrayList<ResponseChannel> channels) {
        this.channels = channels;
    }

    public ResponseChannelList(ArrayList<ResponseChannel> channels) {

        this.channels = channels;
    }

    private ArrayList<ResponseChannel> channels;
}
