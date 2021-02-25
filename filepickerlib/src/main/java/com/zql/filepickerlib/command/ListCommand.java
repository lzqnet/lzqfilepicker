package com.zql.filepickerlib.command;



import com.zql.filepickerlib.model.FileSystemObject;
import com.zql.filepickerlib.util.FileHelper;
import com.zql.filepickerlib.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


/**
 * A class for list information about files and directories.
 */
public class ListCommand extends Command {

    private static final String TAG = "ListCommand"; //$NON-NLS-1$

    /**
     * Constructor of <code>ListCommand</code>. List mode.
     */
    public ListCommand() {
        super();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public List<FileSystemObject> execute(String src) throws NoSuchFileOrDirectoryException {
        if (isTrace()) {
            Log.v(TAG,
                    String.format("Listing %s.", //$NON-NLS-1$
                            src));
        }
        List<FileSystemObject> fileList = new ArrayList<FileSystemObject>();

        File f = new File(src);
//        Log.d("lzqtest","ListCommand.java.execute: 42canRead= "+f.canRead()+"isDirectory="+f.isDirectory());
        if (!f.exists()) {
            if (isTrace()) {
                Log.v(TAG, "Result: FAIL. NoSuchFileOrDirectory"); //$NON-NLS-1$
            }
            throw new NoSuchFileOrDirectoryException(src);
        }
        File[] files = f.listFiles();
        if (files != null) {
            for (File file : files) {
                FileSystemObject fso = FileHelper.createFileSystemObject(file);
                if (fso != null) {
                    if (isTrace()) {
                        Log.v(TAG, String.valueOf(fso));
                    }
                    fileList.add(fso);
                }
            }
        }

        if (isTrace()) {
            Log.v(TAG, "Result: OK"); //$NON-NLS-1$
        }

        return fileList;
    }

}
