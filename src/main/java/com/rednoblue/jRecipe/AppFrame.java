package com.rednoblue.jrecipe;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JTree;
import javax.swing.TransferHandler;
import javax.swing.UIManager;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.rednoblue.jrecipe.dialogs.OpenUrl;
import com.rednoblue.jrecipe.dialogs.SaveDialog;
import com.rednoblue.jrecipe.fulltext.RecipeIndexer;
import com.rednoblue.jrecipe.io.MyFileReader;
import com.rednoblue.jrecipe.io.MyFileWriter;
import com.rednoblue.jrecipe.io.input.IRecipeReader;
import com.rednoblue.jrecipe.io.input.ReaderXmlFile;
import com.rednoblue.jrecipe.model.Book;
import com.rednoblue.jrecipe.model.BookUtils;
import com.rednoblue.jrecipe.model.EDisplayType;
import com.rednoblue.jrecipe.model.Recipe;
import com.rednoblue.jrecipe.model.XmlUtils;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRXmlDataSource;
import net.sf.jasperreports.view.JasperViewer;

@Singleton
public class AppFrame extends javax.swing.JFrame implements FileHistory.IFileHistory {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private javax.swing.JButton btnApplyFilter;
	private javax.swing.JButton btnCancel;
	private javax.swing.JButton btnNew;
	private javax.swing.JButton btnOpen;
	private javax.swing.JButton btnPrint;
	private javax.swing.JButton btnSave;
	private javax.swing.JCheckBox commentsChk;
	private javax.swing.JPanel details_panel;
	private javax.swing.JDialog dlgFilter;
	private javax.swing.JTextField filterText;
	private javax.swing.JCheckBox ingredientsChk;
	private javax.swing.JLabel jLabel1;
	private javax.swing.JScrollPane jScrollPane1;
	private javax.swing.JMenuBar menuBar;
	private javax.swing.JMenuItem miCopy;
	private javax.swing.JMenuItem miCut;
	private javax.swing.JMenuItem miDelRecipe;
	private javax.swing.JMenuItem miExit;
	private javax.swing.JCheckBoxMenuItem miFilter;
	private javax.swing.JMenuItem miNew;
	private javax.swing.JMenuItem miNewRecipe;
	private javax.swing.JMenuItem miOpen;
	private javax.swing.JMenuItem miOpenRecipe;
	private javax.swing.JMenuItem miOpenURL;
	private javax.swing.JMenuItem miPaste;
	private javax.swing.JMenuItem miPdfExport;
	private javax.swing.JMenuItem miPdfView;
	private javax.swing.JMenuItem miPrintRecipe;
	private javax.swing.JMenuItem miSave;
	private javax.swing.JMenuItem miSaveAs;
	private javax.swing.JRadioButtonMenuItem miViewByChapter;
	private javax.swing.JRadioButtonMenuItem miViewByOrigin;
	private javax.swing.JRadioButtonMenuItem miViewByRecipe;
	private javax.swing.JRadioButtonMenuItem miViewBySource;
	private javax.swing.JMenu mnuEdit;
	private javax.swing.JMenu mnuFile;
	private javax.swing.JMenu mnuPdf;
	private javax.swing.JMenu mnuRecipe;
	private javax.swing.JMenu mnuSearch;
	private javax.swing.JMenu mnuView;
	private javax.swing.JMenuItem pmiCopyPlaintext;
	private javax.swing.JMenuItem pmiCopyRec;
	private javax.swing.JMenuItem pmiCutRec;
	private javax.swing.JMenuItem pmiExportRec;
	private javax.swing.JMenuItem pmiNewRec;
	private javax.swing.JMenuItem pmiOpenRec;
	private javax.swing.JMenuItem pmiPasteRec;
	private javax.swing.JMenuItem pmiPrintRec;
	private javax.swing.JSeparator pmiSep1;
	private javax.swing.JSeparator pmiSep2;
	private javax.swing.JPopupMenu pmnuRec;
	private javax.swing.JCheckBox processChk;
	private javax.swing.JCheckBox recipeNameChk;
	private javax.swing.JTree recipeTree;
	private javax.swing.JPanel statusPanel;
	private javax.swing.JToolBar toolBar;
	private javax.swing.JTextField txtStatusBar;
	private javax.swing.JSplitPane vSplitPane;
	private javax.swing.ButtonGroup viewByButtonGroup;
	private javax.swing.JPanel viewer_panel;
	private javax.swing.JScrollPane viewer_scroller;

	private FileHistory fileHistory;
	/** currently selected recipe object */
	private Recipe rec;
	/** currently open book object */
	private Book book;
	/** setting for the tree view */
	private String viewBy = "recipe";
	/** filter for the tree view */
	private HashMap<String, String> filter;

	/** indexer for the filter (lucene engine) */
	private RecipeIndexer recipeIndexer;

	/** compiler for the built-in jasper reports */
	private JasperCompiler jasperCompiler;

	private final Logger logger;
	private XmlUtils xmlUtils;
	private MyFileWriter fileWriter;
	private MyFileReader fileReader;

	@Inject
	public AppFrame(Logger logger, JasperCompiler jasperCompiler, RecipeIndexer recipeIndexer, XmlUtils xmlUtils,
			MyFileWriter fileWriter, MyFileReader fileReader) {
		this.logger = logger;
		this.xmlUtils = xmlUtils;
		this.jasperCompiler = jasperCompiler;
		this.recipeIndexer = recipeIndexer;
		this.fileWriter = fileWriter;
		this.fileReader = fileReader;

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			logger.severe(e.toString());
			e.printStackTrace();
			System.exit(1);
		}
		initComponents();

