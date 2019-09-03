package fishrungames.tes.models.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import fishrungames.tes.models.User;

public class LoginResponse {
    @SerializedName("result")
    @Expose
    private Boolean result;
    @SerializedName("token")
    @Expose
    private String token;
    @SerializedName("user")
    @Expose
    private User user;
    @SerializedName("message")
    @Expose
    private String message;

    public Boolean getResult() {
        return result;
    }
    public void setResult(Boolean result) {
        this.result = result;
    }

    public String getToken() {return token;}
    public void setToken(String token) {
        this.token = token;
    }

    public User getUser() {return user;}
    public void setUser(User user) {this.user = user;}

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}