package org.openpnp;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.prefs.Preferences;

import org.openpnp.model.Identifiable;
import org.openpnp.model.Named;

/**
 * Class/Text title mapping registry.
 * 
 * <p>Mappings are loaded from classpath resource files (class-mappings.properties,
 * text-mappings.properties, text-mapping-patterns.properties) as defaults, then
 * overridden from user-local files in {@code ~/.openpnp/mappings/}. Changes made
 * through the UI are saved back to the user-local files, so they persist across
 * builds without recompilation.</p>
 */
public class ClassTitleRegistry {

    public static class ClassMapping {
        public final String id;
        public final String className;
        public final String simpleName;
        public final String translationKey;
        public String englishTitle;
        public String chineseTitle;
        public boolean appendName;
        public String category;

        public ClassMapping(String id, String className, String simpleName,
                String translationKey, String englishTitle, String chineseTitle,
                boolean appendName, String category) {
            this.id = id;
            this.className = className;
            this.simpleName = simpleName;
            this.translationKey = translationKey;
            this.englishTitle = englishTitle;
            this.chineseTitle = chineseTitle;
            this.appendName = appendName;
            this.category = category;
        }
    }

    public static class TextMapping {
        public final String id;
        public final String text;
        public String englishTranslation;
        public String chineseTranslation;

        public TextMapping(String id, String text, String englishTranslation, String chineseTranslation) {
            this.id = id;
            this.text = text;
            this.englishTranslation = englishTranslation;
            this.chineseTranslation = chineseTranslation;
        }
    }

    public static class TextMappingPattern {
        public final String id;
        public final java.util.regex.Pattern pattern;
        public final String englishTemplate;
        public final String chineseTemplate;

        public TextMappingPattern(String id, String regex, String englishTemplate, String chineseTemplate) {
            this.id = id;
            this.pattern = java.util.regex.Pattern.compile(regex);
            this.englishTemplate = englishTemplate;
            this.chineseTemplate = chineseTemplate;
        }
    }

    private static final List<ClassMapping> mappings = new ArrayList<>();
    private static final Map<String, ClassMapping> mappingsByClassName = new HashMap<>();
    private static final Map<String, ClassMapping> mappingsBySimpleName = new HashMap<>();
    private static final Map<String, ClassMapping> mappingsById = new HashMap<>();
    private static final Preferences prefs = Preferences.userNodeForPackage(ClassTitleRegistry.class);

    private static final List<TextMapping> textMappings = new ArrayList<>();
    private static final Map<String, TextMapping> textMappingsByText = new HashMap<>();
    private static final Map<String, TextMapping> textMappingsById = new HashMap<>();

    private static final List<TextMappingPattern> textMappingPatterns = new ArrayList<>();

    private static final String PREF_MAPPING_ENABLED = "mappingEnabled";

    /** Subdirectory under user.home/.openpnp/ where user mapping overrides are stored. */
    private static final String USER_MAPPINGS_DIR = ".openpnp" + File.separator + "mappings";

    /** Classpath resource names. */
    private static final String CLASS_MAPPINGS_RESOURCE = "class-mappings.properties";
    private static final String TEXT_MAPPINGS_RESOURCE = "text-mappings.properties";
    private static final String TEXT_PATTERNS_RESOURCE = "text-mapping-patterns.properties";

    public static boolean isMappingEnabled() {
        return prefs.getBoolean(PREF_MAPPING_ENABLED, false);
    }

