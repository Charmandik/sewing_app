package fishrungames.tes.utils.retrofit;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitController {
    private String BASE_URL = "https://tes-payment.fishrungames.com/";
    private Retrofit retrofit;
    private  MessagesApi messagesApi;

    public RetrofitController() {
         retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public MessagesApi getApi() {
        if(messagesApi == null) {
            messagesApi = retrofit.create(MessagesApi.class);
        }
        return  messagesApi;
    }
}
