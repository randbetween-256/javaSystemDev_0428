<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<c:import url="/common/base.jsp">
    <c:param name="title">得点管理システム</c:param>

    <c:param name="content">
        <section>
            <h2 class="h3 mb-3 fw-normal bg-secondary bg-opacity-10 py-2 px-4">学生情報登録</h2>

            <%-- 登録して終了ボタン押下 → StudentCreateExecute.action --%>
            <form action="StudentCreateExecute.action" method="post">

                <!-- 入学年度 -->
                <div class="mb-3">
                    <label for="ent_year">入学年度</label>
                    <select class="form-select" id="ent_year" name="ent_year">
                        <option value="0">--------</option>
                        <c:forEach var="year" items="${ent_year_set}">
                            <option value="${year}" <c:if test="${year == ent_year}">selected</c:if>>
                                ${year}
                            </option>
                        </c:forEach>
                    </select>
                    <div class="text-warning">${errors.get("ent_year")}</div>
                </div>

                <!-- 学生番号 -->
                <div class="mb-3">
                    <label for="no">学生番号</label>
                    <input class="form-control" type="text" id="no" name="no"
                           value="${no}" required maxlength="10"
                           placeholder="学生番号を入力してください" />
                    <div class="text-warning">${errors.get("student_no")}</div>
                </div>

                <!-- 氏名 -->
                <div class="mb-3">
                    <label for="name">氏名</label>
                    <input class="form-control" type="text" id="name" name="name"
                           value="${name}" required maxlength="30"
                           placeholder="氏名を入力してください" />
                </div>

                <!-- クラス -->
                <div class="mb-3">
                    <label for="class_num">クラス</label>
                    <select class="form-select" id="class_num" name="class_num">
                        <c:forEach var="num" items="${class_num_set}">
                            <option value="${num}" <c:if test="${num == class_num}">selected</c:if>>
                                ${num}
                            </option>
                        </c:forEach>
                    </select>
                </div>

                <!-- 登録ボタン -->
                <div class="mt-4">
                    <button class="btn btn-secondary" type="submit">登録して終了</button>
                </div>
            </form>

            <!-- 戻るリンク押下 → StudentList.action -->
            <div class="mt-3">
                <a href="StudentList.action">戻る</a>
            </div>
        </section>
    </c:param>
</c:import>
