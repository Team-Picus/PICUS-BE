package com.picus.core.global.utils.regex;

import com.picus.core.global.common.exception.RestApiException;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.regex.Pattern;

import static com.picus.core.global.common.exception.code.status.GlobalErrorStatus._CONTAIN_BAD_WORD;

@Component
public class BadWordFilterUtil implements BadWords {

    private final Set<String> badWordsSet = new HashSet<>(List.of(badWords));
    private final Map<String, Pattern> badWordPatterns = new HashMap<>();

    @PostConstruct // ①
    public void compileBadWordPatterns() {
        String patternText = buildPatternText();

        for (String word : badWordsSet) {
            String[] chars = word.split("");
            badWordPatterns.put(word, Pattern.compile(String.join(patternText, chars)));
        }
    }

    private String buildPatternText() {
        StringBuilder delimiterBuilder = new StringBuilder("[");
        for (String delimiter : delimiters) {
            delimiterBuilder.append(Pattern.quote(delimiter));
        }
        delimiterBuilder.append("]*");
        return delimiterBuilder.toString();
    }

    public void filterBadWord(String input) {
        for (Pattern pattern : badWordPatterns.values()) {
            if (pattern.matcher(input).find()) {
                throw new RestApiException(_CONTAIN_BAD_WORD);
            }
        }
    }
}
