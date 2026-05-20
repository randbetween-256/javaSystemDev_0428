package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import bean.School;
import bean.Student;
import bean.Subject;
import bean.Test;

public class TestDao extends Dao {

    /**
     * 成績1件取得（学生情報 JOIN 版）
     */
    public Test get(School school, String studentNo, String subjectId, int count) throws Exception {

        Test test = null;

        // test と student を結合して必要な情報を1回の SQL で取得する
        String sql =
            "SELECT t.*, " +
            "       s.name AS student_name, " +
            "       s.ent_year AS student_ent_year, " +
            "       s.class_num AS student_class_num " +
            "FROM test t " +
            "JOIN student s ON t.student_no = s.no AND t.school_cd = s.school_cd " +
            "WHERE t.school_cd = ? AND t.student_no = ? AND t.subject_cd = ? AND t.no = ?";

        // Connection と PreparedStatement を自動で close する
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            // プレースホルダに値をセット
            statement.setString(1, school.getCd());
            statement.setString(2, studentNo);
            statement.setString(3, subjectId);
            statement.setInt(4, count);

            // SQL を実行し、結果セットを取得
            try (ResultSet rs = statement.executeQuery()) {

                // 1件でも結果があれば Test オブジェクトを生成
                if (rs.next()) {

                    test = new Test();

                    // DB の no（回数）をセット
                    test.setNo(rs.getInt("no"));

                    // 引数で渡された count をそのままセット
                    test.setCount(count);

                    // point は null の可能性があるため getObject を使う
                    test.setPoint((Integer) rs.getObject("point"));

                    // クラス番号と学校をセット
                    test.setClassNum(rs.getString("class_num"));
                    test.setSchool(school);

                    // 学生情報を Student オブジェクトに詰める
                    Student stu = new Student();
                    stu.setNo(studentNo);
                    stu.setName(rs.getString("student_name"));
                    stu.setEntYear(rs.getInt("student_ent_year"));
                    stu.setClassNum(rs.getString("student_class_num"));
                    stu.setSchool(school);
                    test.setStudent(stu);

                    // 科目情報（ID のみセット）
                    Subject sub = new Subject();
                    sub.setId(subjectId);
                    test.setSubject(sub);
                }
            }
        }

        // 見つかった Test または null を返す
        return test;
    }

    /**
     * 成績登録・更新
     */
    public boolean saveScore(
            String studentNo,
            String subjectCode,
            int testNo,
            int point,
            School school,
            String classNum) throws Exception {

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet rs = null;

        try {
            // DB 接続を取得し、トランザクションを開始
            connection = getConnection();
            connection.setAutoCommit(false);

            // 既存データがあるか確認する SQL
            String checkSql =
                "SELECT 1 FROM test " +
                "WHERE student_no = ? AND subject_cd = ? AND no = ? AND school_cd = ?";

            // 存在チェック用の PreparedStatement を作成
            statement = connection.prepareStatement(checkSql);
            statement.setString(1, studentNo);
            statement.setString(2, subjectCode);
            statement.setInt(3, testNo);
            statement.setString(4, school.getCd());

            // SQL を実行し、既存データがあるか確認
            rs = statement.executeQuery();
            boolean exists = rs.next();

            // ResultSet と Statement を閉じる
            rs.close();
            statement.close();

            int result = 0;

            if (exists) {
                // 既存データがある場合は UPDATE を実行
                String updateSql =
                    "UPDATE test SET point=?, class_num=? " +
                    "WHERE student_no=? AND subject_cd=? AND no=? AND school_cd=?";

                statement = connection.prepareStatement(updateSql);
                statement.setInt(1, point);
                statement.setString(2, classNum);
                statement.setString(3, studentNo);
                statement.setString(4, subjectCode);
                statement.setInt(5, testNo);
                statement.setString(6, school.getCd());

                result = statement.executeUpdate();

            } else {
                // 既存データがない場合は INSERT を実行
                String insertSql =
                    "INSERT INTO test (student_no, subject_cd, school_cd, no, point, class_num) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";

                statement = connection.prepareStatement(insertSql);
                statement.setString(1, studentNo);
                statement.setString(2, subjectCode);
                statement.setString(3, school.getCd());
                statement.setInt(4, testNo);
                statement.setInt(5, point);
                statement.setString(6, classNum);

                result = statement.executeUpdate();
            }

            // すべて成功したのでコミット
            connection.commit();

            // INSERT または UPDATE が成功していれば true
            return result > 0;

        } catch (Exception e) {

            // エラーが発生した場合はロールバック
            if (connection != null) {
                connection.rollback();
            }
            throw e;

        } finally {
            // ResultSet → Statement → Connection の順でクローズ
            if (rs != null) rs.close();
            if (statement != null) statement.close();
            if (connection != null) connection.close();
        }
    }
}
