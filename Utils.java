
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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Utils {

    public static void sendNotification(Context context, String title, String message) {
        // Créer le canal de notification pour Android 8.0 et supérieur
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    "health_tracker_channel",
                    "Canal Health Tracker",
                    NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ? PendingIntent.FLAG_IMMUTABLE : 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "health_tracker_channel")
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, builder.build());
    }

    public static void exportDataToCSV(Context context, List<?> dataList, String fileName) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH-mm", Locale.getDefault());
        String formattedDate = sdf.format(new Date());
        String actualFileName = fileName.replace(".csv", "_" + formattedDate + ".csv");
        
        try {
            File exportDir = new File(context.getExternalFilesDir(null), "HealthTracker");
            if (!exportDir.exists()) {
                exportDir.mkdirs();
            }

            File file = new File(exportDir, actualFileName);
            FileWriter fw = new FileWriter(file);
            
            // En-tête CSV
            if (dataList.size() > 0 && dataList.get(0) instanceof TensionEntity) {
                fw.append("Id,Valeur,Date\n");
                
                for (Object obj : dataList) {
                    TensionEntity tension = (TensionEntity) obj;
                    String date = sdf.format(new Date(tension.getTimestamp()));
                    fw.append(String.valueOf(tension.getId())).append(",")
                      .append(tension.getValue()).append(",")
                      .append(date).append("\n");
                }
            }
            // Ajouter ici d'autres conditions pour les autres types d'entités
            
            fw.flush();
            fw.close();
            
            notifyExportSuccess(context, file.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(context, "Erreur lors de l'exportation: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
    
    private static void notifyExportSuccess(Context context, String filePath) {
        Toast.makeText(context, "Données exportées avec succès dans: " + filePath, Toast.LENGTH_LONG).show();
    }
    
    public static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }
}
