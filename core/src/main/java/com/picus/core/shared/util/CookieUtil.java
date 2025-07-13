package com.picus.core.shared.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.util.SerializationUtils;

import java.util.Base64;
import java.util.Optional;

public class CookieUtil {

    /**
     * 요청에서 지정된 이름의 쿠키를 찾아 반환합니다.
     */
    public static Optional<Cookie> getCookie(HttpServletRequest request, String name) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (name.equals(cookie.getName())) {
                    return Optional.of(cookie);
                }
            }
        }
        return Optional.empty();
    }

    /**
     * HTTP-only 쿠키를 추가합니다.
     */
    public static void addCookie(HttpServletResponse response,
                                 String name,
                                 String value,
                                 int maxAge) {
        Cookie cookie = new Cookie(name, value);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(maxAge);
        response.addCookie(cookie);
    }

    /**
     * Secure, HttpOnly, SameSite=Strict 속성이 포함된 쿠키를 생성합니다.
     * 리프레시 토큰 등 민감 데이터를 저장할 때 사용하세요.
     */
    public static void createSecureCookie(HttpServletResponse response,
                                          String name,
                                          String value,
                                          int maxAge) {
        // 직접 Set-Cookie 헤더 작성으로 SameSite 설정
        StringBuilder sb = new StringBuilder();
        sb.append(name).append("=").append(value)
                .append("; Max-Age=").append(maxAge)
                .append("; Path=/")
                .append("; HttpOnly")
                .append("; Secure")
                .append("; SameSite=Strict");
        response.addHeader("Set-Cookie", sb.toString());
    }

    /**
     * 지정된 이름의 쿠키를 삭제합니다.
     */
    public static void deleteCookie(HttpServletRequest request,
                                    HttpServletResponse response,
                                    String name) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (name.equals(cookie.getName())) {
                    Cookie expired = new Cookie(name, "");
                    expired.setPath("/");
                    expired.setMaxAge(0);
                    expired.setHttpOnly(true);
                    response.addCookie(expired);
                }
            }
        }
    }

    /**
     * 객체를 직렬화하여 Base64 URL-safe 문자열로 인코딩합니다.
     */
    public static String serialize(Object obj) {
        return Base64.getUrlEncoder()
                .encodeToString(SerializationUtils.serialize(obj));
    }

    /**
     * 쿠키 값을 Base64로 디코딩 후 지정된 클래스 타입으로 역직렬화합니다.
     */
    public static <T> T deserialize(Cookie cookie, Class<T> cls) {
        return cls.cast(
                SerializationUtils.deserialize(
                        Base64.getUrlDecoder().decode(cookie.getValue())
                )
        );
    }
}
