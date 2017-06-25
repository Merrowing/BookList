package com.testwork.BotsCrew.BookList;

import javax.persistence.*;

@Entity
@Table(name = "book")
public class Book implements Comparable<Book> {
	
	private long id;
	private String author;
	private String title;

	public Book() {

	}

	public Book(String author, String title) {
		this.author = author;
		this.title = title;
	}
	 public void print(){
		 System.out.print(author + " " + title );
	 }
	 
	 public int compareTo(Book o){
		 return title.compareTo(o.title);

	 }

	@Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

}