package net.scholagest.services;

import com.google.inject.Inject;

public class UserService implements IUserService {
	@Inject
	public UserService() {
		
	}
	
	@Override
	public String[] getVisibleModules(String userKey) throws Exception {
		return new String[] {"js/base.js", "js/base-html.js", "modules/teacher.html", "js/teacher.js",
				"modules/student.html", "js/student.js"};
	}
}
