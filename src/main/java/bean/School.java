package bean;

import java.io.Serializable;

public class School implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 学校コード
     */
    private String cd;

    /**
     * 学校名
     */
    private String name;

    /**
     * getter / setter
     */

    public String getCd() {
        return cd;
    }

    public void setCd(String cd) {
        this.cd = cd;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * デバッグ用に School の内容を文字列として返す
     */
    @Override
    public String toString() {
        return "School [cd=" + cd + ", name=" + name + "]";
    }
}
