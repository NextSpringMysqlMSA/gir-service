package com.nsmm.esg.griservice.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import com.nsmm.esg.griservice.dto.ErrorResponse;

/**
 * GRI 서비스 전역 예외 처리 핸들러
 *
 * - @RestControllerAdvice 어노테이션을 사용하여 컨트롤러 전역에서 발생하는 예외를 처리
 * - GriException을 처리하여 사용자에게 표준화된 에러 응답을 반환
 * - 추후 필요 시 다른 커스텀 예외 또는 시스템 예외를 추가하여 처리 가능
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * GriException 발생 시 처리 메서드
     *
     * - 로그에 경고 메시지 출력
     * - ErrorResponse 객체를 생성하여 상태 코드, 에러 코드, 메시지를 응답 본문에 포함
     * - 클라이언트에게 예외 상황을 일관된 형식으로 전달
     *
     * @param ex 발생한 GriException 예외 객체
     * @return 상태 코드와 ErrorResponse 본문을 포함한 ResponseEntity
     */
    @ExceptionHandler(GriException.class)
    public ResponseEntity<ErrorResponse> handleGriException(GriException ex) {
        log.warn("[GRI 예외] {}", ex.getMessage());
        ErrorResponse error = ErrorResponse.of(
            ex.getErrorCode(),
            ex.getMessage(),
            "" // 요청 path가 없는 경우 빈 문자열로 처리
        );
        return ResponseEntity.status(ex.getStatus()).body(error);
    }
}