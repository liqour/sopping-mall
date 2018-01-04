/**
 * index.js
 */
$(document).ready(function() {
	$(function(){
    	$("#page-wrapper").load("welcome.html");
    });
});

// 跳转
function jump(address){
	if(!isEmpty(address)){
		$("#page-wrapper").load(address+".html");
	}else{
		$("#page-wrapper").load("welcome.html");
	}
}

// 退出登录
function loginOut(){
	request("../userInfo/loginOut","post",null,function(data){
		if(data.success){
			alert(data.data);
		}
	});
}