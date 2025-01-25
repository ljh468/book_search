package com.library.controller.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.LocalDate;

@Schema(description = "검색 결과 응답")
@Builder
public record SearchResponse(
    @Schema(description = "제목", example = "HTTP완벽가이드")
    String title,
    @Schema(description = "저자", example = "데이빗고올리")
    String author,
    @Schema(description = "출판사", example = "인사이트")
    String publisher,
    @Schema(description = "출판일", example = "2015-01-01")
    LocalDate pubDate,
    @Schema(description = "isbn", example = "9788966261208")
    String isbn) {
}
