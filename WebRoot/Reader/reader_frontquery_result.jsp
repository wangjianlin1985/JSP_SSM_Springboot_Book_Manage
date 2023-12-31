﻿<%@ page language="java" import="java.util.*"  contentType="text/html;charset=UTF-8"%> 
<%@ page import="com.chengxusheji.po.Reader" %>
<%@ page import="com.chengxusheji.po.ReaderType" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
    List<Reader> readerList = (List<Reader>)request.getAttribute("readerList");
    //获取所有的readerTypeObj信息
    List<ReaderType> readerTypeList = (List<ReaderType>)request.getAttribute("readerTypeList");
    int currentPage =  (Integer)request.getAttribute("currentPage"); //当前页
    int totalPage =   (Integer)request.getAttribute("totalPage");  //一共多少页
    int recordNumber =   (Integer)request.getAttribute("recordNumber");  //一共多少记录
    String readerNo = (String)request.getAttribute("readerNo"); //读者编号查询关键字
    ReaderType readerTypeObj = (ReaderType)request.getAttribute("readerTypeObj");
    String readerName = (String)request.getAttribute("readerName"); //姓名查询关键字
    String birthday = (String)request.getAttribute("birthday"); //读者生日查询关键字
    String telephone = (String)request.getAttribute("telephone"); //联系电话查询关键字
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1 , user-scalable=no">
<title>读者查询</title>
<link href="<%=basePath %>plugins/bootstrap.css" rel="stylesheet">
<link href="<%=basePath %>plugins/bootstrap-dashen.css" rel="stylesheet">
<link href="<%=basePath %>plugins/font-awesome.css" rel="stylesheet">
<link href="<%=basePath %>plugins/animate.css" rel="stylesheet">
<link href="<%=basePath %>plugins/bootstrap-datetimepicker.min.css" rel="stylesheet" media="screen">
</head>
<body style="margin-top:70px;">
<div class="container">
<jsp:include page="../header.jsp"></jsp:include>
<body style="margin-top:70px;"> 
<div class="container">
<jsp:include page="../header.jsp"></jsp:include>
	<div class="col-md-9 wow fadeInLeft">
		<ul class="breadcrumb">
  			<li><a href="<%=basePath %>index.jsp">首页</a></li>
  			<li><a href="<%=basePath %>Reader/frontlist">读者信息列表</a></li>
  			<li class="active">查询结果显示</li>
  			<a class="pull-right" href="<%=basePath %>Reader/reader_frontAdd.jsp">添加读者</a>
		</ul>
		<div class="row">
			<%
				/*计算起始序号*/
				int startIndex = (currentPage -1) * 5;
				/*遍历记录*/
				for(int i=0;i<readerList.size();i++) {
            		int currentIndex = startIndex + i + 1; //当前记录的序号
            		Reader reader = readerList.get(i); //获取到读者对象
            		String clearLeft = "";
            		if(i%4 == 0) clearLeft = "style=\"clear:left;\"";
			%>
			<div class="col-md-3 bottom15" <%=clearLeft %>>
			  <a  href="<%=basePath  %>Reader/<%=reader.getReaderNo() %>/frontshow"><img class="img-responsive" src="<%=basePath%><%=reader.getPhoto()%>" /></a>
			     <div class="showFields">
			     	<div class="field">
	            		读者编号:<%=reader.getReaderNo() %>
			     	</div>
			     	<div class="field">
	            		读者类型:<%=reader.getReaderTypeObj().getReaderTypeName() %>
			     	</div>
			     	<div class="field">
	            		姓名:<%=reader.getReaderName() %>
			     	</div>
			     	<div class="field">
	            		性别:<%=reader.getSex() %>
			     	</div>
			     	<div class="field">
	            		读者生日:<%=reader.getBirthday() %>
			     	</div>
			     	<div class="field">
	            		联系电话:<%=reader.getTelephone() %>
			     	</div>
			        <a class="btn btn-primary top5" href="<%=basePath %>Reader/<%=reader.getReaderNo() %>/frontshow">详情</a>
			        <a class="btn btn-primary top5" onclick="readerEdit('<%=reader.getReaderNo() %>');">修改</a>
			        <a class="btn btn-primary top5" onclick="readerDelete('<%=reader.getReaderNo() %>');">删除</a>
			     </div>
			</div>
			<%  } %>

			<div class="row">
				<div class="col-md-12">
					<nav class="pull-left">
						<ul class="pagination">
							<li><a href="#" onclick="GoToPage(<%=currentPage-1 %>,<%=totalPage %>);" aria-label="Previous"><span aria-hidden="true">&laquo;</span></a></li>
							<%
								int startPage = currentPage - 5;
								int endPage = currentPage + 5;
								if(startPage < 1) startPage=1;
								if(endPage > totalPage) endPage = totalPage;
								for(int i=startPage;i<=endPage;i++) {
							%>
							<li class="<%= currentPage==i?"active":"" %>"><a href="#"  onclick="GoToPage(<%=i %>,<%=totalPage %>);"><%=i %></a></li>
							<%  } %> 
							<li><a href="#" onclick="GoToPage(<%=currentPage+1 %>,<%=totalPage %>);"><span aria-hidden="true">&raquo;</span></a></li>
						</ul>
					</nav>
					<div class="pull-right" style="line-height:75px;" >共有<%=recordNumber %>条记录，当前第 <%=currentPage %>/<%=totalPage %> 页</div>
				</div>
			</div>
		</div>
	</div>

	<div class="col-md-3 wow fadeInRight">
		<div class="page-header">
    		<h1>读者查询</h1>
		</div>
		<form name="readerQueryForm" id="readerQueryForm" action="<%=basePath %>/Reader/frontlist" class="mar_t15">
			<div class="form-group">
				<label for="readerNo">读者编号:</label>
				<input type="text" id="readerNo" name="readerNo" value="<%=readerNo %>" class="form-control" placeholder="请输入读者编号">
			</div>
            <div class="form-group">
            	<label for="readerTypeObj_readerTypeId">读者类型：</label>
                <select id="readerTypeObj_readerTypeId" name="readerTypeObj.readerTypeId" class="form-control">
                	<option value="0">不限制</option>
	 				<%
	 				for(ReaderType readerTypeTemp:readerTypeList) {
	 					String selected = "";
 					if(readerTypeObj!=null && readerTypeObj.getReaderTypeId()!=null && readerTypeObj.getReaderTypeId().intValue()==readerTypeTemp.getReaderTypeId().intValue())
 						selected = "selected";
	 				%>
 				 <option value="<%=readerTypeTemp.getReaderTypeId() %>" <%=selected %>><%=readerTypeTemp.getReaderTypeName() %></option>
	 				<%
	 				}
	 				%>
 			</select>
            </div>
			<div class="form-group">
				<label for="readerName">姓名:</label>
				<input type="text" id="readerName" name="readerName" value="<%=readerName %>" class="form-control" placeholder="请输入姓名">
			</div>
			<div class="form-group">
				<label for="birthday">读者生日:</label>
				<input type="text" id="birthday" name="birthday" class="form-control"  placeholder="请选择读者生日" value="<%=birthday %>" onclick="SelectDate(this,'yyyy-MM-dd')" />
			</div>
			<div class="form-group">
				<label for="telephone">联系电话:</label>
				<input type="text" id="telephone" name="telephone" value="<%=telephone %>" class="form-control" placeholder="请输入联系电话">
			</div>
            <input type=hidden name=currentPage value="<%=currentPage %>" />
            <button type="submit" class="btn btn-primary">查询</button>
        </form>
	</div>

		</div>
