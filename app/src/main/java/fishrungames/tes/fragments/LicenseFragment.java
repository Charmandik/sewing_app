package fishrungames.tes.fragments;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import fishrungames.tes.MainActivity;
import fishrungames.tes.R;
import fishrungames.tes.adapters.LicenseAdapter;
import fishrungames.tes.models.License;
import fishrungames.tes.models.request.SubscribeRequest;
import fishrungames.tes.models.response.BaseResponse;
import fishrungames.tes.models.response.ClientTokenResponse;
import fishrungames.tes.models.response.LicensesResponse;
import fishrungames.tes.utils.DialogUtil;
import fishrungames.tes.utils.PrefUtil;
import fishrungames.tes.utils.retrofit.MessagesApi;
import fishrungames.tes.utils.retrofit.RetrofitController;

// Braintree GooglePay dependencies
import com.braintreepayments.api.BraintreeFragment;
import com.braintreepayments.api.GooglePayment;
import com.braintreepayments.api.exceptions.InvalidArgumentException;
import com.braintreepayments.api.models.GooglePaymentRequest;
import com.google.android.gms.wallet.TransactionInfo;
import com.google.android.gms.wallet.WalletConstants;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.internal.EverythingIsNonNull;

public class LicenseFragment extends Fragment {

    private BraintreeFragment mBraintreeFragment;
    private String clientToken;
    private MessagesApi messagesApi;
    private RecyclerView licenseRecyclerView;
    private LicenseAdapter licenseAdapter;

    private License[] licenses;
    private License paymentLicense;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        MainActivity.getInstance().showProgressBar();
        licenseRecyclerView = view.findViewById(R.id.licenseRecyclerView);

        RetrofitController retrofitController = new RetrofitController();
        messagesApi = retrofitController.getApi();

        String token = PrefUtil.getToken(getContext());


        Call<ClientTokenResponse> clientTokenResponse = messagesApi.getClientToken(token);
        Callback<ClientTokenResponse> clientTokenResponseCallback = new Callback<ClientTokenResponse>() {
            @Override
            public void onResponse(@EverythingIsNonNull Call<ClientTokenResponse> call, @EverythingIsNonNull Response<ClientTokenResponse> response) {
                if(response.isSuccessful() && response.body() != null && response.body().result) {
                    clientToken = response.body().clientToken;
                    try {
                        mBraintreeFragment = BraintreeFragment.newInstance(MainActivity.getInstance(), clientToken);
                        initLicenseRecyclerView();
                    } catch (InvalidArgumentException e) {
                        DialogUtil.showAlertDialog(getContext(), getString(R.string.response_error));
                    }
                } else {
                    DialogUtil.showAlertDialog(getContext(), getString(R.string.response_error));
                }
            }

            @Override
            public void onFailure(Call<ClientTokenResponse> call, Throwable t) {
                MainActivity.getInstance().hideProgressBar();
                DialogUtil.showAlertDialog(getContext(), getString(R.string.response_error));
            }
        };
        clientTokenResponse.enqueue(clientTokenResponseCallback);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        MainActivity.getInstance().licenseFragment = this;
        return inflater.inflate(R.layout.license_fragment, null, false);
    }

    @Override
    public void onPause() {
        super.onPause();
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            licenseRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            licenseRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 1));
        }
    }

    public void initLicenseRecyclerView() {
        licenseAdapter = new LicenseAdapter();
        int orientation = getResources().getConfiguration().orientation;
        if(orientation == Configuration.ORIENTATION_LANDSCAPE)
            licenseRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        else
            licenseRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 1));
        licenseRecyclerView.setAdapter(licenseAdapter);
        licenseRecyclerView.setVisibility(View.VISIBLE);


        Call<LicensesResponse> licensesResponse = messagesApi.getLicenses(PrefUtil.getToken(getActivity().getApplicationContext()));
        Callback<LicensesResponse> licensesResponseCallback = new Callback<LicensesResponse>() {
            @Override
            public void onResponse(@EverythingIsNonNull Call<LicensesResponse> call, @EverythingIsNonNull Response<LicensesResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().result) {
                    licenses = response.body().licenses;
                    licenseAdapter.setItems(licenses, new LicenseAdapter.OnItemClickListener() {
                        @Override public void onItemClick(License item) {
                            launchGooglePayment(item);
                        }
                    });
                } else {
                        showError();
                }
                MainActivity.getInstance().hideProgressBar();
            }

            @Override
            public void onFailure(Call<LicensesResponse> call, Throwable t) {
                showError();
            }
        };
        licensesResponse.enqueue(licensesResponseCallback);
    }

    // @totalPrice like "10.00"
    public void launchGooglePayment(License license) {
        paymentLicense = license;

        GooglePaymentRequest googlePaymentRequest = new GooglePaymentRequest()
                .transactionInfo(TransactionInfo.newBuilder()
                        .setCurrencyCode("USD")
                        .setTotalPrice(license.getAmount())
                        .setTotalPriceStatus(WalletConstants.TOTAL_PRICE_STATUS_FINAL)
                        .build())
                //.allowPrepaidCards(false)
                //.billingAddressFormat(WalletConstants.BILLING_ADDRESS_FORMAT_FULL)
                //.billingAddressRequired(false)
                .emailRequired(false)
                .phoneNumberRequired(false)
                .shippingAddressRequired(false)
                .googleMerchantId("TEST"); // for production need MerchantId from Google

        GooglePayment.requestPayment(mBraintreeFragment, googlePaymentRequest);
    }

    private void showError() {
        MainActivity.getInstance().hideProgressBar();
        DialogUtil.showAlertDialog(getContext(), getString(R.string.response_error));
        MainActivity.getInstance().setProfileFragment();
    }

    private void showErrorWithText(String text) {
        MainActivity.getInstance().hideProgressBar();
        DialogUtil.showAlertDialog(getContext(), text);
        MainActivity.getInstance().setProfileFragment();
    }

    public void onPaymentMethodNonceCreate(String nonce) {
        SubscribeRequest subscribeRequest = new SubscribeRequest();
        subscribeRequest.setNonce(nonce);
        subscribeRequest.setLicenseId(paymentLicense.getId());
        subscribeRequest.setToken(PrefUtil.getToken(getContext()));

        // Clear payment license
        paymentLicense = null;

        Call<BaseResponse> subscribeResponse = messagesApi.subscribe(subscribeRequest);
        subscribeResponse.enqueue(new Callback<BaseResponse>() {
            @Override
            @EverythingIsNonNull
            public void onResponse(@EverythingIsNonNull Call<BaseResponse> call, @EverythingIsNonNull Response<BaseResponse> response) {
                if(response.isSuccessful() && response.body() != null && response.body().getResult()) {
                    MainActivity.getInstance().setProfileFragment();
                } else {
                    if(response.body() == null || response.body().getMessage().equals("")) {
                        showError();
                    } else {
                        showErrorWithText(response.body().getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                showError();
            }
        });

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        MainActivity.getInstance().licenseFragment = null;
    }
}
