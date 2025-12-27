package com.codesearch.codesearch.index;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.TextField;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import com.codesearch.codesearch.records.IndexedLine;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public class CodeIndexer {

    // private final Path indexPath;

    // public CodeIndexer(Path indexPath) {
    //     this.indexPath = indexPath;
    // }

    public static void createIndex(Path indexPath, List<IndexedLine> lines) {
        try {
            IndexWriter indexWriter = createWriter(indexPath) ;
            for(IndexedLine line : lines) {
                Document doc = new Document() ;

                doc.add(new TextField("content", line.content(), Field.Store.YES)) ;
                doc.add(new StringField("filename", line.filename(), Field.Store.YES)) ;
                doc.add(new StoredField("line", line.lineNumber())) ;
                indexWriter.addDocument(doc) ;
            }
        } catch(IOException ioe) {
            System.out.println("CodeIndexer : createIndex : ");
        }
    }

    private static IndexWriter createWriter(Path indexPath) throws IOException {
        IndexWriterConfig config = new IndexWriterConfig(new StandardAnalyzer());
        config.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);

        Directory directory = FSDirectory.open(indexPath);

        return new IndexWriter(directory, config);
    }
}