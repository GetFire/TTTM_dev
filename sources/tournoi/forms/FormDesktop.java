package tournoi.forms;

import java.awt.*;
import java.awt.event.*;
import java.awt.print.PageFormat;
import java.awt.print.PrinterJob;
import java.beans.PropertyVetoException;
import java.beans.XMLDecoder;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.*;
import org.apache.log4j.Logger;
import org.jconfig.*;

import myUtils.MyFileFilter;
import tournoi.Competition;
import tournoi.Tableau;
import tournoi.TableauPoules;
import tournoi.TableauSimple;

/**
 *  Le bureau de l'application
 */
public class FormDesktop extends JFrame
{
	
	// private final URL location = java.lang.ClassLoader.getSystemResource("tttm.png");
						
	// private final ImageIcon imageIcon = new ImageIcon(location);	 
	// private final Image image = imageIcon.getImage();
	
	/**
     * 
     */
    
    private static final long serialVersionUID = 1L;

    //== configuration de l'application ===	
	public static final Configuration configuration = ConfigurationManager.getConfiguration();

	private JDesktopPane desktop;
	private FormMain formMain = null;
	private FormJoueur formJoueur = null;
	private FormDisplayTables formDisplayTables = null;
	private FormFormulesAuto formFormuleAuto = null;
	private Competition competition = null;
	
	private Logger logger = Logger.getLogger(FormDesktop.class);
	
	private Timer timer = null;	
	
	public static PrinterJob printer = PrinterJob.getPrinterJob();
	public static PageFormat pageFormat = printer.defaultPage();


	public JDesktopPane getDesktop()
	{
		return desktop;
	}
	
