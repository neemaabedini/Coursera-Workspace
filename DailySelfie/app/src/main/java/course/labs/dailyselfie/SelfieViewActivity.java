package course.labs.dailyselfie;

import android.app.AlarmManager;
import android.app.ListActivity;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SelfieViewActivity extends ListActivity {
	private static final long FIVE_MINS = 5 * 60 * 1000;
	private static final String TAG = "DailySelfieActivity";
	private SelfieViewAdapter mAdapter;
    static final int REQUEST_TAKE_PHOTO = 1;
    String mCurrentPhotoPath;
    private AlarmManager mAlarmManager;
    private Intent mNotificationReceiverIntent;
    private PendingIntent mNotificationReceiverPendingIntent;
    private static final long ALARM_DELAY = 2 * 60 * 1000L;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mAdapter = new SelfieViewAdapter(getApplicationContext());
		setListAdapter(mAdapter);

        // Get the AlarmManager Service
        mAlarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        // Create an Intent to broadcast to the AlarmNotificationReceiver
        mNotificationReceiverIntent = new Intent(SelfieViewActivity.this,
                AlarmNotificationReceiver.class);

        // Create an PendingIntent that holds the NotificationReceiverIntent
        mNotificationReceiverPendingIntent = PendingIntent.getBroadcast(
                SelfieViewActivity.this, 0, mNotificationReceiverIntent, 0);





	}

	@Override
	protected void onResume() {
		super.onResume();

        mAdapter.removeAllViews();
        mAdapter.addAllViews(getExternalFilesDir(Environment.DIRECTORY_PICTURES));

	}

	@Override
	protected void onPause() {

		super.onPause();
	}

	// Callback method used by PlaceDownloaderTask
	public void addNewSelfie(SelfieRecord selfie) {
		Log.i(TAG, "Entered addNewSelfie()");

        if (selfie.getSelfieBitmap() == null) {

            Toast.makeText(getApplicationContext(), "Bitmap could not be acquired", Toast.LENGTH_LONG).show();
        }

        mAdapter.add(selfie);
        Log.i(TAG, "Selfie location is "+ selfie.toString());

		
	}

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                Log.e(TAG, "IOException");
                ex.printStackTrace();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        Log.i(TAG, "onActivityResult()");

    if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {

        galleryAddPic();

        Bitmap selfieBitmap = getBitmap();

        SelfieRecord selfie = new SelfieRecord(selfieBitmap, mCurrentPhotoPath);
        addNewSelfie(selfie);

        // Cancel all alarms using mNotificationReceiverPendingIntent
        mAlarmManager.cancel(mNotificationReceiverPendingIntent);

        // Set repeating alarm
        mAlarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME,
                SystemClock.elapsedRealtime() + ALARM_DELAY,
                ALARM_DELAY,
                mNotificationReceiverPendingIntent);

        // Show Toast message
        Toast.makeText(getApplicationContext(), "Repeating Alarm Set",
                Toast.LENGTH_LONG).show();

        }
    }

    private Bitmap getBitmap() {

        Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath);
        return bitmap;
    }

    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

        dispatchTakePictureIntent();

	    return super.onOptionsItemSelected(item);

	}

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        SelfieRecord selfieRecord = (SelfieRecord) l.getItemAtPosition(position);

        String path = selfieRecord.getSelfiePath();

        Intent intent = new Intent(getApplicationContext(), SelfieDetailViewActivity.class);
        intent.putExtra("Path", path);

        startActivity(intent);



    }
}
