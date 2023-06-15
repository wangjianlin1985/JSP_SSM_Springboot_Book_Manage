package com.chengxusheji.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import com.chengxusheji.utils.ExportExcelUtil;
import com.chengxusheji.utils.UserException;
import com.chengxusheji.service.BookService;
import com.chengxusheji.po.Book;
import com.chengxusheji.service.BookTypeService;
import com.chengxusheji.po.BookType;

//Book管理控制层
@Controller
@RequestMapping("/Book")
public class BookController extends BaseController {

    /*业务层对象*/
    @Resource BookService bookService;

    @Resource BookTypeService bookTypeService;
	@InitBinder("bookTypeObj")
	public void initBinderbookTypeObj(WebDataBinder binder) {
		binder.setFieldDefaultPrefix("bookTypeObj.");
	}
	@InitBinder("book")
	public void initBinderBook(WebDataBinder binder) {
		binder.setFieldDefaultPrefix("book.");
	}
	/*跳转到添加Book视图*/
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(Model model,HttpServletRequest request) throws Exception {
		model.addAttribute(new Book());
		/*查询所有的BookType信息*/
		List<BookType> bookTypeList = bookTypeService.queryAllBookType();
		request.setAttribute("bookTypeList", bookTypeList);
		return "Book_add";
	}

	/*客户端ajax方式提交添加图书信息*/
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public void add(@Validated Book book, BindingResult br,
			Model model, HttpServletRequest request,HttpServletResponse response) throws Exception {
		String message = "";
		boolean success = false;
		if (br.hasErrors()) {
			message = "输入信息不符合要求！";
			writeJsonResponse(response, success, message);
			return ;
		}
		if(bookService.getBook(book.getBarcode()) != null) {
			message = "图书条形码已经存在！";
			writeJsonResponse(response, success, message);
			return ;
		}
		try {
			book.setBookPhoto(this.handlePhotoUpload(request, "bookPhotoFile"));
		} catch(UserException ex) {
			message = "图片格式不正确！";
			writeJsonResponse(response, success, message);
			return ;
		}
        bookService.addBook(book);
        message = "图书添加成功!";
        success = true;
        writeJsonResponse(response, success, message);
	}
	/*ajax方式按照查询条件分页查询图书信息*/
	@RequestMapping(value = { "/list" }, method = {RequestMethod.GET,RequestMethod.POST})
	public void list(String barcode,String bookName,@ModelAttribute("bookTypeObj") BookType bookTypeObj,String publishDate,Integer page,Integer rows, Model model, HttpServletRequest request,HttpServletResponse response) throws Exception {
		if (page==null || page == 0) page = 1;
		if (barcode == null) barcode = "";
		if (bookName == null) bookName = "";
		if (publishDate == null) publishDate = "";
		if(rows != 0)bookService.setRows(rows);
		List<Book> bookList = bookService.queryBook(barcode, bookName, bookTypeObj, publishDate, page);
	    /*计算总的页数和总的记录数*/
	    bookService.queryTotalPageAndRecordNumber(barcode, bookName, bookTypeObj, publishDate);
	    /*获取到总的页码数目*/
	    int totalPage = bookService.getTotalPage();
	    /*当前查询条件下总记录数*/
	    int recordNumber = bookService.getRecordNumber();
        response.setContentType("text/json;charset=UTF-8");
		PrintWriter out = response.getWriter();
		//将要被返回到客户端的对象
		JSONObject jsonObj=new JSONObject();
		jsonObj.accumulate("total", recordNumber);
		JSONArray jsonArray = new JSONArray();
		for(Book book:bookList) {
			JSONObject jsonBook = book.getJsonObject();
			jsonArray.put(jsonBook);
		}
		jsonObj.accumulate("rows", jsonArray);
		out.println(jsonObj.toString());
		out.flush();
		out.close();
	}

