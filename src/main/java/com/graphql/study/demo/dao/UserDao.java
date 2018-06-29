package com.graphql.study.demo.dao;

import org.apache.ibatis.annotations.Mapper;

import com.graphql.study.demo.model.User;

@Mapper
public interface UserDao {
	User getUser(Long usrId);
}
