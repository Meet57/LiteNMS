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

    if(message.data.includes("id")){
        localStorage.setItem("id",message.data.split(":")[1])
        console.log(message.data.split(":")[1]+" Socket id set to localstorage")
    }

    if()

}

function sendMessageToServer() {

    if (textMessage.value !== "close") {

        websocket.send(textMessage.value);

        messageTextArea.value += "Send to the Server => : " + textMessage.value + "\n";

        textMessage.value = "";

    } else websocket.close();

}

function processOnClose(message) {

    websocket.send("Client Disconnected.....");

    console.log("Server Disconnected....")

}

function processOnError(message) {

    console.log("Error......")

}