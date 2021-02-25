package com.zql.filepickerlib.util;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.text.TextUtils;


import com.zql.filepickerlib.R;
import com.zql.filepickerlib.model.Directory;
import com.zql.filepickerlib.model.FileSystemObject;
import com.zql.filepickerlib.model.Permissions;
import com.zql.filepickerlib.model.RegularFile;
import com.zql.filepickerlib.model.Symlink;
import com.zql.filepickerlib.model.SystemFile;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * A helper class with useful methods for deal with files.
 */
public final class FileHelper {

    private final static int Decimal1000 = 1000;
    private final static int Decimal1024 = 1024;
    private final static int Decimal = Decimal1024;

    private final static String BUnit = "B";
    private final static String KBUnit = "K";
    private final static String MBUnit = "M";
    private final static String GBUnit = "G";

    /**
     * The root directory.
     *
     * @hide
     */
    public static final String ROOT_DIRECTORY = "/";  //$NON-NLS-1$
    /**
     * The parent directory string.
     *
     * @hide
     */
    public static final String PARENT_DIRECTORY = "..";  //$NON-NLS-1$
    /**
     * The current directory string.
     *
     * @hide
     */
    public static final String CURRENT_DIRECTORY = ".";  //$NON-NLS-1$

    private static final String TAG = "FileHelper";

    // The date/time formats objects
    /**
     * Special extension for compressed tar files
     */
    private static final String[] COMPRESSED_TAR = {"tar.gz", "tar.bz2", "tar.lzma"};

    private static final String[][] MIME_MapTable = {
            {"3gp", "video/3gpp"}, {"apk", "application/vnd.android.package-archive"},
            {"asf", "video/x-ms-asf"}, {"avi", "video/x-msvideo"},
            {"bin", "application/octet-stream"}, {"bmp", "image/bmp"}, {"c", "text/plain"},
            {"class", "application/octet-stream"}, {"conf", "text/plain"}, {"cpp", "text/plain"},
            {"doc", "application/msword"},
            {"docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document"},
            {"xls", "application/vnd.ms-excel"},
            {"xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"},
            {"exe", "application/octet-stream"}, {"gif", "image/gif"},
            {"gtar", "application/x-gtar"}, {"gz", "application/x-gzip"}, {"h", "text/plain"},
            {"htm", "text/html"}, {"html", "text/html"}, {"jar", "application/java-archive"},
            {"java", "text/plain"}, {"jpeg", "image/jpeg"}, {"jpg", "image/jpeg"},
            {"js", "application/x-javascript"}, {"log", "text/plain"}, {"m3u", "audio/x-mpegurl"},
            {"m4a", "audio/mp4a-latm"}, {"m4b", "audio/mp4a-latm"}, {"m4p", "audio/mp4a-latm"},
            {"m4u", "video/vnd.mpegurl"}, {"m4v", "video/x-m4v"}, {"mov", "video/quicktime"},
            {"mp2", "audio/x-mpeg"}, {"mp3", "audio/x-mpeg"}, {"mpg4", "video/mp4"},
            {"mpc", "application/vnd.mpohun.certificate"}, {"mpe", "video/mpeg"},
            {"vob", "video/mpeg"}, {"mpg", "video/mpeg"}, {"mp4", "video/mp4"},
            {"mpga", "audio/mpeg"}, {"mpeg", "video/mpeg"}, {"msg", "application/vnd.ms-outlook"},
            {"ogg", "audio/ogg"}, {"pdf", "application/pdf"}, {"png", "image/png"},
            {"pps", "application/vnd.ms-powerpoint"}, {"ppt", "application/vnd.ms-powerpoint"},
            {"pptx", "application/vnd.openxmlformats-officedocument.presentationml.presentation"},
            {"prop", "text/plain"}, {"rc", "text/plain"}, {"rmvb", "audio/x-pn-realaudio"},
            {"rtf", "application/rtf"}, {"sh", "text/plain"}, {"tar", "application/x-tar"},
            {"tgz", "application/x-compressed"}, {"txt", "text/plain"}, {"wav", "audio/x-wav"},
            {"wma", "audio/x-ms-wma"}, {"wmv", "audio/x-ms-wmv"},
            {"wps", "application/vnd.ms-works"}, {"xml", "text/plain"},
            {"z", "application/x-compress"}, {"zip", "application/x-zip-compressed"},
            {"rar", "application/x-rar-compressed"}, {"ai", "application/postscript"}, {"", "*/*"}
    };
    private static final Map<String, String> EXTENSION_TO_MIME_MAP = new HashMap<>();
    private static final Map<String, String> MIME_TO_EXTENSION_MAP = new HashMap<>();

