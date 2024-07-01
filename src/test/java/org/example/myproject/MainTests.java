package org.example.myproject;

import com.renomad.minum.state.Context;
import com.renomad.minum.htmlparsing.TagName;
import com.renomad.minum.web.FullSystem;
import com.renomad.minum.web.FunctionalTesting;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Map;

import static com.renomad.minum.testing.TestFramework.assertEquals;
import static com.renomad.minum.testing.TestFramework.buildTestingContext;
import static com.renomad.minum.web.StatusLine.StatusCode.CODE_200_OK;

public class MainTests {

    private static Context context;
    private static FunctionalTesting ft;

    @BeforeClass
    public static void init() {
        context = buildTestingContext("_integration_test");
        FullSystem fullSystem = new FullSystem(context).start();
        new Endpoints(fullSystem).registerEndpoints();
        ft = new FunctionalTesting(context, "localhost", 8080);
    }

    @AfterClass
    public static void cleanup() {
        var fs = context.getFullSystem();
        fs.shutdown();
        context.getLogger().stop();
        context.getExecutorService().shutdownNow();
    }


    /**
     * A user should see "hi there world" when running
     * the program.
     */
    @Test
    public void testHomepage() {
        // send a GET request to the server
        var testResponse = ft.get("");

        // check that we got a 200 OK status in response
        assertEquals(testResponse.statusLine().status(), CODE_200_OK);

        // Confirm that the response body, parsed as HTML, yields a paragraph with the expected content
        assertEquals(testResponse.searchOne(TagName.P, Map.of()).innerText(), "Hi there world!");
    }


    /**
     * Test the book entry functionality.
     * <p>
     * A general vision is laid out for functionality.  This test should work for
     * situations whether there are any books listed, or none, just to kick us off.
     * </p>
     * <pre>
     * Enter a Book:
     * -------------
     * Title:   [ Dandelion Wine     ]
     * Author:  [ Ray Bradbury       ]
     * Year:    [ 1999 ]
     * Notes:   [ paperback version  ]
     *
     * [Enter]
     *
     * Current Books:
     * --------------
     * Extreme Programming Explained, by Kent Beck, published 2000
     * The Art of Software Testing, by Glenford J. Myers, published 1979
     * </pre>
     */
    @Test
    public void testBookEntry() {
        // send a GET request for the "book" endpoint
        var response = ft.get("book");

        // check that we got a 200 OK status in response
        assertEquals(response.statusLine().status(), CODE_200_OK);

        // Confirm that we find the expected input fields
        assertEquals(response.searchOne(TagName.H1, Map.of("id", "book_entry_header")).innerText(), "Enter a Book:");
        assertEquals(response.searchOne(TagName.LABEL, Map.of("for","title_input")).innerText(), "Title:");
        assertEquals(response.searchOne(TagName.LABEL, Map.of("for","author_input")).innerText(), "Author:");
        assertEquals(response.searchOne(TagName.LABEL, Map.of("for","year_input")).innerText(), "Year:");
        assertEquals(response.searchOne(TagName.LABEL, Map.of("for","notes_input")).innerText(), "Notes:");

        // confirm we find the expected output table
        assertEquals(response.searchOne(TagName.H1, Map.of("id","book_list_header")).innerText(), "Current Books:");
    }

}
