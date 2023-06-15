package com.chengxusheji.mapper;

import java.util.ArrayList;
import org.apache.ibatis.annotations.Param;
import com.chengxusheji.po.ReaderType;

public interface ReaderTypeMapper {
	/*添加读者类型信息*/
	public void addReaderType(ReaderType readerType) throws Exception;

	/*按照查询条件分页查询读者类型记录*/
	public ArrayList<ReaderType> queryReaderType(@Param("where") String where,@Param("startIndex") int startIndex,@Param("pageSize") int pageSize) throws Exception;

	/*按照查询条件查询所有读者类型记录*/
	public ArrayList<ReaderType> queryReaderTypeList(@Param("where") String where) throws Exception;

	/*按照查询条件的读者类型记录数*/
	public int queryReaderTypeCount(@Param("where") String where) throws Exception; 

	/*根据主键查询某条读者类型记录*/
	public ReaderType getReaderType(int readerTypeId) throws Exception;

	/*更新读者类型记录*/
	public void updateReaderType(ReaderType readerType) throws Exception;

	/*删除读者类型记录*/
	public void deleteReaderType(int readerTypeId) throws Exception;

}
