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
import com.chengxusheji.service.ReaderService;
import com.chengxusheji.po.Reader;
import com.chengxusheji.service.ReaderTypeService;
import com.chengxusheji.po.ReaderType;

//Reader管理控制层
@Controller
@RequestMapping("/Reader")
public class ReaderController extends BaseController {

    /*业务层对象*/
    @Resource ReaderService readerService;

    @Resource ReaderTypeService readerTypeService;
	@InitBinder("readerTypeObj")
	public void initBinderreaderTypeObj(WebDataBinder binder) {
		binder.setFieldDefaultPrefix("readerTypeObj.");
	}
	@InitBinder("reader")
	public void initBinderReader(WebDataBinder binder) {
		binder.setFieldDefaultPrefix("reader.");
	}
	/*跳转到添加Reader视图*/
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(Model model,HttpServletRequest request) throws Exception {
		model.addAttribute(new Reader());
		/*查询所有的ReaderType信息*/
		List<ReaderType> readerTypeList = readerTypeService.queryAllReaderType();
		request.setAttribute("readerTypeList", readerTypeList);
		return "Reader_add";
	}

	/*客户端ajax方式提交添加读者信息*/
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public void add(@Validated Reader reader, BindingResult br,
			Model model, HttpServletRequest request,HttpServletResponse response) throws Exception {
		String message = "";
		boolean success = false;
		if (br.hasErrors()) {
			message = "输入信息不符合要求！";
			writeJsonResponse(response, success, message);
			return ;
		}
		if(readerService.getReader(reader.getReaderNo()) != null) {
			message = "读者编号已经存在！";
			writeJsonResponse(response, success, message);
			return ;
		}
		try {
			reader.setPhoto(this.handlePhotoUpload(request, "photoFile"));
		} catch(UserException ex) {
			message = "图片格式不正确！";
			writeJsonResponse(response, success, message);
			return ;
		}
        readerService.addReader(reader);
        message = "读者添加成功!";
        success = true;
        writeJsonResponse(response, success, message);
	}
	/*ajax方式按照查询条件分页查询读者信息*/
	@RequestMapping(value = { "/list" }, method = {RequestMethod.GET,RequestMethod.POST})
	public void list(String readerNo,@ModelAttribute("readerTypeObj") ReaderType readerTypeObj,String readerName,String birthday,String telephone,Integer page,Integer rows, Model model, HttpServletRequest request,HttpServletResponse response) throws Exception {
		if (page==null || page == 0) page = 1;
		if (readerNo == null) readerNo = "";
		if (readerName == null) readerName = "";
		if (birthday == null) birthday = "";
		if (telephone == null) telephone = "";
		if(rows != 0)readerService.setRows(rows);
		List<Reader> readerList = readerService.queryReader(readerNo, readerTypeObj, readerName, birthday, telephone, page);
	    /*计算总的页数和总的记录数*/
	    readerService.queryTotalPageAndRecordNumber(readerNo, readerTypeObj, readerName, birthday, telephone);
	    /*获取到总的页码数目*/
	    int totalPage = readerService.getTotalPage();
	    /*当前查询条件下总记录数*/
	    int recordNumber = readerService.getRecordNumber();
        response.setContentType("text/json;charset=UTF-8");
		PrintWriter out = response.getWriter();
		//将要被返回到客户端的对象
		JSONObject jsonObj=new JSONObject();
		jsonObj.accumulate("total", recordNumber);
		JSONArray jsonArray = new JSONArray();
		for(Reader reader:readerList) {
			JSONObject jsonReader = reader.getJsonObject();
			jsonArray.put(jsonReader);
		}
		jsonObj.accumulate("rows", jsonArray);
		out.println(jsonObj.toString());
		out.flush();
		out.close();
	}

	/*ajax方式按照查询条件分页查询读者信息*/
	@RequestMapping(value = { "/listAll" }, method = {RequestMethod.GET,RequestMethod.POST})
	public void listAll(HttpServletResponse response) throws Exception {
		List<Reader> readerList = readerService.queryAllReader();
        response.setContentType("text/json;charset=UTF-8"); 
		PrintWriter out = response.getWriter();
		JSONArray jsonArray = new JSONArray();
		for(Reader reader:readerList) {
			JSONObject jsonReader = new JSONObject();
			jsonReader.accumulate("readerNo", reader.getReaderNo());
			jsonReader.accumulate("readerName", reader.getReaderName());
			jsonArray.put(jsonReader);
		}
		out.println(jsonArray.toString());
		out.flush();
		out.close();
	}

	/*前台按照查询条件分页查询读者信息*/
	@RequestMapping(value = { "/frontlist" }, method = {RequestMethod.GET,RequestMethod.POST})
	public String frontlist(String readerNo,@ModelAttribute("readerTypeObj") ReaderType readerTypeObj,String readerName,String birthday,String telephone,Integer currentPage, Model model, HttpServletRequest request) throws Exception  {
		if (currentPage==null || currentPage == 0) currentPage = 1;
		if (readerNo == null) readerNo = "";
		if (readerName == null) readerName = "";
		if (birthday == null) birthday = "";
		if (telephone == null) telephone = "";
		List<Reader> readerList = readerService.queryReader(readerNo, readerTypeObj, readerName, birthday, telephone, currentPage);
	    /*计算总的页数和总的记录数*/
	    readerService.queryTotalPageAndRecordNumber(readerNo, readerTypeObj, readerName, birthday, telephone);
	    /*获取到总的页码数目*/
	    int totalPage = readerService.getTotalPage();
	    /*当前查询条件下总记录数*/
	    int recordNumber = readerService.getRecordNumber();
	    request.setAttribute("readerList",  readerList);
	    request.setAttribute("totalPage", totalPage);
	    request.setAttribute("recordNumber", recordNumber);
	    request.setAttribute("currentPage", currentPage);
	    request.setAttribute("readerNo", readerNo);
	    request.setAttribute("readerTypeObj", readerTypeObj);
	    request.setAttribute("readerName", readerName);
	    request.setAttribute("birthday", birthday);
	    request.setAttribute("telephone", telephone);
	    List<ReaderType> readerTypeList = readerTypeService.queryAllReaderType();
	    request.setAttribute("readerTypeList", readerTypeList);
		return "Reader/reader_frontquery_result"; 
	}

