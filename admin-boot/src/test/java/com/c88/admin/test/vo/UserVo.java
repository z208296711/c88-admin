package com.c88.admin.test.vo;

import java.math.BigInteger;
import java.util.Date;
import lombok.Data;

@Data
public class UserVo {

	private BigInteger id;
	private String name;
	private int age;
	private String address;
	private Date birthDate;

}
