package tournoi;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import org.apache.log4j.Logger;

/**
 * tableau avec des poules de joueurs. Après les sorties de poules, les joueurs se
 * retrouvent dans un ou plusieurs tableaux simples.
 */
public class TableauPoules implements Tableau
{
	public static final String TYPE = "TABLEAU_POULE";
	private static Logger logger = Logger.getLogger(TableauPoules.class);
	private ArrayList joueurs;
	private ArrayList tableaux;
	private ArrayList poules;
	private ArrayList descriptions;
	private int max_joueurs = 0;
	private int min_joueurs = 0;
	private String name = "";
	private int nbJoueursParPoule = 0;
	// permet de garder une trace de la facon dont les joueurs ont été ajouté dans les poules
	private ArrayList cltJoueursInPoule = new ArrayList();


	public boolean started = false;

	/** classement des joueurs à la fin de la poule */
	private ArrayList classementJoueurs;

	public void setClassementJoueurList(ArrayList list)
	{
		classementJoueurs = list;
	}

	/**
	 * supprime un joueur d'une poule
	 * @param poule la poule
	 * @param joueur le joueur à supprimer
	 * @return vrai si l'opération s'est bien passée
	 */
	public boolean removeJoueur(Poule poule, Joueur joueur)
	{
		boolean result = false;
		poule.removeJoueur(joueur);
		result = joueurs.remove(joueur);
		return result;
	}

	public void setDescription(ArrayList descriptionsList)
	{
		this.descriptions = descriptionsList;
	}

	public void createPoule(int nbPoules)
	{
		poules.clear();
		for (int i = 0; i < nbPoules; i++)
		{
			poules.add(new Poule("Poule " + (i + 1)));
		}
	}

	public boolean permuteToRight(Poule pouleOrigine, int index)
	{
		if (index < 0 || index > pouleOrigine.getJoueurs().size() - 1)
		{
			return false;
		}
		int indexPoule = poules.indexOf(pouleOrigine);
		if (indexPoule == -1)
			return false;
		int indexDest = (indexPoule == (poules.size() - 1)) ? 0 : (indexPoule + 1);
		Poule pouleDest = (Poule) poules.get(indexDest);
		Joueur jOrigine = null;
		Joueur jDest = null;
		if (index < pouleOrigine.getJoueurs().size())
		{
			jOrigine = (Joueur) pouleOrigine.getJoueurs().get(index);
		}
		if (index < pouleDest.getJoueurs().size())
		{
			jDest = (Joueur) pouleDest.getJoueurs().get(index);
		}
		pouleDest.replaceJoueur(jOrigine, jDest);
		pouleOrigine.replaceJoueur(jDest, jOrigine);
		return true;
	}

	public boolean permuteToLeft(Poule pouleOrigine, int index)
	{
		if (index < 0 || index > pouleOrigine.getJoueurs().size() - 1)
		{
			return false;
		}
		int indexPoule = poules.indexOf(pouleOrigine);
		if (indexPoule == -1)
			return false;
		int indexDest = (indexPoule == 0) ? (poules.size() - 1) : (indexPoule - 1);
		Poule pouleDest = (Poule) poules.get(indexDest);
		Joueur jOrigine = null;
		Joueur jDest = null;
		if (index < pouleOrigine.getJoueurs().size())
		{
			jOrigine = (Joueur) pouleOrigine.getJoueurs().get(index);
		}
		if (index < pouleDest.getJoueurs().size())
		{
			jDest = (Joueur) pouleDest.getJoueurs().get(index);
		}
		pouleDest.replaceJoueur(jOrigine, jDest);
		pouleOrigine.replaceJoueur(jDest, jOrigine);
		return true;
	}

	public TableauPoules()
	{
		tableaux = new ArrayList();
		poules = new ArrayList();
		descriptions = new ArrayList();
		joueurs = new ArrayList();
		classementJoueurs = new ArrayList();
	}

	public ArrayList getResultats()
	{
		return null;
	}

	public String toString()
	{
		return name;
	}

	/**
	 * recopie d'un tableau.
	 * @return un object de type TableauPoule 
	 */
	public Object clone()
	{
		TableauPoules tableauPoules = new TableauPoules();
		tableauPoules.max_joueurs = max_joueurs;
		tableauPoules.min_joueurs = min_joueurs;
		tableauPoules.joueurs = (ArrayList) getJoueurs().clone();
		tableauPoules.name = name;
		tableauPoules.tableaux = (ArrayList) tableaux.clone();
		tableauPoules.poules = (ArrayList) poules.clone();
		tableauPoules.descriptions = (ArrayList) descriptions.clone();
		return tableauPoules;
	}

	public boolean isTerminated()
	{
		for (Iterator iter = tableaux.iterator(); iter.hasNext();)
		{
			TableauSimple tableau = (TableauSimple) iter.next();
			for (Iterator iter2 = tableau.getTours().iterator(); iter.hasNext();)
			{
				Node node = (Node) iter2.next();
				if (!node.getMatch().isTerminated())
				{
					return false;
				}
			}
		}
		return true;
	}

	public void modifierJoueur(Joueur oldJoueur, Joueur newJoueur)
	{
		int index = joueurs.indexOf(oldJoueur);
		if(index!=-1)
		{
			joueurs.set(index,newJoueur);
		}
		//== modification dans les poules ==		
		for (Iterator iter = poules.iterator(); iter.hasNext();)
		{
			Poule element = (Poule) iter.next();
			int i = element.getJoueurs().indexOf(oldJoueur);
			if(i!=-1)
			{
				element.modifierJoueur(oldJoueur,newJoueur);
			}				
		}
		//== modification dans les sous tableaux
		for (Iterator iter = tableaux.iterator(); iter.hasNext();)
		{
			TableauSimple element = (TableauSimple) iter.next();
			element.modifierJoueur(oldJoueur, newJoueur);
		}
	}

