package fishrungames.tes.models;

import android.os.Debug;
import android.util.Log;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Message {
    @SerializedName("username")
    @Expose
    private String[] username;
    @SerializedName("firstName")
    @Expose
    private String[] firstName;
    @SerializedName("lastName")
    @Expose
    private String[] lastName;
    @SerializedName("password")
    @Expose
    private String[] password;
    @SerializedName("email")
    @Expose
    private String[] email;

    public String getUsername() {
        if (this.username != null)
            return this.username[0];
        return null;
    }

    public String getFirstName() {
        if (this.firstName != null)
            return this.firstName[0];
        return null;
    }

    public String getEmail() {
        if (this.email != null)
            return this.email[0];
        return null;
    }

    public String getLastName() {
        if (this.lastName != null)
            return this.lastName[0];
        return null;
    }

    public String getPassword() {
        if (this.password != null)
            return this.password[0];
        return null;
    }
}
