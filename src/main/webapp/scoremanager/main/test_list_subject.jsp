<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>

<c:import url="/common/base.jsp">
    <c:param name="title">得点管理システム</c:param>

    <c:param name="content">
        <section class="me-4">
            <h2 class="h3 mb-3 fw-normal bg-secondary bg-opacity-10 py-2 px-4">
                成績一覧（科目）
            </h2>

            <div class="border border-bottom mx-3 mb-2 px-3 py-2 align-items-center rounded" id="filter">

                <!-- 科目別検索フォーム -->
                <form action="TestListSubjectExecute.action" method="get">
                    <div class="row">
                        <div class="col-2 mt-4 text-center">
                            <p>科目情報</p>
                        </div>

                        <div class="col-2">
                            <label class="form-label">入学年度</label>
                            <select class="form-select" name="f1">
                                <option value="0">--------</option>
                                <c:forEach var="year" items="${entYearSet}">
                                    <option value="${year}" <c:if test="${year==f1}">selected</c:if>>
                                        ${year}
                                    </option>
                                </c:forEach>
                            </select>
                        </div>

                        <div class="col-2">
                            <label class="form-label">クラス</label>
                            <select class="form-select" name="f2">
                                <option value="0">--------</option>
                                <c:forEach var="num" items="${cNumlist}">
                                    <option value="${num}" <c:if test="${num==f2}">selected</c:if>>
                                        ${num}
                                    </option>
                                </c:forEach>
                            </select>
                        </div>

                        <div class="col-4">
                            <label class="form-label">科目</label>
                            <select class="form-select" name="f3">
                                <option value="0">--------</option>
                                <c:forEach var="subject" items="${list}">
                                    <option value="${subject.id}" <c:if test="${subject.id==f3}">selected</c:if>>
                                        ${subject.name}
                                    </option>
                                </c:forEach>
                            </select>
                        </div>

                        <div class="col-2 mt-3 text-center">
                            <input type="hidden" name="f" value="sj">
                            <button class="btn btn-secondary">検索</button>
                        </div>
                    </div>

                    <!-- エラー表示を -->
                    <c:if test="${not empty error}">
                        <div class="mt-3 text-warning text-start">
                            ${error}
                        </div>
                    </c:if>
                </form>

                <div class="row border-bottom mx-0 mb-3 py-2 align-items-center"></div>

                <!-- 学生別検索フォーム -->
                <form action="TestListStudentExecute.action" method="get">
                    <div class="row">
                        <div class="col-2 mt-3 text-center">
                            <p>学生情報</p>
                        </div>

                        <div class="col-4">
                            <label class="form-label">学生番号</label>
                            <input class="form-control" type="text" name="f4"
                                   value="${f4}" required maxlength="10"
                                   placeholder="学生番号を入力してください"/>
                        </div>

                        <div class="col-2 mt-3 text-center">
                            <input type="hidden" name="f" value="st">
                            <button class="btn btn-secondary">検索</button>
                        </div>
                    </div>
                </form>
            </div>

            <!-- 成績一覧表示 -->
            <c:choose>
                <c:when test="${not empty tlslist and tlslist.size() > 0}">
                    <div>科目：${subject_name}</div>

                    <table class="table table-hover">
                        <tr>
                            <th>入学年度</th>
                            <th>クラス</th>
                            <th>学生番号</th>
                            <th>氏名</th>
                            <th>１回</th>
                            <th>２回</th>
                        </tr>

                        <c:forEach var="test" items="${tlslist}">
                            <tr>
                                <td>${test.entYear}</td>
                                <td>${test.classNum}</td>
                                <td>${test.studentNo}</td>
                                <td>${test.studentName}</td>

                                <td>
                                    <c:choose>
                                        <c:when test="${test.points.get('1') != null}">
                                            ${test.points.get('1')}
                                        </c:when>
                                        <c:otherwise>-</c:otherwise>
                                    </c:choose>
                                </td>

                                <td>
                                    <c:choose>
                                        <c:when test="${test.points.get('2') != null}">
                                            ${test.points.get('2')}
                                        </c:when>
                                        <c:otherwise>-</c:otherwise>
                                    </c:choose>
                                </td>
                            </tr>
                        </c:forEach>
                    </table>
                </c:when>

                <c:otherwise>
                    <c:if test="${empty error}">
                        <div>学生情報が存在しませんでした</div>
                    </c:if>
                </c:otherwise>
            </c:choose>

        </section>
    </c:param>
</c:import>
