# ATP_re API 文件

**版本：** 1.0  
**更新日期：** 2025-11-09  
**適用系統：** ATP_re Java 版本

---

## 📖 目錄

1. [API 概述](#api-概述)
2. [核心模組 API](#核心模組-api)
3. [解碼器 API](#解碼器-api)
4. [資料模型 API](#資料模型-api)
5. [工具類 API](#工具類-api)
6. [使用範例](#使用範例)
7. [錯誤處理](#錯誤處理)

---

## API 概述

### 架構說明

ATP_re 系統採用分層架構，主要 API 包括：

```
┌─────────────────────────────────┐
│   Application Layer (UI)        │
├─────────────────────────────────┤
│   Business Logic Layer          │
│   - RU Decoder API              │
│   - MMI Decoder API             │
│   - Analysis API                │
├─────────────────────────────────┤
│   Data Model Layer              │
│   - Core Data Models            │
│   - DAO (Data Access Objects)   │
├─────────────────────────────────┤
│   Utility Layer                 │
│   - File I/O                    │
│   - Data Conversion             │
│   - Logging                     │
└─────────────────────────────────┘
```

### 命名規範

- **類別名稱**：大駝峰式命名（PascalCase）
- **方法名稱**：小駝峰式命名（camelCase）
- **常數**：全大寫加底線（UPPER_SNAKE_CASE）
- **套件**：全小寫（lowercase）

---

## 核心模組 API

### 1. ATPTask 類別

ATP 任務主類別，代表一次完整的行車任務。

#### 類別定義

```java
package core_re;

public class ATPTask {
    // 欄位
    private String taskId;           // 任務 ID
    private String trainNumber;      // 車次號碼
    private Date startTime;          // 開始時間
    private Date endTime;            // 結束時間
    private List<SpeedRecord> speeds; // 速度記錄
    private List<Event> events;      // 事件記錄
    
    // 建構子
    public ATPTask(String taskId, String trainNumber);
    
    // 公開方法
    public String getTaskId();
    public void setTaskId(String taskId);
    public String getTrainNumber();
    public void setTrainNumber(String trainNumber);
    public Date getStartTime();
    public void setStartTime(Date startTime);
    public Date getEndTime();
    public void setEndTime(Date endTime);
    public List<SpeedRecord> getSpeedRecords();
    public void addSpeedRecord(SpeedRecord record);
    public List<Event> getEvents();
    public void addEvent(Event event);
    public double getMaxSpeed();
    public double getAverageSpeed();
    public long getDuration();
}
```

#### 方法說明

**getMaxSpeed()**
- **功能**：取得任務中的最高速度
- **回傳**：double - 最高速度（km/h）
- **範例**：
```java
ATPTask task = new ATPTask("T001", "1234");
double maxSpeed = task.getMaxSpeed();
System.out.println("最高速度: " + maxSpeed + " km/h");
```

**getAverageSpeed()**
- **功能**：計算平均速度
- **回傳**：double - 平均速度（km/h）
- **範例**：
```java
double avgSpeed = task.getAverageSpeed();
```

**getDuration()**
- **功能**：取得任務持續時間
- **回傳**：long - 持續時間（毫秒）
- **範例**：
```java
long duration = task.getDuration();
long minutes = duration / (1000 * 60);
```

### 2. Station 類別

車站資訊類別。

#### 類別定義

```java
package core_re;

public class Station {
    private String stationId;        // 車站 ID
    private String stationName;      // 車站名稱
    private double kilometer;        // 公里標
    private StationType type;        // 車站類型
    
    public Station(String stationId, String stationName, double kilometer);
    
    public String getStationId();
    public String getStationName();
    public double getKilometer();
    public StationType getType();
    public void setType(StationType type);
}
```

#### 車站類型枚舉

```java
public enum StationType {
    TERMINAL,      // 端點站
    INTERMEDIATE,  // 中間站
    DEPOT         // 機廠
}
```

### 3. User 類別

使用者（駕駛員）資訊類別。

```java
package core_re;

public class User {
    private String userId;           // 使用者 ID
    private String userName;         // 使用者姓名
    private String employeeId;       // 員工編號
    private UserRole role;           // 角色
    
    public User(String userId, String userName);
    
    public String getUserId();
    public String getUserName();
    public String getEmployeeId();
    public void setEmployeeId(String employeeId);
    public UserRole getRole();
    public void setRole(UserRole role);
}

public enum UserRole {
    DRIVER,        // 駕駛員
    OPERATOR,      // 操作員
    MANAGER,       // 管理者
    ADMINISTRATOR  // 系統管理員
}
```

---

## 解碼器 API

### 1. RUDecoder 類別

RU（Recording Unit）檔案解碼器。

#### 類別定義

```java
package decoder_re;

public class RUDecoder {
    // 建構子
    public RUDecoder();
    
    // 主要解碼方法
    public ATPTask decodeFile(String filePath) throws DecoderException;
    public ATPTask decodeFile(File file) throws DecoderException;
    public List<ATPTask> decodeBatch(List<String> filePaths);
    
    // 解碼選項設定
    public void setValidateChecksum(boolean validate);
    public void setStrictMode(boolean strict);
    public void setProgressListener(ProgressListener listener);
    
    // 解碼資訊查詢
    public String getDecoderVersion();
    public String getSupportedFileVersion();
}
```

#### 使用範例

**基本解碼**

```java
// 建立解碼器實例
RUDecoder decoder = new RUDecoder();

try {
    // 解碼單一檔案
    ATPTask task = decoder.decodeFile("data/ru/20231201_1234.RU");
    
    // 取得解碼結果
    System.out.println("車次: " + task.getTrainNumber());
    System.out.println("記錄數: " + task.getSpeedRecords().size());
    
} catch (DecoderException e) {
    System.err.println("解碼失敗: " + e.getMessage());
}
```

**批次解碼**

```java
RUDecoder decoder = new RUDecoder();

// 準備檔案清單
List<String> filePaths = Arrays.asList(
    "data/ru/file1.RU",
    "data/ru/file2.RU",
    "data/ru/file3.RU"
);

// 設定進度監聽器
decoder.setProgressListener(new ProgressListener() {
    @Override
    public void onProgress(int current, int total) {
        System.out.println("處理進度: " + current + "/" + total);
    }
});

// 批次解碼
List<ATPTask> tasks = decoder.decodeBatch(filePaths);
System.out.println("完成解碼 " + tasks.size() + " 個檔案");
```

**進階設定**

```java
RUDecoder decoder = new RUDecoder();

// 啟用嚴格模式（發現錯誤立即停止）
decoder.setStrictMode(true);

// 啟用檢查碼驗證
decoder.setValidateChecksum(true);

// 解碼
ATPTask task = decoder.decodeFile("data/ru/sample.RU");
```

### 2. MMIDecoder 類別

MMI（Man-Machine Interface）檔案解碼器。

#### 類別定義

```java
package decoder_re;

public class MMIDecoder {
    public MMIDecoder();
    
    // 解碼方法
    public MMIData decodeFile(String filePath) throws DecoderException;
    public MMIData decodeFile(File file) throws DecoderException;
    
    // 封包解析
    public List<PacketMMI> decodePackets(byte[] data);
    public PacketMMI decodePacket(byte[] data, int offset);
    
    // 設定
    public void setPacketFilter(PacketFilter filter);
    public void setDecodeMode(DecodeMode mode);
}
```

#### 使用範例

```java
MMIDecoder decoder = new MMIDecoder();

try {
    // 解碼 MMI 檔案
    MMIData mmiData = decoder.decodeFile("data/mmi/20231201.MMI");
    
    // 取得封包清單
    List<PacketMMI> packets = mmiData.getPackets();
    
    // 處理每個封包
    for (PacketMMI packet : packets) {
        System.out.println("封包類型: " + packet.getType());
        System.out.println("時間戳記: " + packet.getTimestamp());
        System.out.println("資料: " + packet.getData());
    }
    
} catch (DecoderException e) {
    System.err.println("解碼失敗: " + e.getMessage());
}
```

**封包篩選**

```java
MMIDecoder decoder = new MMIDecoder();

// 設定封包篩選器，僅解析特定類型
decoder.setPacketFilter(new PacketFilter() {
    @Override
    public boolean accept(int packetType) {
        // 僅接受速度和事件封包
        return packetType == PacketMMI.TYPE_SPEED || 
               packetType == PacketMMI.TYPE_EVENT;
    }
});

MMIData mmiData = decoder.decodeFile("data/mmi/sample.MMI");
```

### 3. BTMDecoder 類別

BTM（Balise Transmission Module）解碼器。

#### 類別定義

```java
package decoder_re;

public class BTMDecoder {
    public BTMDecoder();
    
    // BTM 訊息解碼
    public BTMMessage decode(byte[] data) throws DecoderException;
    public List<BTMMessage> decodeTelegram(byte[] telegram);
    
    // Balise 資訊解析
    public BaliseInfo parseBaliseInfo(BTMMessage message);
}
```

#### 使用範例

```java
BTMDecoder btmDecoder = new BTMDecoder();

// 從 RU 資料中取得 BTM 原始資料
byte[] btmData = ruRecord.getBTMData();

try {
    // 解碼 BTM 訊息
    BTMMessage message = btmDecoder.decode(btmData);
    
    // 取得 Balise 資訊
    BaliseInfo info = btmDecoder.parseBaliseInfo(message);
    
    System.out.println("Balise ID: " + info.getBaliseId());
    System.out.println("位置: " + info.getPosition());
    System.out.println("訊息內容: " + info.getMessage());
    
} catch (DecoderException e) {
    System.err.println("BTM 解碼失敗: " + e.getMessage());
}
```

---

## 資料模型 API

### 1. SpeedRecord 類別

速度記錄資料模型。

```java
package core_re;

public class SpeedRecord {
    private long timestamp;          // 時間戳記（毫秒）
    private double actualSpeed;      // 實際速度（km/h）
    private double targetSpeed;      // 目標速度（km/h）
    private double permittedSpeed;   // 允許速度（km/h）
    private double distance;         // 距離（公尺）
    private double kilometer;        // 公里標
    
    // 建構子
    public SpeedRecord(long timestamp, double actualSpeed);
    
    // Getters & Setters
    public long getTimestamp();
    public void setTimestamp(long timestamp);
    public double getActualSpeed();
    public void setActualSpeed(double speed);
    public double getTargetSpeed();
    public void setTargetSpeed(double speed);
    public double getPermittedSpeed();
    public void setPermittedSpeed(double speed);
    public double getDistance();
    public void setDistance(double distance);
    public double getKilometer();
    public void setKilometer(double kilometer);
    
    // 工具方法
    public Date getDate();
    public boolean isOverSpeed();
    public double getSpeedDifference();
}
```

### 2. Event 類別

事件記錄資料模型。

```java
package core_re;

public class Event {
    private String eventId;          // 事件 ID
    private EventType type;          // 事件類型
    private long timestamp;          // 發生時間
    private EventSeverity severity;  // 嚴重程度
    private String description;      // 描述
    private double kilometer;        // 發生位置
    private Map<String, Object> data; // 附加資料
    
    // 建構子
    public Event(String eventId, EventType type, long timestamp);
    
    // Getters & Setters
    public String getEventId();
    public EventType getType();
    public long getTimestamp();
    public EventSeverity getSeverity();
    public void setSeverity(EventSeverity severity);
    public String getDescription();
    public void setDescription(String description);
    public double getKilometer();
    public void setKilometer(double kilometer);
    
    // 附加資料處理
    public void setData(String key, Object value);
    public Object getData(String key);
    public Map<String, Object> getAllData();
}

// 事件類型枚舉
public enum EventType {
    ATP_SERVICE_BRAKE,    // ATP 服務煞車
    ATP_EMERGENCY_BRAKE,  // ATP 緊急煞車
    OVERSPEED_WARNING,    // 超速警告
    SPAD_WARNING,         // SPAD 警告
    BTM_READ_FAILURE,     // BTM 讀取失敗
    SYSTEM_FAULT,         // 系統故障
    MODE_CHANGE,          // 模式變更
    BUTTON_PRESS          // 按鈕按下
}

// 嚴重程度枚舉
public enum EventSeverity {
    CRITICAL,    // 極高
    HIGH,        // 高
    MEDIUM,      // 中
    LOW,         // 低
    INFO         // 資訊
}
```

### 3. BTMMessage 類別

BTM 訊息資料模型。

```java
package decoder_re;

public class BTMMessage {
    private int baliseId;            // 應答器 ID
    private long timestamp;          // 讀取時間
    private double position;         // 位置（公尺）
    private byte[] rawData;          // 原始資料
    private List<Telegram> telegrams; // 電報清單
    
    public BTMMessage(int baliseId, long timestamp);
    
    public int getBaliseId();
    public long getTimestamp();
    public double getPosition();
    public void setPosition(double position);
    public byte[] getRawData();
    public void setRawData(byte[] data);
    public List<Telegram> getTelegrams();
    public void addTelegram(Telegram telegram);
}
```

---

## 工具類 API

### 1. FileUtils 類別

檔案處理工具類別。

```java
package Tools_re;

public class FileUtils {
    // 檔案讀取
    public static byte[] readFileToBytes(String filePath) throws IOException;
    public static String readFileToString(String filePath) throws IOException;
    public static List<String> readLines(String filePath) throws IOException;
    
    // 檔案寫入
    public static void writeBytes(String filePath, byte[] data) throws IOException;
    public static void writeString(String filePath, String content) throws IOException;
    public static void writeLines(String filePath, List<String> lines) throws IOException;
    
    // 檔案操作
    public static boolean fileExists(String filePath);
    public static boolean deleteFile(String filePath);
    public static boolean copyFile(String source, String dest) throws IOException;
    public static boolean moveFile(String source, String dest) throws IOException;
    
    // 目錄操作
    public static boolean createDirectory(String dirPath);
    public static boolean deleteDirectory(String dirPath);
    public static List<String> listFiles(String dirPath, String extension);
}
```

#### 使用範例

```java
import Tools_re.FileUtils;

// 讀取檔案為位元組陣列
byte[] data = FileUtils.readFileToBytes("data/ru/sample.RU");

// 列出目錄中的所有 RU 檔案
List<String> ruFiles = FileUtils.listFiles("data/ru/", ".RU");
for (String file : ruFiles) {
    System.out.println("找到檔案: " + file);
}

// 複製檔案
boolean success = FileUtils.copyFile(
    "data/ru/original.RU",
    "backup/ru/original_backup.RU"
);
```

### 2. DataConverter 類別

資料轉換工具類別。

```java
package Tools_re;

public class DataConverter {
    // 位元組轉換
    public static int bytesToInt(byte[] bytes, int offset);
    public static long bytesToLong(byte[] bytes, int offset);
    public static short bytesToShort(byte[] bytes, int offset);
    public static float bytesToFloat(byte[] bytes, int offset);
    public static double bytesToDouble(byte[] bytes, int offset);
    
    // 整數轉位元組
    public static byte[] intToBytes(int value);
    public static byte[] longToBytes(long value);
    public static byte[] shortToBytes(short value);
    
    // 字串轉換
    public static String bytesToHexString(byte[] bytes);
    public static byte[] hexStringToBytes(String hex);
    
    // 日期時間轉換
    public static Date timestampToDate(long timestamp);
    public static long dateToTimestamp(Date date);
    public static String formatDateTime(Date date);
    public static Date parseDateTime(String dateStr);
}
```

#### 使用範例

```java
import Tools_re.DataConverter;

// 位元組轉整數（Big-Endian）
byte[] data = new byte[]{0x00, 0x00, 0x01, 0x2C}; // 300
int value = DataConverter.bytesToInt(data, 0);
System.out.println("值: " + value); // 輸出: 300

// 位元組轉十六進位字串
String hex = DataConverter.bytesToHexString(data);
System.out.println("十六進位: " + hex); // 輸出: 0000012C

// 時間戳記轉日期
long timestamp = 1701432000000L;
Date date = DataConverter.timestampToDate(timestamp);
String formatted = DataConverter.formatDateTime(date);
System.out.println("日期: " + formatted);
```

### 3. Logger 類別

日誌記錄工具類別。

```java
package Tools_re;

public class Logger {
    // 日誌等級
    public enum Level {
        DEBUG, INFO, WARNING, ERROR, FATAL
    }
    
    // 取得 Logger 實例
    public static Logger getLogger(Class<?> clazz);
    public static Logger getLogger(String name);
    
    // 日誌方法
    public void debug(String message);
    public void info(String message);
    public void warning(String message);
    public void error(String message);
    public void error(String message, Throwable throwable);
    public void fatal(String message);
    
    // 設定
    public void setLevel(Level level);
    public void setLogFile(String filePath);
}
```

#### 使用範例

```java
import Tools_re.Logger;

public class MyDecoder {
    private static final Logger logger = Logger.getLogger(MyDecoder.class);
    
    public void decode(String filePath) {
        logger.info("開始解碼檔案: " + filePath);
        
        try {
            // 解碼邏輯
            logger.debug("讀取檔案標頭");
            // ...
            
            logger.info("解碼完成");
            
        } catch (Exception e) {
            logger.error("解碼失敗", e);
        }
    }
}
```

---

## 使用範例

### 完整範例：解碼並分析 RU 檔案

```java
import core_re.*;
import decoder_re.*;
import Tools_re.*;

public class AnalysisExample {
    public static void main(String[] args) {
        // 設定日誌
        Logger logger = Logger.getLogger(AnalysisExample.class);
        
        try {
            // 1. 建立解碼器
            logger.info("建立 RU 解碼器");
            RUDecoder decoder = new RUDecoder();
            decoder.setValidateChecksum(true);
            
            // 2. 解碼檔案
            logger.info("開始解碼檔案");
            String filePath = "data/ru/20231201_1234.RU";
            ATPTask task = decoder.decodeFile(filePath);
            
            // 3. 基本資訊
            System.out.println("=== 任務資訊 ===");
            System.out.println("車次: " + task.getTrainNumber());
            System.out.println("開始時間: " + task.getStartTime());
            System.out.println("結束時間: " + task.getEndTime());
            
            // 4. 速度分析
            System.out.println("\n=== 速度分析 ===");
            System.out.println("最高速度: " + task.getMaxSpeed() + " km/h");
            System.out.println("平均速度: " + task.getAverageSpeed() + " km/h");
            System.out.println("速度記錄數: " + task.getSpeedRecords().size());
            
            // 5. 事件分析
            System.out.println("\n=== 事件分析 ===");
            List<Event> events = task.getEvents();
            System.out.println("總事件數: " + events.size());
            
            // 統計各類事件
            Map<EventType, Integer> eventCount = new HashMap<>();
            for (Event event : events) {
                EventType type = event.getType();
                eventCount.put(type, eventCount.getOrDefault(type, 0) + 1);
            }
            
            // 顯示事件統計
            for (Map.Entry<EventType, Integer> entry : eventCount.entrySet()) {
                System.out.println(entry.getKey() + ": " + entry.getValue());
            }
            
            // 6. 檢查超速
            System.out.println("\n=== 超速檢查 ===");
            List<SpeedRecord> overspeedRecords = new ArrayList<>();
            for (SpeedRecord record : task.getSpeedRecords()) {
                if (record.isOverSpeed()) {
                    overspeedRecords.add(record);
                }
            }
            System.out.println("超速次數: " + overspeedRecords.size());
            
            // 7. 產生報表
            logger.info("產生報表");
            Report report = new Report(task);
            report.generate("reports/analysis_" + task.getTaskId() + ".pdf");
            
            logger.info("分析完成");
            
        } catch (DecoderException e) {
            logger.error("解碼失敗", e);
        } catch (Exception e) {
            logger.error("處理失敗", e);
        }
    }
}
```

### 範例：批次處理與進度追蹤

```java
import decoder_re.*;
import java.util.*;

public class BatchProcessingExample {
    public static void main(String[] args) {
        // 建立解碼器
        RUDecoder decoder = new RUDecoder();
        
        // 設定進度監聽器
        decoder.setProgressListener(new ProgressListener() {
            @Override
            public void onProgress(int current, int total) {
                int percentage = (current * 100) / total;
                System.out.print("\r進度: " + percentage + "% [" + current + "/" + total + "]");
            }
            
            @Override
            public void onComplete() {
                System.out.println("\n批次處理完成！");
            }
            
            @Override
            public void onError(String filePath, Exception e) {
                System.err.println("\n處理失敗: " + filePath + " - " + e.getMessage());
            }
        });
        
        // 取得所有 RU 檔案
        List<String> files = FileUtils.listFiles("data/ru/", ".RU");
        System.out.println("找到 " + files.size() + " 個檔案");
        
        // 批次解碼
        List<ATPTask> tasks = decoder.decodeBatch(files);
        
        // 彙總分析
        System.out.println("\n=== 批次分析結果 ===");
        int totalEvents = 0;
        double totalDistance = 0.0;
        
        for (ATPTask task : tasks) {
            totalEvents += task.getEvents().size();
            totalDistance += task.getTotalDistance();
        }
        
        System.out.println("處理任務數: " + tasks.size());
        System.out.println("總事件數: " + totalEvents);
        System.out.println("總行駛距離: " + totalDistance + " km");
    }
}
```

---

## 錯誤處理

### 異常類別層次

```
Exception
└── DecoderException (解碼相關異常)
    ├── FileFormatException (檔案格式錯誤)
    ├── ChecksumException (檢查碼錯誤)
    ├── UnsupportedVersionException (不支援的版本)
    └── CorruptedDataException (資料損毀)
```

### DecoderException

所有解碼相關異常的基礎類別。

```java
package decoder_re;

public class DecoderException extends Exception {
    private String filePath;         // 發生錯誤的檔案路徑
    private int errorCode;           // 錯誤代碼
    private long errorPosition;      // 錯誤位置（位元組偏移）
    
    public DecoderException(String message);
    public DecoderException(String message, Throwable cause);
    
    public String getFilePath();
    public void setFilePath(String filePath);
    public int getErrorCode();
    public void setErrorCode(int errorCode);
    public long getErrorPosition();
    public void setErrorPosition(long position);
}
```

### 錯誤處理範例

```java
RUDecoder decoder = new RUDecoder();

try {
    ATPTask task = decoder.decodeFile("data/ru/sample.RU");
    
} catch (FileFormatException e) {
    System.err.println("檔案格式錯誤: " + e.getMessage());
    System.err.println("錯誤位置: " + e.getErrorPosition());
    
} catch (ChecksumException e) {
    System.err.println("檢查碼驗證失敗");
    System.err.println("可能的原因: 檔案已損毀或傳輸錯誤");
    
} catch (UnsupportedVersionException e) {
    System.err.println("不支援的檔案版本: " + e.getMessage());
    System.err.println("請更新解碼器或轉換檔案格式");
    
} catch (DecoderException e) {
    System.err.println("解碼失敗: " + e.getMessage());
    e.printStackTrace();
}
```

### 錯誤代碼對照表

| 錯誤代碼 | 常數名稱 | 說明 |
|---------|---------|------|
| 1001 | ERR_FILE_NOT_FOUND | 檔案不存在 |
| 1002 | ERR_FILE_FORMAT | 檔案格式錯誤 |
| 1003 | ERR_CHECKSUM | 檢查碼錯誤 |
| 1004 | ERR_UNSUPPORTED_VERSION | 不支援的版本 |
| 1005 | ERR_CORRUPTED_DATA | 資料損毀 |
| 1006 | ERR_INSUFFICIENT_DATA | 資料不完整 |
| 1007 | ERR_INVALID_PARAMETER | 無效的參數 |

---

## 最佳實踐

### 1. 資源管理

```java
// 使用 try-with-resources 自動關閉資源
try (FileInputStream fis = new FileInputStream(filePath);
     BufferedInputStream bis = new BufferedInputStream(fis)) {
    
    byte[] data = new byte[1024];
    int bytesRead = bis.read(data);
    // 處理資料...
    
} catch (IOException e) {
    logger.error("讀取檔案失敗", e);
}
```

### 2. 效能優化

```java
// 使用批次處理提升效能
List<String> files = getFileList();
int batchSize = 10;

for (int i = 0; i < files.size(); i += batchSize) {
    int end = Math.min(i + batchSize, files.size());
    List<String> batch = files.subList(i, end);
    
    // 平行處理批次
    List<ATPTask> tasks = decoder.decodeBatch(batch);
    // 處理結果...
}
```

### 3. 錯誤恢復

```java
RUDecoder decoder = new RUDecoder();
decoder.setStrictMode(false); // 非嚴格模式，遇錯繼續

List<ATPTask> tasks = new ArrayList<>();
List<String> failedFiles = new ArrayList<>();

for (String filePath : fileList) {
    try {
        ATPTask task = decoder.decodeFile(filePath);
        tasks.add(task);
    } catch (DecoderException e) {
        logger.warning("跳過錯誤檔案: " + filePath);
        failedFiles.add(filePath);
    }
}

// 記錄失敗檔案
if (!failedFiles.isEmpty()) {
    FileUtils.writeLines("failed_files.txt", failedFiles);
}
```

---

## 版本相容性

### 支援的檔案版本

| 檔案類型 | 支援版本 | 解碼器類別 |
|---------|---------|-----------|
| RU | v1.6, v1.7, v1.8 | RUDecoder |
| MMI | v3.0, v3.1, v3.2 | MMIDecoder |
| BTM | v2.0+ | BTMDecoder |

### 版本檢查

```java
RUDecoder decoder = new RUDecoder();
String version = decoder.getSupportedFileVersion();
System.out.println("支援的 RU 版本: " + version);

// 檢查檔案版本
String fileVersion = RUDecoder.getFileVersion("data/ru/sample.RU");
if (!decoder.isVersionSupported(fileVersion)) {
    System.err.println("警告: 檔案版本 " + fileVersion + " 可能不被完全支援");
}
```

---

## 附錄

### A. 常用常數定義

```java
// 封包類型
public static final int PACKET_TYPE_SPEED = 0x01;
public static final int PACKET_TYPE_EVENT = 0x02;
public static final int PACKET_TYPE_BTM = 0x03;
public static final int PACKET_TYPE_POSITION = 0x04;

// 檔案簽名
public static final byte[] RU_FILE_SIGNATURE = {0x52, 0x55, 0x4C, 0x4F, 0x47};
public static final byte[] MMI_FILE_SIGNATURE = {0x4D, 0x4D, 0x49, 0x44, 0x41, 0x54};

// 限制值
public static final int MAX_SPEED = 300;          // 最大速度 (km/h)
public static final int MAX_PACKET_SIZE = 65536;  // 最大封包大小 (bytes)
public static final int MAX_RECORDS = 1000000;    // 最大記錄數
```

### B. 工具方法索引

快速查找常用工具方法：

**檔案操作**
- `FileUtils.readFileToBytes()` - 讀取檔案為位元組陣列
- `FileUtils.writeBytes()` - 寫入位元組陣列到檔案
- `FileUtils.listFiles()` - 列出目錄中的檔案

**資料轉換**
- `DataConverter.bytesToInt()` - 位元組轉整數
- `DataConverter.bytesToHexString()` - 位元組轉十六進位字串
- `DataConverter.timestampToDate()` - 時間戳記轉日期

**日誌記錄**
- `Logger.getLogger()` - 取得 Logger 實例
- `Logger.info()` - 記錄資訊
- `Logger.error()` - 記錄錯誤

---

**版權聲明**  
© 2025 ATP_re Project. All rights reserved.

**API 文件維護**  
如發現 API 文件錯誤或需要補充，請提交 GitHub Issue 或 Pull Request。

---

*最後更新：2025-11-09*  
*文件版本：1.0*