    static {
        for (String[] mimeMapTable : MIME_MapTable) {
            EXTENSION_TO_MIME_MAP.put(mimeMapTable[0], mimeMapTable[1]);
            MIME_TO_EXTENSION_MAP.put(mimeMapTable[1], mimeMapTable[0]);
        }
    }

    /**
     * Constructor of <code>FileHelper</code>.
     */
    private FileHelper() {
        super();
    }

    /**
     * Method that check if a file is a symbolic link.
     *
     * @param file File to check
     * @return boolean If file is a symbolic link
     * @throws IOException If real file couldn't be checked
     */
    public static boolean isSymlink(File file) throws IOException {
        return file.getAbsolutePath().compareTo(file.getCanonicalPath()) != 0;
    }

    /**
     * Method that resolves a symbolic link to the real file or directory.
     *
     * @param file File to check
     * @return File The real file or directory
     * @throws IOException If real file couldn't be resolved
     */
    public static File resolveSymlink(File file) throws IOException {
        return file.getCanonicalFile();
    }

    /**
     * Method that returns a more human readable of the size
     * of a file system object.
     *
     * @param fso File system object
     * @return String The human readable size (void if fso don't supports size)
     */
    public static String getHumanReadableSize(FileSystemObject fso) {
        //Only if has size
        if (fso instanceof Directory) {
            return "";
        }
        if (hasSymlinkRef(fso)) {
            if (isSymlinkRefDirectory(fso)) {
                return "";
            }
            return getHumanReadableSize(((Symlink) fso).getLinkRef().getSize());
        }
        return getHumanReadableSize(fso.getSize());
    }


    /**
     * Method that returns a more human readable of a size in bytes.
     *
     * @param size The size in bytes
     * @return String The human readable size
     */
    public static String getHumanReadableSize(long size) {
        final String[] magnitude = {
                BUnit,
                KBUnit,
                MBUnit,
                GBUnit
        };
        double aux = size;
        int cc = magnitude.length;
        for (String i1 : magnitude) {
            if (aux < Decimal) {
                double cleanSize = Math.round(aux * 100);
                return cleanSize / 100 + " " + i1;
            } else {
                aux = aux / Decimal;
            }
        }
        double cleanSize = Math.round(aux * 100);
        return cleanSize / 100 + " " + magnitude[cc - 1];
    }

    /**
     * Method that returns if the file system object if the root directory.
     *
     * @param fso The file system object to check
     * @return boolean if the file system object if the root directory
     */
    public static boolean isRootDirectory(FileSystemObject fso) {
        if (fso.getName() == null) return true;
        return fso.getName().compareTo(FileHelper.ROOT_DIRECTORY) == 0;
    }

    /**
     * Method that returns if the folder if the root directory.
     *
     * @param folder The folder
     * @return boolean if the folder if the root directory
     */
    public static boolean isRootDirectory(String folder) {
        if (folder == null) return true;
        return isRootDirectory(new File(folder));
    }

    /**
     * Method that returns if the folder if the root directory.
     *
     * @param folder The folder
     * @return boolean if the folder if the root directory
     */
    public static boolean isRootDirectory(File folder) {
        return folder.getPath().compareTo(FileHelper.ROOT_DIRECTORY) == 0;
    }

    /**
     * Method that returns if the parent file system object if the root directory.
     *
     * @param fso The parent file system object to check
     * @return boolean if the parent file system object if the root directory
     */
    public static boolean isParentRootDirectory(FileSystemObject fso) {
        if (fso.getParent() == null) return true;
        return fso.getParent().compareTo(FileHelper.ROOT_DIRECTORY) == 0;
    }

    /**
     * Method that returns the name without the extension of a file system object.
     *
     * @param fso The file system object
     * @return The name without the extension of the file system object.
     */
    public static String getName(FileSystemObject fso) {
        return getName(fso.getName());
    }

    /**
     * Method that returns the name without the extension of a file system object.
     *
     * @param name The name of file system object
     * @return The name without the extension of the file system object.
     */
    public static String getName(String name) {
        String ext = getExtension(name);
        if (ext == null) return name;
        return name.substring(0, name.length() - ext.length() - 1);
    }

    /**
     * Method that returns the extension of a file system object.
     *
     * @param fso The file system object
     * @return The extension of the file system object, or <code>null</code>
     * if <code>fso</code> has no extension.
     */
    public static String getExtension(FileSystemObject fso) {
        return getExtension(fso.getName());
    }

