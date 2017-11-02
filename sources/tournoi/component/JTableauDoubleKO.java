/*
 * Created on 27 févr. 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package tournoi.component;

import java.awt.*;

import tournoi.*;

/**
 * Représentation graphique d'un tableau double KO.
 */
public class JTableauDoubleKO extends JTableau
{
	/**
     * 
     */
    private static final long serialVersionUID = 1L;
    private static final int DECALAGE_HEIGHT = 5;
	private static final int DECALAGE_WIDTH = 5;
	private Dimension sizeCalculate;
	
	/** racine du tableau */
	private JNode racineLost;
	private JNode racineWin;

	/**
	 * constructeur par défaut.
	 * @param tableau
	 */
	public JTableauDoubleKO(TableauDoubleKO tableau)
	{
		super(tableau);
		createJNodeComponent();
		int height = racineLost.getHeight();		
		int nbHeight = ((Tour)tableau.getTours().get(0)).getNodes().size(); 
		height = (height*nbHeight)+(DECALAGE_HEIGHT*nbHeight-1);		
		int width = racineLost.getWidth();		
		int nbWidth = tableau.getTours().size(); 
		width = (width*nbWidth)+(DECALAGE_WIDTH*nbWidth);
		sizeCalculate = new Dimension(width,height);				
		setSize(sizeCalculate);		
	}

	
	
	/**
	 * permet de récupérer la "racine gagnante" du tableau.
	 * @return la racine du tableau
	 */
	private Node getRacineWin()
	{
		Node temp = (Node)((Tour)tableau.getTours().get(0)).getNodes().get(0);
		while(temp.getWinNode()!=null)
		{
			temp = temp.getWinNode();
		}
		return temp;
	}
	
	/**
	 * permet de récupérer la "racine perdante" du tableau.
	 * @return la racine du tableau
	 */
	private Node getRacineLost()
	{
		Node temp = (Node)((Tour)tableau.getTours().get(0)).getNodes().get(0);
		temp=temp.getLostNode();
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
			jNode = getJNode(racine);
			if(jNode==null)
			{
				jNode = new JNode(racine, null, null);
				addJNode(jNode); 
				
			}											
			return jNode;
		}	
		jNode = new JNode(racine, constructJNodeArbre(racine.getChild1()),constructJNodeArbre(racine.getChild2()));		
		addJNode(jNode);		
		return jNode;		
	}
	
	private void createJNodeComponent()
	{
		racineLost = constructJNodeArbre(getRacineLost());
		racineWin = constructJNodeArbre(getRacineWin());		
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
	
	private void paintJNodePrevious(Graphics g,JNode node, Point point,int nbNode)
	{		
		int decalageX = ((getWidth()/(tableau.getTours().size()))-node.getWidth())/2;
		if(node.getJNodeChild1()==null && node.getJNodeChild2()==null)
		{
			
			node.setLocation(point.x,point.y);			
		}
		else
		{
			int decalageY = ((getHeight()/(nbNode*2))-node.getHeight())/2;
			
			paintJNodePrevious(g,node.getJNodeChild1(), new Point(point.x-decalageX*2-racineLost.getWidth(),point.y-decalageY-(node.getHeight()/2)),nbNode*2);
			paintJNodePrevious(g,node.getJNodeChild2(), new Point(point.x-decalageX*2-racineLost.getWidth(),point.y+decalageY+(node.getHeight()/2)),nbNode*2);
			node.setLocation(point);
			drawLinkedNodes(g, node.getJNodeChild2(), node);
			drawLinkedNodes(g, node.getJNodeChild1(), node);
		}		
	}

	private void paintJNodeNext(Graphics g,JNode node, Point point,int nbNode)
	{			
		int decalageX = ((getWidth()/(tableau.getTours().size()))-node.getWidth())/2;
			//== évite le clignotement dû au repositionnement des JNode ==
			//== attention à appeller d'abord paintJNodePrevious ==
			//node.setLocation(point.x,point.y);			
		if(node.getJNodeChild1()!=null || node.getJNodeChild2()!=null)
		{
			int decalageY = ((getHeight()/(nbNode*2))-node.getHeight())/2;
			
			paintJNodeNext(g,node.getJNodeChild1(), new Point(point.x+decalageX*2+racineLost.getWidth(),point.y-decalageY-(node.getHeight()/2)),nbNode*2);
			paintJNodeNext(g,node.getJNodeChild2(), new Point(point.x+decalageX*2+racineLost.getWidth(),point.y+decalageY+(node.getHeight()/2)),nbNode*2);
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
		if(racineLost!=null)
		{
			Graphics g2d = g.create();
			int decalageX = ((getWidth()/(tableau.getTours().size()))-racineLost.getWidth())/2;
			paintJNodePrevious(g2d,racineWin,new Point(getWidth()-racineLost.getWidth()-decalageX,(getHeight()/2)-(racineLost.getHeight()/2)),1);
			paintJNodeNext(g2d,racineLost,new Point(0+decalageX,(getHeight()/2)-(racineLost.getHeight()/2)),1);
			g2d.dispose();			
			repaintJNodes();
		}	
		else
		{
			g.dispose();
		}
	}
}
