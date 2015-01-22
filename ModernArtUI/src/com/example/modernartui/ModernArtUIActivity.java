package com.example.modernartui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;


public class ModernArtUIActivity extends Activity {

	private static final String TAG = "ModernArtUIActivity";
	private static final String URL = "http://www.moma.org";
	
	View mView1;
	View mView2;
	View mView3;
	View mView4;
	View mView5;
	
	int mStart1 = 0;
	int mStart2 = 30;
	int mStart3 = 60;
	int mStart5 = 90;
	
	private DialogFragment mDialog;
	
	
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modern_art_ui);
        
        SeekBar seekbar = (SeekBar) findViewById(R.id.seek_bar);
        mView1 = (View) findViewById(R.id.view1);
        mView2 = (View) findViewById(R.id.view2);
        mView3 = (View) findViewById(R.id.view3);
        mView4 = (View) findViewById(R.id.view4);
        mView5 = (View) findViewById(R.id.view5);
        
        setStartingBackgroundColor();
        
        
        
        seekbar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				// TODO Auto-generated method stub
				
				Log.i(TAG, "SeekBar Progress Changed");
				
				changeViewColors(progress / 2);
				
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}
        	
        });
        
        
        
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.modern_art_ui, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.more_info) {
            
        	showView();
        	
        	return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    private void showView() {
		
    	mDialog = AlertDialogFragment.newInstance();
    	
    	mDialog.show(getFragmentManager(), "Alert");
    	
	}


	public void changeViewColors(int progress) {
    	
    	Log.i(TAG, "changing view colors");
    	
    	try {
    		mView1.setBackgroundColor(Color.HSVToColor(getHSVColor(progress+mStart1)));
    		mView2.setBackgroundColor(Color.HSVToColor(getHSVColor(progress+mStart2)));
    		mView3.setBackgroundColor(Color.HSVToColor(getHSVColor(progress+mStart3)));
    		mView5.setBackgroundColor(Color.HSVToColor(getHSVColor(progress+mStart5)));
    		
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    	
    }
    
    public void setStartingBackgroundColor() {
    	
    	mView1.setBackgroundColor(Color.HSVToColor(getHSVColor(mStart1)));
    	mView2.setBackgroundColor(Color.HSVToColor(getHSVColor(mStart2)));
    	mView3.setBackgroundColor(Color.HSVToColor(getHSVColor(mStart3)));
    	mView5.setBackgroundColor(Color.HSVToColor(getHSVColor(mStart5)));
    	
    	
    	
    }
    
    public float[] getHSVColor(int progress) {
    	
    	
    	float[] result = {0 , 1, 1};
    	
    	result[0] = 360f * (progress) / 150;
    	
    	return result;
    }
    
    public static class AlertDialogFragment extends DialogFragment {
    	

		public static AlertDialogFragment newInstance()	{
    		return new AlertDialogFragment();
    	}
		
    	@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			
			return new AlertDialog.Builder(getActivity())
				.setMessage("Inspired by the works of artists " +
						"such as Piet Mondrian and Ben Nicholson." +
						"\n\nClick to learn more!")
						
						//cannot dismiss message with back button
						.setCancelable(false)
						
						//set up the "no" button
						.setNegativeButton("Not Now", new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								((ModernArtUIActivity) getActivity()).dismissDialog();
								
							}
						} )
						
						// set up "yes" option
						.setPositiveButton("Visit MOMA", new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								((ModernArtUIActivity) getActivity()).openWebsite();
								
							}
						}).create();
		}
    	
    	
    	
    }

    // dismisses the alert dialog window
	protected void dismissDialog() {
		// TODO Auto-generated method stub
		mDialog.dismiss();
		
	}


	// opens MOMA.org website via web browser app
	protected void openWebsite() {
		
		Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(URL));
		startActivity(intent);
		
	}
}


