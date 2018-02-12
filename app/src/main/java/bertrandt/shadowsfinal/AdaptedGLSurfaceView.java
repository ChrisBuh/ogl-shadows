package bertrandt.shadowsfinal;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;

/**
 * Created by buhrmanc on 12.02.2018.
 */

public class AdaptedGLSurfaceView extends GLSurfaceView {
    private bertrandt.shadowsfinal.Renderer mRenderer;

    public AdaptedGLSurfaceView(Context context) {
        super(context);

        // Render the view only when there is a change in the drawing data
        //setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }

    private final float TOUCH_SCALE_FACTOR = 180.0f / 320;
    private float mPreviousX;
    private float mPreviousY;

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        // MotionEvent reports input details from the touch screen
        // and other input controls. In this case, you are only
        // interested in events where the touch position changed.

        float x = e.getX();
        float y = e.getY();

        switch (e.getAction()) {
            case MotionEvent.ACTION_MOVE:

                float dx = x - mPreviousX;
                float dy = y - mPreviousY;

                mRenderer.setRotationX(
                        mRenderer.getRotationX() +
                                (dx * TOUCH_SCALE_FACTOR));  // = 180.0f / 320

                mRenderer.setRotationY(
                        mRenderer.getRotationY() +
                                (dy * TOUCH_SCALE_FACTOR));  // = 180.0f / 320

                requestRender();
        }

        mPreviousX = x;
        mPreviousY = y;
        return true;
    }

    public void setRenderer(bertrandt.shadowsfinal.Renderer renderer) {
        mRenderer = renderer;
        super.setRenderer(renderer);
    }
}
