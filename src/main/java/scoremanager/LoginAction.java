package scoremanager;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import tool.Action;

public class LoginAction extends Action {

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {

        // 既存セッションを破棄
        HttpSession session = req.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        // 正しいパスへリダイレクト
        res.sendRedirect(req.getContextPath() + "/scoremanager/login.jsp");
    }
}
