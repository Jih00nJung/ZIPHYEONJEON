package io.pjj.ziphyeonjeon.RiskAnalysis.dto;

// 최근 외부 라이브러리 의존도를 낮추려는 목적으로 lombok을 사용하지 않고, record 클래스를 사용한다고 함.
// 어노테이션 이것저것 안 적어도 되니 좋긴하네

public class RiskRequestDTO {

    public record DisasterRequest(String Address) {

    }

}
