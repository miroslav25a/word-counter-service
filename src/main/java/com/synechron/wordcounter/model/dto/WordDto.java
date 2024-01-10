package com.synechron.wordcounter.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;

import static com.synechron.wordcounter.model.dto.RegExpPattern.VALUE;

@Builder(toBuilder = true)
@Schema(name = "Word",
        description = "Captures the key parameters for a word.",
        requiredProperties = {"value"})
public record WordDto(
        @Schema(description = "The word value should be used to identify the word.", example = "Car")
        @NotNull(message = "Word value should not be null.")
        @Pattern(regexp = VALUE, message = "Word value is invalid.")
        String value,

        @Schema(description = "The word count size should be used to declare the current size of the count.",
                example = "2")
        int countSize) {
}
