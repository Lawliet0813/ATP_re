# ATP_re 測試指南

**版本：** 1.0  
**更新日期：** 2025-11-09  
**適用系統：** ATP_re Java 版本（及未來 Python 版本）

---

## 📖 目錄

1. [測試概述](#測試概述)
2. [測試策略](#測試策略)
3. [測試環境設定](#測試環境設定)
4. [單元測試](#單元測試)
5. [整合測試](#整合測試)
6. [系統測試](#系統測試)
7. [效能測試](#效能測試)
8. [測試資料準備](#測試資料準備)
9. [測試自動化](#測試自動化)
10. [測試報告](#測試報告)

---

## 測試概述

### 測試目標

ATP_re 系統的測試目標包括：

1. **正確性驗證**：確保解碼結果 100% 正確
2. **效能驗證**：達到或超越現有系統效能
3. **穩定性驗證**：長時間執行不崩潰
4. **相容性驗證**：支援所有規定的檔案版本
5. **可用性驗證**：使用者介面友善易用

### 測試範圍

```
┌─────────────────────────────────────┐
│        系統測試                      │
│  - 端對端工作流程                    │
│  - 使用者接受度測試                  │
└──────────────┬──────────────────────┘
               │
┌──────────────┴──────────────────────┐
│        整合測試                      │
│  - 模組間介面測試                    │
│  - 資料庫整合測試                    │
└──────────────┬──────────────────────┘
               │
┌──────────────┴──────────────────────┐
│        單元測試                      │
│  - 個別函式/方法測試                │
│  - 邏輯驗證                          │
└─────────────────────────────────────┘
```

### 測試指標

| 指標類型 | 目標值 | 說明 |
|---------|--------|------|
| 程式碼覆蓋率 | ≥ 80% | 整體覆蓋率 |
| 核心模組覆蓋率 | ≥ 95% | 解碼器、資料模型 |
| 缺陷密度 | < 0.5/KLOC | 每千行程式碼的缺陷數 |
| 對照驗證通過率 | 100% | 與 Java 版本對照 |
| 效能基準 | ≥ 100% | 相對於 Java 版本 |

---

## 測試策略

### 測試金字塔

```
        ┌─────────┐
        │  手動   │  ← 5%  探索性測試、可用性測試
        │  測試   │
        └─────────┘
      ┌─────────────┐
      │  端對端測試  │  ← 15% 完整工作流程
      └─────────────┘
    ┌─────────────────┐
    │   整合測試       │  ← 30% 模組整合
    └─────────────────┘
  ┌───────────────────────┐
  │     單元測試           │  ← 50% 個別函式
  └───────────────────────┘
```

### 測試階段

#### 階段 1：開發階段測試
- 單元測試（開發者撰寫）
- 程式碼覆蓋率檢查
- 靜態程式碼分析

#### 階段 2：整合測試
- 模組介面測試
- 資料庫整合測試
- 外部系統整合（FTP、檔案系統）

#### 階段 3：系統測試
- 功能測試
- 效能測試
- 安全測試
- 相容性測試

#### 階段 4：驗收測試
- 使用者接受度測試（UAT）
- 對照驗證測試
- 回歸測試

---

## 測試環境設定

### 開發測試環境

#### Java 測試環境

**所需工具：**
```bash
# JUnit 5
dependencies {
    testImplementation 'org.junit.jupiter:junit-jupiter:5.9.0'
    testImplementation 'org.mockito:mockito-core:4.8.0'
}

# Maven
<dependency>
    <groupId>org.junit.jupiter</groupId>
    <artifactId>junit-jupiter</artifactId>
    <version>5.9.0</version>
    <scope>test</scope>
</dependency>
```

**目錄結構：**
```
ATP_re/
├── src/
│   ├── main/
│   │   └── java/
│   │       ├── decoder_re/
│   │       ├── core_re/
│   │       └── ...
│   └── test/
│       └── java/
│           ├── decoder_re/
│           │   ├── RUDecoderTest.java
│           │   ├── MMIDecoderTest.java
│           │   └── BTMDecoderTest.java
│           ├── core_re/
│           │   ├── ATPTaskTest.java
│           │   └── ...
│           └── ...
├── test_data/
│   ├── ru/
│   │   ├── sample_v1.6.RU
│   │   ├── sample_v1.7.RU
│   │   └── sample_v1.8.RU
│   └── expected/
│       ├── sample_v1.6_expected.json
│       └── ...
└── test_results/
```

#### Python 測試環境（未來）

**所需工具：**
```bash
# 安裝測試相依套件
pip install pytest pytest-cov pytest-mock

# 目錄結構
atp_re/
├── src/
│   └── atp_re/
│       ├── decoder/
│       ├── models/
│       └── ...
├── tests/
│   ├── unit/
│   │   ├── test_ru_decoder.py
│   │   ├── test_mmi_decoder.py
│   │   └── ...
│   ├── integration/
│   │   ├── test_database.py
│   │   └── ...
│   └── system/
│       └── test_e2e.py
└── pytest.ini
```

### 測試資料庫設定

**建立測試資料庫：**

```sql
-- 建立測試資料庫
CREATE DATABASE ATP_DB_TEST;

-- 使用相同的結構
-- 執行初始化腳本
USE ATP_DB_TEST;
SOURCE init.sql;

-- 插入測試資料
SOURCE test_data.sql;
```

**設定檔（test.properties）：**
```properties
db.host=localhost
db.port=1433
db.name=ATP_DB_TEST
db.user=atp_test_user
db.password=test_password
```

---

## 單元測試

### 測試命名規範

**Java（JUnit）：**
```java
// 測試類別命名：<ClassName>Test
public class RUDecoderTest {
    
    // 測試方法命名：test<MethodName>_<Scenario>_<ExpectedResult>
    @Test
    public void testDecodeFile_ValidRU_Success() {
        // 測試內容
    }
    
    @Test
    public void testDecodeFile_InvalidFormat_ThrowsException() {
        // 測試內容
    }
}
```

**Python（pytest）：**
```python
# 測試檔案命名：test_<module_name>.py
# 測試函式命名：test_<function_name>_<scenario>_<expected>

def test_decode_file_valid_ru_success():
    """測試解碼有效的 RU 檔案"""
    # 測試內容
    pass

def test_decode_file_invalid_format_raises_error():
    """測試解碼無效格式時拋出異常"""
    # 測試內容
    pass
```

### RU 解碼器單元測試範例

**Java：**
```java
package decoder_re;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.io.File;

public class RUDecoderTest {
    
    private RUDecoder decoder;
    private static final String TEST_DATA_PATH = "test_data/ru/";
    
    @BeforeEach
    public void setUp() {
        decoder = new RUDecoder();
    }
    
    @Test
    @DisplayName("測試解碼有效的 RU v1.8 檔案")
    public void testDecodeFile_ValidRUv18_Success() throws Exception {
        // Arrange
        String filePath = TEST_DATA_PATH + "sample_v1.8.RU";
        
        // Act
        ATPTask task = decoder.decodeFile(filePath);
        
        // Assert
        assertNotNull(task, "解碼結果不應為 null");
        assertEquals("1234", task.getTrainNumber(), "車次號碼應為 1234");
        assertTrue(task.getSpeedRecords().size() > 0, "應有速度記錄");
    }
    
    @Test
    @DisplayName("測試解碼不存在的檔案應拋出異常")
    public void testDecodeFile_FileNotFound_ThrowsException() {
        // Arrange
        String filePath = TEST_DATA_PATH + "nonexistent.RU";
        
        // Act & Assert
        assertThrows(
            DecoderException.class,
            () -> decoder.decodeFile(filePath),
            "應拋出 DecoderException"
        );
    }
    
    @Test
    @DisplayName("測試檢查碼驗證")
    public void testDecodeFile_InvalidChecksum_ThrowsException() {
        // Arrange
        String filePath = TEST_DATA_PATH + "corrupted.RU";
        decoder.setValidateChecksum(true);
        
        // Act & Assert
        assertThrows(
            ChecksumException.class,
            () -> decoder.decodeFile(filePath),
            "檢查碼錯誤應拋出 ChecksumException"
        );
    }
    
    @Test
    @DisplayName("測試速度記錄解碼正確性")
    public void testDecodeFile_SpeedRecords_Correct() throws Exception {
        // Arrange
        String filePath = TEST_DATA_PATH + "sample_v1.8.RU";
        
        // Act
        ATPTask task = decoder.decodeFile(filePath);
        SpeedRecord firstRecord = task.getSpeedRecords().get(0);
        
        // Assert
        assertEquals(0.0, firstRecord.getActualSpeed(), 0.1, "初始速度應為 0");
        assertTrue(firstRecord.getTimestamp() > 0, "時間戳記應大於 0");
    }
    
    @Test
    @DisplayName("測試批次解碼")
    public void testDecodeBatch_MultipleFiles_Success() throws Exception {
        // Arrange
        List<String> files = Arrays.asList(
            TEST_DATA_PATH + "sample1.RU",
            TEST_DATA_PATH + "sample2.RU",
            TEST_DATA_PATH + "sample3.RU"
        );
        
        // Act
        List<ATPTask> tasks = decoder.decodeBatch(files);
        
        // Assert
        assertEquals(3, tasks.size(), "應解碼 3 個檔案");
        for (ATPTask task : tasks) {
            assertNotNull(task, "每個任務都不應為 null");
        }
    }
}
```

**Python（未來）：**
```python
import pytest
from atp_re.decoder import RUDecoder
from atp_re.exceptions import DecoderException, ChecksumException

class TestRUDecoder:
    """RU 解碼器單元測試"""
    
    @pytest.fixture
    def decoder(self):
        """建立解碼器實例"""
        return RUDecoder()
    
    @pytest.fixture
    def test_data_path(self):
        """測試資料路徑"""
        return "test_data/ru/"
    
    def test_decode_file_valid_ru_v18_success(self, decoder, test_data_path):
        """測試解碼有效的 RU v1.8 檔案"""
        # Arrange
        file_path = test_data_path + "sample_v1.8.RU"
        
        # Act
        task = decoder.decode_file(file_path)
        
        # Assert
        assert task is not None, "解碼結果不應為 None"
        assert task.train_number == "1234", "車次號碼應為 1234"
        assert len(task.speed_records) > 0, "應有速度記錄"
    
    def test_decode_file_not_found_raises_exception(self, decoder, test_data_path):
        """測試解碼不存在的檔案應拋出異常"""
        # Arrange
        file_path = test_data_path + "nonexistent.RU"
        
        # Act & Assert
        with pytest.raises(DecoderException):
            decoder.decode_file(file_path)
    
    def test_decode_file_invalid_checksum_raises_exception(self, decoder, test_data_path):
        """測試檢查碼驗證"""
        # Arrange
        file_path = test_data_path + "corrupted.RU"
        decoder.validate_checksum = True
        
        # Act & Assert
        with pytest.raises(ChecksumException):
            decoder.decode_file(file_path)
    
    def test_decode_batch_multiple_files_success(self, decoder, test_data_path):
        """測試批次解碼"""
        # Arrange
        files = [
            test_data_path + "sample1.RU",
            test_data_path + "sample2.RU",
            test_data_path + "sample3.RU"
        ]
        
        # Act
        tasks = decoder.decode_batch(files)
        
        # Assert
        assert len(tasks) == 3, "應解碼 3 個檔案"
        for task in tasks:
            assert task is not None, "每個任務都不應為 None"
```

### 測試資料模型

**測試 ATPTask 類別：**
```java
public class ATPTaskTest {
    
    private ATPTask task;
    
    @BeforeEach
    public void setUp() {
        task = new ATPTask("T001", "1234");
        task.setStartTime(new Date(1701432000000L));
        task.setEndTime(new Date(1701435600000L));
    }
    
    @Test
    public void testGetDuration_ValidDates_ReturnsCorrectDuration() {
        // Act
        long duration = task.getDuration();
        
        // Assert
        assertEquals(3600000L, duration, "持續時間應為 1 小時（3600000 毫秒）");
    }
    
    @Test
    public void testAddSpeedRecord_AddRecord_IncreasesCount() {
        // Arrange
        SpeedRecord record = new SpeedRecord(System.currentTimeMillis(), 60.0);
        
        // Act
        task.addSpeedRecord(record);
        
        // Assert
        assertEquals(1, task.getSpeedRecords().size(), "應有 1 筆速度記錄");
    }
    
    @Test
    public void testGetMaxSpeed_MultipleRecords_ReturnsMax() {
        // Arrange
        task.addSpeedRecord(new SpeedRecord(0, 60.0));
        task.addSpeedRecord(new SpeedRecord(1000, 80.0));
        task.addSpeedRecord(new SpeedRecord(2000, 70.0));
        
        // Act
        double maxSpeed = task.getMaxSpeed();
        
        // Assert
        assertEquals(80.0, maxSpeed, 0.1, "最高速度應為 80.0");
    }
}
```

### Mock 物件使用

**使用 Mockito：**
```java
import static org.mockito.Mockito.*;

public class DataFeederTest {
    
    @Test
    public void testProcessData_WithMockedDatabase() {
        // Arrange
        Database mockDb = mock(Database.class);
        when(mockDb.isConnected()).thenReturn(true);
        when(mockDb.insert(any())).thenReturn(true);
        
        DataFeeder feeder = new DataFeeder(mockDb);
        ATPTask task = new ATPTask("T001", "1234");
        
        // Act
        boolean result = feeder.processData(task);
        
        // Assert
        assertTrue(result, "處理應該成功");
        verify(mockDb, times(1)).insert(task);
    }
}
```

---

## 整合測試

### 資料庫整合測試

**測試資料庫操作：**
```java
public class DatabaseIntegrationTest {
    
    private static Database database;
    
    @BeforeAll
    public static void setUpDatabase() throws Exception {
        // 使用測試資料庫
        database = new Database("test.properties");
        database.connect();
        
        // 初始化測試資料
        database.executeScript("test_data.sql");
    }
    
    @AfterAll
    public static void tearDownDatabase() throws Exception {
        // 清理測試資料
        database.executeScript("cleanup.sql");
        database.disconnect();
    }
    
    @Test
    public void testInsertTask_ValidTask_Success() throws Exception {
        // Arrange
        ATPTask task = new ATPTask("T001", "1234");
        task.setStartTime(new Date());
        
        // Act
        boolean result = database.insertTask(task);
        
        // Assert
        assertTrue(result, "插入應該成功");
        
        // Verify
        ATPTask retrieved = database.getTask("T001");
        assertNotNull(retrieved, "應能取回插入的任務");
        assertEquals("1234", retrieved.getTrainNumber());
    }
    
    @Test
    public void testQueryTasks_WithFilter_ReturnsFilteredResults() throws Exception {
        // Arrange
        Date startDate = new Date(1701432000000L);
        Date endDate = new Date(1701518400000L);
        
        // Act
        List<ATPTask> tasks = database.queryTasks(startDate, endDate);
        
        // Assert
        assertFalse(tasks.isEmpty(), "應該有查詢結果");
        for (ATPTask task : tasks) {
            assertTrue(task.getStartTime().after(startDate));
            assertTrue(task.getStartTime().before(endDate));
        }
    }
}
```

### 檔案系統整合測試

```java
public class FileSystemIntegrationTest {
    
    private static Path tempDir;
    
    @BeforeAll
    public static void setUp() throws Exception {
        tempDir = Files.createTempDirectory("atp_test");
    }
    
    @AfterAll
    public static void tearDown() throws Exception {
        Files.walk(tempDir)
            .sorted(Comparator.reverseOrder())
            .forEach(path -> {
                try {
                    Files.delete(path);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
    }
    
    @Test
    public void testSaveAndLoadTask_ValidTask_Success() throws Exception {
        // Arrange
        ATPTask task = createTestTask();
        Path filePath = tempDir.resolve("test_task.dat");
        
        // Act - Save
        TaskSerializer.save(task, filePath.toString());
        
        // Act - Load
        ATPTask loaded = TaskSerializer.load(filePath.toString());
        
        // Assert
        assertNotNull(loaded);
        assertEquals(task.getTaskId(), loaded.getTaskId());
        assertEquals(task.getTrainNumber(), loaded.getTrainNumber());
    }
}
```

### FTP 整合測試

```java
public class FTPIntegrationTest {
    
    private static FTPTestServer ftpServer;
    
    @BeforeAll
    public static void startFTPServer() throws Exception {
        ftpServer = new FTPTestServer(21);
        ftpServer.start();
    }
    
    @AfterAll
    public static void stopFTPServer() throws Exception {
        ftpServer.stop();
    }
    
    @Test
    public void testDownloadFile_ValidFile_Success() throws Exception {
        // Arrange
        FTPClient client = new FTPClient("localhost", 21, "test", "test");
        String remoteFile = "/test/sample.RU";
        String localFile = "temp/downloaded.RU";
        
        // Act
        boolean result = client.downloadFile(remoteFile, localFile);
        
        // Assert
        assertTrue(result, "下載應該成功");
        assertTrue(new File(localFile).exists(), "檔案應該存在");
    }
}
```

---

## 系統測試

### 端對端測試

**完整工作流程測試：**
```java
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class EndToEndTest {
    
    private static ATP_re application;
    
    @BeforeAll
    public static void setUp() {
        application = new ATP_re();
        application.initialize();
    }
    
    @Test
    @Order(1)
    @DisplayName("E2E - 開啟並解碼 RU 檔案")
    public void testOpenAndDecodeRUFile() throws Exception {
        // 開啟檔案
        String filePath = "test_data/ru/sample_v1.8.RU";
        application.openFile(filePath);
        
        // 驗證解碼結果
        ATPTask task = application.getCurrentTask();
        assertNotNull(task);
        assertEquals("1234", task.getTrainNumber());
    }
    
    @Test
    @Order(2)
    @DisplayName("E2E - 產生速度曲線圖")
    public void testGenerateSpeedChart() throws Exception {
        // 產生圖表
        Chart chart = application.generateSpeedChart();
        
        // 驗證圖表
        assertNotNull(chart);
        assertTrue(chart.getDataPoints().size() > 0);
    }
    
    @Test
    @Order(3)
    @DisplayName("E2E - 產生報表")
    public void testGenerateReport() throws Exception {
        // 產生報表
        String reportPath = "test_results/report.pdf";
        boolean result = application.generateReport(reportPath);
        
        // 驗證報表
        assertTrue(result);
        assertTrue(new File(reportPath).exists());
    }
    
    @Test
    @Order(4)
    @DisplayName("E2E - 儲存至資料庫")
    public void testSaveToDatabase() throws Exception {
        // 儲存至資料庫
        boolean result = application.saveToDatabase();
        
        // 驗證
        assertTrue(result);
        
        // 從資料庫讀取驗證
        ATPTask retrieved = application.loadFromDatabase(
            application.getCurrentTask().getTaskId()
        );
        assertNotNull(retrieved);
    }
}
```

### 對照驗證測試

**與 Java 版本對照：**
```python
import pytest
import json

class TestValidation:
    """對照驗證測試 - Python vs Java"""
    
    def test_decode_validation_all_samples(self):
        """驗證所有範例檔案的解碼結果"""
        # 載入 Java 版本的預期結果
        with open('test_data/expected/java_results.json') as f:
            expected_results = json.load(f)
        
        # Python 解碼器
        decoder = RUDecoder()
        
        # 對每個測試檔案進行驗證
        for sample in expected_results:
            file_path = sample['file_path']
            expected = sample['result']
            
            # 解碼
            task = decoder.decode_file(file_path)
            
            # 驗證關鍵欄位
            assert task.train_number == expected['train_number']
            assert len(task.speed_records) == expected['speed_record_count']
            assert len(task.events) == expected['event_count']
            
            # 驗證速度記錄（取樣驗證）
            for i, expected_record in enumerate(expected['sample_speeds']):
                actual_record = task.speed_records[i]
                assert abs(actual_record.actual_speed - expected_record['speed']) < 0.1
                assert actual_record.timestamp == expected_record['timestamp']
```

---

## 效能測試

### 解碼效能測試

```java
public class PerformanceTest {
    
    @Test
    @Timeout(value = 5, unit = TimeUnit.SECONDS)
    public void testDecodePerformance_SmallFile_Under5Seconds() throws Exception {
        // Arrange
        RUDecoder decoder = new RUDecoder();
        String filePath = "test_data/ru/small_file.RU";  // 1 MB
        
        // Act
        long startTime = System.currentTimeMillis();
        ATPTask task = decoder.decodeFile(filePath);
        long endTime = System.currentTimeMillis();
        
        // Assert
        long duration = endTime - startTime;
        System.out.println("解碼時間: " + duration + " ms");
        assertTrue(duration < 5000, "解碼時間應小於 5 秒");
    }
    
    @Test
    public void testBatchDecodePerformance_100Files() throws Exception {
        // Arrange
        RUDecoder decoder = new RUDecoder();
        List<String> files = generateFileList(100);  // 100 個檔案
        
        // Act
        long startTime = System.currentTimeMillis();
        List<ATPTask> tasks = decoder.decodeBatch(files);
        long endTime = System.currentTimeMillis();
        
        // Assert
        long duration = endTime - startTime;
        double avgTime = duration / 100.0;
        
        System.out.println("總時間: " + duration + " ms");
        System.out.println("平均時間: " + avgTime + " ms/file");
        
        assertEquals(100, tasks.size());
        assertTrue(avgTime < 1000, "平均解碼時間應小於 1 秒");
    }
}
```

### 記憶體使用測試

```java
public class MemoryTest {
    
    @Test
    public void testMemoryUsage_LargeFile() throws Exception {
        // 取得初始記憶體使用
        Runtime runtime = Runtime.getRuntime();
        runtime.gc();  // 強制垃圾回收
        long memBefore = runtime.totalMemory() - runtime.freeMemory();
        
        // 解碼大檔案
        RUDecoder decoder = new RUDecoder();
        ATPTask task = decoder.decodeFile("test_data/ru/large_file.RU");  // 100 MB
        
        // 取得解碼後記憶體使用
        long memAfter = runtime.totalMemory() - runtime.freeMemory();
        long memUsed = (memAfter - memBefore) / 1024 / 1024;  // MB
        
        System.out.println("記憶體使用: " + memUsed + " MB");
        
        // 驗證記憶體使用合理
        assertTrue(memUsed < 500, "記憶體使用應小於 500 MB");
    }
}
```

### 壓力測試

```java
public class StressTest {
    
    @Test
    public void testConcurrentDecoding_10Threads() throws Exception {
        // Arrange
        int threadCount = 10;
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        List<Future<ATPTask>> futures = new ArrayList<>();
        
        // Act
        for (int i = 0; i < threadCount; i++) {
            final int index = i;
            Future<ATPTask> future = executor.submit(() -> {
                RUDecoder decoder = new RUDecoder();
                return decoder.decodeFile("test_data/ru/sample" + index + ".RU");
            });
            futures.add(future);
        }
        
        // 等待所有任務完成
        List<ATPTask> results = new ArrayList<>();
        for (Future<ATPTask> future : futures) {
            results.add(future.get(30, TimeUnit.SECONDS));
        }
        
        // Assert
        assertEquals(threadCount, results.size());
        for (ATPTask task : results) {
            assertNotNull(task);
        }
        
        executor.shutdown();
    }
}
```

---

## 測試資料準備

### 測試資料分類

#### 1. 正常資料
- `sample_v1.6.RU` - RU 1.6 版本標準檔案
- `sample_v1.7.RU` - RU 1.7 版本標準檔案
- `sample_v1.8.RU` - RU 1.8 版本標準檔案
- `sample_mmi_v3.2.MMI` - MMI 3.2 版本標準檔案

#### 2. 邊界資料
- `empty.RU` - 空檔案
- `minimal.RU` - 最小有效檔案
- `maximal.RU` - 最大檔案（接近大小限制）
- `zero_speed.RU` - 全程速度為 0

#### 3. 異常資料
- `corrupted.RU` - 損毀的檔案（檢查碼錯誤）
- `invalid_format.RU` - 格式錯誤
- `truncated.RU` - 不完整檔案
- `wrong_extension.txt` - 錯誤副檔名

#### 4. 特殊場景
- `overspeed.RU` - 包含超速事件
- `emergency_brake.RU` - 包含緊急煞車
- `multiple_stations.RU` - 多站停靠
- `long_distance.RU` - 長距離行駛

### 測試資料產生腳本

**generate_test_data.py:**
```python
#!/usr/bin/env python3
"""產生測試資料"""

import struct
import random
from datetime import datetime, timedelta

def generate_ru_file(filename, config):
    """產生 RU 測試檔案"""
    with open(filename, 'wb') as f:
        # 檔案標頭
        f.write(b'RULOG')  # 簽名
        f.write(struct.pack('>H', 0x0108))  # 版本 1.8
        
        # 任務資訊
        train_number = config.get('train_number', '1234')
        f.write(train_number.encode('ascii').ljust(8, b'\x00'))
        
        # 速度記錄
        start_time = int(datetime.now().timestamp() * 1000)
        for i in range(config.get('record_count', 1000)):
            timestamp = start_time + i * 1000
            speed = config.get('speed', 60.0) + random.uniform(-10, 10)
            
            f.write(struct.pack('>Q', timestamp))  # 時間戳記
            f.write(struct.pack('>f', speed))  # 速度
            f.write(struct.pack('>f', speed + 10))  # 目標速度
        
        # 檔案結尾
        # 計算並寫入檢查碼
        # ...

# 產生標準測試檔案
configs = [
    {'filename': 'sample_normal.RU', 'train_number': '1234', 'record_count': 1000},
    {'filename': 'sample_overspeed.RU', 'train_number': '1235', 'speed': 120.0},
    {'filename': 'sample_minimal.RU', 'train_number': '1236', 'record_count': 10},
]

for config in configs:
    filename = config.pop('filename')
    generate_ru_file(f'test_data/ru/{filename}', config)
    print(f'Generated: {filename}')
```

### 測試資料管理

**測試資料清單（test_data_manifest.json）：**
```json
{
  "version": "1.0",
  "test_files": [
    {
      "name": "sample_v1.8.RU",
      "type": "normal",
      "version": "1.8",
      "size": 1048576,
      "md5": "a1b2c3d4e5f6...",
      "expected": {
        "train_number": "1234",
        "speed_record_count": 1000,
        "event_count": 5,
        "max_speed": 120.0
      }
    }
  ]
}
```

---

## 測試自動化

### 持續整合（CI）設定

**GitHub Actions (.github/workflows/test.yml):**
```yaml
name: ATP_re Tests

on:
  push:
    branches: [ main, develop ]
  pull_request:
    branches: [ main ]

jobs:
  test:
    runs-on: ubuntu-latest
    
    steps:
    - uses: actions/checkout@v3
    
    - name: Set up JDK 8
      uses: actions/setup-java@v3
      with:
        java-version: '8'
        distribution: 'adopt'
    
    - name: Run tests
      run: |
        mvn clean test
    
    - name: Generate coverage report
      run: |
        mvn jacoco:report
    
    - name: Upload coverage to Codecov
      uses: codecov/codecov-action@v3
      with:
        files: ./target/site/jacoco/jacoco.xml
    
    - name: Archive test results
      if: always()
      uses: actions/upload-artifact@v3
      with:
        name: test-results
        path: target/surefire-reports/
```

### 測試執行腳本

**run_tests.sh:**
```bash
#!/bin/bash
# ATP_re 測試執行腳本

echo "=========================================="
echo "ATP_re 自動化測試"
echo "=========================================="
echo

# 設定變數
TEST_RESULTS_DIR="test_results/$(date +%Y%m%d_%H%M%S)"
mkdir -p "$TEST_RESULTS_DIR"

# 1. 單元測試
echo "執行單元測試..."
mvn test -Dtest=*Test
UNIT_TEST_RESULT=$?

# 2. 整合測試
echo "執行整合測試..."
mvn test -Dtest=*IntegrationTest
INTEGRATION_TEST_RESULT=$?

# 3. 系統測試
echo "執行系統測試..."
mvn test -Dtest=*E2ETest
SYSTEM_TEST_RESULT=$?

# 4. 產生報告
echo "產生測試報告..."
mvn surefire-report:report
mvn jacoco:report

# 複製報告到結果目錄
cp -r target/site/surefire-report.html "$TEST_RESULTS_DIR/"
cp -r target/site/jacoco "$TEST_RESULTS_DIR/"

# 5. 顯示摘要
echo
echo "=========================================="
echo "測試結果摘要"
echo "=========================================="
echo "單元測試: $([ $UNIT_TEST_RESULT -eq 0 ] && echo '✓ 通過' || echo '✗ 失敗')"
echo "整合測試: $([ $INTEGRATION_TEST_RESULT -eq 0 ] && echo '✓ 通過' || echo '✗ 失敗')"
echo "系統測試: $([ $SYSTEM_TEST_RESULT -eq 0 ] && echo '✓ 通過' || echo '✗ 失敗')"
echo
echo "詳細報告: $TEST_RESULTS_DIR"
echo "=========================================="

# 退出碼
if [ $UNIT_TEST_RESULT -ne 0 ] || [ $INTEGRATION_TEST_RESULT -ne 0 ] || [ $SYSTEM_TEST_RESULT -ne 0 ]; then
    exit 1
fi

exit 0
```

---

## 測試報告

### 測試覆蓋率報告

**使用 JaCoCo 產生覆蓋率報告：**

**pom.xml:**
```xml
<plugin>
    <groupId>org.jacoco</groupId>
    <artifactId>jacoco-maven-plugin</artifactId>
    <version>0.8.10</version>
    <executions>
        <execution>
            <goals>
                <goal>prepare-agent</goal>
            </goals>
        </execution>
        <execution>
            <id>report</id>
            <phase>test</phase>
            <goals>
                <goal>report</goal>
            </goals>
        </execution>
    </executions>
</plugin>
```

**執行並查看報告：**
```bash
# 執行測試並產生覆蓋率報告
mvn clean test jacoco:report

# 查看報告
open target/site/jacoco/index.html
```

### 測試結果報告範本

**test_report_template.md:**
```markdown
# ATP_re 測試報告

**測試日期：** 2025-11-09  
**測試版本：** v1.0  
**測試人員：** QA Team

## 測試摘要

| 測試類型 | 執行數 | 通過數 | 失敗數 | 通過率 |
|---------|--------|--------|--------|--------|
| 單元測試 | 150 | 148 | 2 | 98.7% |
| 整合測試 | 45 | 44 | 1 | 97.8% |
| 系統測試 | 20 | 20 | 0 | 100% |
| **總計** | **215** | **212** | **3** | **98.6%** |

## 程式碼覆蓋率

| 模組 | 行覆蓋率 | 分支覆蓋率 |
|------|----------|-----------|
| decoder_re | 95.2% | 91.5% |
| core_re | 98.5% | 96.3% |
| ui_re | 75.3% | 68.2% |
| **整體** | **85.6%** | **82.1%** |

## 失敗測試清單

### 1. RUDecoderTest.testDecodeCorruptedFile
- **狀態：** 失敗
- **原因：** 未正確處理損毀檔案異常
- **優先級：** 中
- **負責人：** Dev Team

### 2. DatabaseTest.testConnectionPool
- **狀態：** 失敗
- **原因：** 連線池設定錯誤
- **優先級：** 高
- **負責人：** Dev Team

## 效能測試結果

| 測試項目 | 目標值 | 實際值 | 狀態 |
|---------|--------|--------|------|
| 小檔案解碼（1MB） | < 5s | 2.3s | ✓ |
| 大檔案解碼（100MB） | < 60s | 45.2s | ✓ |
| 批次處理（100檔） | < 300s | 245.8s | ✓ |
| 記憶體使用 | < 500MB | 320MB | ✓ |

## 建議

1. 修復失敗的測試案例
2. 提升 UI 模組的測試覆蓋率
3. 增加邊界條件測試
4. 建立自動化回歸測試套件

## 結論

整體測試通過率為 98.6%，程式碼覆蓋率達 85.6%，符合專案品質標準。
建議修復失敗的測試案例後進入下一階段。
```

---

## 附錄

### A. 測試工具清單

**Java 測試工具：**
- JUnit 5 - 單元測試框架
- Mockito - Mock 框架
- AssertJ - 流暢的斷言庫
- JaCoCo - 程式碼覆蓋率工具
- JMeter - 效能測試工具

**Python 測試工具（未來）：**
- pytest - 測試框架
- pytest-cov - 覆蓋率外掛
- mock - Mock 庫
- hypothesis - 屬性測試
- locust - 負載測試

### B. 測試檢查清單

**程式碼提交前：**
- [ ] 所有單元測試通過
- [ ] 新功能有對應測試
- [ ] 程式碼覆蓋率不降低
- [ ] 無編譯警告
- [ ] 程式碼審查完成

**發布前：**
- [ ] 所有測試通過（單元、整合、系統）
- [ ] 對照驗證測試通過
- [ ] 效能測試達標
- [ ] 安全測試完成
- [ ] 文件更新

---

**版權聲明**  
© 2025 ATP_re Project. All rights reserved.

**文件維護**  
如發現文件錯誤或需要補充，請提交 GitHub Issue 或 Pull Request。

---

*最後更新：2025-11-09*  
*文件版本：1.0*
