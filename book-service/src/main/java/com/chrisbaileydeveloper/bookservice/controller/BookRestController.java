package com.chrisbaileydeveloper.bookservice.controller;

import com.chrisbaileydeveloper.bookservice.dto.BookRequest;
import com.chrisbaileydeveloper.bookservice.dto.BookResponse;
import com.chrisbaileydeveloper.bookservice.service.BookImageStorageService;
import com.chrisbaileydeveloper.bookservice.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookRestController {

    private final BookService bookService;
    private final BookImageStorageService imageStorageService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public BookResponse createBook(
            @RequestPart("book") BookRequest bookRequest,
            @RequestPart(value = "image", required = false) MultipartFile image
    ) {
        // 1. Upload to S3
        String imageUrl = imageStorageService.uploadBookImage(image);

        // 2. Set imageUrl on request
        bookRequest.setImageUrl(imageUrl);

        // 3. Reuse existing service logic
       return bookService.createBook(bookRequest);
    }
}