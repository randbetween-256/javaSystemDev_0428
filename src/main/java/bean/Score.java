package bean;

import java.io.Serializable;

public class Score implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 学生情報
     */
    private Student student;

    /**
     * テスト情報
     */
    private Test test;

    /**
     * 得点
     * null の場合は未登録
     */
    private Integer point;

    // ===== getter / setter =====

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Test getTest() {
        return test;
    }

    public void setTest(Test test) {
        this.test = test;
    }

    public Integer getPoint() {
        return point;
    }

    public void setPoint(Integer point) {
        this.point = point;
    }

    /**
     * デバッグ用に Score の内容を文字列として返す
     */
    @Override
    public String toString() {
        return "Score [student=" + student + ", test=" + test + ", point=" + point + "]";
    }
}
