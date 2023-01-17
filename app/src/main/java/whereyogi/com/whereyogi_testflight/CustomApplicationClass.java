package whereyogi.com.whereyogi_testflight;

import android.app.Application;

import androidx.annotation.NonNull;
import io.branch.referral.Branch;

public class CustomApplicationClass extends Application {
    private String data;
    public String getData() {return data;}
    public void setData(String data) {this.data = data;}

    @Override
    public void onCreate() {
        super.onCreate();

        // Branch logging for debugging
        Branch.enableLogging();
        //Branch.enableDebugMode();
        //Branch.enableTestMode();a_oppopai
        //Branch.disableTestMode();
        // Branch object initialization

        Branch.getAutoInstance(this);
        Branch.getInstance().setNetworkTimeout(1000);
        //Branch.getInstance().setBranchRemoteInterface(new OkhttpBranchNetworkInterface());
        //BranchCustomTagProvider.setApplicationContext(this);
        Branch.getInstance().setPreinstallCampaign("agency_654143947735585363_My_FAKE_OPPO_PAI_PreInstall_test");
        Branch.getInstance().setPreinstallPartner("a_oppopai");
    }
}
