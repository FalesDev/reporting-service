package co.com.pragma.api;

import co.com.pragma.api.exception.GlobalExceptionHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
@RequiredArgsConstructor
public class RouterRest {

    @Bean
    public RouterFunction<ServerResponse> routerFunction(Handler handler,
                                                         GlobalExceptionHandler globalExceptionHandler) {
        return RouterFunctions.route()
                .GET("/reporting/api/v1/reports", handler::getReportApprovedById)
                .filter(globalExceptionHandler)
                .build();
    }
}
