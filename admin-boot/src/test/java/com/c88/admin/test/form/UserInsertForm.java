package com.c88.admin.test.form;

import java.util.Date;
import lombok.Data;

@Data
public class UserInsertForm {

	private String name;
	private int age;
	private String address;
	private Date birthDate;

}