     /*前台查询Reader信息*/
	@RequestMapping(value="/{readerNo}/frontshow",method=RequestMethod.GET)
	public String frontshow(@PathVariable String readerNo,Model model,HttpServletRequest request) throws Exception {
		/*根据主键readerNo获取Reader对象*/
        Reader reader = readerService.getReader(readerNo);

        List<ReaderType> readerTypeList = readerTypeService.queryAllReaderType();
        request.setAttribute("readerTypeList", readerTypeList);
        request.setAttribute("reader",  reader);
        return "Reader/reader_frontshow";
	}

	/*ajax方式显示读者修改jsp视图页*/
	@RequestMapping(value="/{readerNo}/update",method=RequestMethod.GET)
	public void update(@PathVariable String readerNo,Model model,HttpServletRequest request,HttpServletResponse response) throws Exception {
        /*根据主键readerNo获取Reader对象*/
        Reader reader = readerService.getReader(readerNo);

        response.setContentType("text/json;charset=UTF-8");
        PrintWriter out = response.getWriter();
		//将要被返回到客户端的对象 
		JSONObject jsonReader = reader.getJsonObject();
		out.println(jsonReader.toString());
		out.flush();
		out.close();
	}

	/*ajax方式更新读者信息*/
	@RequestMapping(value = "/{readerNo}/update", method = RequestMethod.POST)
	public void update(@Validated Reader reader, BindingResult br,
			Model model, HttpServletRequest request,HttpServletResponse response) throws Exception {
		String message = "";
    	boolean success = false;
		if (br.hasErrors()) { 
			message = "输入的信息有错误！";
			writeJsonResponse(response, success, message);
			return;
		}
		String photoFileName = this.handlePhotoUpload(request, "photoFile");
		if(!photoFileName.equals("upload/NoImage.jpg"))reader.setPhoto(photoFileName); 


		try {
			readerService.updateReader(reader);
			message = "读者更新成功!";
			success = true;
			writeJsonResponse(response, success, message);
		} catch (Exception e) {
			e.printStackTrace();
			message = "读者更新失败!";
			writeJsonResponse(response, success, message); 
		}
	}
    /*删除读者信息*/
	@RequestMapping(value="/{readerNo}/delete",method=RequestMethod.GET)
	public String delete(@PathVariable String readerNo,HttpServletRequest request) throws UnsupportedEncodingException {
		  try {
			  readerService.deleteReader(readerNo);
	            request.setAttribute("message", "读者删除成功!");
	            return "message";
	        } catch (Exception e) { 
	            e.printStackTrace();
	            request.setAttribute("error", "读者删除失败!");
				return "error";

	        }

	}

	/*ajax方式删除多条读者记录*/
	@RequestMapping(value="/deletes",method=RequestMethod.POST)
	public void delete(String readerNos,HttpServletRequest request,HttpServletResponse response) throws IOException, JSONException {
		String message = "";
    	boolean success = false;
        try { 
        	int count = readerService.deleteReaders(readerNos);
        	success = true;
        	message = count + "条记录删除成功";
        	writeJsonResponse(response, success, message);
        } catch (Exception e) { 
            //e.printStackTrace();
            message = "有记录存在外键约束,删除失败";
            writeJsonResponse(response, success, message);
        }
	}

	/*按照查询条件导出读者信息到Excel*/
	@RequestMapping(value = { "/OutToExcel" }, method = {RequestMethod.GET,RequestMethod.POST})
	public void OutToExcel(String readerNo,@ModelAttribute("readerTypeObj") ReaderType readerTypeObj,String readerName,String birthday,String telephone, Model model, HttpServletRequest request,HttpServletResponse response) throws Exception {
        if(readerNo == null) readerNo = "";
        if(readerName == null) readerName = "";
        if(birthday == null) birthday = "";
        if(telephone == null) telephone = "";
        List<Reader> readerList = readerService.queryReader(readerNo,readerTypeObj,readerName,birthday,telephone);
        ExportExcelUtil ex = new ExportExcelUtil();
        String title = "Reader信息记录"; 
        String[] headers = { "读者编号","读者类型","姓名","性别","读者生日","联系电话","读者头像"};
        List<String[]> dataset = new ArrayList<String[]>(); 
        for(int i=0;i<readerList.size();i++) {
        	Reader reader = readerList.get(i); 
        	dataset.add(new String[]{reader.getReaderNo(),reader.getReaderTypeObj().getReaderTypeName(),reader.getReaderName(),reader.getSex(),reader.getBirthday(),reader.getTelephone(),reader.getPhoto()});
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
			response.setHeader("Content-disposition","attachment; filename="+"Reader.xls");//filename是下载的xls的名，建议最好用英文 
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