		fileHistory = new FileHistory(this);
		fileHistory.initFileMenuHistory();
	}

	private void initComponents() {
		java.awt.GridBagConstraints gridBagConstraints;

		pmnuRec = new javax.swing.JPopupMenu();
		pmiOpenRec = new javax.swing.JMenuItem();
		pmiPrintRec = new javax.swing.JMenuItem();
		pmiNewRec = new javax.swing.JMenuItem();
		pmiSep1 = new javax.swing.JSeparator();
		pmiExportRec = new javax.swing.JMenuItem();
		pmiCopyPlaintext = new javax.swing.JMenuItem();
		pmiSep2 = new javax.swing.JSeparator();
		pmiCutRec = new javax.swing.JMenuItem();
		pmiCopyRec = new javax.swing.JMenuItem();
		pmiPasteRec = new javax.swing.JMenuItem();
		viewByButtonGroup = new javax.swing.ButtonGroup();
		dlgFilter = new javax.swing.JDialog();
		btnApplyFilter = new javax.swing.JButton();
		btnCancel = new javax.swing.JButton();
		recipeNameChk = new javax.swing.JCheckBox();
		ingredientsChk = new javax.swing.JCheckBox();
		processChk = new javax.swing.JCheckBox();
		commentsChk = new javax.swing.JCheckBox();
		filterText = new javax.swing.JTextField();
		jLabel1 = new javax.swing.JLabel();
		statusPanel = new javax.swing.JPanel();
		txtStatusBar = new javax.swing.JTextField();
		javax.swing.JPanel mainPanel = new javax.swing.JPanel();
		vSplitPane = new javax.swing.JSplitPane();
		details_panel = new javax.swing.JPanel();
		viewer_panel = new javax.swing.JPanel();
		viewer_scroller = new javax.swing.JScrollPane();
		jScrollPane1 = new javax.swing.JScrollPane();
		recipeTree = new javax.swing.JTree();
		recipeTree.addTreeSelectionListener(new RecipeSelectionListener());
		recipeTree.addMouseListener(new RecipeMouseAdapter());
		recipeTree.setTransferHandler(new RecipeTransferHandler());
		toolBar = new javax.swing.JToolBar();
		btnNew = new javax.swing.JButton();
		btnOpen = new javax.swing.JButton();
		btnSave = new javax.swing.JButton();
		btnPrint = new javax.swing.JButton();
		menuBar = new javax.swing.JMenuBar();
		mnuFile = new javax.swing.JMenu();
		miNew = new javax.swing.JMenuItem();
		miOpen = new javax.swing.JMenuItem();
		miOpenURL = new javax.swing.JMenuItem();
		miSave = new javax.swing.JMenuItem();
		miSaveAs = new javax.swing.JMenuItem();
		mnuPdf = new javax.swing.JMenu();
		miPdfExport = new javax.swing.JMenuItem();
		miPdfView = new javax.swing.JMenuItem();
		miExit = new javax.swing.JMenuItem();
		mnuSearch = new javax.swing.JMenu();
		miFilter = new javax.swing.JCheckBoxMenuItem();
		mnuRecipe = new javax.swing.JMenu();
		miOpenRecipe = new javax.swing.JMenuItem();
		miPrintRecipe = new javax.swing.JMenuItem();
		miNewRecipe = new javax.swing.JMenuItem();
		miDelRecipe = new javax.swing.JMenuItem();
		mnuEdit = new javax.swing.JMenu();
		miCut = new javax.swing.JMenuItem();
		miCopy = new javax.swing.JMenuItem();
		miPaste = new javax.swing.JMenuItem();
		mnuView = new javax.swing.JMenu();
		miViewByChapter = new javax.swing.JRadioButtonMenuItem();
		miViewBySource = new javax.swing.JRadioButtonMenuItem();
		miViewByOrigin = new javax.swing.JRadioButtonMenuItem();
		miViewByRecipe = new javax.swing.JRadioButtonMenuItem();

		pmiOpenRec.setText("Open Recipe");
		pmiOpenRec.setToolTipText("Open the selected recipe");
		pmiOpenRec.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				pmiOpenRecActionPerformed(evt);
			}
		});
		pmnuRec.add(pmiOpenRec);

		pmiPrintRec.setText("Print Recipe");
		pmiPrintRec.setToolTipText("Print the selected recipe");
		pmiPrintRec.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				btnPrintActionPerformed(evt);
			}
		});
		pmnuRec.add(pmiPrintRec);

		pmiNewRec.setText("New Recipe");
		pmiNewRec.setToolTipText("Add a new recipe");
		pmiNewRec.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				pmiNewRecActionPerformed(evt);
			}
		});
		pmnuRec.add(pmiNewRec);
		pmnuRec.add(pmiSep1);

		pmiExportRec.setText("Export Recipe");
		pmiExportRec.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				pmiExportRecActionPerformed(evt);
			}
		});
		pmnuRec.add(pmiExportRec);

		pmiCopyPlaintext.setText("Copy Plaintext for Email");
		pmiCopyPlaintext.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				pmiCopyPlaintextActionPerformed(evt);
			}
		});
		pmnuRec.add(pmiCopyPlaintext);
		pmnuRec.add(pmiSep2);

		pmiCutRec.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_X,
				java.awt.event.InputEvent.CTRL_DOWN_MASK));
		pmiCutRec.setText("Cut");
		pmiCutRec.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				miCutActionPerformed(evt);
			}
		});
		pmnuRec.add(pmiCutRec);

		pmiCopyRec.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_C,
				java.awt.event.InputEvent.CTRL_DOWN_MASK));
		pmiCopyRec.setText("Copy");
		pmiCopyRec.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				miCopyActionPerformed(evt);
			}
		});
		pmnuRec.add(pmiCopyRec);

		pmiPasteRec.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_V,
				java.awt.event.InputEvent.CTRL_DOWN_MASK));
		pmiPasteRec.setText("Paste");
		pmiPasteRec.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				miPasteActionPerformed(evt);
			}
		});
		pmnuRec.add(pmiPasteRec);

		dlgFilter.setTitle("Filter");
		dlgFilter.setAlwaysOnTop(true);
		dlgFilter.setModal(true);
		dlgFilter.getContentPane().setLayout(new java.awt.GridBagLayout());

		btnApplyFilter.setText("Apply Filter");
		btnApplyFilter.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				dlgFilterApply(evt);
			}
		});
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 6;
		gridBagConstraints.ipadx = 5;
		gridBagConstraints.ipady = 5;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
		gridBagConstraints.insets = new java.awt.Insets(15, 5, 5, 100);
		dlgFilter.getContentPane().add(btnApplyFilter, gridBagConstraints);

		btnCancel.setText("Cancel");
		btnCancel.setPreferredSize(new java.awt.Dimension(87, 23));
		btnCancel.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				dlgFilterCancel(evt);
			}
		});
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 1;
		gridBagConstraints.gridy = 6;
		gridBagConstraints.ipadx = 5;
		gridBagConstraints.ipady = 5;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
		gridBagConstraints.insets = new java.awt.Insets(15, 100, 5, 5);
		dlgFilter.getContentPane().add(btnCancel, gridBagConstraints);

		recipeNameChk.setSelected(true);
		recipeNameChk.setText("Recipe Name");
		recipeNameChk.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
		recipeNameChk.setMargin(new java.awt.Insets(0, 0, 0, 0));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 0;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
		gridBagConstraints.insets = new java.awt.Insets(20, 50, 0, 0);
		dlgFilter.getContentPane().add(recipeNameChk, gridBagConstraints);

		ingredientsChk.setText("Ingredients");
		ingredientsChk.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
		ingredientsChk.setMargin(new java.awt.Insets(0, 0, 0, 0));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 1;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
		gridBagConstraints.insets = new java.awt.Insets(15, 50, 0, 0);
		dlgFilter.getContentPane().add(ingredientsChk, gridBagConstraints);

		processChk.setText("Process");
		processChk.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
		processChk.setMargin(new java.awt.Insets(0, 0, 0, 0));
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 2;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
		gridBagConstraints.insets = new java.awt.Insets(15, 50, 0, 0);
		dlgFilter.getContentPane().add(processChk, gridBagConstraints);

		commentsChk.setText("Comments");
		commentsChk.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
		commentsChk.setMargin(new java.awt.Insets(0, 0, 0, 0));
		commentsChk.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				commentsChkActionPerformed(evt);
			}
		});
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 3;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
		gridBagConstraints.insets = new java.awt.Insets(15, 50, 0, 0);
		dlgFilter.getContentPane().add(commentsChk, gridBagConstraints);

		filterText.setMinimumSize(new java.awt.Dimension(300, 19));
		filterText.setPreferredSize(new java.awt.Dimension(300, 19));
		filterText.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				filterTextActionPerformed(evt);
			}
		});
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 5;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
		gridBagConstraints.insets = new java.awt.Insets(5, 50, 0, 5);
		dlgFilter.getContentPane().add(filterText, gridBagConstraints);

		jLabel1.setText("Search Text");
		gridBagConstraints = new java.awt.GridBagConstraints();
		gridBagConstraints.gridx = 0;
		gridBagConstraints.gridy = 4;
		gridBagConstraints.gridwidth = 2;
		gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
		gridBagConstraints.insets = new java.awt.Insets(25, 50, 0, 0);
		dlgFilter.getContentPane().add(jLabel1, gridBagConstraints);

		setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
		setName(""); // NOI18N
		addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosed(java.awt.event.WindowEvent evt) {
				mainFrameClosed(evt);
			}

			public void windowClosing(java.awt.event.WindowEvent evt) {
				mainFrameClosing(evt);
			}
		});

		statusPanel.setMaximumSize(new java.awt.Dimension(32767, 14));
		statusPanel.setLayout(new java.awt.GridLayout(1, 0));

		txtStatusBar.setBackground(new java.awt.Color(204, 204, 204));
		txtStatusBar.setEditable(false);
		txtStatusBar.setFont(new java.awt.Font("Dialog", 0, 10));
		txtStatusBar.setBorder(null);
		txtStatusBar.setMaximumSize(new java.awt.Dimension(2147483647, 14));
		txtStatusBar.setMinimumSize(new java.awt.Dimension(100, 14));
		txtStatusBar.setPreferredSize(null);
		statusPanel.add(txtStatusBar);

		getContentPane().add(statusPanel, java.awt.BorderLayout.SOUTH);

		mainPanel.setAlignmentX(10.0F);
		mainPanel.setAlignmentY(10.0F);
		mainPanel.setPreferredSize(new java.awt.Dimension(100, 500));
		mainPanel.setLayout(new java.awt.GridLayout(1, 1));

		vSplitPane.setDividerLocation(300);
		vSplitPane.setMinimumSize(new java.awt.Dimension(100, 50));

		details_panel.setBorder(javax.swing.BorderFactory.createTitledBorder("Recipe Details"));
		details_panel.setMaximumSize(null);
		details_panel.setLayout(new java.awt.GridLayout(1, 0));

		viewer_panel.setToolTipText("Steps to make recipe");
		viewer_panel.setLayout(new java.awt.BorderLayout());

		viewer_scroller.setMaximumSize(null);
		viewer_scroller.setMinimumSize(null);

		viewer_panel.add(viewer_scroller, java.awt.BorderLayout.CENTER);

		details_panel.add(viewer_panel);

		vSplitPane.setRightComponent(details_panel);

		recipeTree.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
		recipeTree.setMaximumSize(new java.awt.Dimension(32768, 32768));
		recipeTree.setModel(null);
		recipeTree.setPreferredSize(null);
		recipeTree.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				RecipeTreePopupHandler(evt);
			}
		});
		jScrollPane1.setViewportView(recipeTree);

		vSplitPane.setLeftComponent(jScrollPane1);

		mainPanel.add(vSplitPane);

		getContentPane().add(mainPanel, java.awt.BorderLayout.CENTER);

		toolBar.setMaximumSize(new java.awt.Dimension(2147483647, 20));
		toolBar.setMinimumSize(new java.awt.Dimension(100, 20));

		btnNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/rednoblue/jRecipe/Images/new.gif"))); // NOI18N
		btnNew.setToolTipText("New");
		btnNew.setBorder(null);
		btnNew.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				newRecipeBookActionPerformed(evt);
			}
		});
		toolBar.add(btnNew);

		btnOpen.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/rednoblue/jRecipe/Images/open.gif"))); // NOI18N
		btnOpen.setToolTipText("Open");
		btnOpen.setBorder(null);
		btnOpen.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				openActionPerformed(evt);
			}
		});
		toolBar.add(btnOpen);

		btnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/rednoblue/jRecipe/Images/save.gif"))); // NOI18N
		btnSave.setToolTipText("Save");
		btnSave.setBorder(null);
		btnSave.setEnabled(false);
		btnSave.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				saveActionPerformed(evt);
			}
		});
		toolBar.add(btnSave);

		btnPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/rednoblue/jRecipe/Images/print.gif"))); // NOI18N
		btnPrint.setToolTipText("Print");
		btnPrint.setBorder(null);
		btnPrint.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				btnPrintActionPerformed(evt);
			}
		});
		toolBar.add(btnPrint);

		getContentPane().add(toolBar, java.awt.BorderLayout.NORTH);

		menuBar.setMaximumSize(new java.awt.Dimension(2147483647, 20));
		menuBar.setMinimumSize(new java.awt.Dimension(100, 20));
		menuBar.setPreferredSize(new java.awt.Dimension(600, 20));

		mnuFile.setMnemonic('F');
		mnuFile.setText("File");
		mnuFile.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				mnuFileActionPerformed(evt);
			}
		});

		miNew.setText("New");
		miNew.setToolTipText("New Recipe Book");
		miNew.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				newRecipeBookActionPerformed(evt);
			}
		});
		mnuFile.add(miNew);

		miOpen.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O,
				java.awt.event.InputEvent.CTRL_DOWN_MASK));
		miOpen.setText("Open");
		miOpen.setToolTipText("Open a Recipe Book");
		miOpen.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				openActionPerformed(evt);
			}
		});
		mnuFile.add(miOpen);

		miOpenURL.setText("Open URL");
		miOpenURL.setToolTipText("Open a Recipe Book on the web");
		miOpenURL.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				miOpenURLActionPerformed(evt);
			}
		});
		mnuFile.add(miOpenURL);

		miSave.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S,
				java.awt.event.InputEvent.CTRL_DOWN_MASK));
		miSave.setText("Save");
		miSave.setToolTipText("Save a Recipe Book");
		miSave.setEnabled(false);
		miSave.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				saveActionPerformed(evt);
			}
		});
		mnuFile.add(miSave);

		miSaveAs.setText("Save As ...");
		miSaveAs.setToolTipText("Save a Recipe Book to a new name");
		miSaveAs.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				saveAsActionPerformed(evt);
			}
		});
		mnuFile.add(miSaveAs);

		mnuPdf.setText("PDF");

		miPdfExport.setText("Export To");
		miPdfExport.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				miPdfExportActionPerformed(evt);
			}
		});
		mnuPdf.add(miPdfExport);

		miPdfView.setText("View");
		miPdfView.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				miPdfViewActionPerformed(evt);
			}
		});
		mnuPdf.add(miPdfView);

		mnuFile.add(mnuPdf);

		miExit.setText("Exit");
		miExit.setToolTipText("Exit");
		miExit.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				miExitActionPerformed(evt);
			}
		});
		mnuFile.add(miExit);

		menuBar.add(mnuFile);

		mnuSearch.setText("Search");

		miFilter.setText("Filter");
		miFilter.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				miFilterActionPerformed(evt);
			}
		});
		mnuSearch.add(miFilter);

		menuBar.add(mnuSearch);

		mnuRecipe.setMnemonic('F');
		mnuRecipe.setText("Recipe");

		miOpenRecipe.setText("Open");
		miOpenRecipe.setToolTipText("Open selected recipe");
		miOpenRecipe.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				miOpenRecipeActionPerformed(evt);
			}
		});
		mnuRecipe.add(miOpenRecipe);

		miPrintRecipe.setText("Print");
		miPrintRecipe.setToolTipText("Print selected recipe");
		miPrintRecipe.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				btnPrintActionPerformed(evt);
			}
		});
		mnuRecipe.add(miPrintRecipe);

		miNewRecipe.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N,
				java.awt.event.InputEvent.CTRL_DOWN_MASK));
		miNewRecipe.setText("New");
		miNewRecipe.setToolTipText("Add a new recipe");
		miNewRecipe.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				miNewRecipeActionPerformed(evt);
			}
		});
		mnuRecipe.add(miNewRecipe);

		miDelRecipe.setText("Delete");
		miDelRecipe.setToolTipText("Delete selected recipe");
		miDelRecipe.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				miDelRecipeActionPerformed(evt);
			}
		});
		mnuRecipe.add(miDelRecipe);

		menuBar.add(mnuRecipe);

		mnuEdit.setMnemonic('E');
		mnuEdit.setText("Edit");

		miCut.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_X,
				java.awt.event.InputEvent.CTRL_DOWN_MASK));
		miCut.setText("Cut");
		miCut.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				miCutActionPerformed(evt);
			}
		});
		mnuEdit.add(miCut);

		miCopy.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_C,
				java.awt.event.InputEvent.CTRL_DOWN_MASK));
		miCopy.setText("Copy");
		miCopy.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				miCopyActionPerformed(evt);
			}
		});
		mnuEdit.add(miCopy);

		miPaste.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_V,
				java.awt.event.InputEvent.CTRL_DOWN_MASK));
		miPaste.setText("Paste");
		miPaste.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				miPasteActionPerformed(evt);
			}
		});
		mnuEdit.add(miPaste);

		menuBar.add(mnuEdit);

		mnuView.setMnemonic('V');
		mnuView.setText("View");

		viewByButtonGroup.add(miViewByChapter);
		miViewByChapter.setText("By Chapter");
		miViewByChapter.setActionCommand("chapter");
		miViewByChapter.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				miViewByChapterActionPerformed(evt);
			}
		});
		mnuView.add(miViewByChapter);

		viewByButtonGroup.add(miViewBySource);
		miViewBySource.setText("By Source");
		miViewBySource.setActionCommand("source");
		miViewBySource.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				miViewBySourceActionPerformed(evt);
			}
		});
		mnuView.add(miViewBySource);

		viewByButtonGroup.add(miViewByOrigin);
		miViewByOrigin.setText("By Origin");
		miViewByOrigin.setActionCommand("origin");
		miViewByOrigin.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				miViewByOriginActionPerformed(evt);
			}
		});
		mnuView.add(miViewByOrigin);

		viewByButtonGroup.add(miViewByRecipe);
		miViewByRecipe.setSelected(true);
		miViewByRecipe.setText("By Recipe");
		miViewByRecipe.setActionCommand("recipe");
		miViewByRecipe.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				miViewByRecipeActionPerformed(evt);
			}
		});
		mnuView.add(miViewByRecipe);

		menuBar.add(mnuView);

		setJMenuBar(menuBar);

		pack();
	}

	private void filterTextActionPerformed(java.awt.event.ActionEvent evt) {
		// TODO
	}

	private void commentsChkActionPerformed(java.awt.event.ActionEvent evt) {
		// TODO
	}

	private void dlgFilterCancel(java.awt.event.ActionEvent evt) {
		dlgFilter.setVisible(false);
	}

	private void dlgFilterApply(java.awt.event.ActionEvent evt) {
		dlgFilter.setVisible(false);
		ArrayList<String> fields = new ArrayList<String>();

		if (recipeNameChk.isSelected()) {
			fields.add("Name");
		}

		if (ingredientsChk.isSelected()) {
			fields.add("Ingredient");
		}

		if (processChk.isSelected()) {
			fields.add("Process");
		}
		if (commentsChk.isSelected()) {
			fields.add("Comments");
		}

		searchFind(fields, filterText.getText());
		if (filter == null) {
			txtStatusBar.setText("Filter OFF");
			miFilter.setSelected(false);
		} else {
			txtStatusBar.setText("Filter ON");
		}

	}

	/**
	 * Copy plain text to windows buffer
	 * 
	 * @param evt
	 */
	private void pmiCopyPlaintextActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_pmiCopyPlaintextActionPerformed
		//
		StringSelection stringSelection = new StringSelection(rec.toStringComplete());
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		clipboard.setContents(stringSelection, null);
	}// GEN-LAST:event_pmiCopyPlaintextActionPerformed

	private void miPdfExportActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_miPdfExportActionPerformed
		exportRecPdf(null);
	}// GEN-LAST:event_miPdfExportActionPerformed

	private void miFilterActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_miFilterActionPerformed
		if (miFilter.isSelected() == true) {

			// Get the size of the screen
			Point loc = this.getLocation();
			Dimension dim = this.getSize();

			dlgFilter.pack();
			// Determine the new location of the window
			int w = dlgFilter.getSize().width;
			int h = dlgFilter.getSize().height;
			int x = loc.x + (dim.width - w) / 2;
			int y = loc.y + (dim.height - h) / 2;
			// Move the window
			dlgFilter.setLocation(x, y);
			dlgFilter.setVisible(true);

		} else {
			filter = null;
			BookUtils bw = new BookUtils(book);
			bw.loadJtree(this.recipeTree, this.viewBy, this.filter);
			txtStatusBar.setText("Filter Off");
		}
	}// GEN-LAST:event_miFilterActionPerformed

	private void pmiExportRecActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_pmiExportRecActionPerformed
		exportRecPdf(rec);
	}// GEN-LAST:event_pmiExportRecActionPerformed

	private void miPdfViewActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_miPdfViewActionPerformed
		pdfView(null);
	}// GEN-LAST:event_miPdfViewActionPerformed

	private void newRecipeBookActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_newRecipeBookActionPerformed
		openNewBook();
	}// GEN-LAST:event_newRecipeBookActionPerformed

	private void mainFrameClosed(java.awt.event.WindowEvent evt) {// GEN-FIRST:event_mainFrameClosed
		System.exit(0);
	}// GEN-LAST:event_mainFrameClosed

	private void miOpenURLActionPerformed(java.awt.event.ActionEvent evt) {

		if (book != null && book.getModified() == true) {
			// prompt for save
			SaveDialog ds = new SaveDialog(this, true);
			ds.setVisible(true);
			if (ds.getReturnStatus() == SaveDialog.RET_CANCEL) {
				// do nothing
				return;
			} else if (ds.getReturnStatus() == SaveDialog.RET_NOSAVE) {
				// do nothing
			} else if (ds.getReturnStatus() == SaveDialog.RET_SAVE) {
				fileWriter.saveFile(book, null, Global.lastFileName, true);
			} else {
				logger.info("Unknown return status from SaveOptionFrame");
				return;
			}
		}

		// prompt for save
		OpenUrl dou = new OpenUrl(this, true);
		dou.setVisible(true);
		if (dou.getReturnStatus() == OpenUrl.RET_CANCEL) {
			// do nothing
			return;
		} else if (dou.getReturnStatus() == OpenUrl.RET_OK) {
			String urlString = dou.getUrl();
			if (urlString != null) {
				openBook(urlString);
			}
		} else {
			logger.info("Unknown return status from DialogOpenURL");
			return;
		}
	}

	private void btnPrintActionPerformed(java.awt.event.ActionEvent evt) {
		// check selection in tree first
		if (rec != null) {
			printRecipe(rec);
		}
	}

	private void miPasteActionPerformed(java.awt.event.ActionEvent evt) {
		// TransferHandler xh = recipeTree.getTransferHandler();
		Action pasteAction = TransferHandler.getPasteAction();
		pasteAction.actionPerformed(new ActionEvent(recipeTree, ActionEvent.ACTION_PERFORMED,
				(String) pasteAction.getValue(Action.NAME), EventQueue.getMostRecentEventTime(), 0));
	}

	private void miCopyActionPerformed(java.awt.event.ActionEvent evt) {
		// TransferHandler xh = recipeTree.getTransferHandler();
		Action copyAction = TransferHandler.getCopyAction();
		copyAction.actionPerformed(new ActionEvent(recipeTree, ActionEvent.ACTION_PERFORMED,
				(String) copyAction.getValue(Action.NAME), EventQueue.getMostRecentEventTime(), 0));
	}

	private void miCutActionPerformed(java.awt.event.ActionEvent evt) {
		// TransferHandler xh = recipeTree.getTransferHandler();
		Action cutAction = TransferHandler.getCutAction();
		cutAction.actionPerformed(new ActionEvent(recipeTree, ActionEvent.ACTION_PERFORMED,
				(String) cutAction.getValue(Action.NAME), EventQueue.getMostRecentEventTime(), 0));
	}

	private void miDelRecipeActionPerformed(java.awt.event.ActionEvent evt) {
		deleteRecipe();
	}

	private void miViewBySourceActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_miViewBySourceActionPerformed
		setViewBy(viewByButtonGroup.getSelection().getActionCommand());
		BookUtils bw = new BookUtils(book);
		bw.loadJtree(this.recipeTree, this.viewBy, this.filter);
	}// GEN-LAST:event_miViewBySourceActionPerformed

	private void pmiNewRecActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_pmiNewRecActionPerformed
		newRecipe();
	}// GEN-LAST:event_pmiNewRecActionPerformed

	private void miNewRecipeActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_miNewRecipeActionPerformed
		newRecipe();
	}// GEN-LAST:event_miNewRecipeActionPerformed

	private void miViewByRecipeActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_miViewByRecipeActionPerformed
		setViewBy(viewByButtonGroup.getSelection().getActionCommand());
		BookUtils bw = new BookUtils(book);
		bw.loadJtree(this.recipeTree, this.viewBy, this.filter);
	}// GEN-LAST:event_miViewByRecipeActionPerformed

	private void miViewByOriginActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_miViewByOriginActionPerformed
		setViewBy(viewByButtonGroup.getSelection().getActionCommand());
		BookUtils bw = new BookUtils(book);
		bw.loadJtree(this.recipeTree, this.viewBy, this.filter);
	}// GEN-LAST:event_miViewByOriginActionPerformed

	private void miViewByChapterActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_miViewByChapterActionPerformed
		setViewBy(viewByButtonGroup.getSelection().getActionCommand());
		BookUtils bw = new BookUtils(book);
		bw.loadJtree(this.recipeTree, this.viewBy, this.filter);
	}// GEN-LAST:event_miViewByChapterActionPerformed

	private void miOpenRecipeActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_miOpenRecipeActionPerformed
		openRecipe();
	}// GEN-LAST:event_miOpenRecipeActionPerformed

	private void mainFrameClosing(java.awt.event.WindowEvent evt) {// GEN-FIRST:event_mainFrameClosing
		exitApp();
	}// GEN-LAST:event_mainFrameClosing

	private void pmiOpenRecActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_pmiOpenRecActionPerformed
		openRecipe();
	}// GEN-LAST:event_pmiOpenRecActionPerformed

	private void RecipeTreePopupHandler(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_RecipeTreePopupHandler
		// popup menu?
		if (evt.isPopupTrigger()) {
			pmnuRec.show(evt.getComponent(), evt.getX(), evt.getY());
			evt.consume();
			return;
		}
		super.processMouseEvent(evt);
	}

	private void saveAsActionPerformed(java.awt.event.ActionEvent evt) {
		String filePath = fileWriter.browseFileSystem(AppFrame.this, "jRecipe");
		if (filePath != null && fileWriter.saveFile(book, null, filePath, true) == true) {
			if (fileWriter.getLastWriter().isReadable()) {
				Global.lastFileName = filePath;
				fileHistory.insertPathname(Global.lastFileName);
				this.setTitle(Global.appName + " - [" + Global.lastFileName + "]");
				book.setModified(false);
				saveButtons(book.getModified());
			}
		}
	}

	private void saveActionPerformed(java.awt.event.ActionEvent evt) {
		fileWriter.saveFile(book, null, Global.lastFileName, true);
		this.setTitle(Global.appName + " - [" + Global.lastFileName + "]");
		book.setModified(false);
		saveButtons(book.getModified());
	}

	private void openActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_openActionPerformed
		if (book != null && book.getModified() == true) {
			// prompt for save
			SaveDialog ds = new SaveDialog(this, true);
			ds.setVisible(true);
			if (ds.getReturnStatus() == SaveDialog.RET_CANCEL) {
				// do nothing
				return;
			} else if (ds.getReturnStatus() == SaveDialog.RET_NOSAVE) {
				// do nothing
			} else if (ds.getReturnStatus() == SaveDialog.RET_SAVE) {
				fileWriter.saveFile(book, null, Global.lastFileName, true);
			} else {
				logger.info("Unknown return status from SaveOptionFrame");
				return;
			}
		}

		String fileName = fileReader.browseFileSystem(AppFrame.this);
		if (fileName != null) {
			if (openBook(fileName)) {
				fileHistory.insertPathname(Global.lastFileName);
			}
		}
	}// GEN-LAST:event_openActionPerformed

	private void mnuFileActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_mnuFileActionPerformed
		// Add your handling code here:
	}// GEN-LAST:event_mnuFileActionPerformed

	private void miExitActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_miExitActionPerformed
		exitApp();
	}// GEN-LAST:event_miExitActionPerformed

	public void setIcon() {

		setIconImage(Toolkit.getDefaultToolkit()
				.createImage(getClass().getResource("/com/rednoblue/jRecipe/Images/appIcon.gif")));

	}

	public void loadGlobals() {
		Global.loadAll();
		if (Global.lastFileName.length() > 0) {
			if (openBook(Global.lastFileName)) {
				this.setTitle(Global.appName + " - [" + Global.lastFileName + "]");
			}
		}
		setLocation(Global.LocX, Global.LocY);
		setSize(Global.SizeW, Global.SizeH);
	}

	public void saveGlobals() {
		Point coordPt = this.getLocation();
		Global.LocX = (int) coordPt.getX();
		Global.LocY = (int) coordPt.getY();

		Dimension appDim = getSize();
		Global.SizeH = appDim.height;
		Global.SizeW = appDim.width;

		Global.saveAll();
	}

	public void openNewBook() {
		if (book != null && book.getModified() == true) {
			// prompt for save
			SaveDialog ds = new SaveDialog(this, true);
			ds.setVisible(true);
			if (ds.getReturnStatus() == SaveDialog.RET_CANCEL) {
				// do nothing
				return;
			} else if (ds.getReturnStatus() == SaveDialog.RET_NOSAVE) {
				// do nothing
			} else if (ds.getReturnStatus() == SaveDialog.RET_SAVE) {
				fileWriter.saveFile(book, null, Global.lastFileName, true);
			} else {
				logger.info("Unknown return status from SaveOptionFrame");
				return;
			}
		}

		book = new Book();
		miFilter.setSelected(false);
		filter = null;
		book.setBookName("New Recipe Book");
		Global.lastFileName = "";
		this.setTitle(Global.appName + " - [New Book]");
		book.setModified(true);
		saveButtons(book.getModified());
		Recipe r = new Recipe();
		r.setRecipeName("Blank Recipe");
		book.addRecipe(r);

		BookUtils bw = new BookUtils(book);
		bw.loadJtree(this.recipeTree, this.viewBy, this.filter);
		this.recipeTree.revalidate();
	}

	public boolean openBook(String path) {
		try {
			fileReader.loadFile(path);
			book = fileReader.getBook();
			if (book == null) {
				return false;
			}
			miFilter.setSelected(false);
			filter = null;
			BookUtils bw = new BookUtils(book);
			bw.loadJtree(this.recipeTree, this.viewBy, this.filter);
			this.recipeTree.revalidate();

			// Save recently opened files
			Global.lastFileName = path;
			setTitle(Global.appName + " - [" + Global.lastFileName + "]");
			recipeIndexer.indexRecipes(book);
			return true;
		} catch (FileNotFoundException e) {
			logger.severe(e.toString());
			e.printStackTrace();
		} catch (IOException e) {
			logger.severe(e.toString());
			e.printStackTrace();
		}

		return false;
	}

	public void setLastFileName(String argFileName) {
		Global.lastFileName = argFileName;
	}

	public String getLastFileName() {
		return Global.lastFileName;
	}

	public Book getBook() {
		return book;
	}

	public String getViewBy() {
		return viewBy;
	}

	public int setViewBy(String viewBy) {
		this.viewBy = viewBy;
		return 1;
	}

	public void printRecipe(Recipe recArg) {

		// check selection in tree first
		if (recArg != null) {
			pdfView(recArg);
		}
	}

	private void openRecipe() {

		// check selection in tree first
		if (rec != null) {
			RecipeFrame rf = new RecipeFrame(this, book, rec);
			rf.setVisible(true);
		}
	}

	private void searchFind(Collection<String> fields, String queryString) {
		if (recipeIndexer.indexThread != null && recipeIndexer.indexThread.isAlive()) {
			// indexer still running, bail
			return;
		}

		filter = recipeIndexer.searchRecipes(fields, queryString);
		if (filter != null) {
			BookUtils bw = new BookUtils(book);
			bw.loadJtree(this.recipeTree, this.viewBy, this.filter);
		}
	}

	private void deleteRecipe() {
		// check selection in tree first
		if (rec != null) {
			book.deleteRecipe(rec);
			book.setModified(true);
			reload(null);
		}
	}

	private void newRecipe() {

		// check selection in tree first

		RecipeFrame rf = new RecipeFrame(this, book, null);
		Point coordPt = this.getLocation();

		rf.setLocation(((int) coordPt.getX()) + 50, ((int) coordPt.getY()) + 50);

		rf.setVisible(true);

	}

	private boolean exitApp() {
		// check for changed recipes/book
		if (book != null && book.getModified() == true) {
			// prompt for save
			SaveDialog ds = new SaveDialog(this, true);
			ds.setVisible(true);
			if (ds.getReturnStatus() == SaveDialog.RET_CANCEL) {
				// do nothing
				return false;
			} else if (ds.getReturnStatus() == SaveDialog.RET_NOSAVE) {
				saveGlobals();
				fileHistory.saveHistoryEntries(); // save entries for next session
				this.dispose();
			} else if (ds.getReturnStatus() == SaveDialog.RET_SAVE) {
				fileWriter.saveFile(book, null, Global.lastFileName, true);
				saveGlobals();
				fileHistory.saveHistoryEntries(); // save entries for next session
				this.dispose();
			} else {
				// do nothing
				return false;
			}
		} else {
			saveGlobals();
			// save file history
			fileHistory.saveHistoryEntries(); // save entries for next session
			// exit app
			this.dispose();
		}
		return true;
	}

	public void reload(Recipe recArg) {
		// reindex for filter
		recipeIndexer.indexRecipes(book);

		// called after new recipe, or edits save to existing recipe
		saveButtons(book.getModified());

		// save selection
		if (recArg == null) {
			// find last selection
			if (rec != null) {
				recArg = rec;
			}
		}

		// fill the tree
		BookUtils bw = new BookUtils(book);
		bw.loadJtree(this.recipeTree, this.viewBy, this.filter);

		// find rec in new tree model
		if (recArg != null) {
			DefaultTreeModel tm = (DefaultTreeModel) recipeTree.getModel();
			DefaultMutableTreeNode root = (DefaultMutableTreeNode) tm.getRoot();

			if (root != null) {
				for (Enumeration<TreeNode> e = root.breadthFirstEnumeration(); e.hasMoreElements();) {
					DefaultMutableTreeNode current = (DefaultMutableTreeNode) e.nextElement();

					if (recArg == current.getUserObject()) {
						// Found Arg, select it.
						TreePath sel = new TreePath(tm.getPathToRoot(current));
						recipeTree.setSelectionPath(sel);
						recipeTree.scrollPathToVisible(sel);
						break;
					}
				}
			}
		}
	}

	public void saveButtons(boolean value) {
		// called changes are made to enable save buttons

		if (Global.lastFileName.startsWith("http://")) {
			btnSave.setEnabled(false);
			miSave.setEnabled(false);
		} else if (Global.lastFileName.equals("")) {
			btnSave.setEnabled(false);
			miSave.setEnabled(false);
		} else {
			btnSave.setEnabled(value);
			miSave.setEnabled(value);
			if (value == true) {
				this.setTitle(Global.appName + " - [" + Global.lastFileName + "*]");
			} else {
				this.setTitle(Global.appName + " - [" + Global.lastFileName + "]");
			}
		}
	}

	/**
	 * --- Implementation of FileHistory.IFileHistory interface ---------------- Get
	 * the application name to identify the configuration file in the the USER_HOME
	 * directory. This name should be unique in this directory.
	 *
	 * @return the application name
	 */
	public String getApplicationName() {
		return Global.appName;
	}

	/**
	 * Get a handle to the frame's file menu.
	 *
	 * @return the frame's file menu
	 */
	public JMenu getFileMenu() {
		return menuBar.getMenu(0);
	}

	/**
	 * Return the size of the main application frame. It is used to center the file
	 * history maintenance window.
	 *
	 * @return the frame's size
	 */
	@Override
	public Dimension getSize() {
		return super.getSize();
	}

	/**
	 * Return the main application frame. It is used to center the file history
	 * maintenance window.
	 *
	 * @return the main GUI frame
	 */
	public JFrame getFrame() {
		return this;
	}

	/**
	 * Simulate a load file activity.
	 *
	 * @param path the pathname of the loaded file
	 */
	public boolean loadFile(String path) {
		if (book != null && book.getModified() == true) {
			// prompt for save
			SaveDialog ds = new SaveDialog(this, true);
			ds.setVisible(true);
			if (ds.getReturnStatus() == SaveDialog.RET_CANCEL) {
				// do nothing
				return false;
			} else if (ds.getReturnStatus() == SaveDialog.RET_NOSAVE) {
				// do nothing
			} else if (ds.getReturnStatus() == SaveDialog.RET_SAVE) {
				fileWriter.saveFile(book, null, Global.lastFileName, true);
			} else {
				logger.info("Unknown return status from SaveOptionFrame");
				return false;
			}
		}
		return openBook(path);
	}

	public JasperPrint get_jasper_print(Recipe r) {
		JasperPrint jasperPrint = new JasperPrint();
		try {
			JRDataSource ds = null;
			if (r == null) {
				ds = new JRXmlDataSource(xmlUtils.transformToXmlDocument(book, EDisplayType.JASPER), "//Recipe");
			} else {
				ds = new JRXmlDataSource(xmlUtils.transformToXmlDocument(book, r, EDisplayType.JASPER), "//Recipe");
			}
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("IngredientSubreport", this.jasperCompiler.get_ingred_report());
			jasperPrint = JasperFillManager.fillReport(this.jasperCompiler.get_main_report(), parameters, ds);
		} catch (JRException e) {
			logger.severe(e.toString());
			e.printStackTrace();
		}
		return jasperPrint;
	}

	public void pdfView(Recipe r) {
		JasperPrint jasperPrint = get_jasper_print(r);
		JasperViewer.viewReport(jasperPrint, false);
	}

	public void exportRecPdf(Recipe recArg) {
		String filePath = fileWriter.browseFileSystem(AppFrame.this, "PDF");
		if (filePath != null && fileWriter.saveFile(book, recArg, filePath, true) == true) {
			if (fileWriter.getLastWriter().isReadable()) {
				Global.lastFileName = filePath;
				fileHistory.insertPathname(Global.lastFileName);
				this.setTitle(Global.appName + " - [" + Global.lastFileName + "]");
				book.setModified(false);
				saveButtons(book.getModified());
			}
		}
	}

	/*
	 * Begin Cut Copy Paste from tree stuff
	 */
	class RecipeTransferHandler extends TransferHandler {
		private static final long serialVersionUID = 1L;

		@Override
		public int getSourceActions(JComponent c) {
			return COPY;
		}

		@Override
		public Transferable createTransferable(JComponent c) {
			JTree tree = (JTree) c;
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
			if (node.isLeaf()) {
				// get the object
				Object nodeInfo = node.getUserObject();
				// you have a recipe
				rec = (Recipe) nodeInfo;
				return new RecipeTransferable(xmlUtils.transformToXmlString(book, rec, EDisplayType.NORMAL) + "\n");
			} else {
				return new RecipeTransferable("");
			}
		}

		@Override
		public void exportDone(JComponent c, Transferable data, int action) {
			if (action == MOVE) {
				logger.info("Removing Recipe");
				JTree tree = (JTree) c;
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
				if (node.isLeaf()) {
					// get the object
					Object nodeInfo = node.getUserObject();
					// you have a recipe
					rec = (Recipe) nodeInfo;
					if (rec != null) {
						book.deleteRecipe(rec);
						book.setModified(true);
						book.setModDate(new Date());
						saveButtons(true);
						reload(null);
					}
				} else {
					// Do Nothing, not on a recipe
				}
			} else {
				// Do Nothing, not a move action
			}

		}

		@Override
		public boolean canImport(JComponent comp, DataFlavor[] transferFlavors) {
			if (comp instanceof JTree) {
				for (int i = 0; i < transferFlavors.length; i++) {
					if (!transferFlavors[i].equals(DataFlavor.stringFlavor)) {
						return false;
					}
				}
				return true;
			}
			return false;

		}

		@Override
		public boolean importData(JComponent comp, Transferable t) {
			if (!(comp instanceof JTree)) {
				return false;
			}
			if (!t.isDataFlavorSupported(DataFlavor.stringFlavor)) {
				return false;
			}

			try {
				// Get the xml string out of the transferable
				String data = (String) t.getTransferData(DataFlavor.stringFlavor);
				// do nothing if you've got an empty string
				if (data.equals("")) {
					return false;
				}

				StringReader sr = new StringReader(data);
				IRecipeReader reader = new ReaderXmlFile();

				Book tbook = reader.parseSource(sr);
				if (tbook.recipeCount() > 0) {
					ArrayList<Recipe> rList = (ArrayList<Recipe>) tbook.getRecipes();
					Recipe rec = (Recipe) rList.get(0);
					logger.info("Got " + rec.getRecipeName());
					int i = 0;
					String newName = rec.getRecipeName();
					while (book.findRecipeByName(newName) != null) {
						i++;
						newName = rec.getRecipeName() + i;
					}
					rec.setRecipeName(newName);
					book.addRecipe(rec);
					book.setModified(true);
					book.setModDate(new Date());
					saveButtons(true);
					reload(rec);
					// select new rec
				}
			} catch (UnsupportedFlavorException e) {
				logger.severe(e.toString());
				e.printStackTrace();
			} catch (IOException e) {
				logger.severe(e.toString());
				e.printStackTrace();
			} catch (java.lang.Exception e) {
				logger.severe(e.toString());
				e.printStackTrace();
			}
			return false;

		}
	}

	class RecipeTransferable implements Transferable {

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

	class RecipeSelectionListener implements TreeSelectionListener {
		public void valueChanged(TreeSelectionEvent e) {

			DefaultMutableTreeNode node = (DefaultMutableTreeNode) recipeTree.getLastSelectedPathComponent();
			if (node != null && node.isLeaf()) {
				Object nodeInfo = node.getUserObject();
				// you have a recipe
				rec = (Recipe) nodeInfo;
				if (jasperCompiler.is_running()) {
					logger.info("JasperComplier is still running");
				} else {
					viewer_scroller.setViewportView(new MyJRViewer(get_jasper_print(rec)));
				}
			} else {
				// no children, ie book, chapter, etc... or no selection
				rec = null;
			}
		}
	}

	class RecipeMouseAdapter extends MouseAdapter {
		@Override
		public void mouseReleased(MouseEvent e) {
			// select row first
			// if (e.isMetaDown()) {
			int row = recipeTree.getRowForLocation(e.getX(), e.getY());
			if (row != -1)
				recipeTree.setSelectionInterval(row, row); // select row
			else
				recipeTree.clearSelection(); // select nothing
			// }
			// popup menu, if right clicked recipe
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) recipeTree.getLastSelectedPathComponent();

			if (node == null)
				return;

			if (e.isPopupTrigger() && (node.isLeaf())) {
				pmnuRec.show((javax.swing.JComponent) e.getSource(), e.getX(), e.getY());
			}
		}

		@Override
		public void mouseClicked(MouseEvent me) {
			if (me.getClickCount() == 2) {
				openRecipe();
				// me.consume();
			}
		}
	}

}
