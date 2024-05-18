package dev.vinpol.nebula.dragonship.web.support.htmx;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpRequest;

public class HTMXUtils {
    public static final String HX_REQUEST_PARAM = "HX-Request";
    public static final String HTMX_HEADER = "HX-Request=true";

    private HTMXUtils() {

    }

    public static boolean isHtmxRequest(HttpServletRequest request) {
        return request.getHeader(HX_REQUEST_PARAM) != null;
    }

    public static boolean isHtmxRequest(HttpRequest request) {
        return request.getHeaders().containsKey(HX_REQUEST_PARAM);
    }
}
