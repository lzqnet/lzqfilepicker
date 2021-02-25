package com.zql.filepickerlib.model;

/**
 * A class that represents a group of the operating system.
 */
public class Group extends AID {

    private static final long serialVersionUID = -6087834824505714560L;

    /**
     * Constructor of <code>Group</code>.
     *
     * @param gid  The group identifier
     * @param name The group name
     */
    public Group(int gid, String name) {
        super(gid, name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "Group [" + super.toString() + "]"; //$NON-NLS-1$ //$NON-NLS-2$
    }

}
