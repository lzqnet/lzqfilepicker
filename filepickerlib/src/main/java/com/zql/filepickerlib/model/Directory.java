package com.zql.filepickerlib.model;


import java.util.Date;

/**
 * A class that represents a directory.
 */
public class Directory extends FileSystemObject {

    /**
     * The unix identifier of the object.
     *
     * @hide
     */
    public static final char UNIX_ID = 'd';
    private static final long serialVersionUID = -3975569940766905884L;

    /**
     * Constructor of <code>Directory</code>.
     *
     * @param name             The name of the object
     * @param parent           The parent folder of the object
     * @param permissions      The permissions of the object
     * @param lastAccessedTime The last time that the object was accessed
     * @param lastModifiedTime The last time that the object was modified
     * @param lastChangedTime  The last time that the object was changed
     */
    public Directory(String name, String parent, Permissions permissions,
                     Date lastAccessedTime, Date lastModifiedTime, Date lastChangedTime) {
        super(name, parent, permissions, 0L,
                lastAccessedTime, lastModifiedTime, lastChangedTime);
    }

    public Directory() {

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
        return "Directory [type=" + super.toString() + "]";  //$NON-NLS-1$//$NON-NLS-2$
    }

}