	public boolean allPoulesAreTerminated()
	{
		for (Iterator iter = poules.iterator(); iter.hasNext();)
		{
			Poule poule = (Poule) iter.next();
			if (!poule.isTerminated())
			{
				return false;
			}
		}
		return true;
	}

	/**
	 * ajoute un joueur à la fin de la poule
	 */
	public void addJoueur(Joueur joueur)
	{
		boolean joueurAdded = false;
		if (!allPoulesAreTerminated())
		{
			int nbMaxJoueursInPoule = (int) Math.ceil((double) ((double) joueurs.size()+1 / (double) poules.size()));
			ClassementJoueurs nextClt = getNextClassement();
			((Poule)poules.get(nextClt.getNumPoule()-1)).addJoueurAtEnd(joueur);
			joueurs.add(joueur);
			cltJoueursInPoule.add(nextClt);
		}
	}

	/**
	 * Aide pour le remplissage des tableaux.
	 * @param nbJoueurs
	 * @return
	 */
	private boolean addJoueursInTab(ArrayList joueursClasses, int nbJoueurs, int first, int numTableau)
	{
		for (int i = 0; i < nbJoueurs; i++)
		{
			logger.debug("i+first = "+(i + first));
			logger.debug("joueurs classés.size"+joueursClasses.size());
			if ((i + first) < joueursClasses.size())
			{
				logger.debug("joueur :"+joueursClasses.get(i + first));
				((TableauSimple) tableaux.get(numTableau)).addJoueur((Joueur) joueursClasses.get(i + first));
			}
			else
			{
				((TableauSimple) tableaux.get(numTableau)).addJoueur(Joueur.EMPTY_JOUEUR);
			}
		}
		return true;
	}

	private int getMatchIndex(ArrayList matchs, int numJoueur)
	{
		for (Iterator iter = matchs.iterator(); iter.hasNext();)
		{
			OrdreMatch element = (OrdreMatch) iter.next();
			if (element.getAdversaire1() == numJoueur || element.getAdversaire2() == numJoueur)
			{
				return matchs.indexOf(element);
			}
		}
		return 0;
	}

	/**
	 * permet d'obtenir le numéro de la poule d'un joueur par rapport à son dossart
	 * @param numJoueur le numéro de dossart du joueur
	 * @return le numéro de la poule ou -1 si la poule n'a pas été  trouvée
	 */
	private int getNumPoule(int numJoueur)
	{
		Joueur joueur = (Joueur) joueurs.get(numJoueur - 1);
		for (int i = 0; i < poules.size(); i++)
		{
			Poule poule = (Poule) poules.get(i);
			for (Iterator iterator = poule.getJoueurs().iterator(); iterator.hasNext();)
			{
				Joueur element = (Joueur) iterator.next();
				if (joueur.equals(element))
				{
					return i + 1;
				}
			}
		}
		return -1;
	}

	/**
	 * class le joueur passé en parametres ainsi que tout les joueurs de sa partie de tableau
	 * @param numDossart le numero de dossart du joueur à classer
	 * @param joueursClasses la liste des joueurs auquel les joueurs doivent être ajoutés
	 * @param ordreMatchs l'ordre de match de tableau
	 */
	private void classeJoueur(
		ClassementJoueurs cltJoueur,
		ArrayList joueursClasses,
		ArrayList ordreMatchs,
		ArrayList cltInPouleList,
        int posFirstInPoules,
        int nbJoueursParPoules,
        boolean whithPoulesInterdites)
	{
		int nbMaxJoueursInTab = nbJoueursParPoules*poules.size();
        int profondeur = findProfondeur(ordreMatchs.size());
        logger.debug("PROFONDEUR = "+profondeur);        
		ArrayList poulesInterdites = new ArrayList();
       if(!joueursClasses.contains(cltJoueur))
        {
            joueursClasses.add(cltJoueur);
            logger.debug("JC="+cltJoueur);
            if(whithPoulesInterdites)
            {    
                poulesInterdites.add(new Integer(cltJoueur.getNumPoule()));
                logger.debug("Poule interdite += "+cltJoueur.getNumPoule());
            }            
        }
        else if(whithPoulesInterdites)
        {            
            {
                int numPoule = getJoueurClasse(cltJoueur.getClassement(),joueursClasses).getNumPoule();
                poulesInterdites.add(new Integer(numPoule));
                logger.debug("Poule interdite += "+numPoule);
            }
        }
       int decalage = (posFirstInPoules-1)*(poules.size());
        ArrayList sortedPlayers = getNextSortedPlayers(cltJoueur.getClassement(),ordreMatchs,nbJoueursParPoules,profondeur);
        for (Iterator iter = sortedPlayers.iterator(); iter.hasNext();)
        {
            int nextPlayer = ((Integer) iter.next()).intValue();
            int pos = (int) Math.ceil((double) ((double) nextPlayer / (double) poules.size()))+posFirstInPoules-1;            
            //== si c une pseudo tete de série
            if(pos==posFirstInPoules)
            {
                ClassementJoueurs clt = getClassementJoueur(cltJoueursInPoule, nextPlayer, posFirstInPoules);
                int sup = poules.size()*(clt.getNumJoueur()-posFirstInPoules+1);
                int inf = sup-poules.size()+1;
                if(!(clt.getClassement()<=sup&&clt.getClassement()>=inf))
                {
                    clt.setClassement(clt.getClassement()-decalage);
                }
                if(clt.compareTo(cltJoueur)!=0)
                {
                    if(!joueursClasses.contains(clt))
                    {
                        joueursClasses.add(clt);
                        logger.debug("JC="+clt);
                        if(whithPoulesInterdites)
                        {
                            poulesInterdites.add(new Integer(clt.getNumPoule()));
                            logger.debug("Poule interdite += "+clt.getNumPoule());
                        }
                    }
                    else if(whithPoulesInterdites)
                    {
                        logger.debug("ATTENTION : "+clt.getClassement());
                        int numPoule = getJoueurClasse(clt.getClassement(),joueursClasses).getNumPoule();
                        poulesInterdites.add(new Integer(numPoule));
                        logger.debug("Poule interdite += "+numPoule);
                    }
                }
            }
            else
            {
                int nbMax = (int) Math.ceil((double) ((double) joueurs.size() / (double) poules.size()));                
                logger.debug("POS "+pos);
                ClassementJoueurs nextClassement =
                    getBadPlayer(nextPlayer, joueursClasses,cltInPouleList, pos, poulesInterdites, nbMax);
                
                if (nextClassement != null)
                {
                    // le joueur ne doit pas déjà être classé et écaser un ancien classement
                    if(getJoueurClasse(nextClassement.getClassement(),joueursClasses)==null)
                    {
                        joueursClasses.add(nextClassement);
                        logger.debug("JC="+nextClassement);                        
                        poulesInterdites.add(new Integer(nextClassement.getNumPoule()));
                        logger.debug("Poule interdite += "+nextClassement.getNumPoule());
                    }
                    else
                    {
                        int numPoule = getJoueurClasse(nextClassement.getClassement(),joueursClasses).getNumPoule();
                        poulesInterdites.add(new Integer(numPoule));
                        logger.debug("Poule interdite += "+numPoule);                        
                    }
                }
                else
                {
                    logger.debug("CLASSEMENT NULL");                    
                }
            }
        }
        
	}

