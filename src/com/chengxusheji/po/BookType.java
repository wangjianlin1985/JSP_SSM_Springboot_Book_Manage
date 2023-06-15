package com.chengxusheji.po;

import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.NotEmpty;
import org.json.JSONException;
import org.json.JSONObject;

public class BookType {
    /*图书类别*/
    private Integer bookTypeId;
    public Integer getBookTypeId(){
        return bookTypeId;
    }
    public void setBookTypeId(Integer bookTypeId){
        this.bookTypeId = bookTypeId;
    }

    /*类别名称*/
    @NotEmpty(message="类别名称不能为空")
    private String bookTypeName;
    public String getBookTypeName() {
        return bookTypeName;
    }
    public void setBookTypeName(String bookTypeName) {
        this.bookTypeName = bookTypeName;
    }

    /*可借阅天数*/
    @NotNull(message="必须输入可借阅天数")
    private Integer days;
    public Integer getDays() {
        return days;
    }
    public void setDays(Integer days) {
        this.days = days;
    }

    public JSONObject getJsonObject() throws JSONException {
    	JSONObject jsonBookType=new JSONObject(); 
		jsonBookType.accumulate("bookTypeId", this.getBookTypeId());
		jsonBookType.accumulate("bookTypeName", this.getBookTypeName());
		jsonBookType.accumulate("days", this.getDays());
		return jsonBookType;
    }}