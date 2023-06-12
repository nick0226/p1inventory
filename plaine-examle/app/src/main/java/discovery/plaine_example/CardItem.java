package discovery.plaine_example;

public class CardItem {
    private String mainText;
    private String subText;
    private int imageResource;

    public CardItem(String mainText, String subText, int imageResource) {
        this.mainText = mainText;
        this.subText = subText;
        this.imageResource = imageResource;
    }

    public String getMainText() {
        return mainText;
    }

    public String getSubText() {
        return subText;
    }

    public int getImageResource() {
        return imageResource;
    }
}
