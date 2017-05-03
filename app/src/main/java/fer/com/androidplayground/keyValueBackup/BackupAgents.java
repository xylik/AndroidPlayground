package fer.com.androidplayground.keyValueBackup;

import android.app.backup.BackupAgent;
import android.app.backup.BackupAgentHelper;
import android.app.backup.BackupDataInput;
import android.app.backup.BackupDataOutput;
import android.app.backup.FileBackupHelper;
import android.app.backup.SharedPreferencesBackupHelper;
import android.os.ParcelFileDescriptor;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import fer.com.androidplayground.App;

/**
 * Created by Igor on 24/04/17.
 */

public class BackupAgents {
    // A key to uniquely identify the set of backup data
    static final String PREFS_BACKUP_KEY = "prefsHelper";
    static final String FILE_BACKUP_KEY = "fileBackupHelper";

    public static class SimpleHybridBackupAgent extends BackupAgentHelper{
        @Override
        public void onCreate() {
            SharedPreferencesBackupHelper sharedPrefsHelper = new SharedPreferencesBackupHelper(this, SharedPrefs.FILE_NAME);
            FileBackupHelper fileBackupHelper = new FileBackupHelper(this, KeyValueBackupActivity.TEXT_FILE_NAME);
            addHelper(PREFS_BACKUP_KEY, sharedPrefsHelper);
            addHelper(FILE_BACKUP_KEY, fileBackupHelper);
        }

        @Override
        public void onBackup(ParcelFileDescriptor oldState, BackupDataOutput data, ParcelFileDescriptor newState) throws IOException {
            super.onBackup(oldState, data, newState);
            Log.d(KeyValueBackupActivity.TAG, "onBackup()");
        }

        @Override
        public void onRestore(BackupDataInput data, int appVersionCode, ParcelFileDescriptor newState) throws IOException {
            super.onRestore(data, appVersionCode, newState);
            Log.d(KeyValueBackupActivity.TAG, "onRestore()");
        }
    }

    public static class ValueBackupAgent extends BackupAgent {
        public static final String USER_ID_BACKUP_KEY = "fer.com.androidplayground.USER_ID_BACKUP_KEY";
        private SharedPrefs prefs;

        public ValueBackupAgent() {
            prefs = SharedPrefs.instance(App.getGlobalContext());
        }

        @Override
        public void onBackup(ParcelFileDescriptor oldState, BackupDataOutput data, ParcelFileDescriptor newState) throws IOException {
            Log.d(KeyValueBackupActivity.TAG, "ValueBackupAgent onBackup()");

            try {
                //read old state value
                long entityModifiedOldState = readEntitiyModifiedOldState(oldState);

                //backup if necessary
                if(entityModifiedOldState != prefs.getUserIdModified()) backupData(data);

                //write new state value
                writeEntityModifiedNewState(newState);

                return;
            }catch (IOException e) {
                //unable to read state file... be safe and do a backup
                backupData(data);
                writeEntityModifiedNewState(newState);
            }
        }

        private long readEntitiyModifiedOldState(ParcelFileDescriptor oldState) throws IOException {
            FileInputStream fis = new FileInputStream(oldState.getFileDescriptor());
            DataInputStream dis = new DataInputStream(fis);
            return dis.readLong();
        }

        private void backupData(BackupDataOutput data) {
            // Send the data to the Backup Manager via the BackupDataOutput
            ByteArrayOutputStream bufStream = new ByteArrayOutputStream();
            DataOutputStream outWriter = new DataOutputStream(bufStream);

            try {
                outWriter.writeUTF(prefs.getUserId());
                outWriter.writeLong(prefs.getUserIdModified());

                byte[] buffer = bufStream.toByteArray();
                int len = buffer.length;
                data.writeEntityHeader(USER_ID_BACKUP_KEY, len);
                data.writeEntityData(buffer, len);
            }catch(IOException e) {
                return;
            }
        }

        private void writeEntityModifiedNewState(ParcelFileDescriptor newState) throws IOException {
            FileOutputStream outStream = new FileOutputStream(newState.getFileDescriptor());
            DataOutputStream out = new DataOutputStream(outStream);
            out.writeLong(prefs.getUserIdModified());
        }

        @Override
        public void onRestore(BackupDataInput data, int appVersionCode, ParcelFileDescriptor newState) throws IOException {
            Log.d(KeyValueBackupActivity.TAG, "ValueBackupAgent onRestore()");
            while(data.readNextHeader()) {
                String key = data.getKey();
                int dataSize = data.getDataSize();

                if(key.equals(USER_ID_BACKUP_KEY)) {
                    //read backup data
                    byte[] dataBuf = new byte[dataSize];
                    data.readEntityData(dataBuf, 0, dataSize);
                    ByteArrayInputStream baStream = new ByteArrayInputStream(dataBuf);
                    DataInputStream in = new DataInputStream(baStream);
                    String userId = in.readUTF();
                    long userModified = in.readLong();

                    restoreBackup(userId, userModified);
                } else {
                    data.skipEntityData();
                }
            }

            // Finally, write to the state blob(newState) that describes the restored data
            FileOutputStream outstream = new FileOutputStream(newState.getFileDescriptor());
            DataOutputStream out = new DataOutputStream(outstream);
            out.writeLong(prefs.getUserIdModified()); //new timestamp
        }

        private void restoreBackup(String userId, long userModified) {
            prefs.setUserId(userId);
        }
    }
}
