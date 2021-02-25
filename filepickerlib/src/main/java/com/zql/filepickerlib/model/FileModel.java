package com.zql.filepickerlib.model;

import android.util.Log;

/**
 * Created by linzhiqing on 2019/3/26.
 */

public class FileModel {
    private FileSystemObject fileSystemObject;
    private boolean isSelected;
    private int mResourceIconId;

    public FileModel(FileSystemObject fileSystemObject) {
        this.fileSystemObject = fileSystemObject;
    }

    public FileSystemObject getFileSystemObject() {
        return fileSystemObject;
    }

    public void setFileSystemObject(FileSystemObject fileSystemObject) {
//        Log.d("lzqtest","FileModel.java.setFileSystemObject: 23 ");
        this.fileSystemObject = fileSystemObject;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        Log.d("lzqtest","FileModel.java.setSelected: selecte "+selected);
        isSelected = selected;
    }

    public boolean isDirectory() {
        return fileSystemObject instanceof Directory;
    }

    /**
     * Method that returns the identifier of the drawable icon associated
     * to the object.
     *
     * @return int The identifier of the drawable icon
     * @hide
     */
    public int getResourceIconId() {
        return this.mResourceIconId;
    }

    /**
     * Method that sets the identifier of the drawable icon associated
     * to the object.
     *
     * @param resourceIconId The identifier of the drawable icon
     * @hide
     */
    public void setResourceIconId(int resourceIconId) {
//        Log.d("lzqtest","FileModel.java.setResourceIconId: resourceIconId= "+resourceIconId);
        this.mResourceIconId = resourceIconId;
    }
//
//    @Override
//    public String toString() {
//        return "FileModel{" +
////                "fileSystemObject=" + fileSystemObject +
//                ", isSelected=" + isSelected +
////                ", mResourceIconId=" + mResourceIconId +
//                '}';
//    }
}
