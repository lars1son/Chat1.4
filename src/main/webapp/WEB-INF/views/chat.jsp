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
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/common.css">
    <script>
        var socket = new WebSocket("ws://" + document.location.host + "/chat");

        socket.onopen = function (event) {

               // alert("Соединение открылось" );

        };
        socket.onclose = function () {
            //         alert("Соединение закрылось");
        };
        socket.onmessage = function (event) {
            //        alert("message")
            var xmlDoc, message, value;
            message = event.data;
            xmlDoc = parseXmlFromString(message);
        //    alert(new XMLSerializer().serializeToString(xmlDoc.documentElement));
            if (xmlDoc.getElementsByTagName("onlineusers").item(0).attributes.getNamedItem("logins").nodeValue != "") {
              //  alert("1 if   " + "${pageContext.request.userPrincipal.name}");

                onlineUsersHandler(xmlDoc, "${pageContext.request.userPrincipal.name}");

            }

            if (xmlDoc.getElementsByTagName("message").item(0).attributes.getNamedItem("textofthemessage").nodeValue != "") {
                messagesDequeHandler(xmlDoc);
    //            alert("2 if")

            }

            if (xmlDoc.getElementsByTagName("dialogrequest").item(0).attributes.getNamedItem("whos").nodeValue != "") {
                dialogRequestHandler(xmlDoc, "${pageContext.request.contextPath}");
     //           alert("3 if")

            }
        };

    </script>
    <style>
        html {
            height: 100%;
        }

        body {
            margin: 0; /* Убираем отступы */
            height: 100%; /* Высота страницы */
            background: url(${pageContext.request.contextPath}/resources/images/RiBaKaTeSkur.jpg); /* Параметры фона */
            background-size: cover; /* Фон занимает всю доступную площадь */
        }
    </style>
</head>
<body bgcolor="#E8E8E8">
<img src="${pageContext.request.contextPath}/resources/images/logo.png" width="228" height="150"
     style="margin: 0px 0px 0px 0px"/>

</div>
<div style="width: 20%; padding:0px 10px 10px 10px; margin: 0px; position: absolute;">
    <table border="0">
        <tr>
            <td style="width:20%;">
                <h3 style="color: #f03b25; text-align: left;"> Welcome ${username} </h3>
                <form action="${pageContext.request.contextPath}/logOut" method="POST">
                    <%--<input type="submit" value="LogOut" onclick="socket.send(generateLogoutMessage());"/>--%>
                    <button type="submit" value="LogOut" onclick="socket.send(generateLogoutMessage());">
                        <img src="${pageContext.request.contextPath}/resources/images/logout.png" width="64" height="15"
                             style="margin: 0px 0px 0px 0px" alt="Logout">
                    </button>

                </form>

                    <button type="submit" value="MyProfile" onclick="myProfile();">
                        <img src="${pageContext.request.contextPath}/resources/images/profile.png" width="64" height="15"
                             style="margin: 0px 0px 0px 0px" alt="Profile">
                    </button>

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

