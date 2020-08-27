package eu.beezig.core.config.i18n;

import eu.beezig.core.config.EnumSetting;

import java.util.Locale;

public class LanguageSetting extends EnumSetting {
    private String enumName;
    private Locale locale;

    public LanguageSetting(String enumName, Locale locale) {
        this.enumName = enumName;
        this.locale = locale;
    }

    public Locale getLocaleId() {
        return locale;
    }

    @Override
    public String name() {
        return enumName;
    }

    public static LanguageSetting valueOf(String name) {
        return LanguageConfiguration.languages.get(name);
    }

    public static LanguageSetting[] values() {
        return LanguageConfiguration.languages.values().toArray(new LanguageSetting[0]);
    }
}
