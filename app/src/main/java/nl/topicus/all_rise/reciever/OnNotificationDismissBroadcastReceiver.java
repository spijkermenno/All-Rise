package nl.topicus.all_rise.reciever;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import nl.topicus.all_rise.service.NotificationService;

public class OnNotificationDismissBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationService.reset();
    }

}