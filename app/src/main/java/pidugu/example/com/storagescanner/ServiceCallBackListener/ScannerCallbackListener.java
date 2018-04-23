package pidugu.example.com.storagescanner.ServiceCallBackListener;


import java.util.Map;
import java.util.Set;

public interface ScannerCallbackListener {

    void onSuccess(final Set<Map.Entry<String, Long>> data);

    void onFailure();
}
