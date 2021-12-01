package com.example.climblabs.global.utils.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SearchType {
    TITLE("제목"),
    CLIMBING_TITLE("암장이름")
    ;

    private final String typeValue;
}
