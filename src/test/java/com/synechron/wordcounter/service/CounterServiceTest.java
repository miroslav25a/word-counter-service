package com.synechron.wordcounter.service;

import com.synechron.wordcounter.model.dto.WordDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.synechron.wordcounter.utils.WordTestConstant.COUNT_SIZE_1;
import static com.synechron.wordcounter.utils.WordTestConstant.COUNT_SIZE_2;
import static com.synechron.wordcounter.utils.WordTestConstant.VALUE_CAR;
import static com.synechron.wordcounter.utils.WordTestConstant.VALUE_FLOWER;
import static com.synechron.wordcounter.utils.WordTestConstant.WORD_DTO_1;
import static com.synechron.wordcounter.utils.WordTestConstant.WORD_DTO_LIST;
import static com.synechron.wordcounter.utils.WordTestConstant.WORD_DTO_LIST_2;
import static com.synechron.wordcounter.utils.WordTestConstant.WORD_DTO_LIST_3;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CounterServiceTest {

    @Mock
    private Translator translator;

    @InjectMocks
    private CounterService counterService;

    @Test
    void shouldAddTwoCountsToOneWord() {
        // Given
        given(translator.translate(anyString())).willReturn(VALUE_FLOWER);

        // When
        final var result = counterService.add(WORD_DTO_LIST);

        // Then
        assertThat(result).isNotNull().hasSize(1);

        assertThat(result.stream()
                .map(WordDto::value)
                .toList()).containsExactlyInAnyOrder(VALUE_FLOWER);
        assertThat(result.stream()
                .map(WordDto::countSize)
                .toList()).containsExactlyInAnyOrder(COUNT_SIZE_2);
        verify(translator, times(2)).translate(anyString());
    }

    @Test
    void shouldAddOneCountToTwoWord() {
        // Given
        given(translator.translate(eq(VALUE_FLOWER))).willReturn(VALUE_FLOWER);
        given(translator.translate(eq(VALUE_CAR))).willReturn(VALUE_CAR);

        // When
        final var result = counterService.add(WORD_DTO_LIST_2);

        // Then
        assertThat(result).isNotNull().hasSize(2);

        assertThat(result.stream()
                .map(WordDto::value)
                .toList()).containsExactlyInAnyOrder(VALUE_FLOWER, VALUE_CAR);
        assertThat(result.stream()
                .map(WordDto::countSize)
                .toList()).containsExactlyInAnyOrder(COUNT_SIZE_1, COUNT_SIZE_1);
        verify(translator).translate(VALUE_FLOWER);
        verify(translator).translate(VALUE_CAR);
    }

    @Test
    void shouldFindByValueOneWord() {
        // Given
        given(translator.translate(anyString())).willReturn(VALUE_FLOWER);
        counterService.add(WORD_DTO_LIST_3);

        // When
        final var result = this.counterService.findByValue(VALUE_FLOWER);

        // Then
        assertThat(result).isNotNull()
                .isPresent()
                .containsInstanceOf(WordDto.class);
        assertThat(result.get().value()).isEqualTo(VALUE_FLOWER);
        assertThat(result.get().countSize()).isEqualTo(COUNT_SIZE_1);
    }

    @Test
    void shouldFindByValueNoWord() {
        // Given
        given(translator.translate(anyString())).willReturn(VALUE_FLOWER);

        // When
        final var result = this.counterService.findByValue(VALUE_FLOWER);

        // Then
        assertThat(result).isNotNull().isEmpty();
    }
}