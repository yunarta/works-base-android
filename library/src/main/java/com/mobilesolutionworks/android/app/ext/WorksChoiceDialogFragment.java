package com.mobilesolutionworks.android.app.ext;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;

import com.mobilesolutionworks.android.app.BiFunction;
import com.mobilesolutionworks.android.app.WorksConstants;
import com.mobilesolutionworks.android.app.WorksDialogFragment;
import com.mobilesolutionworks.android.bolts.BoltsWorksController3;

/**
 * Created by yunarta on 7/12/16.
 */

public class WorksChoiceDialogFragment<Data> extends WorksDialogFragment implements DialogInterface.OnClickListener {

    private AlertDialogFragmentController<Data> mController;

    public static <Data> WorksChoiceDialogFragment<Data> create(BuildInfo info) {
        Bundle args = new Bundle();
        args.putParcelable(":buildInfo", info);

        WorksChoiceDialogFragment<Data> fragment = new WorksChoiceDialogFragment<>();
        fragment.setArguments(args);

        return fragment;
    }

    protected transient Data mTransientData;

    protected transient BiFunction<Integer, Data> mTransientAction;

    public void setTransientData(Data data, BiFunction<Integer, Data> action) {
        mTransientData = data;
        mTransientAction = action;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mController = getControllerManager().initController(WorksConstants.WORKS_ID_DIALOG_CONTROLLER, null, new BoltsWorksController3.ControllerCallbacks<AlertDialogFragmentController<Data>, WorksDialogFragment>(this) {

            @Override
            public AlertDialogFragmentController<Data> onCreateController(int id, Bundle bundle) {
                AlertDialogFragmentController<Data> controller = new AlertDialogFragmentController<Data>(mTransientData, mTransientAction);
                mTransientData = null;
                mTransientAction = null;
                return controller;
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        Bundle args = getArguments();
        BuildInfo build = args.getParcelable(":buildInfo");
        if (build.mMessageRes != -1) {
            builder.setMessage(build.mUseHtml ? fromHtml(getString(build.mMessageRes)) : getString(build.mMessageRes));
        } else {
            builder.setMessage(build.mUseHtml ? fromHtml(build.mMessageText) : build.mMessageText);
        }

        if (build.mPositiveRes != -1) {
            builder.setPositiveButton(getString(build.mPositiveRes), this);
        } else if (!TextUtils.isEmpty(build.mPositiveText)) {
            builder.setPositiveButton(build.mPositiveText, this);
        }

        if (build.mNegativeRes != -1) {
            builder.setNegativeButton(getString(build.mNegativeRes), this);
        } else if (!TextUtils.isEmpty(build.mNegativeText)) {
            builder.setNegativeButton(build.mNegativeText, this);
        }

        return builder.create();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        mController.postResult(which);
    }

    @SuppressWarnings("deprecation")
    private static Spanned fromHtml(String source) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return Html.fromHtml(source, Html.FROM_HTML_MODE_LEGACY);
        } else {
            return Html.fromHtml(source);
        }
    }

    public static BuildInfo newBuilder() {
        return new BuildInfo();
    }

    public static BuildInfo newBuilder(int message, int positive, int negative) {
        BuildInfo info = new BuildInfo();
        info.mMessageRes = message;
        info.mPositiveRes = positive;
        info.mNegativeRes = negative;
        return info;
    }

    public static class BuildInfo implements Parcelable {

        protected boolean mUseHtml;

        protected int mMessageRes;

        protected int mNegativeRes;

        protected int mPositiveRes;

        protected String mMessageText;

        protected String mNegativeText;

        protected String mPositiveText;

        public BuildInfo() {
        }

        public BuildInfo useHtml() {
            mUseHtml = true;
            return this;
        }

        public BuildInfo setMessage(int message) {
            mMessageRes = message;
            mMessageText = null;
            return this;
        }

        public BuildInfo setMessage(String message) {
            mMessageText = message;
            mMessageRes = -1;
            return this;
        }

        public BuildInfo setPositive(int positive) {
            mPositiveRes = positive;
            mPositiveText = null;
            return this;
        }

        public BuildInfo setPositive(String positive) {
            mPositiveText = positive;
            mPositiveRes = -1;
            return this;
        }

        public BuildInfo setNegative(int negative) {
            mNegativeRes = negative;
            mNegativeText = null;
            return this;
        }

        public BuildInfo setNegative(String negative) {
            mNegativeText = negative;
            mNegativeRes = -1;
            return this;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeByte(this.mUseHtml ? (byte) 1 : (byte) 0);
            dest.writeInt(this.mMessageRes);
            dest.writeInt(this.mNegativeRes);
            dest.writeInt(this.mPositiveRes);
            dest.writeString(this.mMessageText);
            dest.writeString(this.mNegativeText);
            dest.writeString(this.mPositiveText);
        }

        protected BuildInfo(Parcel in) {
            this.mUseHtml = in.readByte() != 0;
            this.mMessageRes = in.readInt();
            this.mNegativeRes = in.readInt();
            this.mPositiveRes = in.readInt();
            this.mMessageText = in.readString();
            this.mNegativeText = in.readString();
            this.mPositiveText = in.readString();
        }

        public static final Creator<BuildInfo> CREATOR = new Creator<BuildInfo>() {
            @Override
            public BuildInfo createFromParcel(Parcel source) {
                return new BuildInfo(source);
            }

            @Override
            public BuildInfo[] newArray(int size) {
                return new BuildInfo[size];
            }
        };
    }
}
