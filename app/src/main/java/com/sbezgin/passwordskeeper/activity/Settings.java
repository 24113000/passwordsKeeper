package com.sbezgin.passwordskeeper.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.sbezgin.passwordskeeper.R;
import com.sbezgin.passwordskeeper.activity.main.MainActivityContext;
import com.sbezgin.passwordskeeper.db.DBHelper;
import com.sbezgin.passwordskeeper.utils.SelectFolderDialog;

public class Settings extends AppCompatActivity {
    private String savedPath;
    private DBHelper dbHelper;

    @Override
    public void onBackPressed() {
        String currPath = dbHelper.getPath();
        if (!savedPath.equals(currPath)) {
            Intent openMainActivity= new Intent(this, PasswordApply.class);
            openMainActivity.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            MainActivityContext.getInstance().setFlagReturnMain();
            startActivity(openMainActivity);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        dbHelper = new DBHelper(Settings.this);
        String path = dbHelper.getPath();
        savedPath = path;
        setValue2Label(path);
    }


    public void selectFolder(View view) {
        SelectFolderDialog openFileDialog = new SelectFolderDialog(this);
        openFileDialog.setOpenDialogListener(new SelectFolderDialog.OpenDialogListener() {
            @Override
            public void OnSelectedFile(String path) {
                DBHelper dbHelper = new DBHelper(Settings.this);
                dbHelper.setPath(path);
                setValue2Label(path);
            }
        });
        openFileDialog.show();
    }

    private void setValue2Label(String path) {
        TextView path2File = (TextView) findViewById(R.id.path2File);
        path2File.setText(path);
    }

}
