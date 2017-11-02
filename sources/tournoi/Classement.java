package tournoi;

import java.util.*;

/**
 *  Permet de représenter le classement d'un joueur
 */
public class Classement implements Comparable
{
	/**  pour les joueurs numérotés français */
	private int numero = 0;
	/**  indique si un joueur est numéroté */
	private boolean isNumerote = false;
	/**  ancienne façon de calculer les classements */
	private int oldClassement = 0;
	/**  nouvelle façon de calculer les classements */
	private int newClassement = 0;
	/**  c'est un classement féminin ou masculin ? */
	private boolean cltMasculin = true;
	/**  ecart entre les classement masculin et féminin */
	private final static int ECART_CLASSEMENT = 350;

	private static Vector vOldClassements = new Vector();

	private final static int tOldClassement[] = {0, 90, 85, 80, 75, 70, 65, 60, 55, 50, 45, 40, 35, 30, 25};

	/**
	 *  Gets the oldClassementList attribute of the Classement class
	 *
	 * @return    The oldClassementList value
	 */
	public static Vector getOldClassementList()
	{
		if (vOldClassements.isEmpty())
		{
			for (int x = 0; x < tOldClassement.length; x++)
			{
				vOldClassements.add(new Integer(tOldClassement[x]));
			}
		}		
		return vOldClassements;
	}

	public Classement()
	{
	}
	
	/**
	 *  Constructor for the Classement object
	 *
	 * @param  numero       pour les numéroté(e)s français(e)s
	 * @param  cltMasculin  Description of the Parameter
	 */
	public Classement(int numero, boolean cltMasculin)
	{
		this.cltMasculin = cltMasculin;
		setNumero(numero);
	}

	/**
	 *  Constructor for the Classement object
	 *
	 * @param  oldClassement  ancien "valeur" du classement. Par exemple 40
	 * @param  newClassement  nouvel "valeur" du classement. Par exemple 1658
	 * @param  cltMasculin    vrai pour joueur, faux pour joueuse
	 */
	public Classement(int oldClassement, int newClassement, boolean cltMasculin)
	{
		this.cltMasculin = cltMasculin;
		if (oldClassement < 0 && newClassement <= 0)
		{
			throw new RuntimeException("invalid classement");
		}
		if (newClassement > 0)
		{
			setNewClassement(newClassement);
		}
		else
		{
			setOldClassement(oldClassement);
		}

	}

	/**
	 *  Gets the oldClassement attribute of the Classement object
	 *
	 * @return    The oldClassement value
	 */
	public int getOldClassement()
	{
		return oldClassement;
	}

	/**
	 *  Gets the newClassement attribute of the Classement object
	 *
	 * @return    The newClassement value
	 */
	public int getNewClassement()
	{
		return newClassement;
	}

	/**
	 *  Sets the newClassement attribute of the Classement object
	 *
	 * @param  newClassement  The new newClassement value
	 */
	public void setNewClassement(int newClassement)
	{
		this.oldClassement = getOldClassement(newClassement);
		this.newClassement = newClassement;
		numero = 0;
		isNumerote = false;
	}

	/**
	 *  met à jour le classement d'un numéro français
	 *
	 * @param  numero  Description of the Parameter
	 */
	public void setNumero(int numero)
	{
		isNumerote = true;
		this.numero = numero;
		oldClassement = 0;
		newClassement = 0;
	}

	/**
	 *  Gets the numero attribute of the Classement object
	 *
	 * @return    The numero value
	 */
	public int getNumero()
	{
		return numero;
	}

	/**
	 *  met à jour l'"ancien" classement d'un joueur
	 *
	 * @param  oldClassement  classement : ancienne façon
	 */
	public void setOldClassement(int oldClassement)
	{
		boolean isValide = false; 
		for (int i = 0; i < tOldClassement.length; i++)
		{
			if(tOldClassement[i]==oldClassement)
			{
				isValide = true;
				break;
			}
		}
		if(!isValide)
		{
			throw new RuntimeException("invalid classement");
		}
		this.newClassement = getNewClassement(oldClassement);
		numero = 0;
		isNumerote = false;
		this.oldClassement = oldClassement;
	}

	public boolean isClasse()
	{
		
		return (getOldClassement() == 0&&!isNumerote()) ? false : true;
	}
	
