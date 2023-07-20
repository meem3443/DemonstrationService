package com.police.demonstrationservice;

import java.util.Map;

public class Constants {
    public static final String PACKAGE_NAME = "com.police.demonstrationservice.fileprovider";
    public static final String API_SUNSET_BASE_URL = "https://apis.data.go.kr/";

    public static final String SHARED_PREFERENCES_RECORD_DATE_KEY = "recordDate";
    public static final String SHARED_PREFERENCES_DATE_KEY = "date";
    public static final String SHARED_PREFERENCES_SUNRISE_KEY = "sunrise";
    public static final String SHARED_PREFERENCES_SUNSET_KEY = "sunset";
    public static final String SHARED_PREFERENCES_LOCATION = "location";

    public static final String SHARED_PREFERENCES_MESSAGE_KEY = "messageKey";
    public static final String SHARED_PREFERENCES_NOTIFICATION_KEY = "notification";
    public static final String SHARED_PREFERENCES_DOCUMENT_KEY = "document";

    public static final String SHARED_PREFERENCES_RECENT_KEY = "recnet";
    public static final String SHARED_PREFERENCES_RECENT_HOUR_KEY = "hour";
    public static final String SHARED_PREFERENCES_RECENT_MINUTE_KEY = "minute";

    // DATE format
    public final static String SIMPLE_DATE_FORMAT = "yyyy-MM-dd-HH-mm-ss";
    public final static String YEAR_DATE_FORMAT = "yyyy";
    public final static String YEAR_MONTH_DAY_DATE_FORMAT = "yyyy-MM-dd";
    public final static String HOUR_MINUTE = "HH-mm";

    // Fragment 화면 타입
    public static final int FRAGMENT_TYPE_INPUT_MEASUREMENT = 0;
    public static final int FRAGMENT_TYPE_TEMPORARY = 1;

    // 소음도 타입
    public static final int NOISE_TYPE_EQUIVALENT = 0;
    public static final int NOISE_TYPE_HIGHEST = 1;


    // 고지 타입 (안내문, 최고소음 초과(유지), 등가소음 초과(유지), 최고소음 위반(유지)
    public static final int NOTIFICATION_TYPE_NOT = 0;
    public static final int NOTIFICATION_TYPE_FIRST = 1;
    public static final int NOTIFICATION_TYPE_SECOND = 2;
    public static final int NOTIFICATION_TYPE_THIRD = 3;
    public static final int NOTIFICATION_TYPE_FOURTH = 4;
    public static final int NOTIFICATION_TYPE_FIFTH = 5;
    public static final int NOTIFICATION_TYPE_SIXTH = 6;
    public static final int NOTIFICATION_TYPE_SEVENTH = 7;
    public static final int NOTIFICATION_TYPE_EIGHTH = 8;
    public static final int NOTIFICATION_TYPE_NINTH = 9;
    public static final int NOTIFICATION_TYPE_TENTH = 10;
    public static final int NOTIFICATION_TYPE_ELEVEN = 11;

    // 기본 등가 소음
    public static final int DEFAULT_EQUIVALENT_NOISE_DAY_HOME = 65;
    public static final int DEFAULT_EQUIVALENT_NOISE_NIGHT_HOME = 60;
    public static final int DEFAULT_EQUIVALENT_NOISE_LATE_NIGHT_HOME = 55;
    public static final int DEFAULT_EQUIVALENT_NOISE_DAY_PUBLIC = 65;
    public static final int DEFAULT_EQUIVALENT_NOISE_NIGHT_PUBLIC = 60;
    public static final int DEFAULT_EQUIVALENT_NOISE_LATE_NIGHT_PUBLIC = 60;
    public static final int DEFAULT_EQUIVALENT_NOISE_DAY_ETC = 75;
    public static final int DEFAULT_EQUIVALENT_NOISE_NIGHT_ETC = 65;
    public static final int DEFAULT_EQUIVALENT_NOISE_LATE_NIGHT_ETC = 65;

