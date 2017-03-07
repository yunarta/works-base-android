package com.mobilesolutionworks.works.sample.fragment;

import android.app.Dialog;
import android.content.Context;
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

import com.mobilesolutionworks.works.core.SimpleWorksController;

import java.util.function.Consumer;

/**
 * Created by yunarta on 7/12/16.
 */

public class WorksConfirmationDialogFragment extends WorksDialogFragment implements DialogInterface.OnClickListener {

    private Controller mController;

    /* package */ Consumer<Integer> callback;

    private static WorksConfirmationDialogFragment create(Builder info) {
        Bundle args = new Bundle();
        args.putParcelable(":buildInfo", info);

        WorksConfirmationDialogFragment fragment = new WorksConfirmationDialogFragment();
        fragment.callback = info.mCallback;
        fragment.setArguments(args);

        return fragment;
    }

    private static class Controller extends SimpleWorksController<WorksDialogFragment> {

        private final Consumer<Integer> mAction;

        public Controller(Consumer<Integer> action) {
            this.mAction = action;
        }

        public void postResult(int choiceRes) {
            if(mAction != null ) {
                mAction.accept(choiceRes);
            }
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mController = SimpleWorksController.init(this, WorksConstants.WORKS_ID_DIALOG_CONTROLLER, () -> new Controller(callback));

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        Bundle args = getArguments();
        WorksConfirmationDialogFragment.Builder build = args.getParcelable(":buildInfo");

        builder.setMessage(build.mUseHtml ? fromHtml(build.mMessageText) : build.mMessageText);

        if (!TextUtils.isEmpty(build.mPositiveText)) {
            builder.setPositiveButton(build.mPositiveText, this);
        }

        if (!TextUtils.isEmpty(build.mNegativeText)) {
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

    public static class Builder implements Parcelable{

        private transient Context context;

        private boolean mUseHtml;

        private String mMessageText;

        private String mNegativeText;

        private String mPositiveText;

        private Runnable mOnPositive;

        private Runnable mOnNegative;

        private transient Consumer<Integer> mCallback;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder useHtml() {
            mUseHtml = true;
            return this;
        }

        public Builder message(int message) {
            mMessageText = context.getString(message);
            return this;
        }

        public Builder message(String message) {
            mMessageText = message;
            return this;
        }

        public Builder positive(int positive) {
            mPositiveText = context.getString(positive);
            return this;
        }

        public Builder positive(String positive) {
            mPositiveText = positive;
            return this;
        }

        public Builder negative(int negative) {
            mNegativeText = context.getString(negative);
            return this;
        }

        public Builder negative(String negative) {
            mNegativeText = negative;
            return this;
        }

        public Builder callback(Consumer<Integer> callback) {
            mCallback = callback;
            return this;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeByte(this.mUseHtml ? (byte) 1 : (byte) 0);
            dest.writeString(this.mMessageText);
            dest.writeString(this.mNegativeText);
            dest.writeString(this.mPositiveText);
        }

        protected Builder(Parcel in) {
            this.mUseHtml = in.readByte() != 0;
            this.mMessageText = in.readString();
            this.mNegativeText = in.readString();
            this.mPositiveText = in.readString();
        }

        public static final Parcelable.Creator<WorksConfirmationDialogFragment.Builder> CREATOR = new Parcelable.Creator<WorksConfirmationDialogFragment.Builder>() {
            @Override
            public WorksConfirmationDialogFragment.Builder createFromParcel(Parcel source) {
                return new WorksConfirmationDialogFragment.Builder(source);
            }

            @Override
            public WorksConfirmationDialogFragment.Builder[] newArray(int size) {
                return new WorksConfirmationDialogFragment.Builder[size];
            }
        };

        public WorksConfirmationDialogFragment build() {
            return create(this);
        }
    }
}
