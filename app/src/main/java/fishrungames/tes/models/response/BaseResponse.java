package fishrungames.tes.models.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BaseResponse {

    @SerializedName("result")
    @Expose
    private Boolean result;

    @SerializedName("message")
    @Expose
    private String message;

    public void setMessage(String message) { this.message = message; }
    public String getMessage() { return message; }

    public void setResult(Boolean result) { this.result = result; }
    public Boolean getResult() { return result; }



}
