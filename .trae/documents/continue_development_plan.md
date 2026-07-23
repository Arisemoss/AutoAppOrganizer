# 桌面整理应用完善开发计划文档

## 1. 项目概述

本项目为一款桌面整理（Desktop Organizer）Android 应用，旨在帮助用户自动分类和整理桌面文件。本文档梳理了项目当前完成状态，并制定了后续完善的详细开发计划。

| 项目 | 内容 |
|------|------|
| 应用名称 | 桌面整理 (Desktop Organizer) |
| 包名 | com.example.desktoporganizer |
| 最低 SDK | Android 8.0 (API 26) |
| 目标 SDK | Android 13 (API 33) |
| 构建系统 | Gradle + AGP 8.1.0 |
| 开发语言 | Kotlin + Jetpack Compose |

---

## 2. 项目现状分析

### 2.1 已完成模块

| 模块 | 状态 | 说明 |
|------|------|------|
| 项目脚手架 | ✅ 完成 | Gradle 配置、AGP 8.1.0、Kotlin 1.9.0 |
| 基础 UI 框架 | ✅ 完成 | Jetpack Compose + Material3 主题 |
| 主页面布局 | ✅ 完成 | 桌面文件列表、分类 Tab 切换 |
| 文件扫描服务 | ✅ 完成 | 后台扫描桌面文件，支持递归扫描 |
| 文件分类引擎 | ✅ 完成 | 按扩展名分类（文档/图片/视频/音频/压缩包） |
| 基础设置页面 | ✅ 完成 | 分类规则配置、扫描间隔设置 |
| Room 数据库 | ✅ 完成 | 文件记录、分类规则的持久化存储 |
| 主题切换功能 | ✅ 完成 | 亮色/暗色模式支持 |

### 2.2 待完善功能

#### P0 — 阻塞性问题（必须修复）

| 编号 | 问题 | 影响 | 优先级 |
|------|------|------|--------|
| P0-1 | JDK 25 与 AGP 8.1.0 不兼容 | 项目无法构建 | P0 |
| P0-2 | 缺少 Android SDK 环境 | 项目无法构建 | P0 |
| P0-3 | 缺少 local.properties 文件 | 项目无法定位 SDK | P0 |
| P0-4 | AndroidManifest.xml 缺少 namespace 声明 | 构建警告/潜在错误 | P0 |

#### P1 — 核心功能缺陷

| 编号 | 问题 | 影响 | 优先级 |
|------|------|------|--------|
| P1-1 | 文件移动操作未实现 | 核心功能缺失 | P1 |
| P1-2 | 分类规则不可编辑 | 用户无法自定义规则 | P1 |
| P1-3 | 文件扫描结果未刷新 UI | 用户体验差 | P1 |
| P1-4 | 缺少文件操作权限请求流程 | Android 11+ 无法访问文件 | P1 |

#### P2 — 用户体验增强

| 编号 | 功能 | 描述 | 优先级 |
|------|------|------|--------|
| P2-1 | 文件搜索功能 | 支持按文件名搜索 | P2 |
| P2-2 | 批量操作支持 | 批量移动/删除文件 | P2 |
| P2-3 | 文件预览功能 | 点击文件弹出预览 | P2 |
| P2-4 | 分类统计图表 | 按分类显示文件数量占比 | P2 |
| P2-5 | 撤销操作 | 误操作后可撤销 | P2 |
| P2-6 | 桌面 Widget | 主屏幕快捷整理入口 | P2 |

#### P3 — 工程完善

| 编号 | 功能 | 描述 | 优先级 |
|------|------|------|--------|
| P3-1 | 单元测试覆盖 | 为核心逻辑编写测试 | P3 |
| P3-2 | 国际化支持 | 中英文多语言 | P3 |
| P3-3 | 崩溃收集与分析 | 集成 Firebase Crashlytics | P3 |
| P3-4 | 性能优化 | 大文件列表的流畅度优化 | P3 |
| P3-5 | 无障碍支持 | Content Description 等 | P3 |

---

## 3. 详细变更计划

### 阶段一：修复阻塞性问题（P0）

#### 1.1 降级 JDK 到 17

```bash
# 安装 JDK 17
sudo apt update
sudo apt install openjdk-17-jdk -y

# 设置 JAVA_HOME
export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64
export PATH=$JAVA_HOME/bin:$PATH

# 验证
java -version
```

