package com.example.climblabs.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ExceptionCode {

    /*
        파일관련 에러
     */
    //파일 저장 실패 시
    FAIL_SAVE_IMAGE(500, "이미지 파일을 저장 실패했습니다."),
    //요청 용량 초과 시
    SizeLimitExceededException(500, "요청 용량 초과되었습니다."),
    //파일 용량 초과 시
    FileSizeLimitExceededException(500, "요청한 이미지 중 용량초과된 이미지가 포함되어있습니다."),

    /*
        DB 에러
     */
    NOT_FOUND_POST(500, "등록된 게시물이 없습니다."),

    /*
        외부 플랫폼 연동에러
     */
    OTHER_PLATFORM_HTTP_ERROR(500, "외부 플랫폼에서 에러가 발생했습니다. 다시 시도해주세요.");


    private final int status;
    private final String message;
}
