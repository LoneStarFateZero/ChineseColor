package pers.lonestar.chinesecolor.colorclass;

import org.litepal.crud.LitePalSupport;

/**
 * 保存点击收藏的颜色，借助LitePal使用
 * 每项须有对应的setter和getter
 */
public class FavoriteColor extends LitePalSupport {
    private String name;
    private String hex;
    private String pinyin;

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

    public FavoriteColor(String name, String pinyin, String hex) {
        this.name = name;
        this.pinyin = pinyin;
        this.hex = hex;
    }
}
