package com.intellij.idea.plugin.hybris.impex.tableFormatting.util;

import com.intellij.idea.plugin.hybris.impex.constants.ImpexKeywords;
import com.intellij.idea.plugin.hybris.impex.tableFormatting.model.DelimitersCount;
import com.intellij.idea.plugin.hybris.impex.tableFormatting.model.Range;

import java.util.Set;

import static com.intellij.idea.plugin.hybris.impex.tableFormatting.model.DelimitersCount.pipesCountIn;


/**
 * @author Aleksandr Nosov <nosovae.dev@gmail.com>
 */
public final class ImpexTableUtil {

    public static TableDetector detectTableIn(String text) {
        return new TableDetector(text);
    }

    /**
     * Check that the line is header line or not (by keywords).
     *
     * @param line checked line
     *
     * @return true or false
     */
    public static boolean isHeaderLine(String line) {
        return ImpexKeywords.keywords().stream()
                            .anyMatch(keyword -> line.trim().toLowerCase().startsWith(keyword));
    }

    /**
     * @author Aleksandr Nosov <nosovae.dev@gmail.com>
     */
    public static class TableDetector {

        private String text;
        private DelimitersCount baseLineDelimitersCount;

        private TableDetector(String text) {
            this.text = "\n" + text + "\n";
        }

        public Range around(int position) {
            Range baseLine = findBaseLine(position + 1);
            baseLineDelimitersCount = asIn(baseLine);
            return baseLineDelimitersCount.isZero() ? Range.EMPTY : findTableRange(baseLine);
        }

        private Range findBaseLine(int position) {
            return new Range(findSOL(position), findEOL(position));
        }

        private Range findTableRange(Range baseLine) {
            return new Range(findStartOfTableOn(baseLine), findEndOfTableOn(baseLine) - 1);
        }

        private int findStartOfTableOn(Range line) {
            return tableContains(line) ? findStartOfTableOn(previous(line)) : line.getEnd();
        }

        private int findEndOfTableOn(Range line) {
            return tableContains(line) ? findEndOfTableOn(next(line)) : line.getStart();
        }

        private int findEOL(int position) {
            return text.indexOf("\n", position);
        }

        private int findSOL(int position) {
            final Set<String> keywords = ImpexKeywords.keywords();
            for (final String keyword : keywords) {
                final int lastIndexOf = text.lastIndexOf(keyword, position - 1);
                if (lastIndexOf != -1) {
                    return lastIndexOf - 1 + text.lastIndexOf("#", position - 1);
                }
            }
            return text.lastIndexOf("\n", position - 1);
        }

        private boolean tableContains(Range line) {
            return baseLineDelimitersCount.isSameCount(asIn(line));
        }

        private DelimitersCount asIn(Range currLine) {
            return pipesCountIn(text, currLine);
        }

        private Range previous(Range line) {
            return new Range(findSOL(line.getStart() - 1), line.getStart());
        }

        private Range next(Range line) {
            return new Range(line.getEnd(), findEOL(line.getEnd() + 1));
        }

    }

}
