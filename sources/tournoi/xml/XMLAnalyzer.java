package tournoi.xml;



import javax.xml.parsers.*;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.xml.sax.*;
import org.xml.sax.helpers.DefaultHandler;

import tournoi.*;
import tournoi.forms.*;

/**
 * Lit un fichier .xml pour l'initialisation d'une competition.
 *
 */
public class XMLAnalyzer extends DefaultHandler implements ErrorHandler
{ 
	
	private static Logger logger = Logger.getLogger(XMLAnalyzer.class);
	private int index = 0;
	private DescriptionTableau descriptionTemp;	
	private Competition competition = new Competition();
	
	public XMLReader makeXMLReader() throws Exception
	{		
		SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
		saxParserFactory.setValidating(true);
		SAXParser saxParser = saxParserFactory.newSAXParser();		
		XMLReader parser = saxParser.getXMLReader();
		return parser;
	}	
	
	public Competition getCompetition()
	{
		index = 0;
		competition.clearAllDescriptions();
		try
		{
			XMLReader reader = makeXMLReader();
			reader.setContentHandler(this);
			reader.setErrorHandler(this);	
			String path = System.getProperty("lax.root.install.dir");
			if(path==null) path = "";
			else path=path+"/";				
			reader.parse(new org.xml.sax.InputSource(path+"config/competition.xml"));			
		}
		catch(Exception e)
		{
			logger.fatal(e);
			return null;		
		}		
		return competition;
	}
	
	/**
	 * permet d'enregistrer les paramètres du tableau
	 * @param attributes
	 */
	private void analyseTableauSimpleData(Attributes attributes)
	{
		descriptionTemp = new DescriptionTableau();
		descriptionTemp.setOrdreMatchAuto(false);
		for(int i=0; i<attributes.getLength(); i++)
		{
			// type du tableau
			if(XMLConstant.TABLEAU_TYPE.equals(attributes.getQName(i)))
			{
				if( XMLConstant.TABLEAU_KO.equals(attributes.getValue(i)))
				{
					descriptionTemp.setType(TableauKO.TYPE);
				}
				else if( XMLConstant.TABLEAU_DOUBLE_KO.equals(attributes.getValue(i)))
				{
					descriptionTemp.setType(TableauDoubleKO.TYPE);
				}	
				else if(XMLConstant.TABLEAU_INTEGRALE.equals(attributes.getValue(i)))
				{
					descriptionTemp.setType(TableauIntegral.TYPE);				
				}
			}
			// visibilité du tableau
			if(XMLConstant.TABLEAU_VISIBLE.equals(attributes.getQName(i)))
			{
				String strVisible=attributes.getValue(i);
				boolean visible = strVisible.equals("oui")?true:false;
				descriptionTemp.setVisible(visible);			
			}
			// nom du tableau			
			if(XMLConstant.TABLEAU_NOM.equals(attributes.getQName(i)))
			{
				descriptionTemp.setIdentifiant(attributes.getValue(i));
			}
			// nombre de joueurs max pour le tableau
			if(XMLConstant.TABLEAU_MAX_JOUEURS.equals(attributes.getQName(i)))
			{
				descriptionTemp.setMaxJoueurs(Integer.parseInt(attributes.getValue(i)));
					
			}
			// nombre de joueurs min pour le tableau
			if(XMLConstant.TABLEAU_MIN_JOUEURS.equals(attributes.getQName(i)))
			{
				descriptionTemp.setMinJoueurs(Integer.parseInt(attributes.getValue(i)));				
			}				
		}		
	}

	/**
	 * permet d'enregistrer les paramètres du tableau
	 * @param attributes
	 */
	private void analyseTableauPoulesData(Attributes attributes)
	{
		//== création d'un tableau poule ==
		descriptionTemp = new DescriptionTableau();
		descriptionTemp.setType(TableauPoules.TYPE);	
		// visibilité du tableau
		// on détermine le type de tableau et on l'initialise
		for(int i=0; i<attributes.getLength(); i++)
		{
			if(XMLConstant.TABLEAU_VISIBLE.equals(attributes.getQName(i)))
			{
				String strVisible=attributes.getValue(i);
				boolean visible = strVisible.equals("oui")?true:false;
				descriptionTemp.setVisible(visible);			
			}					
			if(XMLConstant.TABLEAU_NOM.equals(attributes.getQName(i)))
			{
				//== affectation du nom
				descriptionTemp.setIdentifiant(attributes.getValue(i));
			}

		}			
	}

	private void analyseCompetitionData(Attributes attributes)
	{
		for(int i=0; i<attributes.getLength(); i++)
		{
			if(XMLConstant.COMPETITION_NB_TABLES.equals(attributes.getQName(i)))
			{
				//== affectation du nom
				Competition.initFreeTableList(Integer.parseInt(attributes.getValue(i)));
			}

		}					
	}
	
	/**
	 * permet d'enregistrer l'ordre des match du tableau
	 * @param attributes
	 */
	private void analyseOrdreMatchData(Attributes attributes)
	{		
		Integer numJoueur1 = null;
		Integer numJoueur2 = null;
		for(int i=0; i<attributes.getLength(); i++)
		{
			if(XMLConstant.MATCH_ADVERSAIRE1.equals(attributes.getQName(i)))
			{
				numJoueur1 = new Integer(attributes.getValue(i));				

			}
			else if(XMLConstant.MATCH_ADVERSAIRE2.equals(attributes.getQName(i)))
			{
				numJoueur2 = new Integer(attributes.getValue(i));				
			}															
		}		
		descriptionTemp.addOrdreMatch(numJoueur1,numJoueur2);
	}
	

