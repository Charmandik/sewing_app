package fishrungames.tes.models.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ForgotPassRequest {
    @SerializedName("email")
    @Expose
    public String email;
}
