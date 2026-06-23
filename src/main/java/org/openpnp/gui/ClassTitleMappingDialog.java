package org.openpnp.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.table.AbstractTableModel;
import org.openpnp.ClassTitleRegistry;
import org.openpnp.ClassTitleRegistry.ClassMapping;
import org.openpnp.ClassTitleRegistry.TextMapping;
import org.openpnp.Translations;

public class ClassTitleMappingDialog extends JDialog {
    
    private ClassMappingTableModel classTableModel;
    private JTable classTable;
    private JTextField classSearchTextField;
    private List<ClassMapping> allClassMappings;
    
    private TextMappingTableModel textTableModel;
    private JTable textTable;
    private JTextField textSearchTextField;
    private List<TextMapping> allTextMappings;
    
    private JComboBox<String> languageComboBox;
    private JCheckBox enableMappingCheckBox;
    private JTabbedPane tabbedPane;
    private JButton setLanguageButton;
    private JButton saveButton;
    private JButton okButton;
    private JLabel searchLabel;
    private JLabel searchLabel2;
    private JLabel langLabel;
    
    private static final String[] CLASS_COLUMN_KEYS = {
        "ClassTitleMappingDialog.column.id",
        "ClassTitleMappingDialog.column.simpleName",
        "ClassTitleMappingDialog.column.englishTitle",
        "ClassTitleMappingDialog.column.chineseTitle",
        "ClassTitleMappingDialog.column.category",
        "ClassTitleMappingDialog.column.appendName"
    };
    
    private static final String[] TEXT_COLUMN_KEYS = {
        "ClassTitleMappingDialog.column.id",
        "ClassTitleMappingDialog.column.originalText",
        "ClassTitleMappingDialog.column.englishTranslation",
        "ClassTitleMappingDialog.column.chineseTranslation"
    };
    
    public ClassTitleMappingDialog(Frame frame) {
        super(frame, true);
        allClassMappings = ClassTitleRegistry.getAllMappings();
        allTextMappings = ClassTitleRegistry.getAllTextMappings();
        createUi();
        updateUiLanguage();
    }
    
    private void createUi() {
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 1200, 700);
        getContentPane().setLayout(new BorderLayout());
        
