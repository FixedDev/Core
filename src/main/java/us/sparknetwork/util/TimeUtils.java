package us.sparknetwork.util;

public class TimeUtils {

    public static String getMSG(long time) {
        if(time <= 0){
            throw new IllegalArgumentException("The time must be a number major than 0");
        }
        long originalTime = time;
        // Cut off millis
        time /= 1000;
        StringBuilder message = new StringBuilder();
        if (time >= 86400 * 30 * 12) {
            long years = time / (86400 * 30 * 12);
            time = time % (86400 * 30 * 12);
            if (time == 0)
                message.append(years + "Year(s)");
            else
                message.append(years + "Year(s) ");
        }
        if (time >= 86400 * 30) {
            long months = time / (86400 * 30);
            time = time % (86400 * 30);
            if (time == 0)
                message.append(months + " Month(s)");
            else
                message.append(months + " Month(s) ");

        }
        if (time >= 86400 * 7) {
            long weeks = time / (86400 * 7);
            time = time % (86400 * 7);
            if (time == 0)
                message.append(weeks + " Weeks(s)");
            else
                message.append(weeks + " Weeks(s) ");
        }
        if (time >= 86400) {
            long days = time / (86400);
            time = time % (86400);
            if (time == 0)
                message.append(days + " Day(s)");
            else
                message.append(days + " Day(s) ");
        }
        if (time >= 3600) {
            long hours = time / (3600);
            time = time % (3600);
            if (time == 0)
                message.append(hours + " Hour(s)");
            else
                message.append(hours + " Hour(s) ");
        }
        if (time >= 60) {
            long mins = time / (60);
            time = time % (60);
            if (time == 0)
                message.append(mins + " Minute(s)");
            else
                message.append(mins + " Minute(s) ");
        }
        if ((time < 60 && time >= 0) && originalTime >= 0) {
            message.append(time + " Second(s)");
        }
        return message.toString();
    }

}
