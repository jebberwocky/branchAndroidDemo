package whereyogi.com.whereyogi_testflight;

import android.app.Application;
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
        Branch.disableTestMode();
        // Branch object initialization
        Branch.getAutoInstance(this);

        BranchCustomTagProvider.setApplicationContext(this);
    }
}
