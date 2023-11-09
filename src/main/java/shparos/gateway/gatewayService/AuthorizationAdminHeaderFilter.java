package shparos.gateway.gatewayService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHeaders;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Date;


@Component
@Slf4j
//AbstractGatewayFilterFactory인터페이스 구현
public class AuthorizationAdminHeaderFilter extends AbstractGatewayFilterFactory<AuthorizationAdminHeaderFilter.Config> {

    Environment env;

    public AuthorizationAdminHeaderFilter(Environment env) {
        super(Config.class);
        this.env = env;
    }

    public static class Config {

    }

    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {

            //↓ exchange.getRequest()메서드 호출로 request변수 초기화
            //  exchange.getRequest()메서드는 현재 요청에 대한 ServerHttpRequest를 반환하므로 별도의 파라미터가 필요하지 않다.
            ServerHttpRequest request = exchange.getRequest();
            //↓ 헤더에서 Authorization키를 가진 값이 없으면 에러를 반환
            if (!request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                return onError(exchange, "no authorization header", HttpStatus.UNAUTHORIZED);
            }
            //↓ 헤더에 Authorization키를 가진 값이 있으면 토큰을 가져옴
            String authorizationHeader = request.getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
            //↓ Bearer 부분을 제거하고 토큰만 가져옴(치환함)
            String jwt = authorizationHeader.replace("Bearer ", "");
            //↓ 토큰이 유효한지 검사
            if (!isJwtValid(jwt)) {
                return onError(exchange, "JWT token is not valid", HttpStatus.UNAUTHORIZED);
            }
            return chain.filter(exchange);
        });
    }
    //↓ 토큰이 유효한지 검사하는 메소드
    private boolean isJwtValid(String jwt) {
        boolean returnValue = true;

        try {
            //Jwt의 페이로드부분 = Claims
            //Jwt는 헤더,페이로드,서명 3가지 요소로 구성됨
            //페이로드는 클레임(claim)들의 집합으로 이루어져 있으며 각각의 클레임은 정보의 한 조각을 나타내며, 특정한 목적을 가지고 있다.
            //예를 들면 사용자의 식별자, 토근 만료 시간, 권한 등이 클레임으로 사용될 수 있다.
            Claims claims = Jwts.parser().setSigningKey(env.getProperty("token.secret")) // JWT를 생성할때 사용한 비밀키를 가져옴
                    .parseClaimsJws(jwt).getBody(); // JWT를 파싱함 + 파싱한 토큰에서 페이로드를 가져옴

            String role = claims.get("role", String.class);//역할을 가져옴
            Date exp = claims.getExpiration();//만료시간을 가져옴
            Date now = new Date();//현재시간

            if (role == null || role.isEmpty() || exp == null || exp.before(now) || !role.equals("ADMIN")) {
                returnValue = false;
                //토큰의 인증정보가 틀리면 false 리턴
            }

        } catch (Exception e) {
            returnValue = false;
            //예외발생
        }

        return  returnValue;//true 리턴
    }

    private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus httpStatus) { // 응답

        //현재 응답을 response객체에 담음
        ServerHttpResponse response = exchange.getResponse();
        //status코드를 설정
        response.setStatusCode(httpStatus);
        //로그에 에러메시지 출력
        log.error(err);
        return response.setComplete();

    }

}