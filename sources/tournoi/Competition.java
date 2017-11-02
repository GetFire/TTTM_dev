/*
 *
 */
package tournoi;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.*;

import org.apache.log4j.Logger;
import tournoi.exception.TournoiException;

/**
 * permet d'obtenir les diff�rentes ressources de la comp�tition.
 */
public class Competition extends Observable implements Observer
{
	private static Logger logger = Logger.getLogger(Competition.class);
	
	
	//== variables d'options ==
	private boolean restauration = false;
	private static boolean printDialogue = false;
	
	private static boolean afficheDossard = false;
	
	private ArrayList joueurs;
	private ArrayList tableaux;
	private ArrayList descriptionTableauList;
	
	private static Stack freeTablesList = new Stack();
    private static int nbMaxTables = 0;	

	
	public static void freeTable(Integer numTable)
	{
        if(numTable.intValue()<=nbMaxTables)
        {
            freeTablesList.push(numTable);
        }
	}
	
	public static Integer getFreeTable()
	{
		if(freeTablesList.isEmpty())
		{
			return null;
		}
		else
		{
			return (Integer)freeTablesList.pop();
		}
	}
	
    public static void changeNbFreeTableList(int nbTables)
    {
        Integer numTable = new Integer(0);
        for(int i=0; i<nbTables; i++)
        {            
            numTable = new Integer(nbTables-i);
            if(!(freeTablesList.contains(numTable))&&(numTable.intValue()>nbMaxTables))
            {
                freeTable(new Integer(nbTables-i));    
            }            
        }
        if(nbTables<nbMaxTables)
        {
            for(int i=nbTables+1;i<=nbMaxTables;i++)
            {
                freeTablesList.remove(new Integer(i));
            }
        }
           
        nbMaxTables=nbTables;
    }
    
    
	public static void initFreeTableList(int nbTables)
	{
        nbMaxTables = nbTables;
		for(int i=0; i<nbTables; i++)
		{
			freeTable(new Integer(nbTables-i));
		}
	}
		
	public void clearAllDescriptions()
	{
		descriptionTableauList.clear();
		descriptionTableauList.addAll(readFormulesAuto());
	}
	
	public ArrayList getDescriptionTableauList()
	{
		return descriptionTableauList;
	}
	
	private DescriptionTableau getDescription(String identifiant)
	{
		logger.debug("getDescription("+identifiant+")");
		for (Iterator iter = descriptionTableauList.iterator(); iter.hasNext();)
		{
			
			DescriptionTableau element = (DescriptionTableau) iter.next();
			if(element.getIdentifiant().equals(identifiant))
			{
				logger.debug("return element ");
				return element;
			}
		}
		logger.debug("return null !! ");
		return null;
	}
	
	public ArrayList getVisibleTableauxList()
	{
		ArrayList list = new ArrayList();
		for (Iterator iter = descriptionTableauList.iterator(); iter.hasNext();) {
			DescriptionTableau element = (DescriptionTableau) iter.next();
			if(element.isVisible())
			{
				list.add(element);
			}
			
		}
		return list;
		
	}
	
	public void modifierJoueur(Joueur oldJoueur, Joueur newJoueur)
	{
		int index = joueurs.indexOf(oldJoueur);
		if(index!=-1)
		{
			for (Iterator iter = tableaux.iterator(); iter.hasNext();)
			{
				Tableau element = (Tableau) iter.next();
				element.modifierJoueur(oldJoueur,newJoueur);
			}			
			joueurs.set(index,newJoueur);
			setChanged();
			notifyJoueursChanged();
		}
		
	}
	
	public void removeTableau(Tableau tableau)
	{
		tableaux.remove(tableau);
		notifyTableauxChanged();
	}
	
	private TableauSimple createTableauAuto(SortiePoule sortie)
	{
		TableauSimple tableau = null;
		logger.debug("sortie poule type = "+sortie.getType());
		//==== tableau ko ====
		if(TableauKO.TYPE.equals(sortie.getType()))
		{			
			tableau = new TableauKO(this);
			tableau.setMax_joueurs(sortie.getNbJoueurs());			
			((TableauSimple)tableau).calculOrdreMatchList();
		}
		//==== tableau Double Ko ====
		else if(TableauDoubleKO.TYPE.equals(sortie.getType()))
		{			
			tableau = new TableauDoubleKO(this);
			tableau.setMax_joueurs(sortie.getNbJoueurs());
			((TableauSimple)tableau).calculOrdreMatchList();
		}
		//==== tableau int�gral ====
		else if(TableauIntegral.TYPE.equals(sortie.getType()))
		{		
            logger.debug("creation d'un tableau int�gral !! ");
			tableau = new TableauIntegral(this);
			tableau.setMax_joueurs(sortie.getNbJoueurs());
			((TableauSimple)tableau).calculOrdreMatchList();
		}
		return tableau;				
	}
	
