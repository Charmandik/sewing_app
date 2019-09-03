package fishrungames.tes.fragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import fishrungames.tes.MainActivity;
import fishrungames.tes.R;
import fishrungames.tes.models.request.ForgotPassRequest;
import fishrungames.tes.models.response.ForgotPassResponse;
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
import android.widget.Toast;


public class ForgotPassFragment extends Fragment {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        MainActivity.getInstance().forgotPassFragment = this;
        return inflater.inflate(R.layout.forgot_pass_fragment, null, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        ((EditText)getActivity().findViewById(R.id.forgot_pass_email)).setText("");
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final Button forgotPassBtn = getView().findViewById(R.id.forgot_pass_btn);
        forgotPassBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.getInstance().showProgressBar();
                forgotPassBtn.setEnabled(false);
                ForgotPassRequest forgotPassRequest = new ForgotPassRequest();
                EditText email = getActivity().findViewById(R.id.forgot_pass_email);
                forgotPassRequest.email = email.getText().toString();
                RetrofitController controller = new RetrofitController();
                MessagesApi messagesApi = controller.getApi();
                Call<ForgotPassResponse> responses = messagesApi.forgotPass(forgotPassRequest);

                responses.enqueue(new Callback<ForgotPassResponse>() {
                    @Override
                    public void onResponse(Call<ForgotPassResponse> call, Response<ForgotPassResponse> response) {
                        if (response.isSuccessful()) {
                            if(!response.body().getResult())
                                Toast.makeText(getActivity().getApplicationContext(),response.body().getMessage(),Toast.LENGTH_LONG)
                                        .show();
                            else {
                                MainActivity.getInstance().setLoginFragment();
                            }
                        } else {
                            Log.d("response code " , "Error");
                        }
                        MainActivity.getInstance().hideProgressBar();
                        forgotPassBtn.setEnabled(true);
                    }

                    @Override
                    public void onFailure(Call<ForgotPassResponse> call, Throwable t) {
                        Toast.makeText(MainActivity.getInstance(), t.getMessage(), Toast.LENGTH_LONG).show();
                        MainActivity.getInstance().hideProgressBar();
                        forgotPassBtn.setEnabled(true);
                        Log.d("failure " ,"Respond Failed");
                    }
                });

            }
        });

    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        MainActivity.getInstance().forgotPassFragment = null;
    }
}
