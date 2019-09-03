package fishrungames.tes.fragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import fishrungames.tes.MainActivity;
import fishrungames.tes.R;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class StartingFragment extends Fragment {

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        MainActivity.getInstance().startingFragment = this;
        return inflater.inflate(R.layout.starting_fragment, null, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final Button startingLoginBtn = getView().findViewById(R.id.startingFragmentLoginBtn);
        final Button startingSignupBtn = getView().findViewById(R.id.startingFragmentSignupBtn);
        startingLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startingLoginBtn.setEnabled(false);
                startingSignupBtn.setEnabled(false);
                MainActivity.getInstance().showProgressBar();
                MainActivity.getInstance().setLoginFragment();
                MainActivity.getInstance().hideProgressBar();
                startingLoginBtn.setEnabled(true);
                startingSignupBtn.setEnabled(true);
            }
        });
        startingSignupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startingLoginBtn.setEnabled(false);
                startingSignupBtn.setEnabled(false);
                MainActivity.getInstance().showProgressBar();
                MainActivity.getInstance().setRegistrationFragment();
                MainActivity.getInstance().hideProgressBar();
                startingLoginBtn.setEnabled(true);
                startingSignupBtn.setEnabled(true);
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
        MainActivity.getInstance().startingFragment = null;
    }
}
