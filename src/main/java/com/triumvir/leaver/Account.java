package com.triumvir.leaver;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import sailpoint.api.SailPointContext;
import sailpoint.object.Filter;
import sailpoint.object.Identity;
import sailpoint.object.Link;  // Represent the account "object"
import sailpoint.object.ProvisioningPlan;
import sailpoint.object.ProvisioningPlan.AccountRequest;
import sailpoint.object.QueryOptions;
import sailpoint.tools.GeneralException;

public class Account 
{
	Log log = LogFactory.getLog(Account.class);
	
	public Account() 
	{

	}
	/**
	 * @param identity	the identity to provisioning
	 * @param context	the current context from IIQ
	 * @param exclusionList	applications that will not be disable
	 * @return plan	the provisioning plan
	 * 
	 * */
	public ProvisioningPlan getProvisioninPlan(Identity identity, SailPointContext context, List <String> exclusionList) throws GeneralException
	{
		//TODO Implement an exception list 
		
		System.out.println("Identity " + identity +" context " + context);
		ProvisioningPlan plan = new ProvisioningPlan();
		List <AccountRequest> accountList = new ArrayList<AccountRequest>();
		
 		if(identity != null)
		{
			QueryOptions queryOp = new QueryOptions();
			queryOp.add(Filter.eq("identity", identity));
			
			Iterator<Link> iterator = context.getContext().search(Link.class, queryOp);
			while(iterator.hasNext())
			{
				Link account = (Link) iterator.next();
				AccountRequest accountRequest = new AccountRequest();
				
				accountRequest.setApplication(account.getApplicationName());
				accountRequest.setInstance(account.getInstance());
				accountRequest.setNativeIdentity(account.getNativeIdentity());
				accountRequest.setOperation(AccountRequest.Operation.Disable);	
				accountList.add(accountRequest);	
			}
			
			accountList = getAccountsForProvisioning(accountList, exclusionList);
			plan.setAccountRequests(accountList);
		}
		return plan;
	}
	
	public ProvisioningPlan deleteAccounts(Identity identity, List<String>exceptionList)
	{
		List<Link> accounts = identity.getLinks();
		if(accounts == null)
		{
			throw new RuntimeException(String.format("The Identity %s doesn't have applications", identity.getName()));
		}
		else
		{
			List<AccountRequest> accountList = new ArrayList<AccountRequest>();
			for(Link account : accounts)
			{
				AccountRequest accRequest = new AccountRequest();
				accRequest.setApplication(account.getApplicationName());
				accRequest.setInstance(account.getInstance());
				accRequest.setNativeIdentity(account.getNativeIdentity());
				accRequest.setOperation(AccountRequest.Operation.Delete);
				accountList.add(accRequest);
			}
			ProvisioningPlan plan = new ProvisioningPlan();
			plan.setAccountRequests(accountList);
			return plan;
		}
	}
	
	private List<AccountRequest> getAccountsForProvisioning(List <AccountRequest> accountsList, List <String> exclusionList)
	{
		List <AccountRequest> tempList = new ArrayList<AccountRequest>();
		
		for(int i = 0; i < exclusionList.size(); i += 1)
		{
			for(int j = 0; j < accountsList.size(); j += 1)
			{
				if(exclusionList.get(i).equals(accountsList.get(j).getApplicationName()))
				{
					System.out.println("Excluding... " + accountsList.get(j));
					tempList.add(accountsList.get(j));
				}
			}
		}
		
		accountsList.removeAll(tempList);
		return accountsList;
	}
}
