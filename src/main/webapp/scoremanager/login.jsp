<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>

<c:import url="/common/base.jsp">
    <c:param name="title">得点管理システム</c:param>

    <c:param name="content">
        <section>
            <h2 class="h3 mb-3 fw-normal bg-secondary bg-opacity-10 py-2 px-4 text-center">
                ログイン
            </h2>

            <div class="container mt-5">
                <div class="row justify-content-center">
                    <div class="col-4">

                        <%-- 認証エラー表示 --%>
                        <c:if test="${not empty error}">
                            <p class="text-center mb-4" style="color: black; font-size: 1.1rem;">
                                ${error}
                            </p>
                        </c:if>

                        <%-- ログインボタン押下 → LoginExecute.action へ遷移 --%>
                        <form action="/javaSystemDev_0428/LoginExecute.action" method="post">

                            <%-- ID入力 --%>
                            <div class="mb-3">
                                <label class="form-label">ID</label>
                                <input type="text" name="id" class="form-control"
                                       value="${id}" required>
                            </div>

                            <%-- パスワード入力 --%>
                            <div class="mb-3">
                                <label class="form-label">パスワード</label>
                                <input type="password" name="password" id="password"
                                       class="form-control" required>

                                <%-- パスワード表示チェック操作 --%>
                                <div class="form-check mt-2">
                                    <input type="checkbox" id="togglePassword"
                                           class="form-check-input">
                                    <label for="togglePassword" class="form-check-label">
                                        パスワードを表示
                                    </label>
                                </div>
                            </div>

                            <button type="submit" class="btn btn-primary w-100">
                                ログイン
                            </button>
                        </form>

                    </div>
                </div>
            </div>
        </section>

        <script>
            document.getElementById("togglePassword").addEventListener("change", function () {
                const pw = document.getElementById("password");
                pw.type = this.checked ? "text" : "password";
            });
        </script>
    </c:param>
</c:import>
