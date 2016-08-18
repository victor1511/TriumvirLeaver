package com.triumvir.leaver;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import sailpoint.object.Identity;
import sailpoint.object.Link;
import sailpoint.object.ProvisioningPlan;
import sailpoint.object.ProvisioningPlan.AccountRequest;
import sailpoint.object.ProvisioningPlan.AttributeRequest;
import sailpoint.tools.GeneralException;

public class Password 
{
	private Identity identity;
	List <AccountRequest> accountsRequestList = new ArrayList<AccountRequest>();
	Log log = LogFactory.getLog("sailpoint");
	
	public Password(Identity identity)
	{
		this.identity = identity;
	}
		
	public ProvisioningPlan changePassword(Map<String, String> accountsToChangePass) throws GeneralException
	{ 
		List <Link> accounts =  identity.getLinks();
		ProvisioningPlan plan = new ProvisioningPlan();
		
		if(accounts.isEmpty())
		{
			throw new RuntimeException(String.format("The Identity %s does not have applications", identity.getName()));
		}
		
		for(String key : accountsToChangePass.keySet())
		{
			if(key != null)
			{	
				String [] values = accountsToChangePass.get(key).split(","); 
				provisioning(key, values);
			}		
		}
		plan.setAccountRequests(accountsRequestList);
		System.out.println(plan.toXml());
		return plan;
	}
	
	public Identity changeIdentityPassword(Map<String, String> accountsToChangePass)
	{	
		String newPassword = accountsToChangePass.remove("Identity");
		identity.setPassword(newPassword);
		return identity;
	}
	
	private void provisioning(String appName, String [] values) throws GeneralException
	{
		AccountRequest acRequest = new AccountRequest();
		AttributeRequest atRequest = new AttributeRequest();	
		String nativeIdentity = "";
		List <Link> accounts = identity.getLinks();
	
		for(Link account : accounts)
		{
			if(account.getApplicationName().equals(appName))
			{
				nativeIdentity = account.getNativeIdentity();
			}
		}
		
		acRequest.setApplication(appName);
		acRequest.setNativeIdentity(nativeIdentity);
		acRequest.setOperation(AccountRequest.Operation.Modify);
		
		atRequest.setName(values[2]);
		atRequest.setValue(values[1]);
		atRequest.setOperation(ProvisioningPlan.Operation.Set);
		acRequest.add(atRequest);
		
		accountsRequestList.add(acRequest);
	}
}