    // 기본 최고 소음
    public static final int DEFAULT_HIGHEST_NOISE_DAY_HOME = 85;
    public static final int DEFAULT_HIGHEST_NOISE_NIGHT_HOME = 80;
    public static final int DEFAULT_HIGHEST_NOISE_LATE_NIGHT_HOME = 75;
    public static final int DEFAULT_HIGHEST_NOISE_DAY_PUBLIC = 85;
    public static final int DEFAULT_HIGHEST_NOISE_NIGHT_PUBLIC = 80;
    public static final int DEFAULT_HIGHEST_NOISE_LATE_NIGHT_PUBLIC = 80;
    public static final int DEFAULT_HIGHEST_NOISE_ETC = 95;

    // Intent Name
    public static final String INTENT_NAME_MESSAGE = "message";
    public static final String INTENT_NAME_NOTIFICATION_TYPE = "notificationType";
    public static final String INTENT_NAME_REQUEST_INFO = "requestInfo";
    public static final String INTENT_NAME_ORGANIZER_PHONE_NUMBER = "organizerPhoneNumber";
    public static final String INTENT_NAME_CURRENT = "current";
    public static final String INTENT_NAME_DEFAULT_MESSAGE = "defaultMessage";

    // 소음도 보정치 기준표
    public static Map<Double, Double> STANDARD_CORRECTION_NOISE = Map.<Double, Double>ofEntries(
            Map.entry(3.0, -3.0), Map.entry(3.1, -2.9),
            Map.entry(3.2, -2.8), Map.entry(3.3, -2.7),
            Map.entry(3.4, -2.7), Map.entry(3.5, -2.6),
            Map.entry(3.6, -2.5), Map.entry(3.7, -2.4),
            Map.entry(3.8, -2.3), Map.entry(3.9, -2.3),
            Map.entry(4.0, -2.2), Map.entry(4.1, -2.1),
            Map.entry(4.2, -2.1), Map.entry(4.3, -2.0),
            Map.entry(4.4, -2.0), Map.entry(4.5, -1.9),
            Map.entry(4.6, -1.8), Map.entry(4.7, -1.8),
            Map.entry(4.8, -1.7), Map.entry(4.9, -1.7),
            Map.entry(5.0, -1.7), Map.entry(5.1, -1.6),
            Map.entry(5.2, -1.6), Map.entry(5.3, -1.5),
            Map.entry(5.4, -1.5), Map.entry(5.5, -1.4),
            Map.entry(5.6, -1.4), Map.entry(5.7, -1.4),
            Map.entry(5.8, -1.3), Map.entry(5.9, -1.3),
            Map.entry(6.0, -1.3), Map.entry(6.1, -1.2),
            Map.entry(6.2, -1.2), Map.entry(6.3, -1.2),
            Map.entry(6.4, -1.1), Map.entry(6.5, -1.1),
            Map.entry(6.6, -1.1), Map.entry(6.7, -1.0),
            Map.entry(6.8, -1.0), Map.entry(6.9, -1.0),
            Map.entry(7.0, -1.0), Map.entry(7.1, -0.9),
            Map.entry(7.2, -0.9), Map.entry(7.3, -0.9),
            Map.entry(7.4, -0.9), Map.entry(7.5, -0.9),
            Map.entry(7.6, -0.8), Map.entry(7.7, -0.8),
            Map.entry(7.8, -0.8), Map.entry(7.9, -0.8),
            Map.entry(8.0, -0.7), Map.entry(8.1, -0.7),
            Map.entry(8.2, -0.7), Map.entry(8.3, -0.7),
            Map.entry(8.4, -0.7), Map.entry(8.5, -0.7),
            Map.entry(8.6, -0.6), Map.entry(8.7, -0.6),
            Map.entry(8.8, -0.6), Map.entry(8.9, -0.6),
            Map.entry(9.0, -0.6), Map.entry(9.1, -0.6),
            Map.entry(9.2, -0.6), Map.entry(9.3, -0.5),
            Map.entry(9.4, -0.5), Map.entry(9.5, -0.5),
            Map.entry(9.6, -0.5), Map.entry(9.7, -0.5),
            Map.entry(9.8, -0.5), Map.entry(9.9, -0.5)
    );
}
