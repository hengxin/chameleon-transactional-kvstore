package main;

import exception.SiteException;
import exception.network.membership.MemberParseException;
import master.MasterLauncher;

/**
 * Main class at the master side, 
 * in the <i>single-master-multiple-slaves</i> setting.
 * It simply launches master.
 * 
 * @author hengxin
 * @date Created on 12-05-2015
 */
public class MasterMainTest
{
	public static void main(String[] args)
	{
		try
		{
			new MasterLauncher();
		} catch (SiteException | MemberParseException e)
		{
			e.printStackTrace();
			System.exit(1);
		}
	}
}
