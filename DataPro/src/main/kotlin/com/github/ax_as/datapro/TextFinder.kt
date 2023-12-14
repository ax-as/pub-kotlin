//package com.github.ax_as.datapro
//
//import org.apache.lucene.analysis.standard.StandardAnalyzer
//import org.apache.lucene.document.Document
//import org.apache.lucene.document.Field
//import org.apache.lucene.document.TextField
//import org.apache.lucene.index.DirectoryReader
//import org.apache.lucene.index.IndexWriter
//import org.apache.lucene.index.IndexWriterConfig
//import org.apache.lucene.queryparser.classic.QueryParser
//import org.apache.lucene.search.IndexSearcher
//import org.apache.lucene.store.Directory
//import org.apache.lucene.store.FSDirectory
//import java.nio.file.Path
//
//class TextFinder(val directoryPath: Path) {
//    private var reader: DirectoryReader? = null
//
//
//    fun initConfig(text: String) {
//        val directory = FSDirectory.open(directoryPath)
//        val indexed = DirectoryReader.indexExists(directory)
//        if (!indexed) {
//            buildIndex(directory, text)
//        }
//    }
//
//    private fun buildIndex(directory: Directory, text: String) {
//        val analyzer = StandardAnalyzer()
//        val config = IndexWriterConfig(analyzer)
//        val writer = IndexWriter(directory, config)
//
//        val paragraph = text.split(" ")
//        for (p in paragraph) {
//            val doc = Document()
//            doc.add(TextField("content", p, Field.Store.YES))
//            writer.addDocument(doc)
//        }
//        writer.close()
//    }
//
//
//    fun search(query: String) {
//        val directory = FSDirectory.open(directoryPath)
//        reader = DirectoryReader.open(directory)
//        val searcher = IndexSearcher(reader)
//        val queryParser =
//            QueryParser("content", StandardAnalyzer()).parse(query)
//        val docs = searcher.search(queryParser, 10)
//        for (doc in docs.scoreDocs) {
//            var document = searcher.doc(doc.doc)
//            val content = document.get("content")
//
//            println("resultado encontrado ${doc.score} $content")
//        }
//
//        reader?.close()
//    }
//
//}