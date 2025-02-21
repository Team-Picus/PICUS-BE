package com.picus.core.domain.post.helper;

import com.picus.core.global.CryptoUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Component
public class ViewHistoryCookieHelper {

    private static final String COOKIE_NAME = "picus_view_history";
    private static final int COOKIE_TTL_SECONDS = 3 * 60; // 3분
    private static final String DELIMITER = "_";
    private static final int MAX_ENTRIES = 5;

    public boolean hasAlreadyViewed(Long postId, HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();

        Optional<Cookie> existingCookie = Arrays.stream(cookies)
                .filter(cookie -> COOKIE_NAME.equals(cookie.getName()))
                .findFirst();
        if (existingCookie.isEmpty()) {
            return false;
        }

        String decryptedValue = CryptoUtil.decrypt(existingCookie.get().getValue());
        if (decryptedValue.isEmpty()) {
            return false;
        }

        List<String> idList = Arrays.asList(decryptedValue.split(DELIMITER));
        return idList.contains(String.valueOf(postId));
    }

    public void bindViewCookie(Long postId, HttpServletRequest request, HttpServletResponse response) {
        String newId = String.valueOf(postId);
        List<String> idList = new ArrayList<>();

        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            Optional<Cookie> existingCookie = Arrays.stream(cookies)
                    .filter(cookie -> COOKIE_NAME.equals(cookie.getName()))
                    .findFirst();
            if (existingCookie.isPresent()) {
                String decryptedValue = CryptoUtil.decrypt(existingCookie.get().getValue());
                if (!decryptedValue.isEmpty()) {
                    idList.addAll(Arrays.asList(decryptedValue.split(DELIMITER)));
                }
            }
        }

        if (!idList.contains(newId)) {
            if (idList.size() >= MAX_ENTRIES) {
                idList.remove(0);
            }
            idList.add(newId);
        }

        String concatenatedValue = String.join(DELIMITER, idList);
        String encryptedValue = CryptoUtil.encrypt(concatenatedValue);

        Cookie cookie = new Cookie(COOKIE_NAME, encryptedValue);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(COOKIE_TTL_SECONDS);
        response.addCookie(cookie);
    }
}
