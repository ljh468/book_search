package com.library.repository;

import com.library.controller.response.PageResult;
import com.library.controller.response.SearchResponse;
import com.library.feign.Item;
import com.library.feign.NaverBookResponse;
import com.library.feign.NaverFeignClient;
import com.library.util.DateUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

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
    return SearchResponse.builder()
                         .title(item.getTitle())
                         .author(item.getAuthor())
                         .publisher(item.getPublisher())
                         .pubDate(DateUtils.parseYYYYMMDD(item.getPubDate()))
                         .isbn(item.getIsbn())
                         .build();
  }
}
