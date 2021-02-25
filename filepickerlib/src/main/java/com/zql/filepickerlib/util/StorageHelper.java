package com.zql.filepickerlib.util;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Environment;
import android.os.StatFs;
import android.os.storage.StorageManager;
import android.os.storage.StorageVolume;
import android.text.TextUtils;


import com.zql.filepickerlib.R;
import com.zql.filepickerlib.model.StorageVolumeWrapper;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Locale;

/**
 * A helper class with useful methods for deal with storage.
 */
public final class StorageHelper {
    private final static String InternalStoragePath = "/storage/emulated/0";
    private final static String TAG = "StorageHelper";

    private static ArrayList<StorageVolumeWrapper> sStorageVolumes;

    /**
     * Method that returns the storage volumes defined in the system.  This method uses
     * reflection to retrieve the method because CM10 has a {@link Context}
     * as first parameter, that AOSP hasn't.
     *
     * @param context The current context
     * @param reload  If true, re-query the volumes and do not return the already cached list.
     * @return StorageVolume[] The storage volumes defined in the system
     */

    @TargetApi(24)
    public static synchronized ArrayList<StorageVolumeWrapper> getStorageVolumes(Context context, boolean reload) {
        if (sStorageVolumes == null || reload) {
            //IMP!! Android SDK doesn't have a "getVolumeList" but is supported by CM10.
            //Use reflect to get this value (if possible)
            try {
                StorageManager storageManager = (StorageManager) context.getSystemService(Context.STORAGE_SERVICE);
                Method method = storageManager.getClass().getMethod("getVolumeList");
                StorageVolume[] storageVolumes = (StorageVolume[]) method.invoke(storageManager);
                sStorageVolumes = convertStorageVolume(context, storageVolumes);
                return sStorageVolumes;

            } catch (Exception ex) {
                //Ignore. Android SDK StorageManager class doesn't have this method
                //Use default android information from environment
                Log.e(TAG, "getStorageVolumes: ", ex);
                try {
                    File externalStorage = Environment.getExternalStorageDirectory();
                    String path = externalStorage.getCanonicalPath();
                    String description;
                    if (path.toLowerCase(Locale.ROOT).contains("usb")) {
                        description = context.getString(R.string.usb_storage);
                    } else {
                        description = context.getString(R.string.external_storage);
                    }
                    // Android SDK has a different constructor for StorageVolume. In CM10 the
                    // description is a resource id. Create the object by reflection
                    Constructor<StorageVolume> constructor =
                            StorageVolume.class.getConstructor(
                                    String.class,
                                    String.class,
                                    boolean.class,
                                    boolean.class,
                                    int.class,
                                    boolean.class,
                                    long.class);
                    StorageVolume storageVolume = constructor.newInstance(path, description, false, false, 0, false, 0);
                    sStorageVolumes = convertStorageVolume(context, new StorageVolume[]{storageVolume});
                } catch (Exception ex2) {
                    Log.e(TAG, "getStorageVolumes: ", ex2);
                }
            }
            if (sStorageVolumes == null) {
                sStorageVolumes = convertStorageVolume(context, new StorageVolume[]{});
            }
        }
        return sStorageVolumes;
    }

    private static synchronized ArrayList<StorageVolumeWrapper> convertStorageVolume(Context context, StorageVolume[] volumes) {
        ArrayList<StorageVolumeWrapper> wrappers = new ArrayList<>();
        for (StorageVolume volume : volumes) {
            StorageVolumeWrapper storageVolumeWrapper = new StorageVolumeWrapper(volume);
            long totalSize = getTotalMemorySize(storageVolumeWrapper.getPath());
            long usedSize = getUsedMemorySize(storageVolumeWrapper.getPath());
            storageVolumeWrapper.setMaxSize(totalSize);
            storageVolumeWrapper.setUsedSize(usedSize);

            if (TextUtils.equals(storageVolumeWrapper.getPath(), InternalStoragePath)) {
                storageVolumeWrapper.setIconId(R.drawable.drivefilepicker_internal_storage);
            } else {
                storageVolumeWrapper.setIconId(R.drawable.drivefilepicker_sd_card);
            }
            wrappers.add(storageVolumeWrapper);
        }
        return wrappers;
    }

    public static String getDefaultStorage(){
        return InternalStoragePath;
    }

    public static long getTotalMemorySize(String path) {
        try {
            StatFs stat = new StatFs(path);
            long blockSize = stat.getBlockSizeLong();
            long blockCountLong = stat.getBlockCountLong();
            return blockCountLong * blockSize;
        } catch (Exception e) {
            Log.e(TAG, e);
        }
        return 0;
    }

    public static long getAvailableMemorySize(String path) {
        try {
            StatFs stat = new StatFs(path);
            long blockSize = stat.getBlockSizeLong();
            long availableBlocksLong = stat.getAvailableBlocksLong();
            return availableBlocksLong * blockSize;
        } catch (Exception e) {
            Log.e(TAG, e);
        }
        return 0;
    }

    public static long getUsedMemorySize(String path) {
        return getTotalMemorySize(path) - getAvailableMemorySize(path);
    }
}
