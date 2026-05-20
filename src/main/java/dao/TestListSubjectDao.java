package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import bean.School;
import bean.TestListSubject;

public class TestListSubjectDao extends Dao {

    /**
     * 科目別成績参照（学生は必ず全件、成績は LEFT JOIN）
     */
    public List<TestListSubject> filter(School school, int entYear, String classNum, String subjectCd) throws Exception {

        List<TestListSubject> list = new ArrayList<>();

        // SQL を段階的に組み立てる
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT s.ent_year, s.class_num, s.no AS student_no, s.name, ");
        sb.append("       t.no AS test_no, t.point ");
        sb.append("FROM student s ");
        sb.append("LEFT JOIN test t ");
        sb.append("  ON s.no = t.student_no ");     // 学生番号で結合
        sb.append(" AND s.class_num = t.class_num "); // クラス番号も一致させる
        sb.append(" AND t.subject_cd = ? ");          // 指定科目の成績だけ結合
        sb.append(" AND t.school_cd = ? ");           // 学校コードも一致させる
        sb.append("WHERE s.school_cd = ? ");          // 学生側の学校コード
        sb.append("  AND s.ent_year = ? ");           // 入学年度で絞り込み

        // クラス指定がある場合だけ条件を追加
        boolean hasClassFilter = (classNum != null && !classNum.equals("0") && !classNum.isEmpty());
        if (hasClassFilter) {
            sb.append("  AND s.class_num = ? ");
        }

        sb.append("ORDER BY s.no, t.no");             // 学生番号 → テスト回数の順で並べる

        // Connection と PreparedStatement を自動で close する
        try (Connection con = getConnection();
             PreparedStatement st = con.prepareStatement(sb.toString())) {

            // プレースホルダに値をセット（順番に注意）
            int idx = 1;
            st.setString(idx++, subjectCd);
            st.setString(idx++, school.getCd());
            st.setString(idx++, school.getCd());
            st.setInt(idx++, entYear);

            if (hasClassFilter) {
                st.setString(idx++, classNum);
            }

            // SQL を実行し、結果セットを取得
            try (ResultSet rs = st.executeQuery()) {

                TestListSubject tls = null;

                // 取得した行を1件ずつ処理
                while (rs.next()) {

                    // 現在の行の学生番号を取得
                    String stuNo = rs.getString("student_no");

                    // 新しい学生に切り替わった場合、新しい TestListSubject を作成
                    if (tls == null || !tls.getStudentNo().equals(stuNo)) {
                        tls = new TestListSubject();

                        // 学生情報をセット
                        tls.setEntYear(rs.getInt("ent_year"));
                        tls.setClassNum(rs.getString("class_num"));
                        tls.setStudentNo(stuNo);
                        tls.setStudentName(rs.getString("name"));

                        // テスト回数 → 点数 を格納する Map を初期化
                        tls.setPoints(new HashMap<String, Integer>());

                        // リストに追加
                        list.add(tls);
                    }

                    // テスト回数（null の可能性があるため getObject を使用）
                    Integer testNo = (Integer) rs.getObject("test_no");

                    // 点数（null の可能性があるため getObject を使用）
                    Integer point = (Integer) rs.getObject("point");

                    // テスト回数が null でない場合のみ Map に追加
                    if (testNo != null) {
                        tls.getPoints().put(String.valueOf(testNo), point);
                    }
                }
            }
        }

        // 学生ごとの成績一覧を返す
        return list;
    }
}
