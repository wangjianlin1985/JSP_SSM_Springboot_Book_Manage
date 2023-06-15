<%@ page language="java" import="java.util.*"  contentType="text/html;charset=UTF-8"%> 
<%@ page import="com.chengxusheji.po.Book" %>
<%@ page import="com.chengxusheji.po.BookType" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
    List<Book> bookList = (List<Book>)request.getAttribute("bookList");
    //获取所有的bookTypeObj信息
    List<BookType> bookTypeList = (List<BookType>)request.getAttribute("bookTypeList");
    int currentPage =  (Integer)request.getAttribute("currentPage"); //当前页
    int totalPage =   (Integer)request.getAttribute("totalPage");  //一共多少页
    int recordNumber =   (Integer)request.getAttribute("recordNumber");  //一共多少记录
    String barcode = (String)request.getAttribute("barcode"); //图书条形码查询关键字
    String bookName = (String)request.getAttribute("bookName"); //图书名称查询关键字
    BookType bookTypeObj = (BookType)request.getAttribute("bookTypeObj");
    String publishDate = (String)request.getAttribute("publishDate"); //出版日期查询关键字
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1 , user-scalable=no">
<title>图书查询</title>
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
  			<li><a href="<%=basePath %>Book/frontlist">图书信息列表</a></li>
  			<li class="active">查询结果显示</li>
  			<a class="pull-right" href="<%=basePath %>Book/book_frontAdd.jsp">添加图书</a>
		</ul>
		<div class="row">
			<%
				/*计算起始序号*/
				int startIndex = (currentPage -1) * 5;
				/*遍历记录*/
				for(int i=0;i<bookList.size();i++) {
            		int currentIndex = startIndex + i + 1; //当前记录的序号
            		Book book = bookList.get(i); //获取到图书对象
            		String clearLeft = "";
            		if(i%4 == 0) clearLeft = "style=\"clear:left;\"";
			%>
			<div class="col-md-3 bottom15" <%=clearLeft %>>
			  <a  href="<%=basePath  %>Book/<%=book.getBarcode() %>/frontshow"><img class="img-responsive" src="<%=basePath%><%=book.getBookPhoto()%>" /></a>
			     <div class="showFields">
			     	<div class="field">
	            		图书条形码:<%=book.getBarcode() %>
			     	</div>
			     	<div class="field">
	            		图书名称:<%=book.getBookName() %>
			     	</div>
			     	<div class="field">
	            		图书所在类别:<%=book.getBookTypeObj().getBookTypeName() %>
			     	</div>
			     	 
			     	<div class="field">
	            		出版日期:<%=book.getPublishDate() %>
			     	</div>
			     	<div class="field">
	            		出版社:<%=book.getPublish() %>
			     	</div>
			        <a class="btn btn-primary top5" href="<%=basePath %>Book/<%=book.getBarcode() %>/frontshow">详情</a>
			        <a class="btn btn-primary top5" onclick="bookEdit('<%=book.getBarcode() %>');">修改</a>
			        <a class="btn btn-primary top5" onclick="bookDelete('<%=book.getBarcode() %>');">删除</a>
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
    		<h1>图书查询</h1>
		</div>
		<form name="bookQueryForm" id="bookQueryForm" action="<%=basePath %>/Book/frontlist" class="mar_t15">
			<div class="form-group">
				<label for="barcode">图书条形码:</label>
				<input type="text" id="barcode" name="barcode" value="<%=barcode %>" class="form-control" placeholder="请输入图书条形码">
			</div>
			<div class="form-group">
				<label for="bookName">图书名称:</label>
				<input type="text" id="bookName" name="bookName" value="<%=bookName %>" class="form-control" placeholder="请输入图书名称">
			</div>
            <div class="form-group">
            	<label for="bookTypeObj_bookTypeId">图书所在类别：</label>
                <select id="bookTypeObj_bookTypeId" name="bookTypeObj.bookTypeId" class="form-control">
                	<option value="0">不限制</option>
	 				<%
	 				for(BookType bookTypeTemp:bookTypeList) {
	 					String selected = "";
 					if(bookTypeObj!=null && bookTypeObj.getBookTypeId()!=null && bookTypeObj.getBookTypeId().intValue()==bookTypeTemp.getBookTypeId().intValue())
 						selected = "selected";
	 				%>
 				 <option value="<%=bookTypeTemp.getBookTypeId() %>" <%=selected %>><%=bookTypeTemp.getBookTypeName() %></option>
	 				<%
	 				}
	 				%>
 			</select>
            </div>
			<div class="form-group">
				<label for="publishDate">出版日期:</label>
				<input type="text" id="publishDate" name="publishDate" class="form-control"  placeholder="请选择出版日期" value="<%=publishDate %>" onclick="SelectDate(this,'yyyy-MM-dd')" />
			</div>
            <input type=hidden name=currentPage value="<%=currentPage %>" />
            <button type="submit" class="btn btn-primary">查询</button>
        </form>
	</div>

		</div>
</div>
<div id="bookEditDialog" class="modal fade" tabindex="-1" role="dialog">
  <div class="modal-dialog" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
        <h4 class="modal-title"><i class="fa fa-edit"></i>&nbsp;图书信息编辑</h4>
      </div>
      <div class="modal-body" style="height:450px; overflow: scroll;">
      	<form class="form-horizontal" name="bookEditForm" id="bookEditForm" enctype="multipart/form-data" method="post"  class="mar_t15">
		  <div class="form-group">
			 <label for="book_barcode_edit" class="col-md-3 text-right">图书条形码:</label>
			 <div class="col-md-9"> 
			 	<input type="text" id="book_barcode_edit" name="book.barcode" class="form-control" placeholder="请输入图书条形码" readOnly>
			 </div>
		  </div> 
		  <div class="form-group">
		  	 <label for="book_bookName_edit" class="col-md-3 text-right">图书名称:</label>
		  	 <div class="col-md-9">
			    <input type="text" id="book_bookName_edit" name="book.bookName" class="form-control" placeholder="请输入图书名称">
			 </div>
		  </div>
		  <div class="form-group">
		  	 <label for="book_bookTypeObj_bookTypeId_edit" class="col-md-3 text-right">图书所在类别:</label>
		  	 <div class="col-md-9">
			    <select id="book_bookTypeObj_bookTypeId_edit" name="book.bookTypeObj.bookTypeId" class="form-control">
			    </select>
		  	 </div>
		  </div>
		  <div class="form-group">
		  	 <label for="book_price_edit" class="col-md-3 text-right">图书价格:</label>
		  	 <div class="col-md-9">
			    <input type="text" id="book_price_edit" name="book.price" class="form-control" placeholder="请输入图书价格">
			 </div>
		  </div>
		  <div class="form-group">
		  	 <label for="book_count_edit" class="col-md-3 text-right">库存:</label>
		  	 <div class="col-md-9">
			    <input type="text" id="book_count_edit" name="book.count" class="form-control" placeholder="请输入库存">
			 </div>
		  </div>
		  <div class="form-group">
		  	 <label for="book_publishDate_edit" class="col-md-3 text-right">出版日期:</label>
		  	 <div class="col-md-9">
                <div class="input-group date book_publishDate_edit col-md-12" data-link-field="book_publishDate_edit" data-link-format="yyyy-mm-dd">
                    <input class="form-control" id="book_publishDate_edit" name="book.publishDate" size="16" type="text" value="" placeholder="请选择出版日期" readonly>
                    <span class="input-group-addon"><span class="glyphicon glyphicon-remove"></span></span>
                    <span class="input-group-addon"><span class="glyphicon glyphicon-calendar"></span></span>
                </div>
		  	 </div>
		  </div>
		  <div class="form-group">
		  	 <label for="book_publish_edit" class="col-md-3 text-right">出版社:</label>
		  	 <div class="col-md-9">
			    <input type="text" id="book_publish_edit" name="book.publish" class="form-control" placeholder="请输入出版社">
			 </div>
		  </div>
		  <div class="form-group">
		  	 <label for="book_bookPhoto_edit" class="col-md-3 text-right">图书图片:</label>
		  	 <div class="col-md-9">
			    <img  class="img-responsive" id="book_bookPhotoImg" border="0px"/><br/>
			    <input type="hidden" id="book_bookPhoto" name="book.bookPhoto"/>
			    <input id="bookPhotoFile" name="bookPhotoFile" type="file" size="50" />
		  	 </div>
		  </div>
		  <div class="form-group">
		  	 <label for="book_introduction_edit" class="col-md-3 text-right">图书简介:</label>
		  	 <div class="col-md-9">
			    <textarea id="book_introduction_edit" name="book.introduction" rows="8" class="form-control" placeholder="请输入图书简介"></textarea>
			 </div>
		  </div>
		</form> 
	    <style>#bookEditForm .form-group {margin-bottom:5px;}  </style>
      </div>
      <div class="modal-footer"> 
      	<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
      	<button type="button" class="btn btn-primary" onclick="ajaxBookModify();">提交</button>
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
    document.bookQueryForm.currentPage.value = currentPage;
    document.bookQueryForm.submit();
}

