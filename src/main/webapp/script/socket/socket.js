var websocket

if(window.location.protocol === "http:"){
    websocket = new WebSocket("ws://"+window.location.host+"/websocket/endpoint");
}else{
    websocket = new WebSocket("wss://"+window.location.host+"/websocket/endpoint");
}



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

function processOnOpen(message) {
}

function processOnMessage(message) {

    let data = JSON.parse(message.data)

    console.log(data)

    switch (data.type) {

        case 'socketId':

            localStorage.setItem("socketId", data.socketId)

            break

        case 'notification':

            components.alert(data.title, data.status, parseInt(data.code))

            if (data.title === "Discovery Result") {

                discovery.loadDiscoveryTable(null, true);

            }

            if(data.title.includes("Polling result for")){

                let request = {

                    url: "getMetrics",

                    data: {device_id: data.id, type: data.deviceType},

                    callback: monitor.loadMetricData,

                };

                api.ajaxpost(request,true,true);

            }

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

function processOnClose(message) {
}

function processOnError(message) {
}