package lk.shenal.languagelearner.Models;

import java.util.ArrayList;
import java.util.List;

public class TranslatedPhrases {
    private String translatedLanguage;
    private List<String> ForeignPhrases;

    public TranslatedPhrases(String translatedLanguage) {
        this.translatedLanguage = translatedLanguage;
    }

    public String getTranslatedLanguage() {
        return translatedLanguage;
    }

    public void setTranslatedLanguage(String translatedLanguage) {
        this.translatedLanguage = translatedLanguage;
    }

    public List<String> getForeignPhrases() {
        return ForeignPhrases;
    }

    public void setForeignPhrases(List<String> foreignPhrases) {
        ForeignPhrases = foreignPhrases;
    }

    @Override
    public String toString() {
        return "TranslatedPhrases{" +
                "translatedLanguage='" + translatedLanguage + '\'' +
                ", ForeignPhrases=" + ForeignPhrases +
                '}';
    }
}
