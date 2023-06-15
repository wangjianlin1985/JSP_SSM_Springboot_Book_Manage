package com.chengxusheji.po;

import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.NotEmpty;
import org.json.JSONException;
import org.json.JSONObject;

public class LoanInfo {
    /*借阅编号*/
    private Integer loadId;
    public Integer getLoadId(){
        return loadId;
    }
    public void setLoadId(Integer loadId){
        this.loadId = loadId;
    }

    /*图书对象*/
    private Book book;
    public Book getBook() {
        return book;
    }
    public void setBook(Book book) {
        this.book = book;
    }

    /*读者对象*/
    private Reader reader;
    public Reader getReader() {
        return reader;
    }
    public void setReader(Reader reader) {
        this.reader = reader;
    }

    /*借阅时间*/
    private String borrowDate;
    public String getBorrowDate() {
        return borrowDate;
    }
    public void setBorrowDate(String borrowDate) {
        this.borrowDate = borrowDate;
    }

    /*归还时间*/
    private String returnDate;
    public String getReturnDate() {
        return returnDate;
    }
    public void setReturnDate(String returnDate) {
        this.returnDate = returnDate;
    }

    public JSONObject getJsonObject() throws JSONException {
    	JSONObject jsonLoanInfo=new JSONObject(); 
		jsonLoanInfo.accumulate("loadId", this.getLoadId());
		jsonLoanInfo.accumulate("book", this.getBook().getBookName());
		jsonLoanInfo.accumulate("bookPri", this.getBook().getBarcode());
		jsonLoanInfo.accumulate("reader", this.getReader().getReaderName());
		jsonLoanInfo.accumulate("readerPri", this.getReader().getReaderNo());
		jsonLoanInfo.accumulate("borrowDate", this.getBorrowDate().length()>19?this.getBorrowDate().substring(0,19):this.getBorrowDate());
		jsonLoanInfo.accumulate("returnDate", this.getReturnDate().length()>19?this.getReturnDate().substring(0,19):this.getReturnDate());
		return jsonLoanInfo;
    }}