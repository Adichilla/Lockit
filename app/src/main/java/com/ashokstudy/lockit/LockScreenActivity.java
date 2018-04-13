package com.ashokstudy.lockit;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.preference.PreferenceActivity;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;


public class LockScreenActivity extends Activity  {

    static final int RESULT_ENABLE = 1;
    DevicePolicyManager deviceManger;
    ActivityManager activityManager;
    ComponentName compName;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        deviceManger = (DevicePolicyManager)getSystemService(
                Context.DEVICE_POLICY_SERVICE);
        activityManager = (ActivityManager)getSystemService(
                Context.ACTIVITY_SERVICE);
        compName = new ComponentName(this, MyAdmin.class);

        boolean active = deviceManger.isAdminActive(compName);
        if (active) {
            deviceManger.lockNow();
            this.finish();
        }else {
            Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
            intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, compName);
            intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,
                    "You need to activate Device Administrator to perform phonelost tasks!");
            startActivityForResult(intent, RESULT_ENABLE);
        }

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case RESULT_ENABLE:
                try {
                    deviceManger.lockNow();
                    this.finish();
                }catch (Exception ex) {
                    Log.i("exception" , ex.getMessage());
                }
                return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}