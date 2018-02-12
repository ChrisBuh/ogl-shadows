package bertrandt.shadowsfinal.common;

import android.util.Log;

/**
 * Created by buhrmanc on 12.02.2018.
 */

public class FPSCounter {
    long startTime = System.nanoTime();
    int frames = 0;

    public void logFrame() {
        frames++;
        if(System.nanoTime() - startTime >= 1000000000) {
            Log.i("FPSCounter", "fps: " + frames);
            frames = 0;
            startTime = System.nanoTime();
        }
    }
}