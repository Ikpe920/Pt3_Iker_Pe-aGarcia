package com.example.pt3_iker_peagarcia;

import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "CicleVida";

    // Els comptadors són static: sobreviuen encara que l'Activity es recreï
    private static final int[] counts = new int[Cycle.values().length];

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        for (Cycle cycle : Cycle.values()) {
            updateView(cycle);
        }

        if (savedInstanceState == null) {
            register(Cycle.ON_CREATE);
        }
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
        if (!isChangingConfigurations()) {
            register(Cycle.ON_DESTROY);
        } else {
            Log.d(TAG, "onDestroy ignorat: canvi de configuració");
        }
        super.onDestroy();
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Log.d(TAG, "Configuració canviada (rotació): l'Activity no es recrea");
    }

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