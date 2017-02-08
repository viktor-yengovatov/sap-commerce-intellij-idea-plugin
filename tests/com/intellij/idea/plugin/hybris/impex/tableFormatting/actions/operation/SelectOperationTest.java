package com.intellij.idea.plugin.hybris.impex.tableFormatting.actions.operation;

import com.intellij.idea.plugin.hybris.impex.tableFormatting.ImpexTableEditor;
import com.intellij.idea.plugin.hybris.impex.tableFormatting.model.Range;
import com.intellij.idea.plugin.hybris.impex.tableFormatting.model.TableText;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class SelectOperationTest {

    private static final String TEXT_WITH_NOT_FORMATTED_TABLE = "" +
                                                                "Story: Countries\n" +
                                                                "\n" +
                                                                "Scenario: Country currency\n" +
                                                                "When country is <Country>\n" +
                                                                "Then currency is <Currency>\n" +
                                                                "\n" +
                                                                "Examples:\n" +
                                                                "INSERT_UPDATE JspIncludeComponent; $contentCV; uid; name; page; actions; &componentRef\n" +
                                                                ";;ArticleComponent;Article Component;article.jsp;;ArticleComponent\n";

    private ImpexTableEditor utility;

    @Before
    public void before() {
        utility = mock(ImpexTableEditor.class);
    }

    @Test
    public void selectsTable() {
        when(utility.getSelectedText()).thenReturn(new TableText(null, new Range(0, 0)));
        when(utility.getText()).thenReturn(TEXT_WITH_NOT_FORMATTED_TABLE);
        when(utility.getCaretPosition()).thenReturn(229);

        new SelectImpexTableOperation(utility).run();

        verify(utility).setSelection(new Range(109, 263));
    }
}
