package com.library.repository;

import com.library.Item;
import com.library.NaverBookResponse;
import com.library.controller.response.PageResult;
import com.library.controller.response.SearchResponse;
import com.library.feign.NaverFeignClient;
import com.library.util.DateUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Repository
public class NaverBookRepository implements BookRepository {

  private final NaverFeignClient naverFeignClient;

  @Override
  public PageResult<SearchResponse> search(String query, int page, int size) {
    NaverBookResponse response = naverFeignClient.search(query, page, size);
    return new PageResult<>(page,
                            size,
                            response.getTotal(),
                            response.getItems().stream()
                                    .map(this::createResponse)
                                    .collect(Collectors.toList()));
  }

  private SearchResponse createResponse(Item item) {
    String author = Optional.ofNullable(item.getAuthor())
                            .filter(authors -> !authors.isEmpty())
                            .orElse("Unknown Author");
    String publisher = Optional.ofNullable(item.getPublisher())
                               .filter(p -> !p.isBlank())
                               .orElse("Unknown Publisher");

    return SearchResponse.builder()
                         .title(item.getTitle())
                         .author(author)
                         .publisher(publisher)
                         .pubDate(DateUtils.parseYYYYMMDD(item.getPubDate()))
                         .isbn(item.getIsbn())
                         .build();
  }
}
