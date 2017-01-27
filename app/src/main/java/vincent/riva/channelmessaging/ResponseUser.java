package vincent.riva.channelmessaging;

/**
 * Created by rivav on 23/01/2017.
 */
public class ResponseUser {
    public ResponseUser() {
    }

    public ResponseUser(int userID, String identifiant, double lastactivity, String imageUrl) {

        this.userID = userID;
        this.identifiant = identifiant;
        this.lastactivity = lastactivity;
        this.imageUrl = imageUrl;
    }

    public String getImageUrl() {

        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getIdentifiant() {
        return identifiant;
    }

    public void setIdentifiant(String identifiant) {
        this.identifiant = identifiant;
    }

    public double getLastactivity() {
        return lastactivity;
    }

    public void setLastactivity(double lastactivity) {
        this.lastactivity = lastactivity;
    }

    private int userID;
    private String identifiant;
    private double lastactivity;
    private String imageUrl;
}
