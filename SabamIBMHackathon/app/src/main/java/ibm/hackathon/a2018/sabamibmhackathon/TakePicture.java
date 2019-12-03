package ibm.hackathon.a2018.sabamibmhackathon;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import java.io.File;
import java.io.IOException;

import util.Util;


public class TakePicture extends AppCompatActivity {
    private Button takePictureB;
    static final int REQUEST_TAKE_PHOTO = 1;
    private ImageView mImageView;
    private File imageFile;
    private ProgressBar progressBar;
    private TesseractOcr ocr;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_picture);

        initvieuw();

    }

    private void initvieuw() {
        takePictureB = findViewById(R.id.takePicture);
        mImageView = findViewById(R.id.imageTaken);
        ocr = new TesseractOcr();
        progressBar = findViewById(R.id.progressBar);
        takePictureB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dispatchTakePictureIntent();
            }
        });
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            imageFile = null;
            try {
                imageFile = Util.createImageFile(this);
            } catch (IOException ex) {
                Log.d("error", "Oups, error while creating the file");
            }
            // Continue only if the File was successfully created
            if (imageFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this, "util.provider", imageFile);

                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);


            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_TAKE_PHOTO) {
            setPic();
        }

    }

    private void setPic() {
        // Get the dimensions of the View
        int targetW = mImageView.getWidth();
        int targetH = mImageView.getHeight();

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imageFile.getAbsolutePath(), bmOptions);

        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW / targetW, photoH / targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        //bmOptions.inSampleSize = scaleFactor << 1;
        bmOptions.inPurgeable = true;


        Bitmap bitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath(), bmOptions);

        mImageView.setImageBitmap(bitmap);
        doOCR(bitmap);
    }

    private void doOCR(final Bitmap bitmap) {
        progressBar.setVisibility(View.VISIBLE);

        Log.d("bitmap", bitmap.toString());


        new Thread(new Runnable() {
            public void run() {

                final String result = ocr.getOCRResult(bitmap);

                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        if (result != null && !result.equals("")) {
                            Log.d("result ocr", result);
                            progressBar.setVisibility(View.INVISIBLE);
                            Intent playlistInfo = new Intent(TakePicture.this, PlaylistInfo.class).putExtra("resultOcr", result);
                            startActivity(playlistInfo);
                        }
                    }

                });
            }


        }).start();

    }


}




