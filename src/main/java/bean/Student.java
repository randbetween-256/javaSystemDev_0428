package bean;

import java.io.Serializable;

public class Student implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 学生番号
     */
    private String no;

    /**
     * 学生名
     */
    private String name;

    /**
     * 入学年度
     */
    private int entYear;

    /**
     * クラス番号
     */
    private String classNum;

    /**
     * 在学フラグ
     */
    private boolean isAttend;

    /**
     * 学校
     */
    private School school;

    // ===== getter / setter =====

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getEntYear() {
        return entYear;
    }

    public void setEntYear(int entYear) {
        this.entYear = entYear;
    }

    public String getClassNum() {
        return classNum;
    }

    public void setClassNum(String classNum) {
        this.classNum = classNum;
    }

    public boolean isAttend() {
        return isAttend;
    }

    public void setAttend(boolean isAttend) {
        this.isAttend = isAttend;
    }

    public School getSchool() {
        return school;
    }

    public void setSchool(School school) {
        this.school = school;
    }

    /**
     * デバッグ用に Student の内容を文字列として返す
     */
    @Override
    public String toString() {
        return "Student [no=" + no 
                + ", name=" + name 
                + ", entYear=" + entYear 
                + ", classNum=" + classNum 
                + ", isAttend=" + isAttend 
                + ", school=" + school 
                + "]";
    }
}
