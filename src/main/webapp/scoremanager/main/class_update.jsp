<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>

<c:import url="/common/base.jsp">
    <c:param name="title">得点管理システム</c:param>

    <c:param name="content">
        <section>
            <h2 class="h3 mb-3 fw-norma bg-secondary bg-opacity-10 py-2 px-4">クラス情報変更</h2>

            <!-- 変更ボタン押下 → ClassUpdateExecute.action -->
            <form action="ClassUpdateExecute.action" method="post">

                <!-- 現在のクラス番号 -->
                <div class="mx-auto py-2">
                    <label for="oldClassNum">現在のクラス番号</label><br>
                    <input class="border border-0 ps-3" type="text" id="oldClassNum"
                           name="oldClassNum" value="${oldClassNum}" readonly />
                </div>

                <!-- 新しいクラス番号 -->
                <div class="mx-auto py-2">
                    <label for="newClassNum">新しいクラス番号</label><br>
                    <input class="form-control" type="text" id="newClassNum" name="newClassNum"
                           value="${newClassNum}" required maxlength="10" />
                    <div class="text-warning">${errors.get("newClassNum")}</div>
                </div>

                <!-- 変更ボタン -->
                <div class="mx-auto py-2">
                    <input class="btn btn-primary" type="submit" value="変更" />
                </div>
            </form>

            <!-- 戻るリンク -->
            <a href="ClassList.action">戻る</a>
        </section>
    </c:param>
</c:import>
