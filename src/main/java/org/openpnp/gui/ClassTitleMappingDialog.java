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
import java.util.prefs.Preferences;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.table.AbstractTableModel;
import org.openpnp.ClassTitleRegistry;
import org.openpnp.ClassTitleRegistry.Mapping;
import org.openpnp.ClassTitleRegistry.MappingType;
import org.openpnp.Translations;

public class ClassTitleMappingDialog extends JDialog {

    private MappingTableModel tableModel;
    private JTable mappingTable;
    private JTextField searchTextField;
    private List<Mapping> allMappings;

    private JComboBox<String> languageComboBox;
    private JCheckBox enableMappingCheckBox;
    private JButton setLanguageButton;
    private JButton saveButton;
    private JButton okButton;
    private JLabel searchLabel;
    private JLabel langLabel;

    private static final String[] COLUMN_KEYS = {
        "ClassTitleMappingDialog.column.id",
        "ClassTitleMappingDialog.column.type",
        "ClassTitleMappingDialog.column.source",
        "ClassTitleMappingDialog.column.englishTitle",
        "ClassTitleMappingDialog.column.chineseTitle",
        "ClassTitleMappingDialog.column.category"
    };

    public ClassTitleMappingDialog(Frame frame) {
        super(frame, true);
        allMappings = ClassTitleRegistry.getAllMappings();
        createUi();
        loadColumnWidths();
        updateUiLanguage();
    }

    private void createUi() {
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 1400, 700);
        getContentPane().setLayout(new BorderLayout());

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        topPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        enableMappingCheckBox = new JCheckBox();
        enableMappingCheckBox.setSelected(ClassTitleRegistry.isMappingEnabled());
        enableMappingCheckBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                ClassTitleRegistry.setMappingEnabled(enableMappingCheckBox.isSelected());
                refreshMainUi();
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

