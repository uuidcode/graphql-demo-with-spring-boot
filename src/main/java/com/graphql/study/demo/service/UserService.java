package com.graphql.study.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.graphql.study.demo.model.User;
import com.graphql.study.demo.dao.UserDao;

@Service
public class UserService {
	@Autowired
	private UserDao userDao;
	
	public User getUser(Long userId) {
		return userDao.getUser(userId);
	}
}
