package pers.lonestar.chinesecolor;

public class Color {
    private String name;
    private String hex;
    private String pinyin;
    private int red;
    private int green;
    private int blue;

    public String getName() {
        return name;
    }

    public String getHex() {
        return hex;
    }

    public int getRed() {
        return red;
    }

    public int getGreen() {
        return green;
    }

    public int getBlue() {
        return blue;
    }

    public String getPinyin() {
        return pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }

    public Color(String name, String pinyin, String hex) {
        this.name = name;
        this.pinyin = pinyin;
        this.hex = hex;
        int color = android.graphics.Color.parseColor(hex);
        this.red = (color & 0xff0000) >> 16;
        this.green = (color & 0x00ff00) >> 8;
        this.blue = (color & 0x0000ff);
    }

}
