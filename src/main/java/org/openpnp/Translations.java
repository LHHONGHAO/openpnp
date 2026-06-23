
package org.openpnp;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.prefs.Preferences;

public class Translations {
    private static final String BUNDLE_NAME = "org.openpnp.translations";
    private static final String PREF_LANGUAGE = "language";
    
    private static final Preferences prefs = Preferences.userNodeForPackage(Translations.class);
    private static Locale currentLocale;
    private static ResourceBundle RESOURCE_BUNDLE;
    
    static {
        loadLanguage();
    }

    private Translations() {
    }
    
    private static void loadLanguage() {
        String langCode = prefs.get(PREF_LANGUAGE, Locale.getDefault().toString());
        if ("zh_CN".equals(langCode)) {
            currentLocale = Locale.SIMPLIFIED_CHINESE;
        } else if ("en".equals(langCode) || "en_US".equals(langCode)) {
            currentLocale = Locale.US;
        } else {
            try {
                if (langCode.contains("_")) {
                    String[] parts = langCode.split("_");
                    currentLocale = new Locale(parts[0], parts[1]);
                } else {
                    currentLocale = new Locale(langCode);
                }
            } catch (Exception e) {
                currentLocale = Locale.getDefault();
            }
        }
        RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME, currentLocale, new UTF8Control());
    }

    public static String getString(String key) {
        try {
            return RESOURCE_BUNDLE.getString(key);
        } catch (MissingResourceException e) {
            return '!' + key + '!';
        }
    }
    
    public static void setLanguage(String languageCode) {
        prefs.put(PREF_LANGUAGE, languageCode);
        try {
            prefs.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
        loadLanguage();
    }
    
    public static String getLanguage() {
        return prefs.get(PREF_LANGUAGE, Locale.getDefault().toString());
    }
    
    public static Locale getLocale() {
        return currentLocale;
    }

    public static class UTF8Control extends ResourceBundle.Control {
        public ResourceBundle newBundle
                (String baseName, Locale locale, String format, ClassLoader loader, boolean reload)
                throws IOException {
            String bundleName = toBundleName(baseName, locale);
            String resourceName = toResourceName(bundleName, "properties");
            ResourceBundle bundle = null;
            InputStream stream = null;
            if (reload) {
                URL url = loader.getResource(resourceName);
                if (url != null) {
                    URLConnection connection = url.openConnection();
                    if (connection != null) {
                        connection.setUseCaches(false);
                        stream = connection.getInputStream();
                    }
                }
            } else {
                stream = loader.getResourceAsStream(resourceName);
            }
            if (stream != null) {
                try {
                    bundle = new PropertyResourceBundle(new InputStreamReader(stream, StandardCharsets.UTF_8));
                } finally {
                    stream.close();
                }
            }
            return bundle;
        }
    }
}
