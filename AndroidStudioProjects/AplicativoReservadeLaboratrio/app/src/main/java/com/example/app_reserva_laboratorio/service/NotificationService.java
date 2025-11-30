package com.example.app_reserva_laboratorio.service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import com.example.app_reserva_laboratorio.R;
import com.example.app_reserva_laboratorio.data.Reserva;

public class NotificationService {

    private static final String CHANNEL_ID = "reserva_laboratorio_channel";
    private Context context;
    private int notificationId = 1;

    public NotificationService(Context context) {
        this.context = context;
        createNotificationChannel();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Notificações de Reserva de Laboratório";
            String description = "Canal para notificações de confirmação e cancelamento de reservas.";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void showNotification(String title, String content) {
        // A permissão POST_NOTIFICATIONS é necessária para o Android 13+
        // Certifique-se de que a permissão é solicitada em sua atividade.
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(content)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(notificationId++, builder.build());
    }

    public void notificarProfessor(Reserva reserva) {
        String title = "Nova Reserva de Laboratório";
        String content = "Uma nova reserva foi feita para o dia " + reserva.getData() + " às " + reserva.getHorarioFormatado();
        showNotification(title, content);
    }

    public void notificarAlunoConfirmacao(Reserva reserva) {
        String title = "Reserva Confirmada";
        String content = "Sua reserva para o dia " + reserva.getData() + " às " + reserva.getHorarioFormatado() + " foi confirmada.";
        showNotification(title, content);
    }

    public void notificarAlunoCancelamento(Reserva reserva) {
        String title = "Reserva Cancelada";
        String content = "Sua reserva para o dia " + reserva.getData() + " às " + reserva.getHorarioFormatado() + " foi cancelada.";
        showNotification(title, content);
    }
}
