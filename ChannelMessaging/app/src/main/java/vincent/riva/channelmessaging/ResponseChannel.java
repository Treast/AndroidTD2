package vincent.riva.channelmessaging;

/**
 * Created by rivav on 20/01/2017.
 */
public class ResponseChannel {
    public int getChannelID() {
        return channelID;
    }

    @Override
    public String toString() {
        return "ResponseChannel{" +
                "channelID=" + channelID +
                ", name='" + name + '\'' +
                ", connectedusers=" + connectedusers +
                '}';
    }

    public void setChannelID(int channelID) {
        this.channelID = channelID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getConnectedusers() {
        return connectedusers;
    }

    public void setConnectedusers(int connectedusers) {
        this.connectedusers = connectedusers;
    }

    public ResponseChannel(int channelID, String name, int connectedusers) {

        this.channelID = channelID;
        this.name = name;
        this.connectedusers = connectedusers;
    }

    private int channelID;
    private String name;
    private int connectedusers;
}
