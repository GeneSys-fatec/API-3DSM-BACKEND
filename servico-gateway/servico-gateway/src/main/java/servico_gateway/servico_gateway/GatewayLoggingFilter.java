package servico_gateway.servico_gateway;

import java.net.URI;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

@Component
public class GatewayLoggingFilter implements GlobalFilter, Ordered {

    private static final Logger logger = LoggerFactory.getLogger(GatewayLoggingFilter.class);
    public static final String TRACE_ID_HEADER = "X-Trace-Id";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // Gerar traceId
        String traceId = UUID.randomUUID().toString();

        // Criar request modificado com o traceId
        ServerHttpRequest mutatedRequest = exchange.getRequest()
                .mutate()
                .headers(headers -> headers.set(TRACE_ID_HEADER, traceId))
                .build();

        // Ler dados básicos do request
        URI url = mutatedRequest.getURI();
        String method = mutatedRequest.getMethod().name();

        // Log da requisição
        logger.info(
            "=== REQUISIÇÃO RECEBIDA ===\n" +
            "Método: {}\n" +
            "URL: {}\n" +
            "traceId: {}\n",
            method, url, traceId
        );

        return chain.filter(exchange.mutate().request(mutatedRequest).build())
                .doOnError(err -> {
                    logger.error(
                        "=== ERRO DURANTE A REQUISIÇÃO ===\n" +
                        "traceId: {}\n" +
                        "Erro: {}\n",
                        traceId,
                        err.getMessage()
                    );
                })
                .then(Mono.fromRunnable(() -> {
                    HttpStatusCode statusCode = exchange.getResponse().getStatusCode();
                    String statusText;
                    if (statusCode != null) {
                        HttpStatus resolved = HttpStatus.resolve(statusCode.value());
                        statusText = resolved != null
                                ? resolved.value() + " " + resolved.getReasonPhrase()
                                : String.valueOf(statusCode.value());
                    } else {
                        statusText = "UNKNOWN";
                    }

                    // Log da resposta
                    logger.info(
                        "=== RESPOSTA ENVIADA ===\n" +
                        "Status: {}\n" +
                        "traceId: {}\n",
                        statusText,
                        traceId
                    );
                }));
    }

    @Override
    public int getOrder() {
        return -1; // Executa antes de outros filtros
    }
}
