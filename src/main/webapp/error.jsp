<%-- エラーページ --%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<!DOCTYPE html>
<html lang="ja">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">

<!-- Bootstrap CSS -->
<link
    href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/css/bootstrap.min.css"
    rel="stylesheet"
    integrity="sha384-rbsA2VBKQhggwzxH7pPCaAqO46MgnOM80zW1RWuH61DGLwZJEdK2Kadq2F9CUG65"
    crossorigin="anonymous">

<title>エラーページ</title>

<script src="https://code.jquery.com/jquery-3.2.1.min.js"></script>
${param.scripts}
</head>

<body class="bg-light">
<div id="wrapper" class="container">

    <!-- ヘッダー -->
    <header
        class="d-flex flex-wrap justify-content-between align-items-center py-3 px-5 mb-4 border-bottom border-2 bg-primary bg-opacity-10 bg-gradient">

        <h1 class="fs-2 mb-0">得点管理システム</h1>

        <c:if test="${not empty user}">
            <div class="nav align-self-end">
                <span class="nav-item px-2">${user.name}様</span>
                <a class="nav-item px-2"
                   href="${pageContext.request.contextPath}/scoremanager/main/Logout.action">
                   ログアウト
                </a>
            </div>
        </c:if>
    </header>

    <!-- メイン -->
    <div class="row justify-content-center">

        <c:choose>

            <%-- ログイン済み --%>
            <c:when test="${not empty user}">
                <!-- 左側：メニュー -->
                <nav class="col-3 border-end">
                    <ul class="nav nav-pills flex-column mb-auto px-4">
                        <li class="nav-item"><a href="${pageContext.request.contextPath}/scoremanager/main/Menu.action" class="nav-link">メニュー</a></li>
                        <li class="nav-item"><a href="${pageContext.request.contextPath}/scoremanager/main/StudentList.action" class="nav-link">学生管理</a></li>
                        <li class="nav-item"><a href="${pageContext.request.contextPath}/scoremanager/main/TestList.action" class="nav-link">成績管理</a></li>
                        <li class="nav-item"><a href="${pageContext.request.contextPath}/scoremanager/main/TestRegist.action" class="nav-link">成績登録</a></li>
                        <li class="nav-item"><a href="${pageContext.request.contextPath}/scoremanager/main/TestListSubjectExecute.action" class="nav-link">成績参照</a></li>
                        <li class="nav-item"><a href="${pageContext.request.contextPath}/scoremanager/main/SubjectList.action" class="nav-link">科目管理</a></li>
                    </ul>
                </nav>

                <!-- 右側：エラーメッセージ -->
                <div class="col-8 text-start ps-5">
                    <p class="fs-5 mt-5">エラーが発生しました</p>

                    <c:if test="${not empty error}">
                        <div class="alert alert-warning mt-3">
                            ${error}
                        </div>
                    </c:if>
                </div>
            </c:when>

            <%-- 未ログイン --%>
            <c:otherwise>
                <div class="col-8 text-center">
                    <p class="fs-5 mt-5">エラーが発生しました</p>

                    <c:if test="${not empty error}">
                        <div class="alert alert-warning mt-3">
                            ${error}
                        </div>
                    </c:if>

                </div>
            </c:otherwise>

        </c:choose>

    </div>

    <!-- フッター -->
    <footer class="py-2 my-4 bg-dark bg-opacity-10 border-top border-3 text-muted">
        <p class="text-center mb-0">&copy; 2023 TIC</p>
        <p class="text-center mb-0">大原学園</p>
    </footer>

</div>
</body>
</html>
