<%--
  Created by IntelliJ IDEA.
  User: Артем
  Date: 12.09.2017
  Time: 20:41
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="en">
<head>
    <csrf disabled="true"/>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta name="robots" content="noindex, nofollow">
    <meta name="googlebot" content="noindex, nofollow">
    <title>New page</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <script src="${pageContext.request.contextPath}/resources/scripts/chatScript.js"></script>
    <link href="//maxcdn.bootstrapcdn.com/bootstrap/3.1.0/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/style.css">
    <script src="//code.jquery.com/jquery-1.10.2.min.js"></script>
    <script src="//maxcdn.bootstrapcdn.com/bootstrap/3.1.0/js/bootstrap.min.js"></script>
</head>
<body>
<form action="/save_changes" enctype="multipart/form-data" method="POST">
    <%--<div id="path"></div>--%>
    <%--<div id="p1" aria-valuetext="${pageContext.requst.contextPath}"></div>--%>
    <%--<div id="p2" aria-valuetext="${image_path}"></div>--%>
    <%--<script>--%>
        <%--function say_hi() {--%>
            <%--var p1 = document.getElementById('p1').value;--%>
            <%--var p2 = document.getElementById('p2').value;--%>

            <%--var html = p1+p2;--%>

            <%--document.getElementById('path').innerHTML = html;--%>
        <%--}--%>
    <%--</script>--%>
    <div class="container">
        <div class="avatar col-md-4 col-md-offset-2">
            <img id="avatar"
                 name="avatar"
                 src="${image_path}"
                 alt="">
            Photo: <input type="file" name="file">

        </div>
        <div class="forms col-md-4">
            <%--<form class="form-inline">--%>
                <%--<div class="form-group">--%>
                    <%--<label for="exampleInputName2" >Login</label>--%>
                    <%--<input type="text" class="form-control" id="exampleInputName2" placeholder="${username}"--%>
                           <%--pattern="[а-яА-Я]+">--%>
                <%--</div>--%>
            <%--</form>--%>
            <%--<form class="form-inline">--%>
            <%--<div class="form-group">--%>
            <%--<label for="exampleInputName2">Password</label>--%>
            <%--<input type="text" class="form-control" id="exampleInputSurName2" placeholder="Пупкин"--%>
            <%--pattern="[а-яА-Я]+">--%>
            <%--</div>--%>
            <%--</form>--%>
            <%--<form class="form-inline">--%>
            <%--<div class="form-group">--%>
            <%--<label>Дата рождения</label>--%>
            <%--<input type="date" class="form-control" id="exampleInputDOB1" placeholder="Date of Birth">--%>
            <%--</div>--%>
            <%--</form>--%>
            <form class="form-inline" >
                <div class="form-group">
                    <label for="exampleInputEmail2">Email</label>
                    <input type="email" name="email" class="form-control" id="exampleInputEmail2" placeholder="${email}"
                           pattern="[a-z0-9._%+-]+@[a-z0-9.-]+\.[a-z]{2,3}$">
                </div>
            </form>
            <form class="form-inline">
                <div class="form-group">
                    <label for="pwd">Password:</label>
                    <input type="password" name="password" class="form-control" id="pwd">
                </div>
            </form>
            <form class="form-inline" >
                <div class="form-group">
                    <label for="comment">About:</label>
                    <textarea name="about" class="form-control" rows="5" id="comment" placeholder="${about}"></textarea>
                </div>
            </form>
            <button type="submit" class="btn btn-default">Save changes</button>
        </div>
    </div>
</form>

<script type="text/javascript" src="${pageContext.request.contextPath}/resources/scripts/script.js"></script>
</body>
</html>