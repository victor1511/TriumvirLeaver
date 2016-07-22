package com.triumvir.leaver;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import sailpoint.object.Identity;
import sailpoint.tools.GeneralException;
import sailpoint.api.IdentityArchiver;
import sailpoint.api.SailPointContext;
import sailpoint.object.IdentitySnapshot;

public class Snapshot {
	Log log = LogFactory.getLog(Snapshot.class);
	
	public void createSnapshot(Identity person, SailPointContext context)
	{
		IdentityArchiver archiver = new IdentityArchiver(context.getContext());
		try 
		{
			IdentitySnapshot identitySnapshot = archiver.createSnapshot(person);
			context.saveObject(identitySnapshot);
			context.commitTransaction();
			log.info("Making the identity snashot...");
		} 
		catch (GeneralException e)
		{
			log.fatal("Error -> " + e);
		}

	}
}
