package com.mobilesolutionworks.works.sample.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.text.Spanned;
import android.text.TextUtils;

import com.mobilesolutionworks.works.core.Host;
import com.mobilesolutionworks.works.core.SimpleWorksController;
import com.mobilesolutionworks.works.host.HostDialogFragment;


public class HostConfirmationDialogFragment extends HostDialogFragment implements DialogInterface.OnClickListener {

    private static final String KEY = ":buildInfo";

    private Controller mController;

    /* package */ IntConsumer callback;

    /* package */ static HostConfirmationDialogFragment create(Builder info) {
        Bundle args = new Bundle();
        args.putParcelable(KEY, info);

        HostConfirmationDialogFragment fragment = new HostConfirmationDialogFragment();
        fragment.callback = info.mCallback;
        fragment.setArguments(args);

        return fragment;
    }

    private static class Controller extends SimpleWorksController<HostDialogFragment> {

        private final IntConsumer mAction;

        public Controller(IntConsumer action) {
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
        callback = null;

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        Bundle args = getArguments();
        HostConfirmationDialogFragment.Builder build = args.getParcelable(KEY);

        builder.setMessage(build.mUseHtml ? fromHtml(build.mMessageText) : build.mMessageText);

        if (!TextUtils.isEmpty(build.mTitle)) {
            builder.setTitle(build.mTitle);
        }

        if (!TextUtils.isEmpty(build.mPositiveText)) {
            builder.setPositiveButton(build.mPositiveText, this);
        }

        if (!TextUtils.isEmpty(build.mNegativeText)) {
            builder.setNegativeButton(build.mNegativeText, this);
        }

        if(build.mItems != null && build.mItems.length > 0) {
            builder.setItems(build.mItems, (dialog, which) -> mController.postResult(which));
        }

        builder.setCancelable(build.mIsCancelable);

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

    public static class Builder implements Parcelable {

        private transient Resources resources;

        /* package */ boolean mUseHtml;

        /* package */ String mTitle;

        /* package */ String mMessageText;

        /* package */ String mNegativeText;

        /* package */ String mPositiveText;

        /* package */ String[] mItems;

        /* package */ String mKey;

        /* package */ boolean mIsCancelable = true;

        /* package */ transient IntConsumer mCallback;

        private transient Runnable success;
        private transient Runnable negative;
        private transient Runnable neutral;

        public Builder(Resources resources) {
            this.resources = resources;
        }

        public Builder useHtml() {
            mUseHtml = true;
            return this;
        }

        public Builder title(int title) {
            mTitle = resources.getString(title);
            return this;
        }

        @Deprecated
        public Builder key(String key) {
            mKey = key;
            return this;
        }

        public Builder title(String title) {
            mTitle = title;
            return this;
        }

        public Builder message(int message) {
            mMessageText = resources.getString(message);
            return this;
        }

        public Builder message(String message) {
            mMessageText = message;
            return this;
        }

        public Builder positive(int positive) {
            mPositiveText = resources.getString(positive);
            return this;
        }

        public Builder positive(String positive) {
            mPositiveText = positive;
            return this;
        }

        public Builder negative(int negative) {
            mNegativeText = resources.getString(negative);
            return this;
        }

        public Builder negative(String negative) {
            mNegativeText = negative;
            return this;
        }

        public Builder callback(IntConsumer callback) {
            mCallback = callback;
            return this;
        }

        public Builder cancelable(boolean isCancelable) {
            mIsCancelable = isCancelable;
            return this;
        }

        /**
         * WARNING : The callback is safe to use if the dialog as been created inside a controller
         * during the rotation, the controller won't be destroyed, so the link is still valid.
         * If you implement the callback inside an activity (or a fragment), the callback will
         * leak the activity (or the fragment).
         *
         */
        public Builder onSuccess(Runnable callback) {
            success = callback;
            return this;
        }

        public Builder onFail(Runnable callback) {
            negative = callback;
            return this;
        }

        public Builder onNeutral(Runnable callback) {
            neutral = callback;
            return this;
        }

        public Builder choices(String[] items) {
            mItems = items;
            return this;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeByte((byte) (mUseHtml ? 1 : 0));
            dest.writeString(mTitle);
            dest.writeString(mMessageText);
            dest.writeString(mNegativeText);
            dest.writeString(mPositiveText);
            dest.writeStringArray(mItems);
            dest.writeString(mKey);
            dest.writeByte((byte) (mIsCancelable ? 1 : 0));
        }

        protected Builder(Parcel in) {
            mUseHtml = in.readByte() != 0;
            mTitle = in.readString();
            mMessageText = in.readString();
            mNegativeText = in.readString();
            mPositiveText = in.readString();
            mItems = in.createStringArray();
            mKey = in.readString();
            mIsCancelable = in.readByte() != 0;
        }

        public static final Creator<Builder> CREATOR = new Creator<Builder>() {
            @Override
            public HostConfirmationDialogFragment.Builder createFromParcel(Parcel source) {
                return new HostConfirmationDialogFragment.Builder(source);
            }

            @Override
            public HostConfirmationDialogFragment.Builder[] newArray(int size) {
                return new HostConfirmationDialogFragment.Builder[size];
            }
        };

        public HostConfirmationDialogFragment build() {
            if(mCallback == null) {
                mCallback = integer -> {
                    if (integer == AlertDialog.BUTTON_POSITIVE) {
                        if(success != null) {
                            success.run();
                        }
                    } else if (integer == AlertDialog.BUTTON_NEGATIVE) {
                        if(negative != null) {
                            negative.run();
                        }
                    } else if (integer == AlertDialog.BUTTON_NEUTRAL) {
                        if(neutral != null) {
                            neutral.run();
                        }
                    }
                };
            }

            resources = null;

            return create(this);
        }

        public void show(Host host) {
            show(host.getHostFragmentManager(), "alert");
        }

        public void show(FragmentManager manager) {
            show(manager, "alert");
        }

        public void show(FragmentManager manager, String tag) {
            build().show(manager, tag);
        }
    }

    public interface IntConsumer {
        /**
         * Consume the given value.
         * @param i the value
         */
        void accept(int i);
    }

}
