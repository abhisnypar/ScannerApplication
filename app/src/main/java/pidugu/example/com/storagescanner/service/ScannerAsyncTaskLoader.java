package pidugu.example.com.storagescanner.service;

import android.content.Context;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import pidugu.example.com.storagescanner.ServiceCallBackListener.ScannerCallbackListener;

public class ScannerAsyncTaskLoader extends AsyncTaskLoader<Set<Map.Entry<String, Long>>> {
    private ScannerCallbackListener scannerCallbackListener;

    public ScannerAsyncTaskLoader(@NonNull Context context, ScannerCallbackListener scannerInstance) {
        super(context);
        scannerCallbackListener = scannerInstance;
    }

    @Nullable
    @Override
    public Set<Map.Entry<String, Long>> loadInBackground() {
        return null;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        final FilesMapper filesMapper = new FilesMapper();
        deliverResult(filesMapper.sortFiles(listFilesForFolder()));
    }

    public void deliverResult(@Nullable Set<Map.Entry<String, Long>> data) {
        super.deliverResult(data);
        scannerCallbackListener.onSuccess(data);
    }

    private HashMap<String, Long> listFilesForFolder() {
        final String path = Environment.getExternalStorageDirectory().getPath() + "/";
//        Log.d("Files", "Path: " + path);
        final File directory = new File(path);
        final File[] files = directory.listFiles();
//        Log.d("Files", "Size: " + files.length);
        final HashMap<String, Long> allFiles = new HashMap<>();
        for (File file : files) {
            allFiles.put(file.getName(), file.length());
        }
        return allFiles;
    }
}
