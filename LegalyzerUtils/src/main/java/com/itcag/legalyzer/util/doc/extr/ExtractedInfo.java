package com.itcag.legalyzer.util.doc.extr;

import java.util.LinkedList;

/**
 *
 */
public class ExtractedInfo {

    public class Position {

        private final int paragraphIndex;
        private final int sentenceIndex;
        private final int start;
        private final int end;

        private Position(int paragraphIndex, int sentenceIndex, int start, int end) {
            this.paragraphIndex = paragraphIndex;
            this.sentenceIndex = sentenceIndex;
            this.start = start;
            this.end = end;
        }
    
        public int getParagraphIndex() {
            return paragraphIndex;
        }

        public int getSentenceIndex() {
            return sentenceIndex;
        }

        public int getStart() {
            return start;
        }

        public int getEnd() {
            return end;
        }
        
    }
    
    protected final LinkedList<Position> positions = new LinkedList<>();
    
    public LinkedList<Position> getPositions() {
        return this.positions;
    }
    
    public Position getLastPosition() {
        return this.positions.getLast();
    }
    
    public void addPosition(int paragraphIndex, int sentenceIndex, int start, int end) {
        this.positions.add(new Position(paragraphIndex, sentenceIndex, start, end));
    }
    
}
