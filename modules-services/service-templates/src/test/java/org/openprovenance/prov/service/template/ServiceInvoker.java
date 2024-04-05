package org.openprovenance.prov.service.template;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ClientCodecConfigurer;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MimeType;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ClientResponse;

import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.util.Base64;
import java.util.Objects;
import java.util.Optional;

import static org.springframework.http.MediaType.TEXT_HTML;


public class ServiceInvoker {
    static Logger logger = LogManager.getLogger(ServiceInvoker.class);
    public static final String APPLICATION_VND_KCL_PROV_TEMPLATE_JSON = "application/vnd.kcl.prov-template+json";

    WebClient webclient= WebClient.create();


    public String getAccessTokenValue(String  url, String name, String password) {
        String encodedClientData = Base64.getEncoder().encodeToString((name + ":" + password) .getBytes());
        MultiValueMap<String, String> values=new LinkedMultiValueMap<>();
        values.add("grant_type", "password");
        values.add("username", name);
        values.add("password",password);
        values.add("client_id","assistant");
        final JsonNode resource = webclient.post()
                .uri(url)
               // .header("Authorization", "Basic " + encodedClientData)
                .body(BodyInserters.fromFormData(values))
                .retrieve()
                .bodyToMono(JsonNode.class)
                .block();
        String token = Objects.requireNonNull(resource).get("access_token").textValue();
        return token;
    }


    public <T> Optional<T> postInstructions(String serviceEndpoint, T data, String accessTokenValue) {
        Duration timeoutIn10Seconds = Duration.ofSeconds(10);


        return webclient
                .method(HttpMethod.POST)
                .uri(serviceEndpoint)
                .body(BodyInserters.fromValue(data))
                .header(HttpHeaders.CONTENT_TYPE, APPLICATION_VND_KCL_PROV_TEMPLATE_JSON)
                .header(HttpHeaders.ACCEPT, APPLICATION_VND_KCL_PROV_TEMPLATE_JSON)
                .headers(h -> h.setBearerAuth(accessTokenValue))
                .retrieve()
                .bodyToMono((Class<T>)data.getClass())
                .blockOptional(timeoutIn10Seconds);

    }

    public <Tin,Tout> Optional<Tout> postInstructionsInOut(String serviceEndpoint, Tin data, ParameterizedTypeReference<Tout> clazz, String accessTokenValue) {
        Duration timeoutIn10Seconds = Duration.ofSeconds(10);

        return webclient
                .method(HttpMethod.POST)
                .uri(serviceEndpoint)
                .body(BodyInserters.fromValue(data))
                .header(HttpHeaders.CONTENT_TYPE, APPLICATION_VND_KCL_PROV_TEMPLATE_JSON)
                .header(HttpHeaders.ACCEPT, APPLICATION_VND_KCL_PROV_TEMPLATE_JSON)
                .headers(h -> {
                    if (accessTokenValue!=null) h.setBearerAuth(accessTokenValue);
                })
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, response -> {
                    String statusCode = response.statusCode().toString();

                    logTraceResponse(logger,response);

                    return Mono.error(() -> {
                        try {
                            //String b=response.bodyToMono(String.class).toFuture().get();
                            return new IllegalStateException(String.format("4xxClientError %s for input data:\n %s\n", statusCode, new ObjectMapper().writeValueAsString(data)));
                        } catch (JsonProcessingException e) {
                            throw new RuntimeException(e);
                        }
                    });

                })


                .bodyToMono(clazz)
                .onErrorResume(throwable -> {
                    logger.error("\nError issue [{}]\n", throwable.getMessage());
                    return Mono.error(throwable);
                })
                .blockOptional(timeoutIn10Seconds);
    }


    public static void logTraceResponse(Logger log, ClientResponse response) {
        log.error("Response status: {}", response.statusCode());
        log.error("Response headers: {}", response.headers().asHttpHeaders());
        response.bodyToMono(String.class)
                .publishOn(Schedulers.immediate())
                .subscribe(body -> log.error("Response body: {}", body));

    }

    private void acceptedCodecs(ClientCodecConfigurer clientCodecConfigurer) {
        clientCodecConfigurer.customCodecs().encoder(new Jackson2JsonEncoder(new ObjectMapper(), TEXT_HTML));
        clientCodecConfigurer.customCodecs().decoder(new Jackson2JsonDecoder(new ObjectMapper(), TEXT_HTML));
    }

    public <Tout> Optional<Tout> get(String serviceEndpoint, String accept, Class<Tout> clazz, String accessTokenValue) {
        Duration timeoutIn10Seconds = Duration.ofSeconds(10);
        return WebClient
                .builder()

                //.exchangeStrategies(ExchangeStrategies.builder().codecs(this::acceptedCodecs).build())
                .build()

                .method(HttpMethod.GET)
                .uri(serviceEndpoint)
                .accept(MediaType.asMediaType(MimeType.valueOf(accept)))
                .headers(h -> h.setBearerAuth(accessTokenValue))
                .retrieve()
                .bodyToMono(clazz)
                .onErrorResume(throwable -> {
                    logger.error("Error issue [{}]", throwable.getMessage());
                    return Mono.error(throwable);
                })
                .blockOptional(timeoutIn10Seconds);
    }


}
