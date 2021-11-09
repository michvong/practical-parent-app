package ca.cmpt276.titanium.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class NotificationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        boolean dismissed = intent.getBooleanExtra("dismissed", false);

        if (dismissed) {
            TimerActivity.dismissNotification(context.getApplicationContext());
            TimerActivity.toggleVibrations(context.getApplicationContext(), false);
        }
    }
}
