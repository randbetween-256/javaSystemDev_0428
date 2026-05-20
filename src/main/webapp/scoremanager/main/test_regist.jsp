<%-- 成績管理一覧JSP --%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>

<%-- base.jsp を読み込み、title / scripts / content を渡す --%>
<c:import url="/common/base.jsp">
    
    <%-- 画面タイトル --%>
    <c:param name="title">得点管理システム</c:param>

    <%-- 追加スクリプト --%>
    <c:param name="scripts"></c:param>

    <%-- base.jsp の content 部分に入るメインコンテンツ --%>
    <c:param name="content">

        <section>

            <%-- 見出し --%>
            <h2 class="h3 mb-3 fw-norma bg-secondary bg-opacity-10 py-2 px-4">成績管理</h2>

            <%--      検索フォーム         --%>
            <form method="get">

                <div class="row border mx-3 mb-3 py-2 align-items-center rounded" id="filter">

                    <%-- 入学年度選択 --%>
                    <div class="col-2">
                        <label class="form-label" for="student-f1-select">入学年度</label>
                        <select class="form-select" id="student-f1-select" name="f1">
                            <option value="0">--------</option>

                            <%-- entYearList の年度一覧を表示 --%>
                            <c:forEach var="year" items="${entYearList}">
                                <option value="${year}"
                                    <c:if test="${year == f1}">selected</c:if>>
                                    ${year}
                                </option>
                            </c:forEach>
                        </select>
                    </div>

                    <%-- クラス選択 --%>
                    <div class="col-2">
                        <label class="form-label" for="student-f2-select">クラス</label>
                        <select class="form-select" id="student-f2-select" name="f2">
                            <option value="0">--------</option>

                            <%-- cNumList のクラス一覧を表示 --%>
                            <c:forEach var="num" items="${cNumList}">
                                <option value="${num}"
                                    <c:if test="${num == f2}">selected</c:if>>
                                    ${num}
                                </option>
                            </c:forEach>
                        </select>
                    </div>

                    <%-- 科目選択 --%>
                    <div class="col-4">
                        <label class="form-label" for="student-f3-select">科目</label>
                        <select class="form-select" id="student-f3-select" name="f3">
                            <option value="0">--------</option>

                            <%-- list の科目一覧を表示 --%>
                            <c:forEach var="subject" items="${list}">
                                <option value="${subject.id}"
                                    <c:if test="${subject.id == f3}">selected</c:if>>
                                    ${subject.name}
                                </option>
                            </c:forEach>
                        </select>
                    </div>

                    <%-- 回数選択 --%>
                    <div class="col-2">
                        <label class="form-label" for="student-f4-select">回数</label>
                        <select class="form-select" id="student-f4-select" name="f4">
                            <option value="0">--------</option>

                            <%-- countList の回数一覧を表示 --%>
                            <c:forEach var="num" items="${countList}">
                                <option value="${num}"
                                    <c:if test="${num == f4}">selected</c:if>>
                                    ${num}
                                </option>
                            </c:forEach>
                        </select>
                    </div>

                    <%-- 検索ボタン --%>
                    <div class="col-2 text-center">
                        <button class="btn btn-secondary" id="filter-button">検索</button>
                    </div>

                    <%-- 入力エラー表示（科目・回数の組み合わせ不正など） --%>
                    <div class="mt-2 text-warning">${errors.get("a")}</div>

                </div>
            </form>

            <%--      成績登録フォーム     --%>
            
            <form action="TestRegistExecute.action" method="get">

                <%-- 検索条件を保持する hidden パラメータ --%>
                <input type="hidden" name="f1" value="${f1}">
                <input type="hidden" name="f2" value="${f2}">
                <input type="hidden" name="f3" value="${f3}">
                <input type="hidden" name="f4" value="${f4}">

                <c:choose>

                    <%-- 成績データが存在する場合 --%>
                    <c:when test="${testlist.size() > 0}">

                        <div>科目：${subject_name}（${f4}回）</div>

                        <table class="table table-hover">
                            <tr>
                                <th>入学年度</th>
                                <th>クラス</th>
                                <th>学生番号</th>
                                <th>氏名</th>
                                <th>点数</th>
                            </tr>

                            <%-- testlist の内容を1件ずつ表示 --%>
                            <c:forEach var="test" items="${testlist}" varStatus="st">
                                <tr>
                                    <td>${test.student.entYear}</td>
                                    <td>${test.classNum}</td>
                                    <td>${test.student.no}</td>
                                    <td>${test.student.name}</td>

                                    <%-- 点数入力欄 --%>
                                    <td>
                                        <input type="text"
                                            name="point_${test.student.no}"
                                            <c:if test="${test.no != 0}">value="${test.point}"</c:if>>
                                        
                                        <%-- 点数入力エラー表示 --%>
                                        <div class="mt-2 text-warning">${errors.get(st.count)}</div>
                                    </td>
                                </tr>

                                <%-- 学生番号とクラス番号を hidden で保持 --%>
                                <input type="hidden" name="regist" value="${test.student.no}">
                                <input type="hidden" name="class_num_${test.student.no}" value="${test.classNum}">
                            </c:forEach>
                        </table>

                        <%-- 科目ID・回数を hidden で保持 --%>
                        <input type="hidden" name="count" value="${f4}">
                        <input type="hidden" name="subject" value="${f3}">

                        <%-- 登録ボタン --%>
                        <div class="col-2 text-center">
                            <button class="btn btn-secondary">登録して終了</button>
                        </div>

                    </c:when>

                </c:choose>

            </form>

        </section>

    </c:param>
</c:import>
