
package com.nsmm.esg.griservice.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * GRI 서비스에서 사용하는 기본 예외 클래스입니다.
 *
 * ✅ 이 클래스를 상속하여 서비스 전반에 걸쳐 사용자 정의 예외를 구현할 수 있습니다.
 * ✅ 모든 GRI 관련 커스텀 예외는 반드시 이 클래스를 기반으로 작성해야 합니다.
 *
 * 📌 필드 설명:
 * - status: HTTP 상태 코드 (예: 404 NOT_FOUND, 400 BAD_REQUEST 등)
 * - errorCode: 서비스 내부에서 정의하는 고유한 에러 식별 코드 (예: GRI_NOT_FOUND)
 *
 * 📌 사용 예:
 * throw new GriNotFoundException("GRI 데이터를 찾을 수 없습니다");
 */
@Getter
public abstract class GriException extends RuntimeException {
    private final HttpStatus status;
    private final String errorCode;

    protected GriException(String message, HttpStatus status, String errorCode) {
        super(message);
        this.status = status;
        this.errorCode = errorCode;
    }
}