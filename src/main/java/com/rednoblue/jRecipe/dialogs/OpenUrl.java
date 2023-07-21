
package com.rednoblue.jrecipe.dialogs;

import java.awt.Point;

import javax.swing.JDialog;

public class OpenUrl extends JDialog {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final int RET_CANCEL = 0;
	public static final int RET_OK = 1;
	private String url = null;

	public OpenUrl(java.awt.Frame parent, boolean modal) {
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

		okButton = new javax.swing.JButton();
		cancelButton = new javax.swing.JButton();
		lblText = new javax.swing.JLabel();
		urlTextField = new javax.swing.JTextField();

		getContentPane().setLayout(new java.awt.GridBagLayout());

		setTitle("Open Web URL?");
		setModal(true);
		setName("overwriteDialog");
		setResizable(false);
		addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosing(java.awt.event.WindowEvent evt) {
				closeDialog(evt);
			}
		});

		okButton.setText("OK");
		okButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				okButtonActionPerformed(evt);
			}
		});

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.ipadx = 10;
		gridBagConstraints.ipady = 10;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(10, 20, 10, 20);
		getContentPane().add(okButton, gridBagConstraints);

		cancelButton.setText("Cancel");
		cancelButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				cancelButtonActionPerformed(evt);
			}
		});

		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.ipadx = 10;
		gridBagConstraints.ipady = 10;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
		gridBagConstraints.insets = new java.awt.Insets(10, 20, 10, 20);
		getContentPane().add(cancelButton, gridBagConstraints);

		lblText.setText("Type in a web URL:");
		lblText.setAlignmentX(1.0F);
		lblText.setAlignmentY(1.0F);
		lblText.setMaximumSize(new java.awt.Dimension(350, 36));
		lblText.setMinimumSize(new java.awt.Dimension(350, 36));
		lblText.setPreferredSize(new java.awt.Dimension(350, 36));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
		gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 0);
		getContentPane().add(lblText, gridBagConstraints);

		urlTextField.setMinimumSize(new java.awt.Dimension(300, 19));
		urlTextField.setPreferredSize(new java.awt.Dimension(300, 19));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
		gridBagConstraints.insets = new java.awt.Insets(0, 10, 0, 0);
		getContentPane().add(urlTextField, gridBagConstraints);

		pack();
	}// </editor-fold>//GEN-END:initComponents

	private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_cancelButtonActionPerformed
		doClose(RET_CANCEL);
	}// GEN-LAST:event_cancelButtonActionPerformed

	private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_okButtonActionPerformed
		url = urlTextField.getText();
		doClose(RET_OK);
	}// GEN-LAST:event_okButtonActionPerformed

	/** Closes the dialog */
	private void closeDialog(java.awt.event.WindowEvent evt) {// GEN-FIRST:event_closeDialog
		doClose(RET_CANCEL);
	}// GEN-LAST:event_closeDialog

	public String getUrl() {

		return url;
	}

	private void doClose(int retStatus) {
		returnStatus = retStatus;
		setVisible(false);
		dispose();
	}

	// Variables declaration - do not modify//GEN-BEGIN:variables
	private javax.swing.JButton cancelButton;
	private javax.swing.JLabel lblText;
	private javax.swing.JButton okButton;
	private javax.swing.JTextField urlTextField;
	// End of variables declaration//GEN-END:variables

	private int returnStatus = RET_CANCEL;
}
