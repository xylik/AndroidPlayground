package fer.com.androidplayground.keyValueBackup;

import android.app.backup.BackupManager;
import android.app.backup.RestoreObserver;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.UUID;

import butterknife.ButterKnife;
import butterknife.OnClick;
import fer.com.androidplayground.R;

public class KeyValueBackupActivity extends AppCompatActivity {
    public static final String TAG = "KeyValueBackup";
    public static final String TEXT_FILE_NAME = "data.txt";
    public static final String TEXT_FILE_CONTENT = "{'userId':1, 'name':'Igor Armus'}";

    private SharedPrefs prefs;
    private BackupManager backupManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_key_value_backup);
        ButterKnife.bind(this);

        prefs = SharedPrefs.instance(this);
        backupManager = new BackupManager(this);
    }

    @OnClick(R.id.requestBackupBTN)
    void onBackupBtnClick() {
        requestBackup();
    }

    @OnClick(R.id.requestRestoreBTN)
    void onRestoreBtnClick() {
        requestBackupRestore();
    }

    @OnClick(R.id.checkUserIdBTN)
    void onCheckUserIdClick() {
        showUserIdPopup();
    }

    @OnClick(R.id.generateUserIdBTN)
    void onGenerateUserIdClick() {
        saveAndGenerateUserId();
    }

    @OnClick(R.id.checkTextFileBTN)
    void onCheckTextFileClick() {
        showTextFileContent();
    }

    @OnClick(R.id.generateTextFileBTN)
    void onGenerateTextFileClick() {
        writeTextFileContent();
    }

    private void requestBackup() {
        backupManager.dataChanged();
    }

    private void requestBackupRestore() {
        backupManager.requestRestore(new RestoreObserver() {
            @Override
            public void restoreStarting(int numPackages) {
                super.restoreStarting(numPackages);
                Log.d(TAG, "restoreStarting()");
            }

            @Override
            public void onUpdate(int nowBeingRestored, String currentPackage) {
                super.onUpdate(nowBeingRestored, currentPackage);
                Log.d(TAG, "onUpdate()");
            }

            @Override
            public void restoreFinished(int error) {
                super.restoreFinished(error);
                Log.d(TAG, "restoreFinished()");
            }
        });
    }

    private void showUserIdPopup() {
        String msg = "UserId:'" + prefs.getUserId() + "'";
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    private void saveAndGenerateUserId() {
        prefs.setUserId(UUID.randomUUID().toString());
    }

    private void showTextFileContent() {
        String content = "No content!";
        File textFile = new File(getFilesDir(), TEXT_FILE_NAME);
        if(textFile.exists()) {
            String line;
            StringBuilder sb = new StringBuilder();
            BufferedReader br = null;
            try {
                br = new BufferedReader(new InputStreamReader(new FileInputStream(textFile), "utf8"), 8192);
                while((line = br.readLine()) != null) {
                    sb.append(line);
                }
                content = sb.toString();
            } catch (Exception e) {
                e.printStackTrace();
                content = "Can't open file!";
            } finally {
                if(br != null){
                    try {
                        br.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        Toast.makeText(this, content, Toast.LENGTH_SHORT).show();
    }

    private void writeTextFileContent() {
        File outFile = new File(getFilesDir(), TEXT_FILE_NAME);
        if(outFile.exists()) return;

        BufferedWriter bw = null;
        try {
            outFile.createNewFile();
            bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFile), "utf8"), 8192);
            bw.write(TEXT_FILE_CONTENT);
            bw.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(bw != null){
                try {
                    bw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
