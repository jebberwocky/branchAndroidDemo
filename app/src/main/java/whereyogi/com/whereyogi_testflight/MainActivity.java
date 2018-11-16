package whereyogi.com.whereyogi_testflight;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;
import android.util.Log;
import android.view.View;

import org.json.JSONException;
import org.json.JSONObject;

import io.branch.referral.Branch;
import io.branch.referral.BranchError;
import io.branch.indexing.BranchUniversalObject;
import io.branch.referral.util.BRANCH_STANDARD_EVENT;
import io.branch.referral.util.BranchEvent;
import io.branch.referral.util.ContentMetadata;
import io.branch.referral.util.CurrencyType;
import io.branch.referral.util.ProductCategory;
import io.branch.referral.util.BranchContentSchema;
import io.branch.referral.util.LinkProperties;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

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

        // Branch init
        Branch.getInstance().initSession(new Branch.BranchReferralInitListener() {
            @Override
            public void onInitFinished(JSONObject referringParams, BranchError error) {
                if (error == null) {
                    Log.i("BRANCH SDK INIT", referringParams.toString());
                    try {
                        Log.i("BRANCH SDK", referringParams.get("$canonical_url").toString());
                    }catch (JSONException exception){
                        Log.e("Casting error", exception.toString());
                    }
                } else {
                    Log.e("BRANCH SDK ERROR", error.getMessage());
                }
            }
        }, this.getIntent().getData(), this);

        // latest
        JSONObject sessionParams = Branch.getInstance().getLatestReferringParams();
        Log.i("BRANCH SDK latest", sessionParams.toString());

        // first
        JSONObject installParams = Branch.getInstance().getFirstReferringParams();
        Log.i("BRANCH SDK install", installParams.toString());
    }

    @Override
    public void onNewIntent(Intent intent) {
        this.setIntent(intent);
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
                .addControlParameter("$desktop_url", "http://example.com/home")
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

        new BranchEvent(BRANCH_STANDARD_EVENT.ADD_TO_CART)
                .setAffiliation("test_affiliation")
                .setCoupon("Coupon Code")
                .setCurrency(CurrencyType.USD)
                .setDescription("Customer added item to cart")
                .setShipping(0.0)
                .setTax(9.75)
                .setRevenue(1.5)
                .setSearchQuery("Test Search query")
                .addCustomDataProperty("Custom_Event_Property_Key1", "Custom_Event_Property_val1")
                .addCustomDataProperty("Custom_Event_Property_Key2", "Custom_Event_Property_val2")
                .addContentItems(buo)
                .logEvent( MainActivity.this);
    }

}
