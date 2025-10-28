package org.example.myproject;

import com.renomad.minum.web.FullSystem;
import com.renomad.minum.web.Response;
import com.renomad.minum.web.WebFramework;

import static com.renomad.minum.web.RequestLine.Method.GET;
import static com.renomad.minum.web.RequestLine.Method.POST;

public class Endpoints {

    private final WebFramework webFramework;
    private final Book book;

    public Endpoints(FullSystem fullSystem) {
        try {
            this.webFramework = fullSystem.getWebFramework();
            this.book = new Book(fullSystem.getContext().getDb2("books", BookDbData.EMPTY));
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
    
    public void registerEndpoints() {
        webFramework.registerPath(GET, "", request -> Response.htmlOk("<p>Hi there world!</p>"));
        webFramework.registerPath(GET, "book", book::getBookPage);
        webFramework.registerPath(POST, "book", book::postBook);
    }
}