</div>
<div id="readerEditDialog" class="modal fade" tabindex="-1" role="dialog">
  <div class="modal-dialog" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
        <h4 class="modal-title"><i class="fa fa-edit"></i>&nbsp;读者信息编辑</h4>
      </div>
      <div class="modal-body" style="height:450px; overflow: scroll;">
      	<form class="form-horizontal" name="readerEditForm" id="readerEditForm" enctype="multipart/form-data" method="post"  class="mar_t15">
		  <div class="form-group">
			 <label for="reader_readerNo_edit" class="col-md-3 text-right">读者编号:</label>
			 <div class="col-md-9"> 
			 	<input type="text" id="reader_readerNo_edit" name="reader.readerNo" class="form-control" placeholder="请输入读者编号" readOnly>
			 </div>
		  </div> 
		  <div class="form-group">
		  	 <label for="reader_readerTypeObj_readerTypeId_edit" class="col-md-3 text-right">读者类型:</label>
		  	 <div class="col-md-9">
			    <select id="reader_readerTypeObj_readerTypeId_edit" name="reader.readerTypeObj.readerTypeId" class="form-control">
			    </select>
		  	 </div>
		  </div>
		  <div class="form-group">
		  	 <label for="reader_readerName_edit" class="col-md-3 text-right">姓名:</label>
		  	 <div class="col-md-9">
			    <input type="text" id="reader_readerName_edit" name="reader.readerName" class="form-control" placeholder="请输入姓名">
			 </div>
		  </div>
		  <div class="form-group">
		  	 <label for="reader_sex_edit" class="col-md-3 text-right">性别:</label>
		  	 <div class="col-md-9">
			    <input type="text" id="reader_sex_edit" name="reader.sex" class="form-control" placeholder="请输入性别">
			 </div>
		  </div>
		  <div class="form-group">
		  	 <label for="reader_birthday_edit" class="col-md-3 text-right">读者生日:</label>
		  	 <div class="col-md-9">
                <div class="input-group date reader_birthday_edit col-md-12" data-link-field="reader_birthday_edit" data-link-format="yyyy-mm-dd">
                    <input class="form-control" id="reader_birthday_edit" name="reader.birthday" size="16" type="text" value="" placeholder="请选择读者生日" readonly>
                    <span class="input-group-addon"><span class="glyphicon glyphicon-remove"></span></span>
                    <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                </div>
		  	 </div>
		  </div>
		  <div class="form-group">
		  	 <label for="reader_telephone_edit" class="col-md-3 text-right">联系电话:</label>
		  	 <div class="col-md-9">
			    <input type="text" id="reader_telephone_edit" name="reader.telephone" class="form-control" placeholder="请输入联系电话">
			 </div>
		  </div>
		  <div class="form-group">
		  	 <label for="reader_email_edit" class="col-md-3 text-right">联系Email:</label>
		  	 <div class="col-md-9">
			    <input type="text" id="reader_email_edit" name="reader.email" class="form-control" placeholder="请输入联系Email">
			 </div>
		  </div>
		  <div class="form-group">
		  	 <label for="reader_qq_edit" class="col-md-3 text-right">联系qq:</label>
		  	 <div class="col-md-9">
			    <input type="text" id="reader_qq_edit" name="reader.qq" class="form-control" placeholder="请输入联系qq">
			 </div>
		  </div>
		  <div class="form-group">
		  	 <label for="reader_address_edit" class="col-md-3 text-right">读者地址:</label>
		  	 <div class="col-md-9">
			    <input type="text" id="reader_address_edit" name="reader.address" class="form-control" placeholder="请输入读者地址">
			 </div>
		  </div>
		  <div class="form-group">
		  	 <label for="reader_photo_edit" class="col-md-3 text-right">读者头像:</label>
		  	 <div class="col-md-9">
			    <img  class="img-responsive" id="reader_photoImg" border="0px"/><br/>
			    <input type="hidden" id="reader_photo" name="reader.photo"/>
			    <input id="photoFile" name="photoFile" type="file" size="50" />
		  	 </div>
		  </div>
		</form> 
	    <style>#readerEditForm .form-group {margin-bottom:5px;}  </style>
      </div>
      <div class="modal-footer"> 
      	<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
      	<button type="button" class="btn btn-primary" onclick="ajaxReaderModify();">提交</button>
      </div>
    </div><!-- /.modal-content -->
  </div><!-- /.modal-dialog -->
