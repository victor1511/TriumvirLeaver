package com.triumvir.leaver.utils;

import sailpoint.api.Provisioner;
import sailpoint.api.SailPointContext;
import sailpoint.object.Attributes;
import sailpoint.object.ProvisioningPlan;
import sailpoint.object.ProvisioningProject;
import sailpoint.tools.GeneralException;

public class ProvisioningExecutor 
{
	private static ProvisioningExecutor instance = null;
	private ProvisioningProject project = null;
	private Attributes<String, Object> arguments;
	
	private ProvisioningExecutor()
	{
		arguments = new Attributes<String, Object>();
	}
	public static ProvisioningExecutor getInstance()
	{
		if(instance == null)
		{
			instance = new ProvisioningExecutor();
		}
		return instance;
	}
	public void executeProvisioning(ProvisioningPlan plan, SailPointContext context) throws GeneralException
	{
		context = context.getContext();
		arguments.put("optimisticProvisioning", true);
		Provisioner provisioner = new Provisioner(context);
		project = provisioner.compile(plan, arguments);
		provisioner.execute(project);
	}
}