/*可以直接跳转到某页*/
function changepage(totalPage)
{
    var pageValue=document.bookQueryForm.pageValue.value;
    if(pageValue>totalPage) {
        alert('你输入的页码超出了总页数!');
        return ;
    }
    document.bookQueryForm.currentPage.value = pageValue;
    documentbookQueryForm.submit();
}

/*弹出修改图书界面并初始化数据*/
function bookEdit(barcode) {
	$.ajax({
		url :  basePath + "Book/" + barcode + "/update",
		type : "get",
		dataType: "json",
		success : function (book, response, status) {
			if (book) {
				$("#book_barcode_edit").val(book.barcode);
				$("#book_bookName_edit").val(book.bookName);
				$.ajax({
					url: basePath + "BookType/listAll",
					type: "get",
					success: function(bookTypes,response,status) { 
						$("#book_bookTypeObj_bookTypeId_edit").empty();
						var html="";
		        		$(bookTypes).each(function(i,bookType){
		        			html += "<option value='" + bookType.bookTypeId + "'>" + bookType.bookTypeName + "</option>";
		        		});
		        		$("#book_bookTypeObj_bookTypeId_edit").html(html);
		        		$("#book_bookTypeObj_bookTypeId_edit").val(book.bookTypeObjPri);
					}
				});
				$("#book_price_edit").val(book.price);
				$("#book_count_edit").val(book.count);
				$("#book_publishDate_edit").val(book.publishDate);
				$("#book_publish_edit").val(book.publish);
				$("#book_bookPhoto").val(book.bookPhoto);
				$("#book_bookPhotoImg").attr("src", basePath +　book.bookPhoto);
				$("#book_introduction_edit").val(book.introduction);
				$('#bookEditDialog').modal('show');
			} else {
				alert("获取信息失败！");
			}
		}
	});
}

/*删除图书信息*/
function bookDelete(barcode) {
	if(confirm("确认删除这个记录")) {
		$.ajax({
			type : "POST",
			url : basePath + "Book/deletes",
			data : {
				barcodes : barcode,
			},
			success : function (obj) {
				if (obj.success) {
					alert("删除成功");
					$("#bookQueryForm").submit();
					//location.href= basePath + "Book/frontlist";
				}
				else 
					alert(data.message);
			},
		});
	}
}

/*ajax方式提交图书信息表单给服务器端修改*/
function ajaxBookModify() {
	$.ajax({
		url :  basePath + "Book/" + $("#book_barcode_edit").val() + "/update",
		type : "post",
		dataType: "json",
		data: new FormData($("#bookEditForm")[0]),
		success : function (obj, response, status) {
            if(obj.success){
                alert("信息修改成功！");
                $("#bookQueryForm").submit();
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

    /*出版日期组件*/
    $('.book_publishDate_edit').datetimepicker({
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

