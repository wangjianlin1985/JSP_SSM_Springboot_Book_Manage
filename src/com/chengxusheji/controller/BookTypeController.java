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
import com.chengxusheji.service.BookTypeService;
import com.chengxusheji.po.BookType;

//BookType管理控制层
@Controller
@RequestMapping("/BookType")
public class BookTypeController extends BaseController {

    /*业务层对象*/
    @Resource BookTypeService bookTypeService;

	@InitBinder("bookType")
	public void initBinderBookType(WebDataBinder binder) {
		binder.setFieldDefaultPrefix("bookType.");
	}
	/*跳转到添加BookType视图*/
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(Model model,HttpServletRequest request) throws Exception {
		model.addAttribute(new BookType());
		return "BookType_add";
	}

	/*客户端ajax方式提交添加图书类型信息*/
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public void add(@Validated BookType bookType, BindingResult br,
			Model model, HttpServletRequest request,HttpServletResponse response) throws Exception {
		String message = "";
		boolean success = false;
		if (br.hasErrors()) {
			message = "输入信息不符合要求！";
			writeJsonResponse(response, success, message);
			return ;
		}
        bookTypeService.addBookType(bookType);
        message = "图书类型添加成功!";
        success = true;
        writeJsonResponse(response, success, message);
	}
	/*ajax方式按照查询条件分页查询图书类型信息*/
	@RequestMapping(value = { "/list" }, method = {RequestMethod.GET,RequestMethod.POST})
	public void list(Integer page,Integer rows, Model model, HttpServletRequest request,HttpServletResponse response) throws Exception {
		if (page==null || page == 0) page = 1;
		if(rows != 0)bookTypeService.setRows(rows);
		List<BookType> bookTypeList = bookTypeService.queryBookType(page);
	    /*计算总的页数和总的记录数*/
	    bookTypeService.queryTotalPageAndRecordNumber();
	    /*获取到总的页码数目*/
	    int totalPage = bookTypeService.getTotalPage();
	    /*当前查询条件下总记录数*/
	    int recordNumber = bookTypeService.getRecordNumber();
        response.setContentType("text/json;charset=UTF-8");
		PrintWriter out = response.getWriter();
		//将要被返回到客户端的对象
		JSONObject jsonObj=new JSONObject();
		jsonObj.accumulate("total", recordNumber);
		JSONArray jsonArray = new JSONArray();
		for(BookType bookType:bookTypeList) {
			JSONObject jsonBookType = bookType.getJsonObject();
			jsonArray.put(jsonBookType);
		}
		jsonObj.accumulate("rows", jsonArray);
		out.println(jsonObj.toString());
		out.flush();
		out.close();
	}

	/*ajax方式按照查询条件分页查询图书类型信息*/
	@RequestMapping(value = { "/listAll" }, method = {RequestMethod.GET,RequestMethod.POST})
	public void listAll(HttpServletResponse response) throws Exception {
		List<BookType> bookTypeList = bookTypeService.queryAllBookType();
        response.setContentType("text/json;charset=UTF-8"); 
		PrintWriter out = response.getWriter();
		JSONArray jsonArray = new JSONArray();
		for(BookType bookType:bookTypeList) {
			JSONObject jsonBookType = new JSONObject();
			jsonBookType.accumulate("bookTypeId", bookType.getBookTypeId());
			jsonBookType.accumulate("bookTypeName", bookType.getBookTypeName());
			jsonArray.put(jsonBookType);
		}
		out.println(jsonArray.toString());
		out.flush();
		out.close();
	}

	/*前台按照查询条件分页查询图书类型信息*/
	@RequestMapping(value = { "/frontlist" }, method = {RequestMethod.GET,RequestMethod.POST})
	public String frontlist(Integer currentPage, Model model, HttpServletRequest request) throws Exception  {
		if (currentPage==null || currentPage == 0) currentPage = 1;
		List<BookType> bookTypeList = bookTypeService.queryBookType(currentPage);
	    /*计算总的页数和总的记录数*/
	    bookTypeService.queryTotalPageAndRecordNumber();
	    /*获取到总的页码数目*/
	    int totalPage = bookTypeService.getTotalPage();
	    /*当前查询条件下总记录数*/
	    int recordNumber = bookTypeService.getRecordNumber();
	    request.setAttribute("bookTypeList",  bookTypeList);
	    request.setAttribute("totalPage", totalPage);
	    request.setAttribute("recordNumber", recordNumber);
	    request.setAttribute("currentPage", currentPage);
		return "BookType/bookType_frontquery_result"; 
	}

