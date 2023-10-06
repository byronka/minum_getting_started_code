package org.example.myproject;

import com.renomad.minum.database.DbData;

import java.util.Objects;

import static com.renomad.minum.utils.SerializationUtils.deserializeHelper;
import static com.renomad.minum.utils.SerializationUtils.serializeHelper;

public class BookDbData extends DbData<BookDbData> {

    private long index;
    private final String title;
    private final String author;
    private final int year;
    private final String notes;

    public BookDbData(long index, String title, String author, int year, String notes) {
        this.index = index;
        this.title = title;
        this.author = author;
        this.year = year;
        this.notes = notes;
    }

    public static BookDbData EMPTY = new BookDbData(0, "", "", 0, "");

    @Override
    protected String serialize() {
        return serializeHelper(index, title, author, year, notes);
    }

    @Override
    protected BookDbData deserialize(String serializedText) {
        final var tokens = deserializeHelper(serializedText);

        return new BookDbData(
                Long.parseLong(tokens.get(0)),
                tokens.get(1),
                tokens.get(2),
                Integer.parseInt(tokens.get(3)),
                tokens.get(4));
    }

    @Override
    protected long getIndex() {
        return index;
    }

    @Override
    protected void setIndex(long index) {
        this.index = index;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public int getYear() {
        return year;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookDbData that = (BookDbData) o;
        return index == that.index && year == that.year && Objects.equals(title, that.title) && Objects.equals(author, that.author) && Objects.equals(notes, that.notes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(index, title, author, year, notes);
    }
}
