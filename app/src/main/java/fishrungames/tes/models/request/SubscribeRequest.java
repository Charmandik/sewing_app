package fishrungames.tes.models.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import fishrungames.tes.MainActivity;
import fishrungames.tes.utils.PrefUtil;

public class SubscribeRequest {


    @SerializedName("token")
    @Expose
    private String token;

    @SerializedName("nonce")
    @Expose
    private String nonce;

    @SerializedName("licenseId")
    @Expose
    private String licenseId;

    public void setToken(String token) { this.token = token; }
    public String getToken() { return token; }

    public void setLicenseId(String licenseId) {
        this.licenseId = licenseId;
    }

    public String getLicenseId() {
        return licenseId;
    }

    public void setNonce(String nonce) {
        this.nonce = nonce;
    }

    public String getNonce() {
        return nonce;
    }
}
