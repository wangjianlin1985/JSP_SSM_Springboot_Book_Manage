package com.chengxusheji.mapper;

import java.util.ArrayList;
import org.apache.ibatis.annotations.Param;
import com.chengxusheji.po.BookType;

public interface BookTypeMapper {
	/*添加图书类型信息*/
	public void addBookType(BookType bookType) throws Exception;

	/*按照查询条件分页查询图书类型记录*/
	public ArrayList<BookType> queryBookType(@Param("where") String where,@Param("startIndex") int startIndex,@Param("pageSize") int pageSize) throws Exception;

	/*按照查询条件查询所有图书类型记录*/
	public ArrayList<BookType> queryBookTypeList(@Param("where") String where) throws Exception;

	/*按照查询条件的图书类型记录数*/
	public int queryBookTypeCount(@Param("where") String where) throws Exception; 

	/*根据主键查询某条图书类型记录*/
	public BookType getBookType(int bookTypeId) throws Exception;

	/*更新图书类型记录*/
	public void updateBookType(BookType bookType) throws Exception;

	/*删除图书类型记录*/
	public void deleteBookType(int bookTypeId) throws Exception;

}
