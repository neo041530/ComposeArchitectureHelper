package neo.plugin.cleanarchitecture.cleanarchitecturehelper

import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.vfs.VfsUtil

class CleanArchitectureHelperAction : AnAction() {
    override fun actionPerformed(e: AnActionEvent) {
        //獲取當前選中的目錄
        val project = e.project ?: return
        val virtualFile = e.getData(CommonDataKeys.VIRTUAL_FILE) ?: return
        if (!virtualFile.isDirectory) {
            Messages.showErrorDialog(project, "請選擇一個目錄", "錯誤")
            return
        }

        // 獲取新的檔名
        val fileName = Messages.showInputDialog(project, "請輸入檔名", "新增檔案", Messages.getQuestionIcon())
        if (fileName.isNullOrEmpty()) {
            return
        }

        // 新增檔案
        WriteCommandAction.runWriteCommandAction(project) {
            try {
                val getFileContent = GetFileContent(fileName = fileName.capitalizeFirstLetter(), project = project)
                //最外層資料夾
                val newDirectory = virtualFile.createChildDirectory(this, fileName.lowercaseFirstLetter())

                //ComposeRoute.kt
                val composeRouteFile = newDirectory.createChildData(this, "${fileName.capitalizeFirstLetter()}Route.kt")
                VfsUtil.saveText(
                    composeRouteFile, getFileContent.getRouteContent(
                        virtualFile = composeRouteFile
                    )
                )

                //ComposeViewModel.kt
                val composeViewModelFile =
                    newDirectory.createChildData(this, "${fileName.capitalizeFirstLetter()}ViewModel.kt")
                VfsUtil.saveText(
                    composeViewModelFile, getFileContent.getViewModelContent(
                        virtualFile = composeViewModelFile
                    )
                )

                //data資料夾
                val dataDirectory = newDirectory.createChildDirectory(this, "data")

                //DataSource.kt
                val dataSourceFile =
                    dataDirectory.createChildData(this, "${fileName.capitalizeFirstLetter()}DataSource.kt")
                VfsUtil.saveText(
                    dataSourceFile, getFileContent.getDataSourceContent(
                        virtualFile = dataSourceFile
                    )
                )

                //Repository.kt
                val repositoryFile =
                    dataDirectory.createChildData(this, "${fileName.capitalizeFirstLetter()}Repository.kt")
                VfsUtil.saveText(
                    repositoryFile, getFileContent.getRepositoryContent(
                        virtualFile = repositoryFile
                    )
                )

                //RepositoryImpl.kt
                val repositoryImplFile =
                    dataDirectory.createChildData(this, "${fileName.capitalizeFirstLetter()}RepositoryImpl.kt")
                VfsUtil.saveText(
                    repositoryImplFile, getFileContent.getRepositoryImplContent(
                        virtualFile = repositoryImplFile
                    )
                )

                //model資料夾
                val modelDirectory = newDirectory.createChildDirectory(this, "model")
                //UiState.kt
                val uiStateFile = modelDirectory.createChildData(this, "${fileName.capitalizeFirstLetter()}UiState.kt")
                VfsUtil.saveText(
                    uiStateFile, getFileContent.getUiStateContent(
                        virtualFile = uiStateFile
                    )
                )

                //UiEffect.kt
                val uiEffectFile =
                    modelDirectory.createChildData(this, "${fileName.capitalizeFirstLetter()}UiEffect.kt")
                VfsUtil.saveText(
                    uiEffectFile, getFileContent.getUiEffectContent(
                        virtualFile = uiEffectFile
                    )
                )
            } catch (e: Exception) {
                Messages.showErrorDialog(project, "檔案建立失敗: ${e.message}", "錯誤")
            }
        }
    }

    override fun update(event: AnActionEvent) {
        // 設定動作是否可用
        val virtualFile = event.getData(CommonDataKeys.VIRTUAL_FILE)
        event.presentation.isEnabledAndVisible = virtualFile != null && virtualFile.isDirectory
    }

    //後台線程運行
    override fun getActionUpdateThread(): ActionUpdateThread {
        return ActionUpdateThread.BGT
    }
}

//讓第一個字元大寫其他小寫的方法
fun String.capitalizeFirstLetter(): String {
    return this.substring(0, 1).uppercase() + this.substring(1)
}

//讓第一個字元大寫其他小寫的方法
fun String.lowercaseFirstLetter(): String {
    return this.substring(0, 1).lowercase() + this.substring(1)
}