	private void analyseClassementPoulesData(Attributes attributes)
	{
		String name = "";
		ClassementJoueurs classementJoueur = new ClassementJoueurs();
		for(int i=0; i<attributes.getLength(); i++)
		{
			if(XMLConstant.CLASSEMENT_POULES_POULE.equals(attributes.getQName(i)))
			{
				classementJoueur.setNumPoule(Integer.parseInt(attributes.getValue(i)));				
			}
			else if(XMLConstant.CLASSEMENT_POULES_CLASSEMENT.equals(attributes.getQName(i)))
			{
				classementJoueur.setClassement(Integer.parseInt(attributes.getValue(i)));	
			}	
			else if(XMLConstant.CLASSEMENT_POULES_JOUEUR.equals(attributes.getQName(i)))
			{
				classementJoueur.setNumJoueur(Integer.parseInt(attributes.getValue(i)));	
			}						
		}		
		descriptionTemp.addClassementJoueurs(classementJoueur);
	}

	
	private void analyseSortiePouleData(Attributes attributes)
	{
		String name = "";
		SortiePoule sortiePoule = new SortiePoule();
		for(int i=0; i<attributes.getLength(); i++)
		{
			if(XMLConstant.SORTIE_POULES_NOMBRE_JOUEURS.equals(attributes.getQName(i)))
			{
				sortiePoule.setNbJoueurs(Integer.parseInt(attributes.getValue(i)));				
			}
			else if(XMLConstant.SORTIE_POULES_REF_TABLEAU.equals(attributes.getQName(i)))
			{
				sortiePoule.setRefTableau(attributes.getValue(i));							
			}			
		}
		descriptionTemp.addSortiePoule(sortiePoule);
	}
	
	/**
	 * 
	 * @param attributes
	 */
	private void analysePoulesData(Attributes attributes)
	{
		for(int i=0; i<attributes.getLength(); i++)
		{
			if(XMLConstant.POULES_NOMBRE.equals(attributes.getQName(i)))
			{
				descriptionTemp.setNbPoules(Integer.parseInt(attributes.getValue(i)));
			}
			else if(XMLConstant.POULES_NOMBRE_JOUEURS.equals(attributes.getQName(i)))
			{
				descriptionTemp.setNbJoueursParPoule(Integer.parseInt(attributes.getValue(i)));
			}			
		}				
	}
	
	//==== lecture du fichier XML ====
	public void startElement(String namespace, String localname, String type, Attributes attributes)
		throws SAXException
	{
		//== description de la competition ==
		if(XMLConstant.COMPETITION.equals(type))
		{
			analyseCompetitionData(attributes);
		}
		//== description d'un tableau simple ==		
		else if(XMLConstant.TABLEAU_SIMPLE.equals(type))		
		{
			analyseTableauSimpleData(attributes);				
		}
		//== description d'un match pour le tableau simple
		else if(XMLConstant.MATCH.equals(type))		
		{
			analyseOrdreMatchData(attributes);				
		}		
		//== description d'un classement de poule
		else if(XMLConstant.CLASSEMENT_POULES.equals(type))		
		{
			analyseClassementPoulesData(attributes);				
		}		
		
		//== description d'un tableau poules ==
		else if(XMLConstant.TABLEAU_POULES.equals(type))
		{
			analyseTableauPoulesData(attributes);
		}
		else if(XMLConstant.POULES.equals(type))
		{
			analysePoulesData(attributes);
		}
		else if(XMLConstant.SORTIE_POULES.equals(type))
		{
			analyseSortiePouleData(attributes);
		}
		
	}
	
	public void endElement(String namespace, String localname, String type) throws org.xml.sax.SAXException
	{
		if(XMLConstant.TABLEAU_SIMPLE.equals(type))
		{
			competition.addDescriptionTableau(descriptionTemp);
		}
		else if(XMLConstant.TABLEAU_POULES.equals(type))
		{
			competition.addDescriptionTableau(descriptionTemp);
		}
	}
		
	//==== erreur de lecture du fichier XML ====
	public void warning(SAXParseException arg0) throws SAXException
	{
		logger.warn(arg0.getMessage());	
			
	}

	public void error(SAXParseException arg0) throws SAXException
	{
		arg0.printStackTrace();
		logger.error(arg0.getStackTrace());
		logger.error(arg0.getMessage());
	}

	public void fatalError(SAXParseException arg0) throws SAXException
	{
		logger.fatal(arg0.getMessage());
	}	
	
	
	public static void testGUI()
	{	
		try
		{	
			XMLAnalyzer analyser = new XMLAnalyzer();
			Competition competition = analyser.getCompetition();				
			FormDesktop formDesktop = new FormDesktop(competition);
		}
		catch(Throwable th)
		{
			th.printStackTrace();
			logger.fatal(th);
			logger.fatal(th.getMessage());					
		}
	}
	
	/** read an example XML file */
	public static void main(final String[] args) throws Exception
	{			
		PropertyConfigurator.configure("log.properties");
		javax.swing.SwingUtilities.invokeLater(
			new Runnable()
			{
				public void run()
				{
					testGUI();
				}
			});		
	}
}
