package scoremanager.main;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import tool.Action;

public class LogoutAction extends Action {

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {

        // 既存のセッションを取得（存在しない場合は null が返る）
        HttpSession session = req.getSession(false);

        // セッションが存在する場合は破棄してログアウト状態にする
        if (session != null) {
            session.invalidate();
        }

        // logout.jsp へリダイレクトしてログアウト完了画面を表示する
        res.sendRedirect(
                req.getContextPath()
                + "/scoremanager/main/logout.jsp");
    }
}
