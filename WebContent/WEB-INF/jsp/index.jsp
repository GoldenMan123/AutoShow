<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/include.jsp" %>
<%@ include file="/WEB-INF/jsp/header.jsp" %>

<title>Главная страница</title>

<a href="cars.html"> 
	<div class="mybuttonl">
	Автомобили
	</div>
</a>

<a href="clients.html"> 
	<div class="mybuttonr">
	Клиенты
	</div>
</a>

<table>
<tr>
	<th>Номер заказа</th>
	<th>Регистрационный номер автомобиля</th>
	<th>Имя клиента</th>
	<th>Дата</th>
	<th>Тест-драйв</th>
	<th>Статус</th>
	<th></th>
</tr>

<c:forEach items="${orders}" var="order">
	<tr>
		<td>${order.number}</td>
		<td>${order.carId}</td>
		<td><a href="client.html?id=${order.clientId}">${order.client}</a></td>
	  	<td>${order.date}</td>
	  	<td>${order.testdrive}</td>
	  	<td>${order.status}</td>
	  	<td>
		  	<form:form modelAttribute="OrderStatus" method="POST" action="test.html?id=${order.number}">
		  		<form:select path="id" onchange="this.form.submit()">
					<c:forEach items="${statuses}" var="st">
						<option value="${st.id}">${st.status}</option>
					</c:forEach>
				</form:select>
			</form:form>
	  	</td>
	</tr>
</c:forEach>
</table>

<%@ include file="/WEB-INF/jsp/footer.jsp" %>