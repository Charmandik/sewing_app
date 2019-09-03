package fishrungames.tes.models.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RegistryRequest {
    @SerializedName("username")
    @Expose
    public String username;
    @SerializedName("email")
    @Expose
    public String email;
    @SerializedName("password")
    @Expose
    public String password;
    @SerializedName("firstName")
    @Expose
    public String firstName;
    @SerializedName("lastName")
    @Expose
    public String lastName;
}