#### 1.2 安装 Android SDK & 配置 local.properties

```bash
# 创建 SDK 目录
mkdir -p ~/Android/Sdk

# 下载命令行工具
cd ~/Android/Sdk
wget https://dl.google.com/android/repository/commandlinetools-linux-9477386_latest.zip
unzip commandlinetools-linux-9477386_latest.zip
rm commandlinetools-linux-9477386_latest.zip

# 安装 SDK 组件
yes | ~/Android/Sdk/cmdline-tools/bin/sdkmanager --sdk_root=~/Android/Sdk \
  "platforms;android-33" \
  "build-tools;33.0.2" \
  "platform-tools"
```

```properties
# local.properties
sdk.dir=/home/user/Android/Sdk
```

#### 1.3 修复 AndroidManifest.xml

```xml
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools">

    <!-- 权限声明 -->
    <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE"
        android:maxSdkVersion="32" />
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"
        android:maxSdkVersion="29" />
    <uses-permission android:name="android.permission.MANAGE_EXTERNAL_STORAGE"
        tools:ignore="ScopedStorage" />
    <uses-permission android:name="android.permission.FOREGROUND_SERVICE" />
    <uses-permission android:name="android.permission.POST_NOTIFICATIONS" />

    <application
        android:allowBackup="true"
        android:icon="@mipmap/ic_launcher"
        android:label="@string/app_name"
        android:supportsRtl="true"
        android:theme="@style/Theme.DesktopOrganizer">

        <activity
            android:name=".MainActivity"
            android:exported="true"
            android:theme="@style/Theme.DesktopOrganizer">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />
                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>

        <service
            android:name=".service.FileScanService"
            android:foregroundServiceType="dataSync" />

    </application>
</manifest>
```

#### 1.4 更新 build.gradle 配置

```groovy
// app/build.gradle
android {
    namespace 'com.example.desktoporganizer'
    compileSdk 33

    defaultConfig {
        applicationId "com.example.desktoporganizer"
        minSdk 26
        targetSdk 33
        versionCode 1
        versionName "1.0.0"
    }

    compileOptions {
        sourceCompatibility JavaVersion.VERSION_17
        targetCompatibility JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }
}
```

### 阶段二：完善核心功能（P1）

#### 2.1 文件操作权限请求

```kotlin
// 在 MainActivity 中添加权限请求逻辑
class MainActivity : ComponentActivity() {
    private val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { /* 处理结果 */ }

    private fun requestStoragePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            // Android 11+ 使用 MANAGE_EXTERNAL_STORAGE
            val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION).apply {
                data = Uri.parse("package:$packageName")
            }
            permissionLauncher.launch(intent)
        } else {
            // Android 10 及以下使用运行时权限
            requestPermissions(
                arrayOf(READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE),
                REQUEST_CODE_STORAGE_PERMISSION
            )
        }
    }
}
```

#### 2.2 文件移动操作实现

```kotlin
// 文件移动功能实现
class FileOperationManager(private val context: Context) {

    /**
     * 将文件移动到目标分类目录
     * @param file 源文件
     * @param category 目标分类
     * @return 移动结果
     */
    suspend fun moveToCategory(file: File, category: FileCategory): Result<File> {
        return withContext(Dispatchers.IO) {
            try {
                val targetDir = File(
                    Environment.getExternalStorageDirectory(),
                    "DesktopOrganizer/${category.name}"
                )
                if (!targetDir.exists()) targetDir.mkdirs()

                val targetFile = File(targetDir, file.name)
                // 处理重名文件
                val finalFile = resolveConflict(targetFile)

                file.renameTo(finalFile).let { success ->
                    if (success) Result.success(finalFile)
                    else Result.failure(IOException("文件移动失败"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    private fun resolveConflict(file: File): File {
        if (!file.exists()) return file
        val nameWithoutExt = file.nameWithoutExtension
        val ext = file.extension
        var counter = 1
        var resolved: File
        do {
            resolved = File(file.parent, "${nameWithoutExt}_($counter).$ext")
            counter++
        } while (resolved.exists())
        return resolved
    }
}
```

#### 2.3 可编辑分类规则

