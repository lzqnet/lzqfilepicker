package com.zql.filepickerlib.model;

import java.util.Date;

/**
 * A class that represents a symbolic link.
 * <p>
 * {@link "http://en.wikipedia.org/wiki/Named_pipe"}
 */
public class NamedPipe extends SystemFile {

    /**
     * The unix identifier of the object.
     *
     * @hide
     */
    public static final char UNIX_ID = 'p';
    private static final long serialVersionUID = -5199356055601688190L;

    /**
     * Constructor of <code>NamedPipe</code>.
     *
     * @param name             The name of the object
     * @param parent           The parent folder of the object
     * @param permissions      The permissions of the object
     * @param lastAccessedTime The last time that the object was accessed
     * @param lastModifiedTime The last time that the object was modified
     * @param lastChangedTime  The last time that the object was changed
     */
    public NamedPipe(
            String name, String parent, Permissions permissions,
            Date lastAccessedTime, Date lastModifiedTime, Date lastChangedTime) {
        super(name, parent, permissions, 0L,
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
        return "NamedPipe [type=" + super.toString() + "]";  //$NON-NLS-1$//$NON-NLS-2$
    }

}
