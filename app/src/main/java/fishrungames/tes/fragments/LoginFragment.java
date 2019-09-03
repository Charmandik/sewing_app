package fishrungames.tes.fragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import fishrungames.tes.MainActivity;
import fishrungames.tes.R;
import fishrungames.tes.models.request.LoginRequest;
import fishrungames.tes.models.response.LoginResponse;
import fishrungames.tes.utils.PrefUtil;
import fishrungames.tes.utils.retrofit.MessagesApi;
import fishrungames.tes.utils.retrofit.RetrofitController;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class LoginFragment extends Fragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        MainActivity.getInstance().loginFragment = this;
        return inflater.inflate(R.layout.login_fragment, null, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        ((EditText) getActivity().findViewById(R.id.edit_user)).setText("");
        ((EditText) getActivity().findViewById(R.id.edit_password)).setText("");
    }

    private void setEnabledClicks(ArrayList<View> btnArray,Boolean expression)
    {
        for(View v : btnArray) {
            if(v instanceof TextView)
                v.setClickable(expression);
            v.setEnabled(expression);
        }
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        final TextView regBtn = view.findViewById(R.id.registrationText);
        final TextView forgotPassBtn = view.findViewById(R.id.forgotPassText);
        final Button logBtn = view.findViewById(R.id.button_login);
        final ArrayList<View> layoutButtons = new ArrayList<>();
        layoutButtons.add(regBtn);
        layoutButtons.add(forgotPassBtn);
        layoutButtons.add(logBtn);

        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setEnabledClicks(layoutButtons,false);
                MainActivity.getInstance().setRegistrationFragment();
                setEnabledClicks(layoutButtons,true);
            }
        });


        forgotPassBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setEnabledClicks(layoutButtons,false);
                MainActivity.getInstance().setForgotPassFragment();
                setEnabledClicks(layoutButtons,true);
            }
        });


        logBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setEnabledClicks(layoutButtons,false);
                MainActivity.getInstance().showProgressBar();
                LoginRequest loginRequest = new LoginRequest();
                EditText username = getActivity().findViewById(R.id.edit_user);
                EditText password = getActivity().findViewById(R.id.edit_password);
                loginRequest.login = username.getText().toString();
                loginRequest.password = password.getText().toString();
                RetrofitController controller = new RetrofitController();
                MessagesApi messagesApi = controller.getApi();
                Call<LoginResponse> responses = messagesApi.login(loginRequest);

                responses.enqueue(new Callback<LoginResponse>() {
                    @Override
                    public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                        if (response.isSuccessful() && response.body() != null && response.body().getResult()) {
                            PrefUtil.setToken(MainActivity.getInstance(), response.body().getToken());
                            if (!response.body().getResult())
                                Toast.makeText(getActivity().getApplicationContext(), response.body().getMessage(), Toast.LENGTH_LONG).show();
                            else {
                                ((AppCompatActivity) getActivity()).getSupportActionBar().show();
                                MainActivity.getInstance().showGlView();
                            }
                        } else {
                            Toast.makeText(getActivity().getApplicationContext(), response.body().getMessage(), Toast.LENGTH_LONG).show();
                            Log.d("response code ", "Error");
                        }
                        MainActivity.getInstance().hideProgressBar();
                        setEnabledClicks(layoutButtons,true);
                    }

                    @Override
                    public void onFailure(Call<LoginResponse> call, Throwable t) {
                        Toast.makeText(MainActivity.getInstance(), t.getMessage(), Toast.LENGTH_LONG).show();
                        MainActivity.getInstance().hideProgressBar();
                        setEnabledClicks(layoutButtons,true);
                        Log.d("failure ", "Respond Failed");
                    }
                });

            }
        });
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        MainActivity.getInstance().loginFragment = null;
    }
}
