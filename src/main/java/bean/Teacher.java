package bean;

import java.io.Serializable;

public class Teacher implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 教員ID
     */
    private String id;

    /**
     * パスワード
     */
    private String password;

    /**
     * 教員名
     */
    private String name;

    /**
     * 学校
     */
    private School school;

    // ===== getter / setter =====

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public School getSchool() {
        return school;
    }

    public void setSchool(School school) {
        this.school = school;
    }

    /**
     * デバッグ用に Teacher の内容を文字列として返す
     */
    @Override
    public String toString() {
        return "Teacher [id=" + id 
                + ", password=" + password 
                + ", name=" + name 
                + ", school=" + school 
                + "]";
    }
}
