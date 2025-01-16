package com.library;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED) // jackson 역직렬화는 기본생성자가 필요 - PROTECTED
public class NaverBookResponse {

  private String lastBuildDate;

  private int total;

  private int start;

  private int display;

  private List<Item> items;
}
