package com.example.healthtracker;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class NotificationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Utils.sendNotification(context, "Rappel quotidien", "N'oubliez pas d'enregistrer vos valeurs de santé aujourd'hui !");
    }
}
