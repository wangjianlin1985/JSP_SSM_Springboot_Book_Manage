package com.chengxusheji.mapper;

import java.util.ArrayList;
import org.apache.ibatis.annotations.Param;
import com.chengxusheji.po.Reader;

public interface ReaderMapper {
	/*添加读者信息*/
	public void addReader(Reader reader) throws Exception;

	/*按照查询条件分页查询读者记录*/
	public ArrayList<Reader> queryReader(@Param("where") String where,@Param("startIndex") int startIndex,@Param("pageSize") int pageSize) throws Exception;

	/*按照查询条件查询所有读者记录*/
	public ArrayList<Reader> queryReaderList(@Param("where") String where) throws Exception;

	/*按照查询条件的读者记录数*/
	public int queryReaderCount(@Param("where") String where) throws Exception; 

	/*根据主键查询某条读者记录*/
	public Reader getReader(String readerNo) throws Exception;

	/*更新读者记录*/
	public void updateReader(Reader reader) throws Exception;

	/*删除读者记录*/
	public void deleteReader(String readerNo) throws Exception;

}
