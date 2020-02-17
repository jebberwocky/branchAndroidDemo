package whereyogi.com.whereyogi_testflight;
import java.io.IOException;
import java.lang.reflect.Method;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;
import android.util.Log;
import android.view.View;
import android.net.Uri;

import org.json.JSONException;
import org.json.JSONObject;

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

import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnCompleteListener;

import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;
import com.google.firebase.dynamiclinks.ShortDynamicLink;


import android.provider.Settings.Secure;
import com.google.firebase.iid.FirebaseInstanceIdService;


/*
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import com.google.android.gms.analytics.HitBuilders;
*/


import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    //private static GoogleAnalytics sAnalytics;
    //private static Tracker sTracker;

    private TextView mTextMessage;
    private TextView linkText;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            return false;
        }
    };

    @Override
    public void onStart(){

        String android_id = Secure.getString(getApplicationContext().getContentResolver(),
                Secure.ANDROID_ID);
        Log.i("ANDROID_ID_TEST", android_id);

        super.onStart();
        //verify AAID
        Log.i("GPS_TEST",GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(getApplicationContext())+"");

        // GA
        //sAnalytics = GoogleAnalytics.getInstance(this);
        //sTracker = sAnalytics.newTracker("UA-61112099-1");

        //String client_id = sTracker.get("&cid");
        //Log.i("GA client_ud", client_id);
        //Branch.getInstance().setRequestMetadata("$google_analytics_client_id",client_id);

        Branch.getInstance().setIdentity("UUID123456");
        //Branch.getInstance().setPreinstallCampaign("agency_654143947735585363_My_PreInstall_test3_Ad_Campaign");
        //Branch.getInstance().setPreinstallPartner("jeff_fake_OEM");
        Branch.getInstance().setRequestMetadata("app_store","JEFF_STORE");
        Branch.getInstance().setRequestMetadata("$braze_install_id","b87551c4-857a-4186-9117-9a34f93cc19a");
        // Branch init
        /*
        Branch.getInstance().initSession(new Branch.BranchReferralInitListener() {

            @Override
            public void onInitFinished(JSONObject referringParams, BranchError error) {
                if (error == null) {

                    if (referringParams != null) {
                        try {
                            // check if the session is from a Branch link
                            if (referringParams.getBoolean("+clicked_branch_link")) {

                                // create FirebaseAnalyhttps://docs.branch.io/apps/android/tics instance
                                FirebaseAnalytics firebaseAnalytics = FirebaseAnalytics
                                        .getInstance(getApplicationContext());
                                Bundle bundle = new Bundle();

                                bundle.putBoolean("clicked_branch_link",
                                        referringParams.getBoolean("+clicked_branch_link"));
                                // get the click timestamp
                                bundle.putString("click_timestamp",
                                        referringParams.getString("+click_timestamp"));
                                // get the link OG title
                                bundle.putString("link_title",
                                        referringParams.getString("$og_title"));
                                // get the link OG image
                                bundle.putString("link_image",
                                        referringParams.getString("$og_image_url"));
                                // get the link campaign
                                bundle.putString("utm_campaign",
                                        referringParams.getString("~campaign"));
                                // get the link channel
                                bundle.putString("utm_medium",
                                        referringParams.getString("~channel"));
                                // get the link feature
                                bundle.putString("utm_source",
                                        referringParams.getString("~feature"));

                                // you can use the local shared preference to detect if this is an install session or open session as below
                                SharedPreferences sharedPreferences = getApplication()
                                        .getSharedPreferences("local_sharefpref",
                                                Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                boolean isFirstSession = sharedPreferences
                                        .getBoolean("is_first_session", true);
                                if (isFirstSession) {
                                    editor.putBoolean("is_first_session", false);
                                    editor.apply();
                                }

                                // check if the session is install or open
                                String eventName =
                                        isFirstSession ? "branch_install" : "branch_open";

                                // log the event to the firebase
                                firebaseAnalytics.logEvent(eventName, bundle);
                            }
                        } catch (JSONException ignore) {
                        }
                    }


                    Log.i("BRANCH SDK INIT", referringParams.toString());
                    try {
                        //Log.i("BRANCH params", referringParams.get("params").toString());
                        Log.i("BRANCH $canonical_url", referringParams.get("$canonical_url").toString());

                    }catch (JSONException exception){
                        Log.e("Casting error", exception.toString());
                    }
                    int credits = Branch.getInstance(getApplicationContext()).getCredits();
                    Log.i("Branch Credit", Integer.toString(credits));
                } else {
                    Log.e("BRANCH SDK ERROR", error.getMessage());
                }
            }
        }, this.getIntent().getData(), this);

        Branch.getInstance().getLastAttributedTouchData(new
                BranchLastAttributedTouchDataListener() {
            @Override
            public void onDataFetched(JSONObject jsonObject, BranchError error) {
                if (error == null) {
                    Log.i("LATD: ", jsonObject.toString());
                } else {
                    Log.e("LATD error: ", error.getMessage());
                }

            }
        },30);

        Branch.getInstance().getCrossPlatformIds(new ServerRequestGetCPID.BranchCrossPlatformIdListener() {
            public void onDataFetched(BranchCPID branchCPID, BranchError error) {
                if (error == null) {
                    Log.i("CPID: ", branchCPID.toString());
                } else {
                    Log.e("CPID error: ", error.getMessage());
                }
            }
        });
        */

        // latest
        JSONObject sessionParams = Branch.getInstance().getLatestReferringParams();
        Log.i("BRANCH SDK latest", sessionParams.toString());

        // first
        JSONObject installParams = Branch.getInstance().getFirstReferringParams();
        Log.i("BRANCH SDK install", installParams.toString());

        if(this.getIntent() != null&&this.getIntent().getData()!=null)
            Log.i("BRANCH INTENT", this.getIntent().getData().toString());

        Log.i("FIREBASEDLINK", "FIRE read link");
        FirebaseDynamicLinks.getInstance()
                .getDynamicLink(getIntent())
                .addOnSuccessListener(this, new OnSuccessListener<PendingDynamicLinkData>() {
                    @Override
                    public void onSuccess(PendingDynamicLinkData pendingDynamicLinkData) {
                        // Get deep link from result (may be null if no link is found)
                        Uri deepLink = null;
                        if (pendingDynamicLinkData != null) {
                            deepLink = pendingDynamicLinkData.getLink();
                        }

                        if(pendingDynamicLinkData==null)
                            Log.i("FIREBASEDLINK", "pendingDynamicLinkData is null");

                        if(deepLink!=null)
                            Log.i("FIREBASEDLINK", deepLink.toString());
                        else
                            Log.i("FIREBASEDLINK", "deeplink is null");
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("FIREBASEDLINK", "getDynamicLink:onFailure");
                    }
                });

        /*
        FirebaseInstanceId.getInstance().getInstanceId().
                addOnSuccessListener( MainActivity.this, new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                String deviceToken = instanceIdResult.getToken();
                // Do whatever you want with your token now
                // i.e. store it on SharedPreferences or DB
                // or directly send it to server
                Log.i("FIREBASE SDK token", deviceToken);
            }
        });*/
    }

    @Override
    public void onNewIntent(Intent intent) {
        this.setIntent(intent);
        if(this.getIntent() != null&&this.getIntent().getData()!=null)
            Log.i("BRANCH INTENT", this.getIntent().getData().toString());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    //
    public void createLink(View view){

        Branch.getInstance().setIdentity("JEFFUUID");


        BranchUniversalObject buo = new BranchUniversalObject()
                .setCanonicalIdentifier("content/12345")
                .setTitle("Created from Android SDK")
                .setContentDescription("Created from Android SDK")
                .setContentIndexingMode(BranchUniversalObject.CONTENT_INDEX_MODE.PUBLIC)
                .setLocalIndexMode(BranchUniversalObject.CONTENT_INDEX_MODE.PUBLIC)
                .setContentMetadata(new ContentMetadata().addCustomMetadata("key1", "value1"));

        LinkProperties lp = new LinkProperties()
                .setChannel("facebook")
                .setFeature("sharing")
                .setCampaign("content 123 launch")
                .setStage("new user")
                .addControlParameter("$desktop_url", "http://whereyogi.com")
                .addControlParameter("custom", "data")
                .addControlParameter("custom_random", Long.toString(Calendar.getInstance().getTimeInMillis()));

        buo.generateShortUrl(this, lp, new Branch.BranchLinkCreateListener() {
            @Override
            public void onLinkCreate(String url, BranchError error) {
                if (error == null) {
                    Log.i("BRANCH SDK", "got my Branch link to share: " + url);
                    linkText.setText(url);
                }
            }
        });
    }


    public void trackContent(View view){

    }

    public void openWebView(View view){
        Intent intent = new Intent(getApplicationContext(), Main2Activity.class);
        intent.putExtra("url", "http://whereyogi.com/branch/testbranch.html");
        startActivity(intent);
    }


    //
    /** Called when the user touches the button */
    public void trackCommerce(View view) {

        // Do something in response to button click
        BranchUniversalObject buo = new BranchUniversalObject()
                .setCanonicalIdentifier("myprod/1234")
                .setCanonicalUrl("https://test_canonical_url")
                .setTitle("test_title")
                .setContentMetadata(
                        new ContentMetadata()
                                .addCustomMetadata("custom_metadata_key1", "custom_metadata_val1")
                                .addCustomMetadata("custom_metadata_key1", "custom_metadata_val1")
                                .addImageCaptions("image_caption_1", "image_caption2", "image_caption3")
                                .setAddress("Street_Name", "test city", "test_state", "test_country", "test_postal_code")
                                .setRating(5.2, 6.0, 5)
                                .setLocation(-151.67, -124.0)
                                .setPrice(10.0, CurrencyType.USD)
                                .setProductBrand("test_prod_brand")
                                .setProductCategory(ProductCategory.APPAREL_AND_ACCESSORIES)
                                .setProductName("test_prod_name")
                                .setProductCondition(ContentMetadata.CONDITION.EXCELLENT)
                                .setProductVariant("test_prod_variant")
                                .setQuantity(1.5)
                                .setSku("test_sku")
                                .setContentSchema(BranchContentSchema.COMMERCE_PRODUCT))
                .addKeyWord("keyword1")
                .addKeyWord("keyword2");

        new BranchEvent(BRANCH_STANDARD_EVENT.PURCHASE)
                .addContentItems(buo)
                .setCurrency(CurrencyType.USD)
                .setTransactionID("order_id_1231231")
                .setRevenue(10)
                .addCustomDataProperty("payment","Cod")
                .logEvent(this.getApplicationContext());
/*
        new BranchEvent(BRANCH_STANDARD_EVENT.PURCHASE)
                .setCurrency(CurrencyType.USD)
                .setDescription("用户购买xxx")
                .setRevenue(9.99) //此次购买收入
                .addCustomDataProperty("purchase_channel", "Google Play US")
                .logEvent(MainActivity.this);
    */
        new BranchEvent(BRANCH_STANDARD_EVENT.VIEW_ITEM)
                .addContentItems(buo)
                .setCurrency(CurrencyType.USD)
                .setRevenue(10)
                .addCustomDataProperty("type", "user")
                .logEvent(MainActivity.this);

        new BranchEvent(BRANCH_STANDARD_EVENT.VIEW_ITEMS)
                .addContentItems(buo)
                .addCustomDataProperty("type", "user")
                .logEvent(MainActivity.this);


        new BranchEvent("LOGIN")
                .addCustomDataProperty("type", "user")
                .logEvent(MainActivity.this);

        new BranchEvent("YESSTYLE_FIRST_PURCHASE")
                .addCustomDataProperty("user", "abbcdacda")
                .addContentItems(buo)
                .logEvent(MainActivity.this);


        new BranchEvent("Jeff_LIU")
                .addCustomDataProperty("user", "abbcdacda")
                .addContentItems(buo)
                .logEvent(MainActivity.this);

    }
}
