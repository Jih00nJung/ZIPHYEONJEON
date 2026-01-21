package io.pjj.ziphyeonjeon;

import io.pjj.ziphyeonjeon.global.API.ApiDisaster;
import io.pjj.ziphyeonjeon.global.config.AddressCodeMap;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

class ApplicationTests {

    @Test
    void apiTest() {
        RestTemplate restTemplate = new RestTemplate();
        ApiDisaster apiDisaster = new ApiDisaster(restTemplate);

        String serviceKey = "8Z1X40MD48205H28";
        ReflectionTestUtils.setField(apiDisaster, "serviceKey", serviceKey);

        String region = "서울특별시";
        String result = apiDisaster.fetchAllDisasterData(region);

        // 4. 결과 출력
        System.out.println("================ API RAW RESPONSE ================");
        System.out.println(result);
        System.out.println("==================================================");
    }
}
