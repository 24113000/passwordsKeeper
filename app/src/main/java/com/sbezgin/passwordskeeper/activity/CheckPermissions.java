package com.sbezgin.passwordskeeper.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.sbezgin.passwordskeeper.R;
import com.sbezgin.passwordskeeper.db.DBHelper;

public class CheckPermissions extends AppCompatActivity {

    private static final int WRITING_GRANTED = 33;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_permissions);
    }

    @Override
    protected void onResume() {
        super.onResume();
        validateAppParams();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
            finish();
            System.exit(0);
        }

        validateAppParams();
    }

    private void validateAppParams() {
        int resultPermission = ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        );
        if (resultPermission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    WRITING_GRANTED
            );
        } else {
            DBHelper dbHelper = new DBHelper(this);
            String path2File = dbHelper.getPath();
            if (path2File == null || path2File.trim().equals("")) {
                Intent intent = new Intent(this, Settings.class);
                this.startActivity(intent);
            } else {
                Intent intent = new Intent(this, PasswordApply.class);
                this.startActivity(intent);
                finish();
            }
        }
    }
}
