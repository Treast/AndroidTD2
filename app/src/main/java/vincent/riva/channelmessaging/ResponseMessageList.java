package vincent.riva.channelmessaging;

import java.util.ArrayList;

/**
 * Created by rivav on 20/01/2017.
 */
public class ResponseMessageList {
    private ArrayList<ResponseMessage> messages;

    public ArrayList<ResponseMessage> getMessages() {
        return messages;
    }

    public void setMessages(ArrayList<ResponseMessage> messages) {
        this.messages = messages;
    }

    public ResponseMessageList(ArrayList<ResponseMessage> messages) {

        this.messages = messages;
    }
}
