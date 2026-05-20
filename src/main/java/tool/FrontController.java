package tool;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = { "*.action" })
public class FrontController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        try {
            // URL の最後の部分だけ取り出す
            // 例: /scoremanager/main/TestRegist.action → TestRegist
            String path = req.getServletPath();
            String name = path.substring(path.lastIndexOf("/") + 1, path.length() - 7);

            // 探すパッケージ候補（あなたの構成に合わせてある）
            String[] candidates = {
                "scoremanager.main." + name + "Action",
                "scoremanager.login." + name + "Action",
                "scoremanager." + name + "Action"
            };

            Class<?> clazz = null;

            for (String className : candidates) {
                try {
                    clazz = Class.forName(className);
                    break;
                } catch (ClassNotFoundException e) {
                    // 次を試す
                }
            }

            if (clazz == null) {
                throw new ClassNotFoundException("Action class not found: " + name);
            }

            Action action = (Action) clazz.getDeclaredConstructor().newInstance();
            action.execute(req, res);

        } catch (Exception e) {
            e.printStackTrace();
            req.getRequestDispatcher("/error.jsp").forward(req, res);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        doGet(req, res);
    }
}
