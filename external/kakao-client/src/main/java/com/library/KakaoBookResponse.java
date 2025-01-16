package com.library;

import java.util.List;

// 통일성이 중요
public record KakaoBookResponse(
    List<Document> documents,
    Meta meta
) {}
