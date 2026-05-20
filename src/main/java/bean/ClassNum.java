package bean;

import java.io.Serializable;

public class ClassNum implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * クラス番号
     */
    private String classNum;

    /**
     * 学校
     */
    private School school;

    /**
     * getter / setter
     */

    public String getClassNum() {
        return classNum;
    }

    public void setClassNum(String classNum) {
        this.classNum = classNum;
    }

    public School getSchool() {
        return school;
    }

    public void setSchool(School school) {
        this.school = school;
    }

    /**
     * デバッグ用に ClassNum の内容を文字列として返す
     */
    @Override
    public String toString() {
        return "ClassNum [classNum=" + classNum + ", school=" + school + "]";
    }
}
