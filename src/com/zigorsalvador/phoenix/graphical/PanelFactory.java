package com.zigorsalvador.phoenix.graphical;

import java.awt.Color;
import java.awt.Font;
import java.text.DecimalFormat;

import com.zigorsalvador.phoenix.constants.Graphics;

import de.erichseifert.gral.data.DataSource;
import de.erichseifert.gral.plots.Plot;
import de.erichseifert.gral.plots.XYPlot;
import de.erichseifert.gral.plots.areas.AreaRenderer;
import de.erichseifert.gral.plots.areas.DefaultAreaRenderer2D;
import de.erichseifert.gral.plots.axes.Axis;
import de.erichseifert.gral.plots.axes.AxisRenderer;
import de.erichseifert.gral.plots.axes.LinearRenderer2D;
import de.erichseifert.gral.plots.lines.DefaultLineRenderer2D;
import de.erichseifert.gral.plots.lines.LineRenderer;
import de.erichseifert.gral.ui.InteractivePanel;
import de.erichseifert.gral.util.Insets2D;

public class PanelFactory
{
	public static InteractivePanel create(DataSource[] dataSources, Color[] dataColors, Boolean[] dataFills, Integer height)
	{
		XYPlot plot = new XYPlot(dataSources);

		for (int counter = 0; counter < dataSources.length; counter++)
		{		
			plot.setPointRenderer(dataSources[counter], null);

			LineRenderer lineRenderer = new DefaultLineRenderer2D();
			lineRenderer.setSetting(LineRenderer.COLOR, dataColors[counter]);
			
			plot.setLineRenderer(dataSources[counter], lineRenderer);
			
			if (dataFills[counter])
			{
				Color lineColor = dataColors[counter];
				Color fillColor = new Color(lineColor.getRed(), lineColor.getGreen(), lineColor.getBlue(), 55);
			
				AreaRenderer areaRenderer = new DefaultAreaRenderer2D();
				areaRenderer.setSetting(AreaRenderer.COLOR, fillColor);
				
				plot.setAreaRenderer(dataSources[counter], areaRenderer);
			}
		}

		plot.setSetting(Plot.LEGEND, true);
		
		plot.setInsets(new Insets2D.Double(10.0, 55, 40.0, 55.0));
		
		plot.getPlotArea().setSetting(XYPlot.XYPlotArea2D.GRID_MAJOR_X, true);
		plot.getPlotArea().setSetting(XYPlot.XYPlotArea2D.GRID_MINOR_X, true);
		plot.getPlotArea().setSetting(XYPlot.XYPlotArea2D.GRID_MAJOR_Y, true);
		plot.getPlotArea().setSetting(XYPlot.XYPlotArea2D.GRID_MINOR_Y, true);
		
		plot.getAxis(XYPlot.AXIS_X).setRange(0, Graphics.X_RANGE + 0.001);
		plot.getAxis(XYPlot.AXIS_Y).setRange(0, height * 1.1);
	
		Axis rightAxis = new Axis(0, height * 1.1);
		plot.setAxis(XYPlot.AXIS_Y2, rightAxis);
		
		LinearRenderer2D axisRendererY2 = new LinearRenderer2D();
		plot.setAxisRenderer(XYPlot.AXIS_Y2, axisRendererY2);
		
		AxisRenderer axisRendererX = plot.getAxisRenderer(XYPlot.AXIS_X);
		
		axisRendererX.setSetting(AxisRenderer.TICKS_SPACING, 10.0);
		axisRendererX.setSetting(AxisRenderer.TICKS_FONT, new Font("Lucida Grande", Font.PLAIN, 11));

		AxisRenderer axisRendererY = plot.getAxisRenderer(XYPlot.AXIS_Y);
		
		axisRendererY.setSetting(AxisRenderer.TICK_LABELS_FORMAT, new DecimalFormat("#,###"));
		axisRendererY.setSetting(AxisRenderer.TICKS_FONT, new Font("Lucida Grande", Font.PLAIN, 11));

		axisRendererY2.setSetting(AxisRenderer.TICK_LABELS_FORMAT, new DecimalFormat("#,###"));
		axisRendererY2.setSetting(AxisRenderer.TICKS_FONT, new Font("Lucida Grande", Font.PLAIN, 11));
		axisRendererY2.setSetting(AxisRenderer.INTERSECTION, Double.MAX_VALUE);
		
		InteractivePanel panel = new InteractivePanel(plot);
	
		panel.setLocation(10, 10);
		panel.setPannable(false);
		panel.setZoomable(false);
		panel.setSize(680, 350);	

		return panel;
	}
}