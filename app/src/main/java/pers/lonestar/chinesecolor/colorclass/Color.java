package pers.lonestar.chinesecolor.colorclass;

import org.litepal.crud.LitePalSupport;

public class Color extends LitePalSupport {
    private String name;
    private String hex;
    private String pinyin;

    @Override
    public boolean isSaved() {
        return super.isSaved();
    }

    public String getName() {
        return name;
    }

    public String getHex() {
        return hex;
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
    }

}
