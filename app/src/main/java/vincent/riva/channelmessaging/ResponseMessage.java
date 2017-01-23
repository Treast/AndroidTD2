package vincent.riva.channelmessaging;

/**
 * Created by rivav on 20/01/2017.
 */
public class ResponseMessage {
    private int userID;
    private String message;
    private String data;
    private String imageURL;

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public ResponseMessage(int userID, String message, String data, String imageURL) {

        this.userID = userID;
        this.message = message;
        this.data = data;
        this.imageURL = imageURL;
    }
}
