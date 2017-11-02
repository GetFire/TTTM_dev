package tournoi;

import java.io.*;
import java.util.*;


/**
 *  Charge une liste de club.
 */
public class Club
{
	private Vector vClub = new Vector();
	private final static String FILE_NAME = "listeClub.txt";

	/**
	 *  Gets the clubList attribute of the Club object
	 *
	 * @return    The clubList value
	 */
	public Vector getClubList()
	{
		if (vClub.isEmpty())
		{
			// lecture du fichier contenant tout les clubs
			try
			{
				//vClub.add("");
				String path = System.getProperty("lax.root.install.dir");
				if(path==null) path = "";
				else path=path+"/";
			FileInputStream fis = new FileInputStream(path+FILE_NAME);
			BufferedReader br = new BufferedReader(new InputStreamReader(fis));
			String str = "";
				while ((str = br.readLine()) != null)
				{
					vClub.add(str);
				}
				fis.close();
				br.close();
			}
			catch (FileNotFoundException e)
			{
			}
			catch (Exception e)
			{
				// affiche un message en cas d' erreur
				throw new RuntimeException(e.getMessage());
			}
		}
		// on trie la liste des clubs
		Collections.sort(vClub);
		return vClub;
	}

	public void addClub(String club)
	{
		vClub.add(club);
		Collections.sort(vClub);
		saveToFile();
	}

	/**  Description of the Method */
	public void saveToFile()
	{
		if (!vClub.isEmpty())
		{
			// lecture du fichier contenant tout les clubs
			try
			{
				String path = System.getProperty("lax.root.install.dir");
				if(path==null) path = "";
				else path=path+"/";
				File file = new File(path+FILE_NAME);
				if(file.exists())
				{
					file.delete();					
					//file.createNewFile();
				}
				
			FileWriter fw = new FileWriter(file, true);
			String strToWrite = "";
				for (int i = 0; i < vClub.size(); i++)
				{
					strToWrite = (String)vClub.get(i);
					if(i==vClub.size()-1)
					{
						fw.write(strToWrite);
					}
					else
					{
						fw.write(strToWrite+"\n");
					}
					
				}
				fw.close();
			}
			catch (Exception e)
			{
				// affiche un message en cas d' erreur
				throw new RuntimeException(e.getMessage());
			}
		}
	}

}

