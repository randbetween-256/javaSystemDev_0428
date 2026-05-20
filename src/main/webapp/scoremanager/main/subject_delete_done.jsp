<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>

<c:import url="/common/base.jsp">
    <c:param name="title">得点管理システム</c:param>

    <c:param name="content">
        <div id="wrap_box">
            <h2 class="h3 mb-3 fw-normal bg-secondary bg-opacity-10 py-2">科目情報削除</h2>

            <div class="wrap_box">
                <p class="text-center" style="background-color:#8cc3a9">
                    削除が完了しました
                </p>

                <br><br><br>

                <%-- 科目一覧リンク押下 → SubjectList.action --%>
                <a href="SubjectList.action">科目一覧</a>
            </div>
        </div>
    </c:param>
</c:import>
