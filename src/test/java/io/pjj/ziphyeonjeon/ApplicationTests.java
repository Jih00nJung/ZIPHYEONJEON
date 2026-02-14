package io.pjj.ziphyeonjeon;

import io.pjj.ziphyeonjeon.global.API.ApiLoan;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

class ApplicationTests {

    @Test
    void apiTest() {
        RestTemplate restTemplate = new RestTemplate();
        ApiLoan apiLoan = new ApiLoan(restTemplate);

        String serviceKey = "5R1W25iameoAVaGDMFaP03PhY4t3LTsoLrt00XESCDquGBzGjfq7YD%2F7LIbb1o0V0km%2FwLqK5pluQch%2BwaiEAQ%3D%3D";
        ReflectionTestUtils.setField(apiLoan, "serviceKey", serviceKey);

        Map<String, String> result = apiLoan.fetchAllLoanData();

        // 4. 결과 출력
        System.out.println("================ API RAW RESPONSE ================");
        System.out.println(result);
        System.out.println("==================================================");
    }
}
