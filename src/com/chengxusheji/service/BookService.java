package com.chengxusheji.service;

import java.util.ArrayList;
import javax.annotation.Resource; 
import org.springframework.stereotype.Service;
import com.chengxusheji.po.BookType;
import com.chengxusheji.po.Book;

import com.chengxusheji.mapper.BookMapper;
@Service
public class BookService {

	@Resource BookMapper bookMapper;
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

    /*添加图书记录*/
    public void addBook(Book book) throws Exception {
    	bookMapper.addBook(book);
    }

    /*按照查询条件分页查询图书记录*/
    public ArrayList<Book> queryBook(String barcode,String bookName,BookType bookTypeObj,String publishDate,int currentPage) throws Exception { 
     	String where = "where 1=1";
    	if(!barcode.equals("")) where = where + " and t_book.barcode like '%" + barcode + "%'";
    	if(!bookName.equals("")) where = where + " and t_book.bookName like '%" + bookName + "%'";
    	if(null != bookTypeObj && bookTypeObj.getBookTypeId()!= null && bookTypeObj.getBookTypeId()!= 0)  where += " and t_book.bookTypeObj=" + bookTypeObj.getBookTypeId();
    	if(!publishDate.equals("")) where = where + " and t_book.publishDate like '%" + publishDate + "%'";
    	int startIndex = (currentPage-1) * this.rows;
    	return bookMapper.queryBook(where, startIndex, this.rows);
    }

    /*按照查询条件查询所有记录*/
    public ArrayList<Book> queryBook(String barcode,String bookName,BookType bookTypeObj,String publishDate) throws Exception  { 
     	String where = "where 1=1";
    	if(!barcode.equals("")) where = where + " and t_book.barcode like '%" + barcode + "%'";
    	if(!bookName.equals("")) where = where + " and t_book.bookName like '%" + bookName + "%'";
    	if(null != bookTypeObj && bookTypeObj.getBookTypeId()!= null && bookTypeObj.getBookTypeId()!= 0)  where += " and t_book.bookTypeObj=" + bookTypeObj.getBookTypeId();
    	if(!publishDate.equals("")) where = where + " and t_book.publishDate like '%" + publishDate + "%'";
    	return bookMapper.queryBookList(where);
    }

    /*查询所有图书记录*/
    public ArrayList<Book> queryAllBook()  throws Exception {
        return bookMapper.queryBookList("where 1=1");
    }

    /*当前查询条件下计算总的页数和记录数*/
    public void queryTotalPageAndRecordNumber(String barcode,String bookName,BookType bookTypeObj,String publishDate) throws Exception {
     	String where = "where 1=1";
    	if(!barcode.equals("")) where = where + " and t_book.barcode like '%" + barcode + "%'";
    	if(!bookName.equals("")) where = where + " and t_book.bookName like '%" + bookName + "%'";
    	if(null != bookTypeObj && bookTypeObj.getBookTypeId()!= null && bookTypeObj.getBookTypeId()!= 0)  where += " and t_book.bookTypeObj=" + bookTypeObj.getBookTypeId();
    	if(!publishDate.equals("")) where = where + " and t_book.publishDate like '%" + publishDate + "%'";
        recordNumber = bookMapper.queryBookCount(where);
        int mod = recordNumber % this.rows;
        totalPage = recordNumber / this.rows;
        if(mod != 0) totalPage++;
    }

    /*根据主键获取图书记录*/
    public Book getBook(String barcode) throws Exception  {
        Book book = bookMapper.getBook(barcode);
        return book;
    }

    /*更新图书记录*/
    public void updateBook(Book book) throws Exception {
        bookMapper.updateBook(book);
    }

    /*删除一条图书记录*/
    public void deleteBook (String barcode) throws Exception {
        bookMapper.deleteBook(barcode);
    }

    /*删除多条图书信息*/
    public int deleteBooks (String barcodes) throws Exception {
    	String _barcodes[] = barcodes.split(",");
    	for(String _barcode: _barcodes) {
    		bookMapper.deleteBook(_barcode);
    	}
    	return _barcodes.length;
    }
}
