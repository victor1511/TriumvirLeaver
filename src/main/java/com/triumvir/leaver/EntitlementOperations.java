package com.triumvir.leaver;

import java.util.ArrayList;
import java.util.List;

import sailpoint.api.SailPointContext;
import sailpoint.object.Entitlement;
import sailpoint.object.Identity;
import sailpoint.object.Link;
import sailpoint.object.ProvisioningPlan;
import sailpoint.object.ProvisioningPlan.AccountRequest;
import sailpoint.object.ProvisioningPlan.AttributeRequest;
import sailpoint.tools.GeneralException;

public class EntitlementOperations 
{
	final String [] LDAP_APPS = {"OpenLDAP - Direct", "IBM Tivoli DS - Direct", "Novell eDirectory - Direct",
							"LDAP", "ADAM - Direct", "Oracle Internet Directory - Direct"}; 
	
	public ProvisioningPlan getDeleteEntitlementProvisioninPlan(Identity identity, SailPointContext context, List <String> exclusionList) throws GeneralException 
	{	//TODO Delete the context parameter.
		List <AccountRequest> listAccountRequest = new ArrayList<AccountRequest>();	
		List<Link> accounts = identity.getLinks();
		for(Link account : accounts)
		{
			//For each account, check the entitlement list and create an AccountRequest.
			List<Entitlement> entitlements = account.getEntitlements(null, null);
			if(entitlements != null)
			{
				for(Entitlement entitlement : entitlements)
				{
					listAccountRequest = createAccountRequests(account, entitlement, listAccountRequest);
				}
			}
			
		}
		ProvisioningPlan plan = new ProvisioningPlan();
		plan.setIdentity(identity);
		plan.setAccountRequests(listAccountRequest);
		return plan;
	}
	
	public ProvisioningPlan getLdapEntitlements(Identity identity) throws GeneralException
	{
		List<Link> accounts = identity.getLinks();
		List<AccountRequest> listAccountRequest = new ArrayList<AccountRequest>();
		for(Link account : accounts)
		{
			for(String appType : LDAP_APPS)
			{
				if(appType.equals(account.getApplication().getType())) // If the account is LDAP Type, get entitlements.
				{
					for(Entitlement entitlement : account.getEntitlements(null, null))
					{
						listAccountRequest = createAccountRequests(account, entitlement, listAccountRequest);
					}
				}
			}
		}
		ProvisioningPlan plan = new ProvisioningPlan();
		plan.setAccountRequests(listAccountRequest);
		plan.setIdentity(identity);
		
		return plan;
	}
	private List<AccountRequest> createAccountRequests(Link account, Entitlement entitlement, List<AccountRequest> listAccountRequest)
	{
		AccountRequest accRequest = new AccountRequest();
		accRequest.setApplication(account.getApplicationName());
		accRequest.setNativeIdentity(account.getNativeIdentity());
		accRequest.setOperation(AccountRequest.Operation.Modify);
	
		AttributeRequest attributeRequest = new AttributeRequest();
		attributeRequest.setName(entitlement.getAttributeName());
		attributeRequest.setValue(entitlement.getAttributeValue());
		attributeRequest.setOperation(ProvisioningPlan.Operation.Remove);
		accRequest.add(attributeRequest);
		listAccountRequest.add(accRequest);
		return listAccountRequest;
	}
}