</div><!-- /.modal -->
<jsp:include page="../footer.jsp"></jsp:include> 
<script src="<%=basePath %>plugins/jquery.min.js"></script>
<script src="<%=basePath %>plugins/bootstrap.js"></script>
<script src="<%=basePath %>plugins/wow.min.js"></script>
<script src="<%=basePath %>plugins/bootstrap-datetimepicker.min.js"></script>
<script src="<%=basePath %>plugins/locales/bootstrap-datetimepicker.zh-CN.js"></script>
<script type="text/javascript" src="<%=basePath %>js/jsdate.js"></script>
<script>
var basePath = "<%=basePath%>";
/*跳转到查询结果的某页*/
function GoToPage(currentPage,totalPage) {
    if(currentPage==0) return;
    if(currentPage>totalPage) return;
    document.readerQueryForm.currentPage.value = currentPage;
    document.readerQueryForm.submit();
}

/*可以直接跳转到某页*/
function changepage(totalPage)
{
    var pageValue=document.readerQueryForm.pageValue.value;
    if(pageValue>totalPage) {
        alert('你输入的页码超出了总页数!');
        return ;
    }
    document.readerQueryForm.currentPage.value = pageValue;
    documentreaderQueryForm.submit();
}

/*弹出修改读者界面并初始化数据*/
function readerEdit(readerNo) {
	$.ajax({
		url :  basePath + "Reader/" + readerNo + "/update",
		type : "get",
		dataType: "json",
		success : function (reader, response, status) {
			if (reader) {
				$("#reader_readerNo_edit").val(reader.readerNo);
				$.ajax({
					url: basePath + "ReaderType/listAll",
					type: "get",
					success: function(readerTypes,response,status) { 
						$("#reader_readerTypeObj_readerTypeId_edit").empty();
						var html="";
		        		$(readerTypes).each(function(i,readerType){
		        			html += "<option value='" + readerType.readerTypeId + "'>" + readerType.readerTypeName + "</option>";
		        		});
		        		$("#reader_readerTypeObj_readerTypeId_edit").html(html);
		        		$("#reader_readerTypeObj_readerTypeId_edit").val(reader.readerTypeObjPri);
					}
				});
				$("#reader_readerName_edit").val(reader.readerName);
				$("#reader_sex_edit").val(reader.sex);
				$("#reader_birthday_edit").val(reader.birthday);
				$("#reader_telephone_edit").val(reader.telephone);
				$("#reader_email_edit").val(reader.email);
				$("#reader_qq_edit").val(reader.qq);
				$("#reader_address_edit").val(reader.address);
				$("#reader_photo").val(reader.photo);
				$("#reader_photoImg").attr("src", basePath +　reader.photo);
				$('#readerEditDialog').modal('show');
			} else {
				alert("获取信息失败！");
			}
		}
	});
}

