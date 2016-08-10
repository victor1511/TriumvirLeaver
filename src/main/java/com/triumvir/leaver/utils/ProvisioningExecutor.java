package com.triumvir.leaver.utils;

import sailpoint.api.Provisioner;
import sailpoint.api.SailPointContext;
import sailpoint.object.Attributes;
import sailpoint.object.ProvisioningPlan;
import sailpoint.object.ProvisioningProject;
import sailpoint.tools.GeneralException;

public class ProvisioningExecutor {
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
