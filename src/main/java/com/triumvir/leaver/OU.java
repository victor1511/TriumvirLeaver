package com.triumvir.leaver;

import java.util.ArrayList;
import java.util.List;

import sailpoint.object.Identity;
import sailpoint.object.Link;
import sailpoint.object.ProvisioningPlan;
import sailpoint.object.ProvisioningPlan.AccountRequest;
import sailpoint.object.ProvisioningPlan.AttributeRequest;

public class OU 
{
	Identity identity;
	String [] attrSplited;
	
	public OU(Identity identity)
	{
		this.identity = identity;
	}
	
	public ProvisioningPlan moveOU(String attributeValue)
	{
		
		if(attributeValue == null)
		{
			throw new RuntimeException(String.format("The attribute for the identity %s is null", identity.getName()));
		}
		else
		{
			attrSplited = attributeValue.split(",");
			
			List <Link> accounts = identity.getLinks();
			ProvisioningPlan plan = new ProvisioningPlan();
			List<AccountRequest> accReqList = new ArrayList<AccountRequest>();
			//get the AD or LDAP account.
			for(Link account : accounts)
			{
				if("Active Directory - Direct".equals(account.getApplication().getType()))
				{
					plan.setAccountRequests(setNewAttribute(account, accReqList));
				} else if("OpenLDAP - Direct".equals(account.getApplication().getType()))
				{
					plan.setAccountRequests(setNewAttribute(account, accReqList));
				}	
			}
			return plan;
		}
	}
	
	private List<AccountRequest> setNewAttribute(Link account, List<AccountRequest>accReqList)
	{	
		String newValue = nativeIdentityString(account.getNativeIdentity());
		AccountRequest accRequest = new AccountRequest();
		accRequest.setApplication(account.getApplicationName());
		accRequest.setNativeIdentity(account.getNativeIdentity());
		accRequest.setOperation(AccountRequest.Operation.Modify);
		
		AttributeRequest attRequest = new AttributeRequest();
		attRequest.setName(attrSplited[0]);
		attRequest.setValue(newValue);
		attRequest.setOperation(ProvisioningPlan.Operation.Set);
		accRequest.add(attRequest);
		accReqList.add(accRequest);
		return accReqList;
	}
	
	private String nativeIdentityString(String nativeIdentity)
	{
		String [] stringSplited = nativeIdentity.split(",");
		StringBuilder resultNativeIdentity = new StringBuilder();
		
		for(String element : stringSplited)
		{
			if(element.contains("cn"))
			{
				element = "cn=" + attrSplited[1];
				resultNativeIdentity.append(element);
				resultNativeIdentity.append(",");
			}else if(element.contains("ou"))
			 {
				 element = "ou=" + attrSplited[0];
				 resultNativeIdentity.append(element);
			 }else
			 {
				resultNativeIdentity.append(",");
				resultNativeIdentity.append(element);	
			 } 
			
		}
		return resultNativeIdentity.toString();
	}
}
