package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import bean.Teacher;

public class TeacherDao extends Dao {

    // ID から Teacher を取得する
    public Teacher get(String id) throws Exception {

        Teacher teacher = null;

        // 実行する SQL を準備
        String sql = "SELECT * FROM teacher WHERE id = ?";

        // Connection と PreparedStatement を自動で close する
        try (Connection con = getConnection();
             PreparedStatement st = con.prepareStatement(sql)) {

            // プレースホルダに値をセット
            st.setString(1, id);

            // SQL を実行し、結果セットを取得
            try (ResultSet rs = st.executeQuery()) {

                // 1件でも結果があれば Teacher オブジェクトを生成
                if (rs.next()) {

                    teacher = new Teacher();

                    // DB の値を Teacher オブジェクトにセット
                    teacher.setId(rs.getString("id"));
                    teacher.setPassword(rs.getString("password"));
                    teacher.setName(rs.getString("name"));

                    // school_cd を使って SchoolDao から School を取得しセット
                    SchoolDao sDao = new SchoolDao();
                    teacher.setSchool(sDao.get(rs.getString("school_cd")));
                }
            }
        }

        // 見つかった Teacher または null を返す
        return teacher;
    }

    // ログイン認証
    public Teacher login(String id, String password) throws Exception {

        // ID から Teacher を取得
        Teacher teacher = get(id);

        // Teacher が存在し、パスワードが一致すればその Teacher を返す
        if (teacher != null && teacher.getPassword().equals(password)) {
            return teacher;
        }

        // 認証失敗時は null を返す
        return null;
    }
}
