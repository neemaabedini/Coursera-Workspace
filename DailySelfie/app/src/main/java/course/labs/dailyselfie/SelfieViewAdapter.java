package course.labs.dailyselfie;

import java.io.File;
import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class SelfieViewAdapter extends BaseAdapter {

    private static final String TAG = "SelfieViewAdapter";
	private ArrayList<SelfieRecord> list = new ArrayList<SelfieRecord>();
	private static LayoutInflater inflater = null;
	private Context mContext;

	public SelfieViewAdapter(Context context) {
		mContext = context;
		inflater = LayoutInflater.from(mContext);
	}

	public int getCount() {
		return list.size();
	}

	public Object getItem(int position) {
		return list.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {

		View newView = convertView;
		ViewHolder holder;

		SelfieRecord curr = list.get(position);

		if (null == convertView) {
			holder = new ViewHolder();
			newView = inflater.inflate(R.layout.place_badge_view, null);
			holder.thumbnail = (ImageView) newView.findViewById(R.id.flag);
			holder.loc = (TextView) newView.findViewById(R.id.selfie_name);
			newView.setTag(holder);
			
		} else {
			holder = (ViewHolder) newView.getTag();
		}


		holder.thumbnail.setImageBitmap(curr.getSelfieBitmap());
		holder.loc.setText("File: " + curr.getSelfiePath());

		return newView;
	}


	
	static class ViewHolder {
	
		ImageView thumbnail;
		TextView loc;
		
	}

    // needs modification to work with addAllViews
	public void add(SelfieRecord listItem) {
		list.add(listItem);
		notifyDataSetChanged();
	}
	
	public ArrayList<SelfieRecord> getList(){
		return list;
	}
	
	public void removeAllViews(){
		list.clear();
		this.notifyDataSetChanged();
	}

    // Add all the SelfieRecords into Views
    public void addAllViews(File storageDir) {

        // get list of files in Selfie Directory
        ArrayList<File> files = getFileList(storageDir);

        if (files.isEmpty()) {
            Log.i(TAG, "There are no files in /Pictures/");
            return;
        }

        for (File file : files) {
            Bitmap bitmap = new BitmapFactory().decodeFile(file.getAbsolutePath());
            Bitmap rotatedBitmap = rotateBitmap(bitmap);
            SelfieRecord selfieRecord = new SelfieRecord( rotatedBitmap , file.getAbsolutePath());
            add(selfieRecord);
        }


    }

    // Get a list of the files that are in the Selfie Image folder
    private ArrayList<File> getFileList(File storageDir) {

        ArrayList<File> returnFiles = new ArrayList<File>();
        File[] files = storageDir.listFiles();


        for(File file : files) {
            //recursive option - not necessary
           // if (file.isDirectory()) {
            //    returnFiles.addAll(getFileList(file));
            //}
            if (file.getName().endsWith(".jpg")) {
                returnFiles.add(file);
            }
        }

        Log.i(TAG, "Files in /Pictures/ are: "+ returnFiles.toString());

        return returnFiles;
    }

    private Bitmap rotateBitmap(Bitmap source) {

        Matrix matrix = new Matrix();
        matrix.postRotate(90);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(),
                source.getHeight(), matrix, true);
    }

}