```kotlin
// 分类规则编辑功能
@Entity(tableName = "classification_rules")
data class ClassificationRule(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val categoryName: String,
    val extensions: List<String>,  // 例如 ["pdf", "doc", "docx"]
    val isEnabled: Boolean = true,
    val createdTime: Long = System.currentTimeMillis()
)

// 规则管理 ViewModel
class RuleViewModel(private val ruleDao: ClassificationRuleDao) : ViewModel() {

    val allRules: LiveData<List<ClassificationRule>> = ruleDao.getAllRules()

    fun addRule(rule: ClassificationRule) {
        viewModelScope.launch {
            ruleDao.insert(rule)
        }
    }

    fun updateRule(rule: ClassificationRule) {
        viewModelScope.launch {
            ruleDao.update(rule)
        }
    }

    fun deleteRule(rule: ClassificationRule) {
        viewModelScope.launch {
            ruleDao.delete(rule)
        }
    }
}
```

#### 2.4 扫描结果实时刷新

```kotlin
// 使用 Flow 实现扫描结果实时更新
@Dao
interface FileRecordDao {
    @Query("SELECT * FROM file_records ORDER BY lastModified DESC")
    fun getAllFilesFlow(): Flow<List<FileRecord>>

    @Query("SELECT * FROM file_records WHERE category = :category ORDER BY lastModified DESC")
    fun getFilesByCategoryFlow(category: String): Flow<List<FileRecord>>
}

// 在 ViewModel 中收集 Flow
class FileListViewModel(private val repository: FileRepository) : ViewModel() {
    val allFiles: StateFlow<List<FileRecord>> = repository
        .getAllFiles()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
}
```

### 阶段三：用户体验增强（P2）

#### 3.1 文件搜索功能

```kotlin
@Composable
fun SearchBar(onSearch: (String) -> Unit) {
    var query by remember { mutableStateOf("") }

    OutlinedTextField(
        value = query,
        onValueChange = {
            query = it
            onSearch(it)
        },
        placeholder = { Text("搜索文件...") },
        leadingIcon = { Icon(Icons.Default.Search, contentDescription = "搜索") },
        trailingIcon = {
            if (query.isNotEmpty()) {
                IconButton(onClick = { query = ""; onSearch("") }) {
                    Icon(Icons.Default.Clear, contentDescription = "清除")
                }
            }
        },
        modifier = Modifier.fillMaxWidth().padding(16.dp)
    )
}
```

#### 3.2 批量操作

```kotlin
@Composable
fun BatchActionBar(
    selectedCount: Int,
    onMoveAll: () -> Unit,
    onDeleteAll: () -> Unit,
    onCancel: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shadowElevation = 8.dp
    ) {
        Row(
            modifier = Modifier.padding(8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("已选择 $selectedCount 项")
            TextButton(onClick = onMoveAll) { Text("批量移动") }
            TextButton(onClick = onDeleteAll) { Text("批量删除") }
            TextButton(onClick = onCancel) { Text("取消") }
        }
    }
}
```

#### 3.3 分类统计图表

```kotlin
@Composable
fun CategoryPieChart(stats: Map<FileCategory, Int>) {
    // 使用 Canvas 绘制饼图
    Canvas(modifier = Modifier.size(200.dp)) {
        val total = stats.values.sum().toFloat()
        if (total == 0f) return@Canvas

        var startAngle = -90f
        stats.forEach { (category, count) ->
            val sweepAngle = (count / total) * 360f
            drawArc(
                color = category.color,
                startAngle = startAngle,
                sweepAngle = sweepAngle,
                useCenter = true
            )
            startAngle += sweepAngle
        }
    }
}
```

### 阶段四：工程完善（P3）

#### 4.1 单元测试

```kotlin
// 文件分类引擎测试
class FileClassifierTest {
    private val classifier = FileClassifier()

    @Test
    fun `pdf 文件应分类为文档`() {
        val file = File("report.pdf")
        assertEquals(FileCategory.DOCUMENT, classifier.classify(file))
    }

    @Test
    fun `jpg 文件应分类为图片`() {
        val file = File("photo.jpg")
        assertEquals(FileCategory.IMAGE, classifier.classify(file))
    }

    @Test
    fun `未知扩展名应分类为其他`() {
        val file = File("unknown.xyz")
        assertEquals(FileCategory.OTHER, classifier.classify(file))
    }
}
```

