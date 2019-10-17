package uk.ac.brookes.alexrussell.pepperdashboard;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.widget.LinearLayout;
import com.aigestudio.wheelpicker.WheelPicker;

import java.util.ArrayList;

//Creates the wheel selection tool for the number of hours to be displayed
public class NoticeDialogFragment extends DialogFragment {

    private WheelPicker wheelPicker;

    /**
     * The activity that creates an instance of this dialog fragment must
     * implement this interface in order to receive event callbacks.
     * Each method passes the DialogFragment in case the host needs to query it.
     */
    public interface NoticeDialogListener {
        void onDialogPositiveClick(DialogFragment dialog, int selectedNumber);
        void onDialogNegativeClick(DialogFragment dialog);
    }

    // Use this instance of the interface to deliver action events
    NoticeDialogListener mListener;

    // Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (NoticeDialogListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement NoticeDialogListener");
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        ArrayList<String> options = new ArrayList<>();
        for (int i = 0; i < 24; i++) {
            options.add(Integer.toString(i + 1));
        }
        wheelPicker = new WheelPicker(getActivity());
        wheelPicker.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        wheelPicker.setData(options);
        wheelPicker.setSelectedItemTextColor(Color.BLUE);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Time Range (Hours)")
                .setView(wheelPicker);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                int selectedNumber = -1;
                int position = wheelPicker.getCurrentItemPosition();
                String selectedItem = (String) wheelPicker.getData().get(position);
                selectedNumber =  Integer.valueOf(selectedItem);
                mListener.onDialogPositiveClick(NoticeDialogFragment.this, selectedNumber);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                mListener.onDialogNegativeClick(NoticeDialogFragment.this);
            }
        });
        return builder.create();

    }
}


