/*
 * Created on 27 févr. 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package tournoi.component;

import tournoi.*;
import javax.swing.*;

import org.apache.log4j.Logger;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * représentation graphique d'un tableau simple.
 */
public class JTableau extends JComponent implements MouseListener, Printable
{
	/**
     * 
     */
    private static final long serialVersionUID = 1L;
    private static Logger logger = Logger.getLogger(JTableau.class);
	public TableauSimple tableau;
	protected ArrayList nodesListener = new ArrayList();
	public ArrayList jNodesList = new ArrayList();

	protected void repaintJNodes()
	{
		for (Iterator iter = getJNodes().iterator(); iter.hasNext();)
		{
			JNode element = (JNode) iter.next();
			element.repaint();
			element.revalidate();
		}
	}

	public ArrayList getJNodes()
	{
		return jNodesList;
	}

	/**
	 * return le JNode correspondant à Node ou null
	 * si rien à été trouvé
	 * @param node
	 * @return
	 */
	public JNode getJNode(Node node)
	{
		for (Iterator iter = jNodesList.iterator(); iter.hasNext();)
		{
			JNode jNode = (JNode) iter.next();
			if (jNode.getNode() == node)
			{
				return jNode;
			}
		}
		return null;
	}

	public ArrayList getSelectedJNodes()
	{
		ArrayList temp = new ArrayList();
		for (Iterator iter = jNodesList.iterator(); iter.hasNext();)
		{
			JNode jNode = (JNode) iter.next();
			if (jNode.isSelected())
			{
				temp.add(jNode);
			}
		}
		return temp;
	}

	public void addJNode(JNode jNode)
	{
		add(jNode);
		jNodesList.add(jNode);
		jNode.addMouseListener(this);
	}

	public String getName()
	{
		return tableau.getName();
	}

	public void addJNodesListener(JNodesListener nodeListener)
	{
		nodesListener.add(nodeListener);
	}

	public void drawLinkedNodes(Graphics g, JNode jNode1, JNode jNode2)
	{
		Point p1 = jNode1.getLocation();
		Point p2 = jNode2.getLocation();
		g.setColor(Color.BLACK);
		int x1, y1, x2, y2;
		y1 = p1.y + (jNode1.getHeight() / 2);
		y2 = p2.y + (jNode2.getHeight() / 2);
		if (p1.x < p2.x)
		{
			x1 = p1.x + jNode1.getWidth();
			x2 = p2.x;
		}
		else
		{
			x1 = p1.x;
			x2 = p2.x + jNode2.getWidth();
		}
		g.drawLine(x1, y1, x2, y2);
	}

	public JTableau(TableauSimple tableau)
	{
		this.tableau = tableau;
		tableau.genererAllTours();
		setBackground(Color.WHITE);
		setOpaque(true);
	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
	 */
	public void mouseClicked(MouseEvent arg0)
	{
		if (arg0.getButton() == MouseEvent.BUTTON3)
		{
			for (Iterator iter = nodesListener.iterator(); iter.hasNext();)
			{
				JNodesListener listener = (JNodesListener) iter.next();
				listener.nodeClicked((JNode) arg0.getSource());

			}
		}
	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
	 */
	public void mousePressed(MouseEvent arg0)
	{
	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
	 */
	public void mouseReleased(MouseEvent arg0)
	{
	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
	 */
	public void mouseEntered(MouseEvent arg0)
	{
	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
	 */
	public void mouseExited(MouseEvent arg0)
	{
	}

	public int print(Graphics g, PageFormat pageFormat, int pageIndex) throws PrinterException
	{
		int saveWidth = getWidth();
		int saveHeight = getHeight();
		//setSize(getMinimumSize());
		//pageFormat.getPaper().setImageableArea(0,0,pageFormat.getWidth(), pageFormat.getHeight());
		if (getWidth() > getHeight())
		{
			pageFormat.setOrientation(PageFormat.LANDSCAPE);
		}
		else
		{
			pageFormat.setOrientation(PageFormat.PORTRAIT);
		}
		try
		{
			Graphics2D g2 = (Graphics2D) g;
			g2.setColor(Color.black);
			int fontHeight = g2.getFontMetrics().getHeight();
			int fontDesent = g2.getFontMetrics().getDescent();
			//leave room for page number
			double pageHeight = pageFormat.getImageableHeight() - fontHeight;
			double pageWidth = pageFormat.getImageableWidth();
			double tableWidth = (double) getWidth();
			double scale = 1;
			if (tableWidth >= pageWidth)
			{
				scale = pageWidth / tableWidth;
			}
			double tableWidthOnPage = tableWidth * scale;
			double pageHeightForTable = getHeight();
			int totalNumPages = 1;
			if (pageIndex >= totalNumPages)
			{
				setSize(saveWidth, saveHeight);
				return Printable.NO_SUCH_PAGE;
			}
			g2.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
			//bottom center

			//g2.translate(0f, headerHeightOnPage);
			g2.translate(0f, -pageIndex * pageHeightForTable);
			//If this piece of the table is smaller 
			//than the size available,
			//clip to the appropriate bounds.
			if (pageIndex + 1 == totalNumPages)
			{
				int lastRowPrinted = pageIndex;
				int numRowsLeft = 1 - lastRowPrinted;
				g2.setClip(
					0,
					(int) (pageHeightForTable * pageIndex),
					(int) Math.ceil(tableWidthOnPage),
					(int) Math.ceil(getHeight() * numRowsLeft));
			}
			//else clip to the entire area available.
			else
			{
				g2.setClip(
					0,
					(int) (pageHeightForTable * pageIndex),
					(int) Math.ceil(tableWidthOnPage),
					(int) Math.ceil(pageHeightForTable));
			}
			g2.scale(scale, scale);
			paint(g2);
			g2.scale(1 / scale, 1 / scale);
			g2.translate(0f, pageIndex * pageHeightForTable);
			g2.setClip(0, 0, (int) Math.ceil(tableWidthOnPage), (int) getHeight());
			g2.scale(scale, scale);
		}
		catch (Exception e)
		{
			setSize(saveWidth, saveHeight);
		}
		setSize(saveWidth, saveHeight);
		return Printable.PAGE_EXISTS;
	}
}
