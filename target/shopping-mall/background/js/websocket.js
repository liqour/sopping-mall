/**
 * websocket.js
 */
var websocket = null;

// 判断当前浏览器是否支持WebSocket
if ('WebSocket' in window) {
//	 WebSocket("ws://47.95.5.126/shopping-mall/webSocketServer;");
//	WebSocket("ws://192.168.30.197/shopping-mall/webSocketServer;");
//	websocket = new WebSocket("ws://192.168.31.53:8080/shopping-mall/webSocketServer");
//	websocket = new WebSocket("ws://127.0.0.1:8080/shopping-mall/webSocketServer");
} else {
	alert('你的浏览器不支持本系统，请更换浏览器');
}

// 连接发生错误的回调方法
websocket.onerror = function() {
//	reconnect();
};

// 连接成功建立的回调方法
websocket.onopen = function(event) {
	// heartCheck.start();
}

// 接收到消息的回调方法
websocket.onmessage = function(event) {
	if (event.data == 1) {
		alert("你的账号在别处登录,如果不是本人操作请尽快修改密码");
		window.location.href = "../login.html"
	} else if (event.data == 2) {
		alert("连接超时,请重新登录");
		window.location.href = "../login.html"
	} else {
		alert(event.data);
	}
}

// 连接关闭的回调方法
websocket.onclose = function() {
	
}

// 监听窗口关闭事件，当窗口关闭时，主动去关闭websocket连接，防止连接还没断开就关闭窗口，server端会抛异常。
window.onbeforeunload = function() {
	websocket.close();
}

// 关闭连接
function closeWebSocket() {
	websocket.close();
}

// 发送消息
function send(message) {
	websocket.send(message);
}

// 重连
function reconnect() {
	// WebSocket("ws://localhost:8080/shopping-mall/webSocketServer");
}

// 定时器
var heartCheck = {
	timeout : 60000,// 60ms
	timeoutObj : null,
	serverTimeoutObj : null,
	reset : function() {
		clearTimeout(this.timeoutObj);
		clearTimeout(this.serverTimeoutObj);
		this.start();
	},
	start : function() {
		var self = this;
		this.timeoutObj = setTimeout(function() {
			websocket.send("HeartBeat");
			self.serverTimeoutObj = setTimeout(function() {
				websocket.close();// 如果onclose会执行reconnect，我们执行ws.close()就行了.如果直接执行reconnect
				// 会触发onclose导致重连两次
			}, self.timeout)
		}, this.timeout)
	},
}