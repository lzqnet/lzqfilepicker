package com.zql.filepickerlib.model;


import com.zql.filepickerlib.util.FileHelper;

/**
 * A class that represents a link to the parent directory.
 */
public class ParentDirectory extends Directory {

    private static final long serialVersionUID = -3818276335217197479L;

    /**
     * Constructor of <code>ParentDirectory</code>.
     *
     * @param parent The parent folder of the object
     */
    public ParentDirectory(String parent) {
        super(FileHelper.PARENT_DIRECTORY, parent, null, null, null, null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isHidden() {
        return false;
    }

}
