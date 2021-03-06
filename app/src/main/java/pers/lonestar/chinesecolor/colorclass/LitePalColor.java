package pers.lonestar.chinesecolor.colorclass;

import org.litepal.crud.LitePalSupport;

/**
 * 保存从color.json文件中加载得到的颜色，借助LitePal使用
 * 每项须有对应的setter和getter
 */
public class LitePalColor extends LitePalSupport {
    private String name;
    private String hex;
    private String pinyin;

    public LitePalColor(String name, String pinyin, String hex) {
        this.name = name;
        this.pinyin = pinyin;
        this.hex = hex;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHex() {
        return hex;
    }

    public void setHex(String hex) {
        this.hex = hex;
    }

    public String getPinyin() {
        return pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }
}
