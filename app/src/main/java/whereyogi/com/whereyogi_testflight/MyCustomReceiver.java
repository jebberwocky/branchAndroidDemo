package whereyogi.com.whereyogi_testflight;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
//import io.branch.referral.InstallListener;

//Branch no longer uses listener from v4.3.1
public class MyCustomReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        //InstallListener tracker = new InstallListener();
        //tracker.onReceive(context, intent);
    }
}