    public void updateDescriptionInTableau(DescriptionTableau description, Tableau tableau) throws TournoiException
    {
        //==== tableau poule ====
        if(TableauPoules.TYPE.equals(description.getType()))
        {
            if(tableau==null) throw new TournoiException("Ошибка при изменении формулы");
            if(((TableauPoules)tableau).getPoules()==null||((TableauPoules)tableau).getPoules().size()==0)
            {
                throw new TournoiException("Таблица должна быть разработана для внесения изменений");
            }
            if(((TableauPoules)tableau).getPoules().size()!=description.getNbPoules())
            {
                throw new TournoiException("Вы должны выбрать формулу с "+((TableauPoules)tableau).getPoules().size()+" пул");
            }
            ((TableauPoules)tableau).setClassementJoueurList(new ArrayList());
            ((TableauPoules)tableau).setNbJoueursParPoule(description.getNbJoueursParPoule());                              
            ((TableauPoules)tableau).getDescriptions().clear();
            ((TableauPoules)tableau).clearTableaux();
            ((TableauPoules)tableau).getTableaux().clear();
            //== creation des tableaux pour apr�s les sorties de poules ==
            int index=1;
            for (Iterator iter = description.getSortiePouleList().iterator(); iter.hasNext();)
            {
                SortiePoule element = (SortiePoule) iter.next();
                ((TableauPoules)tableau).addDescription(new Integer(element.getNbJoueurs()));
                TableauSimple tableauTemp = null;
                //== si c'est une description automatique : le tableau n'est pas r�f�renc� !
                tableauTemp = createTableauAuto(element);
                if(tableauTemp.getName()==null || tableauTemp.getName().equals(""))
                {
                    tableauTemp.setName("Tableau "+index++);
                }
                
                //== ajout de joueurs vides ==
                ArrayList joueurs = new ArrayList();
                for(int i=0;i<tableauTemp.getMax_joueurs();i++)
                {
                    joueurs.add(Joueur.EMPTY_JOUEUR);
                }
                try
                {
                    tableauTemp.setJoueurs(joueurs);
                }catch(Exception exp){/** ne doit pas arriver **/logger.fatal(exp);}
                tableauTemp.creerPremierTour();
                ((TableauPoules)tableau).addTableau(tableauTemp);                  
            } 
        }
        else
        {
            throw new TournoiException("Вы должны выбрать стол с пулами");
        }
        ((TableauPoules)tableau).initTableaux();
        
        
    }
    
