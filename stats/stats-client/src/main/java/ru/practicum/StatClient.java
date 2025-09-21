package ru.practicum;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.retry.policy.MaxAttemptsRetryPolicy;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;
import ru.practicum.dto.EndpointHitDto;
import ru.practicum.dto.ViewStatsDto;

import java.util.List;

/**
 * The type Stat client.
 */
@Service
@Slf4j
public class StatClient {
    private final RestClient restClient;
    private final DiscoveryClient discoveryClient;

    /*@Value("${discovery.services.stats-server-id}")
    private String statsServerId;*/

    /**
     * Instantiates a new Stat client.
     */
    public StatClient(DiscoveryClient discoveryClient) {
        this.discoveryClient = discoveryClient;
        RetryTemplate retryTemplate = new RetryTemplate();

        FixedBackOffPolicy fixedBackOffPolicy = new FixedBackOffPolicy();
        fixedBackOffPolicy.setBackOffPeriod(3000L);
        retryTemplate.setBackOffPolicy(fixedBackOffPolicy);

        MaxAttemptsRetryPolicy retryPolicy = new MaxAttemptsRetryPolicy();
        retryPolicy.setMaxAttempts(3);
        retryTemplate.setRetryPolicy(retryPolicy);

        ServiceInstance statInstance = retryTemplate.execute(cxt -> getStatInstance());
        String statBaseUrl = statInstance.getUri().toString();
        this.restClient = RestClient.builder()
                .baseUrl(statBaseUrl)
                .build();
    }

    /**
     * Send hit.
     *
     * @param endpointHitDto the endpoint hit dto
     */
    public void sendHit(EndpointHitDto endpointHitDto) {
        try {
            restClient.post()
                    .uri("/hit")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(endpointHitDto)
                    .retrieve()
                    .toBodilessEntity();
            log.info("Статистика посещений записана");
        } catch (Exception e) {
            log.error(getLogMessage("запись") + e.getMessage());
            throw new RuntimeException(getLogMessage("запись") + e);
        }
    }

    /**
     * Gets stats.
     *
     * @param start  the start
     * @param end    the end
     * @param uris   the uris
     * @param unique the unique
     * @return the stats
     */
    public List<ViewStatsDto> getStats(String start, String end, List<String> uris, boolean unique) {
        try {
            var uriBuilder = UriComponentsBuilder.fromPath("/stats")
                    .queryParam("start", start)
                    .queryParam("end", end)
                    .queryParam("unique", unique);
            if (uris != null && !uris.isEmpty()) {
                uris.forEach(uri -> uriBuilder.queryParam("uris", uri));
            }
            log.info("Статистика посещений получена");
            log.info("Параметры запроса - start: {}, end: {}, unique: {}, uris: {}", start, end, unique, uris);
            log.info("Запрос URI: {}", uriBuilder.build().toUriString());
            return restClient.get()
                    .uri(uriBuilder.build().toUriString())
                    .retrieve()
                    .body(new ParameterizedTypeReference<List<ViewStatsDto>>() {
                    });
        } catch (Exception e) {
            log.error(getLogMessage("чтение") + e.getMessage());
            throw new RuntimeException(getLogMessage("чтение") + e);
        }
    }

    /**
     * Gets log message.
     *
     * @param goal the goal
     * @return the log message
     */
    public String getLogMessage(String goal) {
        if (goal.equals("запись")) {
            return "Ошибка записи статистики посещений: ";
        } else if (goal.equals("чтение")) {
            return "Ошибка чтения статистики посещений: ";
        } else
            return "Ошибка статистики посещений: ";
    }

    private ServiceInstance getStatInstance() {
        String statsServerId = "stats-server";

        try {
            return discoveryClient
                    .getInstances(statsServerId)
                    .getFirst();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}