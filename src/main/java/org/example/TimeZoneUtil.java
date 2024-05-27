package org.example;

import javax.servlet.http.HttpServletRequest;
import java.time.ZonedDateTime;

public class TimeZoneUtil {
    private static String parseName(HttpServletRequest request){
        if (request.getParameter("timezone") == null){
            return "UTC " + ZonedDateTime.now().getOffset().toString().charAt(2);
        } else {
            return request.getParameter("timezone");
        }
    }

    public static String getZone(HttpServletRequest req) {
        int length = parseName(req).length();
        String timezone = parseName(req).substring(3,length).trim();
        System.out.println(timezone);
        if (timezone.startsWith("-")){
            return timezone;
        } else {
            return  "+" + timezone;
        }
    }


}
