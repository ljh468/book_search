package com.library.rest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class NaverRestClient {

  private final RestClient restClient;

  private final String naverUrl;

  private final String naverClientId;

  private final String naverClinetSecret;

  public NaverRestClient(
      @Value("${external.naver.url}") String naverUrl,
      @Value("${external.naver.headers.client-id}") String naverClientId,
      @Value("${external.naver.headers.client-secret}") String naverClinetSecret
  ) {
    restClient = RestClient.create();
    this.naverUrl = naverUrl;
    this.naverClientId = naverClientId;
    this.naverClinetSecret = naverClinetSecret;
  }

  public String search(String query) {
    String url = UriComponentsBuilder.fromUriString(naverUrl + "/v1/search/book.json")
                                     .queryParam("query", query)
                                     .queryParam("display", 10)
                                     .queryParam("start", 1)
                                     .toUriString();
    return restClient.get()
                     .uri(url)
                     .header("X-Naver-Client-Id", naverClientId)
                     .header("X-Naver-Client-Secret", naverClinetSecret)
                     .retrieve()
                     .body(String.class);
  }
}
