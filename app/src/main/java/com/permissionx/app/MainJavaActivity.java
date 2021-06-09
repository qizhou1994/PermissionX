package com.permissionx.app;

import android.Manifest;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.permissionx.app.databinding.ActivityMainJavaBinding;
import com.permissionx.qizhou1994.PermissionX;
import com.permissionx.qizhou1994.callback.ExplainReasonCallbackWithBeforeParam;
import com.permissionx.qizhou1994.callback.ForwardToSettingsCallback;
import com.permissionx.qizhou1994.callback.RequestCallback;
import com.permissionx.qizhou1994.request.ExplainScope;
import com.permissionx.qizhou1994.request.ForwardScope;

import java.util.List;

public class MainJavaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainJavaBinding binding = ActivityMainJavaBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.makeRequestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PermissionX.init(MainJavaActivity.this)
                        .permissions(Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                        .explainReasonBeforeRequest()
                        .onExplainRequestReason(new ExplainReasonCallbackWithBeforeParam() {
                            @Override
                            public void onExplainReason(final ExplainScope scope, final List<String> deniedList, boolean beforeRequest) {
//                                CustomDialog customDialog = new CustomDialog(MainJavaActivity.this, "PermissionX needs following permissions to continue", deniedList);
//                                scope.showRequestReasonDialog(customDialog);
                                scope.showRequestReasonDialog(deniedList, "PermissionX needs following permissions to continue", "Allow", "cas", new DialogInterface.OnCancelListener() {
                                    @Override
                                    public void onCancel(DialogInterface dialog) {
                                        Log.e("aaa","aaaa");
                                        scope.showRequestSettingDialog(deniedList, "PermissionX needs following permissions to continue", "Allow", "cas",null);
                                    }
                                });
                            }
                        })
                        .onForwardToSettings(new ForwardToSettingsCallback() {
                            @Override
                            public void onForwardToSettings(ForwardScope scope, List<String> deniedList) {
                                scope.showForwardToSettingsDialog(deniedList, "Please allow following permissions in settings", "Allow");
                            }
                        })
                        .request(new RequestCallback() {
                            @Override
                            public void onResult(boolean allGranted, List<String> grantedList, List<String> deniedList) {
                                if (allGranted) {
                                    Toast.makeText(MainJavaActivity.this, "All permissions are granted", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(MainJavaActivity.this, "The following permissions are denied：" + deniedList, Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }
}