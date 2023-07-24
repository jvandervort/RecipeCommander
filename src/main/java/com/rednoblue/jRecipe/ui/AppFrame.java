package com.rednoblue.jrecipe.ui;

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
import java.util.List;
import java.util.logging.Logger;

import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.KeyStroke;
import javax.swing.TransferHandler;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.border.BevelBorder;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.rednoblue.jrecipe.FileHistory;
import com.rednoblue.jrecipe.UserPrefs;
import com.rednoblue.jrecipe.fulltext.RecipeIndexer;
import com.rednoblue.jrecipe.io.MyFileReader;
import com.rednoblue.jrecipe.io.MyFileWriter;
import com.rednoblue.jrecipe.io.input.IRecipeReader;
import com.rednoblue.jrecipe.io.input.ReaderXmlFile;
import com.rednoblue.jrecipe.model.Book;
import com.rednoblue.jrecipe.model.EDisplayType;
import com.rednoblue.jrecipe.model.Recipe;
import com.rednoblue.jrecipe.report.JasperCompiler;
import com.rednoblue.jrecipe.report.MyJRViewer;
import com.rednoblue.jrecipe.ui.dialog.FilterDialog;
import com.rednoblue.jrecipe.ui.dialog.OpenUrl;
import com.rednoblue.jrecipe.ui.dialog.SaveDialog;
import com.rednoblue.jrecipe.xml.XmlTransformer;

import net.sf.jasperreports.view.JasperViewer;

@Singleton
public class AppFrame extends JFrame implements FileHistory.IFileHistory {
	private static final long serialVersionUID = 1L;
	private final Logger logger;

	private JPanel mainPanel;
	private JButton btnNew;
	private JButton btnOpen;
	private JButton btnPrint;
	private JButton btnSave;
	private JPanel details_panel;
	private JScrollPane jScrollPane1;
	private JMenuBar menuBar;
	private JMenuItem miCopy;
	private JMenuItem miCut;
	private JMenuItem miDelRecipe;
	private JMenuItem miExit;
	private JCheckBoxMenuItem miFilter;
	private JMenuItem miNew;
	private JMenuItem miNewRecipe;
	private JMenuItem miOpen;
	private JMenuItem miOpenRecipe;
	private JMenuItem miOpenURL;
	private JMenuItem miPaste;
	private JMenuItem miPdfExport;
	private JMenuItem miPdfView;
	private JMenuItem miPrintRecipe;
	private JMenuItem miSave;
	private JMenuItem miSaveAs;
	private JRadioButtonMenuItem miViewByChapter;
	private JRadioButtonMenuItem miViewByOrigin;
	private JRadioButtonMenuItem miViewByRecipe;
	private JRadioButtonMenuItem miViewBySource;
	private JMenu mnuEdit;
	private JMenu mnuFile;
	private JMenu mnuPdf;
	private JMenu mnuRecipe;
	private JMenu mnuSearch;
	private JMenu mnuView;
	private JMenuItem pmiCopyPlaintext;
	private JMenuItem pmiCopyRec;
	private JMenuItem pmiCutRec;
	private JMenuItem pmiExportRec;
	private JMenuItem pmiNewRec;
	private JMenuItem pmiOpenRec;
	private JMenuItem pmiPasteRec;
	private JMenuItem pmiPrintRec;
	private JSeparator pmiSep1;
	private JSeparator pmiSep2;
	private JPopupMenu pmnuRec;
	private JTree recipeTree;
	private JPanel statusPanel;
	private JToolBar toolBar;
	private JTextField txtStatusBar;
	private JSplitPane vSplitPane;
	private ButtonGroup viewByButtonGroup;
	private JPanel viewer_panel;
	private JScrollPane viewer_scroller;
	private FilterDialog filterDialog;

	private FileHistory fileHistory;

	/**
	 * currently selected recipe object
	 */

	private Recipe rec;
	/**
	 * currently open book object
	 */

	private Book book;

	/**
	 * setting for the tree view
	 */
	private String viewBy = "recipe";

	/**
	 * filter for the tree view
	 */
	private HashMap<String, String> filter;

	/**
	 * Indexer for the filter (Lucene engine)
	 */
	private RecipeIndexer recipeIndexer;

	/**
	 * compiler for the built-in Jasper Reports
	 */
	private JasperCompiler jasperCompiler;