	private Tableau createTableau(String identifiant)
	{
		DescriptionTableau description = getDescription(identifiant);		
				
		Tableau tableau = null;
		//==== tableau ko ====
		if(TableauKO.TYPE.equals(description.getType()))
		{			
			tableau = new TableauKO(this);
			tableau.setMax_joueurs(description.getMaxJoueurs());
			
			if(description.isOrdreMatchAuto())
			{
				((TableauSimple)tableau).calculOrdreMatchList();
			}
			else
			{
				((TableauSimple)tableau).ordreMatchList = (ArrayList)description.getOrdreMatchList().clone();
			}			 
		}
		//==== tableau Double Ko ====
		else if(TableauDoubleKO.TYPE.equals(description.getType()))
		{			
			tableau = new TableauDoubleKO(this);
			tableau.setMax_joueurs(description.getMaxJoueurs());
			if(description.isOrdreMatchAuto())
			{
				((TableauSimple)tableau).calculOrdreMatchList();
			}
			else
			{
				((TableauSimple)tableau).ordreMatchList = (ArrayList)description.getOrdreMatchList().clone();
			}
		}
		//==== tableau int�gral ====
		else if(TableauIntegral.TYPE.equals(description.getType()))
		{		
			tableau = new TableauIntegral(this);
			tableau.setMax_joueurs(description.getMaxJoueurs());
			if(description.isOrdreMatchAuto())
			{
				((TableauSimple)tableau).calculOrdreMatchList();
			}
			else
			{
				((TableauSimple)tableau).ordreMatchList = (ArrayList)description.getOrdreMatchList().clone();
			}
		}		
		//==== tableau poule ====
		else if(TableauPoules.TYPE.equals(description.getType()))
		{
			logger.debug("c'est un tableau poule");			
			tableau = new TableauPoules();	
			((TableauPoules)tableau).setClassementJoueurList(description.getClassementJoueurList());
			((TableauPoules)tableau).setNbJoueursParPoule(description.getNbJoueursParPoule());								
			//== creation des poules ==
			((TableauPoules)tableau).createPoule(description.getNbPoules());
			//== creation des tableaux pour apr�s les sorties de poules ==
			int index=1;
			for (Iterator iter = description.getSortiePouleList().iterator(); iter.hasNext();)
			{
				SortiePoule element = (SortiePoule) iter.next();
				((TableauPoules)tableau).addDescription(new Integer(element.getNbJoueurs()));
				TableauSimple tableauTemp = null;
				//== si c'est une description automatique : le tableau n'est pas r�f�renc� !
				if(getDescription(element.getRefTableau())==null)
				{
					tableauTemp = createTableauAuto(element);
				}
				//== sinon c'est une description manuelle
				else
				{
					tableauTemp = (TableauSimple)createTableau(element.getRefTableau());
					tableau.setMax_joueurs(description.getMaxJoueurs());
					tableau.setMin_joueurs(description.getMinJoueurs());
				}
				if(tableauTemp.getName()==null || tableauTemp.getName().equals(""))
				{
					tableauTemp.setName("Tableau "+index++);
				}
				
				//== ajout de joueurs vides ==
				ArrayList joueurs = new ArrayList();
				for(int i=0;i<tableauTemp.getMax_joueurs();i++)
				{
					joueurs.add(Joueur.EMPTY_JOUEUR);
				}
				try
				{
					tableauTemp.setJoueurs(joueurs);
				}catch(Exception exp){/** ne doit pas arriver **/logger.fatal(exp);}
				tableauTemp.creerPremierTour();
				((TableauPoules)tableau).addTableau(tableauTemp);							
			}
			
		}		

		if( tableau.getType().equals(TableauKO.TYPE) ||
			tableau.getType().equals(TableauDoubleKO.TYPE) ||
			tableau.getType().equals(TableauIntegral.TYPE))
		{
			((TableauSimple)tableau).addObserver(this);
		}

		return tableau;		
	}
	
	public Tableau addTableau(String identifiant, String name)
	{
        for (Iterator iter = getTableaux().iterator(); iter.hasNext();)
        {
            Tableau tableau = (Tableau) iter.next();
            if(name.equals(tableau.getName()))
            {
                return null;
            }
            
        }
		Tableau tableau = createTableau(identifiant);
		tableau.setName(name);
		tableaux.add(tableau);
		notifyTableauxChanged();			
		return tableau;		
	}
	
	public Tableau getTableau(String tableauName)
    {
        for (Iterator iter = getTableaux().iterator(); iter.hasNext();)
        {
            Tableau tableau = (Tableau) iter.next();
            if(tableauName.equals(tableau.getName()))
            {
                return tableau;
            }            
        }    
        return null;
    }
	
	
	public void addDescriptionTableau(DescriptionTableau description)
	{		
		descriptionTableauList.add(description);
	}
	
	public void notifyTableauxChanged()
	{
		setChanged();
		notifyObservers();				
	}	
	
	public void notifyJoueursChanged()
	{
		setChanged();
		notifyObservers();				
	}
	
	public Competition()
	{
		joueurs = new ArrayList();
		tableaux = new ArrayList();
		descriptionTableauList = new ArrayList();
		ArrayList temp = readFormulesAuto();
		if(temp!=null)
		{
			descriptionTableauList.addAll(readFormulesAuto());	
		}
		
	}
	
	public ArrayList getResultats(Tableau tableau)
	{
		int index = tableaux.indexOf(tableau);
		if(index==-1)
		{
			return null;
		}
		return ((Tableau)tableaux.get(index)).getResultats();
	}
	
	public ArrayList getResultats()
	{
		ArrayList resultats = new ArrayList();
		for (Iterator iter = tableaux.iterator(); iter.hasNext();)
		{
			Tableau tableau = (Tableau) iter.next();
			resultats.addAll(tableau.getResultats());
		}
		return resultats;
	}
	
	public ArrayList getJoueurs()
	{
		return joueurs;
	}

	public ArrayList getTableaux()
	{
		return tableaux;
	}

