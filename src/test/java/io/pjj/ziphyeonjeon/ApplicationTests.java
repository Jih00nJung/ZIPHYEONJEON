package io.pjj.ziphyeonjeon;

import io.pjj.ziphyeonjeon.global.config.AddressCodeMap;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ApplicationTests {

	@Test
	void addressToCode() {
		AddressCodeMap map = new AddressCodeMap();
		map.init();

		String address = "서울특별시 강남구 개포동";
		String code = map.getCode(address);

		System.out.println("검색 주소: [" + address + "]");
		System.out.println("결과 코드: [" + code + "]");

		Assertions.assertEquals("1168010300", code);
	}

}
