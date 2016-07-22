package com.triumvir.leaver;

import java.util.ArrayList;
import java.util.List;

import sailpoint.api.SailPointContext;
import sailpoint.object.AttributeAssignment;
import sailpoint.object.Identity;
import sailpoint.object.ProvisioningPlan;
import sailpoint.object.ProvisioningPlan.AccountRequest;
import sailpoint.object.ProvisioningPlan.AttributeRequest;
import sailpoint.tools.GeneralException;

public class Entitlement 
{
	public ProvisioningPlan getProvisioninPlan(Identity identity, SailPointContext context, List <String> exclusionList) throws GeneralException
	{
		System.out.println("Identity -> " + identity.getName());
		ProvisioningPlan plan = new ProvisioningPlan();
		plan.setIdentity(identity);

		List <AttributeAssignment> entitlementsList = new ArrayList<AttributeAssignment>();
		List <AccountRequest> listAccountRequest = new ArrayList<AccountRequest>();
		
		entitlementsList = identity.getAttributeAssignments();
		for(AttributeAssignment entitlement : entitlementsList)
		{
			AccountRequest accountRequest = new AccountRequest();
			accountRequest.setApplication(entitlement.getApplicationName());
			accountRequest.setNativeIdentity(entitlement.getNativeIdentity());
			accountRequest.setOperation(AccountRequest.Operation.Modify);
			
			AttributeRequest attributeRequest = new AttributeRequest();
			attributeRequest.setName(entitlement.getName());
			attributeRequest.setValue(entitlement.getValue());
			attributeRequest.setOperation(ProvisioningPlan.Operation.Remove);
			accountRequest.add(attributeRequest);
			listAccountRequest.add(accountRequest);
		}
		
		plan.setAccountRequests(listAccountRequest);
		System.out.println(plan.toXml());
		return plan;
	}
	
}