    public static void setMappingEnabled(boolean enabled) {
        prefs.putBoolean(PREF_MAPPING_ENABLED, enabled);
        try {
            prefs.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // -----------------------------------------------------------
    // Initialization – load from classpath defaults + user overrides
    // -----------------------------------------------------------
    static {
        loadAll();
    }

    /** Full reload from both classpath defaults and user override files. */
    private static void loadAll() {
        mappings.clear();
        mappingsByClassName.clear();
        mappingsBySimpleName.clear();
        mappingsById.clear();
        textMappings.clear();
        textMappingsByText.clear();
        textMappingsById.clear();
        textMappingPatterns.clear();

        // 1. Load classpath defaults
        loadClasspathDefaults();

        // 2. Override with user-local files
        loadUserOverrides();
    }

    private static void loadClasspathDefaults() {
        // Class mappings
        loadClassMappingsFromStream(
                ClassTitleRegistry.class.getResourceAsStream(CLASS_MAPPINGS_RESOURCE),
                false);
        // Text mappings
        loadTextMappingsFromStream(
                ClassTitleRegistry.class.getResourceAsStream(TEXT_MAPPINGS_RESOURCE));
        // Text mapping patterns
        loadPatternsFromStream(
                ClassTitleRegistry.class.getResourceAsStream(TEXT_PATTERNS_RESOURCE));
    }

    private static File getUserMappingsDir() {
        return new File(System.getProperty("user.home"), USER_MAPPINGS_DIR);
    }

    /** Returns the user-override properties file for the given resource name. */
    private static File getUserOverrideFile(String resourceName) {
        return new File(getUserMappingsDir(), resourceName);
    }

    private static void loadUserOverrides() {
        File dir = getUserMappingsDir();
        if (!dir.isDirectory()) {
            return; // no overrides yet
        }
        File classFile = new File(dir, CLASS_MAPPINGS_RESOURCE);
        if (classFile.isFile()) {
            try (FileInputStream fis = new FileInputStream(classFile)) {
                loadClassMappingsFromStream(fis, true);
            } catch (Exception e) {
                System.err.println("Failed to load user class mappings: " + e.getMessage());
            }
        }
        File textFile = new File(dir, TEXT_MAPPINGS_RESOURCE);
        if (textFile.isFile()) {
            try (FileInputStream fis = new FileInputStream(textFile)) {
                loadTextMappingsFromStream(fis);
            } catch (Exception e) {
                System.err.println("Failed to load user text mappings: " + e.getMessage());
            }
        }
        File patternFile = new File(dir, TEXT_PATTERNS_RESOURCE);
        if (patternFile.isFile()) {
            try (FileInputStream fis = new FileInputStream(patternFile)) {
                loadPatternsFromStream(fis);
            } catch (Exception e) {
                System.err.println("Failed to load user text patterns: " + e.getMessage());
            }
        }
    }

    /**
     * Load class mappings from an input stream.
     * <p>When {@code isOverride == true}, existing mappings with the same ID
     * are replaced (update mode). Otherwise mappings are simply added.</p>
     */
    private static void loadClassMappingsFromStream(java.io.InputStream is, boolean isOverride) {
        if (is == null) return;
        try (BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) continue;
                // Format: PID|className|simpleName|englishTitle|chineseTitle|appendName|category
                String[] parts = line.split("\\|", 8);
                if (parts.length < 7) continue;
                String pid = "ID_" + parts[0];
                String className = parts[1];
                String simpleName = parts[2];
                String englishTitle = parts[3];
                String chineseTitle = parts[4];
                boolean appendName = Boolean.parseBoolean(parts[5]);
                String category = parts.length > 6 ? parts[6] : "";

                if (isOverride) {
                    // Replace existing mapping with same ID
                    ClassMapping existing = mappingsById.get(pid);
                    if (existing != null) {
                        existing.englishTitle = englishTitle;
                        existing.chineseTitle = chineseTitle;
                        existing.appendName = appendName;
                        existing.category = category;
                        continue;
                    }
                }
                ClassMapping mapping = new ClassMapping(pid, className, simpleName,
                        "ClassTitle." + pid, englishTitle, chineseTitle, appendName, category);
                addMapping(mapping);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void loadTextMappingsFromStream(java.io.InputStream is) {
        if (is == null) return;
        try (BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) continue;
                // Format: PID|originalText|englishTranslation|chineseTranslation
                String[] parts = line.split("\\|", 4);
                if (parts.length < 4) continue;
                String pid = "ID_" + parts[0];
                String originalText = parts[1];
                String englishTranslation = parts[2];
                String chineseTranslation = parts[3];

                TextMapping mapping = new TextMapping(pid, originalText, englishTranslation, chineseTranslation);
                addTextMapping(mapping);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void loadPatternsFromStream(java.io.InputStream is) {
        if (is == null) return;
        try (BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) continue;
                // Format: PID|regex|englishTemplate|chineseTemplate
                String[] parts = line.split("\\|", 4);
                if (parts.length < 4) continue;
                String pid = "ID_" + parts[0];
                String regex = parts[1];
                String englishTemplate = parts[2];
                String chineseTemplate = parts[3];

                addTextMappingPattern(new TextMappingPattern(pid, regex, englishTemplate, chineseTemplate));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // -----------------------------------------------------------
    // Save – write user override files
    // -----------------------------------------------------------

    /** Persist all current mappings to user-local files. */
    public static void saveAll() {
        File dir = getUserMappingsDir();
        try {
            Files.createDirectories(dir.toPath());
        } catch (Exception e) {
            System.err.println("Cannot create mappings directory: " + e.getMessage());
            return;
        }
        saveClassMappings();
        saveTextMappings();
        savePatterns();
    }

    private static void saveClassMappings() {
        File file = getUserOverrideFile(CLASS_MAPPINGS_RESOURCE);
        try (BufferedWriter bw = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8))) {
            bw.write("# Class Mappings (user overrides)\n");
            bw.write("# Format: PID|className|simpleName|englishTitle|chineseTitle|appendName|category\n");
            for (ClassMapping m : mappings) {
                String pid = m.id.startsWith("ID_") ? m.id.substring(3) : m.id;
                bw.write(String.format("%s|%s|%s|%s|%s|%s|%s\n",
                        pid, m.className, m.simpleName,
                        m.englishTitle, m.chineseTitle, m.appendName, m.category));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void saveTextMappings() {
        File file = getUserOverrideFile(TEXT_MAPPINGS_RESOURCE);
        try (BufferedWriter bw = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8))) {
            bw.write("# Text Mappings (user overrides)\n");
            bw.write("# Format: PID|originalText|englishTranslation|chineseTranslation\n");
            for (TextMapping m : textMappings) {
                String pid = m.id.startsWith("ID_") ? m.id.substring(3) : m.id;
                bw.write(String.format("%s|%s|%s|%s\n",
                        pid, m.text, m.englishTranslation, m.chineseTranslation));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void savePatterns() {
        File file = getUserOverrideFile(TEXT_PATTERNS_RESOURCE);
        try (BufferedWriter bw = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8))) {
            bw.write("# Text Mapping Patterns (user overrides)\n");
            bw.write("# Format: PID|regex|englishTemplate|chineseTemplate\n");
            for (TextMappingPattern p : textMappingPatterns) {
                String pid = p.id.startsWith("ID_") ? p.id.substring(3) : p.id;
                bw.write(String.format("%s|%s|%s|%s\n",
                        pid, p.pattern.pattern(), p.englishTemplate, p.chineseTemplate));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // -----------------------------------------------------------
    // Internal add helpers
    // -----------------------------------------------------------

    private static void addMapping(ClassMapping mapping) {
        mappings.add(mapping);
        mappingsByClassName.put(mapping.className, mapping);
        mappingsBySimpleName.put(mapping.simpleName, mapping);
        mappingsById.put(mapping.id, mapping);
    }

    private static void addTextMapping(TextMapping mapping) {
        textMappings.add(mapping);
        textMappingsByText.put(mapping.text, mapping);
        textMappingsById.put(mapping.id, mapping);
    }

    private static void addTextMappingPattern(TextMappingPattern pattern) {
        textMappingPatterns.add(pattern);
    }

    // -----------------------------------------------------------
    // Public query API
    // -----------------------------------------------------------

    public static List<ClassMapping> getAllMappings() {
        return new ArrayList<>(mappings);
    }

    public static List<TextMapping> getAllTextMappings() {
        return new ArrayList<>(textMappings);
    }

    public static List<TextMappingPattern> getAllTextMappingPatterns() {
        return new ArrayList<>(textMappingPatterns);
    }

    public static ClassMapping getMapping(Class<?> clazz) {
        ClassMapping mapping = mappingsByClassName.get(clazz.getName());
        if (mapping == null) {
            mapping = mappingsBySimpleName.get(clazz.getSimpleName());
        }
        return mapping;
    }

    public static ClassMapping getMappingById(String id) {
        return mappingsById.get(id);
    }

    public static TextMapping getTextMappingById(String id) {
        return textMappingsById.get(id);
    }

    private static TextMapping getTextMappingByText(String text) {
        return textMappingsByText.get(text);
    }

    /**
     * Get the display title for an object, with PID prefix when mapping is enabled.
     */
    public static String getTitle(Object obj) {
        if (obj == null) {
            return "";
        }
        if (!isMappingEnabled()) {
            return getDefaultTitle(obj);
        }
        ClassMapping mapping = getMapping(obj.getClass());
        if (mapping == null) {
            return getDefaultTitle(obj);
        }
        String currentLanguage = Translations.getLanguage();
        String title = "zh_CN".equals(currentLanguage) ? mapping.chineseTitle : mapping.englishTitle;
        if (mapping.appendName && obj instanceof Named) {
            String name = ((Named) obj).getName();
            if (name != null && !name.isEmpty()) {
                title = title + " " + name;
            }
        }
        return "(PID:" + mapping.id.substring(3) + ") " + title;
    }

    /**
     * Convenience: get the untranslated default title for an object.
     */
    private static String getDefaultTitle(Object obj) {
        String simpleName = obj.getClass().getSimpleName();
        if (obj instanceof Named) {
            String name = ((Named) obj).getName();
            if (name != null && !name.isEmpty()) {
                return simpleName + " " + name;
            }
        }
        if (obj instanceof Identifiable) {
            String id = ((Identifiable) obj).getId();
            if (id != null && !id.isEmpty()) {
                return simpleName + " " + id;
            }
        }
        return simpleName;
    }

    /**
     * Maps a text string (issue description / solution) through the text mapping table.
     * Falls back to pattern matching for dynamic texts.
     */
    public static String getTextMapping(String text) {
        if (text == null || text.isEmpty()) {
            return text;
        }
        if (!isMappingEnabled()) {
            return text;
        }
        // 1. Try exact text match first
        TextMapping mapping = getTextMappingByText(text);
        if (mapping != null) {
            String currentLanguage = Translations.getLanguage();
            String translatedText;
            if ("zh_CN".equals(currentLanguage)) {
                translatedText = mapping.chineseTranslation;
            } else {
                translatedText = mapping.englishTranslation;
            }
            return "(PID:" + mapping.id.substring(3) + ") " + translatedText;
        }
        // 2. Try pattern matching for dynamic texts
        for (TextMappingPattern patternMapping : textMappingPatterns) {
            java.util.regex.Matcher matcher = patternMapping.pattern.matcher(text);
            if (matcher.matches()) {
                String currentLanguage = Translations.getLanguage();
                String template = "zh_CN".equals(currentLanguage) ? patternMapping.chineseTemplate : patternMapping.englishTemplate;
                String result = template;
                for (int i = 0; i < matcher.groupCount(); i++) {
                    String varPart = matcher.group(i + 1);
                    result = result.replace("{" + i + "}", varPart);
                }
                return "(PID:" + patternMapping.id.substring(3) + ") " + result;
            }
        }
        return text;
    }

    /**
     * Maps display titles that are already assembled by UI components, such as
     * "ReferenceControllerAxis x" in the machine setup tree.
     */
    public static String getTitleFromDisplayText(Object obj, String displayText) {
        if (displayText == null || displayText.isEmpty() || !isMappingEnabled()) {
            return displayText;
        }
        if (obj != null) {
            ClassMapping mapping = getMapping(obj.getClass());
            if (mapping != null) {
                return buildTitle(mapping, getDisplaySuffix(displayText, mapping.simpleName));
            }
        }
        int separator = displayText.indexOf(' ');
        String simpleName = separator >= 0 ? displayText.substring(0, separator) : displayText;
        ClassMapping mapping = mappingsBySimpleName.get(simpleName);
        if (mapping == null) {
            return displayText;
        }
        return buildTitle(mapping, separator >= 0 ? displayText.substring(separator) : "");
    }

    private static String getDisplaySuffix(String displayText, String simpleName) {
        if (displayText.equals(simpleName)) {
            return "";
        }
        if (displayText.startsWith(simpleName + " ")) {
            return displayText.substring(simpleName.length());
        }
        return "";
    }

    private static String buildTitle(ClassMapping mapping, String suffix) {
        String currentLanguage = Translations.getLanguage();
        String title = "zh_CN".equals(currentLanguage) ? mapping.chineseTitle : mapping.englishTitle;
        return "(PID:" + mapping.id.substring(3) + ") " + title + suffix;
    }
}