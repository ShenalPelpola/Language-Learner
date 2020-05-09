package lk.shenal.languagelearner.Models;

public class Phrase {
    private String PhraseContent;
    private String phraseColor;
    private int phraseImage;

    public Phrase(String phraseContent, String phraseColor, int phraseImage) {
        PhraseContent = phraseContent;
        this.phraseColor = phraseColor;
        this.phraseImage = phraseImage;
    }


    public String getPhraseColor() {
        return phraseColor;
    }

    public void setPhraseColor(String phraseColor) {
        this.phraseColor = phraseColor;
    }

    public int getPhraseImage() {
        return phraseImage;
    }

    public void setPhraseImage(int phraseImage) {
        this.phraseImage = phraseImage;
    }

    public String getPhraseContent() {
        return PhraseContent;
    }

    public void setPhraseContent(String phraseContent) {
        PhraseContent = phraseContent;
    }
}
