function onlineUsersHandler(xmlDoc, login) {
    value = xmlDoc.getElementsByTagName("onlineusers").item(0).attributes.getNamedItem("logins").nodeValue;
    document.getElementById('userBlock').innerHTML = parseLogins(value, login);
}

function messagesDequeHandler(doc) {
    var i;
    var strResult = "";
    for (i = 0; i < doc.getElementsByTagName("message").length; i++) {
        strResult = strResult + '<i style="color: #3651e0;">' + doc.getElementsByTagName("message").item(i)
                .attributes.getNamedItem("time").nodeValue + ' </i>' + '<b style="color: #1b8819;">" ' + doc.getElementsByTagName("message").item(i)
                .attributes.getNamedItem("login").nodeValue + ' "</b> ' + htmlStringSplitter(
                doc.getElementsByTagName("message").item(i).attributes.getNamedItem("textofthemessage").nodeValue) + '</br>';
    }
    document.getElementById('mainChatTextArea').innerHTML = strResult;
}

function dialogRequestHandler(doc, contextPath) {
    if (doc.getElementsByTagName("dialogrequest").item(0).attributes.getNamedItem("whos").nodeValue == "onewhocalls") {
        if (confirm("Do you want to start a dialog with " + doc.getElementsByTagName("dialogrequest").item(0).attributes.getNamedItem("onewhocalls").nodeValue + "?")) {
            var path = contextPath + "/dialog?user1=" + doc.getElementsByTagName("dialogrequest")
                    .item(0).attributes.getNamedItem("onewhocalls").nodeValue + "&user2=" + doc
                    .getElementsByTagName("dialogrequest").item(0)
                    .attributes.getNamedItem("thecallee").nodeValue + "&whos=thecallee";
            window.open(path, "_blank");

            setDialogRequestAnswer(doc, "true");
            var answer = generateStringFromXml(doc);
            socket.send(answer);
            return;
        } else {
            setDialogRequestAnswer(doc, "false");
            var answer = generateStringFromXml(doc);
            socket.send(answer);
            return;
        }
    }
    if (doc.getElementsByTagName("dialogrequest").item(0).attributes.getNamedItem("whos").nodeValue == "thecallee") {
        if (doc.getElementsByTagName("dialogrequest").item(0).attributes.getNamedItem("requestanswer").nodeValue == 'true') {
            var path = contextPath + "/dialog?user1=" + doc.getElementsByTagName("dialogrequest")
                    .item(0).attributes.getNamedItem("onewhocalls").nodeValue + "&user2=" + doc
                    .getElementsByTagName("dialogrequest").item(0)
                    .attributes.getNamedItem("thecallee").nodeValue + "&whos=onewhocalls";
            window.open(path, "_blank");
        }

        if (doc.getElementsByTagName("dialogrequest").item(0).attributes.getNamedItem("requestanswer").nodeValue == 'false') {
            alert("Your request for a dialog was denied :(");
        }
    }
}

function parseLogins(stringOflogins, login) {
    var logins = stringOflogins;
    var result = "";
    var temp = "";
    var stop = false;
    for (i = 0; i < logins.length; i++) {
        if (logins.charAt(i) == ' ') {
            if (temp != login) {
                result = result + '<span title="Click to start dialog with ' + temp + '" onclick="socket.send(generateDialogRequest(\'' + temp + '\', \'' + login + '\'))" style="color: #4e4e4e; text-decoration: none;">' + temp + '</span></br>';
                temp = "";
            } else {
                result = result + '<b style="color: #4e4e4e;">' + temp + '</b> </br>';
                temp = "";
            }
        } else {
            temp = temp + logins.charAt(i);
        }
    }
    return result;
}

function readUsersInput(login) {
    var massage = document.getElementById("inputtext").value;
    document.getElementById("inputtext").value = "";
    var socketBox = createSocketBox();
    socketBox = setMessage(socketBox, login, massage);
    var result = generateStringFromXml(socketBox);
    return result;
}

function generateLogoutMessage(login) {
    var socketBox = createSocketBox();
    socketBox = setMessage(socketBox, login, "leaved us.");
    socketBox.getElementsByTagName("message").item(0).attributes.getNamedItem("logout").textContent = "true"
    var result = generateStringFromXml(socketBox);
    return result;
}

