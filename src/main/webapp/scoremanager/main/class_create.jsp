<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<c:import url="/common/base.jsp">
    <c:param name="title">得点管理システム</c:param>

    <c:param name="content">
        <section>
            <h2 class="h3 mb-3 fw-normal bg-secondary bg-opacity-10 py-2 px-4">クラス情報登録</h2>

            <!-- 登録ボタン押下 → ClassCreateExecute.action -->
            <form action="ClassCreateExecute.action" method="post">

                <!-- クラス番号 -->
                <div class="mb-3">
                    <label for="classNum">クラス番号</label>
                    <input class="form-control" type="text" id="classNum" name="classNum"
                           value="${classNum}" required maxlength="10"
                           placeholder="クラス番号を入力してください" />
                    <div class="text-warning">${errors.get("classNum")}</div>
                </div>

                <!-- 登録ボタン -->
                <div class="text-center mt-4">
                    <button class="btn btn-secondary" type="submit">登録して終了</button>
                </div>
            </form>

            <!-- 戻るリンク → ClassList.action -->
            <div class="mt-3">
                <a href="ClassList.action">戻る</a>
            </div>
        </section>
    </c:param>
</c:import>
