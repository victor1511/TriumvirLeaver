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
			plan.setIdentity(identity);
			List<AccountRequest> accReqList = new ArrayList<AccountRequest>();
			//get the AD or LDAP account.
			for(Link account : accounts)
			{
				// switch because maybe we need a different configuration for each app.
				switch(account.getApplication().getType())
				{
					case "OpenLDAP - Direct":
						plan.setAccountRequests(setNewAttribute(account, accReqList));
						break;
					case "IBM Tivoli DS - Direct":
						plan.setAccountRequests(setNewAttribute(account, accReqList));
						break;
					case "Novell eDirectory - Direct":
						plan.setAccountRequests(setNewAttribute(account, accReqList));
						break;
					case "LDAP":
						plan.setAccountRequests(setNewAttribute(account, accReqList));
						break;
					case "ADAM - Direct":
						plan.setAccountRequests(setNewAttribute(account, accReqList));
						break;
					case "Oracle Internet Directory - Direct":
						plan.setAccountRequests(setNewAttribute(account, accReqList));
						break;
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
		
		accRequest.add(new AttributeRequest(attrSplited[0], ProvisioningPlan.Operation.Set, newValue));
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
