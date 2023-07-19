
package com.rednoblue.jrecipe.dialogs;

import java.awt.Point;

public class SaveDialog extends javax.swing.JDialog {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final int RET_CANCEL = 0;
	public static final int RET_SAVE = 1;
	public static final int RET_NOSAVE = 2;

	public SaveDialog(java.awt.Frame parent, boolean modal) {
		super(parent, modal);
		initComponents();
		Point pPt = parent.getLocation();
		int pW = parent.getWidth();
		int pH = parent.getHeight();

		int myW = getWidth();
		int myH = getHeight();

		int myX = (int) pPt.getX() + (pW / 2) - (myW / 2);
		int myY = (int) pPt.getY() + (pH / 2) - (myH / 2);

		setLocation(myX, myY);
	}

	public int getReturnStatus() {
		return returnStatus;
	}

	private void initComponents() {
		java.awt.GridBagConstraints gridBagConstraints;

		northPanel = new javax.swing.JPanel();
		southPanel = new javax.swing.JPanel();
		saveButton = new javax.swing.JButton();
		dontSaveButton = new javax.swing.JButton();
		cancelButton = new javax.swing.JButton();
		westPanel = new javax.swing.JPanel();
		eastPanel = new javax.swing.JPanel();
		jPanel1 = new javax.swing.JPanel();
		lblText = new javax.swing.JLabel();

		setTitle("Save?");
		setModal(true);
		setName("overwriteDialog"); // NOI18N
		setResizable(false);
		addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosing(java.awt.event.WindowEvent evt) {
				closeDialog(evt);
			}
		});
		getContentPane().add(northPanel, java.awt.BorderLayout.NORTH);

		southPanel.setLayout(new java.awt.GridBagLayout());

		saveButton.setText("Save");
		saveButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				saveButtonActionPerformed(evt);
			}
		});
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.insets = new java.awt.Insets(0, 20, 0, 20);
		southPanel.add(saveButton, gridBagConstraints);

		dontSaveButton.setText("Don't Save");
		dontSaveButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				dontSaveButtonActionPerformed(evt);
			}
		});
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.insets = new java.awt.Insets(0, 20, 0, 20);
		southPanel.add(dontSaveButton, gridBagConstraints);

		cancelButton.setText("Cancel");
		cancelButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				cancelButtonActionPerformed(evt);
			}
		});
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.insets = new java.awt.Insets(0, 20, 0, 20);
		southPanel.add(cancelButton, gridBagConstraints);

		getContentPane().add(southPanel, java.awt.BorderLayout.SOUTH);
		getContentPane().add(westPanel, java.awt.BorderLayout.WEST);
		getContentPane().add(eastPanel, java.awt.BorderLayout.EAST);

		jPanel1.setLayout(new java.awt.GridLayout(1, 0));

		lblText.setText("Your Recipe Book has been changed.  Would you like to Save?");
		lblText.setMaximumSize(new java.awt.Dimension(350, 36));
		lblText.setMinimumSize(new java.awt.Dimension(350, 36));
		lblText.setPreferredSize(new java.awt.Dimension(350, 36));
		jPanel1.add(lblText);

		getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);

		pack();
	}// </editor-fold>//GEN-END:initComponents

	private void dontSaveButtonActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_dontSaveButtonActionPerformed
		doClose(RET_NOSAVE);
	}// GEN-LAST:event_dontSaveButtonActionPerformed

	private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_cancelButtonActionPerformed
		doClose(RET_CANCEL);
	}// GEN-LAST:event_cancelButtonActionPerformed

	private void saveButtonActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_saveButtonActionPerformed
		doClose(RET_SAVE);
	}// GEN-LAST:event_saveButtonActionPerformed

	/** Closes the dialog */
	private void closeDialog(java.awt.event.WindowEvent evt) {// GEN-FIRST:event_closeDialog
		doClose(RET_CANCEL);
	}// GEN-LAST:event_closeDialog

	private void doClose(int retStatus) {
		returnStatus = retStatus;
		setVisible(false);
		dispose();
	}

	/**
	 * @param args the command line arguments
	 */
	public static void main(String args[]) {
		new SaveDialog(new javax.swing.JFrame(), true).setVisible(true);
	}

	// Variables declaration - do not modify//GEN-BEGIN:variables
	private javax.swing.JButton cancelButton;
	private javax.swing.JButton dontSaveButton;
	private javax.swing.JPanel eastPanel;
	private javax.swing.JPanel jPanel1;
	private javax.swing.JLabel lblText;
	private javax.swing.JPanel northPanel;
	private javax.swing.JButton saveButton;
	private javax.swing.JPanel southPanel;
	private javax.swing.JPanel westPanel;
	// End of variables declaration//GEN-END:variables

	private int returnStatus = RET_CANCEL;
}