function generateDialogRequest(theCallee, login) {
    alert("Request for the dialog was sent");
    var oneWhoCalls = login;
    var socketBox = createSocketBox();
    socketBox = setDialogRequest(socketBox, oneWhoCalls, theCallee, "false");
    var result = generateStringFromXml(socketBox);
    return result;
}

function createSocketBox() {
    var doc = document.implementation.createDocument('http://www.w3.org/1999/xhtml', 'html', null);
    var socketBox = document.createElementNS('http://www.w3.org/1999/xhtml', 'socketbox');
    doc.documentElement.appendChild(socketBox);
    var message = document.createElementNS('http://www.w3.org/1999/xhtml', 'message');
    message.setAttribute('login', "");
    message.setAttribute('time', "");
    message.setAttribute('textofthemessage', "");
    message.setAttribute('logout', 'false');
    socketBox.appendChild(message);
    var dialogRequest = document.createElementNS('http://www.w3.org/1999/xhtml', 'dialogrequest');
    dialogRequest.setAttribute('onewhocalls', "");
    dialogRequest.setAttribute('thecallee', "");
    dialogRequest.setAttribute('requestanswer', "");
    dialogRequest.setAttribute('whos', ""); //onewhocalls or thecallee
    socketBox.appendChild(dialogRequest);
    var onlineUsers = document.createElementNS('http://www.w3.org/1999/xhtml', 'onlineusers');
    onlineUsers.setAttribute('logins', "");
    socketBox.appendChild(onlineUsers);
    return doc;
}

function setMessage(doc, login, message) {
    doc.getElementsByTagName("message").item(0).attributes.getNamedItem("time").textContent = createTime();
    doc.getElementsByTagName("message").item(0).attributes.getNamedItem("login").textContent = login;
    doc.getElementsByTagName("message").item(0).attributes.getNamedItem("textofthemessage").textContent = message;

    return doc;
}
function setDialogRequest(doc, oneWhoCalls, theCallee, requestAnswer) {
    doc.getElementsByTagName("dialogrequest").item(0).attributes.getNamedItem("onewhocalls").textContent = oneWhoCalls;
    doc.getElementsByTagName("dialogrequest").item(0).attributes.getNamedItem("thecallee").textContent = theCallee;
    doc.getElementsByTagName("dialogrequest").item(0).attributes.getNamedItem("requestanswer").textContent = requestAnswer;
    doc.getElementsByTagName("dialogrequest").item(0).attributes.getNamedItem("whos").textContent = 'onewhocalls';
    return doc;
}

function setDialogRequestAnswer(doc, requestAnswer) {
    doc.getElementsByTagName("dialogrequest").item(0).attributes.getNamedItem("requestanswer").textContent = requestAnswer;
    doc.getElementsByTagName("dialogrequest").item(0).attributes.getNamedItem("whos").textContent = 'thecallee';
    return doc;
}

function createTime() {
    var time = new Date().getTime();
    var hours = (time / 3600000) % 24 + 2; //+2 for Ukraine
    var minutes = (time % 3600000) / 60000;
    var seconds = ((time % 3600000) % 60000) / 1000;
    var result = "";
    if (hours < 10) {
        result = result + "0" + ~~hours;
    } else {
        result = result + ~~hours;
    }
    result = result + ':';
    if (minutes < 10) {
        result = result + "0" + ~~minutes;
    } else {
        result = result + ~~minutes;
    }
    result = result + ':';
    if (seconds < 10) {
        result = result + "0" + ~~seconds;
    } else {
        result = result + ~~seconds;
    }
    return result;
}

function generateStringFromXml(doc) {
    var serializer = new XMLSerializer();
    var xmlString = serializer.serializeToString(doc);
    return xmlString;
}

function parseXmlFromString(message) {
    var parser, xmlDoc;
    parser = new DOMParser();
    xmlDoc = parser.parseFromString(message, "text/xml");
    return xmlDoc;
}

function htmlStringSplitter(text) {
    if (text.length > 100) {
        var result = "</br>";
        var stop = false;
        var start = 0;
        var end = 100;
        while (!stop) {
            if (end < text.length) {
                result = result + text.substring(start, end) + "</br>";
                start = end;
                end += 100;
            } else {
                result = result + text.substring(start, text.length - 1);
                stop = true;
            }
        }
        return result;
    } else {
        return text;
    }
}

function end() {
    socket.close();
    window.close();
}

$('html').keydown(function (eventObject) { //отлавливаем нажатие клавиш
    if (event.keyCode == 13) { //если нажали Enter, то true
        socket.send(readUsersInput());
    }
});