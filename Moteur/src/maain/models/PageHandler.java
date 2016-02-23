package maain.models;

import edu.jhu.nlp.wikipedia.WikiPage;


public interface PageHandler {

   /**
    * Places the page in the queue
    * @param page
    * @throws Exception
    */
   void queueLink(WikiPage page) throws Exception;

   /**
    * Returns the number of visited pages
    * @return
    */
   int size();

   /**
    * Checks if the page was already visited
    * @param page
    * @return
    */
   boolean visited(WikiPage page);

   /**
    * Marks this page as visited
    * @param page
    */
   void addVisited(WikiPage page);
}