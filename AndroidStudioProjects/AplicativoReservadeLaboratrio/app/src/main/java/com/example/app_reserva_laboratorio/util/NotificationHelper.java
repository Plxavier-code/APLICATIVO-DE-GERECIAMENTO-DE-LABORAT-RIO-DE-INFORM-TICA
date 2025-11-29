package com.example.app_reserva_laboratorio.util;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import com.example.app_reserva_laboratorio.R;
import com.example.app_reserva_laboratorio.data.Laboratorio;
import com.example.app_reserva_laboratorio.data.Reserva;
import com.example.app_reserva_laboratorio.data.ReservaRepository;

public class NotificationHelper {

    private static final String CHANNEL_ID = "RESERVA_LAB_CHANNEL";
    private static final String CHANNEL_NAME = "Notificações de Reserva";
    private static final String CHANNEL_DESC = "Canal para notificações sobre status de reservas";

    /**
     * Cria o canal de notificação. Obrigatório para Android 8.0 (API 26) e superior.
     */
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

    /**
     * Constrói e exibe uma notificação para o professor sobre uma nova reserva de laboratório.
     *
     * @param context Contexto da aplicação, necessário para acessar os serviços do sistema.
     * @param novaReserva O objeto da reserva que acabou de ser criada.
     */
    public static void notificarProfessorSobreReserva(Context context, Reserva novaReserva) {
        // Garante que o canal de notificação existe antes de enviar a notificação.
        criarCanalDeNotificacao(context);

        // Busca dados adicionais para enriquecer a notificação
        Laboratorio lab = ReservaRepository.getInstance().getLaboratorioById(novaReserva.getLaboratorioId());
        String nomeLab = (lab != null) ? lab.getNome() : novaReserva.getLaboratorioId();

        String titulo = "Reserva Confirmada: " + nomeLab;
        String conteudo = "A reserva para a data " + novaReserva.getData() + " no horário " + novaReserva.getHorarioFormatado() + " foi realizada com sucesso.";

        // Constrói a notificação
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.icon_fazer_reserva) // Ícone que aparecerá na barra de status
                .setContentTitle(titulo)
                .setContentText(conteudo)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(conteudo)) // Permite texto maior
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true); // Remove a notificação ao ser tocada

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

        // O ID da notificação deve ser único. Usar o hash do ID da reserva é uma boa prática.
        notificationManager.notify(novaReserva.getIdReserva().hashCode(), builder.build());
    }
}
