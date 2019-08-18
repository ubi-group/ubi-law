package com.itcag.util;

import com.itcag.util.txt.TextToolbox;
import java.text.SimpleDateFormat;

import java.sql.Timestamp;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Collection of useful conversion methods. 
 * @author IT Consulting Alicja Gruzdz
 */
public final class Converter {
    
    public final static String INPUT_DATE_FORMAT = "yyyy-MM-dd HH:mm";
    public final static String DISPLAY_DATE_FORMAT = "EEE, d MMM yyyy HH:mm";
    public final static String DISPLAY_TIME_FORMAT = "HH:mm";
    public final static String FILE_NAME_DATE_FORMAT = "yyyy-MM-dd";
    
    public final static Boolean convertStringToBoolean(String input) {
        if (!TextToolbox.isEmpty(input)) {
            try {
                return Boolean.valueOf(input);
            } catch (Exception ex) {
                return null;
            }
        } else {
            return null;
        }
    }
    
    /**
     * Converts a string into an integer - if possible, and if not returns null.
     * @param input String presumably holding a numeric value.
     * @return Integer if successful, null otherwise.
     */
    public final static Integer convertStringToInteger(String input) {
        if (!TextToolbox.isEmpty(input)) {
            try {
                return Integer.parseInt(input);
            } catch (Exception ex) {
                return null;
            }
        } else {
            return null;
        }
    }
    
    /**
     * Converts a string into a long - if possible, and if not returns null.
     * @param input String presumably holding a numeric value.
     * @return Long if successful, null otherwise.
     */
    public final static Long convertStringToLong(String input) {
        if (!TextToolbox.isEmpty(input)) {
            input = input.trim();
            if (input.isEmpty()) return null;
            try {
                return Long.parseLong(input);
            } catch (Exception ex) {
                return null;
            }
        } else {
            return null;
        }
    }
    
    /**
     * Converts a string into a double-precision number - if possible, and if not returns null.
     * @param input String presumably holding a numeric value.
     * @return Double if successful, null otherwise.
     */
    public final static Double convertStringToDouble(String input) {
        if (!TextToolbox.isEmpty(input)) {
            try {
                return Double.parseDouble(input);
            } catch (Exception ex) {
                return null;
            }
        } else {
            return null;
        }
    }
    
    /**
     * Converts a string into a timestamp - if possible, and if not returns null.
     * @param input String presumably holding a timestamp.
     * @param format Valid SimpleDateFormat date/time format that the input is believed to be using.
     * @return Timestamp if successful, null otherwise.
     */
    public final static Timestamp convertStringToTimestamp(String input, String format) {
        if (!TextToolbox.isEmpty(input)) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
            try {
                Date date = simpleDateFormat.parse(input);
                return new Timestamp(date.getTime());
            } catch (Exception ex) {
                return null;
            }
        } else {
            return null;
        }
    }

    public final static Date convertStringToDate(String input, String format) {
        if (!TextToolbox.isEmpty(input)) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
            try {
                return simpleDateFormat.parse(input);
            } catch (Exception ex) {
                return null;
            }
        } else {
            return null;
        }
    }

    public final static String formatDate(Date input, String format) {
        if (input != null) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
            try {
                return simpleDateFormat.format(input);
            } catch (Exception ex) {
                return null;
            }
        } else {
            return null;
        }
    }

    /**
     * Converts a timestamp into a string representing a formatted date.
     * @param timestamp Timestamp to be converted.
     * @param format Valid SimpleDateFormat date/time format that will be applied to the returned string.
     * @return String representing a formatted date.
     */
    public final static String convertTimestampToFromattedString(Timestamp timestamp, String format) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        return simpleDateFormat.format(timestamp);
    }

    /**
     * Converts a date into a string representing a formatted date.
     * @param date Timestamp to be converted.
     * @param format Valid SimpleDateFormat date/time format that will be applied to the returned string.
     * @return String representing a formatted date.
     */
    public final static String convertDateToFormattedString(Date date, String format) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        return simpleDateFormat.format(date.getTime());
    }

    public final static String convertLongToFormattedString(long date, String format) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        return simpleDateFormat.format(date);
    }

    public final static String formatLongTime(long time) {
        return String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(time), TimeUnit.MILLISECONDS.toMinutes(time) % TimeUnit.HOURS.toMinutes(1), TimeUnit.MILLISECONDS.toSeconds(time) % TimeUnit.MINUTES.toSeconds(1));
    }
    
    public final static String formatDouble(double input) {
        if (input == (long) input) {
            return String.format("%d", (long) input);
        } else {
            return String.format("%s", input);
        }        
    }
    
    /**
     * A common method for all enums since they can't have another base class
     * @param <T> Enum type
     * @param c enum type. All enums must be all caps.
     * @param string case insensitive
     * @return corresponding enum, or null
     */
    public static <T extends Enum<T>> T getEnumFromString(Class<T> c, String string) {

        if( c != null && string != null ) {
            
            try {
            
                return Enum.valueOf(c, string.trim().toUpperCase());
            
            } catch(IllegalArgumentException ex) {

                return null;
            
            }
        
        }
        
        return null;
    
    }

}
