<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<c:import url="/common/base.jsp">
    <c:param name="title">得点管理システム</c:param>

    <c:param name="content">
        <section>
            <h2 class="h3 mb-3 fw-normal bg-secondary bg-opacity-10 py-2 px-4">科目情報削除</h2>

            <p class="px-4">
                「${subject.name}（${subject.id}）」を削除してもよろしいですか？
            </p>

            <%-- 削除ボタン押下 → SubjectDeleteExecute.action --%>
            <form action="SubjectDeleteExecute.action" method="post" class="px-4">
                <input type="hidden" name="cd" value="${subject.id}" />
                <button class="btn btn-danger" type="submit">削除</button>
            </form>

            <%-- 戻るリンク押下 → SubjectList.action --%>
            <div class="px-4 mt-3">
                <a href="SubjectList.action">戻る</a>
            </div>
        </section>
    </c:param>
</c:import>
