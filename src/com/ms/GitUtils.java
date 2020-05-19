package com.ms;

import com.intellij.lang.javascript.boilerplate.GithubDownloadUtil;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import org.ini4j.Ini;
import org.ini4j.IniPreferences;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.SystemIndependent;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.Arrays;
import java.util.Optional;

public class GitUtils extends AnAction {

    @Override
    public void actionPerformed(@NotNull AnActionEvent anActionEvent) {
        Project project = anActionEvent.getProject();
        @SystemIndependent String basePath = project.getBasePath();
        VirtualFile workspaceFile = project.getWorkspaceFile();
        try {
            String gitPath = getGitPath(workspaceFile).replace("$PROJECT_DIR$", basePath).replace("\"","");
            File gitDir = new File(gitPath + File.separator + ".git");
            if(gitDir.exists()) {
                File config = new File(gitDir + File.separator + "config");
                Ini ini = new Ini(config);
                IniPreferences iniPreferences = new IniPreferences(ini);
                String url = iniPreferences.node("remote \"origin\"").get("url","");
                if(Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                    Desktop.getDesktop().browse(new URI(url));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getGitPath(VirtualFile workspaceFile) throws ParserConfigurationException, IOException, SAXException, XPathExpressionException {
        XmlUtils xmlUtils = new XmlUtils(new File(workspaceFile.getPath()));
        String attributeValue = xmlUtils.getAttributeValue("//component[@name=\"Git.Settings\"]/option[@name=\"RECENT_GIT_ROOT_PATH\"]/@value");
        return attributeValue;
    }
}
