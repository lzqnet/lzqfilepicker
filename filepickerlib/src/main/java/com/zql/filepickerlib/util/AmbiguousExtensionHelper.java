package com.zql.filepickerlib.util;

import android.media.MediaMetadataRetriever;
import android.util.Log;

import java.util.HashMap;

/**
 * Provides the ability to determine the mimetype of a known file extension that can support
 * multiple mimetypes.
 */
public abstract class AmbiguousExtensionHelper {
    /**
     * All available ambiguous extension helpers.
     */
    public static final HashMap<String, AmbiguousExtensionHelper> AMBIGUOUS_EXTENSIONS_MAP = new HashMap<>();

    static {
        addAmbiguousHelperToMap(new ThreeGPExtensionHelper());
    }

    private static void addAmbiguousHelperToMap(AmbiguousExtensionHelper instance) {
        for (String extension : instance.getSupportedExtensions()) {
            AmbiguousExtensionHelper.AMBIGUOUS_EXTENSIONS_MAP.put(extension, instance);
        }
    }

    public abstract String getMimeType(String absolutePath, String extension);

    public abstract String[] getSupportedExtensions();

    /**
     * An AmbiguousExtensionHelper subclass that can distinguish the mimetype of a given
     * .g3p, .g3pp, .3g2 or .3gpp2 file. The 3GP and 3G2 file formats support both audio and
     * video, and a file with that extension has the possibility of multiple mimetypes, depending
     * on the content of the file.
     */
    public static class ThreeGPExtensionHelper extends AmbiguousExtensionHelper {
        public static final String VIDEO_3GPP_MIME_TYPE = "video/3gpp";
        public static final String AUDIO_3GPP_MIME_TYPE = "audio/3gpp";
        public static final String VIDEO_3GPP2_MIME_TYPE = "video/3gpp2";
        public static final String AUDIO_3GPP2_MIME_TYPE = "audio/3gpp2";
        private static final String TAG = "ThreeGPExtensionHelper";
        private static final String[] sSupportedExtensions = {"3gp", "3gpp", "3g2", "3gpp2"};

        @Override
        public String getMimeType(String absolutePath, String extension) {
            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
            try {
                retriever.setDataSource(absolutePath);
                boolean hasVideo =
                        retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_HAS_VIDEO) !=
                                null;
                if (is3GPP(extension)) {
                    return hasVideo ? VIDEO_3GPP_MIME_TYPE : AUDIO_3GPP_MIME_TYPE;
                } else if (is3GPP2(extension)) {
                    return hasVideo ? VIDEO_3GPP2_MIME_TYPE : AUDIO_3GPP2_MIME_TYPE;
                }
            } catch (RuntimeException e) {
                Log.e(TAG, "Unable to open 3GP file to determine mimetype");
            } finally {
                retriever.release();
            }
            // Default to video 3gp if the file is unreadable as this was the default before
            // ambiguous resolution support was added.
            return VIDEO_3GPP_MIME_TYPE;
        }

        @Override
        public String[] getSupportedExtensions() {
            return sSupportedExtensions;
        }

        private boolean is3GPP(String ext) {
            return "3gp".equals(ext) || "3gpp".equals(ext);
        }

        private boolean is3GPP2(String ext) {
            return "3g2".equals(ext) || "3gpp2".equals(ext);
        }
    }
}