#### 4.2 国际化支持

```xml
<!-- res/values/strings.xml（中文） -->
<resources>
    <string name="app_name">桌面整理</string>
    <string name="tab_documents">文档</string>
    <string name="tab_images">图片</string>
    <string name="tab_videos">视频</string>
    <string name="action_move">移动到</string>
    <string name="action_delete">删除</string>
    <string name="action_search">搜索</string>
    <string name="settings">设置</string>
</resources>

<!-- res/values-en/strings.xml（英文） -->
<resources>
    <string name="app_name">Desktop Organizer</string>
    <string name="tab_documents">Documents</string>
    <string name="tab_images">Images</string>
    <string name="tab_videos">Videos</string>
    <string name="action_move">Move to</string>
    <string name="action_delete">Delete</string>
    <string name="action_search">Search</string>
    <string name="settings">Settings</string>
</resources>
```

#### 4.3 构建系统升级

```groovy
// 升级方案（可选，在当前 AGP 8.1.0 稳定后可考虑）
// project/build.gradle
plugins {
    id 'com.android.application' version '8.2.0' apply false
    id 'org.jetbrains.kotlin.android' version '1.9.20' apply false
}

// 对应 Gradle 版本升级到 8.5+
// gradle-wrapper.properties
// distributionUrl=https\://services.gradle.org/distributions/gradle-8.5-bin.zip
```

---

## 4. 文件变更清单汇总表

| 文件路径 | 变更类型 | 所属阶段 | 说明 |
|---------|---------|---------|------|
| `local.properties` | 新建 | 阶段一 | Android SDK 路径配置 |
| `app/build.gradle` | 修改 | 阶段一 | 添加 namespace、更新 JDK 配置 |
| `app/src/main/AndroidManifest.xml` | 修改 | 阶段一 | 修复命名空间声明、添加权限 |
| `app/src/main/java/.../MainActivity.kt` | 修改 | 阶段二 | 添加权限请求逻辑 |
| `app/src/main/java/.../FileOperationManager.kt` | 新建 | 阶段二 | 文件移动管理器 |
| `app/src/main/java/.../ClassificationRule.kt` | 新建 | 阶段二 | 分类规则实体类 |
| `app/src/main/java/.../RuleViewModel.kt` | 新建 | 阶段二 | 规则管理 ViewModel |
| `app/src/main/java/.../FileRecordDao.kt` | 修改 | 阶段二 | 添加 Flow 查询方法 |
| `app/src/main/java/.../FileListViewModel.kt` | 修改 | 阶段二 | 添加 StateFlow 支持 |
| `app/src/main/java/.../ui/SearchBar.kt` | 新建 | 阶段三 | 搜索栏组件 |
| `app/src/main/java/.../ui/BatchActionBar.kt` | 新建 | 阶段三 | 批量操作栏组件 |
| `app/src/main/java/.../ui/CategoryPieChart.kt` | 新建 | 阶段三 | 分类统计图表 |
| `app/src/main/java/.../ui/FilePreviewDialog.kt` | 新建 | 阶段三 | 文件预览对话框 |
| `app/src/test/java/.../FileClassifierTest.kt` | 新建 | 阶段四 | 分类引擎单元测试 |
| `app/src/main/res/values/strings.xml` | 修改 | 阶段四 | 中文字符串资源 |
| `app/src/main/res/values-en/strings.xml` | 新建 | 阶段四 | 英文字符串资源 |
| `gradle/wrapper/gradle-wrapper.properties` | 修改 | 阶段四 | Gradle 版本升级（可选） |

---

## 5. 实施顺序

```
阶段一（P0 阻塞性问题）
  ├── 1.1 降级 JDK 到 17
  ├── 1.2 安装 Android SDK
  ├── 1.3 配置 local.properties
  └── 1.4 修复 AndroidManifest.xml + build.gradle
      └── 验证：构建成功，输出 app-debug.apk
          ↓
阶段二（P1 核心功能）
  ├── 2.1 权限请求流程
  ├── 2.2 文件移动操作
  ├── 2.3 可编辑分类规则
  └── 2.4 扫描结果实时刷新
      └── 验证：核心功能完整可用
          ↓
阶段三（P2 用户体验）
  ├── 3.1 文件搜索
  ├── 3.2 批量操作
  ├── 3.3 分类统计图表
  ├── 3.4 文件预览
  └── 3.5 撤销操作
      └── 验证：用户体验流畅，功能完整
          ↓
阶段四（P3 工程完善）
  ├── 4.1 单元测试
  ├── 4.2 国际化支持
  ├── 4.3 崩溃收集
  ├── 4.4 性能优化
  └── 4.5 构建系统升级（可选）
      └── 验证：全部测试通过，覆盖率达到目标
```

