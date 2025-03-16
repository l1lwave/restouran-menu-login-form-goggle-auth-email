<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
    <head>
        <title>Prog Academy</title>
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css">
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
        <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>
    </head>

    <body>
        <div class="container">
           <h3><img height="50" width="55" src="<c:url value="/static/logo.png"/>"/>
               <a href="/">Restoran Menu</a>
               <a style="font-size: small" href="/profile">My Profile</a>
           </h3>
            <nav class="navbar navbar-default">
                <div class="container-fluid">
                    <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
                        <ul id="groupList" class="nav navbar-nav">
                            <li><button type="button" id="add_dish" class="btn btn-default navbar-btn">Add dish</button></li>
                            <li><button type="button" id="show_discount" class="btn btn-default navbar-btn">Show dishes on discount</button></li>
                            <li><button type="button" id="show_order" class="btn btn-default navbar-btn">Show order ${count}</button></li>
                            <li><button type="button" id="reset" class="btn btn-default navbar-btn">Reset</button></li>
                        </ul>
                        <form class="navbar-form navbar-left" role="search" action="/price" method="post">
                            <div class="form-group">
                                <input style="width: 100px" type="text" class="form-control" name="priceFrom" placeholder="Price from">
                                <input style="width: 100px" type="text" class="form-control" name="priceTo" placeholder="Price to">
                            </div>
                            <button type="submit" class="btn btn-default">Submit</button>
                        </form>
                        <form class="navbar-form navbar-left" role="search" action="/search" method="post">
                            <div class="form-group">
                                <input type="text" class="form-control" name="pattern" placeholder="Search">
                            </div>
                            <button type="submit" class="btn btn-default">Submit</button>
                        </form>
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
                <c:forEach items="${dishes}" var="dish">
                    <tr>
                        <td>
                            <button style="width: 30px; height: 30px" type="button" class="btn btn-default navbar-btn add-button" data-dish-id="${dish.id}">+</button>
                        </td>
                        <td>${dish.name}</td>
                        <c:if test="${dish.discount ne 0}">
                            <td>
                                <h style="text-decoration: line-through;">${dish.price} $</h>
                                    ${dish.price - (dish.price * (dish.discount / 100))} $
                            </td>
                        </c:if>
                        <c:if test="${dish.discount eq 0}">
                            <td>${dish.price} $</td>
                        </c:if>
                        <td>${dish.discount} %</td>
                        <td>${dish.weight} g</td>
                    </tr>
                </c:forEach>
            </table>

            <nav aria-label="Page navigation">
                <ul class="pagination">
                    <c:if test="${allPages ne null}">
                        <c:forEach var="i" begin="1" end="${allPages}">
                            <li><a href="/?page=<c:out value="${i - 1}"/>"><c:out value="${i}"/></a></li>
                        </c:forEach>
                    </c:if>
                    <c:if test="${byGroupPages ne null}">
                        <c:forEach var="i" begin="1" end="${byGroupPages}">
                            <li><a href="/group/${groupId}?page=<c:out value="${i - 1}"/>"><c:out value="${i}"/></a></li>
                        </c:forEach>
                    </c:if>
                </ul>
            </nav>
        </div>

        <script>
            $('#show_discount').click(function(){
                window.location.href='/discount_dishes';
            });

            $('#add_dish').click(function(){
                window.location.href='/add_dish';
            });

            $('#reset').click(function(){
                window.location.href='/reset';
            });

            $('#show_order').click(function(){
                window.location.href='/order';
            });

            $('.add-button').click(function(){
                let data = { toOrder: $(this).data('dish-id') };
                $.post("/dish/order", data, function(response, status) {
                    window.location.reload();
                });
            });
        </script>
    </body>
</html>