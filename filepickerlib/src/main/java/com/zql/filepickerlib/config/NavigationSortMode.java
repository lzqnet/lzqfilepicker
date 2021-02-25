package com.zql.filepickerlib.config;

/**
 * An enumeration of the navigation sort modes.
 */
public enum NavigationSortMode {

    /**
     * That mode sorts objects by name (ascending).
     */
    NAME_ASC(0),
    /**
     * That mode sorts objects by name (descending).
     */
    NAME_DESC(1),
    /**
     * That mode sorts objects by date (ascending).
     */
    DATE_ASC(2),
    /**
     * That mode sorts objects by date (descending).
     */
    DATE_DESC(3),
    /**
     * That mode sorts objects by size (ascending).
     */
    SIZE_ASC(4),
    /**
     * That mode sorts objects by size (descending).
     */
    SIZE_DESC(5),
    /**
     * That mode sorts objects by type (ascending).
     */
    TYPE_ASC(6),
    /**
     * That mode sorts objects by type (descending).
     */
    TYPE_DESC(7);

    private int mId;

    /**
     * Constructor of <code>NavigationSortMode</code>.
     *
     * @param id The unique identifier of the enumeration
     */
    private NavigationSortMode(int id) {
        this.mId = id;
    }

}
