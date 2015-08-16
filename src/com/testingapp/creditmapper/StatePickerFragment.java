package com.testingapp.creditmapper;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.Spinner;

public class StatePickerFragment extends DialogFragment {
	
	public static final String EXTRA_STATE = "EXTRA_STATE";
	public static final String EMPTY_STRING = "";
	
	private String mState;
	private Spinner mSpinner;
	
	//Communicate result of dialog back to fragment
	private void sendResult(int resultCode){
		if(getTargetFragment() == null) return;
		
		Intent i = new Intent();
		i.putExtra(EXTRA_STATE, mState);
		
		getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, i);
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState){
		
		View v = getActivity().getLayoutInflater().inflate(R.layout.dialog_state, null);
		
		mSpinner = (Spinner) v.findViewById(R.id.searchState_text);
		
		
		return new AlertDialog.Builder(getActivity())
			.setView(v)
			.setTitle(R.string.state)
			.setPositiveButton(android.R.string.ok, 
					new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							StatePickerFragment.this.mState = (String) mSpinner.getSelectedItem();
							sendResult(Activity.RESULT_OK);
						}
					} )
			.create();
	}

}
