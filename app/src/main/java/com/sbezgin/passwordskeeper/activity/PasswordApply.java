package com.sbezgin.passwordskeeper.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;

import com.sbezgin.passwordskeeper.R;
import com.sbezgin.passwordskeeper.activity.main.MainActivity;
import com.sbezgin.passwordskeeper.activity.main.MainActivityContext;
import com.sbezgin.passwordskeeper.db.DBHelper;
import com.sbezgin.passwordskeeper.service.PasswordHolder;
import com.sbezgin.passwordskeeper.service.impl.PasswordHolderImpl;

public class PasswordApply extends AppCompatActivity {

    private MainActivityContext mainActivityContext;
    //TODO back button should close app !!!
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_apply);
        mainActivityContext = MainActivityContext.getInstance();
    }

    @Override
    protected void onResume() {
        super.onResume();
        EditText passwordInput = (EditText) findViewById(R.id.passwordInput);
        passwordInput.setText("");
    }

    public void onApplyPassClick(View view) {

        PasswordHolder passwordHolder = PasswordHolderImpl.getInstance();
        EditText passwordInput = (EditText) findViewById(R.id.passwordInput);
        Editable text = passwordInput.getText();
        passwordHolder.setPassword(text.toString());

        switch (mainActivityContext.getCurrentState()) {
            case READ_DATA_HOLDER:
            case DATA_NOT_SET:
                mainActivityContext.inValidate();
                Intent intent = new Intent(this, MainActivity.class);
                this.startActivity(intent);
                mainActivityContext.setFlagReturnMain();
                break;
            case SAVE_DATA_HOLDER:
                finish();
                mainActivityContext.setFlagReturnMain();
                break;
            default:
                throw new RuntimeException("Unknown current state: " + mainActivityContext.getCurrentState());
        }
    }

    public void cleanAll(View view) {
        DBHelper dbHelper = new DBHelper(this);
        dbHelper.cleanTable();
    }
}
