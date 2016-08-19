package com.triumvir.leaver.utils;

import sailpoint.api.Provisioner;
import sailpoint.api.SailPointContext;
import sailpoint.object.Attributes;
import sailpoint.object.ProvisioningPlan;
import sailpoint.object.ProvisioningProject;
import sailpoint.tools.GeneralException;

public class ProvisioningExecutor {
	private static ProvisioningExecutor instance = null;
	
	public ProvisioningExecutor()
	{
		System.out.println("Esto debería ser desplegado solo una vez.");
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
		Attributes<String, Object> arguments = new Attributes<String, Object>();
		arguments.put("optimisticProvisioning", true);
		Provisioner provisioner = new Provisioner(context);
		ProvisioningProject project = provisioner.compile(plan, arguments);
		provisioner.execute(project);
	}
}
