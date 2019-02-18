package pers.lonestar.chinesecolor.colorclass;

public class jsonColor {
    private int[] CMYK;

    public int[] getCMYK() {
        return CMYK;
    }

    public void setCMYK(int[] CMYK) {
        this.CMYK = CMYK;
    }

    public int[] getRGB() {
        return RGB;
    }

    public void setRGB(int[] RGB) {
        this.RGB = RGB;
    }

    public String getHex() {
        return hex;
    }

    public void setHex(String hex) {
        this.hex = hex;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPinyin() {
        return pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }

    private int[] RGB;
    private String hex;
    private String name;
    private String pinyin;
}
