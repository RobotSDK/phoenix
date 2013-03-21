package com.zigorsalvador.phoenix.graphical;

import java.awt.Color;

import javax.swing.JPanel;

import com.zigorsalvador.phoenix.constants.Graphics;

import de.erichseifert.gral.data.DataSeries;
import de.erichseifert.gral.data.DataSource;
import de.erichseifert.gral.data.DataTable;
import de.erichseifert.gral.data.Row;
import de.erichseifert.gral.data.statistics.Statistics;
import de.erichseifert.gral.plots.XYPlot;
import de.erichseifert.gral.ui.InteractivePanel;

public class RoutingPanel extends JPanel
{
	private static final long serialVersionUID = -7267896404333496142L;

	//////////
	
	private Integer maximum = Graphics.Y_RANGE;
	private InteractivePanel panel;
	private DataTable data;
	private XYPlot plot;

	//////////
	
	public RoutingPanel()
	{
		this.setLayout(null);
		
		createInteractivePanel();

		this.add(panel);
	}
	
	//////////
	
	@SuppressWarnings("unchecked")
	
	private void createInteractivePanel()
	{
		data = new DataTable(Integer.class, Integer.class, Integer.class);
		
		for (int counter = 0; counter < 101; counter++) data.add(counter, 0, 0);
				
		DataSource[] dataSources = new DataSource[2];
		
		dataSources[0] = new DataSeries("Routing table size (filters)", data, 0, 1);
		dataSources[1] = new DataSeries("Routing table size (clients)", data, 0, 2);
			
		Color[] dataColors = new Color[2];
		
		dataColors[0] = new Color(200, 0, 0);
		dataColors[1] = new Color(0, 0, 200);
		
		Boolean[] dataFills = new Boolean[2];
		
		dataFills[0] = true;
		dataFills[1] = true;
		
		panel = PanelFactory.create(dataSources, dataColors, dataFills, Graphics.Y_RANGE);
		
		plot = (XYPlot) panel.getDrawable();
	}
		
	//////////
	
	public void update(Integer amountOfFilters, Integer amountOfClients)
	{
		for (int counter = 0; counter < 100; counter++)
		{
			Row row = data.getRow(counter + 1);
			
			data.set(1, counter, row.get(1));
			data.set(2, counter, row.get(2));
		}
		
		data.set(1, Graphics.X_RANGE, amountOfFilters);
		data.set(2, Graphics.X_RANGE, amountOfClients);
		
		maximum = Math.max(maximum, (int) data.getColumn(2).getStatistics(Statistics.MAX));
		
		plot.getAxis(XYPlot.AXIS_Y2).setRange(0, maximum * 1.1);
		plot.getAxis(XYPlot.AXIS_Y).setRange(0, maximum * 1.1);		
				
		if (panel.isShowing()) this.repaint();
	}
		
	//////////

	public DataTable getData()
	{
		return data;
	}
	
	//////////
	
	public XYPlot getPlot()
	{
		return plot;
	}
}