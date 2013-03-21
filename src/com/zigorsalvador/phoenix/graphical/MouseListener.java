package com.zigorsalvador.phoenix.graphical;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.SwingUtilities;

public class MouseListener extends MouseAdapter
{
	private GeneralPanel generalPanel;
	
	//////////
	
	public MouseListener(GeneralPanel generalPanel)
	{
		this.generalPanel = generalPanel;
	}
	
	//////////

    public void mouseClicked(final MouseEvent event) 
    {
    	SwingUtilities.invokeLater(new Runnable(){public void run(){generalPanel.setFilterLabel(event.getPoint());}});
    }
}
