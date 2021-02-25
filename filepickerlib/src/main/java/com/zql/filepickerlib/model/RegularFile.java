package com.zql.filepickerlib.model;

import java.util.Date;

/**
 * A class that represents a regular file.
 */
public class RegularFile extends FileSystemObject {

    /**
     * The unix identifier of the object.
     *
     * @hide
     */
    public static final char UNIX_ID = '-';
    private static final long serialVersionUID = 7113562456595400525L;

    /**
     * Constructor of <code>RegularFile</code>.
     *
     * @param name             The name of the object
     * @param parent           The parent folder of the object
     * @param permissions      The permissions of the object
     * @param size             The size in bytes of the object
     * @param lastAccessedTime The last time that the object was accessed
     * @param lastModifiedTime The last time that the object was modified
     * @param lastChangedTime  The last time that the object was changed
     */
    public RegularFile(String name, String parent,
                       Permissions permissions, long size,
                       Date lastAccessedTime, Date lastModifiedTime, Date lastChangedTime) {
        super(name, parent, permissions, size,
                lastAccessedTime, lastModifiedTime, lastChangedTime);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public char getUnixIdentifier() {
        return UNIX_ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "RegularFile [type=" + super.toString() + "]";  //$NON-NLS-1$//$NON-NLS-2$
    }

}
