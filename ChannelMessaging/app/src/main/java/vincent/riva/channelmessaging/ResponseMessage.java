package vincent.riva.channelmessaging;

/**
 * Created by rivav on 20/01/2017.
 */
public class ResponseMessage {
    private int userID;
    private String message;
    private String data;
    private String imageUrl;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public ResponseMessage(int userID, String message, String data, String imageUrl, String username) {

        this.userID = userID;
        this.message = message;
        this.data = data;
        this.imageUrl = imageUrl;
        this.username = username;
    }

    private String username;

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

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public ResponseMessage(int userID, String message, String data, String imageUrl) {

        this.userID = userID;
        this.message = message;
        this.data = data;
        this.imageUrl = imageUrl;
    }
}
