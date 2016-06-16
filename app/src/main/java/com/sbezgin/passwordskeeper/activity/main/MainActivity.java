package com.sbezgin.passwordskeeper.activity.main;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.sbezgin.passwordskeeper.R;
import com.sbezgin.passwordskeeper.activity.NewItemActivity;
import com.sbezgin.passwordskeeper.activity.PasswordApply;
import com.sbezgin.passwordskeeper.activity.Settings;
import com.sbezgin.passwordskeeper.activity.main.adapter.SimpleListAdapter;
import com.sbezgin.passwordskeeper.activity.main.adapter.TwoItemArrayAdapter;
import com.sbezgin.passwordskeeper.db.DBHelper;
import com.sbezgin.passwordskeeper.service.PasswordHolder;
import com.sbezgin.passwordskeeper.service.file.FileService;
import com.sbezgin.passwordskeeper.service.file.impl.FileServiceImpl;
import com.sbezgin.passwordskeeper.service.impl.PasswordHolderImpl;
import com.sbezgin.passwordskeeper.service.properties.PropertiesDataHolder;
import com.sbezgin.passwordskeeper.service.properties.PropertyDTO;
import com.sbezgin.passwordskeeper.service.properties.PropertyService;
import com.sbezgin.passwordskeeper.service.properties.impl.PropertyServiceImpl;
import com.sbezgin.passwordskeeper.service.security.SecurityProvider;
import com.sbezgin.passwordskeeper.service.security.impl.SecurityProviderImpl;

