package com.tuchangwei.yora.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.tuchangwei.yora.R;

/**
 * Created by WuQiang on 12/23/16.
 */
public class ChangePasswordDialog extends BaseDialogFragment implements View.OnClickListener {

    private EditText currentPassword;
    private EditText newPassword;
    private EditText confirmNewPassword;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
                super.onCreateDialog(savedInstanceState);
        View dialogView = getActivity().getLayoutInflater().inflate(R.layout.dialog_change_password, null, false);

        currentPassword    = (EditText)dialogView.findViewById(R.id.dialog_change_password_currentPassword);
//        currentPassword    = (EditText)getActivity().findViewById(R.id.dialog_change_password_currentPassword);
        newPassword        = (EditText)getActivity().findViewById(R.id.dialog_change_password_newPassword);
        confirmNewPassword = (EditText)getActivity().findViewById(R.id.dialog_change_password_confirmNewPassword);

        if (!application.getAuth().getUser().isHasPassword())
            currentPassword.setVisibility(View.GONE);

        AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
                .setView(dialogView)
                .setPositiveButton("更新",null)
                .setNegativeButton("取消",null)
                .setTitle("修改密码")
                .show();
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(this);
        return alertDialog;

    }

    @Override
    public void onClick(View view) {
        // TODO: 1/31/16 send new password to server
        Toast.makeText(getActivity(),"Password Updated!",Toast.LENGTH_SHORT).show();
        dismiss();
    }
}
