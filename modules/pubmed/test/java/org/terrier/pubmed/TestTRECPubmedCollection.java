
package org.terrier.pubmed;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.Writer;

import org.junit.Test;
import org.terrier.tests.ApplicationSetupBasedTest;
import org.terrier.tests.ShakespeareEndToEndTest;
import org.terrier.utility.ApplicationSetup;
import org.terrier.utility.Files;

public class TestTRECPubmedCollection extends ApplicationSetupBasedTest
{
    //based on https://github.com/terrier-org/terrier-core/blob/5.x/modules/tests/src/test/java/org/terrier/indexing/TestTRECCollection.java
    @Test public void testIt() throws Exception { 
String dataFilename = writeTemporaryFile("test.trec", new String[]{
                "<DOC>",
                "<DOCNO uid=”54”>doc1</DOCNO>",
                "test",
                "</DOC>"
            });
        Collection c = new TRECPubmedCollection(Files.openFileStream(dataFilename));

        assertTrue(c.nextDocument());
        Document d = c.getDocument();
        assertNotNull(d);
        assertEquals("doc1", d.getProperty("docno"));
        TestTRECCollection.checkContents(d, "test");
        assertFalse(c.nextDocument());
        c.close();

    }
}
