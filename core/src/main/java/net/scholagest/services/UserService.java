package net.scholagest.services;

import com.google.inject.Inject;

public class UserService implements IUserService {
	@Inject
	public UserService() {
		
	}
	
	@Override
	public String[] getVisibleModules(String userKey) throws Exception {
		return new String[] {"modules/teacher.html", "js/teacher.js"};
	}
}