	/**
	 *  Description of the Method
	 *
	 * @return    Description of the Return Value
	 */	
	private JMenuBar createMenuBar()
	{
	//Create the menu bar.
	JMenuBar menuBar = new JMenuBar();
	//	Build the first menu.
	JMenu menuCompetition = new JMenu("Соревнование");
	JMenuItem menuSave = new JMenuItem("Sauvegarde");
	JMenuItem menuRestauration = new JMenuItem("Restauration");
	JCheckBoxMenuItem menuSauvegardeAuto = new JCheckBoxMenuItem("Sauvegarde automatique",false);			

	menuCompetition.add(menuSave);
	menuCompetition.add(menuRestauration);
	menuCompetition.add(menuSauvegardeAuto);
	//Build the second menu.
	JMenu menu = new JMenu("Просмотр");
		menu.getAccessibleContext().setAccessibleDescription(
			"Открыть или закрыть окно");

	JMenu menuImpression = new JMenu("Impression");
	JMenuItem menuOptionImpression = new JMenuItem("Option");
	menuImpression.add(menuOptionImpression);

	// menu affichage joueur
	
		JCheckBoxMenuItem menuJoueur = new JCheckBoxMenuItem("Fen�tre joueurs",false);			
		menuJoueur.getAccessibleContext().setAccessibleDescription(
			"Affiche ou cache la fen�tre des joueurs");

		JCheckBoxMenuItem menuFormules = new JCheckBoxMenuItem("Fen�tre des formules",false);			
		menuJoueur.getAccessibleContext().setAccessibleDescription(
			"Affiche ou cache la fen�tre pour cr�er vos formules");


		JCheckBoxMenuItem menuTables = new JCheckBoxMenuItem("Fen�tre tables libres",false);			
		menuTables.getAccessibleContext().setAccessibleDescription(
			"Affiche ou cache la fen�tre des tables libres");


		JCheckBoxMenuItem menuDossard = new JCheckBoxMenuItem("Dossards",true);
		menuDossard.getAccessibleContext().setAccessibleDescription(
			"Affiche ou cache les num�ros de dossards des joueurs");
		menuDossard.getModel().setSelected(Competition.isAfficheDossard());
		
		menuSauvegardeAuto.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						if(timer.isRunning())
						{
							timer.stop();
						}
						else
						{
							timer.start();
						}

					}
				});	
		
	menuOptionImpression.addActionListener(new ActionListener()
	{
		public void actionPerformed(ActionEvent e)
		{
			menuOptionImpressionClicked(e);

		}
	});	

	menuTables.addActionListener(new ActionListener()
	{
		public void actionPerformed(ActionEvent e)
		{
			formDisplayTables.setVisible(!formDisplayTables.isVisible());

		}
	});	

	
	menuJoueur.addActionListener(new ActionListener()
	{
		public void actionPerformed(ActionEvent e)
		{
			menuJoueurClicked(e);

		}
	});	

	menuFormules.addActionListener(new ActionListener()
	{
		public void actionPerformed(ActionEvent e)
		{
			menuFormulesClicked(e);

		}
	});	


	menuDossard.addActionListener(new ActionListener()
	{
		public void actionPerformed(ActionEvent e)
		{
			Competition.setAfficheDossard(!Competition.isAfficheDossard());
		}
	});	

	menuSave.addActionListener(new ActionListener()
	{
		public void actionPerformed(ActionEvent e)
		{
			menuSaveClicked(e);
		}
	});	

	menuRestauration.addActionListener(new ActionListener()
	{
		public void actionPerformed(ActionEvent e)
		{
			menuRestaurationClicked(e);
		}
	});	

	
	JCheckBoxMenuItem menuTournoi = new JCheckBoxMenuItem("Fen�tre tournois",false);			
		menuJoueur.getAccessibleContext().setAccessibleDescription(
			"Affiche ou cache la fen�tre des tournois");
		menuTournoi.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				menuTournoiClicked(e);

			}
		});
					
		menu.add(menuTournoi);
		menu.add(menuJoueur);
		menu.add(menuFormules);
		menu.add(menuTables);

		
		menu.add(menuDossard);		
		menuBar.add(menuCompetition);
		menuBar.add(menu);
		menuBar.add(menuImpression);
		return menuBar;
	}

	public void menuJoueurClicked(ActionEvent e)
	{
        try
        {
            formJoueur.setMaximum(true);
        }
        catch (PropertyVetoException e1)
        {
            // TODO Auto-generated catch block
            //e1.printStackTrace();
        }
		formJoueur.setVisible(!formJoueur.isVisible());
	}


	public void menuFormulesClicked(ActionEvent e)
	{
		formFormuleAuto.setVisible(!formFormuleAuto.isVisible());
	}


	public void menuTournoiClicked(ActionEvent e)
	{
        try
        {
            formMain.setMaximum(true);
        }
        catch (PropertyVetoException e1)
        {
            // TODO Auto-generated catch block
            //e1.printStackTrace();
        }
        
		formMain.setVisible(!formMain.isVisible());
	}


	public void addInternalFrame(JInternalFrame frame)
	{
		desktop.add(frame,0);
		frame.setVisible(true);
		//frame.pack();
	}
		
	/**  Constructor for the FormMain object */
	public FormDesktop(Competition competition)
	{
		super("Tennis de Table Tournois Manager");
		
		desktop = new JDesktopPane();
	    desktop.setBackground(new Color(155,169,202));
		//desktop.setBackground(new Color(182,162,207));
		this.competition = competition;
		//==== gestion de la sauvegarde automatique ===
		int time = configuration.getIntProperty("timer", 5, "sauvegarde");
		timer = new Timer(time*60*1000,new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				saveCompetition();
			}
		});

	Dimension tailleEcran = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
	int hauteur = (int)tailleEcran.getHeight();
	int largeur = (int)tailleEcran.getWidth();
		setSize(largeur, hauteur);
		setJMenuBar(createMenuBar());	
		getContentPane().add(desktop);
	 	setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
	WindowListener wndCloser =
			new WindowAdapter()
			{
				
				public void windowClosing(WindowEvent e)
				{
					int result = JOptionPane.showConfirmDialog(
						null,
						"Quitter l'application ?",
						"Attention",
						JOptionPane.YES_NO_OPTION);
					if(result==JOptionPane.YES_OPTION)
					{
						System.exit(0);									
					}
					
				}
			};
		addWindowListener(wndCloser);
		formMain = new FormMain(competition, getDesktop());		
		formJoueur = new FormJoueur(competition);
		formDisplayTables = new FormDisplayTables(competition);
		formDisplayTables.setClosable(false);
		formDisplayTables.setVisible(false);
		formJoueur.setClosable(false);
		formMain.setClosable(false);		
		formFormuleAuto = new FormFormulesAuto(competition);		
		addInternalFrame(formFormuleAuto);
		formFormuleAuto.setVisible(false);
		formFormuleAuto.setSize(510,300);		
		addInternalFrame(formMain);		
		addInternalFrame(formJoueur);
		addInternalFrame(formDisplayTables);
		//addInternalFrame(new FormFormuleSimple());
		formDisplayTables.setVisible(false);
		formMain.setVisible(false);
		formJoueur.setVisible(false);													
		setVisible(true);
	}

	public void saveCompetition()
	{
		String fileName = configuration.getProperty("file", "sauvegarde.xml", "sauvegarde");
		competition.save(new File(fileName));
	}
	
	public void menuRestaurationClicked(ActionEvent e)
	{
		JFileChooser fileChooser = new JFileChooser();
		MyFileFilter filter = new MyFileFilter();
		filter.addExtension("xml");
		fileChooser.setFileFilter(filter);
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);		
		int returnVal = fileChooser.showOpenDialog(FormDesktop.this);
		
		if (returnVal == JFileChooser.APPROVE_OPTION)
		{
			File file = fileChooser.getSelectedFile();
			XMLDecoder d = null;			
			try			
			{
				d = new XMLDecoder(
								   new BufferedInputStream(
									   new FileInputStream(file)));
			}
			catch(FileNotFoundException exp)
			{
				return;
			}
			Object result = d.readObject();
			Competition comp = (Competition)result;
			// maj de la competition
			competition.setDescriptionTableauList(comp.getDescriptionTableauList());
			competition.setJoueurs(comp.getJoueurs());
			competition.setTableaux(comp.getTableaux());
			competition.setRestauration(true);
			competition.notifyJoueursChanged();
			competition.notifyTableauxChanged();
			
			ArrayList tab = comp.getTableaux();
			d.close();
			for (Iterator iter = tab.iterator(); iter.hasNext();)
			{					
				Tableau tableau = (Tableau) iter.next();					
				if(tableau.getType().equals(TableauPoules.TYPE))
				{
					FormDisplayTableauPoules form = new FormDisplayTableauPoules(competition, (TableauPoules)tableau);
					addInternalFrame(form);
					form.setVisible(true);
				}
				else
				{						
					FormDisplayTableauSimple form = new FormDisplayTableauSimple((TableauSimple)tableau);
					addInternalFrame(form);
					form.setVisible(true);
				}
			}
		}	
	}		

	public void menuOptionImpressionClicked(ActionEvent e)
	{	
		printer.printDialog();				
		pageFormat = printer.pageDialog(pageFormat);
		
	}
	
	public void menuSaveClicked(ActionEvent e)
	{
		JFileChooser fileChooser = new JFileChooser();
		MyFileFilter filter = new MyFileFilter();
		filter.addExtension("xml");
		fileChooser.setFileFilter(filter);
		fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);		
		int returnVal = fileChooser.showOpenDialog(FormDesktop.this);
		
		if (returnVal == JFileChooser.APPROVE_OPTION)
		{
			File file = fileChooser.getSelectedFile();
			try
			{
				// si le fichier n'existe pas on le cr�er
				if(!file.exists())
				{
					if(!file.getName().endsWith(".xml")&&!file.getName().endsWith(".XML"))
					{
						file = new File(file.getAbsolutePath()+".xml");
						if(!file.createNewFile())
						{
							JOptionPane.showMessageDialog(this,"Erreur lors de l'enregistrement !","Erreur",JOptionPane.ERROR_MESSAGE);							
						}
					}
				}
				if(!competition.save(file))
				{
					JOptionPane.showMessageDialog(this,"Erreur lors de la sauvegarde","Erreur", JOptionPane.ERROR_MESSAGE);
				}
			}	
			catch(Exception exp)
			{			
				JOptionPane.showMessageDialog(this,"Erreur lors de la sauvegarde","Erreur", JOptionPane.ERROR_MESSAGE);
			}
						
		}		
	}
}

