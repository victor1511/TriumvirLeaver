package com.triumvir.leaver.utils;
import java.util.ArrayList;
import java.util.List;

import sailpoint.object.AttributeAssignment;
import sailpoint.object.ProvisioningPlan.AccountRequest;

public class ListExclude {
	
	public List<AccountRequest> getAccounts(List <AccountRequest> accountsList, List <String> exclusionList)
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
	
	public List<AttributeAssignment> getEntitlements(List<AttributeAssignment>entitlementList, List<String>exclusionList)
	{
		List <AttributeAssignment> tempList = new ArrayList<AttributeAssignment>();
		
		for(int i = 0; i < exclusionList.size(); i += 1)
		{
			for(int j = 0; j < entitlementList.size(); j += 1)
			{
				if(exclusionList.get(i).equals(entitlementList.get(j).getName()))
				{
					System.out.println("Excluding... " + entitlementList.get(j));
					tempList.add(entitlementList.get(j));
				}
			}
		}
		
		entitlementList.removeAll(tempList);
		return entitlementList;
	}
}
