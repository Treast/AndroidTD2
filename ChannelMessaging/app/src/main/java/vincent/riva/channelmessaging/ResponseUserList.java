package vincent.riva.channelmessaging;

import java.util.ArrayList;

/**
 * Created by rivav on 23/01/2017.
 */
public class ResponseUserList {
    public ArrayList<ResponseUser> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<ResponseUser> users) {
        this.users = users;
    }

    public ResponseUserList(ArrayList<ResponseUser> users) {

        this.users = users;
    }

    public ResponseUser get(int id)
    {
        for(ResponseUser user : this.users)
        {
            if(user.getUserID() == id)
            {
                return user;
            }
        }
        return new ResponseUser();
    }

    private ArrayList<ResponseUser> users;
}
