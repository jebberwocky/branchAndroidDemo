package whereyogi.com.whereyogi_testflight;

import android.app.Application;
import android.content.Intent;
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
import io.branch.referral.util.BranchCrossPlatformId;
import io.branch.referral.util.BranchEvent;
import io.branch.referral.util.BranchLastAttributedTouchData;
import io.branch.referral.util.ContentMetadata;
import io.branch.referral.util.CurrencyType;
import io.branch.referral.util.ProductCategory;
import io.branch.referral.util.BranchContentSchema;
import io.branch.referral.util.LinkProperties;



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
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mTextMessage.setText(R.string.title_home);
                    return true;
                case R.id.navigation_dashboard:
                    mTextMessage.setText(R.string.title_dashboard);
                    return true;
                case R.id.navigation_notifications:
                    mTextMessage.setText(R.string.title_notifications);
                    return true;
            }
            return false;
        }
    };

    @Override
    public void onStart(){
        super.onStart();
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
        // Branch init
        Branch.getInstance().initSession(new Branch.BranchReferralInitListener() {

            @Override
            public void onInitFinished(JSONObject referringParams, BranchError error) {
                if (error == null) {
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

        Branch.getInstance().getLastAttributedTouchData(new BranchLastAttributedTouchData.
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

        Branch.getInstance().getCrossPlatformIds(new BranchCrossPlatformId.BranchCrossPlatformIdListener() {
            @Override
            public void onDataFetched(BranchCrossPlatformId.BranchCPID branchCPID, BranchError error) {
                if (error == null) {
                    Log.i("CPID: ", branchCPID.toString());
                } else {
                    Log.e("CPID error: ", error.getMessage());
                }
            }
        });

        // latest
        JSONObject sessionParams = Branch.getInstance().getLatestReferringParams();
        Log.i("BRANCH SDK latest", sessionParams.toString());

        // first
        JSONObject installParams = Branch.getInstance().getFirstReferringParams();
        Log.i("BRANCH SDK install", installParams.toString());


        //Log.i("BRANCH INTENT", this.getIntent().getData().toString());

        /*Log.i("FIREBASEDLINK", "FIRE read link");
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
                });*/

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
        //Log.i("BRANCH INTENT", this.getIntent().getData().toString());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTextMessage = (TextView) findViewById(R.id.message);
        linkText = (TextView)findViewById(R.id.linkText);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
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
                .logEvent(MainActivity.this);

    }

}
