/*
 *
 */
package tournoi.forms;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JInternalFrame;

/**
 *
 */
public class FormFormuleSimple extends JInternalFrame
{

	/**
     * 
     */
    private static final long serialVersionUID = 1L;

    private javax.swing.JPanel jContentPane = null;

	private javax.swing.JLabel lblFormules = null;
	private javax.swing.JList jList = null;
	private javax.swing.JLabel lblNom = null;
	private javax.swing.JTextField tfNom = null;
	private javax.swing.JLabel lblNbPoules = null;
	private javax.swing.JTextField tfMax = null;
	private javax.swing.JLabel lblMin = null;
	private javax.swing.JTextField tfMin = null;
	private javax.swing.JLabel lblVisible = null;
	private javax.swing.JTextField tfVisible = null;
	private javax.swing.JTable tableOrdreMatchs = null;
	private javax.swing.JLabel jLabel = null;
	private javax.swing.JPanel jPanel = null;
	private javax.swing.JButton bAjouter = null;
	private javax.swing.JButton bModifier = null;
	private javax.swing.JButton bSupprimer = null;
	
	private javax.swing.JButton bAjouterLigne = null;
	/**
	 * This is the default constructor
	 */
	public FormFormuleSimple()
	{
		super();
		initialize();
	}
	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize()
	{
		this.setSize(717, 447);
		this.setContentPane(getJContentPane());
	}
	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private javax.swing.JPanel getJContentPane()
	{
		if (jContentPane == null)
		{
			jContentPane = new javax.swing.JPanel();
			java.awt.GridBagConstraints consGridBagConstraints00 = new java.awt.GridBagConstraints();
			java.awt.GridBagConstraints consGridBagConstraints10 = new java.awt.GridBagConstraints();			
			java.awt.GridBagConstraints consGridBagConstraints20 = new java.awt.GridBagConstraints();
			java.awt.GridBagConstraints consGridBagConstraints22 = new java.awt.GridBagConstraints();			
			java.awt.GridBagConstraints consGridBagConstraints40 = new java.awt.GridBagConstraints();			
			java.awt.GridBagConstraints consGridBagConstraints62 = new java.awt.GridBagConstraints();
			
			java.awt.GridBagConstraints consGridBagConstraints41 = new java.awt.GridBagConstraints();
			java.awt.GridBagConstraints consGridBagConstraints51 = new java.awt.GridBagConstraints();
			java.awt.GridBagConstraints consGridBagConstraints6 = new java.awt.GridBagConstraints();
			java.awt.GridBagConstraints consGridBagConstraints71 = new java.awt.GridBagConstraints();
			java.awt.GridBagConstraints consGridBagConstraints31 = new java.awt.GridBagConstraints();
			java.awt.GridBagConstraints consGridBagConstraints12 = new java.awt.GridBagConstraints();
			java.awt.GridBagConstraints consGridBagConstraints111 = new java.awt.GridBagConstraints();
			java.awt.GridBagConstraints consGridBagConstraints1 = new java.awt.GridBagConstraints();
			consGridBagConstraints1.gridx = 0;
			consGridBagConstraints1.gridy = 10;
			consGridBagConstraints1.insets = new java.awt.Insets(2,2,2,2);
			consGridBagConstraints1.gridwidth = 3;
			consGridBagConstraints12.gridx = 0;
			consGridBagConstraints12.gridy = 7;
			consGridBagConstraints12.gridwidth = 3;
			consGridBagConstraints111.insets = new java.awt.Insets(2,2,2,2);
			consGridBagConstraints111.gridx = 0;
			consGridBagConstraints111.gridy = 8;
			consGridBagConstraints111.gridwidth = 3;
			consGridBagConstraints111.anchor = java.awt.GridBagConstraints.WEST;
			consGridBagConstraints51.gridx = 0;
			consGridBagConstraints51.gridy = 5;
			consGridBagConstraints51.insets = new java.awt.Insets(2,2,2,2);
			consGridBagConstraints51.gridwidth = 2;
			consGridBagConstraints51.anchor = java.awt.GridBagConstraints.EAST;
			consGridBagConstraints51.weightx = 0.0D;
			consGridBagConstraints41.weightx = 1.0;
			consGridBagConstraints41.fill = java.awt.GridBagConstraints.HORIZONTAL;
			consGridBagConstraints41.anchor = java.awt.GridBagConstraints.WEST;
			consGridBagConstraints41.gridx = 2;
			consGridBagConstraints41.gridy = 6;
			consGridBagConstraints41.insets = new java.awt.Insets(2,2,2,2);
			consGridBagConstraints6.insets = new java.awt.Insets(2,2,2,2);
			consGridBagConstraints6.gridx = 0;
			consGridBagConstraints6.gridy = 6;
			consGridBagConstraints6.anchor = java.awt.GridBagConstraints.EAST;
			consGridBagConstraints6.fill = java.awt.GridBagConstraints.NONE;
			consGridBagConstraints6.gridwidth = 2;
			consGridBagConstraints62.weightx = 1.0;
			consGridBagConstraints62.fill = java.awt.GridBagConstraints.HORIZONTAL;
			consGridBagConstraints62.gridx = 2;
			consGridBagConstraints62.insets = new java.awt.Insets(2,2,2,2);
			consGridBagConstraints71.weightx = 1.0;
			consGridBagConstraints71.weighty = 1.0;
			consGridBagConstraints71.fill = java.awt.GridBagConstraints.BOTH;
			consGridBagConstraints31.weightx = 1.0;
			consGridBagConstraints31.fill = java.awt.GridBagConstraints.HORIZONTAL;
			consGridBagConstraints31.gridy = 5;
			consGridBagConstraints31.gridx = 2;
			consGridBagConstraints31.insets = new java.awt.Insets(2,2,2,2);
			consGridBagConstraints40.gridwidth = 2;
			consGridBagConstraints40.gridx = 0;
			consGridBagConstraints40.gridy = 4;
			consGridBagConstraints40.anchor = java.awt.GridBagConstraints.EAST;
			consGridBagConstraints40.insets = new java.awt.Insets(2,2,2,2);
			consGridBagConstraints40.weightx = 2.0D;
			consGridBagConstraints20.gridx = 0;
			consGridBagConstraints20.gridy = 2;
			consGridBagConstraints20.fill = java.awt.GridBagConstraints.NONE;
			consGridBagConstraints20.anchor = java.awt.GridBagConstraints.NORTHEAST;
			consGridBagConstraints20.insets = new java.awt.Insets(2,2,2,2);
			consGridBagConstraints20.weightx = 1.0D;
			consGridBagConstraints20.gridwidth = 2;
			consGridBagConstraints10.weightx = 1.0;
			consGridBagConstraints10.weighty = 1.0;
			consGridBagConstraints10.fill = java.awt.GridBagConstraints.BOTH;
			consGridBagConstraints10.gridx = 0;
			consGridBagConstraints10.gridy = 1;
			consGridBagConstraints10.insets = new java.awt.Insets(2,2,2,2);
			consGridBagConstraints10.gridwidth = 3;
			consGridBagConstraints22.weightx = 1.0;
			consGridBagConstraints22.fill = java.awt.GridBagConstraints.HORIZONTAL;
			consGridBagConstraints22.anchor = java.awt.GridBagConstraints.SOUTHWEST;
			consGridBagConstraints22.gridx = 2;
			consGridBagConstraints22.gridy = 2;
			consGridBagConstraints71.insets = new java.awt.Insets(2,2,2,2);
			consGridBagConstraints71.gridx = 0;
			consGridBagConstraints71.gridy = 9;
			consGridBagConstraints71.gridwidth = 3;
			consGridBagConstraints00.gridx = 0;
			consGridBagConstraints00.gridy = 0;
			consGridBagConstraints00.insets = new java.awt.Insets(2,2,2,2);
			consGridBagConstraints00.fill = java.awt.GridBagConstraints.HORIZONTAL;
			consGridBagConstraints00.gridwidth = 3;
			consGridBagConstraints00.anchor = java.awt.GridBagConstraints.NORTHEAST;
			consGridBagConstraints62.gridy = 4;
			jContentPane.setLayout(new java.awt.GridBagLayout());
			jContentPane.add(getJLabel(), consGridBagConstraints00);
			jContentPane.add(getJList(), consGridBagConstraints10);
			jContentPane.add(getJLabel1(), consGridBagConstraints20);
			jContentPane.add(getTfNom(), consGridBagConstraints22);
			jContentPane.add(getLblNbPoules(), consGridBagConstraints40);
			jContentPane.add(getTfMax(), consGridBagConstraints62);
			jContentPane.add(getLblMin(), consGridBagConstraints51);
			jContentPane.add(getTfMin(), consGridBagConstraints31);
			jContentPane.add(getLblVisible(), consGridBagConstraints6);
			jContentPane.add(getTfVisible(), consGridBagConstraints41);
			jContentPane.add(getTableOrdreMatchs(), consGridBagConstraints71);
			jContentPane.add(getJLabel3(), consGridBagConstraints111);
			jContentPane.add(getJPanel(), consGridBagConstraints12);
			jContentPane.add(getBAjouterLigne(), consGridBagConstraints1);
		}
		return jContentPane;
	}
	/**
	 * This method initializes jLabel
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getJLabel() {
		if(lblFormules == null) {
			lblFormules = new javax.swing.JLabel();
			lblFormules.setText("Liste des formules :");
			lblFormules.setVerticalAlignment(javax.swing.SwingConstants.TOP);
			lblFormules.setName("");
			lblFormules.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
			lblFormules.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
		}
		return lblFormules;
	}
	/**
	 * This method initializes jList
	 * 
	 * @return javax.swing.JList
	 */
	private javax.swing.JList getJList() {
		if(jList == null) {
			jList = new javax.swing.JList();
			jList.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
			jList.setToolTipText("Liste des formules disponibles");
			jList.setVisibleRowCount(10);
		}
		return jList;
	}
	/**
	 * This method initializes jLabel1
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getJLabel1() {
		if(lblNom == null) {
			lblNom = new javax.swing.JLabel();
			lblNom.setText("Nom de la formule :");
		}
		return lblNom;
	}
	/**
	 * This method initializes tfNom
	 * 
	 * @return javax.swing.JTextField
	 */
	private javax.swing.JTextField getTfNom() {
		if(tfNom == null) {
			tfNom = new javax.swing.JTextField();
		}
		return tfNom;
	}
	/**
	 * This method initializes lblNbPoules
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getLblNbPoules() {
		if(lblNbPoules == null) {
			lblNbPoules = new javax.swing.JLabel();
			lblNbPoules.setText("Nombre de poules :");
		}
		return lblNbPoules;
	}
	/**
	 * This method initializes tfMax
	 * 
	 * @return javax.swing.JTextField
	 */
	private javax.swing.JTextField getTfMax() {
		if(tfMax == null) {
			tfMax = new javax.swing.JTextField();
		}
		return tfMax;
	}
	/**
	 * This method initializes lblMin
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getLblMin() {
		if(lblMin == null) {
			lblMin = new javax.swing.JLabel();
			lblMin.setText("Nombre de joueurs minimum :");
		}
		return lblMin;
	}
	/**
	 * This method initializes tfMin
	 * 
	 * @return javax.swing.JTextField
	 */
	private javax.swing.JTextField getTfMin() {
		if(tfMin == null) {
			tfMin = new javax.swing.JTextField();
		}
		return tfMin;
	}
	/**
	 * This method initializes lblVisible
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getLblVisible() {
		if(lblVisible == null) {
			lblVisible = new javax.swing.JLabel();
			lblVisible.setText("Visible :");
		}
		return lblVisible;
	}
	/**
	 * This method initializes tfVisible
	 * 
	 * @return javax.swing.JTextField
	 */
	private javax.swing.JTextField getTfVisible() {
		if(tfVisible == null) {
			tfVisible = new javax.swing.JTextField();
		}
		return tfVisible;
	}
	/**
	 * This method initializes tableOrdreMatchs
	 * 
	 * @return javax.swing.JTable
	 */
	private javax.swing.JTable getTableOrdreMatchs() {
		if(tableOrdreMatchs == null) {
			tableOrdreMatchs = new javax.swing.JTable();
		}
		return tableOrdreMatchs;
	}
	/**
	 * This method initializes jLabel
	 * 
	 * @return javax.swing.JLabel
	 */
	private javax.swing.JLabel getJLabel3() {
		if(jLabel == null) {
			jLabel = new javax.swing.JLabel();
			jLabel.setText("Ordre des matchs :");
		}
		return jLabel;
	}
	/**
	 * This method initializes jPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private javax.swing.JPanel getJPanel() {
		if(jPanel == null) {
			jPanel = new javax.swing.JPanel();
			java.awt.GridLayout layGridLayout13 = new java.awt.GridLayout();
			layGridLayout13.setRows(1);
			layGridLayout13.setColumns(3);
			jPanel.setLayout(layGridLayout13);
			jPanel.add(getbAjouter());
			jPanel.add(getbModifier());
			jPanel.add(getbSupprimer());
		}
		return jPanel;
	}
	
	private javax.swing.JButton getbAjouter()
	{
		if(bAjouter==null)
		{
			bAjouter = new javax.swing.JButton();
			bAjouter.setText("Ajouter");
		}
		return bAjouter;
	}

	private javax.swing.JButton getbModifier()
	{
		if(bModifier==null)
		{
			bModifier = new javax.swing.JButton();
			bModifier.setText("Modifier");
		}
		return bModifier;
	}

	private javax.swing.JButton getbSupprimer()
	{
		if(bSupprimer==null)
		{
			bSupprimer = new javax.swing.JButton();
			bSupprimer.setText("Supprimer");
		}
		return bSupprimer;
	}

	
	/**
	 * This method initializes bAjouterLigne
	 * 
	 * @return javax.swing.JButton
	 */
	private javax.swing.JButton getBAjouterLigne() {
		if(bAjouterLigne == null) {
			bAjouterLigne = new javax.swing.JButton();
			bAjouterLigne.setText("Ajouter Ligne");
			bAjouterLigne.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					ajouterLigneMatch();

				}
			});			
		}
		
		return bAjouterLigne;
	}
	
	public void ajouterLigneMatch()
	{
		
	}
	
}  //  @jve:visual-info  decl-index=0 visual-constraint="10,10"

