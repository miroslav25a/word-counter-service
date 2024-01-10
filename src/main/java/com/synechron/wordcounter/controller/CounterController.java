package com.synechron.wordcounter.controller;

import com.synechron.wordcounter.model.dto.WordDto;
import com.synechron.wordcounter.service.CounterService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static java.lang.String.format;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;

@Validated
@Slf4j
@RequiredArgsConstructor
@CrossOrigin
@RestController
public class CounterController implements CounterApiV1 {

    private final CounterService counterService;

    @Override
    public ResponseEntity<?> retrieveWord(final String word) {
        log.info("Retrieving Word count size: {}", word);

        final var optionalWordDto = this.counterService.findByValue(word);

        // TODO replace the below if statement by throwing a custom exception in the counter service findByValue
        //  method and introduce an exception handler to return 404.
        if (optionalWordDto.isEmpty()) {
            return new ResponseEntity<>(format("Not found Word: %s", word), NOT_FOUND);
        }

        final var wordDto = optionalWordDto.get();
        return new ResponseEntity<>(wordDto, OK);
    }

    @Override
    public ResponseEntity<List<WordDto>> countWords(final List<WordDto> dto) {
        log.info("Count words: {}", dto);

        final var responseDto = this.counterService.add(dto);

        return ResponseEntity.status(CREATED).body(responseDto);
    }
}
