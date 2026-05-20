<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>

<c:import url="/common/base.jsp">
    <c:param name="title">得点管理システム</c:param>

    <c:param name="scripts"></c:param>

    <c:param name="content">
        <section class="me-4">
            <h2 class="h3 mb-3 fw-norma bg-secondary bg-opacity-10 py-2 px-4">メニュー</h2>

            <div class="row text-center px-4 fs-3 my-5">

                <%-- 学生管理 --%>
                <div class="col d-flex align-items-center justify-content-center mx-2 rounded shadow"
                    style="height: 10rem; background-color: #dbb;">
                    <a href="/javaSystemDev_0428/scoremanager/main/StudentList.action">学生管理</a>
                </div>

                <%-- 成績管理 --%>
                <div class="col d-flex align-items-center justify-content-center mx-2 rounded shadow"
                    style="height: 10rem; background-color: #bdb;">
                    <div>
                        <div class="">成績管理</div>

                        <div class="">
                            <a href="/javaSystemDev_0428/scoremanager/main/TestRegist.action">成績登録</a>
                        </div>

                        <div class="">
                            <a href="/javaSystemDev_0428/scoremanager/main/TestList.action">成績参照</a>
                        </div>
                    </div>
                </div>

                <%-- 科目管理 --%>
                <div class="col d-flex align-items-center justify-content-center mx-2 rounded shadow"
                    style="height: 10rem; background-color: #bbd;">
                    <a href="/javaSystemDev_0428/scoremanager/main/SubjectList.action">科目管理</a>
                </div>

                <%-- クラス管理 --%>
                <div class="col d-flex align-items-center justify-content-center mx-2 rounded shadow"
                    style="height: 10rem; background-color: #ddb;">
                    <a href="/javaSystemDev_0428/scoremanager/main/ClassList.action">クラス管理</a>
                </div>

            </div>
        </section>
    </c:param>
</c:import>
