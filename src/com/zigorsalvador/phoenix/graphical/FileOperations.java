package com.zigorsalvador.phoenix.graphical;

import java.io.FileInputStream;
import java.io.FileOutputStream;

import com.zigorsalvador.phoenix.constants.Graphics;

import de.erichseifert.gral.data.DataTable;
import de.erichseifert.gral.io.data.DataReader;
import de.erichseifert.gral.io.data.DataReaderFactory;
import de.erichseifert.gral.io.data.DataWriter;
import de.erichseifert.gral.io.data.DataWriterFactory;
import de.erichseifert.gral.io.plots.DrawableWriter;
import de.erichseifert.gral.io.plots.DrawableWriterFactory;
import de.erichseifert.gral.plots.XYPlot;

public class FileOperations
{
	public static void writeText(DataTable data, String filename)
	{
		try
		{
			FileOutputStream stream = new FileOutputStream(filename + ".csv");
	        DataWriterFactory factory = DataWriterFactory.getInstance();
	        DataWriter writer = factory.get("text/csv");
	        
	        writer.write(data, stream);

	        stream.close();
        }
        catch (Exception exception)
        {
        	System.out.println(exception);
        	System.exit(1);	
        }
	}
	
	//////////
	
	public static void writeGraphic(XYPlot plot, String filename)
	{
		try
		{
			FileOutputStream stream = new FileOutputStream(filename + ".png");
			DrawableWriterFactory factory = DrawableWriterFactory.getInstance();
			DrawableWriter writer = factory.get("image/png");
		
			writer.write(plot, stream, Graphics.BITMAP_WIDTH, Graphics.BITMAP_HEIGHT);
			
			stream.close();
		}
		catch (Exception exception)
		{
			System.out.println(exception);
			System.exit(1);	
		}
	}
	
	//////////
		
	public static DataTable readText(String filename, Class<? extends Comparable<?>>... types)
	{
		DataTable output = null;
		
		try
		{	
			FileInputStream stream = new FileInputStream(filename + ".csv");
			DataReaderFactory factory = DataReaderFactory.getInstance();
			DataReader reader = factory.get("text/csv");
						
			output = (DataTable) reader.read(stream, types);
			
			stream.close();
		}
		catch (Exception exception)
		{
			System.out.println(exception);
			System.exit(1);
		}
		
		return output;
	}
}
