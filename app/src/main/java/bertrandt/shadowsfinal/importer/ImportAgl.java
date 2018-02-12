package bertrandt.shadowsfinal.importer;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.Matrix;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import bertrandt.shadowsfinal.R;
import bertrandt.shadowsfinal.common.TextureHelper;

/**
 * Created by buhrmanc on 12.02.2018.
 */

public class ImportAgl {
    private static final String TAG = "ImportAgl";
    private Context mContext;

    private String mFileName;

    private List<String> mVerticesList;
    private List<String> mNormalsList;
    private List<String> mColorsList;
    private List<String> mTexelsList;

    private FloatBuffer mVerticesBuffer;
    private FloatBuffer mNormalsBuffer;
    private FloatBuffer mColorsBuffer;
    private FloatBuffer mTexelsBuffer;

    private int mBytesPerFloat = 4;
    private int mVertexSize = 3;
    private int mNormalSize = 3;
    private int mColorSize = 4;
    private int mTexelSize = 2;

    private float mMoveX=0.0f;
    private float mMoveY=0.0f;
    private float mMoveZ=0.0f;

    private int mObjectTextureHandle;

    public ImportAgl(Context context, String fileName) {
        this.mContext = context;
        this.mFileName = fileName;
        readRaw();
        allocateBuffer();
        populateBuffer();
    }

    public ImportAgl(Context context, String fileName,
                     final float moveX, final float moveY, final float moveZ) {
        this.mContext = context;
        this.mFileName = fileName;
        this.mMoveX = moveX;
        this.mMoveY = moveY;
        this.mMoveZ = moveZ;
        readRaw();
        allocateBuffer();
        populateBuffer();
    }

    private void readRaw() {

        mVerticesList = new ArrayList<>();
        mNormalsList = new ArrayList<>();
        mColorsList = new ArrayList<>();
        mTexelsList = new ArrayList<>();

        try {
            Scanner scanner = new Scanner(mContext.getAssets().open(mFileName));
            // Loop through all its lines
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                //Log.i(TAG, "ObjectLoader: " + line);

                if (line.startsWith("v ")) {
                    // Add vertex line to list of vertices
                    mVerticesList.add(line);
                } else if (line.startsWith("c ")) {
                    // Add color line to colorList
                    mColorsList.add(line);
                } else if (line.startsWith("n ")) {
                    // Add normal line to normalList
                    mNormalsList.add(line);
                } else if (line.startsWith("t ")) {
                    // Add normal line to texelList
                    mTexelsList.add(line);
                }
            }
            // Close the scanner
            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "readRaw: object file could not be read");
        }

    }

    private void allocateBuffer() {
        // Create buffer for vertices
        mVerticesBuffer = ByteBuffer.allocateDirect(mVerticesList.size() * mVertexSize * mBytesPerFloat)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();

        // Create buffer for colors
        mColorsBuffer = ByteBuffer.allocateDirect(mColorsList.size() * mColorSize * mBytesPerFloat)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();

        // Create buffer for normals
        mNormalsBuffer = ByteBuffer.allocateDirect(mNormalsList.size() * mNormalSize * mBytesPerFloat)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();

        // Create buffer for texels
        mTexelsBuffer = ByteBuffer.allocateDirect(mTexelsList.size() * mTexelSize * mBytesPerFloat)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();
    }

    private void populateBuffer() {
        for (String vertex : mVerticesList) {
            String coords[] = vertex.split(" "); // Split by space
            float x = Float.parseFloat(coords[1]);
            float y = Float.parseFloat(coords[2]);
            float z = Float.parseFloat(coords[3]);
            //Log.i(TAG, "ObjectLoader: Position:  x: " + x + " y: " + y + " z: " + z);
            mVerticesBuffer.put(x);
            mVerticesBuffer.put(y);
            mVerticesBuffer.put(z);
        }
        mVerticesBuffer.position(0);

        for (String color : mColorsList) {
            String coords[] = color.split(" "); // Split by space
            float x = Float.parseFloat(coords[1]);
            float y = Float.parseFloat(coords[2]);
            float z = Float.parseFloat(coords[3]);
            //Log.i(TAG, "ObjectLoader: Texel: x: " + x + " y: " + y);
            mColorsBuffer.put(x);
            mColorsBuffer.put(y);
            mColorsBuffer.put(z);
            mColorsBuffer.put(1);

        }
        mColorsBuffer.position(0);

        for (String normal : mNormalsList) {
            String coords[] = normal.split(" "); // Split by space
            float x = Float.parseFloat(coords[1]);
            float y = Float.parseFloat(coords[2]);
            float z = Float.parseFloat(coords[3]);
            //Log.i(TAG, "ObjectLoader: Normal: x: " + x + " y: " + y + " z: " + z);
            mNormalsBuffer.put(x);
            mNormalsBuffer.put(y);
            mNormalsBuffer.put(z);
        }
        mNormalsBuffer.position(0);

        for (String texel : mTexelsList) {
            String coords[] = texel.split(" "); // Split by space
            float u = Float.parseFloat(coords[1]);
            float v = Float.parseFloat(coords[2]);
            //Log.i(TAG, "ObjectLoader: Normal: x: " + x + " y: " + y + " z: " + z);
            mTexelsBuffer.put(u);
            mTexelsBuffer.put(v);

        }
        mTexelsBuffer.position(0);

        mObjectTextureHandle = TextureHelper.loadTexture(mContext, R.drawable.gte);
        GLES20.glGenerateMipmap(GLES20.GL_TEXTURE_2D);
    }

    public FloatBuffer getVerticesBuffer() {
        return mVerticesBuffer;
    }

    public FloatBuffer getNormalsBuffer() {
        return mNormalsBuffer;
    }

    public FloatBuffer getTexelsBuffer() {
        return mTexelsBuffer;
    }

    public int getObjectTextureHandle() {
        return mObjectTextureHandle;
    }

    public int getPositionSize(){
        return mVerticesList.size();
    }


}
