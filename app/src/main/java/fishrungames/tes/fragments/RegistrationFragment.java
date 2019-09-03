package fishrungames.tes.fragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import fishrungames.tes.MainActivity;
import fishrungames.tes.R;
import fishrungames.tes.models.request.RegistryRequest;
import fishrungames.tes.models.response.RegistryResponse;
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
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;


public class RegistrationFragment extends Fragment {

    private Boolean isRegistered;

    public void setRegistered(Boolean registered) {
        isRegistered = registered;
    }

    public Boolean getRegistered() {
        return isRegistered;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();

        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        MainActivity.getInstance().registrationFragment = this;
        return inflater.inflate(R.layout.registration_fragment, null, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        ((EditText) getActivity().findViewById(R.id.edit_username_registry)).setText("");
        ((EditText) getActivity().findViewById(R.id.edit_email_registry)).setText("");
        ((EditText) getActivity().findViewById(R.id.edit_pass_registry)).setText("");
        ((EditText) getActivity().findViewById(R.id.edit_confirmPass_registry)).setText("");
        ((EditText) getActivity().findViewById(R.id.edit_firstName_registry)).setText("");
        ((EditText) getActivity().findViewById(R.id.edit_lastname_registry)).setText("");
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final Button regBtn = getView().findViewById(R.id.button_registry);
        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                regBtn.setEnabled(false);
                RegistryRequest registryRequest = new RegistryRequest();
                TextView username = getActivity().findViewById(R.id.edit_username_registry);
                TextView email = getActivity().findViewById(R.id.edit_email_registry);
                final EditText password = (EditText) getActivity().findViewById(R.id.edit_pass_registry);
                final EditText confirmPassword = (EditText) getActivity().findViewById(R.id.edit_confirmPass_registry);
                EditText firstName = getActivity().findViewById(R.id.edit_firstName_registry);
                EditText lastName = getActivity().findViewById(R.id.edit_lastname_registry);
                if (!password.getText().toString().trim().equals(confirmPassword.getText().toString().trim())) {
                    Toast.makeText(getActivity().getApplicationContext(), "Passwords must match", Toast.LENGTH_LONG).show();
                } else if (username.getText().toString().equals(""))
                    Toast.makeText(MainActivity.getInstance(), "Error:Username can not be empty", Toast.LENGTH_LONG).show();
                else if (email.getText().toString().equals(""))
                    Toast.makeText(MainActivity.getInstance(), "Error:Email can not be empty", Toast.LENGTH_LONG).show();
                else if (firstName.getText().toString().equals(""))
                    Toast.makeText(MainActivity.getInstance(), "Error:First Name can not be empty", Toast.LENGTH_LONG).show();
                else if (lastName.getText().toString().equals(""))
                    Toast.makeText(MainActivity.getInstance(), "Error:Last Name can not be empty", Toast.LENGTH_LONG).show();
                else if (password.getText().toString().equals(""))
                    Toast.makeText(MainActivity.getInstance(), "Error:Password can not be empty", Toast.LENGTH_LONG).show();
                else {
                    MainActivity.getInstance().showProgressBar();
                    registryRequest.username = username.getText().toString();
                    registryRequest.email = email.getText().toString();
                    registryRequest.password = password.getText().toString();
                    registryRequest.firstName = firstName.getText().toString();
                    registryRequest.lastName = lastName.getText().toString();
                    RetrofitController controller = new RetrofitController();
                    MessagesApi messagesApi = controller.getApi();
                    Call<RegistryResponse> responses = messagesApi.registration(registryRequest);

                    responses.enqueue(new Callback<RegistryResponse>() {
                        @Override
                        public void onResponse(Call<RegistryResponse> call, Response<RegistryResponse> response) {
                            if (response.isSuccessful()) {
                                if (response.body().getResult()) {
                                    MainActivity.getInstance().setLoginFragment();
                                } else
                                    Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_LONG).show();
                            }
                            MainActivity.getInstance().hideProgressBar();
                            regBtn.setEnabled(true);
                        }

                        @Override
                        public void onFailure(Call<RegistryResponse> call, Throwable t) {
                            Toast.makeText(MainActivity.getInstance(), t.getMessage(), Toast.LENGTH_LONG).show();
                            MainActivity.getInstance().hideProgressBar();
                            regBtn.setEnabled(true);
                            Log.d("failure ", "Respond Failed" + t);
                        }
                    });
                }
            }
        });

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        MainActivity.getInstance().registrationFragment = null;
    }
}


