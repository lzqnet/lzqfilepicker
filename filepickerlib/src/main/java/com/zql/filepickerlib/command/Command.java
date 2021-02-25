package com.zql.filepickerlib.command;

import java.util.concurrent.ExecutionException;

/**
 * An abstract base class for all java executables.
 */
public abstract class Command<T> implements Executable {

    private boolean mTrace;
    private int mBufferSize;
    private boolean mCancelled = false;

    /**
     * Constructor of <code>Program</code>
     */
    public Command() {
        super();
    }

    /**
     * Method that return if the command has to trace his operations
     *
     * @return boolean If the command has to trace
     */
    public boolean isTrace() {
        return this.mTrace;
    }

    /**
     * Method that sets if the command has to trace his operations
     *
     * @param trace If the command has to trace
     */
    public void setTrace(boolean trace) {
        this.mTrace = trace;
    }

    /**
     * Method that return the buffer size of the program
     *
     * @return int The buffer size of the program
     */
    public int getBufferSize() {
        return this.mBufferSize;
    }

    /**
     * Method that sets the buffer size of the program
     *
     * @param bufferSize The buffer size of the program
     */
    public void setBufferSize(int bufferSize) {
        this.mBufferSize = bufferSize;
    }

    /**
     * Method that returns if this program uses an asynchronous model. <code>false</code>
     * by default.
     *
     * @return boolean If this program uses an asynchronous model
     */
    @SuppressWarnings("static-method")
    public boolean isAsynchronous() {
        return false;
    }

    /**
     * Method that executes the program
     *
     * @throws NoSuchFileOrDirectoryException If the file or directory was not found
     * @throws ExecutionException    If the operation returns a invalid exit code
     */
    public abstract T execute(String src)
            throws NoSuchFileOrDirectoryException, ExecutionException;

    public void requestCancel() {
        mCancelled = true;
    }

    public boolean isCancelled() {
        return mCancelled;
    }
}
