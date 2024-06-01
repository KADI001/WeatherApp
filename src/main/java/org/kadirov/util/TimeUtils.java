package org.kadirov.util;

public final class TimeUtils {

    private TimeUtils(){}

    public static float fromMillToSec(long mill){
        return mill / 1000f;
    }

    public static int fromSecToMill(float sec){
        return (int) (sec * 1000);
    }

    public static int fromMinToMill(float min){
        return (int) ((min * 60) * 1000);
    }

    public static int fromMinToSec(float min){
        return (int) (min * 60);
    }

    public static int fromHoursToSeconds(int hours) {
        return hours * 60 * 60;
    }
}