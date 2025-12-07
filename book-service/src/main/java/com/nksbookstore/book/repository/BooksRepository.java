package com.nksbookstore.book.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nksbookstore.book.entity.Book;

@Repository
public interface BooksRepository extends JpaRepository<Book, Long> {

    
}