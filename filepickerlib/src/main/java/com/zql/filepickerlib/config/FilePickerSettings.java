package com.zql.filepickerlib.config;


/**
 * The enumeration of settings of FileManager application.
 */
public enum FilePickerSettings {
    /**
     * The sort mode to use (name or data, ascending or descending).
     *
     * @hide
     */
    SETTINGS_SORT_MODE("sort_mode", NavigationSortMode.NAME_ASC),

    /**
     * When to sort the directories before the files.
     *
     * @hide
     */
    SETTINGS_SHOW_DIRS_FIRST("show_dirs_first", Boolean.TRUE),
    /**
     * When to show the hidden files.
     *
     * @hide
     */
    SETTINGS_SHOW_HIDDEN("show_hidden", Boolean.FALSE),
    /**
     * When to show the system files.
     *
     * @hide
     */
    SETTINGS_SHOW_SYSTEM("show_system", Boolean.FALSE),
    /**
     * When to show the symlinks files.
     *
     * @hide
     */
    SETTINGS_SHOW_SYMLINKS("show_symlinks", Boolean.FALSE);

    private final String mId;
    private final Object mDefaultValue;

    /**
     * Constructor of <code>FileManagerSettings</code>.
     *
     * @param id           The unique identifier of the setting
     * @param defaultValue The default value of the setting
     */
    private FilePickerSettings(String id, Object defaultValue) {
        this.mId = id;
        this.mDefaultValue = defaultValue;
    }


    /**
     * Method that returns the unique identifier of the setting.
     *
     * @return the mId
     */
    public String getId() {
        return this.mId;
    }

    /**
     * Method that returns the default value of the setting.
     *
     * @return Object The default value of the setting
     */
    public Object getDefaultValue() {
        return this.mDefaultValue;
    }
}
