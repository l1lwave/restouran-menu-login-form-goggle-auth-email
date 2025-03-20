<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
    <head>
        <title>My Order</title>
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css">
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
        <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>
    </head>

    <body>
        <div class="container">
           <h3><img height="50" width="55" src="<c:url value="/static/logo.png"/>"/><a href="/">My Order</a></h3>

            <nav class="navbar navbar-default">
                <div class="container-fluid">
                    <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
                        <ul id="groupList" class="nav navbar-nav">
                            <li><button type="button" id="delete_from_order" class="btn btn-default navbar-btn">Delete from order</button></li>
                        </ul>
                    </div><!-- /.navbar-collapse -->
                </div><!-- /.container-fluid -->
            </nav>

            <table class="table table-striped">
                <thead>
                <tr>
                    <td></td>
                    <td><b>Name</b></td>
                    <td><b>Price</b></td>
                    <td><b>Discount</b></td>
                    <td><b>Weight</b></td>
                </tr>
                </thead>
                <c:forEach items="${dishesOrder}" var="dish">
                    <tr>
                        <td><input type="checkbox" name="toDelete[]" value="${dish.id}" id="checkbox_${dish.id}"/></td>
                        <td>${dish.name}</td>
                        <c:if test="${dish.discount ne 0}">
                            <td>
                                <h style="text-decoration: line-through;">${dish.price} $</h>
                                    ${dish.price - (dish.price * (dish.discount / 100))} $ (
                                    ${(dish.price - (dish.price * (dish.discount / 100))) * rate} UAH)
                            </td>
                        </c:if>
                        <c:if test="${dish.discount eq 0}">
                            <td>${dish.price} $ (
                                    ${dish.price * rate} UAH)</td>
                        </c:if>
                        <td>${dish.discount} %</td>
                        <td>${dish.weight} g</td>
                    </tr>
                </c:forEach>
            </table>
        </div>

        <script>

            $('#delete_from_order').click(function(){
                let data = { 'toDelete[]' : []};
                $(":checked").each(function() {
                    data['toDelete[]'].push($(this).val());
                });
                $.post("/dish/orderDelete", data, function(data, status) {
                    window.location.reload();
                });
            });
        </script>
    </body>
</html>