    /**
     * return une list de joueur classes par position
     * @param joueursClasses la liste des joueurs classés
     * @param pos la position à rechercher
     * @return la liste des joueurs situés à la même position.
     */
    private ArrayList getJoueurParPos(ArrayList joueursClasses, int firstInPoule, int pos)
    {
        ArrayList result = new ArrayList();
        for (Iterator iter = joueursClasses.iterator(); iter.hasNext();)
        {
            ClassementJoueurs element = (ClassementJoueurs) iter.next();
            int posElement = (int) Math.ceil((double) ((double)element.getClassement() / (double) poules.size()))+firstInPoule-1;
            if(posElement==pos)
            {
                result.add(element);
            }
        }
        return result;
    }
    
    /**
     * permet de permuter 2 joueurs classés
     * @param joueursClasses la liste des joueurs classés
     * @param j1 le joueur1
     * @param j2 le joueur2
     */
    private void permuteJoueur(ArrayList joueursClasses, ClassementJoueurs j1, ClassementJoueurs j2)
    {
        int index1 = joueursClasses.indexOf(j1);
        int index2 = joueursClasses.indexOf(j2);
        if(index1!=-1 && index2!=-1)
        {
            ClassementJoueurs joueurClasse1 = (ClassementJoueurs)joueursClasses.get(index1);
            ClassementJoueurs joueurClasse2 = (ClassementJoueurs)joueursClasses.get(index2);
            int clt1 = joueurClasse1.getClassement();
            int clt2 = joueurClasse2.getClassement();
            joueurClasse1.setClassement(clt2);
            joueurClasse2.setClassement(clt1);
        }
    }
    
    /**
     * permet de corriger les erreurs d'un tableau
     * @param ordreMatch
     * @param joueursClasses
     * @param profondeur
     * @return
     */
    public int fixeErrors(ArrayList ordreMatch, ArrayList joueursClasses, int posFirst, int profondeur)
    {
        ArrayList errorsList = checkErrorsList(ordreMatch, joueursClasses, profondeur, posFirst);
        int nbErrors = errorsList.size();
        for (Iterator iter = errorsList.iterator(); iter.hasNext();)
        {
            ClassementJoueurs element = (ClassementJoueurs) iter.next();
            int pos = (int) Math.ceil((double) ((double)element.getClassement() / (double) poules.size()))+posFirst-1;
            ArrayList joueursParPos = getJoueurParPos(joueursClasses, posFirst, pos);
            for (Iterator iterator = joueursParPos.iterator(); iterator.hasNext();)
            {
                ClassementJoueurs element2 = (ClassementJoueurs) iterator.next();
                permuteJoueur(joueursClasses,element,element2);
                int newNbErrors = checkErrorsList(ordreMatch, joueursClasses, posFirst, profondeur).size(); 
                if(newNbErrors<nbErrors)
                {
                    return newNbErrors;
                }
                else
                {
                    permuteJoueur(joueursClasses,element,element2);
                }
            }
            
        }
        return nbErrors;
    }
    
	/**
	 * trouve le ClassementJoueur dans la liste suivant les critères fournis.
	 * @param cltJoueurList
	 * @param numPoule le numéro de la poule du joueur
	 * @param numJoueur le numéro du joueur dans la poule
	 * @return null si aucun joueur n'a été trouvé
	 */
	private ClassementJoueurs getClassementJoueur(ArrayList cltJoueurList, int numPoule, int numJoueur)
	{
		for (Iterator iter = cltJoueurList.iterator(); iter.hasNext();)
		{
			ClassementJoueurs element = (ClassementJoueurs) iter.next();
			if (element.getNumPoule() == numPoule && element.getNumJoueur() == numJoueur)
			{
				return element;
			}
		}
		return null;
	}


