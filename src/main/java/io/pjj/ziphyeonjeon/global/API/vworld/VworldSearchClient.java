package io.pjj.ziphyeonjeon.global.API.vworld;

import io.pjj.ziphyeonjeon.global.API.common.ExternalApiProperties;
import io.pjj.ziphyeonjeon.global.API.vworld.dto.search.VworldSearchResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class VworldSearchClient {

    private final WebClient webClient;
    private final ExternalApiProperties props;

    public VworldSearchClient(ExternalApiProperties props) {
        this.props = props;
        this.webClient = WebClient.builder().baseUrl(props.getVworld().getSearchBaseUrl()).build();
    }

    public VworldSearchResponse searchJuso(String query) {
        // category=Juso (도로명주소)
        String uri = UriComponentsBuilder.fromPath("/search.do")
                .queryParam("category", "Juso")
                .queryParam("q", query)
                .queryParam("output", "json")
                .queryParam("pageUnit", 10)
                .queryParam("pageIndex", 1)
                .queryParam("apiKey", props.getVworld().getApiKey())
                .build(true)
                .toUriString();

        return webClient.get().uri(uri).retrieve()
                .bodyToMono(VworldSearchResponse.class).block();
    }

    public VworldSearchResponse searchJibun(String query) {
        // category=Jibun (지번)
        String uri = UriComponentsBuilder.fromPath("/search.do")
                .queryParam("category", "Jibun")
                .queryParam("q", query)
                .queryParam("output", "json")
                .queryParam("pageUnit", 10)
                .queryParam("pageIndex", 1)
                .queryParam("apiKey", props.getVworld().getApiKey())
                .build(true)
                .toUriString();

        return webClient.get().uri(uri).retrieve()
                .bodyToMono(VworldSearchResponse.class).block();
    }
}
