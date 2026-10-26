package com.example.pt3_iker_peagarcia;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "CicleVida";
    private static final String KEY_COUNTS = "key_counts";

    private enum Cycle {
        ON_CREATE(R.id.tvOnCreateCount),
        ON_START(R.id.tvOnStartCount),
        ON_RESTART(R.id.tvOnRestartCount),
        ON_RESUME(R.id.tvOnResumeCount),
        ON_PAUSE(R.id.tvOnPauseCount),
        ON_STOP(R.id.tvOnStopCount),
        ON_DESTROY(R.id.tvOnDestroyCount);

        private final int counterId;

        Cycle(int counterId) {
            this.counterId = counterId;
        }
    }

    private final int[] counts = new int[Cycle.values().length];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Recuperem els comptadors si l'Activity s'ha recreat (p. ex. en girar la pantalla)
        try {
            if (savedInstanceState != null) {
                int[] saved = savedInstanceState.getIntArray(KEY_COUNTS);
                if (saved != null && saved.length == counts.length) {
                    System.arraycopy(saved, 0, counts, 0, counts.length);
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "Error restaurant els comptadors", e);
            showError();
        }

        // Mostrem tots els comptadors (inicialment a 0)
        for (Cycle cycle : Cycle.values()) {
            updateView(cycle);
        }

        register(Cycle.ON_CREATE);
    }

    @Override
    protected void onStart() {
        super.onStart();
        register(Cycle.ON_START);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        register(Cycle.ON_RESTART);
    }

    @Override
    protected void onResume() {
        super.onResume();
        register(Cycle.ON_RESUME);
    }

    @Override
    protected void onPause() {
        register(Cycle.ON_PAUSE);
        super.onPause();
    }

    @Override
    protected void onStop() {
        register(Cycle.ON_STOP);
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        register(Cycle.ON_DESTROY);
        super.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        try {
            outState.putIntArray(KEY_COUNTS, counts);
        } catch (Exception e) {
            Log.e(TAG, "Error desant els comptadors", e);
        }
    }

    /** Incrementa el comptador del cicle i n'actualitza el TextView. */
    private void register(Cycle cycle) {
        try {
            counts[cycle.ordinal()]++;
            Log.d(TAG, cycle.name() + ": " + counts[cycle.ordinal()]);
            updateView(cycle);
        } catch (Exception e) {
            Log.e(TAG, "Error registrant " + cycle.name(), e);
            showError();
        }
    }

    private void updateView(Cycle cycle) {
        TextView tv = findViewById(cycle.counterId);
        if (tv == null) {
            Log.e(TAG, "No s'ha trobat el TextView de " + cycle.name());
            return;
        }
        tv.setText(getString(R.string.times_format, counts[cycle.ordinal()]));
    }

    private void showError() {
        Toast.makeText(this, R.string.error_message, Toast.LENGTH_SHORT).show();
    }
}