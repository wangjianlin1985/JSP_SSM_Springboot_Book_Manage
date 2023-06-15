package com.chengxusheji.service;

import java.util.ArrayList;
import javax.annotation.Resource; 
import org.springframework.stereotype.Service;
import com.chengxusheji.po.Book;
import com.chengxusheji.po.Reader;
import com.chengxusheji.po.LoanInfo;

import com.chengxusheji.mapper.LoanInfoMapper;
@Service
public class LoanInfoService {

	@Resource LoanInfoMapper loanInfoMapper;
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

    /*添加借阅信息记录*/
    public void addLoanInfo(LoanInfo loanInfo) throws Exception {
    	loanInfoMapper.addLoanInfo(loanInfo);
    }

    /*按照查询条件分页查询借阅信息记录*/
    public ArrayList<LoanInfo> queryLoanInfo(Book book,Reader reader,String borrowDate,String returnDate,int currentPage) throws Exception { 
     	String where = "where 1=1";
    	if(null != book &&  book.getBarcode() != null  && !book.getBarcode().equals(""))  where += " and t_loanInfo.book='" + book.getBarcode() + "'";
    	if(null != reader &&  reader.getReaderNo() != null  && !reader.getReaderNo().equals(""))  where += " and t_loanInfo.reader='" + reader.getReaderNo() + "'";
    	if(!borrowDate.equals("")) where = where + " and t_loanInfo.borrowDate like '%" + borrowDate + "%'";
    	if(!returnDate.equals("")) where = where + " and t_loanInfo.returnDate like '%" + returnDate + "%'";
    	int startIndex = (currentPage-1) * this.rows;
    	return loanInfoMapper.queryLoanInfo(where, startIndex, this.rows);
    }

    /*按照查询条件查询所有记录*/
    public ArrayList<LoanInfo> queryLoanInfo(Book book,Reader reader,String borrowDate,String returnDate) throws Exception  { 
     	String where = "where 1=1";
    	if(null != book &&  book.getBarcode() != null && !book.getBarcode().equals(""))  where += " and t_loanInfo.book='" + book.getBarcode() + "'";
    	if(null != reader &&  reader.getReaderNo() != null && !reader.getReaderNo().equals(""))  where += " and t_loanInfo.reader='" + reader.getReaderNo() + "'";
    	if(!borrowDate.equals("")) where = where + " and t_loanInfo.borrowDate like '%" + borrowDate + "%'";
    	if(!returnDate.equals("")) where = where + " and t_loanInfo.returnDate like '%" + returnDate + "%'";
    	return loanInfoMapper.queryLoanInfoList(where);
    }

    /*查询所有借阅信息记录*/
    public ArrayList<LoanInfo> queryAllLoanInfo()  throws Exception {
        return loanInfoMapper.queryLoanInfoList("where 1=1");
    }

    /*当前查询条件下计算总的页数和记录数*/
    public void queryTotalPageAndRecordNumber(Book book,Reader reader,String borrowDate,String returnDate) throws Exception {
     	String where = "where 1=1";
    	if(null != book &&  book.getBarcode() != null && !book.getBarcode().equals(""))  where += " and t_loanInfo.book='" + book.getBarcode() + "'";
    	if(null != reader &&  reader.getReaderNo() != null && !reader.getReaderNo().equals(""))  where += " and t_loanInfo.reader='" + reader.getReaderNo() + "'";
    	if(!borrowDate.equals("")) where = where + " and t_loanInfo.borrowDate like '%" + borrowDate + "%'";
    	if(!returnDate.equals("")) where = where + " and t_loanInfo.returnDate like '%" + returnDate + "%'";
        recordNumber = loanInfoMapper.queryLoanInfoCount(where);
        int mod = recordNumber % this.rows;
        totalPage = recordNumber / this.rows;
        if(mod != 0) totalPage++;
    }

    /*根据主键获取借阅信息记录*/
    public LoanInfo getLoanInfo(int loadId) throws Exception  {
        LoanInfo loanInfo = loanInfoMapper.getLoanInfo(loadId);
        return loanInfo;
    }

    /*更新借阅信息记录*/
    public void updateLoanInfo(LoanInfo loanInfo) throws Exception {
        loanInfoMapper.updateLoanInfo(loanInfo);
    }

    /*删除一条借阅信息记录*/
    public void deleteLoanInfo (int loadId) throws Exception {
        loanInfoMapper.deleteLoanInfo(loadId);
    }

    /*删除多条借阅信息信息*/
    public int deleteLoanInfos (String loadIds) throws Exception {
    	String _loadIds[] = loadIds.split(",");
    	for(String _loadId: _loadIds) {
    		loanInfoMapper.deleteLoanInfo(Integer.parseInt(_loadId));
    	}
    	return _loadIds.length;
    }
}
