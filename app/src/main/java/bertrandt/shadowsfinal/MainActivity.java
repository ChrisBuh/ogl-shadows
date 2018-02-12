package bertrandt.shadowsfinal;

import android.opengl.GLSurfaceView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    private AdaptedGLSurfaceView mGLView;
    private Renderer renderer;
    /**
     * Type of shadow bias to reduce unnecessary shadows
     * 	- constant bias
     * 	- bias value is variable according to slope
     */
    private float mBiasType = 0.0f;
    /**
     * Type of shadow algorithm
     * 	- simple shadow (shadow value is only two state (yes/no) so aliasing is visible, no blur effect is possible)
     *  - Percentage Closer Filtering (PCF)
     */
    private float mShadowType = 0.0f;
    /**
     * Shadow map size:
     * 	- displayWidth * SHADOW_MAP_RATIO
     * 	- displayHeight * SHADOW_MAP_RATIO
     */
    private float mShadowMapRatio = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Create a GLSurfaceView instance and set it
        // as the ContentView for this Activity
        mGLView = new AdaptedGLSurfaceView(this);

        // Create an OpenGL ES 2.0 context.
        mGLView.setEGLContextClientVersion(2);

        renderer = new Renderer(this);
        mGLView.setRenderer(renderer);

        setContentView(mGLView);
    }



    @Override
    protected void onPause() {
        super.onPause();
        mGLView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mGLView.onResume();
    }

}
