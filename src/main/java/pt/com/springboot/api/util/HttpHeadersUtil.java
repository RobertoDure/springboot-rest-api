package pt.com.springboot.api.util;

import org.springframework.http.HttpHeaders;

import java.util.HashMap;

public class HttpHeadersUtil {

    public static HttpHeaders setHttpHeaders(HashMap<String, String> headers) {

        HttpHeaders httpHeaders = new HttpHeaders();
        headers.forEach(httpHeaders::add);
        return httpHeaders;
    }
}