---

## 6. 验证步骤

### 6.1 阶段一验证

```bash
# 1. 验证 JDK 版本
java -version
# 预期：openjdk version "17.x.x"

# 2. 验证 Android SDK
echo $ANDROID_HOME
ls $ANDROID_HOME/platforms/
# 预期：存在 android-33 目录

# 3. 验证构建
./gradlew clean assembleDebug
# 预期：BUILD SUCCESSFUL

# 4. 验证 APK 产物
ls -lh app/build/outputs/apk/debug/app-debug.apk
# 预期：文件存在且大小 > 1MB
```

### 6.2 阶段二验证

| 测试项 | 操作 | 预期结果 |
|--------|------|---------|
| 权限请求 | 首次启动应用 | 弹出文件权限请求对话框 |
| 文件扫描 | 点击刷新按钮 | 桌面文件列表更新 |
| 文件移动 | 选择文件 → 移动到"文档" | 文件出现在文档分类下 |
| 规则编辑 | 设置 → 新增分类规则 | 规则生效，文件按新规则分类 |

### 6.3 阶段三验证

| 测试项 | 操作 | 预期结果 |
|--------|------|---------|
| 文件搜索 | 输入文件名关键字 | 实时显示匹配结果 |
| 批量操作 | 多选文件 → 批量移动 | 全部文件移动到目标目录 |
| 分类统计 | 打开统计页面 | 显示饼图/柱状图 |
| 文件预览 | 点击文件 | 弹出预览对话框 |

### 6.4 阶段四验证

```bash
# 运行全部单元测试
./gradlew testDebugUnitTest
# 预期：全部测试通过

# 检查构建产物
./gradlew assembleRelease
# 预期：BUILD SUCCESSFUL
```

---

## 7. 风险评估

| 风险 | 影响 | 概率 | 缓解措施 |
|------|------|------|---------|
| JDK 降级导致其他项目不可用 | 中 | 低 | 使用 SDKMAN 管理多版本 JDK |
| Android 11+ 文件访问限制 | 高 | 高 | 使用 SAF (Storage Access Framework) 替代直接路径访问 |
| 大文件列表性能问题 | 中 | 中 | 使用 Paging 3 库分页加载 |
| 权限变更（Android 14+） | 中 | 低 | 关注 Android 版本更新，及时适配 |

---

## 8. 附录

### 8.1 依赖库清单

```groovy
dependencies {
    // Jetpack Compose
    implementation 'androidx.compose.ui:ui:1.5.0'
    implementation 'androidx.compose.material3:material3:1.1.0'
    implementation 'androidx.compose.ui:ui-tooling-preview:1.5.0'

    // Navigation
    implementation 'androidx.navigation:navigation-compose:2.6.0'

    // Room
    implementation 'androidx.room:room-runtime:2.5.2'
    implementation 'androidx.room:room-ktx:2.5.2'
    kapt 'androidx.room:room-compiler:2.5.2'

    // Lifecycle
    implementation 'androidx.lifecycle:lifecycle-viewmodel-compose:2.6.2'
    implementation 'androidx.lifecycle:lifecycle-runtime-compose:2.6.2'

    // Coroutines
    implementation 'org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3'

    // Testing
    testImplementation 'junit:junit:4.13.2'
    testImplementation 'org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3'
}
```

### 8.2 参考文档

- [Android 文件访问权限指南](https://developer.android.com/training/data-storage/file-access)
- [Jetpack Compose 官方文档](https://developer.android.com/jetpack/compose)
- [Room 数据库使用指南](https://developer.android.com/training/data-storage/room)
- [Android Gradle Plugin 兼容性表](https://developer.android.com/studio/releases/gradle-plugin#compatibility)