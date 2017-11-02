
package tournoi.forms;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import myUtils.ControledDocument;
import tournoi.*;

/**
 * La fenêtre qui permet d'afficher des matchs.
 */
public class FormMatch extends JInternalFrame
{
	/**
     * 
     */
    private static final long serialVersionUID = 1L;
    private JLabel lblUn = new JLabel("1");
	private JLabel lblDeux = new JLabel("2");
	private JLabel lblTrois = new JLabel("3");
	private JLabel lblQuatre = new JLabel("4");
	private JLabel lblCinq = new JLabel("5");
	private JLabel lblJoueur1Nom = new JLabel("");
	private JLabel lblJoueur2Nom = new JLabel("");
	private JButton bSave = new JButton("Save");
	private JButton	bPrint = new JButton("Print");
	
	private JTextField[] tfTabJoueur1 = {
		new JTextField(new ControledDocument(2, false, true), "", 2),
		new JTextField(new ControledDocument(2, false, true), "", 2),
		new JTextField(new ControledDocument(2, false, true), "", 2),
		new JTextField(new ControledDocument(2, false, true), "", 2),
		new JTextField(new ControledDocument(2, false, true), "", 2)
		};
	private JTextField[] tfTabJoueur2 = {
		new JTextField(new ControledDocument(2, false, true), "", 2),
		new JTextField(new ControledDocument(2, false, true), "", 2),
		new JTextField(new ControledDocument(2, false, true), "", 2),
		new JTextField(new ControledDocument(2, false, true), "", 2),
		new JTextField(new ControledDocument(2, false, true), "", 2)
		};
		
	private Match match;

	public void setMatch(Match match)
	{
		this.match = match;
		updateField();		
	}

