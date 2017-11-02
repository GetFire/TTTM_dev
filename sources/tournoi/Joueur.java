package tournoi;


/**
 *  Modélisation d'un joueur participant au tournoi
 */
public class Joueur implements Comparable
{
	public static final Joueur EMPTY_JOUEUR = new Joueur("", "", true, new Categorie(Categorie.SENIOR));
	
	private int age;
	/**  prénom du joueur */
	private String prenom;
	/**  nom du joueur */
	private String nom;
	/**  le joueur est un homme */
	private boolean masculin;
	/**  categorie du joueur (junior, minime etc...) */
	private Categorie categorie;
	/**  Classement du joueur */
	private Classement classement = null;
	/**  le joueur est il licencié ? */
	private boolean licencie = false;
	/**  le club du joueur */
	private String club = "";
	private boolean here = false;
	
	private boolean participationIsUpdated = false;
	
	private int nbParticipation = 0;
	
	private int dossard = 0;
	
	public Joueur()
	{
		masculin = true;
	}

	
	
	/**
	 *  Constructor for the Joueur object
	 *
	 * @param  nom        nom du joueur
	 * @param  prenom     prénom du joueur
	 * @param  masculin   est-ce un joueur ou une joueuse ?
	 * @param  categorie  Junior, minime etc...
	 */
	public Joueur(String nom, String prenom, boolean masculin, Categorie categorie)
	{
		this();
		this.nom = nom;
		this.prenom = prenom;
		this.masculin = masculin;		
		this.categorie = categorie;
		this.licencie = false;
	}

	/**
	 *  Constructor for the Joueur object
	 *
	 * @param  nom         nom du joueur
	 * @param  prenom      prenom du joueur
	 * @param  masculin    sexe du joueur (masculin ou féminin)
	 * @param  categorie   Junior, minime etc...
	 * @param  classement  classement du joueur
	 */
	public Joueur(String nom, String prenom, boolean masculin, Categorie categorie, Classement classement)
	{
		this();
		this.nom = nom;
		this.prenom = prenom;
		this.masculin = masculin;
		this.categorie = categorie;
		this.classement = classement;
		licencie = (classement != null);
	}

	/**
	 *  Constructor for the Joueur object
	 *
	 * @param  nom         nom du joueur
	 * @param  prenom      prénom du joueur
	 * @param  masculin    sexe du joueur (masculin ou féminin)
	 * @param  categorie   Junior, minime etc...
	 * @param  classement  classement du joueur
	 * @param  licencie    le joueur est il licencié ?
	 */
	public Joueur(		
		String nom,
		String prenom,
		boolean masculin,
		Categorie categorie,
		Classement classement,
		boolean licencie)
	{		
		this(nom, prenom, masculin, categorie, classement);
		this.licencie = licencie;
	}

	/**
	 *  Constructor for the Joueur object
	 *
	 * @param  nom         nom du joueur
	 * @param  prenom      prénom du joueur
	 * @param  masculin    sexe du joueur (masculin ou féminin)
	 * @param  categorie   Junior, minime etc...
	 * @param  classement  classement du joueur
	 * @param  licencie    le joueur est il licencié
	 * @param  Club        le club du joueur
	 */
	public Joueur(
		String nom,
		String prenom,
		boolean masculin,
		Categorie categorie,
		Classement classement,
		boolean licencie,
		String club)
	{
		this(nom, prenom, masculin, categorie, classement, licencie);
		this.club = club;
	}

	/**
	 *  Sets the club attribute of the Joueur object
	 *
	 * @param  club  The new club value
	 */
	public void setClub(String club)
	{
		this.club = club;
	}

	public int getNotationClassement()
	{
		if(!isLicencie()) return -1;
		//if(!isClasse()) return 0;
		return classement.getNewClassement();
	}

	/**
	 *  Gets the club attribute of the Joueur object
	 *
	 * @return    The club value
	 */
	public String getClub()
	{
		return club;
	}

	/**
	 *  permet de dire si deux joueurs sont identiques
	 *
	 * @param  o  le joueur qui doit être comparé
	 * @return    return vrai si le nom et le prenom des deux joueurs sont
	 *      identiques
	 */
	public boolean equals(Object o)
	{
		if (!(o instanceof Joueur))
		{
			return false;
		}
		Joueur cmp = (Joueur) o;
		if (cmp.nom.equals(nom) && cmp.prenom.equals(prenom))
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	/**
	 *  Gets the licencie attribute of the Joueur object
	 *
	 * @return    The licencie value
	 */
	public boolean isLicencie()
	{
		return licencie;
	}

	/**
	 *  Permet de savoir si un joueur est classé ou non
	 *
	 * @return    vrai si le joueur est classé
	 */
	public boolean isClasse()
	{
		if (this.classement == null)
		{
			return false;
		}		
		return classement.isClasse();
	}

	/**
	 *  Gets the classement attribute of the Joueur object
	 *
	 * @return    The classement value
	 */
	public Classement getClassement()
	{
		return classement;
	}

	/**
	 *  Description of the Method
	 *
	 * @param  o  Description of the Parameter
	 * @return    Description of the Return Value
	 */
	public int compareTo(Object o)
	{
		Joueur j = (Joueur) o;
		// pour les joueurs non classés et non licenciés
		if (classement == null)
		{
			if (j.classement != null)
			{
				return -1;
			}
			if (isLicencie())
			{
				return (j.isLicencie()) ? 0 : 1;
			}
			return (j.isLicencie()) ? -1 : 0;
		}
		return (j.classement == null) ? 1 : classement.compareTo(j.classement);
	}

	/**
	 *  Description of the Method
	 *
	 * @return    Description of the Return Value
	 */
	public String toString()
	{
		return nom + " " + prenom + " (" + getStrOldClassement() + ")";
	}
	public Categorie getCategorie()
	{
		return categorie;
	}


	public String getStrClassement()
	{
		if (classement == null)
		{
			return licencie ? "NC" : "NL";
		}
		else
		{
			return classement.toString();
		}		
	}

	public String getStrOldClassement()
	{
		if (classement == null)
		{
			return licencie ? "NC" : "NL";
		}
		else
		{
			return classement.getStrOldClassement();
		}		
	}


	public String getNom()
	{
		return nom;
	}

	public String getPrenom()
	{
		return prenom;
	}

	public void setCategorie(Categorie categorie)
	{
		this.categorie = categorie;
	}

	public void setLicencie(boolean b)
	{
		licencie = b;
	}

	public void setMasculin(boolean masculin)
	{
		this.masculin = masculin;
	}

	public void setNom(String string)
	{
		nom = string;
	}

	public void setPrenom(String string)
	{
		prenom = string;
	}

	public boolean isMasculin()
	{
		return masculin;
	}
	

	public int getAge()
	{
		return age;
	}

	public void setAge(int i)
	{
		age = i;
	}

	public void setClassement(Classement classement)
	{
		this.classement = classement;
	}

	public boolean isHere()
	{
		return here;
	}

	public void setHere(boolean b)
	{
		here = b;
	}

	public int getNbParticipation()
	{
		return nbParticipation;
	}

	public void setNbParticipation(int i)
	{
		nbParticipation = i;
	}
	
	public void incParticipation()
	{
		if(!participationIsUpdated)
		{
			participationIsUpdated = true;
			nbParticipation += 1;
		}				
	}
	
	public void decParticipation()
	{
		if(participationIsUpdated)
		{
			participationIsUpdated = false;
			nbParticipation -= 1;
		}				
	}
	

	public int getDossard()
	{
		return dossard;
	}

	public void setDossard(int i)
	{
		dossard = i;
	}
    
}
