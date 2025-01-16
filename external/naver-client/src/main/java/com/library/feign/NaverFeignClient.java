package com.library.feign;

import com.library.NaverBookResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
    name = "naverFeignClient",
    url = "${external.naver.url}",
    configuration = NaverFeignClientConfiguration.class)
public interface NaverFeignClient {

  @GetMapping("/v1/search/book.json")
  NaverBookResponse search(@RequestParam("query") String query,
                           @RequestParam("start") int start,
                           @RequestParam("display") int display);
}