    /**
     * Method that returns the extension of a file system object.
     *
     * @param name The name of file system object
     * @return The extension of the file system object, or <code>null</code>
     * if <code>fso</code> has no extension.
     */
    public static String getExtension(String name) {
        final char dot = '.';
        int pos = name.lastIndexOf(dot);
        if (pos == -1 || pos == 0) { // Hidden files doesn't have extensions
            return null;
        }

        // Exceptions to the general extraction method
        int cc = COMPRESSED_TAR.length;
        for (int i = 0; i < cc; i++) {
            if (name.endsWith("." + COMPRESSED_TAR[i])) { //$NON-NLS-1$
                return COMPRESSED_TAR[i];
            }
        }

        // General extraction method
        return name.substring(pos + 1);
    }

    /**
     * Method that returns the parent directory of a file/folder
     *
     * @param path The file/folder
     * @return String The parent directory
     */
    public static String getParentDir(String path) {
        return getParentDir(new File(path));
    }

    /**
     * Method that returns the parent directory of a file/folder
     *
     * @param path The file/folder
     * @return String The parent directory
     */
    public static String getParentDir(File path) {
        String parent = path.getParent();
        if (parent == null && path.getAbsolutePath().compareTo(FileHelper.ROOT_DIRECTORY) != 0) {
            parent = FileHelper.ROOT_DIRECTORY;
        }
        return parent;
    }

    /**
     * Method that evaluates if a path is relative.
     *
     * @param src The path to check
     * @return boolean If a path is relative
     */
    public static boolean isRelativePath(String src) {
        if (src.startsWith(CURRENT_DIRECTORY + File.separator)) {
            return true;
        }
        if (src.startsWith(PARENT_DIRECTORY + File.separator)) {
            return true;
        }
        if (src.contains(File.separator + CURRENT_DIRECTORY + File.separator)) {
            return true;
        }
        if (src.contains(File.separator + PARENT_DIRECTORY + File.separator)) {
            return true;
        }
        if (!src.startsWith(ROOT_DIRECTORY)) {
            return true;
        }
        return false;
    }

    /**
     * Method that check if the file system object is a {@link Symlink} and
     * has a link reference.
     *
     * @param fso The file system object to check
     * @return boolean If file system object the has a link reference
     */
    public static boolean hasSymlinkRef(FileSystemObject fso) {
        if (fso instanceof Symlink) {
            return ((Symlink) fso).getLinkRef() != null;
        }
        return false;
    }

    /**
     * Method that check if the file system object is a {@link Symlink} and
     * the link reference is a directory.
     *
     * @param fso The file system object to check
     * @return boolean If file system object the link reference is a directory
     */
    public static boolean isSymlinkRefDirectory(FileSystemObject fso) {
        if (!hasSymlinkRef(fso)) {
            return false;
        }
        return ((Symlink) fso).getLinkRef() instanceof Directory;
    }

    /**
     * Method that check if the file system object is a {@link Symlink} and
     * the link reference is a system file.
     *
     * @param fso The file system object to check
     * @return boolean If file system object the link reference is a system file
     */
    public static boolean isSymlinkRefSystemFile(FileSystemObject fso) {
        if (!hasSymlinkRef(fso)) {
            return false;
        }
        return ((Symlink) fso).getLinkRef() instanceof SystemFile;
    }

    /**
     * Method that checks if a file system object is a directory (real o symlink).
     *
     * @param fso The file system object to check
     * @return boolean If file system object is a directory
     */
    public static boolean isDirectory(FileSystemObject fso) {
        if (fso instanceof Directory) {
            return true;
        }
        if (isSymlinkRefDirectory(fso)) {
            return true;
        }
        return false;
    }

    /**
     * Method that checks if a file system object is a system file (real o symlink).
     *
     * @param fso The file system object to check
     * @return boolean If file system object is a system file
     */
    public static boolean isSystemFile(FileSystemObject fso) {
        if (fso instanceof SystemFile) {
            return true;
        }
        if (isSymlinkRefSystemFile(fso)) {
            return true;
        }
        return false;
    }

    /**
     * Method that returns the real reference of a file system object
     * (the reference file system object if the file system object is a symlink.
     * Otherwise the same reference).
     *
     * @param fso The file system object to check
     * @return FileSystemObject The real file system object reference
     */
    public static FileSystemObject getReference(FileSystemObject fso) {
        if (hasSymlinkRef(fso)) {
            return ((Symlink) fso).getLinkRef();
        }
        return fso;
    }

