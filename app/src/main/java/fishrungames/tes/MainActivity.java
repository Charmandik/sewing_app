package fishrungames.tes;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import androidx.annotation.CallSuper;
import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import fishrungames.tes.fragments.ProfileFragment;

import com.braintreepayments.api.interfaces.BraintreeCancelListener;
import com.braintreepayments.api.interfaces.BraintreeErrorListener;
import com.braintreepayments.api.interfaces.BraintreePaymentResultListener;
import com.braintreepayments.api.interfaces.PaymentMethodNonceCreatedListener;
import com.braintreepayments.api.models.BraintreePaymentResult;
import com.braintreepayments.api.models.Configuration;
import com.braintreepayments.api.models.PaymentMethodNonce;

import fishrungames.salmonengineandroid.EngineWrapper;
import fishrungames.tes.fragments.ForgotPassFragment;
import fishrungames.tes.fragments.LicenseFragment;
import fishrungames.tes.fragments.LoginFragment;
import fishrungames.tes.fragments.RegistrationFragment;

import fishrungames.tes.fragments.StartingFragment;
import fishrungames.tes.models.request.UnsubscribeRequest;
import fishrungames.tes.models.response.BaseResponse;
import fishrungames.tes.utils.PrefUtil;
import fishrungames.tes.utils.retrofit.MessagesApi;
import fishrungames.tes.utils.retrofit.RetrofitController;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.pm.ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;


