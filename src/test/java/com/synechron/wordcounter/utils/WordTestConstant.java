package com.synechron.wordcounter.utils;

import com.synechron.wordcounter.model.dto.WordDto;

import java.util.List;

public interface WordTestConstant {

    String VALUE_FLOWER = "flower";
    String VALUE_CAR = "car";
    String VALUE_CAR_1 = "car 1";
    Integer COUNT_SIZE_1 = 1;
    String VALUE_FLOR = "flor";
    Integer COUNT_SIZE_2 = 2;
    WordDto WORD_DTO_1 = WordDto.builder()
            .value(VALUE_FLOWER)
            .countSize(COUNT_SIZE_1)
            .build();

    WordDto WORD_DTO_2 = WordDto.builder()
            .value(VALUE_FLOR)
            .countSize(COUNT_SIZE_2)
            .build();

    WordDto WORD_DTO_3 = WordDto.builder()
            .value(VALUE_CAR)
            .countSize(COUNT_SIZE_1)
            .build();

    WordDto WORD_DTO_4 = WordDto.builder()
            .value(VALUE_CAR_1)
            .countSize(COUNT_SIZE_1)
            .build();

    List<WordDto> WORD_DTO_LIST = List.of(WORD_DTO_1, WORD_DTO_2);
    List<WordDto> WORD_DTO_LIST_2 = List.of(WORD_DTO_1, WORD_DTO_3);
    List<WordDto> WORD_DTO_LIST_3 = List.of(WORD_DTO_1);
    List<WordDto> WORD_DTO_LIST_4 = List.of(WORD_DTO_1, WORD_DTO_4);
}
