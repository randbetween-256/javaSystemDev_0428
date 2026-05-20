package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import bean.School;
import bean.TestListStudent;

public class TestListStudentDao extends Dao {

    /**
     * 学生別成績参照（科目は必ず全件、成績は LEFT JOIN）
     */
    public List<TestListStudent> filter(School school, String studentNo) throws Exception {

        List<TestListStudent> list = new ArrayList<>();

        // 科目を基準に LEFT JOIN し、指定学生の成績を結合して取得する SQL
        String sql =
            "SELECT sub.cd, sub.name, t.no, t.point " +
            "FROM subject sub " +
            "LEFT JOIN test t " +
            "  ON sub.cd = t.subject_cd " +   // 科目コードで結合
            " AND t.student_no = ? " +        // 指定学生の成績だけを結合
            " AND t.school_cd = ? " +         // 学校コードも一致させる
            "WHERE sub.school_cd = ? " +      // 科目側の学校コード
            "ORDER BY sub.cd, t.no";          // 科目順 → 回数順

        // Connection と PreparedStatement を自動で close する
        try (Connection con = getConnection();
             PreparedStatement st = con.prepareStatement(sql)) {

            // JOIN 条件と WHERE 条件に値をセット
            st.setString(1, studentNo);
            st.setString(2, school.getCd());
            st.setString(3, school.getCd());

            // SQL を実行し、結果セットを取得
            try (ResultSet rs = st.executeQuery()) {

                // 取得した行を1件ずつ処理
                while (rs.next()) {

                    // TestListStudent オブジェクトを生成
                    TestListStudent tls = new TestListStudent();

                    // 科目コードと科目名をセット
                    tls.setSubjectCd(rs.getString("cd"));
                    tls.setSubjectName(rs.getString("name"));

                    // テスト回数（null の可能性があるため getObject を使用）
                    tls.setNum((Integer) rs.getObject("no"));

                    // 点数（null の可能性があるため getObject を使用）
                    tls.setPoint((Integer) rs.getObject("point"));

                    // リストに追加
                    list.add(tls);
                }
            }
        }

        // 科目ごとの成績一覧を返す
        return list;
    }
}
