package com.chengxusheji.service;

import java.util.ArrayList;
import javax.annotation.Resource; 
import org.springframework.stereotype.Service;
import com.chengxusheji.po.ReaderType;

import com.chengxusheji.mapper.ReaderTypeMapper;
@Service
public class ReaderTypeService {

	@Resource ReaderTypeMapper readerTypeMapper;
    /*每页显示记录数目*/
    private int rows = 10;;
    public int getRows() {
		return rows;
	}
	public void setRows(int rows) {
		this.rows = rows;
	}

    /*保存查询后总的页数*/
    private int totalPage;
    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }
    public int getTotalPage() {
        return totalPage;
    }

    /*保存查询到的总记录数*/
    private int recordNumber;
    public void setRecordNumber(int recordNumber) {
        this.recordNumber = recordNumber;
    }
    public int getRecordNumber() {
        return recordNumber;
    }

    /*添加读者类型记录*/
    public void addReaderType(ReaderType readerType) throws Exception {
    	readerTypeMapper.addReaderType(readerType);
    }

    /*按照查询条件分页查询读者类型记录*/
    public ArrayList<ReaderType> queryReaderType(int currentPage) throws Exception { 
     	String where = "where 1=1";
    	int startIndex = (currentPage-1) * this.rows;
    	return readerTypeMapper.queryReaderType(where, startIndex, this.rows);
    }

    /*按照查询条件查询所有记录*/
    public ArrayList<ReaderType> queryReaderType() throws Exception  { 
     	String where = "where 1=1";
    	return readerTypeMapper.queryReaderTypeList(where);
    }

    /*查询所有读者类型记录*/
    public ArrayList<ReaderType> queryAllReaderType()  throws Exception {
        return readerTypeMapper.queryReaderTypeList("where 1=1");
    }

    /*当前查询条件下计算总的页数和记录数*/
    public void queryTotalPageAndRecordNumber() throws Exception {
     	String where = "where 1=1";
        recordNumber = readerTypeMapper.queryReaderTypeCount(where);
        int mod = recordNumber % this.rows;
        totalPage = recordNumber / this.rows;
        if(mod != 0) totalPage++;
    }

    /*根据主键获取读者类型记录*/
    public ReaderType getReaderType(int readerTypeId) throws Exception  {
        ReaderType readerType = readerTypeMapper.getReaderType(readerTypeId);
        return readerType;
    }

    /*更新读者类型记录*/
    public void updateReaderType(ReaderType readerType) throws Exception {
        readerTypeMapper.updateReaderType(readerType);
    }

    /*删除一条读者类型记录*/
    public void deleteReaderType (int readerTypeId) throws Exception {
        readerTypeMapper.deleteReaderType(readerTypeId);
    }

    /*删除多条读者类型信息*/
    public int deleteReaderTypes (String readerTypeIds) throws Exception {
    	String _readerTypeIds[] = readerTypeIds.split(",");
    	for(String _readerTypeId: _readerTypeIds) {
    		readerTypeMapper.deleteReaderType(Integer.parseInt(_readerTypeId));
    	}
    	return _readerTypeIds.length;
    }
}
