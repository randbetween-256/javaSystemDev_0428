package bean;

import java.io.Serializable;

public class TestListStudent implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 科目名
     */
    private String subjectName;

    /**
     * 科目コード
     */
    private String subjectCd;

    /**
     * 回数
     * null の場合は未登録
     */
    private Integer num;

    /**
     * 得点
     * null の場合は未登録
     */
    private Integer point;

    // ===== getter / setter =====

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getSubjectCd() {
        return subjectCd;
    }

    public void setSubjectCd(String subjectCd) {
        this.subjectCd = subjectCd;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public Integer getPoint() {
        return point;
    }

    public void setPoint(Integer point) {
        this.point = point;
    }

    /**
     * デバッグ用に TestListStudent の内容を文字列として返す
     */
    @Override
    public String toString() {
        return "TestListStudent [subjectName=" + subjectName
                + ", subjectCd=" + subjectCd
                + ", num=" + num
                + ", point=" + point
                + "]";
    }
}
