package com.example.healthtracker;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Environment;
import android.widget.Toast;
import androidx.core.app.NotificationCompat;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class Utils {

    public static void sendNotification(Context context, String title, String message) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "health_tracker_channel")
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, builder.build());
    }

    public static void exportDataToCSV(Context context, List<TensionEntity> dataList, String fileName) {
        if (isExternalStorageWritable()) {
            File exportDir = new File(Environment.getExternalStorageDirectory(), "HealthTracker");
            if (!exportDir.exists()) {
                exportDir.mkdirs();
            }

            File file = new File(exportDir, fileName);
            try (FileWriter writer = new FileWriter(file)) {
                writer.append("ID,Value\n");
                for (TensionEntity data : dataList) {
                    writer.append(data.getId()).append(",").append(data.getValue()).append("\n");
                }
                Toast.makeText(context, "Données exportées avec succès !", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(context, "Erreur lors de l'exportation des données", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(context, "Stockage externe non disponible", Toast.LENGTH_SHORT).show();
        }
    }

    public static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }
}
