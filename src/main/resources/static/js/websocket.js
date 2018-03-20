class WebsocketClient {

    constructor() {
        this.stompClient = null;

        this.isConnected = false;
        this.waitingrequests = [];
        this.connect();

        this.allSubscriptions = [];
    }

    connect() {
        const url = '/gs-guide-websocket';
        console.log('Trying to connect to ' + url);
        let socket = new SockJS(url);

        this.stompClient = Stomp.over(socket);
        this.stompClient.debug = null
        this.stompClient.connect({}, (frame) => this.connectCallback(frame), (error) => this.error_callback(error));
        let that = this;
        socket.onclose = function() {
            console.log('socket closed');
            that.disconnect();
            that.connect();
        };
    };

    connectCallback(frame) {
        console.log('Connected: ', frame);
        this.isConnected = true;

        this.allSubscriptions.forEach((subscription) => {
            console.log("SUBSCRIBE", subscription);
            this.stompClient.subscribe(subscription.url, (response) => subscription.callback(response));
        });
    
        this.waitingrequests.forEach(request => {
            this.stompClient.send(request.url, {}, JSON.stringify(request.datas));
        });
        this.waitingrequests = [];
        
        // stompClient.subscribe('/topic/greetings', function (greeting) {
        //     console.log("response", greeting);
        //     showGreeting(JSON.parse(greeting.body).label);
        // });
    };

    error_callback(error) {
        console.log("ERROR while connecting WS");
        console.log(error.headers.message);
        this.isConnected = false;
    };

    disconnect() {
        if (this.stompClient !== null) {
            this.stompClient.disconnect();
            this.stompClient = null;
        }
        this.isConnected = false;
        console.log("Disconnected");
    };

    sendMessage(url, datas) {
        if (!this.isConnected) {
            this.waitingrequests.push({ url : url, datas : datas });
            return;
        }
        this.stompClient.send(url, {}, JSON.stringify(datas));
    };


    subscribe(url, callback) {
        if (this.isConnected) {
            this.stompClient.subscribe(url, (response) => {
                callback(response);
            });
        }

        this.allSubscriptions.push({
            url : url,
            callback : callback
        });
    };

}