	private void clearDescriptionsAuto()
	{	
		boolean oneMore = false;
		while(true)
		{
			for (int i =0;i<descriptionTableauList.size();i++)
			{
				DescriptionTableau desc = (DescriptionTableau)descriptionTableauList.get(i);
				if(desc.isOrdreMatchAuto())
				{
					descriptionTableauList.remove(i);
					i=0;
					oneMore = true;					
				}					
			}			
			if(!oneMore)
			{
				break;
			}
			else
			{
				oneMore = false;
			}			
		}
	}

	public void updateDescriptionAuto(List descriptions)
	{
		clearDescriptionsAuto();
		descriptionTableauList.addAll(descriptions);
	}

	public boolean saveFormulesAuto()
	{
		XMLEncoder e = null;
		try
		{
			String path = System.getProperty("lax.root.install.dir");
			if(path==null) path = "";
			else path=path+"/";				
			File file = new File(path+"config/formulesAuto.xml");			
			// si le fichier n'existe pas on le cr�er
			if(!file.exists())
			{
				if(!file.getName().endsWith(".xml")&&!file.getName().endsWith(".XML"))
				{
					file = new File(file.getAbsolutePath()+".xml");
					if(!file.createNewFile())
					{
						return false;							
					}
				}
			}
			e = new XMLEncoder(
							   new BufferedOutputStream(
								   new FileOutputStream(file)));
			for (Iterator iter = descriptionTableauList.iterator(); iter.hasNext();)
			{				
				DescriptionTableau desc = (DescriptionTableau)iter.next();
				if(desc.isOrdreMatchAuto())
				{					
					e.writeObject(desc);					
				}					
			}						
		}
		catch(Exception exp)
		{
			System.out.println(exp);
			return false;
		}	
		finally
		{
			if(e!=null) e.close();
		}
		return true;				
		
	}

	public List getFormulesAuto()
	{
		List list = new ArrayList();
		for (Iterator iter = descriptionTableauList.iterator(); iter.hasNext();)
		{
			DescriptionTableau desc = (DescriptionTableau)iter.next();
			if(desc.isOrdreMatchAuto())
			{
				list.add(desc);					
			}					
		}				
		return list;
	}

	public void setJoueurs(ArrayList list)
	{
		joueurs = list;
		notifyJoueursChanged();
	}

	public void setTableaux(ArrayList list)
	{
		tableaux = list;
		setChanged();
		notifyObservers();				
	}

	public static Stack getFreeTablesList()
	{
		return freeTablesList;
	}

	public static void setFreeTablesList(Stack stack)
	{
		freeTablesList = stack;
	}

	public void setDescriptionTableauList(ArrayList list)
	{
		descriptionTableauList = list;
	}

	public static boolean isPrintDialogue()
	{
		return printDialogue;
	}

	public static void setPrintDialogue(boolean b)
	{
		printDialogue = b;
	}
	
	public boolean save(File file)
	{
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
						return false;							
					}
				}
			}
			XMLEncoder e = new XMLEncoder(
							   new BufferedOutputStream(
								   new FileOutputStream(file)));								  
			e.writeObject(this);
			e.close();
		}
		catch(Exception exp)
		{
			logger.error(exp);
			return false;
		}	
		return true;					
	}
	
	

	public boolean isRestauration()
	{
		return restauration;
	}

	public void setRestauration(boolean b)
	{
		restauration = b;
	}

	public static boolean isAfficheDossard()
	{
		return afficheDossard;
	}

	public static void setAfficheDossard(boolean b)
	{
		afficheDossard = b;
	}

	/**
	 * observe les changements dans les tableaux
	 */
	public void update(Observable o, Object arg)
	{
		notifyTableauxChanged();		
	}

	public ArrayList readFormulesAuto()
	{
		XMLDecoder d = null;
		ArrayList resultList = new ArrayList();
		String path = System.getProperty("lax.root.install.dir");
		if(path==null) path = "";
		else path=path+"/";				
		File file = new File(path+"config/formulesAuto.xml");
		if(!file.exists()) return null;
		try		
		{
			d = new XMLDecoder(
							   new BufferedInputStream(
								   new FileInputStream(file)));
			
		}
		catch(FileNotFoundException e)
		{
			return null;
		}
		while(true)
		{
			try
			{
				Object result = d.readObject();
				resultList.add(result);
			}
			catch(Exception e)
			{	
				break;			
			}				
				
		}
		if(d!=null) d.close();
		return resultList;
	}

}
