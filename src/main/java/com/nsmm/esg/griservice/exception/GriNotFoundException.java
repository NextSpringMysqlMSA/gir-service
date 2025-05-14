package com.nsmm.esg.griservice.exception;

import org.springframework.http.HttpStatus;

/**
 * 예외 클래스: 요청한 GRI 정보를 찾을 수 없을 때 발생하는 예외입니다.
 * - 주로 GRI 식별자(ID)로 조회한 결과가 존재하지 않을 경우 사용됩니다.
 * - HTTP 상태 코드: 404 Not Found
 * - 에러 코드: GRI_NOT_FOUND
 */
public class GriNotFoundException extends GriException {
    public GriNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND, "GRI_NOT_FOUND");
    }
}