	/**
	 * calcul la description la meilleur pour le classement des joueurs
	 * en sortie de poules.
	 * Afin que 2 joueurs de la même poule se rencontre le plus tard possible
	 * @return une liste de ClassementJoueurs
	 */
	private ArrayList calculDescription()
	{        
        ArrayList descriptions = getDescriptions();
        
        int posFirst = 1;
        ArrayList result = new ArrayList();
        int nbMaxJoueursInPoule = (int) Math.ceil((double) ((double) joueurs.size() / (double) poules.size()));
        for (Iterator iter1 = descriptions.iterator(); iter1.hasNext();)
        {
        	int decalage = (posFirst-1)*(poules.size());
            Integer element1 = (Integer) iter1.next();
            int nbJoueurs = element1.intValue();
            //== si le nombre de joueur est egal à 0 on prend tous le joueurs restant
            if(nbJoueurs==0)
            {
                logger.debug("nbMaxJoueursInPoule = "+nbMaxJoueursInPoule);
                logger.debug("posFirst = "+posFirst);
                nbJoueurs=(nbMaxJoueursInPoule-posFirst+1)*poules.size();
            }
            int nbJoueurParPoules = (int)(nbJoueurs/poules.size());
                        
            ArrayList joueursClasses = new ArrayList();        
            //== récupération du classement des joueurs dans les poules ==
            ArrayList cltInPouleList = getCompletedCltJoueurInPoule();
            logger.debug("CLTINPOULELIST.SIZE = "+cltInPouleList.size());
            //== récupération de l'ordre des matchs par rapport au nombre de joueurs ==
            ArrayList ordreMatchs = TableauSimple.getOrdreMatchList(nbJoueurs);
            logger.debug("OrdreMatchs.size()="+ordreMatchs.size());
            logger.debug("nbJoueurs="+nbJoueurs);            
            //== classement des têtes de série ==
            // attention, les joueurs passés en paramètres sont peut être les derniers de la poule
            // dans ce cas, les têtes de séries ne sont pas forcement en position 1 dans la poule       
            for (int i = 0; i < poules.size() && joueursClasses.size()<joueurs.size(); i++)
            {
                ClassementJoueurs clt = getClassementJoueur(cltInPouleList, i + 1, posFirst);
                int sup = poules.size()*(clt.getNumJoueur()-posFirst+1);
                int inf = sup-poules.size()+1;
                if(!(clt.getClassement()<=sup&&clt.getClassement()>=inf))
                {
                    clt.setClassement(clt.getClassement()-decalage);
                }              
                classeJoueur(clt, joueursClasses, ordreMatchs, cltInPouleList,posFirst,nbJoueurParPoules, true);
                logger.debug("JoueursClasses.size()="+joueursClasses.size());
            }
            //== on tente de classer les autres joueurs en évitant les poules interdites ==
            logger.debug("joueur.size()"+joueurs.size());
            for (Iterator iter = cltInPouleList.iterator(); iter.hasNext();)
            {                
                ClassementJoueurs element = (ClassementJoueurs) iter.next();                
                logger.debug("P"+element.getNumPoule()+"J"+element.getNumJoueur());
                if (!joueursClasses.contains(element)&&element.getNumJoueur()>=posFirst)
                {
                    logger.debug("classement suplémentaire : ");
                    logger.debug("Joueur "+element.getClassement());
                    int sup = poules.size()*(element.getNumJoueur()-posFirst+1);
                    int inf = sup-poules.size()+1;
                    if(!(element.getClassement()<=sup&&element.getClassement()>=inf))
                    {
                        element.setClassement(element.getClassement()-decalage);
                    }                                        
                    classeJoueur(element, joueursClasses, ordreMatchs, cltInPouleList,posFirst,nbJoueurParPoules, true);
                    logger.debug("JoueursClasses.size()="+joueursClasses.size());
                }
                if(joueursClasses.size()>=(nbJoueurParPoules*poules.size())) break;    
            }
            if(joueursClasses.size() < (nbJoueurParPoules*poules.size()))
            {
                //== on tente de classer les dernier joueur coûte que coûte            
                for (Iterator iter = cltInPouleList.iterator(); iter.hasNext();)
                {                
                    ClassementJoueurs element = (ClassementJoueurs) iter.next();
                    logger.debug("P"+element.getNumPoule()+"J"+element.getNumJoueur());
                    if (!joueursClasses.contains(element)&&element.getNumJoueur()>=posFirst)
                    {
                        logger.debug("classement suplémentaire : ");
                        logger.debug("Joueur "+element.getClassement());
                        int sup = poules.size()*(element.getNumJoueur()-posFirst+1);
                        int inf = sup-poules.size()+1;
                        if(!(element.getClassement()<=sup&&element.getClassement()>=inf))
                        {
                            element.setClassement(element.getClassement()-decalage);
                        }                                        
                        classeJoueur(element, joueursClasses, ordreMatchs, cltInPouleList,posFirst,nbJoueurParPoules, false);
                        logger.debug("JoueursClasses.size()="+joueursClasses.size());
                    }
                    if(joueursClasses.size()>=(nbJoueurParPoules*poules.size())) break;    
                }
            }
            //== détection et correction des joueurs de la même poule situés dans 
            //== la même partie de tableau
            int nbError = fixeErrors(ordreMatchs,joueursClasses,posFirst, findProfondeur(ordreMatchs.size()));
            logger.debug(nbError+" ERREURS DETECTEES !!!");
            int currentErrors = nbError;
            while(currentErrors!=0)
            {
                currentErrors = fixeErrors(ordreMatchs,joueursClasses,posFirst, findProfondeur(ordreMatchs.size()));
                if(currentErrors==nbError) break;
                nbError=currentErrors;
            }
            if(currentErrors>0)
            {
                logger.debug("Il reste "+currentErrors+" erreurs !!! ");
            }
            posFirst = posFirst+nbJoueurParPoules;
            addJoueursClasses(joueursClasses, result, decalage);
            // result.addAll(joueursClasses);
            joueursClasses.clear();
        }
        return result;
	}

