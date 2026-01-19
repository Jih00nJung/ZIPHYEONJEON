package io.pjj.ziphyeonjeon.PriceSearch.service;

import io.pjj.ziphyeonjeon.global.API.seoul.SeoulOpenApiClient;
import io.pjj.ziphyeonjeon.global.API.vworld.VworldGeocodeClient;
import io.pjj.ziphyeonjeon.global.API.vworld.VworldSearchClient;
import io.pjj.ziphyeonjeon.global.API.vworld.dto.geocode.VworldGeocodeResponse;
import io.pjj.ziphyeonjeon.global.API.vworld.dto.search.VworldSearchResponse;
import io.pjj.ziphyeonjeon.global.API.vworld.landprice.VworldOfficialLandPriceClient;
import org.springframework.stereotype.Service;

@Service
public class PriceSearchService {

    private final VworldGeocodeClient vworldGeocodeClient;
    private final VworldSearchClient vworldSearchClient;
    private final VworldOfficialLandPriceClient vworldOfficialLandPriceClient;
    private final SeoulOpenApiClient seoulOpenApiClient;

    public PriceSearchService(
            VworldGeocodeClient vworldGeocodeClient,
            VworldSearchClient vworldSearchClient,
            VworldOfficialLandPriceClient vworldOfficialLandPriceClient,
            SeoulOpenApiClient seoulOpenApiClient) {
        this.vworldGeocodeClient = vworldGeocodeClient;
        this.vworldSearchClient = vworldSearchClient;
        this.vworldOfficialLandPriceClient = vworldOfficialLandPriceClient;
        this.seoulOpenApiClient = seoulOpenApiClient;
    }

    public String getOfficialLandPriceByAddress(String address) {
        // 1) 좌표
        VworldGeocodeResponse geo = vworldGeocodeClient.getCoord(address, "ROAD");
        System.out.println("Geocoding Result: " + geo);

        // 2) PNU (도로명으로 안 나오면 Jibun으로 fallback)
        VworldSearchResponse search = vworldSearchClient.searchJuso(address);
        String pnu = null;

        if (search != null && search.result != null && search.result.items != null && !search.result.items.isEmpty()) {
            pnu = search.result.items.get(0).pnu;
        }
        if (pnu == null) {
            VworldSearchResponse search2 = vworldSearchClient.searchJibun(address);
            if (search2 != null && search2.result != null && search2.result.items != null
                    && !search2.result.items.isEmpty()) {
                pnu = search2.result.items.get(0).pnu;
            }
        }

        if (pnu == null) {
            return "PNU를 찾지 못했습니다. 주소가 너무 추상적이거나 검색 결과가 없습니다.";
        }

        // 3) 공시지가 (여기 uri 확정되면 정상 동작)
        return vworldOfficialLandPriceClient.getOfficialLandPriceRaw(pnu);
    }

    public String getSeoulRealTradeSample(String rcptYr, String cggCd) {
        // 서비스명은 너 캡쳐 기준 (tblnOpendataRtmsV)
        // tailSegments는 문서에 맞게 순서대로 붙이면 됨
        return seoulOpenApiClient.getRealTradeRaw(
                "tblnOpendataRtmsV",
                1,
                50,
                rcptYr, // 접수년도
                cggCd // 자치구코드 등
        );
    }
}
