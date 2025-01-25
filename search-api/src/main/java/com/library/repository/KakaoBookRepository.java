package com.library.repository;

import com.library.Document;
import com.library.KakaoBookResponse;
import com.library.controller.response.PageResult;
import com.library.controller.response.SearchResponse;
import com.library.feign.KakaoClient;
import com.library.util.DateUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
@Repository
public class KakaoBookRepository implements BookRepository {

  private final KakaoClient kakaoClient;

  @Override
  public PageResult<SearchResponse> search(String query, int page, int size) {
    KakaoBookResponse response = kakaoClient.search(query, page, size);
    List<SearchResponse> responses = response.documents().stream()
                                             .map(this::createResponse)
                                             .collect(Collectors.toList());
    return new PageResult<>(page, size, response.meta().totalCount(), responses);
  }

  private SearchResponse createResponse(Document document) {
    String author = Optional.ofNullable(document.authors())
                            .filter(authors -> !authors.isEmpty())
                            .map(List::getFirst)
                            .orElse("Unknown Author");
    String publisher = Optional.ofNullable(document.publisher())
                               .filter(p -> !p.isBlank())
                               .orElse("Unknown Publisher");
    return SearchResponse.builder()
                         .title(document.title())
                         .author(author)
                         .publisher(publisher)
                         .pubDate(DateUtils.parseOffsetDateTime(document.dateTime()).toLocalDate())
                         .isbn(document.isbn())
                         .build();
  }
}
