package com.Ajie8021.plagiarism.preprocess;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DefaultTextPreprocessorTest {
    private final DefaultTextPreprocessor preprocessor = new DefaultTextPreprocessor();

    @Test void normalTextShouldBePreserved() {
        assertEquals("hello world", preprocessor.preprocess("hello world"));
    }
    @Test void htmlTagsShouldBeRemoved() {
        assertEquals("hello world", preprocessor.preprocess("<p>hello</p> <b>world</b>"));
    }
    @Test void scriptShouldBeRemoved() {
        assertEquals("hello", preprocessor.preprocess("<script>alert(1)</script><p>hello</p>"));
    }
    @Test void styleShouldBeRemoved() {
        assertEquals("hello", preprocessor.preprocess("<style>p{color:red}</style><p>hello</p>"));
    }
    @Test void entitiesShouldBeDecoded() {
        assertEquals("A & B", preprocessor.preprocess("A &amp; B"));
    }
    @Test void whitespaceShouldBeNormalized() {
        assertEquals("hello world", preprocessor.preprocess(" hello\n\t world "));
    }
}
