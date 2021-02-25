package com.zql.filepickerlib.mimeType;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.text.TextUtils;


import com.zql.filepickerlib.R;
import com.zql.filepickerlib.model.FileSystemObject;
import com.zql.filepickerlib.model.Symlink;
import com.zql.filepickerlib.model.SystemFile;
import com.zql.filepickerlib.util.AmbiguousExtensionHelper;
import com.zql.filepickerlib.util.FileHelper;
import com.zql.filepickerlib.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;


/**
 * A helper class with useful methods for deal with mime types.
 */
public final class MimeTypeHelper {

    /**
     * A constant that defines a string of all mime-types
     */
    public static final String ALL_MIME_TYPES = "*/*";
    private static final String TAG = "MimeTypeHelper";
    private Map<String, ArrayList<MimeTypeInfo>> sMimeTypes;
    private static MimeTypeHelper mMimeTypeHelperInstance;
    private HandlerThread mWorkThread;
    private final Object mLock = new Object();
    private volatile boolean mLoaded = false;
    /**
     * Maps from a combination key of <extension> + <mimetype> to MimeTypeInfo objects.
     */
    private static HashMap<String, MimeTypeInfo> sExtensionMimeTypes;

    /**
     * Constructor of <code>MimeTypeHelper</code>.
     */
    private MimeTypeHelper() {
        super();
    }

    private void startLoadFromDisk(Context context) {
        synchronized (mLock) {
            mLoaded = false;
        }
        new Handler(mWorkThread.getLooper()).post(() -> {
            synchronized (mLock) {
                if (mLoaded) {
                    return;
                }
                mLoaded = true;
                try {
                    loadMimeTypes(context);
                } finally {
                    mLock.notifyAll();
                }
                mWorkThread.quitSafely();
            }
        });
    }

    public void initMimeTypes(Context context) {
        mWorkThread = new HandlerThread("MimeTypeHelper-load");
        mWorkThread.start();
        startLoadFromDisk(context);
    }

    private void awaitLoadedLocked() {
        if (!mLoaded) {
            Log.i(TAG, Thread.currentThread().getName() + " waiting mime type loading from disk...");
        }
        while (!mLoaded) {
            try {
                mLock.wait();
            } catch (InterruptedException unused) {
                Log.e(TAG, unused);
            }
        }
    }

    public static MimeTypeHelper getMimeTypeHelperInstance() {
        if (mMimeTypeHelperInstance == null) {
            synchronized (MimeTypeHelper.class) {
                if (mMimeTypeHelperInstance == null) {
                    mMimeTypeHelperInstance = new MimeTypeHelper();
                }
            }
        }
        return mMimeTypeHelperInstance;
    }

    /**
     * Method that returns the mime/type of the {@link FileSystemObject}.
     *
     * @param fso The file system object
     * @return String The mime/type
     */
    private String getMimeTypeFromFso(FileSystemObject fso) {
        //Directories don't have a mime type
        if (FileHelper.isDirectory(fso)) {
            return null;
        }

        //Get the extension and delivery
        return getMimeTypeFromExtension(fso);
    }

    /**
     * Gets the mimetype of a file, if there are multiple possibilities given it's extension.
     *
     * @param absolutePath The absolute path of the file for which to find the mimetype.
     * @param ext          The extension of the file.
     * @return The correct mimetype for this file, or null if the mimetype cannot be determined
     * or is not ambiguous.
     */
    private String getAmbiguousExtensionMimeType(String absolutePath, String ext) {
        if (AmbiguousExtensionHelper.AMBIGUOUS_EXTENSIONS_MAP.containsKey(ext)) {
            AmbiguousExtensionHelper helper = AmbiguousExtensionHelper.AMBIGUOUS_EXTENSIONS_MAP.get(ext);
            String mimeType = helper.getMimeType(absolutePath, ext);
            if (!TextUtils.isEmpty(mimeType)) {
                return mimeType;
            }
        }
        return null;
    }


    public MimeTypeInfo getMimeTypeFromPath(String absolutePath, String ext) {
        return getMimeTypeFromPath(absolutePath, ext, false);
    }