/*删除读者信息*/
function readerDelete(readerNo) {
	if(confirm("确认删除这个记录")) {
		$.ajax({
			type : "POST",
			url : basePath + "Reader/deletes",
			data : {
				readerNos : readerNo,
			},
			success : function (obj) {
				if (obj.success) {
					alert("删除成功");
					$("#readerQueryForm").submit();
					//location.href= basePath + "Reader/frontlist";
				}
				else 
					alert(data.message);
			},
		});
	}
}

/*ajax方式提交读者信息表单给服务器端修改*/
function ajaxReaderModify() {
	$.ajax({
		url :  basePath + "Reader/" + $("#reader_readerNo_edit").val() + "/update",
		type : "post",
		dataType: "json",
		data: new FormData($("#readerEditForm")[0]),
		success : function (obj, response, status) {
            if(obj.success){
                alert("信息修改成功！");
                $("#readerQueryForm").submit();
            }else{
                alert(obj.message);
            } 
		},
		processData: false,
		contentType: false,
	});
}

$(function(){
	/*小屏幕导航点击关闭菜单*/
    $('.navbar-collapse a').click(function(){
        $('.navbar-collapse').collapse('hide');
    });
    new WOW().init();

    /*读者生日组件*/
    $('.reader_birthday_edit').datetimepicker({
    	language:  'zh-CN',  //语言
    	format: 'yyyy-mm-dd',
    	minView: 2,
    	weekStart: 1,
    	todayBtn:  1,
    	autoclose: 1,
    	minuteStep: 1,
    	todayHighlight: 1,
    	startView: 2,
    	forceParse: 0
    });
})
</script>
</body>
</html>