    private void addJoueursClasses(ArrayList joueursClasses, ArrayList result, int decalage)
    {
        for (Iterator iter = joueursClasses.iterator(); iter.hasNext();)
        {
            ClassementJoueurs element = (ClassementJoueurs) iter.next();
            element.setClassement(element.getClassement()+decalage);
            result.add(element);
        }
    }
    
	private ClassementJoueurs getBadPlayer(
		int classement,
		ArrayList joueursClasses,
		ArrayList cltJoueursInPoule,
		int pos,
		ArrayList poulesInterdites,
		int nbMaxJoueurInPoule)
	{
        logger.debug("DANS GET BAD PLAYER : ");
        logger.debug("POS = "+pos);
        logger.debug("nbMaxJoueurInPOule = "+nbMaxJoueurInPoule);
		if (pos > nbMaxJoueurInPoule)
		{
			return null;
		}
		int joueurIndex = -1;
		int pouleIndex = -1;
        
        
		// en évitant les poules interdites, on récupère le joueurs le moins bien classé
		for (int i = 0; i < poules.size(); i++)
		{
			Integer ii = new Integer(i + 1);
			if (!poulesInterdites.contains(ii))
			{
				Joueur currentJoueur = Joueur.EMPTY_JOUEUR;
				ClassementJoueurs clt = getClassementJoueur(cltJoueursInPoule, i + 1, pos);
				if (!joueursClasses.contains(clt))
				{
					if (clt != null && joueurIndex < clt.getClassement())
					{
						joueurIndex = clt.getClassement();
						pouleIndex = i;
					}
				}
			}
		}
        if (joueurIndex == -1)
        {
            logger.debug("Joueur introuvable");
            logger.debug("FACHE DE DIOS !!! ");
            return null;
        }
		ClassementJoueurs clt = new ClassementJoueurs();
		clt.setClassement(classement);
		clt.setNumJoueur(pos);
		clt.setNumPoule(pouleIndex + 1);
		return clt;
	}

    /**
     * Fonction interne utile pour récupérer un liste triés des prochain joueurs
     * placés dans la même partie du tableau que le joueur passé en paramètre
     * @param numJoueur le numéro du joueur
     * @param ordreMatchs l'ordre des match du tableau
     * @return la liste des dossards triés des prochains joueurs de la même partie du tableau
     */
    /**
     * @param numJoueur
     * @param ordreMatchs
     * @return
     */
    private ArrayList getNextSortedPlayers(int numJoueur, ArrayList ordreMatchs, int nbMaxJoueursInPoule, int profondeur)
    {
        int coef = (numJoueur % 2 == 0) ? -1 : 1;        
        int matchIndex = getMatchIndex(ordreMatchs, numJoueur);
        int prof = (int)Math.floor((double)profondeur/(double)2);
        int deadLine = (int)Math.floor((double)matchIndex/(double)prof);
        int currentLine = deadLine;
        logger.debug("Prof = "+prof);
        logger.debug("deadLine = "+deadLine);
        ArrayList sortedPlayerList = new ArrayList();    
        while(sortedPlayerList.size()<profondeur)
        {
            OrdreMatch ordreMatch = (OrdreMatch) ordreMatchs.get(matchIndex);
            int adv1 = ordreMatch.getAdversaire1();
            int adv2 = ordreMatch.getAdversaire2();
            /** si on peut encore ajouter deux joueur **/
            if(sortedPlayerList.size()+1<profondeur)
            {                
                if(adv1<=(poules.size()*nbMaxJoueursInPoule))
                {
                    sortedPlayerList.add(new Integer(adv1));
                    logger.debug("ajout de "+adv1);
                }               
                if(adv2<=(poules.size()*nbMaxJoueursInPoule))
                {
                    sortedPlayerList.add(new Integer(adv2));
                    logger.debug("ajout de "+adv2);
                }                
            }
            /** sinon, on ajoute le meilleur joueur **/
            else if((adv1==0||adv2==0)&&(adv1!=0||adv2!=0))
            {
                if(adv1!=0 && adv1<=(poules.size()*nbMaxJoueursInPoule))
                {
                    sortedPlayerList.add(new Integer(adv1));
                    logger.debug("ajout de "+adv1);
                }               
                if(adv2!=0 && adv2<=(poules.size()*nbMaxJoueursInPoule))
                {
                    sortedPlayerList.add(new Integer(adv2));
                    logger.debug("ajout de "+adv2);
                }                                
            }
            matchIndex += coef;
            currentLine = (int)Math.floor((double)matchIndex/(double)prof);
            if(currentLine!=deadLine) break;
        }
        /** on retourne la liste trié **/
        Collections.sort(sortedPlayerList);
        return sortedPlayerList;
    }
    
    private boolean isRacine2(int number)
    {
    	int tmpNum = 2;
    	while(tmpNum<number)
    	{
    		tmpNum*=2;
    	}
    	return (tmpNum==number)?true:false;
    }
    
