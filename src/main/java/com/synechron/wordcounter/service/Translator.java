package com.synechron.wordcounter.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Assumption is that the class is provided by a third party and that is fully tested.
 */
@Service("translator")
@RequiredArgsConstructor
public class Translator {
    private Map<String, String> translationMap = Map.of(
            "flor", "flower",
            "blume", "flower");

    public String translate(final String word) {
        return translationMap.getOrDefault(word, word);
    }
}
