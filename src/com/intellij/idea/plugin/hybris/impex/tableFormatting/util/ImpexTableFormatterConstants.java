package com.intellij.idea.plugin.hybris.impex.tableFormatting.util;


/**
 * @author Aleksandr Nosov <nosovae.dev@gmail.com>
 */
public interface ImpexTableFormatterConstants {

    char PIPE = ';';
    char[] DEFAULT_DELIMITERS = new char[]{PIPE, '\n', '\r'};

    String PIPE_COMMENT_START = "#";
    String PIPE_COMMENT_END = PIPE_COMMENT_START;

}
