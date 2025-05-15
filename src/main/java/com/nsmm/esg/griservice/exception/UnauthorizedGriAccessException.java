package com.nsmm.esg.griservice.exception;

import org.springframework.http.HttpStatus;


/**
 * GRI(지속가능경영 정보) 리소스에 대한 접근 권한이 없는 경우 발생하는 예외 클래스입니다.
 * 예: 사용자가 자신이 작성하지 않은 GRI 정보를 수정하거나 접근하려고 할 때 이 예외가 발생합니다.
 *
 * HttpStatus.FORBIDDEN (403)을 반환하며, 에러 코드로 "UNAUTHORIZED_GRI_ACCESS"를 제공합니다.
 */
public class UnauthorizedGriAccessException extends GriException {
    /**
     * 접근 권한이 없는 경우 예외를 생성합니다.
     * @param message 예외 메시지 (주로 사용자에게 보여질 수 있는 설명)
     */
    public UnauthorizedGriAccessException(String message) {
        super(message, HttpStatus.FORBIDDEN, "UNAUTHORIZED_GRI_ACCESS");
    }
}