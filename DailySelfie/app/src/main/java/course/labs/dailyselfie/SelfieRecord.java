package course.labs.dailyselfie;

import android.graphics.Bitmap;

public class SelfieRecord {
	private Bitmap mSelfieBitmap;
    private String mSelfieFilePath;

	public SelfieRecord(Bitmap bitmap, String path) {
		this.mSelfieBitmap = bitmap;
        this.mSelfieFilePath = path;
	}

    public Bitmap getSelfieBitmap() {
        return mSelfieBitmap;
    }

    public String getSelfiePath() {
        return mSelfieFilePath;
    }

	@Override
	public String toString(){
		return "SelfiePath: " + mSelfieFilePath;
		
	}
}
