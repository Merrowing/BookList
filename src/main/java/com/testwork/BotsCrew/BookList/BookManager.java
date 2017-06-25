package com.testwork.BotsCrew.BookList;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.boot.MetadataSources;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

public class BookManager {

	private static SessionFactory sessionFactory;

	protected void setup() {
		final StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure().build();
		try {
			sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
		} catch (Exception ex) {
			StandardServiceRegistryBuilder.destroy(registry);
		}
	}

	protected void exit() {
		sessionFactory.close();
	}

	public static void main(String[] args) throws IOException {
		BookManager manager = new BookManager();
		manager.setup();
		String consoleInput = "";
		InputStreamReader converter = new InputStreamReader(System.in);
		BufferedReader in = new BufferedReader(converter);
		String title = "";
		String author = "";
		consoleInput = in.readLine();
		String[] inLine = consoleInput.trim().split(" ");
		try {
			while (!(inLine[0].equals("exit"))) {
				if (inLine[0].equals("add")) {
					author = inLine[1];
					title = inLine[2];
					manager.createBook(author, title);
					System.out.println("book" + " " + author + " " + title + "was added");
				} else if (inLine[0].equals("remove")) {
					title = inLine[1];
					manager.removeBook(title);
					System.out.println("remove" + " " + title);
				} else if (inLine[0].equals("edit")) {
					String newTitle = in.readLine();
					manager.editBook(title, newTitle);
					System.out.println("book" + " " + title + "was edited");
				} else if (inLine[0].equals("all") && inLine[1].equals("books")) {
					System.out.println("Our books:");
					manager.getBooks();
				}
				consoleInput = in.readLine();
				inLine = consoleInput.trim().split(" ");
			}
		} catch (Exception e) {
		}
		manager.exit();
	}

	public void createBook(String author, String title) {
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		Book book = new Book(author, title);
		session.save(book);
		session.getTransaction().commit();
		session.close();

	}

	public void removeBook(String title) throws IOException {
		Session session = sessionFactory.openSession();
		CriteriaBuilder builder = session.getCriteriaBuilder();
		CriteriaQuery<Book> criteria = builder.createQuery(Book.class);
		Root<Book> bookRoot = criteria.from(Book.class);
		criteria.select(bookRoot);

		criteria.where(builder.equal(bookRoot.get("title"), title));
		List<Book> bookList = session.createQuery(criteria).getResultList();
		if (bookList.size() > 1) {
			for (int i = 0; i < bookList.size(); i++) {
				Book book = new Book();
				book = bookList.get(i);
				System.out.print(book.getId() + "." + " ");
				book.print();
				System.out.println();
			}
			String consoleInput = "";
			InputStreamReader converter = new InputStreamReader(System.in);
			BufferedReader in = new BufferedReader(converter);
			consoleInput = in.readLine();
			long id = Integer.parseInt(consoleInput);
			for (int i = 0; i < bookList.size(); i++) {
				Book book = new Book();
				book = bookList.get(i);
				if (book.getId() == id) {
					session.delete(book);
				}
			}
		} else {
			Book book = new Book();
			book = bookList.get(0);
			session.delete(book);
		}
		session.beginTransaction();
		session.getTransaction().commit();
		session.close();
	}

	public void editBook(String title, String newTitle) {
		Session session = sessionFactory.openSession();
		CriteriaBuilder builder = session.getCriteriaBuilder();
		CriteriaQuery<Book> criteria = builder.createQuery(Book.class);
		Root<Book> bookRoot = criteria.from(Book.class);
		criteria.select(bookRoot);

		criteria.where(builder.equal(bookRoot.get("title"), title));
		List<Book> bookList = session.createQuery(criteria).getResultList();
		Book book = new Book();
		book = bookList.get(0);
		book.setTitle(newTitle);
		session.beginTransaction();
		session.update(book);

		session.getTransaction().commit();
		session.close();
	}

	public List<Book> getBooks() {
		List<Book> BookList = new ArrayList<Book>();
		Session session = sessionFactory.openSession();
		for (Object oneObject : session.createQuery("FROM Book").getResultList()) {
			BookList.add((Book) oneObject);
		}
		session.close();
		Collections.sort(BookList);
		for (int i = 0; i < BookList.size(); i++) {
			BookList.get(i).print();
			System.out.println();
		}
		return BookList;
	}

}
