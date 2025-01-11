package com.library.controller;

import com.library.controller.request.SearchRequest;
import com.library.controller.response.PageResult;
import com.library.controller.response.SearchResponse;
import com.library.service.BookApplicationService;
import com.library.service.BookQueryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/books") // 버저닝을 통한 무중단 배포
public class BookController {

  private final BookApplicationService bookApplicationService;

  @GetMapping
  public PageResult<SearchResponse> search(@Valid SearchRequest request) {
    return bookApplicationService.search(request.query(), request.page(), request.size());
  }
}
