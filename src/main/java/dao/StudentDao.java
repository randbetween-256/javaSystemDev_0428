package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import bean.School;
import bean.Student;

public class StudentDao extends Dao {

    // 学生1件取得
    public Student get(String no, School school) throws Exception {
        Student student = null;

        // 実行する SQL を準備
        String sql = "SELECT * FROM student WHERE no = ? AND school_cd = ?";

        // Connection と PreparedStatement を自動 close する
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            // プレースホルダに値をセット
            statement.setString(1, no);
            statement.setString(2, school.getCd());

            // SQL を実行し、結果セットを取得
            try (ResultSet rSet = statement.executeQuery()) {

                // 1件でも結果があれば Student オブジェクトを生成
                if (rSet.next()) {
                    student = new Student();

                    // DB の値を Student オブジェクトにセット
                    student.setNo(rSet.getString("no"));
                    student.setName(rSet.getString("name"));
                    student.setEntYear(rSet.getInt("ent_year"));
                    student.setClassNum(rSet.getString("class_num"));
                    student.setAttend(rSet.getBoolean("is_attend"));

                    // School は引数で渡されたものをそのままセット
                    student.setSchool(school);
                }
            }
        }

        // 見つかった Student または null を返す
        return student;
    }

    // 学生一覧取得（学校単位）
    public List<Student> filter(School school) throws Exception {
        List<Student> list = new ArrayList<>();

        // 指定された学校の学生をすべて取得する SQL
        String sql = "SELECT * FROM student WHERE school_cd = ? ORDER BY no";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            // 学校コードをセット
            statement.setString(1, school.getCd());

            // SQL を実行
            try (ResultSet rSet = statement.executeQuery()) {

                // 取得した学生を1件ずつ Student オブジェクトに詰めてリストへ追加
                while (rSet.next()) {
                    Student student = new Student();
                    student.setNo(rSet.getString("no"));
                    student.setName(rSet.getString("name"));
                    student.setEntYear(rSet.getInt("ent_year"));
                    student.setClassNum(rSet.getString("class_num"));
                    student.setAttend(rSet.getBoolean("is_attend"));
                    student.setSchool(school);
                    list.add(student);
                }
            }
        }

        // 学生一覧を返す
        return list;
    }

    // 学生一覧取得（学校＋在籍状態）
    public List<Student> filter(School school, boolean isAttend) throws Exception {
        List<Student> list = new ArrayList<>();

        // 在籍状態で絞り込む SQL
        String sql = "SELECT * FROM student WHERE school_cd = ? AND is_attend = ? ORDER BY no";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            // 学校コードと在籍状態をセット
            statement.setString(1, school.getCd());
            statement.setBoolean(2, isAttend);

            try (ResultSet rSet = statement.executeQuery()) {

                // 条件に一致する学生をリストへ追加
                while (rSet.next()) {
                    Student student = new Student();
                    student.setNo(rSet.getString("no"));
                    student.setName(rSet.getString("name"));
                    student.setEntYear(rSet.getInt("ent_year"));
                    student.setClassNum(rSet.getString("class_num"));
                    student.setAttend(rSet.getBoolean("is_attend"));
                    student.setSchool(school);
                    list.add(student);
                }
            }
        }

        return list;
    }

    // 学生一覧取得（学校＋入学年度＋クラス＋在籍状態）
    public List<Student> filter(School school, int entYear, String classNum, boolean isAttend) throws Exception {
        List<Student> list = new ArrayList<>();

        // 入学年度・クラス・在籍状態で絞り込む SQL
        String sql = "SELECT * FROM student WHERE school_cd = ? AND ent_year = ? AND class_num = ? AND is_attend = ? ORDER BY no";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            // 条件をセット
            statement.setString(1, school.getCd());
            statement.setInt(2, entYear);
            statement.setString(3, classNum);
            statement.setBoolean(4, isAttend);

            try (ResultSet rSet = statement.executeQuery()) {

                // 条件に一致する学生をリストへ追加
                while (rSet.next()) {
                    Student student = new Student();
                    student.setNo(rSet.getString("no"));
                    student.setName(rSet.getString("name"));
                    student.setEntYear(rSet.getInt("ent_year"));
                    student.setClassNum(rSet.getString("class_num"));
                    student.setAttend(rSet.getBoolean("is_attend"));
                    student.setSchool(school);
                    list.add(student);
                }
            }
        }

        return list;
    }

    // 学生一覧取得（学校＋入学年度＋在籍状態）
    public List<Student> filter(School school, int entYear, boolean isAttend) throws Exception {
        List<Student> list = new ArrayList<>();

        // 入学年度と在籍状態で絞り込む SQL
        String sql = "SELECT * FROM student WHERE school_cd = ? AND ent_year = ? AND is_attend = ? ORDER BY no";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            // 条件をセット
            statement.setString(1, school.getCd());
            statement.setInt(2, entYear);
            statement.setBoolean(3, isAttend);

            try (ResultSet rSet = statement.executeQuery()) {

                // 条件に一致する学生をリストへ追加
                while (rSet.next()) {
                    Student student = new Student();
                    student.setNo(rSet.getString("no"));
                    student.setName(rSet.getString("name"));
                    student.setEntYear(rSet.getInt("ent_year"));
                    student.setClassNum(rSet.getString("class_num"));
                    student.setAttend(rSet.getBoolean("is_attend"));
                    student.setSchool(school);
                    list.add(student);
                }
            }
        }

        return list;
    }

    // 学生登録
    public boolean save(Student student) throws Exception {
        int count = 0;

        // INSERT 文を準備
        String sql = "INSERT INTO student(no, name, ent_year, class_num, is_attend, school_cd) VALUES(?, ?, ?, ?, ?, ?)";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            // Student の値を SQL にセット
            statement.setString(1, student.getNo());
            statement.setString(2, student.getName());
            statement.setInt(3, student.getEntYear());
            statement.setString(4, student.getClassNum());
            statement.setBoolean(5, student.isAttend());
            statement.setString(6, student.getSchool().getCd());

            // INSERT を実行し、影響行数を取得
            count = statement.executeUpdate();
        }

        // 1件以上登録できていれば true
        return count > 0;
    }

    // 学生更新（newNo を使わない版）
    public boolean update(Student student) throws Exception {

        int count = 0;

        // UPDATE 文を準備
        String sql =
            "UPDATE student SET name=?, ent_year=?, class_num=?, is_attend=? " +
            "WHERE no=? AND school_cd=?";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            // 更新する値をセット
            statement.setString(1, student.getName());
            statement.setInt(2, student.getEntYear());
            statement.setString(3, student.getClassNum());
            statement.setBoolean(4, student.isAttend());
            statement.setString(5, student.getNo());
            statement.setString(6, student.getSchool().getCd());

            // UPDATE を実行
            count = statement.executeUpdate();
        }

        // 更新件数が 1 件以上なら true
        return count > 0;
    }
 // 入学年度一覧取得（学校単位）
    public List<Integer> getEntYearSet(School school) throws Exception {
        List<Integer> entYearList = new ArrayList<>();

        // 入学年度を重複なしで取得する SQL
        String sql = "SELECT DISTINCT ent_year FROM student WHERE school_cd = ? ORDER BY ent_year";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            // 学校コードをセット
            statement.setString(1, school.getCd());

            // SQL 実行
            try (ResultSet rSet = statement.executeQuery()) {
                while (rSet.next()) {
                    entYearList.add(rSet.getInt("ent_year"));
                }
            }
        }

        return entYearList;
    }

}