	public FormMatch(Match match)
	{
		super("Match ", true, true);
		this.match = match;
		updateField();
		lblJoueur1Nom.setText(match.getJoueur1().toString());
		lblJoueur2Nom.setText(match.getJoueur2().toString());
		setBounds(10, 10, 500, 300);
		GridBagConstraints constraints = new GridBagConstraints();
		JPanel panel = new JPanel(new BorderLayout());
		panel.setLayout(new GridBagLayout());
		int ligne = 0;
		for (int i = 0; i < tfTabJoueur1.length; i++)
		{
			tfTabJoueur1[i].setHorizontalAlignment(JTextField.CENTER);
			tfTabJoueur2[i].setHorizontalAlignment(JTextField.CENTER);
		}
		//================= 1ere ligne =========================
		constraints.gridx = 1;
		constraints.gridy = ligne;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 0.0;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.fill = GridBagConstraints.NONE;
		constraints.ipadx = 0;
		constraints.ipady = 0;
		constraints.insets = new Insets(2, 2, 2, 2);							
		panel.add(lblUn, constraints);
	
		constraints.gridx = 2;
		panel.add(lblDeux, constraints);
	
		constraints.gridx = 3;
		panel.add(lblTrois, constraints);
	
		constraints.gridx = 4;
		panel.add(lblQuatre, constraints);
	
		constraints.gridx = 5;
		panel.add(lblCinq, constraints);
	
		//================= 2eme ligne =========================
		ligne++;
		constraints.gridx = 0;
		constraints.gridy = ligne;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 0.0;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.NORTHWEST;
		panel.add(lblJoueur1Nom, constraints);
	
		constraints.gridx = 1;
		constraints.gridheight = 1;
		constraints.weightx = 0.0;
		constraints.anchor = GridBagConstraints.NORTHEAST;
		constraints.fill = GridBagConstraints.NONE;
		panel.add(tfTabJoueur1[0], constraints);
	
		constraints.gridx = 2;
		panel.add(tfTabJoueur1[1], constraints);		
	
		constraints.gridx = 3;
		panel.add(tfTabJoueur1[2], constraints);
		
	
		constraints.gridx = 4;
		panel.add(tfTabJoueur1[3], constraints);
		
		constraints.gridx = 5;
		panel.add(tfTabJoueur1[4], constraints);
			
	
		//================= 3eme ligne =========================
		ligne++;
		constraints.gridx = 0;
		constraints.gridy = ligne;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 0.0;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.NORTHEAST;
		panel.add(lblJoueur2Nom, constraints);
	
		constraints.gridx = 1;
		constraints.gridheight = 1;
		constraints.weightx = 0.0;
		constraints.anchor = GridBagConstraints.NORTHWEST;
		constraints.fill = GridBagConstraints.NONE;
		panel.add(tfTabJoueur2[0], constraints);
	
		constraints.gridx = 2;
		panel.add(tfTabJoueur2[1], constraints);		
	
		constraints.gridx = 3;
		panel.add(tfTabJoueur2[2], constraints);
		
	
		constraints.gridx = 4;
		panel.add(tfTabJoueur2[3], constraints);
		
		constraints.gridx = 5;
		panel.add(tfTabJoueur2[4], constraints);
	
		//=================== 4ème ligne =========================
		ligne++;
		constraints.gridx = 0;
		constraints.gridy = ligne;
		constraints.gridwidth = 3;
		constraints.gridheight = 1;
		constraints.weightx = 0.0;
		constraints.weighty = 0.0;
		constraints.anchor = GridBagConstraints.NORTH;
		constraints.fill = GridBagConstraints.NONE;
		constraints.ipadx = 0;
		constraints.ipady = 0;
		constraints.insets = new Insets(2, 2, 2, 2);
		bSave.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				bSaveCliked(arg0);
	
			}
		});
		panel.add(bSave, constraints);
		
		constraints.gridx = 3;
		bPrint.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				bPrintCliked(arg0);

			}
		});
		bPrint.setFocusable(false);
		panel.add(bPrint, constraints);
			
		getContentPane().add(BorderLayout.CENTER, panel);
}

	public void clearMatch()
	{
		for (int i = 0; i < tfTabJoueur1.length; i++)
		{
			tfTabJoueur1[i].setText("");
			tfTabJoueur2[i].setText("");
		}
	}

	public void updateField()
	{
		for(int i=0; i<tfTabJoueur1.length; i++)
		{
			Manche manche = (Manche)match.getManches().get(i);
			if(manche.isTerminated())
			{
				tfTabJoueur1[i].setText(""+manche.getScore1().getScore());
				tfTabJoueur2[i].setText(""+manche.getScore2().getScore());
			}
			else
			{
				tfTabJoueur1[i].setText("");
				tfTabJoueur2[i].setText("");
			}
		}
	}
		
	public void bSaveCliked(ActionEvent actionEvent)
	{	
		if(match==null) return;
		for (int i = 0; i < tfTabJoueur1.length; i++)
		{			
			if(!tfTabJoueur1[i].getText().equals("")&&!tfTabJoueur2[i].getText().equals(""))
			{
				int score1 = Integer.parseInt(tfTabJoueur1[i].getText());
				int score2 = Integer.parseInt(tfTabJoueur2[i].getText());			
				match.setManche(i, new Score(match.getJoueur1(),score1),new Score(match.getJoueur2(),score2));
			}
			else
			{
				match.setManche(i, new Manche());
			}
		} 
	}

	private void drawGrille(Graphics g, int x, int y)
	{
		int height = 60;
		int width = 400;
		g.drawRect(x, y, width, height);
		int yTemp = y+30;
		int xTemp = x+200;
		g.drawLine(x,yTemp, x+width, yTemp);
		//g.drawLine(x+200,yTemp-15, x+width, yTemp-15);					
		for(int i=0; i<5; i++)
		{
			g.drawLine(xTemp,y,xTemp,y+height);
			xTemp += 40;		
		}		
	}

	public static synchronized int getMatchHeight(Match match)
	{ 
		if(match==null) return 0;
		return 130;
	}

	public static synchronized void printMatch(Graphics g, Match match, int line)
	{
		if(match==null) return;
		
		int margeLeft = 30;
		line += 50; 
		//== impression des joueurs du match ==
		g.setFont(new Font("Helvetica",Font.PLAIN,20));
		g.drawString("Joueurs : ",margeLeft,line);
		g.drawString("Scores : ",margeLeft+250,line);
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
		g.drawString(strJoueur1,margeLeft,line);
		line+=25;
		g.drawString(strJoueur2,margeLeft,line);
		int height = 60;
		int width = 400;
		g.drawRect(margeLeft, y, width, height);
		int yTemp = y+30;
		int xTemp = margeLeft+200;
		g.drawLine(margeLeft,yTemp, margeLeft+width, yTemp);
		//g.drawLine(x+200,yTemp-15, x+width, yTemp-15);					
		for(int i=0; i<5; i++)
		{
			g.drawLine(xTemp,y,xTemp,y+height);
			xTemp += 40;		
		}

	}


	public static synchronized void printMatch(Graphics g, Match match)
	{
		if(match==null) return;
		
		int margeLeft = 30;
		int margeUp = 50;
		int line = margeUp; 
		//== impression des joueurs du match ==
		g.setFont(new Font("Helvetica",Font.PLAIN,20));
		g.drawString("Joueurs : ",margeLeft,margeUp);
		g.drawString("Scores : ",margeLeft+250,margeUp);
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
		g.drawString(strJoueur1,margeLeft,line);
		line+=25;
		g.drawString(strJoueur2,margeLeft,line);
		int height = 60;
		int width = 400;
		g.drawRect(margeLeft, y, width, height);
		int yTemp = y+30;
		int xTemp = margeLeft+200;
		g.drawLine(margeLeft,yTemp, margeLeft+width, yTemp);
		//g.drawLine(x+200,yTemp-15, x+width, yTemp-15);					
		for(int i=0; i<5; i++)
		{
			g.drawLine(xTemp,y,xTemp,y+height);
			xTemp += 40;		
		}

	}
	
	protected void printComponent(Graphics g)
	{
		int margeLeft = 30;
		int margeUp = 50;
		int line = margeUp; 
		//== impression des joueurs du match ==
		g.setFont(new Font("Helvetica",Font.PLAIN,20));
		g.drawString("Joueurs : ",margeLeft,margeUp);
		g.drawString("Scores : ",margeLeft+250,margeUp);
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
		g.drawString(strJoueur1,margeLeft,line);
		line+=25;
		g.drawString(strJoueur2,margeLeft,line);
		drawGrille(g, margeLeft, y);		
	}
	
	public void bPrintCliked(ActionEvent actionEvent)
	{	
		PrintJob pJob = getToolkit().getPrintJob(new Frame(),
					 "Printing_Match", null);
		   if (pJob != null)
			 {
			   Graphics pg = pJob.getGraphics();
			   printComponent(pg);
			   pg.dispose();
			   pJob.end();
			 }				
	}	
}