    /**
     * Method that add to the path the trailing slash
     *
     * @param path The path
     * @return String The path with the trailing slash
     */
    public static String addTrailingSlash(String path) {
        if (path == null) return null;
        return path.endsWith(File.separator) ? path : path + File.separator;
    }

    /**
     * Method that cleans the path and removes the trailing slash
     *
     * @param path The path to clean
     * @return String The path without the trailing slash
     */
    public static String removeTrailingSlash(String path) {
        if (path == null) return null;
        if (path.trim().compareTo(ROOT_DIRECTORY) == 0) return path;
        if (path.endsWith(File.separator)) {
            return path.substring(0, path.length() - 1);
        }
        return path;
    }


    /**
     * Method that returns is a {@link FileSystemObject} can be handled by this application
     * allowing the uncompression of the file
     *
     * @param fso The file system object to verify
     * @return boolean If the file is supported
     */

    /**
     * Method that converts an absolute path to a relative path
     *
     * @param path       The absolute path to convert
     * @param relativeTo The absolute path from which make path relative to (a folder)
     * @return String The relative path
     */
    public static String toRelativePath(String path, String relativeTo) {
        // Normalize the paths
        File f1 = new File(path);
        File f2 = new File(relativeTo);
        String s1 = f1.getAbsolutePath();
        String s2 = f2.getAbsolutePath();
        if (!s2.endsWith(File.separator)) {
            s2 = s2 + File.separator;
        }

        // If s2 contains s1 then the relative is replace of the start of the path
        if (s1.startsWith(s2)) {
            return s1.substring(s2.length());
        }

        StringBuffer relative = new StringBuffer();
        do {
            File f3 = new File(s2);
            relative.append(String.format("..%s", File.separator)); //$NON-NLS-1$
            s2 = f3.getParent() + File.separator;
        } while (!s1.startsWith(s2) && !s1.startsWith(new File(s2).getAbsolutePath()));
        s2 = new File(s2).getAbsolutePath();
        return relative.toString() + s1.substring(s2.length());
    }

    /**
     * Method that creates a {@link FileSystemObject} from a {@link File}
     *
     * @param file The file or folder reference
     * @return FileSystemObject The file system object reference
     */
    public static FileSystemObject createFileSystemObject(File file) {
        try {
            // The user and group name of the files. Use the defaults one for sdcards
            final String USER = "root"; //$NON-NLS-1$
            final String GROUP = "sdcard_r"; //$NON-NLS-1$

            // The user and group name of the files. In ChRoot, aosp give restrict access to
            // this user and group. This applies for permission also. This has no really much
            // interest if we not allow to change the permissions
            Permissions perm = file.isDirectory()
                    ? Permissions.createDefaultFolderPermissions()
                    : Permissions.createDefaultFilePermissions();

            // Build a directory?
            Date lastModified = new Date(file.lastModified());
            String fullPath = file.getAbsolutePath();
            if (file.isDirectory()) {
                return new Directory(file.getName(), file.getParent(), perm, lastModified, lastModified, lastModified); // The only date we have
            }

            // Build a regular file
            return new RegularFile(file.getName(), file.getParent(), perm, file.length(), lastModified, lastModified, lastModified); // The only date we have
        } catch (Exception e) {
            Log.e(TAG, "Exception retrieving the fso", e); //$NON-NLS-1$
        }
        return null;
    }

    /**
     * 打开文件
     * 若打开失败，使用兼容的mimeType重试一次
     *
     * @param context
     * @param file
     * @param mimeType
     * @return
     */
    public static void openFile(Context context, File file, String mimeType) {

        if (file != null && file.exists()) {
            boolean firstOpenResult = openFileInner(context, file, mimeType);
            if (!firstOpenResult) {

                String compatType = new MimeTypeCompat().getCompatType(mimeType);
                if (!TextUtils.isEmpty(compatType)) {//尝试使用兼容的mimeType重试一次

                    boolean secondOpenResult = openFileInner(context, file, compatType);
                    if (!secondOpenResult) {
                        Toast.showFailureText(context, context.getString(R.string.Drive_FilePicker_OpenNoApp), Toast.LENGTH_SHORT);
                    }

                } else {
                    Toast.showFailureText(context, context.getString(R.string.Drive_FilePicker_OpenNoApp), Toast.LENGTH_SHORT);
                }
            }
        } else {
            Toast.showFailureText(context, context.getString(R.string.Drive_FilePicker_OpenFail), Toast.LENGTH_SHORT);
        }
    }

    /**
     * MimeType兼容类
     */
    private static class MimeTypeCompat {

        private static final String[][] COMPAT_MIME_PAIR = {
                {"application/zip", "application/x-zip-compressed"}//zip的两种mimeType
        };

