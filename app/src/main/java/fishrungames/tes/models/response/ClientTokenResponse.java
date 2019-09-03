package fishrungames.tes.models.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ClientTokenResponse {

    @SerializedName("result")
    @Expose
    public Boolean result;

    @SerializedName("clientToken")
    @Expose
    public String clientToken;

}
