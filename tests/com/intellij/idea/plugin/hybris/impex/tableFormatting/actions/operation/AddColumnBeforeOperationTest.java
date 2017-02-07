package com.intellij.idea.plugin.hybris.impex.tableFormatting.actions.operation;

import com.intellij.idea.plugin.hybris.impex.tableFormatting.ImpexTableEditor;
import com.intellij.idea.plugin.hybris.impex.tableFormatting.model.Range;
import com.intellij.idea.plugin.hybris.impex.tableFormatting.model.TableText;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AddColumnBeforeOperationTest {

    private static final String NOT_FORMATTED_TABLE = "INSERT_UPDATE JspIncludeComponent; $contentCV; uid; name; page; actions; &componentRef\n" +
                                                      ";;ArticleComponent;Article Component;article.jsp;;ArticleComponent\n";


    private static final String FORMATTED_TABLE_WITH_NEW_COLUMN = "INSERT_UPDATE JspIncludeComponent ; $contentCV ; uid              ; name              ; page        ;  ; actions ; &componentRef   \n" +
                                                                  "                                  ;            ; ArticleComponent ; Article Component ; article.jsp ;  ;         ; ArticleComponent\n";

    private ImpexTableEditor utility;

    @Before
    public void before() {
        utility = mock(ImpexTableEditor.class);
        when(utility.getSelectedText()).thenReturn(new TableText(NOT_FORMATTED_TABLE, new Range(1, 300)));
        when(utility.getCaretPosition()).thenReturn(65);
    }


    @Test
    public void addsColumn() {
        new AddColumnBeforeOperation(utility).run();

        verify(utility).replaceText(FORMATTED_TABLE_WITH_NEW_COLUMN, new Range(1, 300));
    }
}
