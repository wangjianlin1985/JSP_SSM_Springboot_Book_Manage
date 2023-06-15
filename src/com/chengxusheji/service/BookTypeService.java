package com.chengxusheji.service;

import java.util.ArrayList;
import javax.annotation.Resource; 
import org.springframework.stereotype.Service;
import com.chengxusheji.po.BookType;

import com.chengxusheji.mapper.BookTypeMapper;
@Service
public class BookTypeService {

	@Resource BookTypeMapper bookTypeMapper;
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

    /*添加图书类型记录*/
    public void addBookType(BookType bookType) throws Exception {
    	bookTypeMapper.addBookType(bookType);
    }

    /*按照查询条件分页查询图书类型记录*/
    public ArrayList<BookType> queryBookType(int currentPage) throws Exception { 
     	String where = "where 1=1";
    	int startIndex = (currentPage-1) * this.rows;
    	return bookTypeMapper.queryBookType(where, startIndex, this.rows);
    }

    /*按照查询条件查询所有记录*/
    public ArrayList<BookType> queryBookType() throws Exception  { 
     	String where = "where 1=1";
    	return bookTypeMapper.queryBookTypeList(where);
    }

    /*查询所有图书类型记录*/
    public ArrayList<BookType> queryAllBookType()  throws Exception {
        return bookTypeMapper.queryBookTypeList("where 1=1");
    }

    /*当前查询条件下计算总的页数和记录数*/
    public void queryTotalPageAndRecordNumber() throws Exception {
     	String where = "where 1=1";
        recordNumber = bookTypeMapper.queryBookTypeCount(where);
        int mod = recordNumber % this.rows;
        totalPage = recordNumber / this.rows;
        if(mod != 0) totalPage++;
    }

    /*根据主键获取图书类型记录*/
    public BookType getBookType(int bookTypeId) throws Exception  {
        BookType bookType = bookTypeMapper.getBookType(bookTypeId);
        return bookType;
    }

    /*更新图书类型记录*/
    public void updateBookType(BookType bookType) throws Exception {
        bookTypeMapper.updateBookType(bookType);
    }

    /*删除一条图书类型记录*/
    public void deleteBookType (int bookTypeId) throws Exception {
        bookTypeMapper.deleteBookType(bookTypeId);
    }

    /*删除多条图书类型信息*/
    public int deleteBookTypes (String bookTypeIds) throws Exception {
    	String _bookTypeIds[] = bookTypeIds.split(",");
    	for(String _bookTypeId: _bookTypeIds) {
    		bookTypeMapper.deleteBookType(Integer.parseInt(_bookTypeId));
    	}
    	return _bookTypeIds.length;
    }
}
