<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>

<c:import url="/common/base.jsp">
    <c:param name="title">
        得点管理システム
    </c:param>

    <c:param name="content">
        <section>
            <h2 class="h3 mb-3 fw-norma bg-secondary bg-opacity-10 py-2 px-4">科目情報変更</h2>

            <%-- 変更ボタン押下 → SubjectUpdateExecute.action --%>
            <form action="SubjectUpdateExecute.action" method="post">

                <!-- 科目コード -->
                <div class="mx-auto py-2">
                    <label for="cd">科目コード</label><br>
                    <input class="border border-0 ps-3" type="text" id="cd"
                           name="cd" value="${cd}" readonly />
                </div>

                <!-- 科目名 -->
                <div class="mx-auto py-2">
                    <label for="name">科目名</label><br>
                    <input class="form-control" type="text" id="name" name="name"
                           value="${name}" required maxlength="20" />
                    <div class="text-warning">${errors.get("name")}</div>
                </div>

                <!-- 変更ボタン -->
                <div class="mx-auto py-2">
                    <input class="btn btn-primary" type="submit" value="変更" />
                </div>
            </form>

            <%-- 戻るリンク押下 → SubjectList.action --%>
            <a href="SubjectList.action">戻る</a>
        </section>
    </c:param>
</c:import>
