package com.agrihusac.SysInvent;

import org.junit.jupiter.api.Test;
class SysInventApplicationTests {

	@Test
	void applicationClassIsAvailableWithoutLoadingSpringContext() {
		org.junit.jupiter.api.Assertions.assertDoesNotThrow(
				() -> Class.forName("com.agrihusac.SysInvent.SysInventApplication"));
	}

}
