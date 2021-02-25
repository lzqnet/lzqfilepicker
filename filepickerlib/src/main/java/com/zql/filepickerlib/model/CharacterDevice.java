package com.zql.filepickerlib.model;

import java.util.Date;

/**
 * A class that represents a block device.
 * <p>
 * {@link "http://en.wikipedia.org/wiki/Character_special_file#Character_devices"}
 */
public class CharacterDevice extends SystemFile {

    /**
     * The unix identifier of the object.
     *
     * @hide
     */
    public static final char UNIX_ID = 'c';
    private static final long serialVersionUID = -3585051204874199619L;

    /**
     * Constructor of <code>CharacterDevice</code>.
     *
     * @param name             The name of the object
     * @param parent           The parent folder of the object
     * @param permissions      The permissions of the object
     * @param lastAccessedTime The last time that the object was accessed
     * @param lastModifiedTime The last time that the object was modified
     * @param lastChangedTime  The last time that the object was changed
     */
    public CharacterDevice(
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
        return "CharacterDevice [type=" + super.toString() + "]";  //$NON-NLS-1$//$NON-NLS-2$
    }

}
