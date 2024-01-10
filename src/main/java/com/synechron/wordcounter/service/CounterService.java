package com.synechron.wordcounter.service;

import com.synechron.wordcounter.model.dto.WordDto;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class CounterService {

    private Map<String, Integer> counter = new ConcurrentHashMap<>();
    private final Translator translator;

    /**
     * Add a list of words to the counter.
     *
     * @param wordDtoList   words to add
     * @return the lost of words
     */
    public List<WordDto> add(final List<WordDto> wordDtoList) {
        Map<String, Integer> requestCounter = new ConcurrentHashMap<>();

        wordDtoList.forEach(word -> {
            //TODO check if the word is in English and skip the call to translate
            final var translatedWord = this.translator.translate(word.value().toLowerCase()).toLowerCase();

            if (counter.containsKey(translatedWord)) {
                final var countSize = counter.get(translatedWord);
                final var newCountSize = countSize + 1;
                counter.put(translatedWord, newCountSize);
                requestCounter.put(translatedWord, newCountSize);
            } else {
                counter.put(translatedWord, 1);
                requestCounter.put(translatedWord, 1);
            }
        });

        return requestCounter.entrySet().stream()
                .map(e -> WordDto.builder()
                    .value(e.getKey())
                    .countSize(e.getValue())
                    .build())
                .collect(toList());
    }

    /**
     * Find a count size for a specified word.
     *
     * @param value     word value
     * @return optional word
     */
    public Optional<WordDto> findByValue(final String value) {
        final var translatedWord = this.translator.translate(value.toLowerCase());

        return this.counter.containsKey(translatedWord.toLowerCase()) ? Optional.of(WordDto.builder()
                .value(translatedWord.toLowerCase())
                .countSize(this.counter.get(value.toLowerCase()))
                .build()) : Optional.empty();
    }
}
