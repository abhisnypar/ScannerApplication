package pidugu.example.com.storagescanner;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Map;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pidugu.example.com.storagescanner.ServiceCallBackListener.ScannerCallbackListener;
import pidugu.example.com.storagescanner.fragment.ResultListFragment;
import pidugu.example.com.storagescanner.service.ScannerAsyncTaskLoader;

public class MainActivity extends AppCompatActivity implements ScannerCallbackListener {

    @BindView(R.id.progress_bar)
    ProgressBar mProgressBar;
    @BindView(R.id.start_scanning_text_view)
    TextView mStartScanText;
    @BindView(R.id.scan_image)
    ImageView mScanImageView;

    private ScannerAsyncTaskLoader asyncTaskLoader;
    private Set<Map.Entry<String, Long>> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);
        ButterKnife.bind(this);
        if (savedInstanceState != null && (savedInstanceState.getInt("Data") != 0)) {
            startScanning();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1) {
            startScanning();
        } else {
            Toast.makeText(getApplicationContext(), "Permission not granted", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        stopScanning();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt("Data",  data.size());
        super.onSaveInstanceState(outState);
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @OnClick(R.id.scan_image)
    public void startScan(final View view) {
        mStartScanText.setText(getString(R.string.scanning_in_progress));
        mProgressBar.setVisibility(View.VISIBLE);
        checkRequirements();
    }

    private void checkRequirements() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkPermissions();
        } else {
            startScanning();
        }
    }

    private void startScanning() {
        if ((isExternalStorageReadable() && isExternalStorageWritable())) {
            asyncTaskLoader = new ScannerAsyncTaskLoader(getApplicationContext(), getScannerInstance());
            asyncTaskLoader.startLoading();
        }
    }

    private void stopScanning() {
        if (asyncTaskLoader != null) {
            mProgressBar.setVisibility(View.INVISIBLE);
            mScanImageView.setVisibility(View.INVISIBLE);
            asyncTaskLoader.stopLoading();
        }
        mStartScanText.setText(getString(R.string.list_results));
        ResultListFragment resultListFragment = new ResultListFragment();
        resultListFragment.setData(data);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, resultListFragment)
                .commit();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void checkPermissions() {
        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE);
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        } else {
            startScanning();
        }
    }

    public boolean isExternalStorageWritable() {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }

    /* Checks if external storage is available to at least read */
    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state) || Environment.MEDIA_MOUNTED_READ_ONLY.equals(state);
    }

    @Override
    public void onSuccess(final Set<Map.Entry<String, Long>> data) {
        this.data = data;
        new Handler().postDelayed(
                new Runnable() {
                    @Override
                    public void run() {
                        stopScanning();
                        mStartScanText.setText(getString(R.string.scanning_success));
                    }
                }, 2000
        );
    }

    @Override
    public void onFailure() {
    }

    private ScannerCallbackListener getScannerInstance() {
        return this;
    }
}