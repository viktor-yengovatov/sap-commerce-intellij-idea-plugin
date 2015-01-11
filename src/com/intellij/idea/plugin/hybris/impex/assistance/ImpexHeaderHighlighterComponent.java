package com.intellij.idea.plugin.hybris.impex.assistance;

import com.intellij.openapi.components.ApplicationComponent;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.EditorFactory;
import com.intellij.openapi.editor.actionSystem.TypedAction;
import com.intellij.openapi.editor.event.CaretEvent;
import com.intellij.openapi.editor.event.CaretListener;
import com.intellij.openapi.editor.event.EditorFactoryEvent;
import com.intellij.openapi.editor.event.EditorFactoryListener;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.project.ProjectManagerListener;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.PsiTreeChangeEvent;
import com.intellij.psi.PsiTreeChangeListener;
import com.intellij.psi.util.PsiUtilBase;
import org.jetbrains.annotations.NotNull;

/**
 * Created 19:56 11 January 2015
 *
 * @author Alexander Bartash <AlexanderBartash@gmail.com>
 */
public class ImpexHeaderHighlighterComponent implements ApplicationComponent {

    private final CaretListener caretListener = new ImpexHeaderHighlightingCaretListener();
    private final ProjectManagerListener projectManagerListener = new ImpexProjectManagerListener();
    private final PsiTreeChangeListener psiTreeChangeListener = new ImpexPsiTreeChangeListener();
    private final EditorFactoryListener editorFactoryListener = new ImpexEditorFactoryListener();
    private final ImpexHeaderNameHighlighter impexHeaderNameHighlighter = new DefaultImpexHeaderNameHighlighter();

    @Override
    public void initComponent() {
        EditorFactory.getInstance().getEventMulticaster().addCaretListener(caretListener);
        ProjectManager.getInstance().addProjectManagerListener(this.projectManagerListener);
    }

    @Override
    public void disposeComponent() {
        EditorFactory.getInstance().getEventMulticaster().removeCaretListener(caretListener);
        ProjectManager.getInstance().removeProjectManagerListener(this.projectManagerListener);
    }

    @NotNull
    @Override
    public String getComponentName() {
        return this.getClass().getName();
    }

    protected class ImpexHeaderHighlightingCaretListener implements CaretListener {

        @Override
        public void caretPositionChanged(final CaretEvent e) {
            if (TypedAction.isTypedActionInProgress()) {
                return;
            }

            impexHeaderNameHighlighter.highlightCurrentHeader(e.getEditor());
        }

        @Override
        public void caretAdded(final CaretEvent e) {
        }

        @Override
        public void caretRemoved(final CaretEvent e) {
        }
    }

    protected class ImpexProjectManagerListener implements ProjectManagerListener {

        @Override
        public void projectOpened(final Project project) {
            PsiManager.getInstance(project).addPsiTreeChangeListener(psiTreeChangeListener);
            EditorFactory.getInstance().addEditorFactoryListener(editorFactoryListener, project);
        }

        @Override
        public boolean canCloseProject(final Project project) {
            return true;
        }

        @Override
        public void projectClosed(final Project project) {
            PsiManager.getInstance(project).removePsiTreeChangeListener(psiTreeChangeListener);
        }

        @Override
        public void projectClosing(final Project project) {

        }
    }

    protected class ImpexPsiTreeChangeListener implements PsiTreeChangeListener {

        private void highlightHeader(final PsiTreeChangeEvent psiTreeChangeEvent) {
            final PsiFile file = psiTreeChangeEvent.getFile();

            if (null == file) {
                return;
            }

            final Editor editor = PsiUtilBase.findEditor(file);

            if (null == editor) {
                return;
            }

            impexHeaderNameHighlighter.highlightCurrentHeader(editor);
        }

        @Override
        public void beforeChildAddition(@NotNull final PsiTreeChangeEvent psiTreeChangeEvent) {
            //this.highlightHeader(psiTreeChangeEvent);
        }

        @Override
        public void beforeChildRemoval(@NotNull final PsiTreeChangeEvent psiTreeChangeEvent) {
            //this.highlightHeader(psiTreeChangeEvent);
        }

        @Override
        public void beforeChildReplacement(@NotNull final PsiTreeChangeEvent psiTreeChangeEvent) {
            //this.highlightHeader(psiTreeChangeEvent);
        }

        @Override
        public void beforeChildMovement(@NotNull final PsiTreeChangeEvent psiTreeChangeEvent) {
            //this.highlightHeader(psiTreeChangeEvent);
        }

        @Override
        public void beforeChildrenChange(@NotNull final PsiTreeChangeEvent psiTreeChangeEvent) {
            //this.highlightHeader(psiTreeChangeEvent);
        }

        @Override
        public void beforePropertyChange(@NotNull final PsiTreeChangeEvent psiTreeChangeEvent) {
            //this.highlightHeader(psiTreeChangeEvent);
        }

        @Override
        public void childAdded(@NotNull final PsiTreeChangeEvent psiTreeChangeEvent) {
            this.highlightHeader(psiTreeChangeEvent);
        }

        @Override
        public void childRemoved(@NotNull final PsiTreeChangeEvent psiTreeChangeEvent) {
            this.highlightHeader(psiTreeChangeEvent);
        }

        @Override
        public void childReplaced(@NotNull final PsiTreeChangeEvent psiTreeChangeEvent) {
            //this.highlightHeader(psiTreeChangeEvent);
        }

        @Override
        public void childrenChanged(@NotNull final PsiTreeChangeEvent psiTreeChangeEvent) {
            //this.highlightHeader(psiTreeChangeEvent);
        }

        @Override
        public void childMoved(@NotNull final PsiTreeChangeEvent psiTreeChangeEvent) {
            //this.highlightHeader(psiTreeChangeEvent);
        }

        @Override
        public void propertyChanged(@NotNull final PsiTreeChangeEvent psiTreeChangeEvent) {
            //this.highlightHeader(psiTreeChangeEvent);
        }
    }

    protected class ImpexEditorFactoryListener implements EditorFactoryListener {

        @Override
        public void editorCreated(@NotNull final EditorFactoryEvent editorFactoryEvent) {

        }

        @Override
        public void editorReleased(@NotNull final EditorFactoryEvent editorFactoryEvent) {
            impexHeaderNameHighlighter.releaseEditorData(editorFactoryEvent.getEditor());
        }
    }
}