        private static final Map<String, String> COMPAT_MIME_MAP = new HashMap<String, String>() {
            {
                for (String[] strings : COMPAT_MIME_PAIR) {
                    put(strings[0], strings[1]);
                    put(strings[1], strings[0]);
                }
            }
        };

        private String getCompatType(String type) {
            return COMPAT_MIME_MAP.get(type);
        }
    }

    /**
     * 打开文件
     *
     * @param context
     * @param file
     * @param mimeType
     * @return
     */
    private static boolean openFileInner(Context context, File file, String mimeType) {
        //如果外部服务未提供mimeType，使用本地文件名解析
        if (TextUtils.isEmpty(mimeType)) {
            mimeType = getMIMEType(file);
        }

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(UriUtil.getUriForFile(context, file), mimeType);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        }
        try {
            context.startActivity(intent);
        } catch (Exception e) {
            Log.w(TAG, "openFile: " + e.getMessage());
            return false;
        }
        return true;
    }

    public static String getMIMEType(File f) {
        return getMIMEType(f.getPath());
    }


    public static String getMIMEType(String filePath) {
        // 提前加判空s
        if (TextUtils.isEmpty(filePath)) {
            return "*/*";
        }

        String end = getFileExtension(filePath);

        String type = EXTENSION_TO_MIME_MAP.get(end);
        if (TextUtils.isEmpty(type)) {
            type = "*/*";
        }

        return type;
    }

    public static String getFileExtension(String filePath) {
        String end = "";
        if (filePath.lastIndexOf(".") > 0) {
            end = filePath.substring(filePath.lastIndexOf(".") + 1).toLowerCase();
        }
        return end;
    }

    /**
     * Method that deletes a folder recursively
     *
     * @param folder The folder to delete
     * @return boolean If the folder was deleted
     */
    public static boolean deleteFolder(File folder) {
        File[] files = folder.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    if (!deleteFolder(file)) {
                        return false;
                    }
                } else {
                    if (!file.delete()) {
                        return false;
                    }
                }
            }
        }
        return folder.delete();
    }

    /**
     * Method that returns the canonical/absolute path of the path.<br/>
     * This method performs path resolution
     *
     * @param path The path to convert
     * @return String The canonical/absolute path
     */
    public static String getAbsPath(String path) {
        try {
            return new File(path).getCanonicalPath();
        } catch (Exception e) {
            return new File(path).getAbsolutePath();
        }
    }

    /**
     * Method that returns the .nomedia file
     *
     * @param fso The folder that contains the .nomedia file
     * @return File The .nomedia file
     */
    public static File getNoMediaFile(FileSystemObject fso) {
        File file = null;
        try {
            file = new File(fso.getFullPath()).getCanonicalFile();
        } catch (Exception e) {
            file = new File(fso.getFullPath()).getAbsoluteFile();
        }
        return new File(file, ".nomedia").getAbsoluteFile(); //$NON-NLS-1$
    }

    /**
     * Method that create a new temporary filename
     *
     * @param external If the file should be created in the external or the internal cache dir
     */
    public static synchronized File createTempFilename(Context context, boolean external) {
        File tempDirectory = external ? context.getExternalCacheDir() : context.getCacheDir();
        File tempFile;
        do {
            UUID uuid = UUID.randomUUID();
            tempFile = new File(tempDirectory, uuid.toString());
        } while (tempFile.exists());
        return tempFile;
    }

    /**
     * Method that delete a file or a folder
     *
     * @param src The file or folder to delete
     * @return boolean If the operation was successfully
     */
    public static boolean deleteFileOrFolder(File src) {
        if (src.isDirectory()) {
            return FileHelper.deleteFolder(src);
        }
        return src.delete();
    }

    /**
     * Method that checks if the source file passed belongs to (is under) the directory passed
     *
     * @param src The file to check
     * @param dir The parent file to check
     * @return boolean If the source belongs to the directory
     */
    public static boolean belongsToDirectory(File src, File dir) {
        if (dir.isFile()) {
            return false;
        }
        return src.getAbsolutePath().startsWith(dir.getAbsolutePath());
    }

    /**
     * Method that checks if both path are the same (by checking sensitive cases).
     *
     * @param src The source path
     * @param dst The destination path
     * @return boolean If both are the same path
     */
    public static boolean isSamePath(String src, String dst) {
        // This is only true if both are exactly the same path or the same file in insensitive
        // file systems
        File o1 = new File(src);
        File o2 = new File(dst);
        return o1.equals(o2);
    }
}