    public MimeTypeInfo getMimeTypeFromPath(String absolutePath, String ext, boolean firstFound) {
        synchronized (mLock) {
            awaitLoadedLocked();
            MimeTypeInfo mimeTypeInfo = null;
            ArrayList<MimeTypeInfo> mimeTypeInfoList = sMimeTypes.get(ext.toLowerCase(Locale.ROOT));
            // Multiple mimetypes map to the same extension, try to resolve it.
            if (mimeTypeInfoList != null && mimeTypeInfoList.size() > 1) {
                if (absolutePath != null && !firstFound) {
                    String mimeType = getAmbiguousExtensionMimeType(absolutePath, ext);
                    mimeTypeInfo = sExtensionMimeTypes.get(ext + mimeType);
                } else {
                    // We don't have the ability to read the file to resolve the ambiguity,
                    // so default to the first available mimetype.
                    mimeTypeInfo = mimeTypeInfoList.get(0);
                }
            } else if (mimeTypeInfoList != null && mimeTypeInfoList.size() == 1) {
                // Only one possible mimetype, so pick that one.
                mimeTypeInfo = mimeTypeInfoList.get(0);
            }
            return mimeTypeInfo;
        }
    }

    private String getMimeTypeFromExtension(final FileSystemObject fso) {
        String ext = FileHelper.getExtension(fso);
        if (ext == null) {
            return "application/octet-stream";
        }

        // If this extension is ambiguous, attempt to resolve it.
        String mimeType = getAmbiguousExtensionMimeType(fso.getFullPath(), ext);
        if (mimeType != null) {
            return mimeType;
        }

        //Load from the database of mime types
        MimeTypeInfo mimeTypeInfo = getMimeTypeFromPath(fso.getFullPath(), ext);
        if (mimeTypeInfo == null) {
            return "application/octet-stream";
        }

        return mimeTypeInfo.mMimeType;
    }

    /**
     * Method that returns the mime/type category of the file.
     *
     * @param context      The current context
     * @param ext          The extension of the file
     * @param absolutePath The absolute path of the file. Can be null if not available.
     * @return MimeTypeCategory The mime/type category
     */
    private final MimeTypeCategory getCategoryFromExt(Context context, String ext, String absolutePath) {
        // Ensure that have a context
        if (context == null) {
            return MimeTypeCategory.NONE;
        }
        if (ext != null) {
            //Load from the database of mime types
            MimeTypeInfo mimeTypeInfo = getMimeTypeFromPath(absolutePath, ext);
            if (mimeTypeInfo != null) {
                return mimeTypeInfo.mCategory;
            }
        }
        return MimeTypeCategory.NONE;
    }

    /**
     * Method that returns the mime/type category of the file.
     *
     * @param context The current context
     * @param file    The file
     * @return MimeTypeCategory The mime/type category
     */
    private MimeTypeCategory getCategory(Context context, File file) {
        if (file.isDirectory()) {
            return MimeTypeCategory.NONE;
        }
        return getCategoryFromExt(context, FileHelper.getExtension(file.getName()), file.getAbsolutePath());
    }

    /**
     * Method that returns the mime/type category of the file system object.
     *
     * @param context The current context
     * @param fso     The file system object
     * @return MimeTypeCategory The mime/type category
     */
    public final MimeTypeCategory getCategory(Context context, FileSystemObject fso) {
        // Directory and Symlinks no computes as category
        if (FileHelper.isDirectory(fso)) {
            return MimeTypeCategory.NONE;
        }
        if (fso instanceof Symlink) {
            return MimeTypeCategory.NONE;
        }

        //Get the extension and delivery
        final MimeTypeCategory category = getCategoryFromExt(context, FileHelper.getExtension(fso), fso.getFullPath());

        // Check  system file
        if (category == MimeTypeCategory.NONE && fso instanceof SystemFile) {
            return MimeTypeCategory.SYSTEM;
        }

        return category;
    }

    /**
     * Method that returns if a file system object matches with a mime-type expression.
     *
     * @param fso                The file system object to check
     * @param mimeTypeExpression The mime-type expression (xe: *&#47;*, audio&#47;*)
     * @return boolean If the file system object matches the mime-type expression
     */
    public final boolean matchesMimeType(FileSystemObject fso, String mimeTypeExpression) {
        String mimeType = getMimeTypeFromFso(fso);
        if (mimeType == null) return false;
        return mimeType.matches(convertToRegExp(mimeTypeExpression));
    }

