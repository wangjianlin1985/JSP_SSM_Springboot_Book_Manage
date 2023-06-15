package com.chengxusheji.mapper;

import java.util.ArrayList;
import org.apache.ibatis.annotations.Param;
import com.chengxusheji.po.Book;

public interface BookMapper {
	/*添加图书信息*/
	public void addBook(Book book) throws Exception;

	/*按照查询条件分页查询图书记录*/
	public ArrayList<Book> queryBook(@Param("where") String where,@Param("startIndex") int startIndex,@Param("pageSize") int pageSize) throws Exception;

	/*按照查询条件查询所有图书记录*/
	public ArrayList<Book> queryBookList(@Param("where") String where) throws Exception;

	/*按照查询条件的图书记录数*/
	public int queryBookCount(@Param("where") String where) throws Exception; 

	/*根据主键查询某条图书记录*/
	public Book getBook(String barcode) throws Exception;

	/*更新图书记录*/
	public void updateBook(Book book) throws Exception;

	/*删除图书记录*/
	public void deleteBook(String barcode) throws Exception;

}
