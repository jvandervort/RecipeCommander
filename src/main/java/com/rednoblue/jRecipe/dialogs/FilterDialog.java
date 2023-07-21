package com.rednoblue.jrecipe.dialogs;

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
	private JLabel jLabel1 = new JLabel();
	private JCheckBox commentsChk = new JCheckBox();
	private JCheckBox ingredientsChk = new JCheckBox();
	private JCheckBox processChk = new JCheckBox();
	private JCheckBox recipeNameChk = new JCheckBox();
	private JTextField filterText = new JTextField();
	private Frame parent;

	private ArrayList<String> filterFields = new ArrayList<String>();
	private String filterTextString = "";

	public FilterDialog(Frame parent, boolean modal) {
		super(parent, modal);
		this.parent = parent;
		initComponents();
	}

	public void setFocusOnFilterText() {
		filterText.requestFocusInWindow();
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

	public String getFilterStrgin() {
		return this.filterTextString;
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

		recipeNameChk.setSelected(true);
		recipeNameChk.setText("Recipe Name");
		recipeNameChk.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
		recipeNameChk.setMargin(new Insets(0, 0, 0, 0));
		gbConstraints = new GridBagConstraints();
		gbConstraints.gridx = 0;
		gbConstraints.gridy = 0;
		gbConstraints.gridwidth = 2;
		gbConstraints.anchor = GridBagConstraints.NORTHWEST;
		gbConstraints.insets = new Insets(20, 50, 0, 0);
		this.getContentPane().add(recipeNameChk, gbConstraints);

		ingredientsChk.setText("Ingredients");
		ingredientsChk.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
		ingredientsChk.setMargin(new Insets(0, 0, 0, 0));
		gbConstraints = new GridBagConstraints();
		gbConstraints.gridx = 0;
		gbConstraints.gridy = 1;
		gbConstraints.gridwidth = 2;
		gbConstraints.anchor = GridBagConstraints.NORTHWEST;
		gbConstraints.insets = new Insets(15, 50, 0, 0);
		this.getContentPane().add(ingredientsChk, gbConstraints);

		processChk.setText("Process");
		processChk.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
		processChk.setMargin(new Insets(0, 0, 0, 0));
		gbConstraints = new GridBagConstraints();
		gbConstraints.gridx = 0;
		gbConstraints.gridy = 2;
		gbConstraints.gridwidth = 2;
		gbConstraints.anchor = GridBagConstraints.NORTHWEST;
		gbConstraints.insets = new Insets(15, 50, 0, 0);
		this.getContentPane().add(processChk, gbConstraints);

		commentsChk.setText("Comments");
		commentsChk.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
		commentsChk.setMargin(new Insets(0, 0, 0, 0));
		commentsChk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				// TODO
			}
		});
		gbConstraints = new GridBagConstraints();
		gbConstraints.gridx = 0;
		gbConstraints.gridy = 3;
		gbConstraints.gridwidth = 2;
		gbConstraints.anchor = GridBagConstraints.NORTHWEST;
		gbConstraints.insets = new Insets(15, 50, 0, 0);
		this.getContentPane().add(commentsChk, gbConstraints);

		filterText.setMinimumSize(new Dimension(300, 19));
		filterText.setPreferredSize(new Dimension(300, 19));
		filterText.addActionListener(new ActionListener() {
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
		this.getContentPane().add(filterText, gbConstraints);

		jLabel1.setText("Search Text");
		gbConstraints = new GridBagConstraints();
		gbConstraints.gridx = 0;
		gbConstraints.gridy = 4;
		gbConstraints.gridwidth = 2;
		gbConstraints.anchor = GridBagConstraints.NORTHWEST;
		gbConstraints.insets = new Insets(25, 50, 0, 0);
		this.getContentPane().add(jLabel1, gbConstraints);

		pack();
	}

	private void dlgFilterCancel(ActionEvent evt) {
		this.setVisible(false);
		filterFields.clear();
	}

	private void dlgFilterApply(ActionEvent evt) {
		this.setVisible(false);
		this.filterFields.clear();
		this.filterTextString = "";

		if (recipeNameChk.isSelected()) {
			filterFields.add("Name");
		}

		if (ingredientsChk.isSelected()) {
			filterFields.add("Ingredient");
		}

		if (processChk.isSelected()) {
			filterFields.add("Process");
		}
		if (commentsChk.isSelected()) {
			filterFields.add("Comments");
		}

		this.filterTextString = filterText.getText().trim();
	}
}
