package bertrandt.shadowsfinal.draw;

import android.content.Context;
import android.opengl.GLES20;

import bertrandt.shadowsfinal.importer.ImportObj;


/**
 * Created by buhrmanc on 05.02.2018.
 */

public class DrawObj {

    private ImportObj mImportObj;

    private boolean initialised;

    public DrawObj(final Context context, final String fileName) {

        mImportObj = new ImportObj(context, fileName);
        initialised = true;

    }

    public DrawObj(final Context context, final String fileName,
                   final float moveX, final float moveY, final float moveZ, final float scale) {

        mImportObj = new ImportObj(context, fileName, moveX, moveY, moveZ, scale);
        initialised = true;

    }

    public boolean getInitialised() {
        return initialised;
    }

    public void render(int positionAttribute, int normalAttribute, int mTexelCoordinateHandle, int mTextureUniformHandle, boolean onlyPosition) {

        // Pass position information to shader
        mImportObj.getVerticesBuffer().rewind();
        GLES20.glVertexAttribPointer(positionAttribute, 3, GLES20.GL_FLOAT, false,
                0, mImportObj.getVerticesBuffer());

        GLES20.glEnableVertexAttribArray(positionAttribute);

        if (!onlyPosition)
        {
            // Pass normal information to shader
            mImportObj.getNormalsBuffer().rewind();
            GLES20.glVertexAttribPointer(normalAttribute, 3, GLES20.GL_FLOAT, false,
                    0, mImportObj.getNormalsBuffer());

            GLES20.glEnableVertexAttribArray(normalAttribute);

            // Pass color information to shader
            /**planeColor.position(0);
             GLES20.glVertexAttribPointer(colorAttribute, 4, GLES20.GL_FLOAT, false,
             0, planeColor);

             GLES20.glEnableVertexAttribArray(colorAttribute);*/

            // Pass in the texel information
            mImportObj.getTexelsBuffer().rewind();
            GLES20.glVertexAttribPointer(mTexelCoordinateHandle, 2, GLES20.GL_FLOAT, false,
                    0, mImportObj.getTexelsBuffer());
            GLES20.glEnableVertexAttribArray(mTexelCoordinateHandle);

            // Set the active texture unit to texture unit 0.
            GLES20.glActiveTexture(GLES20.GL_TEXTURE1);
            // Bind the texture to this unit.
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mImportObj.getObjectTextureHandle());

            GLES20.glUniform1i(mTextureUniformHandle, 1);
        }

        // Draw the plane
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, mImportObj.getPositionSize());
    }
}

