package io.pjj.ziphyeonjeon.global.API.vworld.landprice;

import io.pjj.ziphyeonjeon.global.API.common.ExternalApiProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class VworldOfficialLandPriceClient {

    private final WebClient webClient;
    private final ExternalApiProperties props;

    public VworldOfficialLandPriceClient(ExternalApiProperties props) {
        this.props = props;
        this.webClient = WebClient.builder()
                .baseUrl(props.getVworld().getBaseUrl())
                .build();
    }

    /** PNU로 공시지가 raw JSON 조회 (엔드포인트/파라미터는 실제 스펙에 맞게 수정) */
    public String getOfficialLandPriceRaw(String pnu) {
        String uri = "/req/landprice?key=" + props.getVworld().getApiKey()
                + "&pnu=" + pnu
                + "&format=json";

        return webClient.get()
                .uri(uri)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }
}
