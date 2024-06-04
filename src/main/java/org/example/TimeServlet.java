package org.example;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.FileTemplateResolver;


import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Map;

@WebServlet(value = "/time")
public class TimeServlet extends HttpServlet {

    public static final String TEMPLATE_PATH = "D:/projects/goIT/HW-2-11/src/main/resources/templates/";
    private TemplateEngine engine;

    @Override
    public void init() throws ServletException {
        engine = new TemplateEngine();
        FileTemplateResolver resolver = new FileTemplateResolver();
        resolver.setPrefix(TEMPLATE_PATH);
        resolver.setSuffix(".html");
        engine.setTemplateResolver(resolver);
    }


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        resp.setContentType("text/html");

        String zone = getZoneFromCookie(req);

        Cookie cookies = new Cookie("lastTimezone", zone);
        cookies.setPath("/time");
        resp.addCookie(cookies);

        String localTime = ZonedDateTime.now().withZoneSameInstant(ZoneId.of(zone)).format(DateTimeFormatter.ofPattern(
                "yyyy-MM-dd HH:mm:ss 'UTC${timezone}'"
        ));

        Context context = new Context(req.getLocale(),
                Map.of("time", localTime.replace("${timezone}", zone)));

        engine.process("index", context, resp.getWriter());

        resp.getWriter().close();
    }

    private static String getZoneFromCookie(HttpServletRequest req) {
        String zone = "";

        if (req.getParameter("timezone") == null) {
            Cookie cookieValue = Arrays.stream(req.getCookies())
                    .filter(it -> it.getName().equals("lastTimezone"))
                    .findFirst()
                    .get();

            zone = cookieValue.getValue();
        } else {
            zone = TimeZoneUtil.getZone(req);
        }
        return zone;
    }


}
