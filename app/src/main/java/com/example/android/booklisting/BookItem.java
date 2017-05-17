package com.example.android.booklisting;

/**
 * {@link BookItem} represents a book which will be shown in the book_list layout.
 * It contains an image, a title, the author's name, the book's page count, a book description as
 * well as a link to the book's preview. */

public class BookItem {

    /** Url for the book's thumbnail */
    private String mSmallThumbnailURL;

    /** Url for the book's preview */
    private String mPreviewURL;

    /** Book Title */
    private String mTitle;

    /** Book Author */
    private String mAuthor;

    /** Book Page Count */
    private int mPageCount;

    /** Book Language */
    private String mLanguage;

    /** Book Description */
    private String mDescription;

    /**
    * Create a new TourItem object for Sightseeing, Shopping
     * @param smallThumbnailURL is the thumbnail which will be shown as the book's image
     * @param previewURL is the link to the book's previewURL page
     * @param title is the book's title
     * @param author is the book's author
     * @param pageCount is the book's number of pages
     * @param language is the book's language
     * @param description is a short descrption of the book
    */
    public BookItem(String smallThumbnailURL, String previewURL, String title, String author, int
            pageCount, String language, String description) {
        mSmallThumbnailURL = smallThumbnailURL;
        mPreviewURL = previewURL;
        mTitle = title;
        mAuthor = author;
        mPageCount = pageCount;
        mLanguage = language;
        mDescription = description;
    }

    public String getmSmallThumbnailURL() {
        return mSmallThumbnailURL;
    }

    public String getmPreviewURL() {
        return mPreviewURL;
    }

    public String getmTitle() {
        return mTitle;
    }

    public String getmAuthor() {
        return mAuthor;
    }

    public String getmPageCount() {
        return String.valueOf(mPageCount);
    }

    public String getmLanguage() {
        return mLanguage;
    }

    public String getmDescription() {
        return mDescription;
    }
}