package com.example.a2.util;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import com.example.a2.R;
import com.example.a2.data.Laboratorio;
import com.example.a2.data.Reserva;
import com.example.a2.data.ReservaRepository;

public class NotificationHelper {

    private static final String CHANNEL_ID = "RESERVA_LAB_CHANNEL";
    private static final String CHANNEL_NAME = "Notificações de Reserva";
    private static final String CHANNEL_DESC = "Canal para notificações sobre status de reservas";

    private static void criarCanalDeNotificacao(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription(CHANNEL_DESC);
            NotificationManager manager = context.getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(channel);
            }
        }
    }

    public static void notificarConfirmacao(Context context, Reserva novaReserva) {
        criarCanalDeNotificacao(context);

        Laboratorio lab = ReservaRepository.getInstance().getLaboratorioById(novaReserva.getLaboratorioId());
        String nomeLab = (lab != null) ? lab.getNome() : novaReserva.getLaboratorioId();

        String titulo = "Reserva Confirmada: " + nomeLab;
        String conteudo = "Sua reserva para " + novaReserva.getData() + " às " + novaReserva.getHorarioFormatado() + " foi confirmada.";

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground) // Ícone padrão do app
                .setContentTitle(titulo)
                .setContentText(conteudo)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(conteudo))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);

        NotificationManagerCompat.from(context).notify(novaReserva.getIdReserva().hashCode(), builder.build());
    }

    public static void notificarSobreCancelamento(Context context, Reserva reservaCancelada) {
        criarCanalDeNotificacao(context);

        Laboratorio lab = ReservaRepository.getInstance().getLaboratorioById(reservaCancelada.getLaboratorioId());
        String nomeLab = (lab != null) ? lab.getNome() : reservaCancelada.getLaboratorioId();

        String titulo = "Reserva Cancelada: " + nomeLab;
        String conteudo = "A sua reserva para " + nomeLab + " no dia " + reservaCancelada.getData() + " foi cancelada.";

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_dialog_info) // Ícone padrão do Android
                .setContentTitle(titulo)
                .setContentText(conteudo)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(conteudo))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);

        NotificationManagerCompat.from(context).notify(reservaCancelada.getIdReserva().hashCode(), builder.build());
    }
}