	/*ajax方式按照查询条件分页查询图书信息*/
	@RequestMapping(value = { "/listAll" }, method = {RequestMethod.GET,RequestMethod.POST})
	public void listAll(HttpServletResponse response) throws Exception {
		List<Book> bookList = bookService.queryAllBook();
        response.setContentType("text/json;charset=UTF-8"); 
		PrintWriter out = response.getWriter();
		JSONArray jsonArray = new JSONArray();
		for(Book book:bookList) {
			JSONObject jsonBook = new JSONObject();
			jsonBook.accumulate("barcode", book.getBarcode());
			jsonBook.accumulate("bookName", book.getBookName());
			jsonArray.put(jsonBook);
		}
		out.println(jsonArray.toString());
		out.flush();
		out.close();
	}

	/*前台按照查询条件分页查询图书信息*/
	@RequestMapping(value = { "/frontlist" }, method = {RequestMethod.GET,RequestMethod.POST})
	public String frontlist(String barcode,String bookName,@ModelAttribute("bookTypeObj") BookType bookTypeObj,String publishDate,Integer currentPage, Model model, HttpServletRequest request) throws Exception  {
		if (currentPage==null || currentPage == 0) currentPage = 1;
		if (barcode == null) barcode = "";
		if (bookName == null) bookName = "";
		if (publishDate == null) publishDate = "";
		List<Book> bookList = bookService.queryBook(barcode, bookName, bookTypeObj, publishDate, currentPage);
	    /*计算总的页数和总的记录数*/
	    bookService.queryTotalPageAndRecordNumber(barcode, bookName, bookTypeObj, publishDate);
	    /*获取到总的页码数目*/
	    int totalPage = bookService.getTotalPage();
	    /*当前查询条件下总记录数*/
	    int recordNumber = bookService.getRecordNumber();
	    request.setAttribute("bookList",  bookList);
	    request.setAttribute("totalPage", totalPage);
	    request.setAttribute("recordNumber", recordNumber);
	    request.setAttribute("currentPage", currentPage);
	    request.setAttribute("barcode", barcode);
	    request.setAttribute("bookName", bookName);
	    request.setAttribute("bookTypeObj", bookTypeObj);
	    request.setAttribute("publishDate", publishDate);
	    List<BookType> bookTypeList = bookTypeService.queryAllBookType();
	    request.setAttribute("bookTypeList", bookTypeList);
		return "Book/book_frontquery_result"; 
	}

     /*前台查询Book信息*/
	@RequestMapping(value="/{barcode}/frontshow",method=RequestMethod.GET)
	public String frontshow(@PathVariable String barcode,Model model,HttpServletRequest request) throws Exception {
		/*根据主键barcode获取Book对象*/
        Book book = bookService.getBook(barcode);

        List<BookType> bookTypeList = bookTypeService.queryAllBookType();
        request.setAttribute("bookTypeList", bookTypeList);
        request.setAttribute("book",  book);
        return "Book/book_frontshow";
	}

