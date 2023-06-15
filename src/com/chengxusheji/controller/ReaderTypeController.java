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
import com.chengxusheji.service.ReaderTypeService;
import com.chengxusheji.po.ReaderType;

//ReaderType管理控制层
@Controller
@RequestMapping("/ReaderType")
public class ReaderTypeController extends BaseController {

    /*业务层对象*/
    @Resource ReaderTypeService readerTypeService;

	@InitBinder("readerType")
	public void initBinderReaderType(WebDataBinder binder) {
		binder.setFieldDefaultPrefix("readerType.");
	}
	/*跳转到添加ReaderType视图*/
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(Model model,HttpServletRequest request) throws Exception {
		model.addAttribute(new ReaderType());
		return "ReaderType_add";
	}

	/*客户端ajax方式提交添加读者类型信息*/
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public void add(@Validated ReaderType readerType, BindingResult br,
			Model model, HttpServletRequest request,HttpServletResponse response) throws Exception {
		String message = "";
		boolean success = false;
		if (br.hasErrors()) {
			message = "输入信息不符合要求！";
			writeJsonResponse(response, success, message);
			return ;
		}
        readerTypeService.addReaderType(readerType);
        message = "读者类型添加成功!";
        success = true;
        writeJsonResponse(response, success, message);
	}
	/*ajax方式按照查询条件分页查询读者类型信息*/
	@RequestMapping(value = { "/list" }, method = {RequestMethod.GET,RequestMethod.POST})
	public void list(Integer page,Integer rows, Model model, HttpServletRequest request,HttpServletResponse response) throws Exception {
		if (page==null || page == 0) page = 1;
		if(rows != 0)readerTypeService.setRows(rows);
		List<ReaderType> readerTypeList = readerTypeService.queryReaderType(page);
	    /*计算总的页数和总的记录数*/
	    readerTypeService.queryTotalPageAndRecordNumber();
	    /*获取到总的页码数目*/
	    int totalPage = readerTypeService.getTotalPage();
	    /*当前查询条件下总记录数*/
	    int recordNumber = readerTypeService.getRecordNumber();
        response.setContentType("text/json;charset=UTF-8");
		PrintWriter out = response.getWriter();
		//将要被返回到客户端的对象
		JSONObject jsonObj=new JSONObject();
		jsonObj.accumulate("total", recordNumber);
		JSONArray jsonArray = new JSONArray();
		for(ReaderType readerType:readerTypeList) {
			JSONObject jsonReaderType = readerType.getJsonObject();
			jsonArray.put(jsonReaderType);
		}
		jsonObj.accumulate("rows", jsonArray);
		out.println(jsonObj.toString());
		out.flush();
		out.close();
	}

	/*ajax方式按照查询条件分页查询读者类型信息*/
	@RequestMapping(value = { "/listAll" }, method = {RequestMethod.GET,RequestMethod.POST})
	public void listAll(HttpServletResponse response) throws Exception {
		List<ReaderType> readerTypeList = readerTypeService.queryAllReaderType();
        response.setContentType("text/json;charset=UTF-8"); 
		PrintWriter out = response.getWriter();
		JSONArray jsonArray = new JSONArray();
		for(ReaderType readerType:readerTypeList) {
			JSONObject jsonReaderType = new JSONObject();
			jsonReaderType.accumulate("readerTypeId", readerType.getReaderTypeId());
			jsonReaderType.accumulate("readerTypeName", readerType.getReaderTypeName());
			jsonArray.put(jsonReaderType);
		}
		out.println(jsonArray.toString());
		out.flush();
		out.close();
	}

	/*前台按照查询条件分页查询读者类型信息*/
	@RequestMapping(value = { "/frontlist" }, method = {RequestMethod.GET,RequestMethod.POST})
	public String frontlist(Integer currentPage, Model model, HttpServletRequest request) throws Exception  {
		if (currentPage==null || currentPage == 0) currentPage = 1;
		List<ReaderType> readerTypeList = readerTypeService.queryReaderType(currentPage);
	    /*计算总的页数和总的记录数*/
	    readerTypeService.queryTotalPageAndRecordNumber();
	    /*获取到总的页码数目*/
	    int totalPage = readerTypeService.getTotalPage();
	    /*当前查询条件下总记录数*/
	    int recordNumber = readerTypeService.getRecordNumber();
	    request.setAttribute("readerTypeList",  readerTypeList);
	    request.setAttribute("totalPage", totalPage);
	    request.setAttribute("recordNumber", recordNumber);
	    request.setAttribute("currentPage", currentPage);
		return "ReaderType/readerType_frontquery_result"; 
	}

