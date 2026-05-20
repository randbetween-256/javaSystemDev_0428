package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import bean.School;

public class SchoolDao extends Dao {

    /**
     * 学校コードを指定して学校インスタンスを1件取得する
     *
     * @param cd 学校コード
     * @return School 存在しない場合は null
     * @throws Exception
     */
    public School get(String cd) throws Exception {

        School school = null;

        // 実行する SQL を準備
        String sql = "SELECT * FROM school WHERE cd = ?";

        // try-with-resources により Connection と PreparedStatement が自動で close される
        try (
            Connection connection = getConnection();              // DB へ接続
            PreparedStatement statement = connection.prepareStatement(sql) // SQL を準備
        ) {

            // プレースホルダに学校コードをセット
            statement.setString(1, cd);

            // SQL を実行し、結果セットを取得
            try (ResultSet rs = statement.executeQuery()) {

                // 1件でも結果があれば School オブジェクトを生成
                if (rs.next()) {
                    school = new School();

                    // DB の値を School オブジェクトにセット
                    school.setCd(rs.getString("cd"));
                    school.setName(rs.getString("name"));
                }
            }

        } catch (Exception e) {

            // 例外が発生した場合は呼び出し元へそのまま投げる
            throw e;
        }

        // 見つかった School または null を返す
        return school;
    }
}
