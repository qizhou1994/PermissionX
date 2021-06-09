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

import android.content.DialogInterface;

import androidx.annotation.NonNull;

import com.permissionx.qizhou1994.dialog.RationaleDialog;
import com.permissionx.qizhou1994.dialog.RationaleDialogFragment;

import java.util.List;

/**
 * Provide specific scopes for {@link com.permissionx.qizhou1994.callback.ExplainReasonCallback} and {@link com.permissionx.qizhou1994.callback.ExplainReasonCallbackWithBeforeParam} to give it specific functions to call.
 * @author guolin
 * @since 2020/3/18
 */
public class ExplainScope {

    private PermissionBuilder pb;

    private ChainTask chainTask;

    ExplainScope(PermissionBuilder pb, ChainTask chainTask) {
        this.pb = pb;
        this.chainTask = chainTask;
    }

    /**
     * Show a rationale dialog to explain to user why you need these permissions.
     * @param permissions
     *          Permissions that to request.
     * @param message
     *          Message that show to user.
     * @param positiveText
     *          Text on the positive button. When user click, PermissionX will request permissions again.
     * @param negativeText
     *          Text on the negative button. When user click, PermissionX will finish request.
     */
    public void showRequestReasonDialog(List<String> permissions, String message, String positiveText, String negativeText) {
        pb.showHandlePermissionDialog(chainTask, true, permissions, message, positiveText, negativeText);
    }

    public void showRequestReasonDialog(List<String> permissions, String message, String positiveText, String negativeText, DialogInterface.OnCancelListener cancelListener) {
        pb.showHandlePermissionDialog(chainTask, true, permissions, message, positiveText, negativeText,cancelListener);
    }
    public void showRequestSettingDialog(List<String> permissions, String message, String positiveText, String negativeText, DialogInterface.OnCancelListener cancelListener) {
        pb.showHandlePermissionDialog(chainTask, false, permissions, message, positiveText, negativeText,cancelListener);
    }

    /**
     * Show a rationale dialog to explain to user why you need these permissions.
     * @param permissions
     *          Permissions that to request.
     * @param message
     *          Message that show to user.
     * @param positiveText
     *          Text on the positive button. When user click, PermissionX will request permissions again.
     */
    public void showRequestReasonDialog(List<String> permissions, String message, String positiveText) {
        showRequestReasonDialog(permissions, message, positiveText, null,null);
    }
    public void showRequestReasonDialog(List<String> permissions, String message, String positiveText,DialogInterface.OnCancelListener cancelListener) {
        showRequestReasonDialog(permissions, message, positiveText, null,cancelListener);
    }
    /**
     * Show a rationale dialog to explain to user why you need these permissions.
     * @param dialog
     *          Dialog to explain to user why these permissions are necessary.
     */
    public void showRequestReasonDialog(@NonNull RationaleDialog dialog) {
        pb.showHandlePermissionDialog(chainTask, true, dialog);
    }

    /**
     * Show a rationale dialog to explain to user why you need these permissions.
     * @param dialogFragment
     *          DialogFragment to explain to user why these permissions are necessary.
     */
    public void showRequestReasonDialog(@NonNull RationaleDialogFragment dialogFragment) {
        pb.showHandlePermissionDialog(chainTask, true, dialogFragment);
    }

}