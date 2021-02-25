package com.zql.filepickerlib.model;

import java.util.Date;

/**
 * A class that represents a system file (files with some system functionality).
 *
 * @see BlockDevice
 * @see CharacterDevice
 * @see NamedPipe
 * @see DomainSocket
 */
public abstract class SystemFile extends FileSystemObject {

    private static final long serialVersionUID = -1396396017050697459L;

    /**
     * Constructor of <code>SystemFile</code>.
     *
     * @param name             The name of the object
     * @param parent           The parent folder of the object
     * @param permissions      The permissions of the object
     * @param size             The size in bytes of the object
     * @param lastAccessedTime The last time that the object was accessed
     * @param lastModifiedTime The last time that the object was modified
     * @param lastChangedTime  The last time that the object was changed
     */
    public SystemFile(
            String name, String parent,
            Permissions permissions, long size,
            Date lastAccessedTime, Date lastModifiedTime, Date lastChangedTime) {
        super(name, parent, permissions, size,
                lastAccessedTime, lastModifiedTime, lastChangedTime);
    }
}
