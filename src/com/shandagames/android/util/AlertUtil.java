package com.shandagames.android.util;

import java.util.List;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnKeyListener;
import android.view.KeyEvent;
import com.shandagames.android.support.DiagnosticSupport;
import com.shandagames.android.support.IntentSupport;


public class AlertUtil {

	private static final String PROGRESS_DIALOG_TITLE_RESOURCE = "droidfu_progress_dialog_title";

	private static final String PROGRESS_DIALOG_MESSAGE_RESOURCE = "droidfu_progress_dialog_message";

	public static final String ERROR_DIALOG_TITLE_RESOURCE = "droidfu_error_dialog_title";
	
	 /**
     * Creates a new ProgressDialog
     * 
     * @param activity
     * @param progressDialogTitleId
     *            The resource id for the title. If this is less than or equal to 0, a default title
     *            is used.
     * @param progressDialogMsgId
     *            The resource id for the message.
     * @return The new dialog
     */
    public static ProgressDialog createProgressDialog(final Activity activity,
            int progressDialogTitleId, int progressDialogMsgId) {
        ProgressDialog progressDialog = new ProgressDialog(activity);
        if (progressDialogTitleId <= 0) {
            progressDialogTitleId = activity.getResources().getIdentifier(
                    PROGRESS_DIALOG_TITLE_RESOURCE, "string", activity.getPackageName());
        }
        progressDialog.setTitle(progressDialogTitleId);
        if (progressDialogMsgId <= 0) {
            progressDialogMsgId = activity.getResources().getIdentifier(
                    PROGRESS_DIALOG_MESSAGE_RESOURCE, "string", activity.getPackageName());
        }
        progressDialog.setMessage(activity.getString(progressDialogMsgId));
        progressDialog.setIndeterminate(true);
        progressDialog.setOnKeyListener(new OnKeyListener() {
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                activity.onKeyDown(keyCode, event);
                return false;
            }
        });
        // progressDialog.setInverseBackgroundForced(true);
        return progressDialog;
    }
	
	/**
     * Creates a new Yes/No AlertDialog
     * 
     * @param context
     * @param dialogTitle
     * @param screenMessage
     * @param iconResourceId
     * @param listener
     * @return
     */
    public static AlertDialog newYesNoDialog(final Context context, String dialogTitle,
            String screenMessage, int iconResourceId, OnClickListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(false);
        builder.setPositiveButton(android.R.string.yes, listener);
        builder.setNegativeButton(android.R.string.no, listener);

        builder.setTitle(dialogTitle);
        builder.setMessage(screenMessage);
        builder.setIcon(iconResourceId);

        return builder.create();
    }
    
    /**
     * Creates a new AlertDialog to display a simple message
     * 
     * @param context
     * @param dialogTitle
     * @param screenMessage
     * @param iconResourceId
     * @return
     */
    public static AlertDialog newMessageDialog(final Context context, String dialogTitle,
            String screenMessage, int iconResourceId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(false);
        builder.setPositiveButton("Okay", new OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.setTitle(dialogTitle);
        builder.setMessage(screenMessage);
        builder.setIcon(iconResourceId);

        return builder.create();
    }

    /**
     * Displays a error dialog with an exception as its body. Also displays a Send Email button to
     * send the exception to the developer. Implement the following resource IDs
     * droidfu_error_report_email_address - The email address the exception is sent to.
     * droidfu_error_report_email_subject - The subject of the email.
     * droidfu_dialog_button_send_error_report - The text on the Send Email button.
     * 
     * @param activity
     * @param dialogTitle
     * @param error
     * @return
     */
    public static AlertDialog newErrorHandlerDialog(final Activity activity, String dialogTitle,
            Exception error) {
        String screenMessage = error.getLocalizedMessage();

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(dialogTitle);
        builder.setMessage(screenMessage);
        builder.setIcon(android.R.drawable.ic_dialog_alert);
        builder.setCancelable(false);
        builder.setPositiveButton(activity.getString(android.R.string.ok), new OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        if (IntentSupport.isIntentAvailable(activity, Intent.ACTION_SEND,
                IntentSupport.MIME_TYPE_EMAIL)) {
            int buttonId = activity.getResources().getIdentifier(
                    "droidfu_dialog_button_send_error_report", "string", activity.getPackageName());
            String buttonText = activity.getString(buttonId);
            int bugEmailAddressId = activity.getResources().getIdentifier(
                    "droidfu_error_report_email_address", "string", activity.getPackageName());
            String bugReportEmailAddress = activity.getString(bugEmailAddressId);
            int bugEmailSubjectId = activity.getResources().getIdentifier(
                    "droidfu_error_report_email_subject", "string", activity.getPackageName());
            String bugReportEmailSubject = activity.getString(bugEmailSubjectId);
            final String diagnosis = DiagnosticSupport.createDiagnosis(activity, error);
            final Intent intent = IntentSupport.newEmailIntent(activity, bugReportEmailAddress,
                    bugReportEmailSubject, diagnosis);
            builder.setNegativeButton(buttonText, new OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    activity.startActivity(intent);
                }
            });
        }

        return builder.create();
    }
    
    /**
     * Creates a AlertDialog that shows a list of elements. The listener's onClick method gets
     * called when the user taps a list item.
     * 
     * @param <T>
     *            The type of each element
     * @param context
     * @param dialogTitle
     *            the title or null to disable the title
     * @param elements
     *            List of elements to be displayed. Each elements toString() method will be called.
     * @param listener
     *            The listener to handle the onClick events.
     * @param closeOnSelect
     *            If true the dialog closes as soon as one list item is selected, otherwise multiple
     *            onClick events may be sent.
     * @return The new dialog.
     */
    public static <T> Dialog newListDialog(final Activity context, String dialogTitle,
            final List<T> elements, final DialogClickListener<T> listener,
            final boolean closeOnSelect) {
        return newListDialog(context, dialogTitle, elements, listener, closeOnSelect, 0);
    }

    public static <T> Dialog newListDialog(final Activity context, String dialogTitle,
            final List<T> elements, final DialogClickListener<T> listener,
            final boolean closeOnSelect, int selectedItem) {
        final int entriesSize = elements.size();
        String[] entries = new String[entriesSize];
        for (int i = 0; i < entriesSize; i++) {
            entries[i] = elements.get(i).toString();
        }

        Builder builder = new AlertDialog.Builder(context);
        if (dialogTitle != null) {
            builder.setTitle(dialogTitle);
        }
        builder.setSingleChoiceItems(entries, selectedItem, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                if (closeOnSelect)
                    dialog.dismiss();
                listener.onClick(which, elements.get(which));
            }
        });

        return builder.create();
    }
    
    
    public static interface DialogClickListener<T> {

        void onClick(int index, T element);

    }
}
