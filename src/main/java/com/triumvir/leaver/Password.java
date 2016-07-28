package com.triumvir.leaver;

import java.util.List;
import java.util.Map;

import sailpoint.object.Identity;
import sailpoint.object.Link;

public class Password 
{
	private Identity identity;
	
	public Password(Identity identity)
	{
		this.identity = identity;
	}
	
	public Identity changePassword(List<String> appListToReset)
	{ 
		List <Link> accounts =  identity.getLinks();
		if(accounts.isEmpty())
		{
			throw new RuntimeException("The Identity " + identity.getName() + "does not have applications");
		}
		for(Link account : accounts)
		{
			for(String application : appListToReset)
			{
				if(application.equals(account.getName()))
				{
					System.out.println(account.getApplication().getAttributes());
				}
			}
		}
		return identity;
	}
	
	public Identity changePassword(Map<String, String> accountsToChangePass)
	{	
		if(accountsToChangePass.isEmpty())
		{
			throw new RuntimeException("No accounts to change password");
		}
		else
		{
			String newPassword = accountsToChangePass.get("Identity");
			identity.setPassword(newPassword);
		}
		
		//TODO get values from for the other applications.
		
		return identity;
	}
}