     /*前台查询ReaderType信息*/
	@RequestMapping(value="/{readerTypeId}/frontshow",method=RequestMethod.GET)
	public String frontshow(@PathVariable Integer readerTypeId,Model model,HttpServletRequest request) throws Exception {
		/*根据主键readerTypeId获取ReaderType对象*/
        ReaderType readerType = readerTypeService.getReaderType(readerTypeId);

        request.setAttribute("readerType",  readerType);
        return "ReaderType/readerType_frontshow";
	}

	/*ajax方式显示读者类型修改jsp视图页*/
	@RequestMapping(value="/{readerTypeId}/update",method=RequestMethod.GET)
	public void update(@PathVariable Integer readerTypeId,Model model,HttpServletRequest request,HttpServletResponse response) throws Exception {
        /*根据主键readerTypeId获取ReaderType对象*/
        ReaderType readerType = readerTypeService.getReaderType(readerTypeId);

        response.setContentType("text/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
		//将要被返回到客户端的对象 
		JSONObject jsonReaderType = readerType.getJsonObject();
		out.println(jsonReaderType.toString());
		out.flush();
		out.close();
	}

	/*ajax方式更新读者类型信息*/
	@RequestMapping(value = "/{readerTypeId}/update", method = RequestMethod.POST)
	public void update(@Validated ReaderType readerType, BindingResult br,
			Model model, HttpServletRequest request,HttpServletResponse response) throws Exception {
		String message = "";
    	boolean success = false;
		if (br.hasErrors()) { 
			message = "输入的信息有错误！";
			writeJsonResponse(response, success, message);
			return;
		}
		try {
			readerTypeService.updateReaderType(readerType);
			message = "读者类型更新成功!";
			success = true;
			writeJsonResponse(response, success, message);
		} catch (Exception e) {
			e.printStackTrace();
			message = "读者类型更新失败!";
			writeJsonResponse(response, success, message); 
		}
	}
    /*删除读者类型信息*/
	@RequestMapping(value="/{readerTypeId}/delete",method=RequestMethod.GET)
	public String delete(@PathVariable Integer readerTypeId,HttpServletRequest request) throws UnsupportedEncodingException {
		  try {
			  readerTypeService.deleteReaderType(readerTypeId);
	            request.setAttribute("message", "读者类型删除成功!");
	            return "message";
	        } catch (Exception e) { 
	            e.printStackTrace();
	            request.setAttribute("error", "读者类型删除失败!");
				return "error";

	        }

	}

	/*ajax方式删除多条读者类型记录*/
	@RequestMapping(value="/deletes",method=RequestMethod.POST)
	public void delete(String readerTypeIds,HttpServletRequest request,HttpServletResponse response) throws IOException, JSONException {
		String message = "";
    	boolean success = false;
        try { 
        	int count = readerTypeService.deleteReaderTypes(readerTypeIds);
        	success = true;
        	message = count + "条记录删除成功";
        	writeJsonResponse(response, success, message);
        } catch (Exception e) { 
            //e.printStackTrace();
            message = "有记录存在外键约束,删除失败";
            writeJsonResponse(response, success, message);
        }
	}

	/*按照查询条件导出读者类型信息到Excel*/
	@RequestMapping(value = { "/OutToExcel" }, method = {RequestMethod.GET,RequestMethod.POST})
	public void OutToExcel( Model model, HttpServletRequest request,HttpServletResponse response) throws Exception {
        List<ReaderType> readerTypeList = readerTypeService.queryReaderType();
        ExportExcelUtil ex = new ExportExcelUtil();
        String title = "ReaderType信息记录"; 
        String[] headers = { "读者类型编号","读者类型","可借阅数目"};
        List<String[]> dataset = new ArrayList<String[]>(); 
        for(int i=0;i<readerTypeList.size();i++) {
        	ReaderType readerType = readerTypeList.get(i); 
        	dataset.add(new String[]{readerType.getReaderTypeId() + "",readerType.getReaderTypeName(),readerType.getNumber() + ""});
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
			response.setHeader("Content-disposition","attachment; filename="+"ReaderType.xls");//filename是下载的xls的名，建议最好用英文 
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