     /*前台查询BookType信息*/
	@RequestMapping(value="/{bookTypeId}/frontshow",method=RequestMethod.GET)
	public String frontshow(@PathVariable Integer bookTypeId,Model model,HttpServletRequest request) throws Exception {
		/*根据主键bookTypeId获取BookType对象*/
        BookType bookType = bookTypeService.getBookType(bookTypeId);

        request.setAttribute("bookType",  bookType);
        return "BookType/bookType_frontshow";
	}

	/*ajax方式显示图书类型修改jsp视图页*/
	@RequestMapping(value="/{bookTypeId}/update",method=RequestMethod.GET)
	public void update(@PathVariable Integer bookTypeId,Model model,HttpServletRequest request,HttpServletResponse response) throws Exception {
        /*根据主键bookTypeId获取BookType对象*/
        BookType bookType = bookTypeService.getBookType(bookTypeId);

        response.setContentType("text/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
		//将要被返回到客户端的对象 
		JSONObject jsonBookType = bookType.getJsonObject();
		out.println(jsonBookType.toString());
		out.flush();
		out.close();
	}

	/*ajax方式更新图书类型信息*/
	@RequestMapping(value = "/{bookTypeId}/update", method = RequestMethod.POST)
	public void update(@Validated BookType bookType, BindingResult br,
			Model model, HttpServletRequest request,HttpServletResponse response) throws Exception {
		String message = "";
    	boolean success = false;
		if (br.hasErrors()) { 
			message = "输入的信息有错误！";
			writeJsonResponse(response, success, message);
			return;
		}
		try {
			bookTypeService.updateBookType(bookType);
			message = "图书类型更新成功!";
			success = true;
			writeJsonResponse(response, success, message);
		} catch (Exception e) {
			e.printStackTrace();
			message = "图书类型更新失败!";
			writeJsonResponse(response, success, message); 
		}
	}
    /*删除图书类型信息*/
	@RequestMapping(value="/{bookTypeId}/delete",method=RequestMethod.GET)
	public String delete(@PathVariable Integer bookTypeId,HttpServletRequest request) throws UnsupportedEncodingException {
		  try {
			  bookTypeService.deleteBookType(bookTypeId);
	            request.setAttribute("message", "图书类型删除成功!");
	            return "message";
	        } catch (Exception e) { 
	            e.printStackTrace();
	            request.setAttribute("error", "图书类型删除失败!");
				return "error";

	        }

	}

	/*ajax方式删除多条图书类型记录*/
	@RequestMapping(value="/deletes",method=RequestMethod.POST)
	public void delete(String bookTypeIds,HttpServletRequest request,HttpServletResponse response) throws IOException, JSONException {
		String message = "";
    	boolean success = false;
        try { 
        	int count = bookTypeService.deleteBookTypes(bookTypeIds);
        	success = true;
        	message = count + "条记录删除成功";
        	writeJsonResponse(response, success, message);
        } catch (Exception e) { 
            //e.printStackTrace();
            message = "有记录存在外键约束,删除失败";
            writeJsonResponse(response, success, message);
        }
	}

	/*按照查询条件导出图书类型信息到Excel*/
	@RequestMapping(value = { "/OutToExcel" }, method = {RequestMethod.GET,RequestMethod.POST})
	public void OutToExcel( Model model, HttpServletRequest request,HttpServletResponse response) throws Exception {
        List<BookType> bookTypeList = bookTypeService.queryBookType();
        ExportExcelUtil ex = new ExportExcelUtil();
        String title = "BookType信息记录"; 
        String[] headers = { "图书类别","类别名称","可借阅天数"};
        List<String[]> dataset = new ArrayList<String[]>(); 
        for(int i=0;i<bookTypeList.size();i++) {
        	BookType bookType = bookTypeList.get(i); 
        	dataset.add(new String[]{bookType.getBookTypeId() + "",bookType.getBookTypeName(),bookType.getDays() + ""});
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
			response.setHeader("Content-disposition","attachment; filename="+"BookType.xls");//filename是下载的xls的名，建议最好用英文 
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
