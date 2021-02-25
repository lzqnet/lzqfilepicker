package com.zql.filepickerlib.dependency;

import android.os.Parcel;
import android.os.Parcelable;

import com.zql.filepickerlib.util.DateUtil;
import com.zql.filepickerlib.util.Log;
import com.zql.filepickerlib.util.Toast;


public class DependencyWrapper implements Parcelable {

    private IFilePickerDateFormat filePickerDateFormat;
    private IFilePickerLogger filePickerLogger;
    private IFilePickerToast filePickerToast;

    public DependencyWrapper(DependencyWrapper dependencyWrapper) {
        this.filePickerDateFormat = dependencyWrapper.getFilePickerDateFormat();
        this.filePickerLogger = dependencyWrapper.getFilePickerLogger();
        this.filePickerToast = dependencyWrapper.getFilePickerToast();
    }

    public DependencyWrapper() {

    }

    public void initDependency() {
        Log.init(filePickerLogger);
        Toast.init(filePickerToast);
        DateUtil.init(filePickerDateFormat);
    }

    protected DependencyWrapper(Parcel in) {
        filePickerDateFormat = in.readParcelable(IFilePickerDateFormat.class.getClassLoader());
        filePickerLogger = in.readParcelable(IFilePickerLogger.class.getClassLoader());
        filePickerToast = in.readParcelable(IFilePickerToast.class.getClassLoader());
    }

    public static final Creator<DependencyWrapper> CREATOR = new Creator<DependencyWrapper>() {
        @Override
        public DependencyWrapper createFromParcel(Parcel in) {
            return new DependencyWrapper(in);
        }

        @Override
        public DependencyWrapper[] newArray(int size) {
            return new DependencyWrapper[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(filePickerDateFormat, flags);
        dest.writeParcelable(filePickerLogger, flags);
        dest.writeParcelable(filePickerToast, flags);
    }

    public IFilePickerDateFormat getFilePickerDateFormat() {
        return filePickerDateFormat;
    }

    public DependencyWrapper setFilePickerDateFormat(IFilePickerDateFormat filePickerDateFormat) {
        this.filePickerDateFormat = filePickerDateFormat;
        return this;
    }

    public IFilePickerLogger getFilePickerLogger() {
        return filePickerLogger;
    }

    public DependencyWrapper setFilePickerLogger(IFilePickerLogger filePickerLogger) {
        this.filePickerLogger = filePickerLogger;
        return this;
    }

    public IFilePickerToast getFilePickerToast() {
        return filePickerToast;
    }

    public DependencyWrapper setFilePickerToast(IFilePickerToast filePickerToast) {
        this.filePickerToast = filePickerToast;
        return this;
    }
}
