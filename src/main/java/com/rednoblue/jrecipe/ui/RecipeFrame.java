/*
 * RecipeFrame.java
 *
 * Created on June 5, 2002, 12:10 PM
 */
package com.rednoblue.jrecipe.ui;

import java.awt.Point;
import java.awt.Toolkit;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.logging.Logger;

import javax.swing.JFrame;

import com.rednoblue.jrecipe.model.Book;
import com.rednoblue.jrecipe.model.Recipe;

/**
 *
 * @author jcv9868
 */
public class RecipeFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private final static Logger LOGGER = Logger.getLogger(RecipeFrame.class.getName());

	// currently selected recipe object
	private AppFrame mainframe;
	private Recipe rec;
	private Recipe rec_tmp;
	private Book book;
	private String mode = "None";

	public RecipeFrame(AppFrame mainframe, Book book, Recipe recArg) {
		initComponents();

		ArrayList<String> l = book.getChapters();
		Iterator<String> it = l.iterator();
		while (it.hasNext()) {
			String s = it.next();
			chapterComboBox.addItem(s);
		}

		l = book.getCategories();
		it = l.iterator();
		while (it.hasNext()) {
			String s = (String) it.next();
			catComboBox.addItem(s);
		}

		l = book.getSubCategories();
		it = l.iterator();
		while (it.hasNext()) {
			String s = (String) it.next();
			subCatComboBox.addItem(s);
		}

		this.setIconImage(Toolkit.getDefaultToolkit().createImage(getClass().getResource("/images/appIcon.gif")));
		this.mainframe = mainframe;
		this.book = book;
		if (recArg == null) {
			this.rec = new Recipe();
			this.mode = "Add";
		} else {
			this.rec = recArg;
			this.mode = "Edit";
		}
		this.rec_tmp = rec.duplicate();
		setComponents();
		Point coordPt = mainframe.getLocation();
		setLocation(((int) coordPt.getX()) + 50, ((int) coordPt.getY()) + 50);
	}

	private void initComponents() {
		java.awt.GridBagConstraints gridBagConstraints;

		pnlRecInfo = new javax.swing.JPanel();
		nameLabel = new javax.swing.JLabel();
		nameTextField = new javax.swing.JTextField();
		submitterLabel = new javax.swing.JLabel();
		sourceTextField = new javax.swing.JTextField();
		originLabel = new javax.swing.JLabel();
		originTextField = new javax.swing.JTextField();
		modDateLabel = new javax.swing.JLabel();
		modDateTextField = new javax.swing.JTextField();
		chapterLabel = new javax.swing.JLabel();
		catLabel = new javax.swing.JLabel();
		subcatLabel = new javax.swing.JLabel();
		chapterComboBox = new javax.swing.JComboBox<String>();
		catComboBox = new javax.swing.JComboBox<String>();
		subCatComboBox = new javax.swing.JComboBox<String>();
		mainPanel = new javax.swing.JPanel();
		pnlIngredients = new javax.swing.JPanel();
		sclIngredients = new javax.swing.JScrollPane();
		txtIngredients = new javax.swing.JTextArea();
		pnlProcess = new javax.swing.JPanel();
		sclProcess = new javax.swing.JScrollPane();
		txtProcess = new javax.swing.JTextArea();
		pnlComments = new javax.swing.JPanel();
		sclComments = new javax.swing.JScrollPane();
		txtComments = new javax.swing.JTextArea();
		buttonPanel = new javax.swing.JPanel();
		okButton = new javax.swing.JButton();
		printButton = new javax.swing.JButton();
		cancelButton = new javax.swing.JButton();

		addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosing(java.awt.event.WindowEvent evt) {
				exitForm(evt);
			}
		});

		pnlRecInfo.setBorder(javax.swing.BorderFactory.createTitledBorder("General"));
		pnlRecInfo.setFont(new java.awt.Font("Arial", 0, 11));
		pnlRecInfo.setMaximumSize(new java.awt.Dimension(600, 185));
		pnlRecInfo.setMinimumSize(new java.awt.Dimension(0, 185));
		pnlRecInfo.setPreferredSize(new java.awt.Dimension(600, 185));
		pnlRecInfo.setLayout(new java.awt.GridBagLayout());

		nameLabel.setFont(new java.awt.Font("Dialog", 1, 10));
		nameLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
		nameLabel.setText("Name:");
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.weightx = 1.0;
		pnlRecInfo.add(nameLabel, gridBagConstraints);

		nameTextField.setFont(new java.awt.Font("Arial", 1, 12));
		nameTextField.setHorizontalAlignment(javax.swing.JTextField.LEFT);
		nameTextField.setText("name");
		nameTextField.setBorder(null);
		nameTextField.setMinimumSize(new java.awt.Dimension(129, 25));
		nameTextField.setPreferredSize(new java.awt.Dimension(129, 25));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 10);
		pnlRecInfo.add(nameTextField, gridBagConstraints);

		submitterLabel.setFont(new java.awt.Font("Dialog", 1, 10));
		submitterLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
		submitterLabel.setText("Source/Submitter:");
		submitterLabel.setVerticalAlignment(javax.swing.SwingConstants.TOP);
		submitterLabel.setAlignmentY(0.25F);
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.weightx = 1.0;
		pnlRecInfo.add(submitterLabel, gridBagConstraints);

		sourceTextField.setFont(new java.awt.Font("Arial", 1, 12));
		sourceTextField.setHorizontalAlignment(javax.swing.JTextField.LEFT);
		sourceTextField.setText("submitter/source");
		sourceTextField.setBorder(null);
		sourceTextField.setMinimumSize(new java.awt.Dimension(129, 25));
		sourceTextField.setPreferredSize(new java.awt.Dimension(129, 25));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 3;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 10);
		pnlRecInfo.add(sourceTextField, gridBagConstraints);

		originLabel.setFont(new java.awt.Font("Dialog", 1, 10));
		originLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
		originLabel.setText("Origin/Ethnicitiy:");
		originLabel.setVerticalAlignment(javax.swing.SwingConstants.TOP);
		originLabel.setAlignmentY(0.25F);
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 4;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.weightx = 1.0;
		pnlRecInfo.add(originLabel, gridBagConstraints);

		originTextField.setFont(new java.awt.Font("Arial", 1, 12));
		originTextField.setHorizontalAlignment(javax.swing.JTextField.LEFT);
		originTextField.setText("origin");
		originTextField.setBorder(null);
		originTextField.setMinimumSize(new java.awt.Dimension(129, 25));
		originTextField.setPreferredSize(new java.awt.Dimension(129, 25));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 5;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 10);
		pnlRecInfo.add(originTextField, gridBagConstraints);

		modDateLabel.setFont(new java.awt.Font("Dialog", 1, 10));
		modDateLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
		modDateLabel.setText("Modified Date:");
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 6;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.weightx = 1.0;
		pnlRecInfo.add(modDateLabel, gridBagConstraints);

		modDateTextField.setEditable(false);
		modDateTextField.setFont(new java.awt.Font("Arial", 1, 12));
		modDateTextField.setHorizontalAlignment(javax.swing.JTextField.LEFT);
		modDateTextField.setText("mod Date");
		modDateTextField.setBorder(null);
		modDateTextField.setMinimumSize(new java.awt.Dimension(129, 25));
		modDateTextField.setPreferredSize(new java.awt.Dimension(129, 25));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 7;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 10);
		pnlRecInfo.add(modDateTextField, gridBagConstraints);

		chapterLabel.setFont(new java.awt.Font("Dialog", 1, 10));
		chapterLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
		chapterLabel.setText("Chapter:");
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.weightx = 1.0;
		pnlRecInfo.add(chapterLabel, gridBagConstraints);

		catLabel.setFont(new java.awt.Font("Dialog", 1, 10));
		catLabel.setText("Category:");
		catLabel.setVerticalAlignment(javax.swing.SwingConstants.TOP);
		catLabel.setAlignmentY(0.25F);
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.weightx = 1.0;
		pnlRecInfo.add(catLabel, gridBagConstraints);

		subcatLabel.setFont(new java.awt.Font("Dialog", 1, 10));
		subcatLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
		subcatLabel.setText("Sub-Category:");
		subcatLabel.setVerticalAlignment(javax.swing.SwingConstants.TOP);
		subcatLabel.setAlignmentY(0.25F);
		subcatLabel.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 4;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.weightx = 1.0;
		pnlRecInfo.add(subcatLabel, gridBagConstraints);

		chapterComboBox.setEditable(true);
		chapterComboBox.setFont(new java.awt.Font("Arial", 1, 12));
		chapterComboBox.setMinimumSize(new java.awt.Dimension(129, 25));
		chapterComboBox.setPreferredSize(new java.awt.Dimension(129, 25));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.weightx = 1.0;
		pnlRecInfo.add(chapterComboBox, gridBagConstraints);

		catComboBox.setEditable(true);
		catComboBox.setFont(new java.awt.Font("Arial", 1, 12));
		catComboBox.setMinimumSize(new java.awt.Dimension(129, 25));
		catComboBox.setPreferredSize(new java.awt.Dimension(129, 25));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 3;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.weightx = 1.0;
		pnlRecInfo.add(catComboBox, gridBagConstraints);

		subCatComboBox.setEditable(true);
		subCatComboBox.setFont(new java.awt.Font("Arial", 1, 12));
		subCatComboBox.setMinimumSize(new java.awt.Dimension(129, 25));
		subCatComboBox.setPreferredSize(new java.awt.Dimension(129, 25));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 5;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.weightx = 1.0;
		pnlRecInfo.add(subCatComboBox, gridBagConstraints);

		getContentPane().add(pnlRecInfo, java.awt.BorderLayout.NORTH);

		mainPanel.setLayout(new java.awt.GridBagLayout());

		pnlIngredients.setBorder(javax.swing.BorderFactory.createTitledBorder("Ingredients"));
		pnlIngredients.setFont(new java.awt.Font("Arial", 0, 11));
		pnlIngredients.setMaximumSize(null);
		pnlIngredients.setMinimumSize(new java.awt.Dimension(0, 150));
		pnlIngredients.setPreferredSize(new java.awt.Dimension(0, 150));
		pnlIngredients.setLayout(new java.awt.GridLayout(1, 0));

		sclIngredients.setMaximumSize(null);
		sclIngredients.setMinimumSize(null);

		txtIngredients.setMaximumSize(null);
		txtIngredients.setMinimumSize(null);
		txtIngredients.setPreferredSize(null);
		sclIngredients.setViewportView(txtIngredients);

		pnlIngredients.add(sclIngredients);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
		gridBagConstraints.ipadx = 10;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.weighty = 1.0;
		mainPanel.add(pnlIngredients, gridBagConstraints);

		pnlProcess.setBorder(javax.swing.BorderFactory.createTitledBorder("Process"));
		pnlProcess.setFont(new java.awt.Font("Arial", 0, 11));
		pnlProcess.setMinimumSize(new java.awt.Dimension(0, 150));
		pnlProcess.setPreferredSize(new java.awt.Dimension(0, 150));
		pnlProcess.setLayout(new java.awt.GridLayout(1, 0));

		sclProcess.setMaximumSize(null);
		sclProcess.setMinimumSize(null);

		txtProcess.setLineWrap(true);
		txtProcess.setWrapStyleWord(true);
		txtProcess.setMaximumSize(null);
		sclProcess.setViewportView(txtProcess);

		pnlProcess.add(sclProcess);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.weighty = 1.0;
		mainPanel.add(pnlProcess, gridBagConstraints);

		pnlComments.setBorder(javax.swing.BorderFactory.createTitledBorder("Comments"));
		pnlComments.setFont(new java.awt.Font("Arial", 0, 11));
		pnlComments.setMinimumSize(new java.awt.Dimension(0, 150));
		pnlComments.setPreferredSize(new java.awt.Dimension(0, 150));
		pnlComments.setLayout(new java.awt.GridLayout(1, 0));

		sclComments.setMaximumSize(null);
		sclComments.setMinimumSize(null);

		txtComments.setLineWrap(true);
		txtComments.setWrapStyleWord(true);
		txtComments.setMaximumSize(null);
		sclComments.setViewportView(txtComments);

		pnlComments.add(sclComments);

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.weighty = 1.0;
		mainPanel.add(pnlComments, gridBagConstraints);

		getContentPane().add(mainPanel, java.awt.BorderLayout.CENTER);

		buttonPanel.setMaximumSize(new java.awt.Dimension(32767, 35));
		buttonPanel.setMinimumSize(new java.awt.Dimension(245, 35));
		buttonPanel.setPreferredSize(new java.awt.Dimension(0, 35));
		buttonPanel.setLayout(new java.awt.GridBagLayout());

		okButton.setText("OK");
		okButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				okButtonActionPerformed(evt);
			}
		});
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
		buttonPanel.add(okButton, gridBagConstraints);

		printButton.setText("Print");
		printButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				printButtonActionPerformed(evt);
			}
		});
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
		buttonPanel.add(printButton, gridBagConstraints);

		cancelButton.setText("Cancel");
		cancelButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				cancelButtonActionPerformed(evt);
			}
		});
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 2;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.weightx = 1.0;
		gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 10);
		buttonPanel.add(cancelButton, gridBagConstraints);

		getContentPane().add(buttonPanel, java.awt.BorderLayout.SOUTH);

		pack();
	}// </editor-fold>//GEN-END:initComponents

	private void printButtonActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_printButtonActionPerformed

		// copy the form data into recCopy
		setRecCopyData();

		// book.pdf();
		// mainframe.pdfView(recCopy);

		// call print routine in mainapp
		mainframe.printRecipe(rec_tmp);

		// PrintPreviewFrame pvf = new PrintPreviewFrame(this, recCopy);
		// pvf.setVisible(true);

	}// GEN-LAST:event_printButtonActionPerformed

	private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_okButtonActionPerformed

		// if this is new, link it into book first
		if (mode.equals("Add")) {
			LOGGER.info("Linking rec into book");
			book.addRecipe(rec);
		}

		// copy the form data into recCopy
		setRecCopyData();

		// Compare rec and recCopy, if different, update rec, refresh
		if (rec_tmp.equals(rec) == false) {
			rec.setRecipeName(rec_tmp.getRecipeName());
			rec.setSource(rec_tmp.getSource());
			rec.setChapter(rec_tmp.getChapter());
			rec.setCat(rec_tmp.getCat());
			rec.setSubCat(rec_tmp.getSubCat());
			rec.setOrigin(rec_tmp.getOrigin());
			rec.setIngredientsString(rec_tmp.getIngredientList("text", false));
			rec.setProcess(rec_tmp.getProcess());
			rec.setComments(rec_tmp.getComments());

			Date current = new Date();
			rec.setModDate(current);
			book.setModDate(current);
			book.setModified(true);
			// mainframe.saveButtons(true);
			mainframe.reload(rec);
		}

		this.dispose();
	}// GEN-LAST:event_okButtonActionPerformed

	private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_cancelButtonActionPerformed
		this.dispose();

	}// GEN-LAST:event_cancelButtonActionPerformed

	/** Exit the Application */
	private void exitForm(java.awt.event.WindowEvent evt) {// GEN-FIRST:event_exitForm
		// check to see if recipe has been changed

		// if yes, prompt for save/cancel

		this.setVisible(false);
		this.dispose();
	}// GEN-LAST:event_exitForm

	public void setComponents() {
		// using the rec object, fill all of the controls
		setTitle(rec_tmp.getRecipeName());

		// Load Rec Properties
		nameTextField.setText(rec_tmp.getRecipeName());
		sourceTextField.setText(rec_tmp.getSource());
		chapterComboBox.setSelectedItem(rec_tmp.getChapter());
		catComboBox.setSelectedItem(rec_tmp.getCat());
		subCatComboBox.setSelectedItem(rec_tmp.getSubCat());
		originTextField.setText(rec_tmp.getOrigin());

		String dateText;
		if (rec_tmp.getModDate() == null) {
			dateText = "<No Date>";
		} else {
			dateText = DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.FULL).format(rec_tmp.getModDate());
		}
		modDateTextField.setText(dateText);

		txtIngredients.setText(rec_tmp.getIngredientList("text", false));
		txtProcess.setText(rec_tmp.getProcess());
		txtComments.setText(rec_tmp.getComments());
	}

	private void setRecCopyData() {
		// Save the form data into recCopy object
		rec_tmp.setRecipeName(nameTextField.getText());
		rec_tmp.setSource(sourceTextField.getText());
		rec_tmp.setChapter(chapterComboBox.getSelectedItem().toString());
		rec_tmp.setCat(catComboBox.getSelectedItem().toString());
		rec_tmp.setSubCat(subCatComboBox.getSelectedItem().toString());
		rec_tmp.setOrigin(originTextField.getText());
		rec_tmp.setIngredientsString(txtIngredients.getText());
		rec_tmp.setProcess(txtProcess.getText());
		rec_tmp.setComments(txtComments.getText());
	}

	// Variables declaration - do not modify//GEN-BEGIN:variables
	private javax.swing.JPanel buttonPanel;
	private javax.swing.JButton cancelButton;
	private javax.swing.JComboBox<String> catComboBox;
	private javax.swing.JLabel catLabel;
	private javax.swing.JComboBox<String> chapterComboBox;
	private javax.swing.JLabel chapterLabel;
	private javax.swing.JPanel mainPanel;
	private javax.swing.JLabel modDateLabel;
	private javax.swing.JTextField modDateTextField;
	private javax.swing.JLabel nameLabel;
	private javax.swing.JTextField nameTextField;
	private javax.swing.JButton okButton;
	private javax.swing.JLabel originLabel;
	private javax.swing.JTextField originTextField;
	private javax.swing.JPanel pnlComments;
	private javax.swing.JPanel pnlIngredients;
	private javax.swing.JPanel pnlProcess;
	private javax.swing.JPanel pnlRecInfo;
	private javax.swing.JButton printButton;
	private javax.swing.JScrollPane sclComments;
	private javax.swing.JScrollPane sclIngredients;
	private javax.swing.JScrollPane sclProcess;
	private javax.swing.JTextField sourceTextField;
	private javax.swing.JComboBox<String> subCatComboBox;
	private javax.swing.JLabel subcatLabel;
	private javax.swing.JLabel submitterLabel;
	private javax.swing.JTextArea txtComments;
	private javax.swing.JTextArea txtIngredients;
	private javax.swing.JTextArea txtProcess;
	// End of variables declaration//GEN-END:variables

}
