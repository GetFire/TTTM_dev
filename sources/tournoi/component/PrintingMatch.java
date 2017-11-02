/*
 *
 */
package tournoi.component;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import tournoi.Match;

/**
 *
 */
public class PrintingMatch implements Printable
{
	public ArrayList printedMatchList = null;
	
	private static Logger logger = Logger.getLogger(PrintingMatch.class);
	
	/**
	 * construction de l'object pour l'impression des feuilles de rencontres
	 * @param printedMatch
	 */
	public PrintingMatch(PrintedMatch printedMatch)
	{
		this();
		printedMatchList.add(printedMatch);
	}

	/**
	 * construction de l'object pour l'impression des feuilles de rencontres
	 * @param printedMatch
	 */
	public PrintingMatch()
	{
		printedMatchList = new ArrayList();
		printedMatchList.clear();
	}

	
	public void addPrintedMatch(PrintedMatch printedMatch)
	{
		printedMatchList.add(printedMatch);
	}
	
	public static synchronized int getMatchHeight(Match match)
	{	
		if(match==null) return 0;
		if(match.getName()==null||match.getName().equals(""))
		{
			return 225;
		}
		return 255;
	}

	private static synchronized int getMaxMatchHeight()
	{	
		return 255;
	}

	private static synchronized int getMaxMatchWidth()
	{	
		return 400;
	}

	
	private int getTotalPage(PageFormat format)
	{
		int totalPages = 0;
		
		totalPages = (int)(printedMatchList.size()/getMatchParPage(format));
		if((printedMatchList.size()%getMatchParPage(format))>0)
		{
			totalPages += 1;
		}
		return totalPages;
	}
	
	private int getMatchParPage(PageFormat format)
	{
		int size = PrintingMatch.getMaxMatchHeight();
		int result = (int)(format.getImageableHeight()/size);
		if(result==0) result=1;
		return result;		
	}
	
	
	public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException
	{
		Graphics2D g = (Graphics2D)graphics; 
		int current = 0;
		int matchsParPage = getMatchParPage(pageFormat);
		int totalPages = getTotalPage(pageFormat);
		Match match = null;
		String tabName =  "";
		
		double scaleW = 1.0;
		double scaleH = 1.0;		
		// si la taille de la derni�re poule excede la taille de la page : on redimensionne la poule
		if(matchsParPage == 1)
		{
			match = ((PrintedMatch)printedMatchList.get(0)).getMatch();
			if(pageFormat.getImageableWidth()<PrintingMatch.getMaxMatchWidth())
			{				
				scaleW = pageFormat.getImageableWidth()/PrintingMatch.getMaxMatchWidth();
			}
			if(pageFormat.getImageableHeight()<PrintingMatch.getMaxMatchHeight())
			{
				scaleH = pageFormat.getImageableHeight()/PrintingMatch.getMaxMatchHeight();
			}
			g.setClip(0,0,PrintingMatch.getMaxMatchWidth(),PrintingMatch.getMaxMatchHeight()); 
			g.scale(scaleW,scaleH);		

		}
		
		
		if(pageIndex>=totalPages) return NO_SUCH_PAGE;
		if(pageIndex==0) current = 0;		
		g.translate((int) pageFormat.getImageableX(), (int)pageFormat.getImageableY());
		g.setColor(Color.black);
		int line = 0;
		for(int i=0; i<matchsParPage; i++)
		{
			current = (matchsParPage*pageIndex)+i;
			if(current<printedMatchList.size())
			{
				match = ((PrintedMatch)printedMatchList.get(current)).getMatch();
				tabName =  ((PrintedMatch)printedMatchList.get(current)).getTabName();					
				//== impression des joueurs du match ==
				g.setFont(new Font("Helvetica",Font.PLAIN,20));
				g.drawString(tabName,0,line);
				line+=30;
				if(match.getName()!=null && !match.getName().equals(""))
				{
					g.setFont(new Font("Helvetica",Font.ITALIC,20));
					g.drawString(match.getName().toLowerCase(),0,line);
					line+=30;
					g.setFont(new Font("Helvetica",Font.PLAIN,20));
				}	
				//== impression du num�ro de table ==
				g.setFont(new Font("Helvetica",Font.PLAIN,15));
				String numTable = (match.getNumTable()==0)?"table ?":"table "+match.getNumTable();
				g.drawString(numTable,0,line);
				line+=25;
				g.setFont(new Font("Helvetica",Font.PLAIN,20));
				
				g.drawString("Участник : ",0,line);
				g.drawString("Результат : ",250,line);
				line+=30;
				g.setFont(new Font("Helvetica",Font.PLAIN,15));
				String strJoueur1 = match.getJoueur1().toString();
				if(strJoueur1.length()>25)
				{
					strJoueur1 = strJoueur1.substring(0, 25);
				}
	
				String strJoueur2 = match.getJoueur2().toString();
				if(strJoueur2.length()>25)
				{
					strJoueur2 = strJoueur2.substring(0, 25);
				}
				int y=line-15;
				line+=5;
				g.drawString(strJoueur1,0,line);
				line+=25;
				g.drawString(strJoueur2,0,line);
				int height = 60;
				int width = 400;
				g.drawRect(0, y, width, height);
				int yTemp = y+30;
				int xTemp = 200;
				g.drawLine(0,yTemp, 0+width, yTemp);
				//g.drawLine(x+200,yTemp-15, x+width, yTemp-15);					
				for(int x=0; x<5; x++)
				{
					g.drawLine(xTemp,y,xTemp,y+height);
					xTemp += 40;		
				}
				g.setFont(new Font("Helvetica",Font.ITALIC,10));
				line = y+height+30;
				g.drawString("Объедините имя победителя",0,line);
				line += 50;
			}								
		}	
		return PAGE_EXISTS;	
	}

}
