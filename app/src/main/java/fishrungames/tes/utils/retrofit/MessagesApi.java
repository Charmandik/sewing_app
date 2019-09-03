package fishrungames.tes.utils.retrofit;

import fishrungames.tes.models.request.ActivatePromoRequest;
import fishrungames.tes.models.request.ChangePassRequest;
import fishrungames.tes.models.request.ForgotPassRequest;
import fishrungames.tes.models.request.LoginRequest;
import fishrungames.tes.models.request.RegistryRequest;
import fishrungames.tes.models.request.SubscribeRequest;
import fishrungames.tes.models.request.UnsubscribeRequest;
import fishrungames.tes.models.response.BaseResponse;
import fishrungames.tes.models.response.ClientTokenResponse;
import fishrungames.tes.models.response.ForgotPassResponse;
import fishrungames.tes.models.response.LicensesResponse;
import fishrungames.tes.models.response.LoginResponse;
import fishrungames.tes.models.response.ProfileResponse;
import fishrungames.tes.models.response.RegistryResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface MessagesApi {

        @POST("/api/v1/user/login")
        Call<LoginResponse> login(@Body LoginRequest loginRequest);

        @POST("/api/v1/user/register")
        Call<RegistryResponse> registration(@Body RegistryRequest registryRequest);

        @POST("/api/v1/user/reset")
        Call<ForgotPassResponse> forgotPass(@Body ForgotPassRequest forgotPassRequest);

        @GET("/api/v1/user/clienttoken")
        Call<ClientTokenResponse> getClientToken(@Query("token") String token);

        @GET("/api/v1/licenses")
        Call<LicensesResponse> getLicenses(@Query("token") String token);

        @POST("/api/v1/user/subscribe")
        Call<BaseResponse> subscribe(@Body SubscribeRequest subscribeRequest);

        @POST("/api/v1/user/unsubscribe")
        Call<BaseResponse> unsubscribe(@Body UnsubscribeRequest unsubscribeRequest);

        @GET("api/v1/user")
        Call<ProfileResponse> getProfile(@Query("token") String token);

        @POST("/api/v1/user/password")
        Call<BaseResponse> changePassword(@Body ChangePassRequest changePassRequest);

        @POST("/api/v1/user/promocode")
        Call<BaseResponse> activatePromoCode(@Body ActivatePromoRequest activatePromoRequest);

}
//https://tes-payment.fishrungames.com/api/