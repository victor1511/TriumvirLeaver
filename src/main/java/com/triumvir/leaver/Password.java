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
		
	public Identity getIdentity() 
	{
		return identity;
	}

	public ProvisioningPlan changePassword(Map<String, String> accountsToChangePass) throws GeneralException
	{ 
		if(accountsToChangePass.containsKey("Identity"))
		{
			changeIdentityPassword(accountsToChangePass);
		}
		
		List <Link> accounts =  identity.getLinks();
		if(accounts.isEmpty())
		{
			throw new RuntimeException(String.format("The Identity %s does not have applications", identity.getName()));
		}
		
		for(String key : accountsToChangePass.keySet())
		{
			if(key != null && !key.equals("Identity"))
			{	
				String [] values = accountsToChangePass.get(key).split(","); 
				provisioning(key, values);
			}		
		}
		
		ProvisioningPlan plan = new ProvisioningPlan();
		plan.setIdentity(identity);
		plan.setAccountRequests(accountsRequestList);
		System.out.println(plan.toXml());
		return plan;
	}
	
	private void changeIdentityPassword(Map<String, String> accountsToChangePass)
	{	
		String newPassword = accountsToChangePass.get("Identity");
		identity.setPassword(newPassword);
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
		
		atRequest.setName(values[0]);
		atRequest.setValue(values[1]);
		atRequest.setOperation(ProvisioningPlan.Operation.Set);
		acRequest.add(atRequest);
		
		accountsRequestList.add(acRequest);
	}
}
