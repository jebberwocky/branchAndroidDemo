package whereyogi.com.whereyogi_testflight;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Handler;

import io.branch.referral.Branch;
import io.branch.referral.BranchError;
import io.branch.indexing.BranchUniversalObject;
import io.branch.referral.util.BRANCH_STANDARD_EVENT;
import io.branch.referral.ServerRequestGetCPID.BranchCrossPlatformIdListener;
import io.branch.referral.ServerRequestGetCPID;
import io.branch.referral.util.BranchCPID;
import io.branch.referral.util.BranchEvent;
import io.branch.referral.ServerRequestGetLATD.BranchLastAttributedTouchDataListener;
import io.branch.referral.util.ContentMetadata;
import io.branch.referral.util.CurrencyType;
import io.branch.referral.util.ProductCategory;
import io.branch.referral.util.BranchContentSchema;
import io.branch.referral.util.LinkProperties;
import io.branch.referral.validators.IntegrationValidator;

import org.json.JSONException;
import org.json.JSONObject;
import android.util.Log;

public class SplashActivity extends AppCompatActivity {

    /** Duration of wait **/
    private final int SPLASH_DISPLAY_LENGTH = 3000;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.content_splash);
    }

    @Override protected void onStart() {
        super.onStart();

        Branch.getInstance().setRequestMetadata("jeff_data","JEFF_LIU");
        //Branch.getInstance().setPreinstallPartner("a_duanxin");
        IntegrationValidator.validate(this);
        //Branch.getInstance().setPreinstallPartner("a_duanxin");

        Branch.getInstance().sessionBuilder(this).withCallback(new Branch.BranchReferralInitListener() {

            @Override
            public void onInitFinished(JSONObject referringParams, BranchError error) {
                if(referringParams !=null) {
                    Log.i("BRANCH Params", referringParams.toString());
                    CustomApplicationClass applicationClass = (CustomApplicationClass)getApplicationContext();
                    applicationClass.setData(referringParams.toString());
                }else{
                    Log.i("BRANCH Params", "NULL");
                }
                if(error!=null)
                    Log.e("BRANCH ERROR", error.getMessage());
               //New Handler to start the Menu-Activity
               //and close this Splash-Screen after some seconds.

                new Handler().postDelayed(new Runnable(){
                    @Override
                    public void run() {
                        Intent mainIntent = new Intent(SplashActivity.this,MainActivity.class);
                        SplashActivity.this.startActivity(mainIntent);
                        SplashActivity.this.finish();
                    }
                }, SPLASH_DISPLAY_LENGTH);
            }}
        ).init();

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                Intent mainIntent = new Intent(SplashActivity.this,MainActivity.class);
                SplashActivity.this.startActivity(mainIntent);
                SplashActivity.this.finish();
            }
        }, SPLASH_DISPLAY_LENGTH);
    }
/*
    @Override
    public void onNewIntent(Intent intent) {
        this.setIntent(intent);
        if(this.getIntent() != null&&this.getIntent().getData()!=null)
            Log.i("BRANCH INTENT", this.getIntent().getData().toString());
    }*/

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        // if activity is in foreground (or in backstack but partially visible) launching the same
        // activity will skip onStart, handle this case with reInitSession
        if (intent != null &&
                intent.hasExtra("branch_force_new_session") &&
                intent.getBooleanExtra("branch_force_new_session",false)) {
            //if need to reinit, please use following code
            //Branch.sessionBuilder(this).withCallback(branchReferralInitListener).reInit();
        }
    }




}
