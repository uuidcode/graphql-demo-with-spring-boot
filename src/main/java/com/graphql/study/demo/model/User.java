package com.graphql.study.demo.model;

public class User {
	private Long userId;
	private String name;
	private Integer age;
	private Long balance;

	public Long getBalance() {
	    return this.balance;
	}

	public User setBalance(Long balance) {
	    this.balance = balance;
	    return this;
	}
	public Integer getAge() {
	    return this.age;
	}

	public User setAge(Integer age) {
	    this.age = age;
	    return this;
	}
	public String getName() {
	    return this.name;
	}

	public User setName(String name) {
	    this.name = name;
	    return this;
	}
	public Long getUserId() {
	    return this.userId;
	}

	public User setUserId(Long userId) {
	    this.userId = userId;
	    return this;
	}
}
