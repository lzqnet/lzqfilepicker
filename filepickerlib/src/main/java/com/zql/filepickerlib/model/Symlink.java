package com.zql.filepickerlib.model;

import java.util.Date;

/**
 * A class that represents a symbolic link.
 * <p>
 * {@link "http://en.wikipedia.org/wiki/Symbolic_link"}
 */
public class Symlink extends FileSystemObject {

    /**
     * The unix identifier of the object.
     *
     * @hide
     */
    public static final char UNIX_ID = 'l';
    private static final long serialVersionUID = -6411787401264288389L;
    private String mLink;
    private FileSystemObject mLinkRef;

    /**
     * Constructor of <code>Symlink</code>.
     *
     * @param name             The name of the object
     * @param link             The real file that this symlink is point to
     * @param parent           The parent folder of the object
     * @param permissions      The permissions of the object
     * @param lastAccessedTime The last time that the object was accessed
     * @param lastModifiedTime The last time that the object was modified
     * @param lastChangedTime  The last time that the object was changed
     */
    public Symlink(String name, String link, String parent, Permissions permissions,
                   Date lastAccessedTime, Date lastModifiedTime, Date lastChangedTime) {
        super(name, parent, permissions, 0L,
                lastAccessedTime, lastModifiedTime, lastChangedTime);
        this.mLink = link;
    }

    /**
     * Method that returns the real file that this symlink is point to.
     *
     * @return String The real file that this symlink is point to.
     */
    public String getLink() {
        return this.mLink;
    }

    /**
     * Method that sets the real file that this symlink is point to.
     *
     * @param link the real file that this symlink is point to
     */
    public void setLink(String link) {
        this.mLink = link;
    }

    /**
     * Method that returns the {@link FileSystemObject} reference of the symlink.
     *
     * @return FileSystemObject The {@link FileSystemObject} reference of the symlink
     */
    public FileSystemObject getLinkRef() {
        return this.mLinkRef;
    }

    /**
     * Method that sets the {@link FileSystemObject} reference of the symlink.
     *
     * @param linkRef The {@link FileSystemObject} reference of the symlink
     */
    public void setLinkRef(FileSystemObject linkRef) {
        this.mLinkRef = linkRef;
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
        return "Symlink [type=" + super.toString() + ", link=" //$NON-NLS-1$//$NON-NLS-2$
                + this.mLink + "]";  //$NON-NLS-1$
    }

}
