
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
import androidx.room.Room;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Utils {
    public static String formatDate(long timestamp) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
        return dateFormat.format(new Date(timestamp));
    }

    public static void sendNotification(Context context, String title, String message) {
        String channelId = "health_tracker_channel";
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        
        // Créer le canal de notification pour Android 8.0+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    channelId,
                    "Rappels HealthTracker",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            channel.setDescription("Notifications pour les rappels quotidiens");
            notificationManager.createNotificationChannel(channel);
        }
        
        // Intent pour ouvrir l'app quand la notification est cliquée
        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                context, 
                0, 
                intent, 
                PendingIntent.FLAG_IMMUTABLE
        );
        
        // Construire la notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);
        
        // Afficher la notification
        notificationManager.notify(1, builder.build());
    }

    public static void exportDataToCSV(Context context) {
        try {
            // Obtenir le dossier de stockage interne
            File directory = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
            if (directory == null) {
                directory = context.getFilesDir();
            }
            
            if (!directory.exists()) {
                directory.mkdirs();
            }
            
            // Créer le fichier avec date et heure
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault());
            String fileName = "health_data_" + dateFormat.format(new Date()) + ".csv";
            File file = new File(directory, fileName);
            
            FileWriter writer = new FileWriter(file);
            
            // Accès à la base de données
            HealthDatabase db = Room.databaseBuilder(context, HealthDatabase.class, "health-database")
                    .allowMainThreadQueries()
                    .build();
            
            // Écriture des en-têtes
            writer.append("Type,Valeur,Date\n");
            
            // Exportation des données de tension
            List<TensionEntity> tensions = db.tensionDao().getAll();
            for (TensionEntity tension : tensions) {
                writer.append("Tension,")
                      .append(tension.getValue())
                      .append(",")
                      .append(formatDate(tension.getTimestamp()))
                      .append("\n");
            }
            
            // Exportation des données de diabète
            List<DiabeteEntity> diabeteValues = db.diabeteDao().getAll();
            for (DiabeteEntity diabete : diabeteValues) {
                writer.append("Diabète,")
                      .append(diabete.getValue())
                      .append(",")
                      .append(formatDate(diabete.getTimestamp()))
                      .append("\n");
            }
            
            // Exportation des données de masse corporelle
            List<MasseCorporelleEntity> masseValues = db.masseCorporelleDao().getAll();
            for (MasseCorporelleEntity masse : masseValues) {
                writer.append("IMC,")
                      .append(masse.getValue())
                      .append(",")
                      .append(formatDate(masse.getTimestamp()))
                      .append("\n");
            }
            
            // Exportation des données de souffle
            List<SouffleEntity> souffleValues = db.souffleDao().getAll();
            for (SouffleEntity souffle : souffleValues) {
                writer.append("Souffle,")
                      .append(souffle.getValue())
                      .append(",")
                      .append(formatDate(souffle.getTimestamp()))
                      .append("\n");
            }
            
            // Fermer le fichier
            writer.flush();
            writer.close();
            
            Toast.makeText(context, "Données exportées avec succès: " + file.getAbsolutePath(), Toast.LENGTH_LONG).show();
            
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(context, "Erreur lors de l'exportation: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
