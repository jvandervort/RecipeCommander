package com.rednoblue.jrecipe.ui;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;

public class RecipeTransferable implements Transferable {

	private String data;

	public RecipeTransferable(String data) {
		this.data = data;
	}

	public DataFlavor[] getTransferDataFlavors() {
		return new DataFlavor[] { DataFlavor.stringFlavor };
	}

	public boolean isDataFlavorSupported(DataFlavor flavor) {
		return flavor.equals(DataFlavor.stringFlavor);
	}

	public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException {
		if (isDataFlavorSupported(flavor)) {
			return data;
		}

		throw new UnsupportedFlavorException(flavor);
	}
}