    /**
     * calcul une profondeur afin que deux joueur ne puissent se rencontrer
     * avant les 1/2 finales
     * @param nbMatchs le nombre de matchs du tableau
     * @return la profondeur calculée
     */
    private int findProfondeur(int nbMatchs)    
    {
    	
        if(nbMatchs<4) return poules.size();
        if(isRacine2(poules.size())&&poules.size()<=nbMatchs)
        {
        	return poules.size();
        }
        int profondeur=nbMatchs/2;
        while(profondeur>poules.size())
        {
            profondeur /= 2;
        }
        return profondeur;
    }

	/**
	 * fonction interne qui permet de savoir si un joueur à déjà été classé
	 * @param numJoueur le dossard du joueur
	 * @param joueursClasses les joueurs déja classés
	 * @return true si le joueur à déjà été classé
	 */
	private boolean isClasse(int numJoueur, ArrayList joueursClasses)
	{
		for (Iterator iter = joueursClasses.iterator(); iter.hasNext();)
		{
			ClassementJoueurs element = (ClassementJoueurs) iter.next();
			if (element.getClassement() == numJoueur)
			{
				return true;
			}
		}
		return false;
	}

    private ClassementJoueurs getJoueurClasse(int classement, ArrayList joueursClasses)
    {
        for (Iterator iter = joueursClasses.iterator(); iter.hasNext();)
        {
            ClassementJoueurs element = (ClassementJoueurs) iter.next();
            if (element.getClassement() == classement)
            {
                return element;
            }
        }
        return null;
    }

    
	/**
	 * permet de classer automatiquement les joueurs en sortie de poule.
	 * @return les joueurs triés
	 */
	private ArrayList sortWithoutDescription()
	{
		classementJoueurs = calculDescription();
		return sortWithDescription();
	}

	private ArrayList sortWithDescription()
	{
		ArrayList joueursClasses = new ArrayList();
		// on remplit la liste avec des joueurs vident
        int nbMaxJoueursInPoule = (int) Math.ceil((double) ((double) joueurs.size() / (double) poules.size()));
        int nbMaxJoueursInTab = nbMaxJoueursInPoule*poules.size();
        int nb_Joueur = 0;
        for(nb_Joueur=1;nb_Joueur<nbMaxJoueursInTab;nb_Joueur=nb_Joueur*2);         
		for (int x = 0; x < nb_Joueur; x++)
		{
			joueursClasses.add(Joueur.EMPTY_JOUEUR);
		}
		// on suit l'ordre de la description
		for (Iterator iter = classementJoueurs.iterator(); iter.hasNext();)
		{
            logger.debug("Dans classement joueurs : ");
			ClassementJoueurs element = (ClassementJoueurs) iter.next();
            logger.debug("P"+element.getNumPoule()+"J"+element.getNumJoueur());
            ArrayList joueurList = new ArrayList();
            if(element.getNumPoule() - 1>=0)
            {
                joueurList = ((Poule) poules.get(element.getNumPoule() - 1)).getJoueursParPosition();
            }
			Joueur joueur = Joueur.EMPTY_JOUEUR;
            logger.debug("joueurList.size()"+joueurList.size());
			if ((element.getNumJoueur() - 1) < joueurList.size())
			{
				joueur = (Joueur) joueurList.get(element.getNumJoueur() - 1);
			}
           if(!joueur.equals(Joueur.EMPTY_JOUEUR))
            {
                joueursClasses.set(element.getClassement() - 1, joueur);
            }
		}
		return joueursClasses;
	}

	/**
	 * A obptimiser.
	 * complète les tableau avec les joueurs en respectant la description.
	 * toutes les poules doivent être terminées.
	 */
	public boolean completeTableaux()
	{
		//== on vérifie que toutes les poules sont bien terminées ==
		for (Iterator iter = poules.iterator(); iter.hasNext();)
		{
			Poule poule = (Poule) iter.next();
			if (!poule.isTerminated())
			{
				return false;
			}
		}
		// on classe les joueurs automatiquement ou suivant l'ordre indiqué dans le fichier de configuration
		ArrayList joueursClasses = new ArrayList();
		if (classementJoueurs==null || classementJoueurs.isEmpty())
		{
			logger.debug("sort without description");
			joueursClasses = sortWithoutDescription();
		}
		else
		{
			logger.debug("sort with description");
			joueursClasses = sortWithDescription();
		}
		//== on complète les tableaux avec les joueurs présents dans les poules ==
		int i = 0;
		int index = 0;
		boolean stop = false;
		for (Iterator iter = descriptions.iterator(); iter.hasNext();)
		{
			int nbJoueurs = ((Integer) iter.next()).intValue();
			if(nbJoueurs==0)
			{
				int nbMaxJoueursInPoule = (int) Math.ceil((double) ((double) joueurs.size() / (double) poules.size()));
				nbJoueurs = (nbMaxJoueursInPoule*poules.size()) - index;
				logger.debug("0 : on ajoute tous les joueurs restants : "+nbJoueurs);
				logger.debug("joueurd classés.size() : "+joueursClasses.size());
				stop=true;
			}
			addJoueursInTab(joueursClasses, nbJoueurs, index, i);
			i++;
			index += nbJoueurs;
			if(stop) break;
		}
		return true;
	}

	/**
	 * initialisation de tous les tabelaux simples 
	 * contenus dans ce tableau. 
	 */
	public void initTableaux()
	{
		int index = 0;
		logger.debug("dans init");
		for (Iterator iter = tableaux.iterator(); iter.hasNext();)
		{
			Tableau element = (Tableau) iter.next();
			ArrayList joueursVides = new ArrayList();
			logger.debug("max : "+element.getMax_joueurs());
			if(element.getMax_joueurs()!=0)
			{
				index += element.getMax_joueurs();
				for (int i = 0; i < element.getMax_joueurs(); i++)
				{
					joueursVides.add(Joueur.EMPTY_JOUEUR);
				}				
			}
			else
			{
				int maxJoueurs = joueurs.size()-index;
				int nb_Joueur = 0;
				for(nb_Joueur=1;nb_Joueur<maxJoueurs;nb_Joueur=nb_Joueur*2);
				logger.debug("ajout de "+nb_Joueur+" joueurs vides");
				for (int i = 0; i < nb_Joueur; i++)
				{
					joueursVides.add(Joueur.EMPTY_JOUEUR);
				}						
			}
			try
			{
				element.setJoueurs(joueursVides);
			}
			catch (Exception e)
			{
			}

		}
	}

