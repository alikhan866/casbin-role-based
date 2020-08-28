package com.springrest.springrest.services;

import java.util.List;

import org.casbin.adapter.JDBCAdapter;
import org.casbin.jcasbin.main.Enforcer;
import org.springframework.beans.factory.annotation.Autowired;

import com.springrest.springrest.dao.CourseDao;
import com.springrest.springrest.entities.Course;
import com.mysql.cj.jdbc.MysqlDataSource;
public class AuthorizationServiceImpl implements AuthorizationService {
	@Override
	public List<Course> getAuthorizedCourses(String uid, List<Course> allCourses, CourseDao courseDao) {
		String modelPath = System.getProperty("user.dir") + "/model.conf";
		String policyPath = System.getProperty("user.dir") + "/policy.csv";

		Enforcer enforcer = new Enforcer(modelPath, policyPath);

		String sub = uid; // the user that wants to access a resource.
		String obj[] = new String[allCourses.size()]; // the resource that is going to be accessed.
		String act = "read"; // the operation that the user performs on the resource.

		for (int i = 0; i < allCourses.size(); i++) {
			obj[i] = Long.toString(allCourses.get(i).getId());
		}

		for (int i = 0; i < obj.length; i++) {

			if (enforcer.enforce(sub, obj[i], act) == false) {
				allCourses.remove(courseDao.getOne(Long.parseLong(obj[i])));
			}
		}
		return allCourses;
	}

	@Override
	public JDBCAdapter getDatabaseInstance() {
		String driver = "com.mysql.cj.jdbc.Driver";
        String url = "jdbc:mysql://localhost:3306/db_name";
        String username = "root";
        String password = "123456";
        // The adapter will use the table named "casbin_rule".
        // Use driver, url, username and password to initialize a JDBC adapter.
        JDBCAdapter a = new JDBCAdapter(driver, url, username, password); 
        
        // Recommend use DataSource to initialize a JDBC adapter.
        // Implementer of DataSource interface, such as hikari, c3p0, durid, etc.
        MysqlDataSource dataSource = new MysqlDataSource();
        dataSource.setURL(url);
        dataSource.setUser(username);
        dataSource.setPassword(password);

        a = JDBCAdapter(dataSource);        
	}
}
