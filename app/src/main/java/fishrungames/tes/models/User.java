package fishrungames.tes.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class User {

    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("customerId")
    @Expose
    private int customerId;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("firstName")
    @Expose
    private String firstName;
    @SerializedName("lastName")
    @Expose
    private String lastName;
    @SerializedName("isVerified")
    @Expose
    private Boolean isVerified;
    @SerializedName("license")
    @Expose
    private String license;
    @SerializedName("isLicenseValid")
    @Expose
    private Boolean isLicenseValid;
    @SerializedName("licenseLeft")
    @Expose
    private String licenseLeft;
    @SerializedName("licenseTimestamp")
    @Expose
    private int licenseTimestamp;
    @SerializedName("isAutoPayment")
    @Expose
    private Boolean isAutoPayment;

    public String getUsername(){ return username;}
    public void setUsername(String username) { this.username = username;}

    public int getCustomerId(){ return customerId;}
    public void setCustomerId(int customerId){ this.customerId = customerId;}

    public String getEmail(){ return email;}
    public void setEmail(String email){ this.email = email;}

    public String getFirstName(){ return firstName;}
    public void setFirstName(String firstName){ this.firstName = firstName;}

    public String getLastName(){ return lastName;}
    public void setLastName(String lastName){ this.lastName = lastName;}

    public String getIsVerified(){
        if(this.isVerified)
            return "Email is Verified";
        return "Email is Not Verified";
    }
    public void setIsVerified(Boolean isVerified){ this.isVerified = isVerified;}

    public String getLicense(){ return license;}
    public void setLicense(String license){ this.license = license;}

    public Boolean getIsLicenseValid(){ return isLicenseValid;}
    public void setIsLicenseValid(Boolean isLicenseValid){ this.isLicenseValid = isLicenseValid;}

    public String getLicenseLeft(){ return licenseLeft;}
    public void setLicenseLeft(String licenseLeft){ this.licenseLeft = licenseLeft;}

    public int getLicenseTimestamp(){ return licenseTimestamp;}
    public void setLicenseTimestamp(int licenseTimestamp){ this.licenseTimestamp = licenseTimestamp;}

    public Boolean getLicenseValid() {
        return isLicenseValid;
    }

    public void setIsAutoPaymentEnabled(Boolean isAutoPaymentEnabled) {
        this.isAutoPayment = isAutoPaymentEnabled;
    }

    public Boolean getIsAutoPaymentEnabled() {
        return isAutoPayment;
    }
}
