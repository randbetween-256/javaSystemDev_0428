package bean;

import java.io.Serializable;
import java.util.Map;

public class TestListSubject implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 入学年度
     */
    private int entYear;

    /**
     * 学生番号
     */
    private String studentNo;

    /**
     * 学生名
     */
    private String studentName;

    /**
     * クラス番号
     */
    private String classNum;

    /**
     * 回数 → 点数 のマップ
     * JSP EL で扱いやすくするため String キーを使用
     */
    private Map<String, Integer> points;

    // ===== getter / setter =====

    public int getEntYear() {
        return entYear;
    }

    public void setEntYear(int entYear) {
        this.entYear = entYear;
    }

    public String getStudentNo() {
        return studentNo;
    }

    public void setStudentNo(String studentNo) {
        this.studentNo = studentNo;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getClassNum() {
        return classNum;
    }

    public void setClassNum(String classNum) {
        this.classNum = classNum;
    }

    public Map<String, Integer> getPoints() {
        return points;
    }

    public void setPoints(Map<String, Integer> points) {
        this.points = points;
    }

    /**
     * デバッグ用に TestListSubject の内容を文字列として返す
     */
    @Override
    public String toString() {
        return "TestListSubject [entYear=" + entYear
                + ", studentNo=" + studentNo
                + ", studentName=" + studentName
                + ", classNum=" + classNum
                + ", points=" + points
                + "]";
    }
}
