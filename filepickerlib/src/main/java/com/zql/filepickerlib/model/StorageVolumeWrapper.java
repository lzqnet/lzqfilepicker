
package com.zql.filepickerlib.model;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Environment;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.storage.StorageVolume;
import android.util.Log;


import com.zql.filepickerlib.util.ReflectUtils;

import java.io.File;


public final class StorageVolumeWrapper implements Parcelable {
    private final static String TAG = "StorageVolumeWrapper";
    private StorageVolume storageVolume;
    private int mIconId;
    private long mUsedSize;
    private long mMaxSize;

    public StorageVolumeWrapper(StorageVolume volume) {
        storageVolume = volume;
    }

    @TargetApi(24)
    public StorageVolumeWrapper(Parcel in) {
        storageVolume = in.readParcelable(StorageVolume.class.getClassLoader());
        mIconId = in.readInt();
        mUsedSize = in.readLong();
        mMaxSize = in.readLong();
    }

    public static final Creator<StorageVolumeWrapper> CREATOR = new Creator<StorageVolumeWrapper>() {
        @Override
        public StorageVolumeWrapper createFromParcel(Parcel in) {
            return new StorageVolumeWrapper(in);
        }

        @Override
        public StorageVolumeWrapper[] newArray(int size) {
            return new StorageVolumeWrapper[size];
        }
    };

    public String getId() {
        try {
            return ReflectUtils.reflect(storageVolume).method("getId").get();

        } catch (Exception e) {
            Log.e(TAG, "getId: ", e);
            return "";
        }
    }

    /**
     * Returns the mount path for the volume.
     *
     * @return the mount path
     */

    public String getPath() {
        try {
            return ReflectUtils.reflect(storageVolume).method("getPath").get();
        } catch (Exception e) {
            Log.e(TAG, "getPath: ", e);
            return "";
        }
    }

    public File getPathFile() {
        return ReflectUtils.reflect(storageVolume).method("getPathFile").get();
    }

    /**
     * Returns a user-visible description of the volume.
     *
     * @return the volume description
     */
    @TargetApi(24)
    public String getDescription(Context context) {
        try {
            return storageVolume.getDescription(context);
        } catch (Exception e) {
            Log.e(TAG, "getDescription: ", e);
            return "";
        }
    }

    /**
     * Returns true if the volume is the primary shared/external storage, which is the volume
     * backed by {@link Environment#getExternalStorageDirectory()}.
     */
    @TargetApi(24)
    public boolean isPrimary() {
        try {
            return storageVolume.isPrimary();

        } catch (Exception e) {
            Log.e(TAG, "isPrimary: ", e);
            return false;
        }
    }


    /**
     * Returns the MTP storage ID for the volume.
     * this is also used for the storage_id column in the media provider.
     *
     * @return MTP storage ID
     * @hide
     */
    public int getStorageId() {
        try {
            return ReflectUtils.reflect(storageVolume).method("getStorageId").get();

        } catch (Exception e) {
            Log.w(TAG, "getStorageId: ", e);
            return -1;
        }
    }


    /**
     * Gets the volume UUID, if any.
     */
    @TargetApi(24)
    public String getUuid() {
        try {
            return storageVolume.getUuid();

        } catch (Exception e) {
            Log.e(TAG, "getUuid: ", e);
            return "";
        }
    }


    public String getUserLabel() {
        try {
            return ReflectUtils.reflect(storageVolume).method("getUserLabel").get();
        } catch (Exception e) {
            Log.e(TAG, "getUserLabel: ", e);
            return "";
        }
    }

    /**
     * Returns the current state of the volume.
     *
     * @return one of {@link Environment#MEDIA_UNKNOWN}, {@link Environment#MEDIA_REMOVED},
     * {@link Environment#MEDIA_UNMOUNTED}, {@link Environment#MEDIA_CHECKING},
     * {@link Environment#MEDIA_NOFS}, {@link Environment#MEDIA_MOUNTED},
     * {@link Environment#MEDIA_MOUNTED_READ_ONLY}, {@link Environment#MEDIA_SHARED},
     * {@link Environment#MEDIA_BAD_REMOVAL}, or {@link Environment#MEDIA_UNMOUNTABLE}.
     */
    @TargetApi(24)
    public String getState() {
        try {
            return storageVolume.getState();

        } catch (Exception e) {
            Log.e(TAG, "getState: ", e);
            return "";
        }
    }

    @Override
    public String toString() {
        return "StorageVolumeWrapper{" +
                "storageVolume=" + storageVolume +
                '}';
    }

    public int getIconId() {
        return mIconId;
    }

    public void setIconId(int mIconId) {
        this.mIconId = mIconId;
    }

    public long getUsedSize() {
        return mUsedSize;
    }

    public void setUsedSize(long mUsedSize) {
        this.mUsedSize = mUsedSize;
    }

    public long getMaxSize() {
        return mMaxSize;
    }

    public void setMaxSize(long mMaxSize) {
        this.mMaxSize = mMaxSize;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    @TargetApi(24)
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(storageVolume, flags);
        dest.writeInt(mIconId);
        dest.writeLong(mUsedSize);
        dest.writeLong(mMaxSize);
    }
}
