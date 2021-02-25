package com.zql.filepickerlib.util;

import android.content.Context;


import com.zql.filepickerlib.config.DisplayRestrictions;
import com.zql.filepickerlib.config.FilePickerSettings;
import com.zql.filepickerlib.config.NavigationSortMode;
import com.zql.filepickerlib.mimeType.MimeTypeCategory;
import com.zql.filepickerlib.mimeType.MimeTypeHelper;
import com.zql.filepickerlib.model.FileSystemObject;
import com.zql.filepickerlib.model.Symlink;
import com.zql.filepickerlib.model.SystemFile;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class RestrictionHelper {

    /**
     * Method that applies the configuration modes to the listed files
     * (sort mode, hidden files, ...).
     *
     * @param files        The listed files
     * @param restrictions The restrictions to apply when displaying files
     * @param noSort       If sort must be applied
     * @return List<FileSystemObject> The applied mode listed files
     */
    public static List<FileSystemObject> applyUserPreferences(List<FileSystemObject> files,
                                                              Map<DisplayRestrictions, Object> restrictions,
                                                              boolean noSort,
                                                              final Context context) {
        //Retrieve user preferences
        FilePickerSettings sortModePref = FilePickerSettings.SETTINGS_SORT_MODE;
        FilePickerSettings showDirsFirstPref = FilePickerSettings.SETTINGS_SHOW_DIRS_FIRST;
        FilePickerSettings showHiddenPref = FilePickerSettings.SETTINGS_SHOW_HIDDEN;
        FilePickerSettings showSystemPref = FilePickerSettings.SETTINGS_SHOW_SYSTEM;
        FilePickerSettings showSymlinksPref = FilePickerSettings.SETTINGS_SHOW_SYMLINKS;

        //Remove all unnecessary files (no required by the user)
        int cc = files.size();
        for (int i = cc - 1; i >= 0; i--) {
            FileSystemObject file = files.get(i);

            //Hidden files
            if (!(boolean) showHiddenPref.getDefaultValue()) {
                if (file.isHidden()) {
                    files.remove(i);
                    continue;
                }
            }

            //System files
            if (!(Boolean) showSystemPref.getDefaultValue()) {
                if (file instanceof SystemFile) {
                    files.remove(i);
                    continue;
                }
            }

            //Symlinks files
            if (!(Boolean) showSymlinksPref.getDefaultValue()) {
                if (file instanceof Symlink) {
                    files.remove(i);
                    continue;
                }
            }

            // Restrictions (only apply to files)
            if (restrictions != null) {
                if (!FileHelper.isDirectory(file)) {
                    if (!isDisplayAllowed(file, restrictions)) {
                        files.remove(i);
                        continue;
                    }
                }
            }
        }
        //Apply sort mode
        if (!noSort) {
            final boolean showDirsFirst = (Boolean) showDirsFirstPref.getDefaultValue();
            final NavigationSortMode sortMode = (NavigationSortMode) sortModePref.getDefaultValue();
            Collections.sort(files, (lhs, rhs) -> {
                if (showDirsFirst) {
                    boolean isLhsDirectory = FileHelper.isDirectory(lhs);
                    boolean isRhsDirectory = FileHelper.isDirectory(rhs);
                    if (isLhsDirectory || isRhsDirectory) {
                        if (isLhsDirectory && isRhsDirectory) {
                            //Apply sort mode
                            return CompareHelper.doCompare(lhs, rhs, sortMode);
                        }
                        return (isLhsDirectory) ? -1 : 1;
                    }
                }
                //Apply sort mode
                return CompareHelper.doCompare(lhs, rhs, sortMode);
            });
        }
        //Return the files
        return files;
    }

    /**
     * Method that check if a file should be displayed according to the restrictions
     *
     * @param fso          The file system object to check
     * @param restrictions The restrictions map
     * @return boolean If the file should be displayed
     */
    private static boolean isDisplayAllowed(FileSystemObject fso, Map<DisplayRestrictions, Object> restrictions) {
        Iterator<DisplayRestrictions> it = restrictions.keySet().iterator();
        while (it.hasNext()) {
            DisplayRestrictions restriction = it.next();
            Object value = restrictions.get(restriction);
            if (value == null) {
                continue;
            }
            switch (restriction) {
                case CATEGORY_TYPE_RESTRICTION:
                    if (value instanceof MimeTypeCategory) {
                        MimeTypeCategory cat1 = (MimeTypeCategory) value;
                        // NOTE: We don't need the context here, because mime-type
                        // database should be loaded prior to this call
                        MimeTypeCategory cat2 = MimeTypeHelper.getMimeTypeHelperInstance().getCategory(null, fso);
                        if (cat1.compareTo(cat2) != 0) {
                            return false;
                        }
                    }
                    break;

                case MIME_TYPE_RESTRICTION:
                    String[] mimeTypes = null;
                    if (value instanceof String) {
                        mimeTypes = new String[]{(String) value};
                    } else if (value instanceof String[]) {
                        mimeTypes = (String[]) value;
                    }
                    if (mimeTypes != null) {
                        boolean matches = false;
                        for (String mimeType : mimeTypes) {
                            if (mimeType.compareTo(MimeTypeHelper.ALL_MIME_TYPES) == 0) {
                                matches = true;
                                break;
                            }
                            // NOTE: We don't need the context here, because mime-type
                            // database should be loaded prior to this call
                            if (MimeTypeHelper.getMimeTypeHelperInstance().matchesMimeType(fso, mimeType)) {
                                matches = true;
                                break;
                            }
                        }
                        if (!matches) {
                            return false;
                        }
                    }
                    break;

                case SIZE_RESTRICTION:
                    if (value instanceof Long) {
                        Long maxSize = (Long) value;
                        if (fso.getSize() > maxSize.longValue()) {
                            return false;
                        }
                    }
                    break;

                case DIRECTORY_ONLY_RESTRICTION:
                    if (value instanceof Boolean) {
                        Boolean directoryOnly = (Boolean) value;
                        if (directoryOnly.booleanValue() && !FileHelper.isDirectory(fso)) {
                            return false;
                        }
                    }
                    break;

                case LOCAL_FILESYSTEM_ONLY_RESTRICTION:
                    if (value instanceof Boolean) {
                        Boolean localOnly = (Boolean) value;
                        if (localOnly.booleanValue()) {
                            /** TODO Needed when CMFM gets networking **/
                        }
                    }
                    break;

                default:
                    break;
            }
        }
        return true;
    }
}
