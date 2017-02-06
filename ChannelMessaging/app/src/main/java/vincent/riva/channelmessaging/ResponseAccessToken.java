package vincent.riva.channelmessaging;

public class ResponseAccessToken {
    private String response;
    private String code;
    private String accesstoken;

    public ResponseAccessToken(String response, String code, String accesstoken) {
        this.response = response;
        this.code = code;
        this.accesstoken = accesstoken;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getAccesstoken() {
        return accesstoken;
    }

    public void setAccesstoken(String accesstoken) {
        this.accesstoken = accesstoken;
    }
}
