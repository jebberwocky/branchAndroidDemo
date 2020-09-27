package whereyogi.com.whereyogi_testflight;

import android.content.Context;

import com.google.android.gms.tagmanager.CustomTagProvider;

import java.util.Map;

import io.branch.referral.Branch;
import io.branch.referral.util.BranchEvent;

public class BranchCustomTagProvider implements CustomTagProvider {

    private static Context sApplicationContext;

    public static void setApplicationContext(Context applicationContext) {
        if (applicationContext != null) {
            sApplicationContext = applicationContext.getApplicationContext();
        }
    }


    @Override
    public void execute(Map<String, Object> variables) {
        BranchEvent event = new BranchEvent("branch_custom_tag");
        for (String key: variables.keySet()) {
            event.addCustomDataProperty(key, variables.get(key).toString());
        }
        event.logEvent(sApplicationContext);
    }
}
