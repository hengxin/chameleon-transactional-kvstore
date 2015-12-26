package main;

import exception.MemberParseException;
import exception.SiteException;
import slave.SlaveLauncher;

/**
 * Main class at the slave side in the <i>single-master-multiple-slaves</i> setting.
 * 
 * @author hengxin
 * @date Created on 12-05-2015
 */
public class SlaveMainTest
{
	public static void main(String[] args)
	{
		try
		{
			new SlaveLauncher();
		} catch (SiteException | MemberParseException e)
		{
			e.printStackTrace();
			System.exit(1);
		}
	}
}
