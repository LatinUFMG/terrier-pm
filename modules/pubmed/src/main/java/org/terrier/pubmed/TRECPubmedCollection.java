package org.terrier.indexing;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.terrier.indexing.TRECCollection;
import org.terrier.utility.TagSet;

public class TRECPubmedCollection extends TRECCollection {

   public TRECPubmedCollection(String CollectionSpecFilename, String TagSet, String BlacklistSpecFilename, String ignored) {
      super(CollectionSpecFilename, TagSet, BlacklistSpecFilename, ignored);
   }
   
   public TRECPubmedCollection(List<String> files, String TagSet, String BlacklistSpecFilename, String ignored) {       
      super(files, TagSet, BlacklistSpecFilename, ignored);
   }
   
   public TRECPubmedCollection(String collSpec) {
      super(collSpec);
   }
   
   /**
   * A default constructor that reads the collection specification
   * file, as configured by the property <tt>collection.spec</tt>,
   * reads a list of blacklisted document numbers, specified by the
   * property <tt>trec.blacklist.docids</tt> and opens the
   * first collection file to process. TagSet TagSet.TREC_DOC_TAGS is used to tokenize
   * the collection.
   */
   public TRECPubmedCollection()
   {
      super();
   }
   /**
   * A constructor that reads only the document in the specificed
   * InputStream. Also reads a list of blacklisted document numbers, specified by the
   * property <tt>trec.blacklist.docids</tt> and opens the
   * first collection file to process. */
   public TRECPubmedCollection(InputStream input)
   {
      super(input);
   }

   protected void setTags(String t)
   {
      super.setTags(t);
      TagSet tagSet = new TagSet(t);
      String tmpDocTag = "<" + tagSet.getDocTag();
  
      String tmpEndDocTag = "</" + tagSet.getDocTag()  + ">";
      String tmpDocnoTag = "<" + tagSet.getIdTag();
      String tmpEndDocnoTag = "</" + tagSet.getIdTag()  + ">";
      start_docTag = tmpDocTag.toCharArray();
         start_docTagLength = start_docTag.length;
         start_docnoTag = tmpDocnoTag.toCharArray();
         start_docnoTagLength = start_docnoTag.length;
         end_docTag = tmpEndDocTag;
         end_docTagLength = end_docTag.length();
         end_docnoTag = tmpEndDocnoTag.toCharArray();
         end_docnoTagLength = end_docnoTag.length;

   }


   /**
       * Scans through a document reading in the first occurrence of the specified tag,
       * returning its contents as a StringBuilder object
       * @param taglength - the length of the start tag
       * @param startTag - the start tag
       * @param endTag - the end tag
       * @return - the tag contents
       * @throws IOException
       */
      protected StringBuilder getTag(int taglength, char[] startTag, char[] endTag) throws IOException {
         int readerState = 0;
         int c;
         StringBuilder string = new StringBuilder();
         while (readerState < taglength) {
               if ((c = br.read()) == -1) {
                  if (openNextFile()) {
                     logger.warn("Forced a skip (1: looking for open "+new String(startTag)+" tag) - is the collection corrupt or do the property tags exist?");
                     continue;
                  } else {
                     eoc = true;
                     return null;
                  }
               }
               
               char cc = (char)c;
               char cu = startTag[readerState];
               
               if (!tags_CaseSensitive) {
                  cc= Character.toUpperCase((char)c);
                  cu=Character.toUpperCase((char)cu);
               }
               
               if (cc==cu)
                  readerState++;
               else
                  readerState = 0;
         }
         //look for > that ends the opening tag
   do {
   if ((c = br.read()) == -1) {
                  if (openNextFile()) {
                     logger.warn("Forced a skip (2: looking for end of "+new String(startTag)+" tag) - is the collection corrupt?");
                     continue;
                  } else {
                     eoc = true;
                     return null;
                  }
               }
               
         } while (c != '>');
      

         //looking for end of docno
         readerState = 0;
         while (readerState < (taglength+1)) {
               if ((c = br.read()) == -1) {
                  if (openNextFile()) {
                     logger.warn("Forced a skip (2: looking for end of "+new String(startTag)+" tag) - is the collection corrupt?");
                     continue;
                  } else {
                     eoc = true;
                     return null;
                  }
               }
               
               char cc = (char)c;
               char cu = endTag[readerState];
               
               //System.err.println((char)cc+" "+cu);
               
               if (!tags_CaseSensitive) {
                  cc= Character.toUpperCase((char)c);
                  cu=Character.toUpperCase((char)cu);
               }
               
               if (cc==cu) {
                  readerState++;
               } else {
                  readerState = 0;
                  string.append((char)c);
               }
         }
         return string;
         
      }
   }
