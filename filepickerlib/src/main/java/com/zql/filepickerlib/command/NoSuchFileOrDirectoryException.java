package com.zql.filepickerlib.command;

/**
 * An exception thrown when the file or directory is not found.
 */
public class NoSuchFileOrDirectoryException extends Exception {

    private static final long serialVersionUID = 8601894104043734066L;
    private String path;

    /**
     * Constructor of <code>NoSuchFileOrDirectory</code>.
     */
    public NoSuchFileOrDirectoryException() {
        super();
    }

    /**
     * Constructor of <code>NoSuchFileOrDirectory</code>.
     *
     * @param src The file or directory not found
     */
    public NoSuchFileOrDirectoryException(String src) {
        super(src);
        this.path = src;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
