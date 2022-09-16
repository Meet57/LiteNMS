var websocket = new WebSocket("ws://localhost:8080/websocket/endpoint");

websocket.onopen = function (message) {
    processOnOpen(message);
};

websocket.onmessage = function (message) {
    processOnMessage(message);
};

websocket.onclose = function (message) {
    processOnClose(message);
};

websocket.onerror = function (message) {
    processOnError(message);
};

function processOnOpen(message) {}

function processOnMessage(message) {

    let data = JSON.parse(message.data)

    console.log(data)

    switch (data.type){
        case 'socketId':
            localStorage.setItem("socketId",data.socketId)
            break
    }

}

function sendMessageToServer() {

    if (textMessage.value !== "close") {

        websocket.send(textMessage.value);

        messageTextArea.value += "Send to the Server => : " + textMessage.value + "\n";

        textMessage.value = "";

    } else websocket.close();

}

function processOnClose(message) {}

function processOnError(message) {}