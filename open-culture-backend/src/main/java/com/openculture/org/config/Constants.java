package com.openculture.org.config;

/**
 * Application constants.
 */
public final class Constants {

    // Regex for acceptable logins
    public static final String LOGIN_REGEX = "^(?>[a-zA-Z0-9!$&*+=?^_`{|}~.-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*)|(?>[_.@A-Za-z0-9-]+)$";

    public static final String SYSTEM_ACCOUNT = "system";
    public static final String DEFAULT_LANGUAGE = "en";
    public static final String ANONYMOUS_USER = "anonymoususer";

    public static final String HEADER_CONTENT_TYPE = "Content-Type";
    public static final String HEADER_CONTENT_LENGTH = "Content-Length";
    public static final String HEADER_ACCEPT_RANGES = "Accept-Ranges";
    public static final String HEADER_CONTENT_RANGE = "Content-Range";
    public static final String CONTENT_TYPE_MP4 = "video/mp4";
    public static final String CONTENT_TYPE_FLV = "video/x-flv";
    public static final String CONTENT_TYPE_AVI = "video/x-msvideo";
    public static final String CONTENT_TYPE_WMV = "video/x-ms-wmv";
    public static final String CONTENT_TYPE_MP3 = "audio/x-mpeg-3";
    public static final String CONTENT_TYPE_MP2  = "audio/x-mpeg";




    private Constants() {
    }
}
