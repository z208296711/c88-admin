package com.c88.admin.test.form;

import java.math.BigInteger;
import lombok.Data;

@Data
public class UserUpdateForm {

	private BigInteger id;
	private String name;
	private int age;
	private String address;
}
