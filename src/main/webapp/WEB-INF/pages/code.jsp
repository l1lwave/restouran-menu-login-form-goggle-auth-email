<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
    <title>Prog.kiev.ua</title>
</head>
<body>
<div align="center">
    <form action="code" method="POST">
            Check your email and enter the code:<br/>
            Code:<br/><input type="text" name="inputCode"><br/>
            <input type="hidden" name="email" value="${email}">
            <input type="hidden" name="password" value="${password}">
            <input type="hidden" name="phone" value="${phone}">
            <input type="hidden" name="address" value="${address}">
            <input type="hidden" name="trueCode" value="${trueCode}">
        <input type="submit" name="submitCode" value="Verify Code"/>
    </form>

    <c:if test="${errorCode eq true}">
        <p>Wrong code!</p>
    </c:if>
</div>
</body>
</html>
