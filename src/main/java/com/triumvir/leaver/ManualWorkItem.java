package com.triumvir.leaver;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import sailpoint.api.SailPointContext;
import sailpoint.object.Application;
import sailpoint.object.Identity;
import sailpoint.object.Link;
import sailpoint.tools.GeneralException;

public class ManualWorkItem {
	final static Logger logger = Logger.getLogger(ManualWorkItem.class);
	
	public List<String> getApplicationsWithProvSupport(Identity identity, SailPointContext context) throws GeneralException
	{
		List<Link> accounts = new ArrayList<Link>();
		List<String> appList = new ArrayList<String>();
		
		accounts = identity.getLinks();
		
		if(accounts.isEmpty())
		{
			throw new RuntimeException("This identity " + identity.getName() + " doesn't has any accounts");
		}
		else
		{
			for(Link account : accounts)
			{
				appList.add(account.getApplicationName());
			}
			
			return appListProvisioningSupport(appList, context);
		}		
	}
	
	private List <String> appListProvisioningSupport(List <String> applicationList, SailPointContext context) throws GeneralException
	{
		List <String>temporalList = new ArrayList<String>();
		
		for(String application : applicationList)
		{
			Application app = new Application();
			app = context.getContext().getObjectByName(Application.class, application);
			if(!app.isSupportsProvisioning())
			{
				logger.debug("This app doesn't support provisioning " + application);
				temporalList.add(application);
			}
		}
		applicationList.removeAll(temporalList);
		
		return applicationList;
	}
}
