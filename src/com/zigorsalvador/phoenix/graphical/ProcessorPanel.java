package com.zigorsalvador.phoenix.graphical;

import java.awt.Color;

import javax.swing.JPanel;

import com.zigorsalvador.phoenix.constants.Graphics;

import de.erichseifert.gral.data.DataSeries;
import de.erichseifert.gral.data.DataSource;
import de.erichseifert.gral.data.DataTable;
import de.erichseifert.gral.plots.XYPlot;
import de.erichseifert.gral.ui.InteractivePanel;

public class ProcessorPanel extends JPanel
{
	private static final long serialVersionUID = -256500544132976836L;

	//////////
	
	private InteractivePanel panel;
	private DataTable data;
	private XYPlot plot;
	
	//////////
	
	public ProcessorPanel()
	{
		this.setLayout(null);

		createInteractivePanel();
		
		this.add(panel);
	}
	
	//////////
	
	@SuppressWarnings("unchecked")
	
	private void createInteractivePanel()
	{
		data = new DataTable(Integer.class, Float.class);
		
		for (int counter = 0; counter < 101; counter++) data.add(counter, 0.0f);
		
		DataSource[] dataSources = new DataSource[1];
		
		dataSources[0] = new DataSeries("Processor utilization (%)", data, 0, 1);
			
		Color[] dataColors = new Color[1];
		
		dataColors[0] = new Color(200, 0, 0);
		
		Boolean[] dataFills = new Boolean[1];
		
		dataFills[0] = true;
		
		panel = PanelFactory.create(dataSources, dataColors, dataFills, 100);
		
		plot = (XYPlot) panel.getDrawable();
	}
	
	//////////
	
	public void update(Float processorUtilization)
	{
		for (int counter = 0; counter < 100; counter++)
		{
			data.set(1, counter, data.get(1, counter + 1));
		}

		data.set(1, Graphics.X_RANGE, processorUtilization);
		
		plot.getAxis(XYPlot.AXIS_Y2).setRange(0, 100 * 1.1);
		plot.getAxis(XYPlot.AXIS_Y).setRange(0, 100 * 1.1);	
		
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