public class MainActivity extends AppCompatActivity implements
        PaymentMethodNonceCreatedListener, BraintreeCancelListener, BraintreeErrorListener,
        BraintreePaymentResultListener {

    GLView mView;
    static MainActivity instance;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    public LoginFragment loginFragment = null;
    public RegistrationFragment registrationFragment = null;
    public ForgotPassFragment forgotPassFragment = null;
    public LicenseFragment licenseFragment = null;
    public ProfileFragment profileFragment = null;
    public StartingFragment startingFragment = null;
    public Boolean isUserSubscribed = true;
    private Menu menu;

    ProgressBar progressBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        instance = this;
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        EngineWrapper.LoadSalmonEngineLibrary();
        EngineWrapper.SetActivityInstance(this);
        EngineWrapper.SetupEnviroment();

        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        String apkFilePath = null;
        ApplicationInfo appInfo = null;
        PackageManager packMgmr = this.getPackageManager();
        try {
            appInfo = packMgmr.getApplicationInfo("fishrungames.tes", 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException("Unable to locate assets, aborting...");
        }
        apkFilePath = appInfo.sourceDir;

        EngineWrapper.SetupApkFilePath(apkFilePath);

        mView = (GLView) findViewById(R.id.gl_surface_view);

        EngineWrapper.SetView(mView);

        progressBar = findViewById(R.id.progressBar);

        Toolbar toolbar = findViewById(R.id.toolbar_actionbar);
        setSupportActionBar(toolbar);

        if (savedInstanceState == null) {
            if (PrefUtil.getToken(getApplicationContext()).trim().equals(""))
                setStartingFragment();
            else
                showGlView();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        findViewById(R.id.toolbar).setVisibility(View.VISIBLE);
        getMenuInflater().inflate(R.menu.menu_activity_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_logout:
                AlertDialog dialog = new AlertDialog.Builder(MainActivity.getInstance())
                        .setMessage("Are you sure that you want to logout?")

                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                PrefUtil.setToken(MainActivity.getInstance(), "");
                                hideGLView();
                                MainActivity.getInstance().setStartingFragment();
                                dialog.dismiss();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .create();
                dialog.show();
                break;
            case R.id.item_return:
                hideGLView();
                this.showGlView();
                break;
            case R.id.item_profile:
                hideGLView();
                this.setProfileFragment();
                break;
            case R.id.item_history:
                hideGLView();
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.threadses.com/membership/web/order/index"));
                startActivity(browserIntent);
                break;
            case R.id.item_subscribe:  //todo:hide this in some function
                hideGLView();
                if (this.isUserSubscribed) {
                    AlertDialog unsubscribe_dialog = new AlertDialog.Builder(MainActivity.getInstance())
                            .setMessage("Unsubscribe?")
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    RetrofitController controller = new RetrofitController();
                                    final MessagesApi messagesApi = controller.getApi();
                                    MainActivity.getInstance().showProgressBar();
                                    UnsubscribeRequest unsubscribeRequest = new UnsubscribeRequest();
                                    unsubscribeRequest.setToken(PrefUtil.getToken(MainActivity.getInstance()));
                                    final Call<BaseResponse> responses = messagesApi.unsubscribe(unsubscribeRequest);

                                    responses.enqueue(new Callback<BaseResponse>() {
                                        @Override
                                        public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                                            if (response.isSuccessful() && response.body() != null && response.body().getResult()) {
                                                MainActivity.getInstance().hideProgressBar();
                                                isUserSubscribed = !isUserSubscribed;
                                                updateMenuTitles();
                                            } else {
                                                Toast.makeText(MainActivity.getInstance(), response.body().getMessage(), Toast.LENGTH_LONG).show();
                                                MainActivity.getInstance().hideProgressBar();
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<BaseResponse> call, Throwable t) {
                                            MainActivity.getInstance().hideProgressBar();
                                            Log.e("Failure", "Respond Failed" + t.getMessage());
                                        }
                                    });
                                }
                            })
                            .create();
                    unsubscribe_dialog.show();
                } else
                    this.setLicenseFragment();

                break;

        }
        return super.onOptionsItemSelected(item);
    }

    public void updateMenuTitles() {
        MenuItem item_subscribe = menu.findItem(R.id.item_subscribe);
        if (this.isUserSubscribed)
            item_subscribe.setVisible(false);
        else {
            item_subscribe.setVisible(true);
            item_subscribe.setTitle("Subscribe");
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        updateMenuTitles();
        return super.onPrepareOptionsMenu(menu);
    }

    public void setStartingFragment() {
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.container, new StartingFragment());
//        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public void setProfileFragment() {
        fragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, new ProfileFragment());
        fragmentTransaction.commit();
    }

    public void setRegistrationFragment() {
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.container, new RegistrationFragment());
        fragmentTransaction.addToBackStack("registry");
        fragmentTransaction.commit();
    }

    public void setForgotPassFragment() {
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, new ForgotPassFragment());
        fragmentTransaction.addToBackStack("forgotPass");
        fragmentTransaction.commit();
    }

    public void setLoginFragment() {
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.container, new LoginFragment());
        fragmentTransaction.addToBackStack("login");
        fragmentTransaction.commit();
    }

    public void setLicenseFragment() {
        showProgressBar();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, new LicenseFragment());
        fragmentTransaction.addToBackStack("license");
        fragmentTransaction.commit();
    }

    public void showGlView() {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        if (findViewById(R.id.container) != null)
            if (findViewById(R.id.container).getVisibility() == View.VISIBLE) {
                View view = this.findViewById(android.R.id.content);
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                findViewById(R.id.container).setVisibility(View.GONE);
                findViewById(R.id.gl_surface_view).setVisibility(View.VISIBLE);
            } else {
                setProfileFragment();
            }
    }

    public void hideGLView() {
        if (findViewById(R.id.container).getVisibility() == View.GONE) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
            findViewById(R.id.container).setVisibility(View.VISIBLE);
            findViewById(R.id.gl_surface_view).setVisibility(View.GONE);
        }
    }

    //Function to Call Main Activities functions from any fragment
    static public MainActivity getInstance() {
        return instance;
    }

    @Override
    protected void onPause() {
        super.onPause();
        EngineWrapper.CallDestroy();
        mView.onPause();
    }

    protected void onDestroy() {
        super.onDestroy();
        instance = null;
    }

    @Override
    protected void onResume() {
        super.onResume();
        mView.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    public boolean onTouchEvent(MotionEvent event) {
        EngineWrapper.ProcessTouchEvent(event);
        return true;
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        EngineWrapper.ProcessKeyDown(keyCode, event);
        return super.onKeyDown(keyCode, event);
    }

    // UI helpers
    public void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
        FrameLayout frameLayout = findViewById(R.id.container);
        frameLayout.setClickable(true);
    }

    public void hideProgressBar() {
        progressBar.setVisibility(View.GONE);
        FrameLayout frameLayout = findViewById(R.id.container);
        frameLayout.setClickable(false);
    }


    // Braintee GooglePay Integration
    @CallSuper
    @Override
    public void onPaymentMethodNonceCreated(PaymentMethodNonce paymentMethodNonce) {
        licenseFragment.onPaymentMethodNonceCreate(paymentMethodNonce.getNonce());
    }

    @CallSuper
    @Override
    public void onCancel(int requestCode) {
        Log.d(getClass().getSimpleName(), "Cancel received: " + requestCode);
    }

    @CallSuper
    @Override
    public void onError(Exception error) {
        Log.d(getClass().getSimpleName(), "Error received (" + error.getClass() + "): " + error.getMessage());
        Log.d(getClass().getSimpleName(), error.toString());
    }

    @CallSuper
    @Override
    public void onBraintreePaymentResult(BraintreePaymentResult result) {
        Log.d(getClass().getSimpleName(), "Braintree Payment Result received: " + result.getClass().getSimpleName());
    }
}