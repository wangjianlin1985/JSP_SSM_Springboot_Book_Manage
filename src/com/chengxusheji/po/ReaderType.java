package com.chengxusheji.po;

import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.NotEmpty;
import org.json.JSONException;
import org.json.JSONObject;

public class ReaderType {
    /*读者类型编号*/
    private Integer readerTypeId;
    public Integer getReaderTypeId(){
        return readerTypeId;
    }
    public void setReaderTypeId(Integer readerTypeId){
        this.readerTypeId = readerTypeId;
    }

    /*读者类型*/
    @NotEmpty(message="读者类型不能为空")
    private String readerTypeName;
    public String getReaderTypeName() {
        return readerTypeName;
    }
    public void setReaderTypeName(String readerTypeName) {
        this.readerTypeName = readerTypeName;
    }

    /*可借阅数目*/
    @NotNull(message="必须输入可借阅数目")
    private Integer number;
    public Integer getNumber() {
        return number;
    }
    public void setNumber(Integer number) {
        this.number = number;
    }

    public JSONObject getJsonObject() throws JSONException {
    	JSONObject jsonReaderType=new JSONObject(); 
		jsonReaderType.accumulate("readerTypeId", this.getReaderTypeId());
		jsonReaderType.accumulate("readerTypeName", this.getReaderTypeName());
		jsonReaderType.accumulate("number", this.getNumber());
		return jsonReaderType;
    }}