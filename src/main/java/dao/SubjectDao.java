package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import bean.School;
import bean.Subject;

public class SubjectDao extends Dao {

    // 科目1件取得
    public Subject get(String cd, School school) throws Exception {

        Subject subject = null;

        // 実行する SQL を準備
        String sql = "SELECT * FROM SUBJECT WHERE CD = ? AND SCHOOL_CD = ?";

        // Connection と PreparedStatement を自動で close する
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            // プレースホルダに値をセット
            statement.setString(1, cd);
            statement.setString(2, school.getCd());

            // SQL を実行し、結果セットを取得
            try (ResultSet rSet = statement.executeQuery()) {

                // 1件でも結果があれば Subject オブジェクトを生成
                if (rSet.next()) {
                    subject = new Subject();

                    // DB の値を Subject オブジェクトにセット
                    subject.setId(rSet.getString("CD"));
                    subject.setName(rSet.getString("NAME"));

                    // School は引数で渡されたものをそのままセット
                    subject.setSchool(school);
                }
            }
        }

        // 見つかった Subject または null を返す
        return subject;
    }

    // 科目一覧取得
    public List<Subject> filter(School school) throws Exception {

        List<Subject> list = new ArrayList<>();

        // 指定された学校の科目をすべて取得する SQL
        String sql = "SELECT * FROM SUBJECT WHERE SCHOOL_CD = ? ORDER BY CD";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            // 学校コードをセット
            statement.setString(1, school.getCd());

            // SQL を実行
            try (ResultSet rSet = statement.executeQuery()) {

                // 取得した科目を1件ずつ Subject オブジェクトに詰めてリストへ追加
                while (rSet.next()) {
                    Subject subject = new Subject();
                    subject.setId(rSet.getString("CD"));
                    subject.setName(rSet.getString("NAME"));
                    subject.setSchool(school);
                    list.add(subject);
                }
            }
        }

        // 科目一覧を返す
        return list;
    }

    // 科目登録
    public boolean save(Subject subject) throws Exception {

        int count = 0;

        // INSERT 文を準備
        String sql = "INSERT INTO SUBJECT(CD, NAME, SCHOOL_CD) VALUES(?, ?, ?)";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            // Subject の値を SQL にセット
            statement.setString(1, subject.getId());
            statement.setString(2, subject.getName());
            statement.setString(3, subject.getSchool().getCd());

            // INSERT を実行し、影響行数を取得
            count = statement.executeUpdate();
        }

        // 1件以上登録できていれば true
        return count > 0;
    }

    // 科目更新
    public boolean update(Subject subject) throws Exception {

        int count = 0;

        // UPDATE 文を準備
        String sql = "UPDATE SUBJECT SET NAME = ? WHERE CD = ? AND SCHOOL_CD = ?";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            // 更新する値をセット
            statement.setString(1, subject.getName());
            statement.setString(2, subject.getId());
            statement.setString(3, subject.getSchool().getCd());

            // UPDATE を実行
            count = statement.executeUpdate();
        }

        // 更新件数が 1 件以上なら true
        return count > 0;
    }

    // 科目削除
    public boolean delete(String cd, School school) throws Exception {

        int count = 0;

        // DELETE 文を準備
        String sql = "DELETE FROM SUBJECT WHERE CD = ? AND SCHOOL_CD = ?";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            // 削除条件をセット
            statement.setString(1, cd);
            statement.setString(2, school.getCd());

            // DELETE を実行
            count = statement.executeUpdate();
        }

        // 削除件数が 1 件以上なら true
        return count > 0;
    }
}
