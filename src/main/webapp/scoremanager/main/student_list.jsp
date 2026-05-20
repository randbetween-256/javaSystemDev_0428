<%-- 学生一覧JSP --%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<%-- base.jsp を読み込み、title / scripts / content を渡す --%>
<c:import url="/common/base.jsp" >
    
    <%-- 画面タイトル --%>
    <c:param name="title">
        得点管理システム
    </c:param>

    <c:param name="scripts"></c:param>

    <%-- base.jsp の content 部分に入るメインコンテンツ --%>
    <c:param name="content">

        <section class="me=4">

            <%-- 見出し --%>
            <h2 class="h3 mb-3 fw-norma bg-secondary bg-opacity-10 py-2 px-4">学生管理</h2>

            <%-- 新規登録リンク --%>
            <div class="my-2 text-end px-4">
                <a href="StudentCreate.action">新規登録</a>
            </div>

            <%-- 絞り込みフォーム（GET送信） --%>
            <form method="get">

                <%-- 絞り込み条件エリア --%>
                <div class="row border mx-3 mb-3 py-2 align-items-center rounded" id="filter">

                    <%-- 入学年度選択 --%>
                    <div class="col-4">
                        <label class="form-label" for="student-f1-select">入学年度</label>
                        <select class="form-select" id="student-f1-select" name="f1">
                            <option value="0">--------</option>

                            <%-- ent_year_set の年度一覧を表示 --%>
                            <c:forEach var="year" items="${ent_year_set }">

                                <%-- 選択されていた年度と一致していれば selected を付与 --%>
                                <option value="${year }" 
                                    <c:if test="${year==f1 }">selected</c:if>>
                                    ${year }
                                </option>

                            </c:forEach>
                        </select>
                    </div>

                    <%-- クラス選択 --%>
                    <div class="col-4">
                        <label class="form-label" for="student-f2-select">クラス</label>
                        <select class="form-select" id="student-f2-select" name="f2">
                            <option value="0">--------</option>

                            <%-- class_num_set のクラス一覧を表示 --%>
                            <c:forEach var="num" items="${class_num_set }">

                                <%-- 選択されていたクラスと一致していれば selected を付与 --%>
                                <option value="${num }" 
                                    <c:if test="${num==f2 }">selected</c:if>>
                                    ${num }
                                </option>

                            </c:forEach>
                        </select>
                    </div>

                    <%-- 在学中チェックボックス --%>
                    <div class="col-2 form-check text-center">
                        <label class="form-check-label" for="student-f3-check">在学中
                            
                            <%-- f3 が存在していれば checked を付与 --%>
                            <input class="form-check-input" type="checkbox"
                                id="student-f3-check" name="f3" value="t"
                                <c:if test="${!empty f3 }">checked</c:if> />
                        </label>
                    </div>

                    <%-- 絞り込みボタン --%>
                    <div class="col-2 text-center">
                        <button class="btn btn-secondary" id="filter-button">絞込み</button>
                    </div>

                    <%-- クラスのみ指定した場合のエラー表示 --%>
                    <div class="mt-2 text-warning">${errors.get("f1") }</div>

                </div>
            </form>

            <%-- 学生一覧表示エリア --%>
            <c:choose>

                <%-- 学生が1件以上存在する場合 --%>
                <c:when test="${students.size()>0 }">

                    <div>検索結果：${students.size() }件</div>

                    <table class="table table-hover">
                        <tr>
                            <th>入学年度</th>
                            <th>学生番号</th>
                            <th>氏名</th>
                            <th>クラス</th>
                            <th class="text-center">在学中</th>
                            <th></th>
                        </tr>

                        <%-- students の内容を1件ずつ表示 --%>
                        <c:forEach var="student" items="${students }">
                            <tr>
                                <td>${student.entYear }</td>
                                <td>${student.no }</td>
                                <td>${student.name }</td>
                                <td>${student.classNum }</td>

                                <%-- 在学中フラグ表示 --%>
                                <td class="text-center">
                                    <c:choose>
                                        <c:when test="${student.isAttend() }">◯</c:when>
                                        <c:otherwise>×</c:otherwise>
                                    </c:choose>
                                </td>

                                <%-- 変更リンク --%>
                                <td>
                                    <a href="StudentUpdate.action?no=${student.no }">変更</a>
                                </td>
                            </tr>
                        </c:forEach>

                    </table>
                </c:when>

                <%-- 学生が0件の場合 --%>
                <c:otherwise>
                    <div>学生情報が存在しませんでした。</div>
                </c:otherwise>

            </c:choose>

        </section>

    </c:param>
</c:import>