    private ClassementJoueurs getJoueur(ArrayList joueursClasses, int classement)
    {
        for (Iterator iter = joueursClasses.iterator(); iter.hasNext();)
        {
            ClassementJoueurs element = (ClassementJoueurs) iter.next();
            if(element.getClassement()==classement)
            {
                return element;
            }
        }
        return null;
    }
    
    /**
     * permet d'obtenir la list des joueurs de la même poule classé dans la<br>
     * même partie de tableau
     * @param OrdreMatch la liste des matchs du tableau
     * @param joueursClasses la liste des joueurs déja classés
     * @param profondeur le nombre de matchs pour lesquels les joueurs doivent tous <br>
     * être de poules différentes
     * @return la liste des joueurs concernés par une erreur
     */
    private ArrayList checkErrorsList(ArrayList ordreMatch, ArrayList joueursClasses, int posFirst, int profondeur)
    {
        ArrayList resultList = new ArrayList();
        ArrayList poulesList = new ArrayList();
        int cur = 1;
        int nbMax = (int) Math.ceil((double) ((double) joueurs.size() / (double) poules.size()));
        int decalage = (posFirst-1)*(poules.size()); 
        for (Iterator iter = ordreMatch.iterator(); iter.hasNext();)
        {
            OrdreMatch element = (OrdreMatch) iter.next();   
            ClassementJoueurs clt1 =  getJoueur(joueursClasses,element.getAdversaire1()+decalage);
            if(clt1!=null)
            {
                logger.debug("JOUEUR TROUVE");
                if(poulesList.contains(new Integer(clt1.getNumPoule())))
                {                    
                    resultList.add(clt1);
                    logger.debug("ERROR dans check error liste: "+clt1);
                }
                else
                {
                    poulesList.add(new Integer(clt1.getNumPoule()));
                    logger.debug("Ajout de la poule : "+clt1.getNumPoule());
                }
            }
            ClassementJoueurs clt2 =  getJoueur(joueursClasses,element.getAdversaire2()+decalage);
            if(clt2!=null)
            {
                logger.debug("JOUEUR TROUVE");
                if(poulesList.contains(new Integer(clt2.getNumPoule())))
                {
                    JoueurInTab error = new JoueurInTab(ordreMatch.indexOf(element), 2,clt2.getClassement());
                    resultList.add(clt2);
                    logger.debug("ERROR : "+clt2);
                }
                else
                {
                    poulesList.add(new Integer(clt2.getNumPoule()));
                    logger.debug("Ajout de la poule : "+clt2.getNumPoule());
                }
            }
            if(cur>=profondeur)
            {
                logger.debug("On efface les poules");
                cur=1;
                poulesList.clear();
            }
            cur+=2;
        }
        return resultList;
    }
    
	public void clearTableaux()
	{
		for (Iterator iter = tableaux.iterator(); iter.hasNext();)
		{
			Tableau element = (Tableau) iter.next();
			element.getJoueurs().clear();
		}
	}

	public void addDescription(Integer numberOFJoueurs)
	{
		descriptions.add(numberOFJoueurs);
	}

	public void addTableau(Tableau tableau)
	{
		tableaux.add(tableau);
	}

	public void addPoule(Poule poule)
	{
		poules.add(poule);
	}

	public ArrayList getPoules()
	{
		return poules;
	}

	public ArrayList getTableaux()
	{
		return tableaux;
	}

	/* (non-Javadoc)
	 * @see tournoi.Tableau#getType()
	 */
	public String getType()
	{
		return TYPE;
	}

	/* (non-Javadoc)
	 * @see tournoi.Tableau#getName()
	 */
	public String getName()
	{
		return name;
	}
	private ArrayList getCompletedCltJoueurInPoule()
	{        
		ArrayList list = new ArrayList();
        int nbMaxJoueursInPoule = (int) Math.ceil((double) ((double) joueurs.size() / (double) poules.size()));
        int numPoule=0;
        int cltJ = 1;
        for(int j=0; j<nbMaxJoueursInPoule; j++)
        {
            for(int p=0; p<poules.size(); p++)
            {
                //== si derniers joueurs de la poules
                if(j==(nbMaxJoueursInPoule-1))
                {
                    numPoule=poules.size()-(p);                        
                }
                else
                {
                    //== si ligne impaire
                    if((j+1)%2!=0)
                    {                    
                        numPoule=p+1;
                    }
                    //== si ligne paire
                    else
                    {
                        numPoule=poules.size()-(p);
                    }
                }
                ClassementJoueurs classement = new ClassementJoueurs();
                classement.setNumPoule(numPoule);
                classement.setNumJoueur(j+1);
                classement.setClassement(cltJ);
                logger.debug("classement : P"+classement.getNumPoule()+"J"+classement.getNumJoueur()+" "+classement.getClassement());               
                list.add(classement);
                cltJ+=1;
            }
            
        }
		return list;
	}