	/**
	 *  Gets the newClassement attribute of the Classement object
	 *
	 * @param  oldClassement  Description of the Parameter
	 * @return                The newClassement value
	 */
	public int getNewClassement(int oldClassement)
	{
		switch (oldClassement)
		{
						case 0:
							return (cltMasculin) ? 650 : 650 - ECART_CLASSEMENT;
						case 90:
							return (cltMasculin) ? 670 : 670 - ECART_CLASSEMENT;
						case 85:
							return (cltMasculin) ? 690 : 690 - ECART_CLASSEMENT;
						case 80:
							return (cltMasculin) ? 750 : 750 - ECART_CLASSEMENT;
						case 75:
							return (cltMasculin) ? 850 : 850 - ECART_CLASSEMENT;
						case 70:
							return (cltMasculin) ? 950 : 950 - ECART_CLASSEMENT;
						case 65:
							return (cltMasculin) ? 1050 : 1050 - ECART_CLASSEMENT;
						case 60:
							return (cltMasculin) ? 1150 : 1150 - ECART_CLASSEMENT;
						case 55:
							return (cltMasculin) ? 1250 : 1250 - ECART_CLASSEMENT;
						case 50:
							return (cltMasculin) ? 1350 : 1350 - ECART_CLASSEMENT;
						case 45:
							return (cltMasculin) ? 1450 : 1450 - ECART_CLASSEMENT;
						case 40:
							return (cltMasculin) ? 1550 : 1550 - ECART_CLASSEMENT;
						case 35:
							return (cltMasculin) ? 1650 : 1650 - ECART_CLASSEMENT;
						case 30:
							return (cltMasculin) ? 1750 : 1750 - ECART_CLASSEMENT;
						case 25:
							return (cltMasculin) ? 1850 : 1850 - ECART_CLASSEMENT;
		}
		return 0;
	}

	/**
	 *  Gets the oldClassement attribute of the Classement object
	 *
	 * @param  newClassement  Description of the Parameter
	 * @return                The oldClassement value
	 */
	public int getOldClassement(int newClassement)
	{
	int ecart = (cltMasculin ? 0 : ECART_CLASSEMENT);
		if (newClassement >= 1800 - ecart)
		{
			return 25;
		}
		if (newClassement >= 1700 - ecart)
		{
			return 30;
		}
		if (newClassement >= 1600 - ecart)
		{
			return 35;
		}
		if (newClassement >= 1500 - ecart)
		{
			return 40;
		}
		if (newClassement >= 1400 - ecart)
		{
			return 45;
		}
		if (newClassement >= 1300 - ecart)
		{
			return 50;
		}
		if (newClassement >= 1200 - ecart)
		{
			return 55;
		}
		if (newClassement >= 1100 - ecart)
		{
			return 60;
		}
		if (newClassement >= 1000 - ecart)
		{
			return 65;
		}
		if (newClassement >= 900 - ecart)
		{
			return 70;
		}
		if (newClassement >= 800 - ecart)
		{
			return 75;
		}
		if (newClassement >= 700 - ecart)
		{
			return 80;
		}
		if (newClassement >= 680 - ecart)
		{
			return 85;
		}
		if (newClassement >= 660 - ecart)
		{
			return 90;
		}
		return 0;
	}

	/**
	 *  Gets the numero attribute of the Classement object
	 *
	 * @return    The numero value
	 */
	public boolean isNumerote()
	{
		return isNumerote;
	}

	/**
	 *  Description of the Method
	 *
	 * @param  o  Description of the Parameter
	 * @return    Description of the Return Value
	 */
	public int compareTo(Object o)
	{
		Classement c = (Classement)o;
		if (isNumerote)
		{
			if (c.isNumerote())
			{
				if (numero > c.getNumero())
				{
					return -1;
				}
				else if (numero == c.getNumero())
				{
					return 0;
				}
				else
				{
					return 1;
				}
			}
			return 1;
		}
		if (c.isNumerote())
		{
			return -1;
		}
		if (newClassement > c.getNewClassement())
		{
			return 1;
		}
		if (newClassement == c.getNewClassement())
		{
			return 0;
		}
		if (newClassement < c.getNewClassement())
		{
			return -1;
		}		
		return 0;
	}

	public String getStrOldClassement()
	{
		if(isNumerote)
		{
			return "N° " + Integer.toString(getNumero());
		}
		if(getOldClassement()==0) return "NC";
		return Integer.toString(getOldClassement());		
	}
	
	/**
	 *  Description of the Method
	 *
	 * @return    Description of the Return Value
	 */
	public String toString()
	{
		if(isNumerote)
		{
			return "N° " + Integer.toString(getNumero());
		}
		return Integer.toString(getNewClassement());
	}

	public boolean isCltMasculin()
	{
		return cltMasculin;
	}

	public void setCltMasculin(boolean b)
	{
		cltMasculin = b;
	}

	public void setNumero(boolean b)
	{
		isNumerote = b;
	}

}

