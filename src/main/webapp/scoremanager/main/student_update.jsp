<%-- 学生情報変更JSP --%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>

<c:import url="/common/base.jsp">
    <c:param name="title">得点管理システム</c:param>

    <c:param name="scripts"></c:param>

    <c:param name="content">

        <section>
            <h2 class="h3 mb-3 fw-norma bg-secondary bg-opacity-10 py-2 px-4">
                学生情報変更
            </h2>

            <!-- 変更処理へ POST -->
            <form action="StudentUpdateExecute.action" method="post">

                <!-- 入学年度 -->
                <div class="mx-auto py-2">
                    <label for="ent_year">入学年度</label><br>
                    <input class="border border-0 ps-3" type="text"
                           id="ent_year" name="ent_year"
                           value="${ent_year}" readonly />
                </div>

                <!-- 学生番号（変更不可） -->
                <div class="mx-auto py-2">
                    <label for="no">学生番号</label><br>
                    <input class="border border-0 ps-3" type="text"
                           id="no" name="no"
                           value="${no}" readonly />
                </div>

                <!-- 氏名 -->
                <div class="mx-auto py-2">
                    <label for="name">氏名</label><br>
                    <input class="form-control" type="text"
                           id="name" name="name"
                           value="${name}" required maxlength="30" />
                </div>

                <!-- クラス -->
                <div class="mx-auto py-2">
                    <label for="class_num">クラス</label><br>
                    <select class="form-select" id="class_num" name="class_num">
                        <c:forEach var="num" items="${class_num_set}">
                            <option value="${num}"
                                <c:if test="${num == class_num}">selected</c:if>>
                                ${num}
                            </option>
                        </c:forEach>
                    </select>
                </div>

                <!-- 在学中 -->
                <div class="mx-auto py-2">
                    <label for="is_attend">在学中</label>
                    <input type="checkbox" id="is_attend" name="is_attend"
                           <c:if test="${is_attend}">checked</c:if> />
                </div>

                <!-- 変更ボタン -->
                <div class="mx-auto py-2">
                    <input class="btn btn-primary" type="submit" value="変更" />
                </div>

            </form>

            <!-- 戻る -->
            <a href="StudentList.action">戻る</a>

        </section>

    </c:param>
</c:import>
