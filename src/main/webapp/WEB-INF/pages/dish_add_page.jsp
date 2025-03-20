<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
    <head>
        <title>New Dish</title>
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.1/css/bootstrap.min.css">
        <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.1/js/bootstrap.min.js"></script>
    </head>
    <body>
        <div class="container">
            <form role="form" class="form-horizontal" action="/dish/add" method="post">
                        <h3>New dish</h3>
                        <input class="form-control form-group" type="text" name="name" placeholder="Name">
                        <input class="form-control form-group" type="text" name="price" placeholder="Price">
                        <input class="form-control form-group" type="text" name="weight" placeholder="Weight">
                        <input class="form-control form-group" type="text" name="discount" placeholder="Discount">
                    <input type="submit" class="btn btn-primary" value="Add">
            </form>
        </div>

        <script>
            $('.selectpicker').selectpicker();
        </script>
    </body>
</html>