import java.io.ByteArrayInputStream;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private static final int LAST_LEVEL = 2;
    private static final int MIDDLE_LEVEL = 1;
    public static final int FIRST_LEVEl = 0;
    public static final String SUPER_HIDDEN_GROUP = "Super hidden";

    private PathHolder pathHolder = new PathHolder();
    private MainActivityContext mainActivityContext;
    private DBHelper dbHelper = new DBHelper(this);
    private String currentPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainActivityContext = MainActivityContext.getInstance();
        ListView listView = (ListView) findViewById(R.id.itemList);
        assert listView != null;
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Object item = parent.getAdapter().getItem(position);
                if (!item.getClass().getName().equals(PropertyDTO.class.getName())) {
                    PathHolder pathHolder = MainActivity.this.pathHolder;

                    if (pathHolder.getLevel() == FIRST_LEVEl) {
                        pathHolder.setGroup(item.toString());
                    }

                    if (pathHolder.getLevel() == MIDDLE_LEVEL) {
                        pathHolder.setName(item.toString());
                    }
                    pathHolder.setNextLevel();
                    displayList(mainActivityContext.getPropertiesDataHolder());
                    updatePath();
                }
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final Object item = parent.getAdapter().getItem(position);
                String msg = item.toString();
                if (item.getClass().equals(PropertyDTO.class)) {
                    PropertyDTO dto = (PropertyDTO) item;
                    msg = "'" + dto.getKey() + "' '" + dto.getName() + "' ";
                }

                AlertDialog.Builder deleteDialog = new AlertDialog.Builder(MainActivity.this);
                deleteDialog.setMessage("Are you sure to delete this item '" + msg + "' ?");
                deleteDialog.setTitle("Are you sure to delete this item '" + msg + "' ?");
                deleteDialog.setCancelable(false);

                deleteDialog.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        PropertiesDataHolder dataHolder = mainActivityContext.getPropertiesDataHolder();

                        if (item.getClass().equals(PropertyDTO.class)) {
                            dataHolder.removeProperty(getRealGroupName(pathHolder.getGroup()), pathHolder.getName(), (PropertyDTO) item);
                        } else if (pathHolder.getLevel() == MIDDLE_LEVEL) {
                            dataHolder.removeName(getRealGroupName(pathHolder.getGroup()), item.toString());
                        } else if (pathHolder.getLevel() == FIRST_LEVEl) {
                            String groupName = item.toString();
                            dataHolder.removeGroup(getRealGroupName(groupName));
                        }
                        displayList(mainActivityContext.getPropertiesDataHolder());
                        dialog.dismiss();
                    }
                });

                deleteDialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                deleteDialog.show();

                return true;
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (pathHolder.getLevel() == FIRST_LEVEl) {
            super.onBackPressed();
        } else {
            if (pathHolder.getLevel() == LAST_LEVEL) {
                pathHolder.setName(null);
            }

            if (pathHolder.getLevel() == MIDDLE_LEVEL) {
                pathHolder.setGroup(null);
            }
            pathHolder.setPreviousLevel();
            displayList(getPropertiesDataHolder());
        }
        updatePath();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.new_item:
                showAddItemActivity();
                return true;
            case R.id.settings_item:
                showSettings();
                return true;
            case R.id.save_data:
                mainActivityContext.setCurrentState(MainState.SAVE_DATA_HOLDER);
                Intent intent = new Intent(this, PasswordApply.class);
                startActivityForResult(intent, 0);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mainActivityContext.isFlagReturnMain()) {
            mainActivityContext.resetFlagReturnMain();
            PropertiesDataHolder dataHolder;
            if (mainActivityContext.isDataValid()) {
                dataHolder = mainActivityContext.getPropertiesDataHolder();
                switch (mainActivityContext.getCurrentState()) {
                    case SAVE_DATA_HOLDER: {
                        saveDataHolder(dataHolder);
                        dataHolder = getPropertiesDataHolder();
                        break;
                    }
                }
            } else {
                dataHolder = getPropertiesDataHolder();
                mainActivityContext.setPropertiesDataHolder(dataHolder);
            }

            displayList(dataHolder);
            updatePath();
        } else {
            Intent openMainActivity= new Intent(MainActivity.this, PasswordApply.class);
            openMainActivity.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(openMainActivity);
        }
    }

    private void updatePath() {
        TextView pathView = (TextView) findViewById(R.id.pathView);
        StringBuilder path = new StringBuilder();
        if (pathHolder.getGroup() != null) {
            path.append("/").append(pathHolder.getGroup()).append("/");
        }

        if (pathHolder.getName() != null) {
            path.append(pathHolder.getName()).append("/");
        }

        pathView.setText(path.toString());
    }

    private void displayList(PropertiesDataHolder dataHolder) {
        ListAdapter adapter = buildAdapter(dataHolder);
        ListView listView = (ListView) findViewById(R.id.itemList);
        listView.setAdapter(adapter);
    }

    private ListAdapter buildAdapter(PropertiesDataHolder dataHolder) {
        if (pathHolder.getLevel() == LAST_LEVEL) {
            String group = getRealGroupName(pathHolder.getGroup());
            String name = pathHolder.getName();
            Set<PropertyDTO> propertyDTOs = dataHolder.getProperties(group, name);
            return new TwoItemArrayAdapter(this, propertyDTOs.toArray(new Object[propertyDTOs.size()]), pathHolder.getGroup());
        } else if (pathHolder.getLevel() == MIDDLE_LEVEL) {
            String groupName = getRealGroupName(pathHolder.getGroup());
            Set<String> names = dataHolder.getNames(groupName);
            return new SimpleListAdapter(this, names.toArray(new String[names.size()]), pathHolder.getGroup());
        }

        //0 level
        Set<String> groups = dataHolder.getGroups();
        if (groups.contains(PropertiesDataHolder.SECRET_PROPERTIES)) {
            groups.remove(PropertiesDataHolder.SECRET_PROPERTIES);
            groups.add(SUPER_HIDDEN_GROUP);
        }
        return new SimpleListAdapter(this, groups.toArray(new String[groups.size()]), pathHolder.getGroup());
    }

    private String getRealGroupName(String group) {
        if (group.equals(SUPER_HIDDEN_GROUP)) {
            return PropertiesDataHolder.SECRET_PROPERTIES;
        }
        return group;
    }

    private void saveDataHolder(PropertiesDataHolder dataHolder) {
        PropertyService propertyService = PropertyServiceImpl.getInstance();
        String convert2text = propertyService.convert2text(dataHolder);

        SecurityProvider securityProvider = new SecurityProviderImpl();
        try {
            PasswordHolder passwordHolder = PasswordHolderImpl.getInstance();
            String password = passwordHolder.getPassword();
            if (currentPassword.equals(password)) {
                byte[] bytes = securityProvider.encryptData(convert2text.getBytes("UTF-8"), password);
                FileService fileService = new FileServiceImpl(dbHelper.getPath());
                fileService.saveDate(bytes);
                mainActivityContext.setCurrentState(MainState.READ_DATA_HOLDER);
                mainActivityContext.setPropertiesDataHolder(getPropertiesDataHolder());

                Toast toast = Toast.makeText(
                        getApplicationContext(),
                        "Data is saved",
                        Toast.LENGTH_SHORT
                );
                toast.show();
            } else {
                Intent openMainActivity= new Intent(MainActivity.this, PasswordApply.class);
                openMainActivity.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(openMainActivity);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @NonNull
    private PropertiesDataHolder getPropertiesDataHolder() {
        String folder = dbHelper.getPath();
        FileService fileService = new FileServiceImpl(folder);

        PropertiesDataHolder dataHolder;

        if (fileService.isFileExists()) {
            try {
                PasswordHolder passwordHolder = PasswordHolderImpl.getInstance();

                byte[] currentFileBytes = fileService.getCurrentFileBytes();
                SecurityProvider securityProvider = new SecurityProviderImpl();
                byte[] decryptData = new byte[0];
                try {
                    String password = passwordHolder.getPassword();
                    decryptData = securityProvider.decryptData(currentFileBytes, password);
                    this.currentPassword = password;
                } catch (Exception e) {
                    AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                    alertDialog.setTitle("Decryption is failed");
                    alertDialog.setMessage("Decryption is failed");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    Intent openMainActivity= new Intent(MainActivity.this, PasswordApply.class);
                                    openMainActivity.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                                    startActivity(openMainActivity);
                                }
                            });
                    alertDialog.show();
                }

                PropertyService propertyService = PropertyServiceImpl.getInstance();
                dataHolder = propertyService.readProperties(new ByteArrayInputStream(decryptData));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else {
            dataHolder = new PropertiesDataHolder();
        }

        return dataHolder;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    private void showSettings() {
        Intent intent = new Intent(this, Settings.class);
        this.startActivity(intent);
    }

    private void showAddItemActivity() {
        Intent intent = new Intent(this, NewItemActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        this.startActivity(intent);
    }
}
