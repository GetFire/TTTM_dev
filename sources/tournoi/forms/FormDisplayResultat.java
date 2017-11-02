/*
 *
 */
package tournoi.forms;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.util.ArrayList;

// import javax.swing.JFrame;
import javax.swing.JInternalFrame;

import org.apache.log4j.Logger;

import tournoi.component.JTableSorted;
import tournoi.component.ResultatTableModel;

/**
 *
 */
public class FormDisplayResultat extends JInternalFrame implements Printable
{

	/**
     * 
     */
    private static final long serialVersionUID = 1L;

    private javax.swing.JPanel jContentPane = null;

	private javax.swing.JScrollPane jScrollPane = null;
	private javax.swing.JTable jTable = null;
	private javax.swing.JButton bPrint = null;
	private static Logger logger = Logger.getLogger(FormDisplayResultat.class);

	/**
	 * This is the default constructor
	 */
	public FormDisplayResultat(ArrayList joueurs, String tableauName)
	{
		super();
		ResultatTableModel modelResultat = new ResultatTableModel(joueurs);
		JTableSorted tableSorted = new JTableSorted(modelResultat);

		jContentPane = new javax.swing.JPanel();
		java.awt.GridBagConstraints consGridBagConstraints11 = new java.awt.GridBagConstraints();
		java.awt.GridBagConstraints consGridBagConstraints1 = new java.awt.GridBagConstraints();
		consGridBagConstraints11.gridx = 0;
		consGridBagConstraints11.gridy = 1;
		consGridBagConstraints1.weightx = 1.0;
		consGridBagConstraints1.weighty = 1.0;
		consGridBagConstraints1.fill = java.awt.GridBagConstraints.BOTH;
		consGridBagConstraints1.gridx = 0;
		consGridBagConstraints1.gridy = 0;
		consGridBagConstraints1.insets = new java.awt.Insets(2, 2, 2, 2);
		jContentPane.setLayout(new java.awt.GridBagLayout());
		jContentPane.add(tableSorted, consGridBagConstraints1);
		jContentPane.add(getBPrint(), consGridBagConstraints11);
		tableSorted.setVisible(true);
		jContentPane.setVisible(true);
		jContentPane.setOpaque(true);
		tableSorted.setOpaque(true);
		this.setContentPane(jContentPane);
		this.setTitle(tableauName);
		initialize();
		bPrint.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				printClicked();

			}
		});
		jTable = tableSorted.getJTable();
	}
	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize()
	{
		this.setVisible(true);
		this.setResizable(true);
		this.setClosable(true);
		this.setIconifiable(true);
		pack();
	}

	/**
	 * This method initializes bPrint
	 * 
	 * @return javax.swing.JButton
	 */
	private javax.swing.JButton getBPrint()
	{
		if (bPrint == null)
		{
			bPrint = new javax.swing.JButton();
			bPrint.setText("Печать");
		}
		return bPrint;
	}

	public void printClicked()
	{		
		PrinterJob pj = FormDesktop.printer;
		pj.setPrintable(FormDisplayResultat.this);
		pj.printDialog();
		try
		{
			pj.print();
		}
		catch (Exception exp)
		{
			logger.error(exp);
		}
	}

	public int print(Graphics g, PageFormat pageFormat, int pageIndex) throws PrinterException
	{
		try
		{
			Graphics2D g2 = (Graphics2D) g;
			g2.setColor(Color.black);
			int fontHeight = g2.getFontMetrics().getHeight();
			int fontDesent = g2.getFontMetrics().getDescent();			
			//leave room for page number
			double pageHeight = pageFormat.getImageableHeight() - fontHeight;
			double pageWidth = pageFormat.getImageableWidth();
			double tableWidth = (double) jTable.getColumnModel().getTotalColumnWidth();
			double scale = 1;
			if (tableWidth >= pageWidth)
			{
				scale = pageWidth / tableWidth;
			}
			double headerHeightOnPage = jTable.getTableHeader().getHeight() * scale;
			double tableWidthOnPage = tableWidth * scale;

			double oneRowHeight = (jTable.getRowHeight() + jTable.getRowMargin()) * scale;
			int numRowsOnAPage = (int) ((pageHeight - headerHeightOnPage) / oneRowHeight);
			double pageHeightForTable = oneRowHeight * numRowsOnAPage;
			int totalNumPages = (int) Math.ceil(((double) jTable.getRowCount()) / numRowsOnAPage);
			if (pageIndex >= totalNumPages)
			{
				return Printable.NO_SUCH_PAGE;
			}			
			g2.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
			//bottom center
			g2.drawString(
				"Page: " + (pageIndex + 1),
				(int) pageWidth / 2 - 35,
				(int) (pageHeight + fontHeight - fontDesent));

			g2.translate(0f, headerHeightOnPage);
			g2.translate(0f, -pageIndex * pageHeightForTable);			
			//If this piece of the table is smaller 
			//than the size available,
			//clip to the appropriate bounds.
			if (pageIndex + 1 == totalNumPages)
			{
				int lastRowPrinted = numRowsOnAPage * pageIndex;
				int numRowsLeft = jTable.getRowCount() - lastRowPrinted;
				g2.setClip(
					0,
					(int) (pageHeightForTable * pageIndex),
					(int) Math.ceil(tableWidthOnPage),
					(int) Math.ceil(oneRowHeight * numRowsLeft));
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
			jTable.paint(g2);
			g2.scale(1 / scale, 1 / scale);
			g2.translate(0f, pageIndex * pageHeightForTable);
			g2.translate(0f, -headerHeightOnPage);
			g2.setClip(0, 0, (int) Math.ceil(tableWidthOnPage), (int) Math.ceil(headerHeightOnPage));
			g2.scale(scale, scale);			
			jTable.getTableHeader().paint(g2);
			//paint header at top
		}
		catch (Exception e)
		{
			logger.error(e);
		}
		return Printable.PAGE_EXISTS;
	}

}
