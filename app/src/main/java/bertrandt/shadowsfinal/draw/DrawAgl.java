package bertrandt.shadowsfinal.draw;

import android.content.Context;
import android.opengl.GLES20;

import bertrandt.shadowsfinal.importer.ImportAgl;
import bertrandt.shadowsfinal.importer.ImportObj;

/**
 * Created by buhrmanc on 12.02.2018.
 */

public class DrawAgl {
    private ImportAgl mImportAgl;

    private boolean initialised;

    public DrawAgl(final Context context, final String fileName) {

        mImportAgl = new ImportAgl(context, fileName);
        initialised = true;

    }

    public DrawAgl(final Context context, final String fileName,
                   final float moveX, final float moveY, final float moveZ) {

        mImportAgl = new ImportAgl(context, fileName);
        initialised = true;

    }

    public boolean getInitialised() {
        return initialised;
    }

    public void render(int positionAttribute, int normalAttribute, int mTexelCoordinateHandle, int mTextureUniformHandle, boolean onlyPosition) {

        // Pass position information to shader
        mImportAgl.getVerticesBuffer().rewind();
        GLES20.glVertexAttribPointer(positionAttribute, 3, GLES20.GL_FLOAT, false,
                0, mImportAgl.getVerticesBuffer());

        GLES20.glEnableVertexAttribArray(positionAttribute);

        if (!onlyPosition)
        {
            // Pass normal information to shader
            mImportAgl.getNormalsBuffer().rewind();
            GLES20.glVertexAttribPointer(normalAttribute, 3, GLES20.GL_FLOAT, false,
                    0, mImportAgl.getNormalsBuffer());

            GLES20.glEnableVertexAttribArray(normalAttribute);

            // Pass color information to shader
            /**planeColor.position(0);
             GLES20.glVertexAttribPointer(colorAttribute, 4, GLES20.GL_FLOAT, false,
             0, planeColor);

             GLES20.glEnableVertexAttribArray(colorAttribute);*/

            // Pass in the texel information
            mImportAgl.getTexelsBuffer().rewind();
            GLES20.glVertexAttribPointer(mTexelCoordinateHandle, 2, GLES20.GL_FLOAT, false,
                    0, mImportAgl.getTexelsBuffer());
            GLES20.glEnableVertexAttribArray(mTexelCoordinateHandle);

            // Set the active texture unit to texture unit 0.
            GLES20.glActiveTexture(GLES20.GL_TEXTURE1);
            // Bind the texture to this unit.
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mImportAgl.getObjectTextureHandle());

            GLES20.glUniform1i(mTextureUniformHandle, 1);
        }

        // Draw the plane
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, mImportAgl.getPositionSize());
    }
}
