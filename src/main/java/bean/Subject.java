package bean;

import java.io.Serializable;

public class Subject implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 科目コード
     */
    private String id;

    /**
     * 科目名
     */
    private String name;

    /**
     * 学校
     */
    private School school;

    public Subject() {
    }

    /**
     * TestRegistAction 用コンストラクタ
     */
    public Subject(String id, String name) {
        this.id = id;
        this.name = name;
    }

    // ===== getter =====

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public School getSchool() {
        return school;
    }

    // ===== setter =====

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSchool(School school) {
        this.school = school;
    }

    /**
     * デバッグ用に Subject の内容を文字列として返す
     */
    @Override
    public String toString() {
        return "Subject [id=" + id + ", name=" + name + ", school=" + school + "]";
    }
}
