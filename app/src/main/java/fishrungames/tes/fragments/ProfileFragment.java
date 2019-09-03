package fishrungames.tes.fragments;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

import fishrungames.tes.MainActivity;
import fishrungames.tes.R;
import fishrungames.tes.models.request.ActivatePromoRequest;
import fishrungames.tes.models.request.ChangePassRequest;
import fishrungames.tes.models.request.UnsubscribeRequest;
import fishrungames.tes.models.response.BaseResponse;
import fishrungames.tes.models.response.ProfileResponse;
import fishrungames.tes.utils.PrefUtil;
import fishrungames.tes.utils.retrofit.MessagesApi;
import fishrungames.tes.utils.retrofit.RetrofitController;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileFragment extends Fragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        MainActivity.getInstance().profileFragment = this;
        return inflater.inflate(R.layout.profile_fragment, null, false);
    }

    private void setEnabledClicks(ArrayList<View> btnArray, Boolean expression)
    {
        for(View v : btnArray) {
            v.setEnabled(expression);
        }
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RetrofitController controller = new RetrofitController();
        final MessagesApi messagesApi = controller.getApi();
        final TextView username = getView().findViewById(R.id.profileUsernameInterlinearText);
        final TextView email = getView().findViewById(R.id.profileEmailInterlinearText);
        final TextView license = getView().findViewById(R.id.profileLicenseInterlinearText);
        final TextView status = getView().findViewById(R.id.profileStatusInterlinearText);
        final TextView licenseLeft = getView().findViewById(R.id.profileLicenceLeftInterlinearText);
        final TextView promoCode = getView().findViewById(R.id.promo_editText);
        final TextView changePassText = getView().findViewById(R.id.changePassText);
        final TextView changePassConfirmText = getView().findViewById(R.id.changePassConfirmText);
        final Button changePassBtn = getView().findViewById(R.id.changePassBtn);
        final Button promoBtn = getView().findViewById(R.id.promo_btn);
        final ArrayList<View> layoutButtons = new ArrayList<>();
        layoutButtons.add(changePassBtn);
        layoutButtons.add(promoBtn);

        Call<ProfileResponse> responses = messagesApi.getProfile(PrefUtil.getToken(MainActivity.getInstance()));
        MainActivity.getInstance().showProgressBar();
        responses.enqueue(new Callback<ProfileResponse>() {
            @Override
            public void onResponse(Call<ProfileResponse> call, Response<ProfileResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (!response.body().getResult()) {
                        PrefUtil.setToken(MainActivity.getInstance(), "");
                        MainActivity.getInstance().profileFragment = null;
                        MainActivity.getInstance().setLoginFragment();
                        if(!response.body().getMessage().equals("Invalid token"))
                         Toast.makeText(MainActivity.getInstance(), response.body().getMessage(), Toast.LENGTH_LONG).show();
                        // todo: check ISSUE#71
                    } else {
                        username.setText(response.body().getUser().getUsername());
                        email.setText(response.body().getUser().getEmail());
                        license.setText(response.body().getUser().getLicense());
                        licenseLeft.setText(response.body().getUser().getLicenseLeft());
                        if (response.body().getUser().getIsAutoPaymentEnabled())
                            status.setText("Active");
                        else
                            status.setText("Not Active");

                        if (response.body().getUser().getIsAutoPaymentEnabled())
                            MainActivity.getInstance().isUserSubscribed = true;
                        else
                            MainActivity.getInstance().isUserSubscribed = false;

                        MainActivity.getInstance().updateMenuTitles();

                    }
                } else {
                    Log.d("response code ", "Error");
                }
                MainActivity.getInstance().hideProgressBar();
                setEnabledClicks(layoutButtons,true);
            }

            @Override
            public void onFailure(Call<ProfileResponse> call, Throwable t) {
                Toast.makeText(MainActivity.getInstance(), t.getMessage(), Toast.LENGTH_LONG).show();
                MainActivity.getInstance().hideProgressBar();
                setEnabledClicks(layoutButtons,true);
                Log.d("failure ", "Respond Failed");
            }
        });

        changePassBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setEnabledClicks(layoutButtons,false);
                MainActivity.getInstance().showProgressBar();

                if (changePassText.getText().toString().equals(changePassConfirmText.getText().toString())) {
                    ChangePassRequest changePassRequest = new ChangePassRequest();
                    changePassRequest.setPassword(changePassText.getText().toString());
                    changePassRequest.setToken(PrefUtil.getToken(MainActivity.getInstance()));
                    Call<BaseResponse> responses = messagesApi.changePassword(changePassRequest);
                    responses.enqueue(new Callback<BaseResponse>() {
                        @Override
                        public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                            if (response.isSuccessful() && response.body() != null && response.body().getResult()) {
                                Toast.makeText(MainActivity.getInstance(), "Password changed", Toast.LENGTH_LONG).show();
                            } else
                                Toast.makeText(MainActivity.getInstance(), response.body().getMessage(), Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onFailure(Call<BaseResponse> call, Throwable t) {
                            Toast.makeText(MainActivity.getInstance(), t.getMessage(), Toast.LENGTH_LONG).show();
                            Log.e("Failure", "Respond Failed" + t.getMessage());
                        }
                    });
                } else
                    Toast.makeText(MainActivity.getInstance(), "Passwords must match", Toast.LENGTH_LONG).show();

                MainActivity.getInstance().hideProgressBar();
                setEnabledClicks(layoutButtons,true);
            }
        });

        promoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setEnabledClicks(layoutButtons,false);
                MainActivity.getInstance().showProgressBar();
                ActivatePromoRequest activatePromoRequest = new ActivatePromoRequest();
                activatePromoRequest.setCode(promoCode.getText().toString());
                activatePromoRequest.setToken(PrefUtil.getToken(MainActivity.getInstance()));
                Call<BaseResponse> responses = messagesApi.activatePromoCode(activatePromoRequest);
                responses.enqueue(new Callback<BaseResponse>() {
                    @Override
                    public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                        if (response.isSuccessful() && response.body() != null && response.body().getResult()) {
                            Toast.makeText(MainActivity.getInstance(), "Promo code successfully activated", Toast.LENGTH_LONG).show();


                            //UPDATING LICENSE LEFT FIELDS IN PROFILE
                            Call<ProfileResponse> responses = messagesApi.getProfile(PrefUtil.getToken(MainActivity.getInstance()));
                            responses.enqueue(new Callback<ProfileResponse>() {
                                @Override
                                public void onResponse(Call<ProfileResponse> call, Response<ProfileResponse> response) {
                                    if (response.isSuccessful() && response.body() != null) {
                                        if (!response.body().getResult())
                                            Toast.makeText(MainActivity.getInstance(), response.body().getMessage(), Toast.LENGTH_LONG).show();
                                        else
                                            licenseLeft.setText(response.body().getUser().getLicenseLeft());
                                    } else {
                                        Log.d("response code ", "Error");
                                    }
                                    MainActivity.getInstance().hideProgressBar();
                                    setEnabledClicks(layoutButtons,true);
                                }

                                @Override
                                public void onFailure(Call<ProfileResponse> call, Throwable t) {
                                    Toast.makeText(MainActivity.getInstance(), t.getMessage(), Toast.LENGTH_LONG).show();
                                    MainActivity.getInstance().hideProgressBar();
                                    setEnabledClicks(layoutButtons,true);
                                    Log.d("failure ", "Respond Failed");
                                }
                            });
                        } else
                            Toast.makeText(MainActivity.getInstance(), response.body().getMessage(), Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onFailure(Call<BaseResponse> call, Throwable t) {
                        Toast.makeText(MainActivity.getInstance(), t.getMessage(), Toast.LENGTH_LONG).show();
                        Log.e("Failure", "Respond Failed" + t.getMessage());
                    }
                });

                MainActivity.getInstance().hideProgressBar();
                setEnabledClicks(layoutButtons,true);
            }
        });

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        MainActivity.getInstance().profileFragment = null;
    }
}
