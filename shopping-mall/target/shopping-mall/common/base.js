/**
 * base.js
 */
// 请求方法
function request(url, method, params, callback) {
	$.ajax({
		type : method,
		url : url,
		data : params,
		dataType : "json",
		success : function(data) {
			callback(data);
		},
		error : function(XMLHttpRequest, textStatus) {
			if (XMLHttpRequest.status == 500) {
				// var result = eval("(" + XMLHttpRequest.responseText + ")");
				var text = textStatus;
				var result = XMLHttpRequest.responseText;
				alert(result);
				// alert(result.exception.message);
			}
			if (XMLHttpRequest.status == 408) {
				alert("请求超时");
			}
			if(XMLHttpRequest.status == 503){
				alert("服务器暂不可用");
			}
		}
	});
}

// 带同步属性
function requestAsync(url, method, params, async, callback) {
	$.ajax({
		type : method,
		url : url,
		data : params,
		async : async,
		dataType : "json",
		success : function(data) {
			if (!data.success) {
				callback(data);
			}
		},
		error : function(XMLHttpRequest, textStatus) {
			if (XMLHttpRequest.status == 500) {
				// var result = eval("(" + XMLHttpRequest.responseText + ")");
				var text = textStatus;
				var result = XMLHttpRequest.responseText;
				alert(result);
				// alert(result.exception.message);
			}
		}
	});
}

function alertf(msg,callback){
	alert(msg);
	callback();
}

/**
 * 空判断
 */
function isEmpty(str) {
	if (str != null && str != undefined && str != "") {
		return false;
	}else{
		return true;
	}
}

/**
 * 长连接
 */
function longPolling(url){
	 $.ajax({
         url: url,
         data: {"timed": new Date().getTime()},
         dataType: "text",
         timeout: 5000,
         error: function (XMLHttpRequest, textStatus, errorThrown) {
//             $("#state").append("[state: " + textStatus + ", error: " + errorThrown + " ]<br/>");
             if (textStatus == "timeout") { // 请求超时
                     longPolling(); // 递归调用
                 // 其他错误，如网络错误等
                 } else { 
                     longPolling();
                 }
             },
         success: function (data, textStatus) {
//             $("#state").append("[state: " + textStatus + ", data: { " + data + "} ]<br/>");
             if (textStatus == "success") { // 请求成功
                 longPolling();
             }
         }
     });
}