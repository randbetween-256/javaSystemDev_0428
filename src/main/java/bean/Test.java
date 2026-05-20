package bean;

import java.io.Serializable;

public class Test implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 学生
     */
    private Student student;

    /**
     * 科目
     */
    private Subject subject;

    /**
     * 学校
     */
    private School school;

    /**
     * クラス番号
     */
    private String classNum;

    /**
     * 回数（1回目、2回目…）
     */
    private int no;

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

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public School getSchool() {
        return school;
    }

    public void setSchool(School school) {
        this.school = school;
    }

    public String getClassNum() {
        return classNum;
    }

    public void setClassNum(String classNum) {
        this.classNum = classNum;
    }

    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }

    /**
     * TestRegistAction 互換用
     */
    public int getCount() {
        return no;
    }

    /**
     * TestRegistAction 互換用
     */
    public void setCount(int count) {
        this.no = count;
    }

    public Integer getPoint() {
        return point;
    }

    public void setPoint(Integer point) {
        this.point = point;
    }

    /**
     * デバッグ用に Test の内容を文字列として返す
     */
    @Override
    public String toString() {
        return "Test [student=" + student
                + ", subject=" + subject
                + ", school=" + school
                + ", classNum=" + classNum
                + ", no=" + no
                + ", point=" + point
                + "]";
    }
}
