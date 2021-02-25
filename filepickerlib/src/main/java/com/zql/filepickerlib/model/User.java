package com.zql.filepickerlib.model;

/**
 * A class that represents a user of the operating system.
 */
public class User extends AID {

    private static final long serialVersionUID = 8250909336356908786L;

    /**
     * Constructor of <code>User</code>.
     *
     * @param uid  The user identifier
     * @param name The user name
     */
    public User(int uid, String name) {
        super(uid, name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "User [" + super.toString() + "]"; //$NON-NLS-1$ //$NON-NLS-2$
    }

}
