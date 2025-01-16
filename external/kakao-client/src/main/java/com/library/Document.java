package com.library;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record Document(
    String title,
    List<String> authors,
    String isbn,
    String publisher,
    @JsonProperty("datetime")
    String dateTime
) {}
