package com.triumvir.leaver.utils;

import sailpoint.api.SailPointContext;
import sailpoint.object.Identity;
import sailpoint.object.WorkItem;
import sailpoint.tools.GeneralException;

public class ObjectSaver
{
	private static ObjectSaver instance = null;

	public static ObjectSaver getInstance()
	{
		if(instance == null)
		{
			instance = new ObjectSaver();
		}
		return instance;
	}
	
	public void saveIdentityChanges(SailPointContext context, Identity identity) throws GeneralException
	{	
		context = context.getContext();
		context.saveObject(identity);
		context.commitTransaction();
	}
	
	public void saveManualWorkItem(SailPointContext context, WorkItem workitem) throws GeneralException
	{
		context = context.getContext();
		context.saveObject(workitem);
		context.commitTransaction();
	}
}
