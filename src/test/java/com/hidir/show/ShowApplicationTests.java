package com.hidir.show;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ShowApplicationTests {

	@Test
	void contextLoads() {
	}

	@Test
	public void applicationContextTest() {
		ShowApplication.main(new String[] {});
	}

}
