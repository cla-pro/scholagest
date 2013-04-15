package net.scholagest.app.rest;

import net.scholagest.managers.impl.CoreNamespace;

public class ScholagestNamespace extends CoreNamespace {
	public static final String scholagestKeyspace = "scholagestKeyspace";
	
	//Session
	public static final String tSession = scholagestNs + "#tSession";
	private static final String pSessionNs = "pSession";
	public static final String pSessionExpirationDate = pSessionNs
			+ "ExpirationDate";
	public static final String pSessionUserType = pSessionNs + "UserType";
}
