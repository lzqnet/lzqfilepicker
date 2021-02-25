package com.zql.filepickerlib.model;

import android.os.Parcel;
import android.os.Parcelable;


public class FilePathModel implements Parcelable {

    private String name;
    private String path;

    public FilePathModel(){

    }

    public FilePathModel(String name, String path){
        this.name = name;
        this.path = path;
    }

    protected FilePathModel(Parcel in) {
        name = in.readString();
        path = in.readString();
    }

    public static final Creator<FilePathModel> CREATOR = new Creator<FilePathModel>() {
        @Override
        public FilePathModel createFromParcel(Parcel in) {
            return new FilePathModel(in);
        }

        @Override
        public FilePathModel[] newArray(int size) {
            return new FilePathModel[size];
        }
    };

    @Override
    public String toString() {
        return "FilePathModel{" +
                "name='" + name + '\'' +
                ", path='" + path + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(path);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
