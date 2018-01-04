/**
 * upload.js
 */
var upload = function(){
	$.ajaxFileUpload({
	    //请求路径
	    url: '../excel/import',
	    //文件
	    fileElementId: 'commonExcelFile',
	    data:{"excelTitle":$("#excelTitle").val()+",","batchImportColumn":$("#batchImportColumn").val(),"tableName":$("#tableName").val()},
	    //传输类型
	    dataType: 'json',
	    //请求成功给前台页面赋值
	    success: function(data, status) {
	    	var dbTitle = $("#batchImportColumn").val().split(",");
	    	var title = "<tr>";
	    	title += "<td>#</td>";
	    	for(var j=0;j<dbTitle.length;j++){
	    		title += "<td>"+dbTitle[j]+"</td>";
	    	}
	    	$("#ta thead").html("");
	    	$("#ta thead").append(title+"</tr>");
	    	var tbs="";
	    	for(var i=0;i<data.data.object.dataList.length;i++){
	    		tbs+="<tr class='warning'>";
	    		tbs+="<td>"+(i+1)+"</td>";
		    	for(var j=0;j<dbTitle.length;j++){
		    		tbs+="<td>"+((data.data.object.dataList[i][dbTitle[j]])==null?"":data.data.object.dataList[i][dbTitle[j]])+"</td>";
		    	}
		    	tbs+="</tr>";
	    	}
	    	$("#ta tbody").html("");
	    	$("#ta tbody").append(tbs);
	    	if($("#sql")){
	    		$("#sql").remove();
	    	}
	    	$("#ta").after("<textarea rows='10' cols='50' id='sql' style='height:200px;'>"+data.data.object.sql+"</textarea>");
//	    	debugger
//	    	var oTable = document.getElementById("table");
//	    	if(!oTable){  
//	            return;  
//	        }  
//	    	var oTbody = oTable.getElementsByTagName("tbody"); 
////	    	oTbody[0].append("<tr class='success'><td>我去</td><td>添加</td><td>测试</td><td>好了</td></tr>");
//	    	var tr = document.createElement("tr"); //新建一个tr类型的Element节点
//	        var td1 = document.createElement("td"); //新建一个td类型的Element节点
//	        td1.innerHTML = "我去";
//	        tr.appendChild(td);
//	        oTbody[0].appendChild(tr);
//	    	document.getElementsByTagName("tbody")[0].append("<tr class='success'><td>我去</td><td>添加</td><td>测试</td><td>好了</td></tr>");
//	        $scope.isSubmit = 2;
//	        //正确数据集合
//	        var excelSuccesssList = data.data.excelSuccesssList;
//	        //给正确数据赋值
//	        if(excelSuccesssList.length > 0) {
//	            $dataSourceManager.dataSources["successSourceList"].records = excelSuccesssList;
//	            $rootScope.$broadcast('successSourceList', $dataSourceManager.dataSources["successSourceList"]);
//	        }
//	        //错误数据集合
//	        var excelErrorList = data.data.excelErrorList;
//	        //给错误数据赋值
//	        if(excelErrorList.length > 0) {
//	            $dataSourceManager.dataSources["errorSourceList"].records = excelErrorList;
//	            $rootScope.$broadcast('errorSourceList', $dataSourceManager.dataSources["errorSourceList"]);
//	        }
	//
//	        $scope.excelErrorMsgList = data.data.excelErrorMsgList;
	//
//	        $scope.reminder();
	//
//	        GillionMsg.alert("提示", "添加成功,共" + excelSuccesssList.length + "条数据校验通过," + excelErrorList.length + "条数据校验不通过")

	    },
	    error: function(data, status, e) {
	    	alert("上传失败");
//	        GillionMsg.alert('提示', data.responseXML.activeElement.innerText);
	    }
	});
}