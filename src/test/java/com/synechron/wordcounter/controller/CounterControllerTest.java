package com.synechron.wordcounter.controller;

import com.synechron.wordcounter.WordCounterServiceApplication;
import com.synechron.wordcounter.model.dto.WordDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.synechron.wordcounter.utils.WordTestConstant.COUNT_SIZE_2;
import static com.synechron.wordcounter.utils.WordTestConstant.VALUE_FLOWER;
import static com.synechron.wordcounter.utils.WordTestConstant.WORD_DTO_1;
import static com.synechron.wordcounter.utils.WordTestConstant.WORD_DTO_LIST;
import static com.synechron.wordcounter.utils.WordTestConstant.WORD_DTO_LIST_4;
import static java.util.Arrays.asList;
import static java.util.Optional.ofNullable;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = WordCounterServiceApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CounterControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void postInvalidWordList() {
        // When
        final var responseEntity = restTemplate
                .postForEntity("http://localhost:" + port + "/api/v1/counters", WORD_DTO_LIST_4, Map.class);

        // Then
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(400);
        assertThat(responseEntity.getBody()).isNotEmpty();
        assertThat(responseEntity.getBody()).containsKey("errors");
        assertThat((List<String>) responseEntity.getBody().get("errors")).contains("Word value is invalid.");
    }

    @Test
    public void postValidWordList() {
        // When
        final var responseEntity = restTemplate
                .postForEntity("http://localhost:" + port + "/api/v1/counters", WORD_DTO_LIST, WordDto[].class);

        // Then
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(201);
        assertThat(responseEntity.getBody().length).isEqualTo(1);
        assertThat(asList(responseEntity.getBody()).stream()
                .map(WordDto::value)
                .toList()).containsExactlyInAnyOrder(VALUE_FLOWER);
        assertThat(asList(responseEntity.getBody()).stream()
                .map(WordDto::countSize)
                .toList()).containsExactlyInAnyOrder(COUNT_SIZE_2);
    }

    @Test
    public void postEmptyWordList() {
        // When
        final var responseEntity = restTemplate
                .postForEntity("http://localhost:" + port + "/api/v1/counters", List.of(), Map.class);

        // Then
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(400);
        assertThat(responseEntity.getBody()).isNotEmpty();
        assertThat(responseEntity.getBody()).containsKey("errors");
        assertThat((List<String>) responseEntity.getBody().get("errors")).contains("Word count request cannot be empty");
    }

    @ParameterizedTest
    @NullSource
    @EmptySource
    @ValueSource(strings = {"car 1", "car ", "1", "$"})
    public void postInvalidWordValue(String input) {
        // Given
        var dtos = new ArrayList<>(WORD_DTO_LIST);
        dtos.add(WORD_DTO_1.toBuilder().value(input).build());

        // When
        final var responseEntity = restTemplate
                .postForEntity("http://localhost:" + port + "/api/v1/counters", dtos, Map.class);

        // Then
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(400);
        assertThat(responseEntity.getBody().size()).isEqualTo(1);
        assertThat(responseEntity.getBody()).containsKey("errors");
        var expectedErrorMessage = ofNullable(input)
                .map(id -> "Word value is invalid.")
                .orElse("Word value should not be null.");
        assertThat(((ArrayList) responseEntity.getBody().get("errors")).get(0)).isEqualTo(expectedErrorMessage);
    }

    @Test
    public void getUnknownWord() {
        // When
        final var responseEntity = restTemplate
                .getForEntity("http://localhost:" + port + "/api/v1/counters/car", String.class);

        // Then
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(404);
        assertThat(responseEntity.getBody()).isNotEmpty();
        assertThat(responseEntity.getBody()).isEqualTo("Not found Word: car");
    }

    @Test
    public void getKnownWord() {
        // Given
        restTemplate
                .postForEntity("http://localhost:" + port + "/api/v1/counters", WORD_DTO_LIST, WordDto[].class);

        // When
        final var responseEntity = restTemplate
                .getForEntity("http://localhost:" + port + "/api/v1/counters/flower", WordDto.class);

        // Then
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(200);
        assertThat(responseEntity.getBody().value()).isEqualTo(VALUE_FLOWER);
        assertThat(responseEntity.getBody().countSize()).isEqualTo(COUNT_SIZE_2);
    }
}