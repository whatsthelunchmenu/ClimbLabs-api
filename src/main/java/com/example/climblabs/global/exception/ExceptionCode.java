package com.example.climblabs.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ExceptionCode {

    //file
    FAIL_SAVE_IMAGE(500, "이미지 파일을 저장 실패했습니다."),

    //platform
    OTHER_PLATFORM_HTTP_ERROR(500, "외부 플랫폼에서 에러가 발생했습니다. 다시 시도해주세요.");

    private final int status;
    private final String message;
}
