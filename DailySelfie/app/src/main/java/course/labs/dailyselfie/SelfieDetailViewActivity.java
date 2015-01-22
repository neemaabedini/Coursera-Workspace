package course.labs.dailyselfie;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * Created by Neema on 11/24/14.
 */
public class SelfieDetailViewActivity extends Activity {

    private static final String TAG = "SelfieDetailViewActivity";
    private ImageView mImageView;
    private String mPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        mPath = intent.getStringExtra("Path");

        setContentView(R.layout.selfie_detail_view);
        mImageView = (ImageView) findViewById(R.id.imageView);

        addImage();
    }

    private void addImage() {


        Bitmap sourceBitmap = BitmapFactory.decodeFile(mPath);
        Matrix matrix = new Matrix();
        matrix.postRotate(90);
        Bitmap rotatedBitmap = Bitmap.createBitmap(sourceBitmap, 0, 0, sourceBitmap.getWidth(),
                sourceBitmap.getHeight(), matrix, true);

        mImageView.setImageBitmap(rotatedBitmap);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}
