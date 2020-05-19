package com.ms;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.SystemIndependent;

import java.io.File;
import java.io.IOException;

public class MsUtils extends AnAction {

    @Override
    public void actionPerformed(@NotNull AnActionEvent anActionEvent) {
        Project currentProject = anActionEvent.getProject();
        assert currentProject != null;
        @SystemIndependent String basePath = currentProject.getBasePath();
        try {
            Runtime.getRuntime().exec("cmd /c cd \""+basePath+"\" & start cmd.exe /k \"gradlew clean build\"");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(@NotNull AnActionEvent e) {
        Project project = e.getProject();
        assert project != null;
        @SystemIndependent String basePath = project.getBasePath();
        File file = new File(basePath + File.separator + "gradlew.bat");
        boolean isVisible = file.exists();
        e.getPresentation().setEnabled(isVisible);
    }
}