        // Top panel with search, language, and enable toggle
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        topPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        // Enable mapping toggle
        enableMappingCheckBox = new JCheckBox();
        enableMappingCheckBox.setSelected(ClassTitleRegistry.isMappingEnabled());
        enableMappingCheckBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                ClassTitleRegistry.setMappingEnabled(enableMappingCheckBox.isSelected());
            }
        });
        topPanel.add(enableMappingCheckBox);
        
        langLabel = new JLabel();
        topPanel.add(langLabel);
        
        String[] languages = {"English", "Chinese (Simplified)"};
        languageComboBox = new JComboBox<>(languages);
        String currentLang = Translations.getLanguage();
        if ("zh_CN".equals(currentLang)) {
            languageComboBox.setSelectedIndex(1);
        } else {
            languageComboBox.setSelectedIndex(0);
        }
        topPanel.add(languageComboBox);
        
        getContentPane().add(topPanel, BorderLayout.NORTH);
        
        // Tabbed pane for class mapping and text mapping
        tabbedPane = new JTabbedPane();
        
        // ==== Class Mapping Tab ====
        JPanel classPanel = new JPanel(new BorderLayout());
        
        JPanel classSearchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchLabel = new JLabel();
        classSearchTextField = new JTextField(30);
        classSearchTextField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                filterClassTable();
            }
        });
        classSearchPanel.add(searchLabel);
        classSearchPanel.add(classSearchTextField);
        classPanel.add(classSearchPanel, BorderLayout.NORTH);
        
        classTableModel = new ClassMappingTableModel(allClassMappings);
        classTable = new JTable(classTableModel);
        JScrollPane classScrollPane = new JScrollPane(classTable);
        classPanel.add(classScrollPane, BorderLayout.CENTER);
        
        tabbedPane.addTab("", classPanel);
        
        // ==== Text Mapping Tab ====
        JPanel textPanel = new JPanel(new BorderLayout());
        
        JPanel textSearchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchLabel2 = new JLabel();
        textSearchTextField = new JTextField(30);
        textSearchTextField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                filterTextTable();
            }
        });
        textSearchPanel.add(searchLabel2);
        textSearchPanel.add(textSearchTextField);
        textPanel.add(textSearchPanel, BorderLayout.NORTH);
        
        textTableModel = new TextMappingTableModel(allTextMappings);
        textTable = new JTable(textTableModel);
        JScrollPane textScrollPane = new JScrollPane(textTable);
        textPanel.add(textScrollPane, BorderLayout.CENTER);
        
        tabbedPane.addTab("", textPanel);
        
        getContentPane().add(tabbedPane, BorderLayout.CENTER);
        
        // Button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        getContentPane().add(buttonPanel, BorderLayout.SOUTH);
        
        setLanguageButton = new JButton();
        setLanguageButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                int index = languageComboBox.getSelectedIndex();
                if (index == 1) {
                    Translations.setLanguage("zh_CN");
                } else {
                    Translations.setLanguage("en_US");
                }
                updateUiLanguage();
            }
        });
        buttonPanel.add(setLanguageButton);
        
        saveButton = new JButton();
        saveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                ClassTitleRegistry.saveAll();
            }
        });
        buttonPanel.add(saveButton);
        
        okButton = new JButton();
        okButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                ClassTitleRegistry.saveAll();
                setVisible(false);
            }
        });
        buttonPanel.add(okButton);
        getRootPane().setDefaultButton(okButton);
    }
    
    private void updateUiLanguage() {
        setTitle(Translations.getString("ClassTitleMappingDialog.title"));
        enableMappingCheckBox.setText(Translations.getString("ClassTitleMappingDialog.enableMapping"));
        searchLabel.setText(Translations.getString("ClassTitleMappingDialog.search"));
        searchLabel2.setText(Translations.getString("ClassTitleMappingDialog.search"));
        langLabel.setText("  " + Translations.getString("ClassTitleMappingDialog.language.english") + "/" 
                + Translations.getString("ClassTitleMappingDialog.language.chinese") + ":  ");
        setLanguageButton.setText(Translations.getString("ClassTitleMappingDialog.setLanguage"));
        saveButton.setText(Translations.getString("ClassTitleMappingDialog.saveAll"));
        okButton.setText(Translations.getString("ClassTitleMappingDialog.ok"));
        
        tabbedPane.setTitleAt(0, Translations.getString("ClassTitleMappingDialog.tab.classMapping"));
        tabbedPane.setTitleAt(1, Translations.getString("ClassTitleMappingDialog.tab.textMapping"));
        
        // Refresh language combo selection
        String currentLang = Translations.getLanguage();
        if ("zh_CN".equals(currentLang)) {
            languageComboBox.setSelectedIndex(1);
        } else {
            languageComboBox.setSelectedIndex(0);
        }
        
        // Refresh table headers
        classTableModel.fireTableStructureChanged();
        textTableModel.fireTableStructureChanged();
    }
    
    private void filterClassTable() {
        String searchText = classSearchTextField.getText().toLowerCase();
        List<ClassMapping> filteredMappings = new ArrayList<>();
        
        for (ClassMapping mapping : allClassMappings) {
            if (mapping.className.toLowerCase().contains(searchText) ||
                mapping.simpleName.toLowerCase().contains(searchText) ||
                mapping.englishTitle.toLowerCase().contains(searchText) ||
                mapping.chineseTitle.contains(searchText) ||
                mapping.id.toLowerCase().contains(searchText) ||
                mapping.category.toLowerCase().contains(searchText)) {
                filteredMappings.add(mapping);
            }
        }
        
        classTableModel.setMappings(filteredMappings);
    }
    
    private void filterTextTable() {
        String searchText = textSearchTextField.getText().toLowerCase();
        List<TextMapping> filteredMappings = new ArrayList<>();
        
        for (TextMapping mapping : allTextMappings) {
            if (mapping.text.toLowerCase().contains(searchText) ||
                mapping.englishTranslation.toLowerCase().contains(searchText) ||
                mapping.chineseTranslation.contains(searchText) ||
                mapping.id.toLowerCase().contains(searchText)) {
                filteredMappings.add(mapping);
            }
        }
        
        textTableModel.setMappings(filteredMappings);
    }
    
    // ====== Class Mapping Table Model ======
    public class ClassMappingTableModel extends AbstractTableModel {
        private List<ClassMapping> mappings;
        
        public ClassMappingTableModel(List<ClassMapping> mappings) {
            this.mappings = new ArrayList<>(mappings);
        }
        
        public void setMappings(List<ClassMapping> mappings) {
            this.mappings = new ArrayList<>(mappings);
            fireTableDataChanged();
        }
        
        @Override
        public String getColumnName(int column) {
            if (column >= 0 && column < CLASS_COLUMN_KEYS.length) {
                return Translations.getString(CLASS_COLUMN_KEYS[column]);
            }
            return "";
        }
        
        @Override
        public int getColumnCount() {
            return CLASS_COLUMN_KEYS.length;
        }
        
        @Override
        public int getRowCount() {
            return mappings.size();
        }
        
        @Override
        public Class<?> getColumnClass(int columnIndex) {
            if (columnIndex == 5) {
                return Boolean.class;
            }
            return String.class;
        }
        
        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return false;
        }
        
        @Override
        public Object getValueAt(int row, int column) {
            ClassMapping mapping = mappings.get(row);
            switch (column) {
                case 0: return mapping.id;
                case 1: return mapping.simpleName;
                case 2: return mapping.englishTitle;
                case 3: return mapping.chineseTitle;
                case 4: return mapping.category;
                case 5: return mapping.appendName;
                default: return null;
            }
        }
        
        @Override
        public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        }
    }
    
    // ====== Text Mapping Table Model ======
    public class TextMappingTableModel extends AbstractTableModel {
        private List<TextMapping> mappings;
        
        public TextMappingTableModel(List<TextMapping> mappings) {
            this.mappings = new ArrayList<>(mappings);
        }
        
        public void setMappings(List<TextMapping> mappings) {
            this.mappings = new ArrayList<>(mappings);
            fireTableDataChanged();
        }
        
        @Override
        public String getColumnName(int column) {
            if (column >= 0 && column < TEXT_COLUMN_KEYS.length) {
                return Translations.getString(TEXT_COLUMN_KEYS[column]);
            }
            return "";
        }
        
        @Override
        public int getColumnCount() {
            return TEXT_COLUMN_KEYS.length;
        }
        
        @Override
        public int getRowCount() {
            return mappings.size();
        }
        
        @Override
        public Class<?> getColumnClass(int columnIndex) {
            return String.class;
        }
        
        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return false;
        }
        
        @Override
        public Object getValueAt(int row, int column) {
            TextMapping mapping = mappings.get(row);
            switch (column) {
                case 0: return mapping.id;
                case 1: return mapping.text;
                case 2: return mapping.englishTranslation;
                case 3: return mapping.chineseTranslation;
                default: return null;
            }
        }
        
        @Override
        public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        }
    }
}