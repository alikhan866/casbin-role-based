package com.springrest.springrest.services;

import java.util.List;

import com.springrest.springrest.dao.CourseDao;
import com.springrest.springrest.entities.Course;
import org.casbin.adapter.JDBCAdapter;

public interface AuthorizationService {
	public List<Course> getAuthorizedCourses(String uid,List<Course> allCourses,CourseDao courseDao);
	public JDBCAdapter getDatabaseInstance();
}
