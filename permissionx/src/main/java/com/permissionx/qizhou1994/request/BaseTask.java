/*
 * Copyright (C)  guolin, PermissionX Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.permissionx.qizhou1994.request;

import android.Manifest;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;

import com.permissionx.qizhou1994.PermissionX;

import java.util.ArrayList;
import java.util.List;

/**
 * Define a BaseTask to implement the duplicate logic codes. No need to implement them in every task.
 *
 * @author guolin
 * @since 2020/6/10
 */
abstract class BaseTask implements ChainTask {

    /**
     * Point to the next task. When this task finish will run next task. If there's no next task, the request process end.
     */
    protected ChainTask next;

    /**
     * Instance of PermissionBuilder.
     */
    protected PermissionBuilder pb;

    /**
     * Provide specific scopes for explainReasonCallback for specific functions to call.
     */
    ExplainScope explainReasonScope;

    /**
     * Provide specific scopes for forwardToSettingsCallback for specific functions to call.
     */
    ForwardScope forwardToSettingsScope;

    BaseTask(PermissionBuilder permissionBuilder) {
        pb = permissionBuilder;
        explainReasonScope = new ExplainScope(pb, this);
        forwardToSettingsScope = new ForwardScope(pb, this);
    }

    @Override
    public ExplainScope getExplainScope() {
        return explainReasonScope;
    }

    @Override
    public ForwardScope getForwardScope() {
        return forwardToSettingsScope;
    }

    @Override
    public void finish() {
        if (next != null) { // If there's next task, then run it.
            next.request();
        } else { // If there's no next task, finish the request process and notify the result
            List<String> deniedList = new ArrayList<>();
            deniedList.addAll(pb.deniedPermissions);
            deniedList.addAll(pb.permanentDeniedPermissions);
            deniedList.addAll(pb.permissionsWontRequest);
            if (pb.shouldRequestBackgroundLocationPermission()) {
                if (PermissionX.isGranted(pb.activity, RequestBackgroundLocationPermission.ACCESS_BACKGROUND_LOCATION)) {
                    pb.grantedPermissions.add(RequestBackgroundLocationPermission.ACCESS_BACKGROUND_LOCATION);
                } else {
                    deniedList.add(RequestBackgroundLocationPermission.ACCESS_BACKGROUND_LOCATION);
                }
            }
            if (pb.shouldRequestSystemAlertWindowPermission()
                    && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                    && pb.getTargetSdkVersion() >= Build.VERSION_CODES.M) {
                if (Settings.canDrawOverlays(pb.activity)) {
                    pb.grantedPermissions.add(Manifest.permission.SYSTEM_ALERT_WINDOW);
                } else {
                    deniedList.add(Manifest.permission.SYSTEM_ALERT_WINDOW);
                }
            }
            if (pb.shouldRequestWriteSettingsPermission()
                    && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                    && pb.getTargetSdkVersion() >= Build.VERSION_CODES.M) {
                if (Settings.System.canWrite(pb.activity)) {
                    pb.grantedPermissions.add(Manifest.permission.WRITE_SETTINGS);
                } else {
                    deniedList.add(Manifest.permission.WRITE_SETTINGS);
                }
            }
            if (pb.shouldRequestManageExternalStoragePermission()) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R &&
                        Environment.isExternalStorageManager()) {
                    pb.grantedPermissions.add(RequestManageExternalStoragePermission.MANAGE_EXTERNAL_STORAGE);
                } else {
                    deniedList.add(RequestManageExternalStoragePermission.MANAGE_EXTERNAL_STORAGE);
                }
            }
            if (pb.requestCallback != null) {
                pb.requestCallback.onResult(deniedList.isEmpty(), new ArrayList<>(pb.grantedPermissions), deniedList);
            }
        }
    }

}
