<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="en">
<script src="https://cdn.jsdelivr.net/npm/chart.js@2.8.0"></script>

<jsp:include page="admin_header.jsp" />

<body data-target="#nino-navbar" data-spy="scroll">
	
	<div id="nums">
		<c:forEach var="c" items="${categoryList}">
			<input type="hidden" value="${c.CATEGORY_NUM}" class="cat_num">
		</c:forEach>
	</div>
	
	<c:forEach var="c" items="${categoryList}">
		<input type="hidden" value="${c.CATEGORY_NAME}" class="cat_name">
	</c:forEach>
	
	<c:forEach var="ca" items="${categoryCount}">
		<input type="hidden" value="${ca}" class="inter_count">
	</c:forEach>
	
	<c:forEach var="bo" items="${boardCount}">
		<input type="hidden" value="${bo}" class="board_count">
	</c:forEach>
	
	
	<div class="container admin_out">
		<nav class="admin_nav" id="nino-navbar" class="navbar navbar-default" role="navigation">
			<ul id="admin_ul" class="nav navbar-nav">
				<li><a href="dash.ad">대시보드</a></li>
				<li><a href="user_list.ad">회원관리</a></li>
				<li><a href="report.ad">신고관리</a></li>
			</ul>
		</nav>
	</div>
	
	<div class="dashdiv">
		<div class="pie">
			<h2 class="pie_title">회원 흥미분야 통계</h2>
			<canvas id="myChart"></canvas>
		</div>
		
		<div class="bar">
			<h2 class="bar_title">월별 게시물 총 갯수</h2>
			<canvas id="myBarChart"></canvas>
		</div>
	</div>
	
	
	<script type="text/javascript" src="resources/js/chart.js"></script>
</body>
</html>