	/*ajax方式显示图书修改jsp视图页*/
	@RequestMapping(value="/{barcode}/update",method=RequestMethod.GET)
	public void update(@PathVariable String barcode,Model model,HttpServletRequest request,HttpServletResponse response) throws Exception {
        /*根据主键barcode获取Book对象*/
        Book book = bookService.getBook(barcode);

        response.setContentType("text/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
		//将要被返回到客户端的对象 
		JSONObject jsonBook = book.getJsonObject();
		out.println(jsonBook.toString());
		out.flush();
		out.close();
	}

	/*ajax方式更新图书信息*/
	@RequestMapping(value = "/{barcode}/update", method = RequestMethod.POST)
	public void update(@Validated Book book, BindingResult br,
			Model model, HttpServletRequest request,HttpServletResponse response) throws Exception {
		String message = "";
    	boolean success = false;
		if (br.hasErrors()) { 
			message = "输入的信息有错误！";
			writeJsonResponse(response, success, message);
			return;
		}
		String bookPhotoFileName = this.handlePhotoUpload(request, "bookPhotoFile");
		if(!bookPhotoFileName.equals("upload/NoImage.jpg"))book.setBookPhoto(bookPhotoFileName); 


		try {
			bookService.updateBook(book);
			message = "图书更新成功!";
			success = true;
			writeJsonResponse(response, success, message);
		} catch (Exception e) {
			e.printStackTrace();
			message = "图书更新失败!";
			writeJsonResponse(response, success, message); 
		}
	}
    /*删除图书信息*/
	@RequestMapping(value="/{barcode}/delete",method=RequestMethod.GET)
	public String delete(@PathVariable String barcode,HttpServletRequest request) throws UnsupportedEncodingException {
		  try {
			  bookService.deleteBook(barcode);
	            request.setAttribute("message", "图书删除成功!");
	            return "message";
	        } catch (Exception e) { 
	            e.printStackTrace();
	            request.setAttribute("error", "图书删除失败!");
				return "error";

	        }

	}

	/*ajax方式删除多条图书记录*/
	@RequestMapping(value="/deletes",method=RequestMethod.POST)
	public void delete(String barcodes,HttpServletRequest request,HttpServletResponse response) throws IOException, JSONException {
		String message = "";
    	boolean success = false;
        try { 
        	int count = bookService.deleteBooks(barcodes);
        	success = true;
        	message = count + "条记录删除成功";
        	writeJsonResponse(response, success, message);
        } catch (Exception e) { 
            //e.printStackTrace();
            message = "有记录存在外键约束,删除失败";
            writeJsonResponse(response, success, message);
        }
	}

	/*按照查询条件导出图书信息到Excel*/
	@RequestMapping(value = { "/OutToExcel" }, method = {RequestMethod.GET,RequestMethod.POST})
	public void OutToExcel(String barcode,String bookName,@ModelAttribute("bookTypeObj") BookType bookTypeObj,String publishDate, Model model, HttpServletRequest request,HttpServletResponse response) throws Exception {
        if(barcode == null) barcode = "";
        if(bookName == null) bookName = "";
        if(publishDate == null) publishDate = "";
        List<Book> bookList = bookService.queryBook(barcode,bookName,bookTypeObj,publishDate);
        ExportExcelUtil ex = new ExportExcelUtil();
        String title = "Book信息记录"; 
        String[] headers = { "图书条形码","图书名称","图书所在类别","图书价格","库存","出版日期","出版社","图书图片"};
        List<String[]> dataset = new ArrayList<String[]>(); 
        for(int i=0;i<bookList.size();i++) {
        	Book book = bookList.get(i); 
        	dataset.add(new String[]{book.getBarcode(),book.getBookName(),book.getBookTypeObj().getBookTypeName(),book.getPrice() + "",book.getCount() + "",book.getPublishDate(),book.getPublish(),book.getBookPhoto()});
        }
        /*
        OutputStream out = null;
		try {
			out = new FileOutputStream("C://output.xls");
			ex.exportExcel(title,headers, dataset, out);
		    out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		*/
		OutputStream out = null;//创建一个输出流对象 
		try { 
			out = response.getOutputStream();//
			response.setHeader("Content-disposition","attachment; filename="+"Book.xls");//filename是下载的xls的名，建议最好用英文 
			response.setContentType("application/msexcel;charset=UTF-8");//设置类型 
			response.setHeader("Pragma","No-cache");//设置头 
			response.setHeader("Cache-Control","no-cache");//设置头 
			response.setDateHeader("Expires", 0);//设置日期头  
			String rootPath = request.getSession().getServletContext().getRealPath("/");
			ex.exportExcel(rootPath,title,headers, dataset, out);
			out.flush();
		} catch (IOException e) { 
			e.printStackTrace(); 
		}finally{
			try{
				if(out!=null){ 
					out.close(); 
				}
			}catch(IOException e){ 
				e.printStackTrace(); 
			} 
		}
    }
}
