package com.triumvir.leaver;

import java.util.List;

import org.apache.log4j.Logger;

import sailpoint.object.Identity;
import sailpoint.object.Link;
import sailpoint.object.ProvisioningPlan;
import sailpoint.object.ProvisioningPlan.AccountRequest;
import sailpoint.object.ProvisioningPlan.AttributeRequest;
import sailpoint.object.ProvisioningResult;

public class Email 
{
	final static Logger logger = Logger.getLogger("sailpoint");
	private Identity identity;

	public Email(Identity identity)
	{
		this.identity = identity;
	}
	
	public ProvisioningPlan  hideEmail()
	{
		List <Link> accounts = identity.getLinks();
		Link primaryAdLink = null;
		for(Link account : accounts)
		{
			if("Active Directory - Direct".equals(account.getApplication().getType()))
			{
				primaryAdLink = account;
			}
		}
		
		ProvisioningPlan plan = new ProvisioningPlan();
		if(primaryAdLink ==  null)
		{	
			String message = String.format("The identity %s doesn't has an AD account", identity.getName());
			logger.fatal(message);
			throw new RuntimeException(message);
		}
		else
		{
			AccountRequest adAcctReq = new AccountRequest();
			adAcctReq.setApplication(primaryAdLink.getApplicationName());
			adAcctReq.setNativeIdentity(primaryAdLink.getNativeIdentity());
			adAcctReq.setResult(new ProvisioningResult());
			adAcctReq.setOperation(AccountRequest.Operation.Modify);
			adAcctReq.add(new AttributeRequest("msExchHideFromAddressLists", ProvisioningPlan.Operation.Set,Boolean.TRUE));
			plan.add(adAcctReq);
		}
		
		return plan;
	}
}