	private ClassementJoueurs getNextClassement()
	{
		ClassementJoueurs cltLast = (ClassementJoueurs)cltJoueursInPoule.get(cltJoueursInPoule.size()-1);
		ClassementJoueurs cltLast_1 = (ClassementJoueurs)cltJoueursInPoule.get(cltJoueursInPoule.size()-2);
		ClassementJoueurs clt = new ClassementJoueurs();
		clt.setClassement(cltLast.getClassement()+1);
		//== si les deux joueurs sont sur la même ligne :
		if(cltLast.getNumJoueur()==cltLast_1.getNumJoueur())
		{
			int coef = cltLast.getNumPoule()-cltLast_1.getNumPoule();
			//== de la gauche vers la droite
			if(coef==1)
			{
				//== on est en fin de poule
				if(cltLast.getNumPoule()==poules.size())
				{
					clt.setNumPoule(cltLast.getNumPoule());
					clt.setNumJoueur(cltLast.getNumJoueur()+1);					
				}
				else
				{
					clt.setNumPoule(cltLast.getNumPoule()+1);
					clt.setNumJoueur(cltLast.getNumJoueur());					
				}
			}
			//== de la droite vers la gauche
			else
			{
				//== on est en début de poule
				if(cltLast.getNumPoule()==1)
				{
					clt.setNumPoule(cltLast.getNumPoule());
					clt.setNumJoueur(cltLast.getNumJoueur()+1);					
				}
				else
				{
					clt.setNumPoule(cltLast.getNumPoule()-1);
					clt.setNumJoueur(cltLast.getNumJoueur());					
				}
				
			}
		}
		else
		{
			if(cltLast.getNumPoule()==1)
			{
				clt.setNumPoule(cltLast.getNumPoule()+1);
				clt.setNumJoueur(cltLast.getNumJoueur());
			}
			else
			{
				clt.setNumPoule(cltLast.getNumPoule()-1);
				clt.setNumJoueur(cltLast.getNumJoueur());				
			}
		}
		return clt;
	}

	private int getNumNextPoule()
	{
		return getNextClassement().getNumPoule();
	}


	public void setJoueurs(ArrayList joueurs)
	{
		this.joueurs = joueurs;
		//== réinitialistaion des poules
		for (Iterator iter = poules.iterator(); iter.hasNext();)
		{
			Poule poule = (Poule) iter.next();
			poule.getJoueurs().clear();
		}
		int numPoule = 0;
		int coef = 1;
		int indexLastPoule = poules.size() - 1;
		// on applique la méthode du serpentins
		for (int i=0; i<joueurs.size(); i++)
		{
			Joueur joueur = (Joueur) joueurs.get(i);
			Poule poule = ((Poule) poules.get(numPoule));
			poule.addJoueur(joueur);
			ClassementJoueurs cltJoueur = new ClassementJoueurs();
			cltJoueur.setNumPoule(numPoule+1);
			cltJoueur.setNumJoueur(poule.getJoueurs().size());
			cltJoueur.setClassement(i+1);
			cltJoueursInPoule.add(cltJoueur);
			
			//== si dernière ligne ==
			if (nbJoueursParPoule % 2 != 0 && numPoule == 0 && poule.getJoueurs().size() == nbJoueursParPoule - 1)
			{
				numPoule = poules.size() - 1;
				coef = 0;
			}
			else
			{
				if (numPoule == indexLastPoule && coef == 0)
				{
					coef = -1;
				}

				if (numPoule == indexLastPoule && coef == 1)
				{
					coef = 0;
				}

				if (numPoule == 0 && coef == 0 && poules.size() != 1)
				{
					coef = 1;
				}
				if (numPoule == 0 && coef == -1)
				{
					coef = 0;
				}
			}
			numPoule += coef;
		}
	}

	/* 
	 * récupère tous les joueurs du tableau
	 */
	public ArrayList getJoueurs()
	{
		return joueurs;
	}

	/* (non-Javadoc)
	 * @see tournoi.Tableau#getMax_joueurs()
	 */
	public int getMax_joueurs()
	{
		return max_joueurs;
	}

	/* (non-Javadoc)
	 * @see tournoi.Tableau#getMin_joueurs()
	 */
	public int getMin_joueurs()
	{
		return min_joueurs;
	}

	/* (non-Javadoc)
	 * @see tournoi.Tableau#setName(java.lang.String)
	 */
	public void setName(String name)
	{
		this.name = name;

	}

	/* (non-Javadoc)
	 * @see tournoi.Tableau#setMax_joueurs(int)
	 */
	public void setMax_joueurs(int i)
	{
		max_joueurs = i;

	}

	/* (non-Javadoc)
	 * @see tournoi.Tableau#setMin_joueurs(int)
	 */
	public void setMin_joueurs(int i)
	{
		min_joueurs = i;

	}

	public int getNbJoueursParPoule()
	{
		return nbJoueursParPoule;
	}

	public void setNbJoueursParPoule(int i)
	{
		nbJoueursParPoule = i;
	}

	public ArrayList getClassementJoueurs()
	{
		return classementJoueurs;
	}

	public ArrayList getDescriptions()
	{
		return descriptions;
	}

	public void setClassementJoueurs(ArrayList list)
	{
		classementJoueurs = list;
	}

	public void setDescriptions(ArrayList list)
	{
		descriptions = list;
	}

	public void setPoules(ArrayList list)
	{
		poules = list;
	}

	public void setTableaux(ArrayList list)
	{
		tableaux = list;
	}

	public boolean isStarted()
	{
		return started;
	}

	public void setStarted(boolean b)
	{
		started = b;
	}

	public ArrayList getCltJoueursInPoule()
	{
		return cltJoueursInPoule;
	}

	public void setCltJoueursInPoule(ArrayList list)
	{
		cltJoueursInPoule = list;
	}

}
