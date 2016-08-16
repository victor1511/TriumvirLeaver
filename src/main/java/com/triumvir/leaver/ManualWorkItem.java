package com.triumvir.leaver;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import sailpoint.api.SailPointContext;
import sailpoint.object.Application;
import sailpoint.object.ApprovalItem;
import sailpoint.object.ApprovalSet;
import sailpoint.object.Attributes;
import sailpoint.object.Identity;
import sailpoint.object.Link;
import sailpoint.object.WorkItem;
import sailpoint.object.WorkItem.Level;
import sailpoint.persistence.Sequencer;
import sailpoint.tools.GeneralException;

public class ManualWorkItem {
	final static Logger logger = Logger.getLogger("sailpoint");
	
	private List<String> getApplicationsWithNoProvSupport(Identity identity, SailPointContext context) throws GeneralException
	{
		List<Link> accounts = new ArrayList<Link>();
		List<String> appList = new ArrayList<String>();
		
		accounts = identity.getLinks();
		
		if(accounts.isEmpty())
		{
			String message = String.format("This identity %s doesn't has any accounts", identity.getName());
			logger.fatal(message);
			throw new RuntimeException(message);
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
			if(app.isSupportsProvisioning())
			{
				temporalList.add(application);
			}
		}
		applicationList.removeAll(temporalList);		
		return applicationList;
	}
	
	public WorkItem createWorkItem(Identity identity, Identity owner, SailPointContext context, Identity launcher) throws GeneralException 
	{
		if(owner == null)
		{
			owner = identity.getManager();
		}
		
		WorkItem  workItem = new WorkItem();
		Sequencer sequencer = new Sequencer(); // Get the request number for the WorkItem.
		context = context.getContext();		
		workItem.setType(WorkItem.Type.ManualAction);
		workItem.setName(sequencer.generateId(context.getContext(), workItem));
		workItem.setTarget(identity);
		workItem.setTargetClass(Identity.class.getName());
		workItem.setRequester(launcher);
		workItem.setLevel(Level.High);
		workItem.setOwner(owner);
		workItem.setTargetClass(sailpoint.object.Identity.class);
		workItem.setHandler(sailpoint.api.Workflower.class);
		workItem.setRenderer("lcmManualActionsRenderer.xhtml");
		workItem.setDescription("Manual actions for the applications below");
		
		Attributes<String, Object> wItemAttributes = new Attributes<String, Object>();
		ApprovalSet approvalSet = new ApprovalSet();
		
		List <String> appsWithNoProv = getApplicationsWithNoProvSupport(identity, context);
		for(String app : appsWithNoProv)
		{
			ApprovalItem approvalItem = new ApprovalItem();
			Application newApp = context.getObjectByName(Application.class, app);
			approvalItem.setApplication(newApp);
			approvalItem.setNativeIdentity(identity.getName());
			approvalItem.setOperation("Delete"); 
			approvalItem.setValue(newApp);
			approvalSet.add(approvalItem);
		}
		
		wItemAttributes.put("approvalSet", approvalSet);
		workItem.setAttributes(wItemAttributes);
	
		return workItem;
	}
}
