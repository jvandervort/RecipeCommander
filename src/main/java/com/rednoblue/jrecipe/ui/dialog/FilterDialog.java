package com.rednoblue.jrecipe.ui.dialog;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class FilterDialog extends JDialog {
	private static final long serialVersionUID = 1L;

	private JButton btnApplyFilter = new JButton();
	private JButton btnCancel = new JButton();
	private JCheckBox chkComments = new JCheckBox();
	private JCheckBox chkIngredient = new JCheckBox();
	private JCheckBox chkProcess = new JCheckBox();
	private JCheckBox chkRecipeName = new JCheckBox();
	private JLabel lblSearchText = new JLabel();
	private JTextField txtFilter = new JTextField();
	private final Frame parent;

	private ArrayList<String> filterFields = new ArrayList<String>();
	private String filterText = "";

	public FilterDialog(Frame parent, boolean modal) {
		super(parent, modal);
		this.parent = parent;
		initComponents();
	}

	public void setFocusOnFilterText() {
		txtFilter.requestFocusInWindow();
	}

	public void setDialogLocation() {
		Point pPt = parent.getLocation();
		int pW = parent.getWidth();
		int pH = parent.getHeight();

		int myW = getWidth();
		int myH = getHeight();

		int myX = (int) pPt.getX() + (pW / 2) - (myW / 2);
		int myY = (int) pPt.getY() + (pH / 2) - (myH / 2);

		setLocation(myX, myY);
	}

	public List<String> getFilterFields() {
		return this.filterFields;
	}

	public String getFilterText() {
		return this.filterText;
	}

	
	
	private void initComponents() {
		this.setTitle("Filter");
		this.setAlwaysOnTop(true);
		this.setModal(true);
		this.getContentPane().setLayout(new GridBagLayout());

		btnApplyFilter.setText("Apply Filter");
		btnApplyFilter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				dlgFilterApply(evt);
			}
		});
		GridBagConstraints gbConstraints = new GridBagConstraints();
		gbConstraints.gridx = 0;
		gbConstraints.gridy = 6;
		gbConstraints.ipadx = 5;
		gbConstraints.ipady = 5;
		gbConstraints.anchor = GridBagConstraints.WEST;
		gbConstraints.insets = new Insets(15, 5, 5, 100);
		this.getContentPane().add(btnApplyFilter, gbConstraints);

		btnCancel.setText("Cancel");
		btnCancel.setPreferredSize(new Dimension(87, 23));
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				dlgFilterCancel(evt);
			}
		});
		gbConstraints = new GridBagConstraints();
		gbConstraints.gridx = 1;
		gbConstraints.gridy = 6;
		gbConstraints.ipadx = 5;
		gbConstraints.ipady = 5;
		gbConstraints.anchor = GridBagConstraints.EAST;
		gbConstraints.insets = new Insets(15, 100, 5, 5);
		this.getContentPane().add(btnCancel, gbConstraints);

		chkRecipeName.setSelected(true);
		chkRecipeName.setText("Recipe Name");
		chkRecipeName.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
		chkRecipeName.setMargin(new Insets(0, 0, 0, 0));
		gbConstraints = new GridBagConstraints();
		gbConstraints.gridx = 0;
		gbConstraints.gridy = 0;
		gbConstraints.gridwidth = 2;
		gbConstraints.anchor = GridBagConstraints.NORTHWEST;
		gbConstraints.insets = new Insets(20, 50, 0, 0);
		this.getContentPane().add(chkRecipeName, gbConstraints);

		chkIngredient.setText("Ingredients");
		chkIngredient.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
		chkIngredient.setMargin(new Insets(0, 0, 0, 0));
		gbConstraints = new GridBagConstraints();
		gbConstraints.gridx = 0;
		gbConstraints.gridy = 1;
		gbConstraints.gridwidth = 2;
		gbConstraints.anchor = GridBagConstraints.NORTHWEST;
		gbConstraints.insets = new Insets(15, 50, 0, 0);
		this.getContentPane().add(chkIngredient, gbConstraints);

		chkProcess.setText("Process");
		chkProcess.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
		chkProcess.setMargin(new Insets(0, 0, 0, 0));
		gbConstraints = new GridBagConstraints();
		gbConstraints.gridx = 0;
		gbConstraints.gridy = 2;
		gbConstraints.gridwidth = 2;
		gbConstraints.anchor = GridBagConstraints.NORTHWEST;
		gbConstraints.insets = new Insets(15, 50, 0, 0);
		this.getContentPane().add(chkProcess, gbConstraints);

		chkComments.setText("Comments");
		chkComments.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
		chkComments.setMargin(new Insets(0, 0, 0, 0));
		gbConstraints = new GridBagConstraints();
		gbConstraints.gridx = 0;
		gbConstraints.gridy = 3;
		gbConstraints.gridwidth = 2;
		gbConstraints.anchor = GridBagConstraints.NORTHWEST;
		gbConstraints.insets = new Insets(15, 50, 0, 0);
		this.getContentPane().add(chkComments, gbConstraints);

		txtFilter.setMinimumSize(new Dimension(300, 19));
		txtFilter.setPreferredSize(new Dimension(300, 19));
		txtFilter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dlgFilterApply(e);
			}
		});
		gbConstraints = new GridBagConstraints();
		gbConstraints.gridx = 0;
		gbConstraints.gridy = 5;
		gbConstraints.gridwidth = 2;
		gbConstraints.fill = GridBagConstraints.HORIZONTAL;
		gbConstraints.anchor = GridBagConstraints.NORTH;
		gbConstraints.insets = new Insets(5, 50, 0, 5);
		this.getContentPane().add(txtFilter, gbConstraints);

		lblSearchText.setText("Search Text");
		gbConstraints = new GridBagConstraints();
		gbConstraints.gridx = 0;
		gbConstraints.gridy = 4;
		gbConstraints.gridwidth = 2;
		gbConstraints.anchor = GridBagConstraints.NORTHWEST;
		gbConstraints.insets = new Insets(25, 50, 0, 0);
		this.getContentPane().add(lblSearchText, gbConstraints);

		pack();
	}

	private void dlgFilterCancel(ActionEvent evt) {
		this.setVisible(false);
		filterFields.clear();
	}

	private void dlgFilterApply(ActionEvent evt) {
		this.setVisible(false);
		this.filterFields.clear();
		this.filterText = "";

		if (chkRecipeName.isSelected()) {
			filterFields.add("Name");
		}

		if (chkIngredient.isSelected()) {
			filterFields.add("Ingredient");
		}

		if (chkProcess.isSelected()) {
			filterFields.add("Process");
		}
		if (chkComments.isSelected()) {
			filterFields.add("Comments");
		}

		this.filterText = txtFilter.getText().trim();
	}
}
