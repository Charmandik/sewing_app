package fishrungames.tes.models.response;

import android.util.Log;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.lang.reflect.Method;

import fishrungames.tes.models.Message;
import fishrungames.tes.models.User;

public class RegistryResponse {
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
    private Message message;

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

    public String getMessage(){
       if(this.message.getEmail() != null)
               return this.message.getEmail();

       if((!message.getLastName().equals("")) && (message.getLastName() != null))
           return this.message.getLastName();

        if((!message.getFirstName().equals(""))&&(message.getFirstName() != null))
            return this.message.getFirstName();

        if((!message.getPassword().equals(""))&& (message.getPassword() != null))
            return this.message.getPassword();
        if(message.getUsername()!=null)
            return this.message.getUsername();
        return "Error";

    }
    public void setMessage(Message message) {this.message = message;}


}