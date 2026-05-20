<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<c:import url="/common/base.jsp">
    <c:param name="title">得点管理システム</c:param>

    <c:param name="content">
        <section>
            <h2 class="h3 mb-3 fw-normal bg-secondary bg-opacity-10 py-2 px-4">
                科目情報登録
            </h2>

            <form action="SubjectCreateExecute.action" method="post">

                <!-- 科目コード -->
                <div class="mb-3">
                    <label for="cd">科目コード</label>
                    <input class="form-control" type="text" id="cd" name="cd"
                           value="${cd}" required maxlength="3"
                           placeholder="科目コードを入力してください" />

                    <c:if test="${not empty errors}">
                        <div class="text-warning text-start">
                            ${errors.get("cd")}
                        </div>
                    </c:if>
                </div>

                <!-- 科目名 -->
                <div class="mb-3">
                    <label for="name">科目名</label>
                    <input class="form-control" type="text" id="name" name="name"
                           value="${name}" required maxlength="20"
                           placeholder="科目名を入力してください" />

                    <c:if test="${not empty errors}">
                        <div class="text-warning text-start">
                            ${errors.get("name")}
                        </div>
                    </c:if>
                </div>

                <!-- 登録ボタン -->
                <div class="text-center mt-4">
                    <button class="btn btn-secondary" type="submit">登録して終了</button>
                </div>
            </form>

            <div class="mt-3">
                <a href="SubjectList.action">戻る</a>
            </div>
        </section>
    </c:param>
</c:import>
