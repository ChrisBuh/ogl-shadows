package bertrandt.shadowsfinal;

import android.content.Context;
import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import bertrandt.shadowsfinal.common.RenderConstants;
import bertrandt.shadowsfinal.common.TextureHelper;

/**
 * Created by buhrmanc on 12.02.2018.
 */

public class Plane {
    private final FloatBuffer planePosition;
    private final FloatBuffer planeNormal;
    //private final FloatBuffer planeColor;

    private FloatBuffer planeTexel;

    private int mPlaneTextureHandle;

    //TODO: remove
    int translateY = 0;
    int translateZ = 0;

    float[] planePositionData = {
            // X, Y, Z,
            -50.0f, -5.0f, -50.0f + translateZ,
            -50.0f, -5.0f, 50.0f + translateZ,
            50.0f, -5.0f, -50.0f + translateZ,
            -50.0f, -5.0f, 50.0f + translateZ,
            50.0f, -5.0f, 50.0f + translateZ,
            50.0f, -5.0f, -50.0f + translateZ
    };

    float[] planeNormalData = {
            // nX, nY, nZ
            0.0f, 1.0f, 0.0f,
            0.0f, 1.0f, 0.0f,
            0.0f, 1.0f, 0.0f,
            0.0f, 1.0f, 0.0f,
            0.0f, 1.0f, 0.0f,
            0.0f, 1.0f, 0.0f
    };

    /**float[] planeColorData = {
            // R, G, B, A
            0.5f, 0.5f, 0.5f, 1.0f,
            0.5f, 0.5f, 0.5f, 1.0f,
            0.5f, 0.5f, 0.5f, 1.0f,
            0.5f, 0.5f, 0.5f, 1.0f,
            0.5f, 0.5f, 0.5f, 1.0f,
            0.5f, 0.5f, 0.5f, 1.0f
    };*/

    float[] planeTexelData = {
            // U, V
            0.0f, 0.0f,
            0.0f, 1.0f,
            1.0f, 0.0f,
            0.0f, 1.0f,
            1.0f, 1.0f,
            1.0f, 0.0f
    };

    public Plane(Context context) {
        // Buffer initialization
        ByteBuffer bPos = ByteBuffer.allocateDirect(planePositionData.length * RenderConstants.FLOAT_SIZE_IN_BYTES);
        bPos.order(ByteOrder.nativeOrder());
        planePosition = bPos.asFloatBuffer();

        ByteBuffer bNormal = ByteBuffer.allocateDirect(planeNormalData.length * RenderConstants.FLOAT_SIZE_IN_BYTES);
        bNormal.order(ByteOrder.nativeOrder());
        planeNormal = bNormal.asFloatBuffer();

        planeTexel = ByteBuffer.allocateDirect(planeTexelData.length * RenderConstants.FLOAT_SIZE_IN_BYTES)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();
        planeTexel.put(planeTexelData).position(0);

        mPlaneTextureHandle = TextureHelper.loadTexture(context, R.drawable.floor);
        GLES20.glGenerateMipmap(GLES20.GL_TEXTURE_2D);

        /**ByteBuffer bColor = ByteBuffer.allocateDirect(planeColorData.length * RenderConstants.FLOAT_SIZE_IN_BYTES);
        bColor.order(ByteOrder.nativeOrder());
        planeColor = bColor.asFloatBuffer();*/

        planePosition.put(planePositionData).position(0);
        planeNormal.put(planeNormalData).position(0);
        /**planeColor.put(planeColorData).position(0);*/
    }

    public void render(int positionAttribute, int normalAttribute, int mTexelCoordinateHandle, int mTextureUniformHandle, boolean onlyPosition) {

        // Pass position information to shader
        planePosition.position(0);
        GLES20.glVertexAttribPointer(positionAttribute, 3, GLES20.GL_FLOAT, false,
                0, planePosition);

        GLES20.glEnableVertexAttribArray(positionAttribute);

        if (!onlyPosition)
        {
            // Pass normal information to shader
            planeNormal.position(0);
            GLES20.glVertexAttribPointer(normalAttribute, 3, GLES20.GL_FLOAT, false,
                    0, planeNormal);

            GLES20.glEnableVertexAttribArray(normalAttribute);

            // Pass color information to shader
            /**planeColor.position(0);
            GLES20.glVertexAttribPointer(colorAttribute, 4, GLES20.GL_FLOAT, false,
                    0, planeColor);

            GLES20.glEnableVertexAttribArray(colorAttribute);*/

            // Pass in the texel information
            planeTexel.rewind();
            GLES20.glVertexAttribPointer(mTexelCoordinateHandle, 2, GLES20.GL_FLOAT, false,
                    0, planeTexel);
            GLES20.glEnableVertexAttribArray(mTexelCoordinateHandle);

            // Set the active texture unit to texture unit 0.
            GLES20.glActiveTexture(GLES20.GL_TEXTURE1);
            // Bind the texture to this unit.
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mPlaneTextureHandle);

            GLES20.glUniform1i(mTextureUniformHandle, 1);
        }

        // Draw the plane
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 6);
    }
}