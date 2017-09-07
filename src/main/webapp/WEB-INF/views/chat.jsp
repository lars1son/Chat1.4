<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Chat Page</title>
    <script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/1/jquery.min.js"></script>
    <script src="${pageContext.request.contextPath}/resources/scripts/chatScript.js"></script>
    <script>
        var socket = new WebSocket("ws://"+ document.location.host + document.location.pathname + "/chat" );

        socket.onopen = function (event) {
            alert("Соединение открылось");

        };
        socket.onclose = function () {
            // alert("Соединение закрылось");
        };
        socket.onmessage = function (event) {
            alert("message")
            var xmlDoc, message, value;
            message = event.data;

            xmlDoc = parseXmlFromString(message);

            if (xmlDoc.getElementsByTagName("onlineusers").item(0).attributes.getNamedItem("logins").nodeValue != "") {
                onlineUsersHandler(xmlDoc, "HERE MUST BE UR NICKNAME");
            }

            if (xmlDoc.getElementsByTagName("message").item(0).attributes.getNamedItem("textofthemessage").nodeValue != "") {
                messagesDequeHandler(xmlDoc);
            }

            if (xmlDoc.getElementsByTagName("dialogrequest").item(0).attributes.getNamedItem("whos").nodeValue != "") {
                dialogRequestHandler(xmlDoc, "${pageContext.request.contextPath}");
            }
        };

    </script>

</head>
<body bgcolor="#E8E8E8">

<%--<form action="<%=request.getContextPath()%>/chat" method="POST">--%>

<div style="background-image:url(${pageContext.request.contextPath}/webapp/images/texture.jpg); background-repeat:repeat; width: 100%; height: 150px">

    <img src="${pageContext.request.contextPath}/resources/images/logo.png" width="228" height="150"
         style="margin: 0px 0px 0px 0px"/>

</div>

<div style="width: 20%; padding:0px 10px 10px 10px; margin: 0px; position: absolute;">
    <table border="0">
        <tr>
            <td style="width:20%;">
                <h3 style="color: #f03b25; text-align: left;"> Welcome ${pageContext.request.userPrincipal.name} </h3>
                <form action="${pageContext.request.contextPath}/logOut" method="POST">
                    <%--<input type="submit" value="LogOut" onclick="socket.send(generateLogoutMessage());"/>--%>
                    <button type="submit" value="LogOut" onclick="socket.send(generateLogoutMessage());">
                        <img src="${pageContext.request.contextPath}/resources/images/logout.png" width="64" height="15"
                             style="margin: 0px 0px 0px 0px" alt="Logout">
                    </button>
                </form>
                <h4 style="color: #4e4e4e; text-align: left;">You can click on a nick to start dialogue.</h4>
                <h3 style="color: #4e4e4e; text-align: left;">Online users:</h3>
            </td>
        </tr>
        <tr>
            <td id='userBlock'>

            </td>
        </tr>
    </table>
</div>

<div style="width: 80%; padding: 5px; margin-left: 20%;">
    <%--MAIN--%>

    <table border="0">
        <tr>
            <td>
                <input type="text" name="login" value="" size="90" id="inputtext"/>
                <button type="submit" onclick=socket.send(readUsersInput());>
                    <img src="${pageContext.request.contextPath}/resources/images/send.png" width="56" height="16"
                         style="margin: 0px 0px 0px 0px" alt="Send">
                </button>
            </td>
        </tr>
        <tr>
            <td>
                <span id='mainChatTextArea'></span>
            </td>
        </tr>
    </table>
</div>

</body>
</html>

