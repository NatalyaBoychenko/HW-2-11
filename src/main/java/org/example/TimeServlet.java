package org.example;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@WebServlet(value = "/time")
public class TimeServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/html; charset=utf-8");

        String zone = TimeZoneUtil.getZone(req);
        System.out.println(zone);

        String localTime = ZonedDateTime.now().withZoneSameInstant(ZoneId.of(zone)).format(DateTimeFormatter.ofPattern(
                "yyyy-MM-dd HH:mm:ss 'UTC${timezone}'"
        ));

        resp.getWriter().write(localTime.replace("${timezone}", zone));
        resp.getWriter().close();;

    }

    private String parseName(HttpServletRequest request){
        if (request.getParameter("timezone") == null){
            return "UTC " + ZonedDateTime.now().getOffset().toString().charAt(2);
        } else {
            return request.getParameter("timezone");
        }
    }

    private String getZone(HttpServletRequest req) {
        String timezone = parseName(req).substring(3,5);

        if (timezone.startsWith("-")){
            return timezone;
        } else {
            return  "+" + timezone.trim();
        }
    }
}
