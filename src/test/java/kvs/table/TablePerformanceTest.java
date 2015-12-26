package kvs.table;

import java.util.Random;

import org.junit.Before;
import org.junit.Test;

import kvs.component.Cell;
import kvs.component.Column;
import kvs.component.Row;
import kvs.component.Timestamp;
import kvs.compound.ITimestampedCell;
import kvs.compound.TimestampedCell;

/**
 * Performance tests for {@link MasterTable} and {@link SlaveTable}.
 * 
 * @author hengxin
 * @date Created on Dec 20, 2015
 */
public class TablePerformanceTest
{
	private final static int DURATION = 5 * 60 * 1000;	// in ms; 30 minutes
	private final static int RANGE = 100;	// key&col range 
	private final static int PUTS = 5;
	private final static int GETS = 2; 
	
	private AbstractTable table;

	private Thread timer;
	
	private	Random rand = new Random();
	
	private final String[] string_range = new String[RANGE];
	private final Row[] row_range = new Row[RANGE];
	private final Column[] col_range = new Column[RANGE];
	private final Cell[] cell_range = new Cell[RANGE];

	@Before
	public void setUp() throws Exception
	{
		Thread current_thread = Thread.currentThread();
		
		timer = new Thread(new Runnable()
			{
				@Override
				public void run()
				{
					try
					{
						Thread.sleep(DURATION);
						current_thread.interrupt();
					} catch (InterruptedException ie)
					{
						ie.printStackTrace();
					}
				}
			}, "Timer");
	}

	@Test
	public void testMasterTable()
	{
//		this.table = new MasterTable();
		this.table = new SlaveTable();
		
		timer.start();
		
		int index;
		String index_str;
		long ts = 0;
		long number = 0;

		Row row;
		Column col;
		Cell cell;
		while(! Thread.interrupted())
		{
			for(int i = 0; i < PUTS; i++)
			{
				index = rand.nextInt(RANGE);

				if(string_range[index] == null)
				{
					index_str = String.valueOf(index);
					row = new Row(index_str);
					col = new Column(index_str);
					cell = new Cell(index_str);

					string_range[index] = index_str;
					row_range[index] = row;
					col_range[index] = col;
					cell_range[index] = cell;
				} else
				{
					row = row_range[index];
					col = col_range[index];
					cell = cell_range[index];
				}
				
				ITimestampedCell tc = new TimestampedCell(new Timestamp(ts++), cell);
				this.table.put(row, col, tc);
			}
			
			for(int i = 0; i < GETS; i++)
			{
				index = rand.nextInt(RANGE);

				if(string_range[index] == null)
				{
					index_str = String.valueOf(index);
					
					row = new Row(index_str);
					col = new Column(index_str);
					
					string_range[index] = index_str;
					row_range[index] = row;
					col_range[index] = col;
					
					cell_range[index] = new Cell(index_str);	// put an extra {@link Cell} for later reuse
				} else
				{
					row = row_range[index];
					col = col_range[index];
				}

				this.table.getTimestampedCell(row, col);
			}
			
			number = number + PUTS + GETS;
			System.out.println(number);
		}
		
		System.out.println(String.format("[%d] operations in [%d] ms.", number, DURATION));
		System.out.println("Average: " + (number * 1.0) / DURATION * 1000.0);
	}
}
