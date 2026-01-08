package io.pjj.ziphyeonjeon.global.API.seoul;

import io.pjj.ziphyeonjeon.global.API.common.ExternalApiProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class SeoulOpenApiClient {

    private final WebClient webClient;
    private final ExternalApiProperties props;

    public SeoulOpenApiClient(ExternalApiProperties props) {
        this.props = props;
        this.webClient = WebClient.builder().baseUrl(props.getSeoul().getBaseUrl()).build();
    }

    /**
     * 예: 서비스명 tblnOpendataRtmsV (너 캡쳐 기준)
     * TYPE: json
     * START/END: 1/100 같은 페이징
     * 나머지 필터는 "path segment"로 계속 뒤에 붙는 구조라서 String으로 받는 게 편함
     */
    public String getRealTradeRaw(String serviceName, int start, int end, String... tailSegments) {
        String key = props.getSeoul().getApiKey();

        StringBuilder path = new StringBuilder();
        path.append("/").append(key)
            .append("/json/")
            .append(serviceName)
            .append("/").append(start)
            .append("/").append(end);

        if (tailSegments != null) {
            for (String seg : tailSegments) {
                if (seg != null && !seg.isBlank()) {
                    path.append("/").append(seg);
                }
            }
        }

        return webClient.get()
                .uri(path.toString())
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }
}
