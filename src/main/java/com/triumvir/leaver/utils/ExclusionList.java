package com.triumvir.leaver.utils;
import java.util.ArrayList;
import java.util.List;
import sailpoint.object.Identity;

public class ExclusionList 
{	
	private List<String>listToExclude;
	private List<Identity>identityList;
	
	public ExclusionList(List<String> listToExclude, List<Identity> identityList)
	{
		if(listToExclude.isEmpty() || identityList.isEmpty())
		{
			throw new RuntimeException("Empty value for listToExclude or identityList");
		}else
		{
			this.listToExclude = listToExclude;
			this.identityList = identityList;
		}	
	}
	
	public List<Identity> getExclusionList()
	{
		List<Identity> tempList = new ArrayList<Identity>();
		for(Identity identity : identityList)
		{
			for(String identityName : listToExclude)
			{
				if(identityName.equalsIgnoreCase(identity.getName()))
				{
					tempList.add(identity);
				}
			}
		}		
		this.identityList.removeAll(tempList);
		return identityList;
	}
}
