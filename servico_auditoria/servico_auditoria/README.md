# Serviço de Auditoria (Spring Boot + MongoDB)

Registra ações dos usuários nos microserviços e fornece endpoints para consulta no frontend.

## Modelo de Dados

Collection: `auditoria_eventos`

Campos:
- `id` (String)
- `projetoId` (String)
- `tarefaId` (String, opcional)
- `usuario` (String)
- `acao` (String)
- `traceId` (String, opcional)
- `data` (LocalDateTime)

## Endpoints

- `GET /auditoria/projeto/{id}`: lista eventos do projeto (ordem desc).
- `POST /auditoria/evento` (opcional): registra um evento.
  - Body JSON: `{ "projetoId", "tarefaId", "usuario", "acao", "traceId" }`
  - Headers aceitos: `X-Trace-Id` (sobrepõe body), `X-User` (sobrepõe body)

## Configuração

Arquivo `src/main/resources/application.properties`:

```
spring.data.mongodb.uri=mongodb://localhost:27017/auditoria_db
server.port=8085
```

## Exemplos de uso a partir de outros microserviços

### 1) Via injeção de dependência usando `WebClient`

```java
// Configuração em outro microserviço
@Configuration
public class AuditoriaClientConfig {
    @Bean
    public WebClient auditoriaWebClient(
            @Value("${auditoria.service.base-url:http://localhost:8085}") String baseUrl) {
        return WebClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }
}

@Component
public class AuditoriaClient {
    private final WebClient webClient;

    public AuditoriaClient(WebClient auditoriaWebClient) {
        this.webClient = auditoriaWebClient;
    }

    public Mono<Void> registrarEvento(String projetoId, String tarefaId, String usuario, String acao, String traceId) {
        Map<String, Object> body = Map.of(
            "projetoId", projetoId,
            "tarefaId", tarefaId,
            "usuario", usuario,
            "acao", acao,
            "traceId", traceId
        );

        return webClient.post()
                .uri("/auditoria/evento")
                .header("X-Trace-Id", traceId)
                .header("X-User", usuario)
                .bodyValue(body)
                .retrieve()
                .bodyToMono(Void.class);
    }
}

// Exemplo de uso em um service qualquer
@Service
public class TarefaService {
    private final AuditoriaClient auditoriaClient;

    public TarefaService(AuditoriaClient auditoriaClient) {
        this.auditoriaClient = auditoriaClient;
    }

    public void deletarComentario(String projetoId, String tarefaId, String usuario, String traceId) {
        // ... lógica de deleção ...
        auditoriaClient.registrarEvento(projetoId, tarefaId, usuario, "deletou comentário", traceId).subscribe();
    }
}
```

### 2) Via requisição HTTP direta (RestTemplate)

```java
@Component
public class AuditoriaHttpClient {
    private final RestTemplate rest = new RestTemplate();
    @Value("${auditoria.service.base-url:http://localhost:8085}")
    private String baseUrl;

    public void registrarEvento(String projetoId, String tarefaId, String usuario, String acao, String traceId) {
        String url = baseUrl + "/auditoria/evento";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("X-Trace-Id", traceId);
        headers.add("X-User", usuario);

        Map<String, Object> body = new HashMap<>();
        body.put("projetoId", projetoId);
        body.put("tarefaId", tarefaId);
        body.put("usuario", usuario);
        body.put("acao", acao);
        body.put("traceId", traceId);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);
        rest.postForEntity(url, request, String.class);
    }
}
```

### 3) cURL

```bash
curl -X POST "http://localhost:8085/auditoria/evento" \
  -H "Content-Type: application/json" \
  -H "X-Trace-Id: 123e4567-e89b-12d3-a456-426614174000" \
  -H "X-User: Pedro" \
  -d '{
    "projetoId": "12313131313",
    "tarefaId": "1",
    "usuario": "Pedro",
    "acao": "deletou comentário",
    "traceId": "123e4567-e89b-12d3-a456-426614174000"
  }'
```

## Observações
- O `traceId` deve vir do Gateway (header `X-Trace-Id`).
- O header `X-User` pode ser populado pelo serviço de autenticação e repassado via Gateway.
- O endpoint GET suporta paginação facilmente no futuro, se necessário.
