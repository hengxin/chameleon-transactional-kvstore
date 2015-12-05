package main;

import master.context.MasterLauncher;

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
		new MasterLauncher();
	}
}
