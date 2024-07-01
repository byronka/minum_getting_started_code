package org.example.myproject;

import com.renomad.minum.database.Db;
import com.renomad.minum.templating.TemplateProcessor;
import com.renomad.minum.web.Request;
import com.renomad.minum.web.Response;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

import static com.renomad.minum.utils.StringUtils.safeHtml;

public class Book {

    private final TemplateProcessor bookTemplate;
    private final Db<BookDbData> bookDb;

    public Book(Db<BookDbData> bookDb) throws IOException {
        String myTemplate = Files.readString(Path.of("src/main/webapp/templates/bookentry.html"));
        this.bookTemplate = TemplateProcessor.buildProcessor(myTemplate);
        this.bookDb = bookDb;
    }

    /**
     * Returns the web page for creating book entries and viewing the existing books
     */
    public Response getBookPage(Request request) {
        Collection<BookDbData> books = bookDb.values();
        String renderedBooks;
        if (books.isEmpty()) {
            renderedBooks = "<li>(No books)</li>";
        } else {
            renderedBooks = books.stream()
                    .map(x -> String.format("<li>%s, by %s, published %d</li>",
                            safeHtml(x.getTitle()),
                            safeHtml(x.getAuthor()),
                            x.getYear()))
                    .collect(Collectors.joining("\n"));
        }
        return Response.htmlOk(bookTemplate.renderTemplate(Map.of("books", renderedBooks)));
    }

    /**
     * This receives a POST request with data about books
     */
    public Response postBook(Request request) {
        // get data from the user.  No validation, blindly trust the input
        // certainly something to improve
        String title = request.body().asString("title_input");
        String author = request.body().asString("author_input");
        int year = Integer.parseInt(request.body().asString("year_input"));
        String notes = request.body().asString("notes_input");

        // store the data in the database
        BookDbData bookDbData = new BookDbData(0, title, author, year, notes);
        bookDb.write(bookDbData);

        // redirect back to GET book
        return Response.redirectTo("/book");
    }
}
