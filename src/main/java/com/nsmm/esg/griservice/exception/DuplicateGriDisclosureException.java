package com.nsmm.esg.griservice.exception;

import org.springframework.http.HttpStatus;

/**
 * 중복된 GRI 공시 요청이 발생했을 때 발생하는 예외 클래스입니다.
 * 예를 들어, 동일한 GRI 항목에 대해 이미 공시가 완료된 경우에 사용됩니다.
 * HttpStatus.CONFLICT(409) 상태 코드와 함께 "DUPLICATE_GRI_DISCLOSURE" 에러 코드를 반환합니다.
 */
public class DuplicateGriDisclosureException extends GriException {
    /**
     * 중복된 GRI 공시 요청에 대해 예외를 생성합니다.
     *
     * @param message 예외에 대한 설명 메시지
     */
    public DuplicateGriDisclosureException(String message) {
        super(message, HttpStatus.CONFLICT, "DUPLICATE_GRI_DISCLOSURE");
    }
}