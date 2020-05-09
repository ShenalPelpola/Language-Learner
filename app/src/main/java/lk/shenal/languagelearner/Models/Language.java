package lk.shenal.languagelearner.Models;

public class Language {
    private String languageCode;
    private String languageName;
    private boolean isSubscribed;
    private int countryImage;

    public Language(String languageCode, String languageName) {
        this.languageCode = languageCode;
        this.languageName = languageName;
        this.isSubscribed = false;
    }


    public int getCountryImage() {
        return countryImage;
    }

    public void setCountryImage(int countryImage) {
        this.countryImage = countryImage;
    }

    public String getLanguageCode() {
        return languageCode;
    }

    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }

    public String getLanguageName() {
        return languageName;
    }

    public void setLanguageName(String languageName) {
        this.languageName = languageName;
    }

    public boolean getIsSubscribed() {
        return isSubscribed;
    }

    public void setIsSubscribed(boolean isSubscribed) {
        this.isSubscribed = isSubscribed;
    }

    @Override
    public String toString() {
        return "Language{" +
                "languageCode='" + languageCode + '\'' +
                ", languageName='" + languageName + '\'' +
                ", isSubscribed=" + isSubscribed +
                '}';
    }
}
