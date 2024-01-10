package com.synechron.wordcounter.controller;

import com.synechron.wordcounter.model.dto.WordDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;

interface CounterApiV1 {

    @GetMapping(value = "/api/v1/counters/{word}", produces = "application/json")
    @Operation(
            summary = "Find a word count",
            description = "Retrieving a single word count",
            operationId = "findWord")
    @ApiResponse(
            responseCode = "200",
            description = "Words count successfully found",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = WordDto.class)))
    @ApiResponse(
            responseCode = "404",
            description = "Not Found.",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = String.class)))
    ResponseEntity<?> retrieveWord(@PathVariable("word") final String word);

    @PostMapping(value = "/api/v1/counters", consumes = "application/json", produces = "application/json")
    @Operation(
            summary = "Count words",
            description = "Count words.",
            operationId = "countWord")
    @ApiResponse(
            responseCode = "201",
            description = "Words count successfully created",
            content = @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = WordDto.class))))
    @ApiResponse(
            responseCode = "400",
            description = "Malformed word/s supplied - check error message for details.",
            content = @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = WordDto.class))))
    @ResponseStatus(CREATED)
    ResponseEntity<List<WordDto>> countWords(@Valid
                                             @RequestBody
                                             @NotEmpty(message = "Word count request cannot be empty")
                                             List<@Valid WordDto> dto);
}
