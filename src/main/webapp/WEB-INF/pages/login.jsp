<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <title>Prog.kiev.ua</title>
</head>
<body>
    <div align="center">
        <form action="/j_spring_security_check" method="POST">
            Email:<br/><input type="text" name="j_email"><br/>
            Password:<br/><input type="password" name="j_password"><br/>
            <input type="submit" />

            <p><a href="/register">Register new user</a></p>

            <c:if test="${param.error ne null}">
                <p>Wrong password!</p>
            </c:if>

            <c:if test="${param.errorEmail ne null}">
                <p>This email already exist!</p>
            </c:if>

            <c:if test="${param.logout ne null}">
                <p>Chao!</p>
            </c:if>
        </form>
        <a href="/oauth2/authorization/google">
            <button>Join via Google</button>
        </a>

    </div>
</body>
</html>
