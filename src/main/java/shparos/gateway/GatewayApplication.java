package shparos.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

@SpringBootApplication
@EnableDiscoveryClient
public class GatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(GatewayApplication.class, args);
	}

	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		CorsConfiguration corsConfiguration = new CorsConfiguration();
		//↓ access를 허용할 Origin을 설정함 (모든 Origin 허용)
		//  정규 표현식 패턴을 사용하여 여러 오리진을 한번에 허용할 수 있다.
		corsConfiguration.addAllowedOriginPattern("*");
		//↓ Authorization, Content-Type, X-Requested-With등의 request헤더를 설정한다.(브라우저에서 수행한다.)
		corsConfiguration.addAllowedHeader("*");
		//↓ 클라이언트에서 접근가능하게 할 헤더를 명시적으로 지정한다.
		// + 클라이언트에서 접근할수 있고 노출시킬 헤더를 설정
		corsConfiguration.addExposedHeader("*");
		//↓ addAllowedOriginPattern과는 다르게 접근을 허용할 특정 origin을 명시적으로 지정한다.
		//↓ localhost:3000에서 오는 요청을 허용한다.
		corsConfiguration.addAllowedOrigin("http://localhost:3000");
		//↓GET,POST...등등 허용할 HTTP method를 설정한다.
		corsConfiguration.addAllowedMethod("*");
		//↓자격증명 설정
		corsConfiguration.setAllowCredentials(true);
		//↓모든 경로(/**)에 대해 CORS구성을 적용한다.
		//위에서 설정한 corsConfiguration을 주입한다.
		source.registerCorsConfiguration("/**", corsConfiguration);

		return source;
	}

	@Bean
	public CorsWebFilter corsWebFilter() {
		return new CorsWebFilter(corsConfigurationSource());
	}

}
