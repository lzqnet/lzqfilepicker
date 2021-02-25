package com.zql.filepickerlib.config;

/**
 * An enumeration of the restrictions that can be applied when displaying list of files.
 */
public enum DisplayRestrictions {
    /**
     * Restriction for display only files with the category.
     */
    CATEGORY_TYPE_RESTRICTION,
    /**
     * Restriction for display only files with these mime/types (this restriction
     * accepts a String or String[] as parameter).
     */
    MIME_TYPE_RESTRICTION,
    /**
     * Restriction for display only files with a size lower than the specified
     */
    SIZE_RESTRICTION,
    /**
     * Restriction for display only directories
     */
    DIRECTORY_ONLY_RESTRICTION,
    /**
     * Restriction for display only files from the local file system. Avoid remote files.
     */
    LOCAL_FILESYSTEM_ONLY_RESTRICTION
}
