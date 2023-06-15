package com.chengxusheji.mapper;

import java.util.ArrayList;
import org.apache.ibatis.annotations.Param;
import com.chengxusheji.po.LoanInfo;

public interface LoanInfoMapper {
	/*添加借阅信息信息*/
	public void addLoanInfo(LoanInfo loanInfo) throws Exception;

	/*按照查询条件分页查询借阅信息记录*/
	public ArrayList<LoanInfo> queryLoanInfo(@Param("where") String where,@Param("startIndex") int startIndex,@Param("pageSize") int pageSize) throws Exception;

	/*按照查询条件查询所有借阅信息记录*/
	public ArrayList<LoanInfo> queryLoanInfoList(@Param("where") String where) throws Exception;

	/*按照查询条件的借阅信息记录数*/
	public int queryLoanInfoCount(@Param("where") String where) throws Exception; 

	/*根据主键查询某条借阅信息记录*/
	public LoanInfo getLoanInfo(int loadId) throws Exception;

	/*更新借阅信息记录*/
	public void updateLoanInfo(LoanInfo loanInfo) throws Exception;

	/*删除借阅信息记录*/
	public void deleteLoanInfo(int loadId) throws Exception;

}
