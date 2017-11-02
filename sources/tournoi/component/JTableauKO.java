/*
 * Created on 27 févr. 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package tournoi.component;

import java.awt.*;

import org.apache.log4j.Logger;

import tournoi.*;

/**
 * Représentation graphique d'un tableau KO.
 */
public class JTableauKO extends JTableau
{
	/**
     * 
     */
    private static final long serialVersionUID = 1L;
    private static final int DECALAGE_HEIGHT = 5;
	private static final int DECALAGE_WIDTH = 10;
	private Dimension sizeCalculate;
	Logger logger = Logger.getLogger(JTableauKO.class);
	
	/** racine du tableau */
	private JNode racine;
	
	/**
	 * permet de récupérer la "racine" du tableau.
	 * @return la racine du tableau
	 */
	private Node getRacine()
	{
		logger.debug("tours="+tableau.getTours().size());
		logger.debug("nodes="+((Tour)tableau.getTours().get(0)).getNodes().size());
		
		Node temp = (Node)((Tour)tableau.getTours().get(0)).getNodes().get(0);
		while(temp.getWinNode()!=null)
		{
			temp = temp.getWinNode();
		}
		return temp;
	}
	
	/**
	 * créer l'arborscence à partie du noeud racine
	 * @param racine le noeud à la racine de l'arborescence
	 * @return le JNode à la racine de l'arbre.
	 */
	private JNode constructJNodeArbre(Node racine)
	{
		JNode jNode;
		if(racine.getChild1()==null && racine.getChild2()==null)
		{
			jNode = new JNode(racine, null, null);			
			addJNode(jNode);			
			return jNode;
		}	
		jNode = new JNode(racine, constructJNodeArbre(racine.getChild1()),constructJNodeArbre(racine.getChild2()));		
		addJNode(jNode);		
		return jNode;		
	}
	
	private void createJNodeComponent()
	{
		racine = constructJNodeArbre(getRacine());		
	}
	 
	
	public JTableauKO(TableauKO tableau)
	{
		super(tableau);
		createJNodeComponent();
		int height = racine.getHeight();		
		int nbHeight = ((Tour)tableau.getTours().get(0)).getNodes().size(); 
		height = (height*nbHeight)+(DECALAGE_HEIGHT*nbHeight-1);		
		int width = racine.getWidth();		
		int nbWidth = tableau.getTours().size(); 
		width = (width*nbWidth)+(DECALAGE_WIDTH*nbWidth-1);
		sizeCalculate = new Dimension(width,height);				
		setSize(sizeCalculate);		
	}
	
	
	
	/**
	 *  Gets the preferredSize attribute of the IconDisplayer object
	 *
	 *@return    The preferredSize value
	 */
	public Dimension getPreferredSize()
	{
		return sizeCalculate;
	}


	/**
	 *  Gets the minimumSize attribute of the IconDisplayer object
	 *
	 *@return    The minimumSize value
	 */
	public Dimension getMinimumSize()
	{
			return sizeCalculate;
	}		
	
	private void paintJNode(Graphics g,JNode node, Point point,int nbNode)
	{
		int decalageX = ((getWidth()/(tableau.getTours().size()))-node.getWidth())/2;
		if(node.getJNodeChild1()==null && node.getJNodeChild2()==null)
		{
			
			node.setLocation(point.x,point.y);			
		}
		else
		{
			int decalageY = ((getHeight()/(nbNode*2))-node.getHeight())/2;
			
			paintJNode(g,node.getJNodeChild1(), new Point(point.x-decalageX*2-racine.getWidth(),point.y-decalageY-(node.getHeight()/2)),nbNode*2);
			paintJNode(g,node.getJNodeChild2(), new Point(point.x-decalageX*2-racine.getWidth(),point.y+decalageY+(node.getHeight()/2)),nbNode*2);
			node.setLocation(point);
			drawLinkedNodes(g, node.getJNodeChild2(), node);
			drawLinkedNodes(g, node.getJNodeChild1(), node);
		}
	}
	
	
	protected void paintComponent(Graphics g)
	{	
		//super.paintComponent(g)		
		if(isOpaque())
		{
			g.setColor(getBackground());
			g.fillRect(0, 0, getWidth(), getHeight());
		}
		if(racine!=null)
		{
			Graphics g2 = g.create();
			int decalageX = ((getWidth()/(tableau.getTours().size()))-racine.getWidth())/2;
			paintJNode(g2,racine,new Point(getWidth()-racine.getWidth()-decalageX,(getHeight()/2)-(racine.getHeight()/2)),1);
			g2.dispose();
			repaintJNodes();			
			
		}	
		else
		{
			g.dispose();	
		}		
	}
}
