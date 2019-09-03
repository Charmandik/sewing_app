package fishrungames.tes.models.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import fishrungames.tes.models.License;

public class LicensesResponse {
    @SerializedName("result")
    @Expose
    public Boolean result;

    @SerializedName("message")
    @Expose
    public String message;

    @SerializedName("licenses")
    @Expose
    public License[] licenses;
}
