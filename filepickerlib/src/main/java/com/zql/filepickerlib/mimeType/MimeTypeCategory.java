package com.zql.filepickerlib.mimeType;


public enum MimeTypeCategory {
    /**
     * No category
     */
    NONE,
    /**
     * System file
     */
    SYSTEM,
    /**
     * Application, Installer, ...
     */
    APP,
    /**
     * Binary file
     */
    BINARY,
    /**
     * Text file
     */
    TEXT,
    /**
     * Document file (text, spreedsheet, presentation, pdf, ...)
     */
    DOCUMENT,
    /**
     * e-Book file
     */
    EBOOK,
    /**
     * Mail file (email, message, contact, calendar, ...)
     */
    MAIL,
    /**
     * Compressed file
     */
    COMPRESS,
    /**
     * Executable file
     */
    EXEC,
    /**
     * Database file
     */
    DATABASE,
    /**
     * Font file
     */
    FONT,
    /**
     * Image file
     */
    IMAGE,
    /**
     * Audio file
     */
    AUDIO,
    /**
     * Video file
     */
    VIDEO,
    /**
     * Security file (certificate, keys, ...)
     */
    SECURITY;

    public static String[] names() {
        MimeTypeCategory[] categories = values();
        String[] names = new String[categories.length];

        for (int i = 0; i < categories.length; i++) {
            names[i] = categories[i].name();
        }

        return names;
    }
}
