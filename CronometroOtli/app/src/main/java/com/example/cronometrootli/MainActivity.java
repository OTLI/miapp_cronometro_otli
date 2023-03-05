package com.example.cronometrootli;


import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private Timer timer;
    private long startTime = 0L;
    private TextView tv_Tiempo;
    private ListView lv_List_Vueltas;
    private Button btn_Empezar_Parar, btn_Vuelta;
    private Handler handler = new Handler();
    private ArrayList<String> listaVueltas = new ArrayList<>();
    private ArrayAdapter<String> listaAdapter;

    private int cont = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv_Tiempo = findViewById(R.id.tv_tiempo);
        lv_List_Vueltas = findViewById(R.id.lv_lista_vueltas);
        btn_Empezar_Parar = findViewById(R.id.btn_empezar_parar);
        btn_Vuelta = findViewById(R.id.btn_vuelta);

        btn_Empezar_Parar.setCompoundDrawablesWithIntrinsicBounds(R.drawable.baseline_play_circle_24, 0, 0, 0);

        listaAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listaVueltas);
        lv_List_Vueltas.setAdapter(listaAdapter);

        btn_Empezar_Parar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (timer == null) {
                    startTime = System.currentTimeMillis();
                    timer = new Timer();
                    timer.scheduleAtFixedRate(new TimerTask() {
                        public void run() {
                            actualizarTiempo();
                        }
                    }, 0, 1000);
                    btn_Empezar_Parar.setText("Parar");
                    btn_Empezar_Parar.setCompoundDrawablesWithIntrinsicBounds(R.drawable.baseline_stop_circle_24, 0, 0, 0);
                    btn_Vuelta.setText("Vuelta");
                    cont = 0;
                    listaVueltas.clear();
                    listaAdapter.notifyDataSetChanged();
                } else {
                    timer.cancel();
                    timer = null;
                    btn_Empezar_Parar.setText("Empezar");
                    btn_Empezar_Parar.setCompoundDrawablesWithIntrinsicBounds(R.drawable.baseline_play_circle_24, 0, 0, 0);
                    btn_Vuelta.setText("Reiniciar");
                }
            }
        });

        btn_Vuelta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (timer == null) {
                    startTime = 0L;
                    tv_Tiempo.setText("00:00:00");
                    btn_Vuelta.setText("Vuelta");
                    listaVueltas.clear();
                    listaAdapter.notifyDataSetChanged();
                } else {
                    String tiempoVuelta = getTiempo();
                    listaVueltas.add(0, String.valueOf(cont) + ": " + tiempoVuelta);
                    cont++;
                    listaAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    private void actualizarTiempo() {
        final String timerText = getTiempo();

        handler.post(new Runnable() {
            @Override
            public void run() {
                tv_Tiempo.setText(timerText);
            }
        });
    }

    private String getTiempo() {
        long tiempo = System.currentTimeMillis() - startTime;
        int segundos = (int) (tiempo / 1000) % 60;
        int minutos = (int) ((tiempo / (1000 * 60)) % 60);
        int horas = (int) ((tiempo / (1000 * 60 * 60)) % 24);

        return String.format(Locale.getDefault(), "%02d:%02d:%02d", horas, minutos, segundos);
    }
}