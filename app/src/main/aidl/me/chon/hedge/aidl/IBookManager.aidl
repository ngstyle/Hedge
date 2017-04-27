// IBookManager.aidl
package me.chon.hedge.aidl;

// Declare any non-default types here with import statements
import me.chon.hedge.aidl.Book;

interface IBookManager {

    List<Book> getBookList();
    void addBook(in Book book);
}
