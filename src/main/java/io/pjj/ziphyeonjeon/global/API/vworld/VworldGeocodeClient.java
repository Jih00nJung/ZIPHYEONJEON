package io.pjj.ziphyeonjeon.global.API.vworld;
//기능: address → 좌표(lat/lng), 행정코드/지번정보/pnu 후보 가져오기

import io.pjj.ziphyeonjeon.global.API.common.ExternalApiProperties;
import io.pjj.ziphyeonjeon.global.API.vworld.dto.geocode.VworldGeocodeResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class VworldGeocodeClient {

    private final WebClient webClient;
    private final ExternalApiProperties props;

    public VworldGeocodeClient(ExternalApiProperties props) {
        this.props = props;
        this.webClient = WebClient.builder().baseUrl(props.getVworld().getBaseUrl()).build();
    }

    public VworldGeocodeResponse getCoord(String address, String type) {
        // type: ROAD or PARCEL (vworld 문서 기준)
        String uri = UriComponentsBuilder.fromPath("/req/address")
                .queryParam("service", "address")
                .queryParam("request", "getCoord")
                .queryParam("format", "json")
                .queryParam("crs", "epsg:4326")
                .queryParam("key", props.getVworld().getApiKey())
                .queryParam("type", type)        // "ROAD" or "PARCEL"
                .queryParam("address", address)
                .build(true)
                .toUriString();

        return webClient.get()
                .uri(uri)
                .retrieve()
                .bodyToMono(VworldGeocodeResponse.class)
                .block();
    }
}

