package com.codesearch.codesearch.records;

public record IndexedLine(
    String filename,
    int lineNumber,
    String content
)  { }
