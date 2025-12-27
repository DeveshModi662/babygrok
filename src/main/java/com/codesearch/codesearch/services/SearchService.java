package com.codesearch.codesearch.services;

import java.io.IOException;
import java.nio.file.Path;
// import java.util.Set;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
// import org.apache.lucene.search.PhraseQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.WildcardQuery;
import org.apache.lucene.store.FSDirectory;

public class SearchService {

    public static void search(String contentQuery, Path indexPath) {
        try {
            DirectoryReader dirReader = DirectoryReader.open(FSDirectory.open(indexPath)) ;
            IndexSearcher searcher = new IndexSearcher(dirReader) ;
            
            Query query = buildQuery(contentQuery) ;

            TopDocs results = searcher.search(query, 4) ;
            for(ScoreDoc sd : results.scoreDocs) {
                // Document doc = searcher.doc(sd.doc, Set.of("filename", "lineNumber", "content")) ;
                Document doc = searcher.storedFields().document(sd.doc) ;
                System.out.println(doc.get("filename") + " : " + doc.get("line") + " -> " + doc.get("content"));
            }

        } catch(ParseException pe) {
        } catch(IOException ioe) {
        }            
    }

    private static Query buildQuery(String input) throws ParseException {

        // Exact phrase: "exactSearch"
        if (input.startsWith("\"") && input.endsWith("\"")) {
            QueryParser parser = new QueryParser("content", new StandardAnalyzer());
            Query q = parser.parse(input);
            
            // String phrase = input.substring(1, input.length() - 1).toLowerCase();
            // PhraseQuery.Builder builder = new PhraseQuery.Builder();

            // builder.add(new Term(phrase)) ;
            // return builder.build();
            return q ;
        }
        else if (input.contains("*")) {        // Wildcard: *wild*card*
            return new WildcardQuery(
                    new Term("content", input.toLowerCase())
            );
        }

        // Default term search
        return new TermQuery(
                new Term("content", input.toLowerCase())
        );
    }

}
