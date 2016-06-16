package com.sbezgin.passwordskeeper.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.sbezgin.passwordskeeper.R;
import com.sbezgin.passwordskeeper.activity.main.MainActivityContext;
import com.sbezgin.passwordskeeper.activity.main.PathHolder;
import com.sbezgin.passwordskeeper.service.properties.PropertiesDataHolder;
import com.sbezgin.passwordskeeper.utils.RandomPass;

public class NewItemActivity extends AppCompatActivity {
    @Override
    protected void onResume() {
        super.onResume();
        fillGroupAndName();
    }

    private MainActivityContext mainActivityContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_item);
        mainActivityContext = MainActivityContext.getInstance();
        final EditText valueEdit = (EditText) findViewById(R.id.valueEditField);
        assert valueEdit != null;
        valueEdit.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                valueEdit.setText(RandomPass.generatePass());
                return false;
            }
        });

        fillGroupAndName();
    }

    private void fillGroupAndName() {
        PathHolder pathHolder = mainActivityContext.getPathHolder();
        EditText groupEdit = (EditText) findViewById(R.id.groupEditField);
        EditText nameEdit = (EditText) findViewById(R.id.nameEditField);
        groupEdit.setText(pathHolder.getGroup());
        nameEdit.setText(pathHolder.getName());
    }

    public void saveNewItem(View view) {
        EditText groupEdit = (EditText) findViewById(R.id.groupEditField);
        EditText nameEdit = (EditText) findViewById(R.id.nameEditField);
        EditText keyEdit = (EditText) findViewById(R.id.keyEditField);
        EditText valueEdit = (EditText) findViewById(R.id.valueEditField);

        PropertiesDataHolder dataHolder = mainActivityContext.getPropertiesDataHolder();
        String groupStr = groupEdit.getText().toString();
        String nameStr = nameEdit.getText().toString();
        String keyStr = keyEdit.getText().toString();
        String valueStr = valueEdit.getText().toString();
        if (!paramsEmpty(groupStr, nameStr, keyStr, valueStr)) {
            dataHolder.addNewProperty(
                    groupStr,
                    nameStr,
                    keyStr,
                    valueStr
            );
            mainActivityContext.setFlagReturnMain();
            finish();
        } else {
            Toast toast = Toast.makeText(
                    getApplicationContext(),
                    "Fields shouldn't be empty",
                    Toast.LENGTH_SHORT
            );
            toast.show();
        }
    }

    private boolean paramsEmpty(String groupStr, String nameStr, String keyStr, String valueStr) {
        return isEmpty(groupStr) || isEmpty(nameStr) || isEmpty(keyStr) || isEmpty(valueStr);
    }

    private boolean isEmpty(String str){
        return str == null || str.trim().equals("");
    }
}
