package vincent.riva.channelmessaging;

/**
 * Created by rivav on 20/01/2017.
 */
public class ResponseMessage {
    private int userID;
    private String message;
    private String date;
    private String imageUrl;
    private String messageImageUrl;

    public String getMessageImageUrl() {
        return messageImageUrl;
    }

    public void setMessageImageUrl(String messageImageUrl) {
        this.messageImageUrl = messageImageUrl;
    }

    public ResponseMessage(int userID, String message, String date, String imageUrl, String messageImageUrl, String username) {

        this.userID = userID;
        this.message = message;
        this.date = date;
        this.imageUrl = imageUrl;
        this.messageImageUrl = messageImageUrl;
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public ResponseMessage(int userID, String message, String date, String imageUrl, String username) {

        this.userID = userID;
        this.message = message;
        this.date = date;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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
        this.date = data;
        this.imageUrl = imageUrl;
    }
}
