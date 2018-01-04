/**
 * login.js
 */
var pwd = null;
$(document).ready(function() {
	// 记住密码功能
	var str = getCookie("loginInfo");
	str = str.substring(1, str.length - 1);
	var username = str.split(",")[0];
	var password = str.split(",")[1];
	pwd = password;
	// 自动填充用户名和密码
	if (!isEmpty(username) && !isEmpty(password)) {
		$("#username").val(username);
		$("#password").val(password);
		$("#remFlag").attr("checked", true);
		$("#remFlag").val("1");
	} else {
		$("#username").val("");
		$("#password").val("");
	}
});

// 获取cookie
function getCookie(name) {
	var arr, reg = new RegExp("(^| )" + name + "=([^;]*)(;|$)");
	if (arr = document.cookie.match(reg))
		return unescape(arr[2]);
	else
		return null;
}

// 记住密码功能
function remember() {
	var remFlag = $("input[type='checkbox']").is(':checked');
	if (remFlag == true) { // 如果选中设置remFlag为1
		// cookie存用户名和密码,回显的是真实的用户名和密码,存在安全问题.
		var conFlag = confirm("记录密码功能不宜在公共场所(如网吧等)使用,以防密码泄露.您确定要使用此功能吗?");
		if (conFlag) { // 确认标志
			$("#remFlag").val("1");
		} else {
			$("input[type='checkbox']").removeAttr('checked');
			$("#remFlag").val("");
		}
	} else { // 如果没选中设置remFlag为""
		$("#remFlag").val("");
	}
}

// 登录
function login() {
	var password = $("#password").val();
	var username = $("#username").val();
	if(isEmpty(username)){
		alert("请填写用户名");
		return;
	}
	if(isEmpty(password)){
		alert("请填写密码");
		return;
	}
	if(isEmpty(getCookie("loginInfo")) || pwd!=password){
		$("#password").val(md5(password));
	}
	var params = $("#loginForm").serialize();
	request("userInfo/login", "post", params, function(data) {
		if(data.success){
//			alert("IP为:" + data.data);
//			window.location.href = "background/index.html"
		}
	});
}