    /**
     * Method that loads the mime type information.
     *
     * @param context The current context
     */
    //IMP! This must be invoked from the main activity creation
    private void loadMimeTypes(Context context) {
        try {
            // Load the mime/type database
            // Parse the properties to an in-memory structure
            // Format:  <extension> = <category> | <mime type> | <drawable>
            sMimeTypes = new HashMap<>();

            Properties mimeTypes = new Properties();
            mimeTypes.load(context.getResources().openRawResource(R.raw.drivefilepicker_mime_types));

            sExtensionMimeTypes = new HashMap<>();
            Enumeration<Object> objectEnumeration = mimeTypes.keys();
            while (objectEnumeration.hasMoreElements()) {
                try {
                    String extension = (String) objectEnumeration.nextElement();
                    String data = mimeTypes.getProperty(extension);
                    String[] datas = data.split(",");
                    for (String theData : datas) {
                        String[] mimeData = theData.split("\\|");  //$NON-NLS-1$

                        // Create a reference of MimeType
                        MimeTypeInfo mimeTypeInfo = new MimeTypeInfo();
                        mimeTypeInfo.mCategory = MimeTypeCategory.valueOf(mimeData[0].trim());
                        mimeTypeInfo.mMimeType = mimeData[1].trim();
                        mimeTypeInfo.mDrawable = mimeData[2].trim();

                        // If no list exists yet for this mimetype, create one.
                        // Else, add it to the existing list.
                        if (sMimeTypes.get(extension) == null) {
                            ArrayList<MimeTypeInfo> infoList = new ArrayList<>();
                            infoList.add(mimeTypeInfo);
                            sMimeTypes.put(extension, infoList);
                        } else {
                            sMimeTypes.get(extension).add(mimeTypeInfo);
                        }
                        sExtensionMimeTypes.put(extension + mimeTypeInfo.mMimeType, mimeTypeInfo);
                    }

                } catch (Exception e1) {
                    Log.e(TAG, "Fail to load objectEnumeration", e1);
                }
            }

        } catch (Exception e2) {
            Log.e(TAG, "Fail to load mime types raw file.", e2);
        }
    }

    /**
     * Method that converts the mime-type expression to a regular expression
     *
     * @param mimeTypeExpression The mime-type expression
     * @return String The regular expression
     */
    private String convertToRegExp(String mimeTypeExpression) {
        return mimeTypeExpression.replaceAll("\\*", ".\\*");
    }

    private static final String MIME_TYPE_APK = "application/vnd.android.package-archive";

    /**
     * Method that returns if the FileSystemObject is an Android app.
     *
     * @param context The current context
     * @param fso     The FileSystemObject to check
     * @return boolean If the FileSystemObject is an Android app.
     */
    public boolean isAndroidApp(Context context, FileSystemObject fso) {
        return MIME_TYPE_APK.equals(getMimeTypeFromFso(fso));
    }

    /**
     * Method that returns if the FileSystemObject is an image file.
     *
     * @param context The current context
     * @param fso     The FileSystemObject to check
     * @return boolean If the FileSystemObject is an image file.
     */
    public boolean isImage(Context context, FileSystemObject fso) {
        return getCategory(context, fso).compareTo(MimeTypeCategory.IMAGE) == 0;
    }

    /**
     * Method that returns if the FileSystemObject is an video file.
     *
     * @param context The current context
     * @param fso     The FileSystemObject to check
     * @return boolean If the FileSystemObject is an video file.
     */
    public boolean isVideo(Context context, FileSystemObject fso) {
        return getCategory(context, fso).compareTo(MimeTypeCategory.VIDEO) == 0;
    }

    /**
     * Method that returns if the File is an image file.
     *
     * @param context The current context
     * @param file    The File to check
     * @return boolean If the File is an image file.
     */
    public boolean isImage(Context context, File file) {
        return getCategory(context, file).compareTo(MimeTypeCategory.IMAGE) == 0;
    }

    /**
     * Method that returns if the File is an video file.
     *
     * @param context The current context
     * @param file    The File to check
     * @return boolean If the File is an video file.
     */
    public boolean isVideo(Context context, File file) {
        return getCategory(context, file).compareTo(MimeTypeCategory.VIDEO) == 0;
    }

    /**
     * Method that returns if the File is an audio file.
     *
     * @param context The current context
     * @param file    The File to check
     * @return boolean If the File is an audio file.
     */
    public boolean isAudio(Context context, File file) {
        return getCategory(context, file).compareTo(MimeTypeCategory.AUDIO) == 0;
    }

}
