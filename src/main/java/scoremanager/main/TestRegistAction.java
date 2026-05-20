package scoremanager.main;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import bean.Student;
import bean.Subject;
import bean.Teacher;
import bean.Test;
import dao.ClassNumDao;
import dao.StudentDao;
import dao.SubjectDao;
import dao.TestDao;
import tool.Action;

public class TestRegistAction extends Action {

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {

        // ===== ログインチェック =====

        // 既存のセッションを取得（存在しない場合は null）
        HttpSession session = req.getSession(false);

        // セッションからログイン中の Teacher を取得
        Teacher teacher = (Teacher) session.getAttribute("user");

        // Teacher が無ければログイン画面へ
        if (teacher == null) {
            req.getRequestDispatcher("/scoremanager/login.jsp").forward(req, res);
            return;
        }

        // ===== DAO 準備 =====

        ClassNumDao classNumDao = new ClassNumDao();
        SubjectDao subjectDao = new SubjectDao();
        StudentDao studentDao = new StudentDao();
        TestDao testDao = new TestDao();

        // ===== 入学年度リスト作成（現在年から過去10年） =====

        LocalDate today = LocalDate.now();
        int year = today.getYear();

        List<Integer> entYearList = new ArrayList<>();
        for (int i = year - 10; i <= year; i++) {
            entYearList.add(i);
        }

        // ===== クラス一覧取得 =====

        List<String> cNumList = classNumDao.filter(teacher.getSchool());

        // ===== 科目一覧取得 =====

        List<Subject> list = subjectDao.filter(teacher.getSchool());

        // ===== 回数リスト（1〜10） =====

        List<Integer> countList = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            countList.add(i);
        }

        // ===== パラメータ取得 =====

        String f1 = req.getParameter("f1"); // 入学年度
        String f2 = req.getParameter("f2"); // クラス
        String f3 = req.getParameter("f3"); // 科目コード
        String f4 = req.getParameter("f4"); // 回数

        // 成績一覧を格納するリスト
        List<Test> testlist = new ArrayList<>();

        // 科目名（画面表示用）
        String subject_name = "";

        // ===== 検索条件が揃っている場合のみ成績を取得 =====

        if (f1 != null && f2 != null && f3 != null && f4 != null &&
            !f1.equals("0") && !f2.equals("0") && !f3.equals("0") && !f4.equals("0")) {

            // 入学年度と回数を数値に変換
            int entYear = Integer.parseInt(f1);
            int count = Integer.parseInt(f4);

            // 科目名を取得
            for (Subject s : list) {
                if (s.getId().equals(f3)) {
                    subject_name = s.getName();
                }
            }

            // ===== ① 入学年度＋クラスで学生一覧を取得 =====

            List<Student> students =
                    studentDao.filter(teacher.getSchool(), entYear, f2, true);

            // ===== ② 学生ごとに成績を取得して Test オブジェクトを作成 =====

            for (Student stu : students) {

                // 既存の成績を取得
                Test test = testDao.get(teacher.getSchool(), stu.getNo(), f3, count);

                // 成績が存在しない場合は空の Test を作成
                if (test == null) {
                    test = new Test();
                    test.setStudent(stu);
                    test.setClassNum(stu.getClassNum());
                    test.setSubject(new Subject(f3, subject_name));
                    test.setCount(count);
                    test.setPoint(0); // 未入力扱い
                }

                // リストに追加
                testlist.add(test);
            }
        }

        // ===== JSPへ値を渡す =====

        req.setAttribute("entYearList", entYearList);
        req.setAttribute("cNumList", cNumList);
        req.setAttribute("list", list);
        req.setAttribute("countList", countList);

        req.setAttribute("testlist", testlist);
        req.setAttribute("subject_name", subject_name);

        // 検索条件保持
        req.setAttribute("f1", f1);
        req.setAttribute("f2", f2);
        req.setAttribute("f3", f3);
        req.setAttribute("f4", f4);

        // 画面表示
        req.getRequestDispatcher("/scoremanager/main/test_regist.jsp").forward(req, res);
    }
}