	private XmlTransformer xmlUtils;
	private MyFileWriter fileWriter;
	private MyFileReader fileReader;
	private UserPrefs userPrefs;

	@Inject
	public AppFrame(Logger logger, JasperCompiler jasperCompiler, RecipeIndexer recipeIndexer, XmlTransformer xmlUtils,
			MyFileWriter fileWriter, MyFileReader fileReader, UserPrefs userPrefs) {
		this.logger = logger;
		this.userPrefs = userPrefs;

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			logger.severe(e.toString());
			e.printStackTrace();
			System.exit(1);
		}

		this.xmlUtils = xmlUtils;
		this.jasperCompiler = jasperCompiler;
		this.recipeIndexer = recipeIndexer;
		this.fileWriter = fileWriter;
		this.fileReader = fileReader;

		initComponents();

		this.filterDialog = new FilterDialog(this, true);

		this.fileHistory = new FileHistory(this);
		this.fileHistory.initFileMenuHistory();
	}

	private void initComponents() {
		pmnuRec = new JPopupMenu();
		pmiOpenRec = new JMenuItem();
		pmiPrintRec = new JMenuItem();
		pmiNewRec = new JMenuItem();
		pmiSep1 = new JSeparator();
		pmiExportRec = new JMenuItem();
		pmiCopyPlaintext = new JMenuItem();
		pmiSep2 = new JSeparator();
		pmiCutRec = new JMenuItem();
		pmiCopyRec = new JMenuItem();
		pmiPasteRec = new JMenuItem();
		viewByButtonGroup = new ButtonGroup();
		statusPanel = new JPanel();
		txtStatusBar = new JTextField();
		mainPanel = new JPanel();
		vSplitPane = new JSplitPane();
		details_panel = new JPanel();
		viewer_panel = new JPanel();
		viewer_scroller = new JScrollPane();
		jScrollPane1 = new JScrollPane();
		recipeTree = new JTree();
		recipeTree.addTreeSelectionListener(new RecipeSelectionListener());
		recipeTree.addMouseListener(new RecipeMouseAdapter());
		recipeTree.setTransferHandler(new RecipeTransferHandler());
		toolBar = new JToolBar();
		btnNew = new JButton();
		btnOpen = new JButton();
		btnSave = new JButton();
		btnPrint = new JButton();
		menuBar = new JMenuBar();
		mnuFile = new JMenu();
		miNew = new JMenuItem();
		miOpen = new JMenuItem();
		miOpenURL = new JMenuItem();
		miSave = new JMenuItem();
		miSaveAs = new JMenuItem();
		mnuPdf = new JMenu();
		miPdfExport = new JMenuItem();
		miPdfView = new JMenuItem();
		miExit = new JMenuItem();
		mnuSearch = new JMenu();
		miFilter = new JCheckBoxMenuItem();
		mnuRecipe = new JMenu();
		miOpenRecipe = new JMenuItem();
		miPrintRecipe = new JMenuItem();
		miNewRecipe = new JMenuItem();
		miDelRecipe = new JMenuItem();
		mnuEdit = new JMenu();
		miCut = new JMenuItem();
		miCopy = new JMenuItem();
		miPaste = new JMenuItem();
		mnuView = new JMenu();
		miViewByChapter = new JRadioButtonMenuItem();
		miViewBySource = new JRadioButtonMenuItem();
		miViewByOrigin = new JRadioButtonMenuItem();
		miViewByRecipe = new JRadioButtonMenuItem();

		pmiOpenRec.setText("Open Recipe");
		pmiOpenRec.setToolTipText("Open the selected recipe");
		pmiOpenRec.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				openRecipe();
			}
		});
		pmnuRec.add(pmiOpenRec);

		pmiPrintRec.setText("Print Recipe");
		pmiPrintRec.setToolTipText("Print the selected recipe");
		pmiPrintRec.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				// check selection in tree first
				if (rec != null) {
					printRecipe(rec);
				}
			}
		});
		pmnuRec.add(pmiPrintRec);

		pmiNewRec.setText("New Recipe");
		pmiNewRec.setToolTipText("Add a new recipe");
		pmiNewRec.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				newRecipe();
			}
		});
		pmnuRec.add(pmiNewRec);
		pmnuRec.add(pmiSep1);

		pmiExportRec.setText("Export Recipe");
		pmiExportRec.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				exportRecPdf(rec);
			}
		});
		pmnuRec.add(pmiExportRec);

		pmiCopyPlaintext.setText("Copy Plaintext for Email");
		pmiCopyPlaintext.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				StringSelection stringSelection = new StringSelection(rec.toStringComplete());
				Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
				clipboard.setContents(stringSelection, null);
			}
		});
		pmnuRec.add(pmiCopyPlaintext);
		pmnuRec.add(pmiSep2);

		pmiCutRec.setAccelerator(
				KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_X, java.awt.event.InputEvent.CTRL_DOWN_MASK));
		pmiCutRec.setText("Cut");
		pmiCutRec.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				miCutActionPerformed(evt);
			}
		});
		pmnuRec.add(pmiCutRec);

		pmiCopyRec.setAccelerator(
				KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_C, java.awt.event.InputEvent.CTRL_DOWN_MASK));
		pmiCopyRec.setText("Copy");
		pmiCopyRec.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				miCopyActionPerformed(evt);
			}
		});
		pmnuRec.add(pmiCopyRec);

		pmiPasteRec.setAccelerator(
				KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_V, java.awt.event.InputEvent.CTRL_DOWN_MASK));
		pmiPasteRec.setText("Paste");
		pmiPasteRec.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				miPasteActionPerformed(evt);
			}
		});
		pmnuRec.add(pmiPasteRec);

		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
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

		details_panel.setBorder(BorderFactory.createTitledBorder("Recipe Details"));
		details_panel.setMaximumSize(null);
		details_panel.setLayout(new java.awt.GridLayout(1, 0));

		viewer_panel.setToolTipText("Steps to make recipe");
		viewer_panel.setLayout(new java.awt.BorderLayout());

		viewer_scroller.setMaximumSize(null);
		viewer_scroller.setMinimumSize(null);

		viewer_panel.add(viewer_scroller, java.awt.BorderLayout.CENTER);

		details_panel.add(viewer_panel);

		vSplitPane.setRightComponent(details_panel);

		recipeTree.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
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

		btnNew.setIcon(new ImageIcon(getClass().getResource("/images/new.gif"))); // NOI18N
		btnNew.setToolTipText("New");
		btnNew.setBorder(null);
		btnNew.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				newRecipeBookActionPerformed(evt);
			}
		});
		toolBar.add(btnNew);

		btnOpen.setIcon(new ImageIcon(getClass().getResource("/images/open.gif"))); // NOI18N
		btnOpen.setToolTipText("Open");
		btnOpen.setBorder(null);
		btnOpen.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				openActionPerformed(evt);
			}
		});
		toolBar.add(btnOpen);

		btnSave.setIcon(new ImageIcon(getClass().getResource("/images/save.gif"))); // NOI18N
		btnSave.setToolTipText("Save");
		btnSave.setBorder(null);
		btnSave.setEnabled(false);
		btnSave.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				saveActionPerformed(evt);
			}
		});
		toolBar.add(btnSave);

		btnPrint.setIcon(new ImageIcon(getClass().getResource("/images/print.gif"))); // NOI18N
		btnPrint.setToolTipText("Print");
		btnPrint.setBorder(null);
		btnPrint.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				// check selection in tree first
				if (rec != null) {
					printRecipe(rec);
				}
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

		miOpen.setAccelerator(
				KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_DOWN_MASK));
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

		miSave.setAccelerator(
				KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_DOWN_MASK));
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
				// check selection in tree first
				if (rec != null) {
					printRecipe(rec);
				}
			}
		});
		mnuRecipe.add(miPrintRecipe);

		miNewRecipe.setAccelerator(
				KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.CTRL_DOWN_MASK));
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

		miCut.setAccelerator(
				KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_X, java.awt.event.InputEvent.CTRL_DOWN_MASK));
		miCut.setText("Cut");
		miCut.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				miCutActionPerformed(evt);
			}
		});
		mnuEdit.add(miCut);

		miCopy.setAccelerator(
				KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_C, java.awt.event.InputEvent.CTRL_DOWN_MASK));
		miCopy.setText("Copy");
		miCopy.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				miCopyActionPerformed(evt);
			}
		});
		mnuEdit.add(miCopy);

		miPaste.setAccelerator(
				KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_V, java.awt.event.InputEvent.CTRL_DOWN_MASK));
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

	public void setAppFrameTitle() {
		setTitle(userPrefs.appName);
	}

	private void miPdfExportActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_miPdfExportActionPerformed
		exportRecPdf(null);
	}// GEN-LAST:event_miPdfExportActionPerformed

	private void miFilterActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_miFilterActionPerformed
		if (miFilter.isSelected() == true) {

			filterDialog.setFocusOnFilterText();
			filterDialog.setDialogLocation();
			filterDialog.setVisible(true);

			String filterText = filterDialog.getFilterText();
			List<String> filterFields = filterDialog.getFilterFields();
			if (filterText.length() > 0 && filterFields.size() > 0) {
				searchFind(filterFields, filterText);
				if (filter == null) {
					txtStatusBar.setText("Filter OFF");
					miFilter.setSelected(false);
				} else {
					txtStatusBar.setText("Filter ON");
				}
			} else {
				filter = null;
				RecipeTreeLoader bw = new RecipeTreeLoader();
				bw.loadTree(this.recipeTree, this.viewBy, this.filter, book);
				txtStatusBar.setText("Filter Off");
				miFilter.setSelected(false);
			}

		} else {
			filter = null;
			RecipeTreeLoader bw = new RecipeTreeLoader();
			bw.loadTree(this.recipeTree, this.viewBy, this.filter, book);
			txtStatusBar.setText("Filter Off");
		}
	}// GEN-LAST:event_miFilterActionPerformed

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
				fileWriter.saveFile(book, null, userPrefs.lastFileName, true);
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
		Action cutAction = TransferHandler.getCutAction();
		cutAction.actionPerformed(new ActionEvent(recipeTree, ActionEvent.ACTION_PERFORMED,
				(String) cutAction.getValue(Action.NAME), EventQueue.getMostRecentEventTime(), 0));
	}

	private void miDelRecipeActionPerformed(java.awt.event.ActionEvent evt) {
		deleteRecipe();
	}

	private void miViewBySourceActionPerformed(java.awt.event.ActionEvent evt) {
		setViewBy(viewByButtonGroup.getSelection().getActionCommand());
		RecipeTreeLoader bw = new RecipeTreeLoader();
		bw.loadTree(this.recipeTree, this.viewBy, this.filter, book);
	}

	private void miNewRecipeActionPerformed(java.awt.event.ActionEvent evt) {
		newRecipe();
	}

	private void miViewByRecipeActionPerformed(java.awt.event.ActionEvent evt) {
		setViewBy(viewByButtonGroup.getSelection().getActionCommand());
		RecipeTreeLoader bw = new RecipeTreeLoader();
		bw.loadTree(this.recipeTree, this.viewBy, this.filter, book);
	}

	private void miViewByOriginActionPerformed(java.awt.event.ActionEvent evt) {
		setViewBy(viewByButtonGroup.getSelection().getActionCommand());
		RecipeTreeLoader bw = new RecipeTreeLoader();
		bw.loadTree(this.recipeTree, this.viewBy, this.filter, book);
	}

	private void miViewByChapterActionPerformed(java.awt.event.ActionEvent evt) {
		setViewBy(viewByButtonGroup.getSelection().getActionCommand());
		RecipeTreeLoader bw = new RecipeTreeLoader();
		bw.loadTree(this.recipeTree, this.viewBy, this.filter, book);
	}

	private void miOpenRecipeActionPerformed(java.awt.event.ActionEvent evt) {
		openRecipe();
	}

	private void mainFrameClosing(java.awt.event.WindowEvent evt) {
		exitApp();
	}

	private void RecipeTreePopupHandler(java.awt.event.MouseEvent evt) {
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
				userPrefs.lastFileName = filePath;
				fileHistory.insertPathname(userPrefs.lastFileName);
				this.setTitle(userPrefs.appName + " - [" + userPrefs.lastFileName + "]");
				book.setModified(false);
				saveButtons(book.getModified());
			}
		}
	}

	private void saveActionPerformed(java.awt.event.ActionEvent evt) {
		fileWriter.saveFile(book, null, userPrefs.lastFileName, true);
		this.setTitle(userPrefs.appName + " - [" + userPrefs.lastFileName + "]");
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
				fileWriter.saveFile(book, null, userPrefs.lastFileName, true);
			} else {
				logger.info("Unknown return status from SaveOptionFrame");
				return;
			}
		}

		String fileName = fileReader.browseFileSystem(AppFrame.this);
		if (fileName != null) {
			if (openBook(fileName)) {
				fileHistory.insertPathname(userPrefs.lastFileName);
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

		setIconImage(Toolkit.getDefaultToolkit().createImage(getClass().getResource("/images/appIcon.gif")));

	}

	public void loadGlobals() {
		userPrefs.loadAll();
		if (userPrefs.lastFileName.length() > 0) {
			if (openBook(userPrefs.lastFileName)) {
				this.setTitle(userPrefs.appName + " - [" + userPrefs.lastFileName + "]");
			}
		}
		setLocation(userPrefs.LocX, userPrefs.LocY);
		setSize(userPrefs.SizeW, userPrefs.SizeH);
	}

	public void saveGlobals() {
		Point coordPt = this.getLocation();
		userPrefs.LocX = (int) coordPt.getX();
		userPrefs.LocY = (int) coordPt.getY();

		Dimension appDim = getSize();
		userPrefs.SizeH = appDim.height;
		userPrefs.SizeW = appDim.width;

		userPrefs.saveAll();
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
				fileWriter.saveFile(book, null, userPrefs.lastFileName, true);
			} else {
				logger.info("Unknown return status from SaveOptionFrame");
				return;
			}
		}

		book = new Book();
		miFilter.setSelected(false);
		filter = null;
		book.setBookName("New Recipe Book");
		userPrefs.lastFileName = "";
		this.setTitle(userPrefs.appName + " - [New Book]");
		book.setModified(true);
		saveButtons(book.getModified());
		Recipe r = new Recipe();
		r.setRecipeName("Blank Recipe");
		book.addRecipe(r);

		RecipeTreeLoader bw = new RecipeTreeLoader();
		bw.loadTree(this.recipeTree, this.viewBy, this.filter, book);
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
			RecipeTreeLoader bw = new RecipeTreeLoader();
			bw.loadTree(this.recipeTree, this.viewBy, this.filter, book);
			this.recipeTree.revalidate();

			// Save recently opened files
			userPrefs.lastFileName = path;
			setTitle(userPrefs.appName + " - [" + userPrefs.lastFileName + "]");
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
		userPrefs.lastFileName = argFileName;
	}

	public String getLastFileName() {
		return userPrefs.lastFileName;
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
			RecipeTreeLoader bw = new RecipeTreeLoader();
			bw.loadTree(this.recipeTree, this.viewBy, this.filter, book);
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
				fileWriter.saveFile(book, null, userPrefs.lastFileName, true);
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
		// re-index for filter
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
		RecipeTreeLoader bw = new RecipeTreeLoader();
		bw.loadTree(this.recipeTree, this.viewBy, this.filter, book);

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

		if (userPrefs.lastFileName.startsWith("http://")) {
			btnSave.setEnabled(false);
			miSave.setEnabled(false);
		} else if (userPrefs.lastFileName.equals("")) {
			btnSave.setEnabled(false);
			miSave.setEnabled(false);
		} else {
			btnSave.setEnabled(value);
			miSave.setEnabled(value);
			if (value == true) {
				this.setTitle(userPrefs.appName + " - [" + userPrefs.lastFileName + "*]");
			} else {
				this.setTitle(userPrefs.appName + " - [" + userPrefs.lastFileName + "]");
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
		return userPrefs.appName;
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
				fileWriter.saveFile(book, null, userPrefs.lastFileName, true);
			} else {
				logger.info("Unknown return status from SaveOptionFrame");
				return false;
			}
		}
		return openBook(path);
	}

	public void pdfView(Recipe r) {
		JasperViewer.viewReport(this.jasperCompiler.getJasperPrint(book, r), false);
	}

	public void exportRecPdf(Recipe recArg) {
		String filePath = fileWriter.browseFileSystem(AppFrame.this, "PDF");
		if (filePath != null && fileWriter.saveFile(book, recArg, filePath, true) == true) {
			if (fileWriter.getLastWriter().isReadable()) {
				userPrefs.lastFileName = filePath;
				fileHistory.insertPathname(userPrefs.lastFileName);
				this.setTitle(userPrefs.appName + " - [" + userPrefs.lastFileName + "]");
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
					viewer_scroller.setViewportView(new MyJRViewer(jasperCompiler.getJasperPrint(book, rec)));
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
				pmnuRec.show((JComponent) e.getSource(), e.getX(), e.getY());
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
