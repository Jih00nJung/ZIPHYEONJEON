package io.pjj.ziphyeonjeon.interaction.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JwtMockUtil {

    /**
     * 임시 JWT 파싱 메서드입니다.
     * 로그인 담당자가 완료하면 여기를 진짜 JWT 파싱 로직으로 교체하면 됩니다.
     * @param token HTTP Header의 "Bearer [token]"
     * @return 임의의 사용자 ID (현재는 고정값 1L 리턴)
     */
    public Long extractUserIdFromToken(String token) {
        log.warn("Mocking JWT parser. Received token: {}", token);
        return 1L; // 임시 유저 아이디 리턴
    }
}
