package neo.plugin.cleanarchitecture.cleanarchitecturehelper

import com.intellij.openapi.project.Project
import com.intellij.openapi.roots.ProjectRootManager
import com.intellij.openapi.vfs.VirtualFile

class GetFileContent(private val fileName: String, private val project: Project) {
    //Route.kt
    fun getRouteContent(virtualFile: VirtualFile): String {
        return """
            package ${getRelativePathFromSourceRoot(project, virtualFile)}
            
            import androidx.compose.runtime.Composable
            import androidx.compose.ui.tooling.preview.Preview
            
            @Composable
            fun ${fileName}Route() {
                ${fileName}Screen()
            }

            @Composable
            fun ${fileName}Screen() {

            }

            @Preview
            @Composable
            private fun ${fileName}ScreenPreview() {
                ${fileName}Screen()
            }
        """.trimIndent()
    }

    //ViewModel.kt
    fun getViewModelContent(virtualFile: VirtualFile): String {
        return """
            package ${getRelativePathFromSourceRoot(project, virtualFile)}
            
            import androidx.lifecycle.ViewModel
            import dagger.hilt.android.lifecycle.HiltViewModel
            import javax.inject.Inject

            @HiltViewModel
            class ${fileName}ViewModel @Inject constructor() : ViewModel(){}
        """.trimIndent()
    }

    //DataSource.kt
    fun getDataSourceContent(virtualFile: VirtualFile): String {
        return """
            package ${getRelativePathFromSourceRoot(project, virtualFile)}
            
            import javax.inject.Singleton

            @Singleton
            class ${fileName}DataSource (){}
        """.trimIndent()
    }

    //Repository.kt
    fun getRepositoryContent(virtualFile: VirtualFile): String {
        return """
            package ${getRelativePathFromSourceRoot(project, virtualFile)}
            
            interface ${fileName}Repository {}
        """.trimIndent()
    }

    //RepositoryImpl.kt
    fun getRepositoryImplContent(virtualFile: VirtualFile): String {
        return """
            package ${getRelativePathFromSourceRoot(project, virtualFile)}
            
            import javax.inject.Inject
            
            class ${fileName}RepositoryImpl @Inject constructor() : ${fileName}Repository {}
        """.trimIndent()
    }

    //UiState.kt
    fun getUiStateContent(virtualFile: VirtualFile): String {
        return """
            package ${getRelativePathFromSourceRoot(project, virtualFile)}
            
            import androidx.annotation.Keep
            
            @Keep
            data class ${fileName}UiState(
                val editUiState: ${fileName}EditUiState = ${fileName}EditUiState()
            )
            
            @Keep
            data class ${fileName}EditUiState(
                val logout: Boolean = false,
                val errorDialog: Boolean = false,
                val errorText: String = "",
                val isLoading: Boolean = false,
            )
        """.trimIndent()
    }

    //UiEffect
    fun getUiEffectContent(virtualFile: VirtualFile): String {
        return """
            package ${getRelativePathFromSourceRoot(project, virtualFile)}
            
            sealed class ${fileName}UiEffect {}
        """.trimIndent()
    }

    //拿到路徑的方法
    private fun getRelativePathFromSourceRoot(project: Project, virtualFile: VirtualFile): String? {
        val rootManager = ProjectRootManager.getInstance(project)
        val sourceRoots = rootManager.contentSourceRoots

        for (sourceRoot in sourceRoots) {
            val path = virtualFile.canonicalPath
            if (path != null && path.startsWith(sourceRoot.path)) {
                val relativePath = path.substring(sourceRoot.path.length)
                val packagePath = relativePath.substringBeforeLast('/')
                return packagePath.replace('/', '.').substring(1)
            }
        }

        return null
    }
}