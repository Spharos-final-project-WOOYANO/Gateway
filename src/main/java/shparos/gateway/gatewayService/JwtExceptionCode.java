package shparos.gateway.gatewayService;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;


@Getter
@AllArgsConstructor
//Jwt 예외코드
public enum JwtExceptionCode {

    UNKNOWN_ERROR(HttpStatus.UNAUTHORIZED, "UNKNOWN_ERROR"),
    NOT_FOUND_TOKEN(HttpStatus.UNAUTHORIZED, "Headers에 토큰 형식의 값 찾을 수 없음"),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰"),
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "기간이 만료된 토큰"),
    WRONG_TYPE_TOKEN(HttpStatus.UNAUTHORIZED, "잘못된 타입의 토큰"),
    ACCESS_DENIED(HttpStatus.UNAUTHORIZED, "접근 거부"),
    UNSUPPORTED_TOKEN(HttpStatus.UNAUTHORIZED, "지원하지 않는 토큰");

    private final HttpStatus httpStatus;
    private final String detail;

}
