package com.c88.admin.test;

import com.c88.admin.AdminBootApplication;
import com.c88.admin.test.component.LogOperationExampleData;
import com.c88.admin.test.form.UserDeleteForm;
import com.c88.admin.test.form.UserInsertForm;
import com.c88.admin.test.form.UserUpdateForm;
import java.math.BigInteger;
import java.text.ParseException;
import org.apache.commons.lang3.time.DateUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest(classes = {AdminBootApplication.class, TestConfig.class})
@ContextConfiguration
@TestPropertySource("classpath:application.yml")
@ActiveProfiles({"local"})
public class LogOperationTest {

	@Autowired
	private LogOperationExampleData exampleData;

	@Test
	public void basicLog() {
		exampleData.oneLog("101", "index10", 459);
	}

	@Test
	public void multipleLogs() {
		exampleData.manyLogs("202", "index20", 883);
	}

	@Test
	public void insertUser() throws ParseException {
		UserInsertForm newUser = new UserInsertForm();
		newUser.setName("John");
		newUser.setAge(20);
		newUser.setAddress("台北市健康路156號");
		newUser.setBirthDate(DateUtils.parseDate("1990-03-05 15:15:15", "yyyy-MM-dd hh:mm:ss"));
		exampleData.insert(newUser);
	}

	@Test
	public void deleteUser() {
		UserDeleteForm deletedUser = new UserDeleteForm();
		deletedUser.setId(BigInteger.valueOf(200l));
		exampleData.deleteWithOutput(deletedUser);
		exampleData.deleteWithNoOutput(deletedUser);
	}

	@Test
	public void updateUser() throws ParseException {
		UserUpdateForm newUser = new UserUpdateForm();
		newUser.setId(BigInteger.valueOf(5l));
		newUser.setName("Nicole");
		newUser.setAge(33);
		newUser.setAddress("新北市中山路150號");
		exampleData.modifyWithOutPut(newUser);
//		exampleData.modifyWithNoOutPut(newUser);
	}

}