        JPanel mainPanel = new JPanel(new BorderLayout());

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchLabel = new JLabel();
        searchTextField = new JTextField(50);
        searchTextField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                filterTable();
            }
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    e.consume();
                }
            }
        });
        searchPanel.add(searchLabel);
        searchPanel.add(searchTextField);
        mainPanel.add(searchPanel, BorderLayout.NORTH);

        tableModel = new MappingTableModel(allMappings);
        mappingTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(mappingTable);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        getContentPane().add(mainPanel, BorderLayout.CENTER);

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
                refreshMainUi();
            }
        });
        buttonPanel.add(setLanguageButton);

        saveButton = new JButton();
        saveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                saveColumnWidths();
                ClassTitleRegistry.saveAll();
                refreshMainUi();
            }
        });
        buttonPanel.add(saveButton);

        okButton = new JButton();
        okButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                saveColumnWidths();
                ClassTitleRegistry.saveAll();
                refreshMainUi();
                setVisible(false);
            }
        });
        buttonPanel.add(okButton);
        getRootPane().setDefaultButton(okButton);
    }

    private void loadColumnWidths() {
        Preferences prefs = Preferences.userNodeForPackage(ClassTitleMappingDialog.class);
        for (int i = 0; i < mappingTable.getColumnCount(); i++) {
            int width = prefs.getInt("mappingTable.colWidth." + i, -1);
            if (width > 0) {
                mappingTable.getColumnModel().getColumn(i).setPreferredWidth(width);
            }
        }
    }

    private void saveColumnWidths() {
        Preferences prefs = Preferences.userNodeForPackage(ClassTitleMappingDialog.class);
        for (int i = 0; i < mappingTable.getColumnCount(); i++) {
            int width = mappingTable.getColumnModel().getColumn(i).getWidth();
            prefs.putInt("mappingTable.colWidth." + i, width);
        }
    }

    private void updateUiLanguage() {
        setTitle(Translations.getString("ClassTitleMappingDialog.title"));
        enableMappingCheckBox.setText(Translations.getString("ClassTitleMappingDialog.enableMapping"));
        searchLabel.setText(Translations.getString("ClassTitleMappingDialog.search"));
        langLabel.setText("  " + Translations.getString("ClassTitleMappingDialog.language.english") + "/"
                + Translations.getString("ClassTitleMappingDialog.language.chinese") + ":  ");
        setLanguageButton.setText(Translations.getString("ClassTitleMappingDialog.setLanguage"));
        saveButton.setText(Translations.getString("ClassTitleMappingDialog.saveAll"));
        okButton.setText(Translations.getString("ClassTitleMappingDialog.ok"));

        String currentLang = Translations.getLanguage();
        if ("zh_CN".equals(currentLang)) {
            languageComboBox.setSelectedIndex(1);
        } else {
            languageComboBox.setSelectedIndex(0);
        }

        tableModel.fireTableStructureChanged();
        loadColumnWidths();
    }

    private void refreshMainUi() {
        MainFrame mainFrame = MainFrame.get();
        if (mainFrame != null && mainFrame.getMachineSetupTab() != null) {
            mainFrame.getMachineSetupTab().refreshTree();
        }
    }

    private void filterTable() {
        String searchText = searchTextField.getText().toLowerCase();
        List<Mapping> filteredMappings = new ArrayList<>();

        for (Mapping mapping : allMappings) {
            boolean matches = false;
            if (mapping.id.toLowerCase().contains(searchText)) {
                matches = true;
            }
            if (!matches && mapping.source.toLowerCase().contains(searchText)) {
                matches = true;
            }
            if (!matches && mapping.englishTitle.toLowerCase().contains(searchText)) {
                matches = true;
            }
            if (!matches && mapping.chineseTitle.contains(searchText)) {
                matches = true;
            }
            if (!matches && mapping.category.toLowerCase().contains(searchText)) {
                matches = true;
            }
            if (!matches && mapping.type.name().toLowerCase().contains(searchText)) {
                matches = true;
            }
            if (!matches && mapping.simpleName != null && mapping.simpleName.toLowerCase().contains(searchText)) {
                matches = true;
            }
            
            if (matches) {
                filteredMappings.add(mapping);
            }
        }

        tableModel.setMappings(filteredMappings);
    }

    public class MappingTableModel extends AbstractTableModel {
        private List<Mapping> mappings;

        public MappingTableModel(List<Mapping> mappings) {
            this.mappings = new ArrayList<>(mappings);
        }

        public void setMappings(List<Mapping> mappings) {
            this.mappings = new ArrayList<>(mappings);
            fireTableDataChanged();
        }

        @Override
        public String getColumnName(int column) {
            if (column >= 0 && column < COLUMN_KEYS.length) {
                return Translations.getString(COLUMN_KEYS[column]);
            }
            return "";
        }

        @Override
        public int getColumnCount() {
            return COLUMN_KEYS.length;
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
            return columnIndex >= 3;
        }

        @Override
        public Object getValueAt(int row, int column) {
            Mapping mapping = mappings.get(row);
            switch (column) {
                case 0: return mapping.id.startsWith("ID_") ? mapping.id.substring(3) : mapping.id;
                case 1: return getTypeDisplayName(mapping.type);
                case 2: return getSourceDisplay(mapping);
                case 3: return mapping.englishTitle;
                case 4: return mapping.chineseTitle;
                case 5: return mapping.category;
                default: return null;
            }
        }

        @Override
        public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
            Mapping mapping = mappings.get(rowIndex);
            switch (columnIndex) {
                case 3:
                    mapping.englishTitle = (String) aValue;
                    break;
                case 4:
                    mapping.chineseTitle = (String) aValue;
                    break;
                case 5:
                    mapping.category = (String) aValue;
                    break;
            }
            fireTableCellUpdated(rowIndex, columnIndex);
        }

        private String getTypeDisplayName(MappingType type) {
            switch (type) {
                case CLASS: return Translations.getString("ClassTitleMappingDialog.type.class");
                case TEXT: return Translations.getString("ClassTitleMappingDialog.type.text");
                case PATTERN: return Translations.getString("ClassTitleMappingDialog.type.pattern");
                default: return type.name();
            }
        }

        private String getSourceDisplay(Mapping mapping) {
            if (mapping.type == MappingType.CLASS) {
                return mapping.simpleName != null ? mapping.simpleName : mapping.source;
            }
            return mapping.source;
        }
    }
}