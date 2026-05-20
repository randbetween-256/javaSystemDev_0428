<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>

<c:import url="/common/base.jsp">
    <c:param name="title">得点管理システム</c:param>

    <c:param name="content">
        <section class="me-4">
            <h2 class="h3 mb-3 fw-normal bg-secondary bg-opacity-10 py-2 px-4">
                成績一覧（学生）
            </h2>

            <div class="border border-bottom mx-3 mb-2 px-3 py-2 align-items-center rounded" id="filter">

                <%-- ：科目別検索ボタン押下 --%>
                <form action="TestListSubjectExecute.action" method="get">
                    <div class="row">
                        <div class="col-2 mt-4 text-center">
                            <p>科目情報</p>
                        </div>

                        <!-- 入学年度 -->
                        <div class="col-2">
                            <label class="form-label">入学年度</label>
                            <select class="form-select" name="f1">
                                <option value="0">--------</option>
                                <c:forEach var="year" items="${entYearSet}">
                                    <option value="${year}" <c:if test="${year == f1}">selected</c:if>>
                                        ${year}
                                    </option>
                                </c:forEach>
                            </select>
                        </div>

                        <!-- クラス -->
                        <div class="col-2">
                            <label class="form-label">クラス</label>
                            <select class="form-select" name="f2">
                                <option value="0">--------</option>
                                <c:forEach var="num" items="${cNumlist}">
                                    <option value="${num}" <c:if test="${num == f2}">selected</c:if>>
                                        ${num}
                                    </option>
                                </c:forEach>
                            </select>
                        </div>

                        <!-- 科目 -->
                        <div class="col-4">
                            <label class="form-label">科目</label>
                            <select class="form-select" name="f3">
                                <option value="0">--------</option>
                                <c:forEach var="subject" items="${list}">
                                    <option value="${subject.id}" <c:if test="${subject.id == f3}">selected</c:if>>
                                        ${subject.name}
                                    </option>
                                </c:forEach>
                            </select>
                        </div>

                        <div class="col-2 mt-3 text-center">
                            <input type="hidden" name="f" value="sj">
                            <button class="btn btn-secondary">検索</button>
                        </div>

                        <div class="mt-2 text-warning">${errors.get("1")}</div>
                    </div>
                </form>

                <div class="row border-bottom mx-0 mb-3 py-2 align-items-center"></div>

                <%-- 学生別検索ボタン押下 --%>
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

            <!-- 学生別一覧表示 -->
            <div>氏名：${student.name}（${f4}）</div>

            <c:choose>
                <c:when test="${tlslist.size() > 0}">
                    <table class="table table-hover">
                        <tr>
                            <th>科目名</th>
                            <th>科目コード</th>
                            <th>回数</th>
                            <th>点数</th>
                        </tr>

                        <c:forEach var="test" items="${tlslist}">
                            <c:if test="${test.point != null}">
                                <tr>
                                    <td>${test.subjectName}</td>
                                    <td>${test.subjectCd}</td>
                                    <td>${test.num}</td>
                                    <td>${test.point}</td>
                                </tr>
                            </c:if>
                        </c:forEach>

                    </table>
                </c:when>

                <c:otherwise>
                    <div>成績情報が存在しませんでした</div>
                </c:otherwise>
            </c:choose>

        </section>
    </c:param>
</c:import>
