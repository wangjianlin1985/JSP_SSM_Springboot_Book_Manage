package com.chengxusheji.service;

import java.util.ArrayList;
import javax.annotation.Resource; 
import org.springframework.stereotype.Service;
import com.chengxusheji.po.ReaderType;
import com.chengxusheji.po.Reader;

import com.chengxusheji.mapper.ReaderMapper;
@Service
public class ReaderService {

	@Resource ReaderMapper readerMapper;
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

    /*添加读者记录*/
    public void addReader(Reader reader) throws Exception {
    	readerMapper.addReader(reader);
    }

    /*按照查询条件分页查询读者记录*/
    public ArrayList<Reader> queryReader(String readerNo,ReaderType readerTypeObj,String readerName,String birthday,String telephone,int currentPage) throws Exception { 
     	String where = "where 1=1";
    	if(!readerNo.equals("")) where = where + " and t_reader.readerNo like '%" + readerNo + "%'";
    	if(null != readerTypeObj && readerTypeObj.getReaderTypeId()!= null && readerTypeObj.getReaderTypeId()!= 0)  where += " and t_reader.readerTypeObj=" + readerTypeObj.getReaderTypeId();
    	if(!readerName.equals("")) where = where + " and t_reader.readerName like '%" + readerName + "%'";
    	if(!birthday.equals("")) where = where + " and t_reader.birthday like '%" + birthday + "%'";
    	if(!telephone.equals("")) where = where + " and t_reader.telephone like '%" + telephone + "%'";
    	int startIndex = (currentPage-1) * this.rows;
    	return readerMapper.queryReader(where, startIndex, this.rows);
    }

    /*按照查询条件查询所有记录*/
    public ArrayList<Reader> queryReader(String readerNo,ReaderType readerTypeObj,String readerName,String birthday,String telephone) throws Exception  { 
     	String where = "where 1=1";
    	if(!readerNo.equals("")) where = where + " and t_reader.readerNo like '%" + readerNo + "%'";
    	if(null != readerTypeObj && readerTypeObj.getReaderTypeId()!= null && readerTypeObj.getReaderTypeId()!= 0)  where += " and t_reader.readerTypeObj=" + readerTypeObj.getReaderTypeId();
    	if(!readerName.equals("")) where = where + " and t_reader.readerName like '%" + readerName + "%'";
    	if(!birthday.equals("")) where = where + " and t_reader.birthday like '%" + birthday + "%'";
    	if(!telephone.equals("")) where = where + " and t_reader.telephone like '%" + telephone + "%'";
    	return readerMapper.queryReaderList(where);
    }

    /*查询所有读者记录*/
    public ArrayList<Reader> queryAllReader()  throws Exception {
        return readerMapper.queryReaderList("where 1=1");
    }

    /*当前查询条件下计算总的页数和记录数*/
    public void queryTotalPageAndRecordNumber(String readerNo,ReaderType readerTypeObj,String readerName,String birthday,String telephone) throws Exception {
     	String where = "where 1=1";
    	if(!readerNo.equals("")) where = where + " and t_reader.readerNo like '%" + readerNo + "%'";
    	if(null != readerTypeObj && readerTypeObj.getReaderTypeId()!= null && readerTypeObj.getReaderTypeId()!= 0)  where += " and t_reader.readerTypeObj=" + readerTypeObj.getReaderTypeId();
    	if(!readerName.equals("")) where = where + " and t_reader.readerName like '%" + readerName + "%'";
    	if(!birthday.equals("")) where = where + " and t_reader.birthday like '%" + birthday + "%'";
    	if(!telephone.equals("")) where = where + " and t_reader.telephone like '%" + telephone + "%'";
        recordNumber = readerMapper.queryReaderCount(where);
        int mod = recordNumber % this.rows;
        totalPage = recordNumber / this.rows;
        if(mod != 0) totalPage++;
    }

    /*根据主键获取读者记录*/
    public Reader getReader(String readerNo) throws Exception  {
        Reader reader = readerMapper.getReader(readerNo);
        return reader;
    }

    /*更新读者记录*/
    public void updateReader(Reader reader) throws Exception {
        readerMapper.updateReader(reader);
    }

    /*删除一条读者记录*/
    public void deleteReader (String readerNo) throws Exception {
        readerMapper.deleteReader(readerNo);
    }

    /*删除多条读者信息*/
    public int deleteReaders (String readerNos) throws Exception {
    	String _readerNos[] = readerNos.split(",");
    	for(String _readerNo: _readerNos) {
    		readerMapper.deleteReader(_readerNo);
    	}
    	return _readerNos.length;
    }
}
