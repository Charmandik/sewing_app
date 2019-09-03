package fishrungames.tes.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class License {
    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("amount")
    @Expose
    private Integer amount;

    @SerializedName("license_expire")
    @Expose
    private Integer expire;

    @SerializedName("text")
    @Expose
    private String text;

    @SerializedName("infoLink")
    @Expose
    private String link;


    public String getTitle() {
        return this.title;
    }

    public String getExpire() {
        Integer days = this.expire / 86400;
        return days.toString() + " days";
    }

    public String getAmount() {
        return this.amount.toString();
    }

    public String getTotalPrice() {
        return  "";
    }

    public String getId() {
        return id;
    }
}
