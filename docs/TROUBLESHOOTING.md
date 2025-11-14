# ATP_re 故障排除指南

**版本：** 1.0  
**更新日期：** 2025-11-09  
**適用系統：** ATP_re Java 版本

---

## 📖 目錄

1. [故障排除概述](#故障排除概述)
2. [啟動問題](#啟動問題)
3. [解碼問題](#解碼問題)
4. [效能問題](#效能問題)
5. [資料庫問題](#資料庫問題)
6. [UI 顯示問題](#ui-顯示問題)
7. [網路連線問題](#網路連線問題)
8. [日誌分析](#日誌分析)
9. [錯誤代碼對照](#錯誤代碼對照)
10. [診斷工具](#診斷工具)
11. [聯絡技術支援](#聯絡技術支援)

---

## 故障排除概述

### 問題診斷流程

```
┌─────────────────────┐
│   發現問題          │
└──────────┬──────────┘
           │
           ▼
┌─────────────────────┐
│ 1. 記錄錯誤訊息     │
│ 2. 檢查日誌檔案     │
│ 3. 確認系統狀態     │
└──────────┬──────────┘
           │
           ▼
┌─────────────────────┐
│ 查找本指南相關章節  │
└──────────┬──────────┘
           │
           ▼
┌─────────────────────┐
│   套用解決方案      │
└──────────┬──────────┘
           │
           ▼
     ┌─────┴─────┐
     │ 問題解決？ │
     └─────┬─────┘
           │
      ┌────┴────┐
      │ 是      │ 否
      ▼         ▼
  ┌─────┐   ┌──────────┐
  │完成 │   │聯絡技術  │
  └─────┘   │支援      │
            └──────────┘
```

### 資訊收集檢查清單

在報告問題或尋求協助時，請準備以下資訊：

- [ ] 作業系統版本
- [ ] Java 版本 (`java -version`)
- [ ] ATP_re 版本
- [ ] 錯誤訊息（完整文字或截圖）
- [ ] 日誌檔案（logs/atp.log）
- [ ] 重現步驟
- [ ] 問題發生時間
- [ ] 最近的系統變更

---

## 啟動問題

### 問題 1：應用程式無法啟動 - 找不到 Java

**症狀：**
```
'java' 不是內部或外部命令、可執行程式或批次檔。
```

**原因：**
- Java 未安裝
- Java 未加入 PATH 環境變數

**解決方案：**

**Windows:**
```cmd
# 檢查 Java 是否安裝
java -version

# 如果未安裝，下載並安裝 JRE 8+
# https://www.java.com/

# 設定 JAVA_HOME 環境變數
setx JAVA_HOME "C:\Program Files\Java\jre1.8.0_211"
setx PATH "%PATH%;%JAVA_HOME%\bin"

# 重新開啟命令提示字元並測試
java -version
```

**macOS:**
```bash
# 檢查 Java 是否安裝
java -version

# 安裝 Java（使用 Homebrew）
brew install openjdk@8

# 設定環境變數（加入 ~/.bash_profile 或 ~/.zshrc）
export JAVA_HOME=$(/usr/libexec/java_home -v 1.8)
export PATH=$JAVA_HOME/bin:$PATH

# 套用設定
source ~/.bash_profile  # 或 source ~/.zshrc
```

---

### 問題 2：OutOfMemoryError - Java heap space

**症狀：**
```
Exception in thread "main" java.lang.OutOfMemoryError: Java heap space
```

**原因：**
- JVM 記憶體配置不足
- 處理的資料量過大
- 記憶體洩漏

**解決方案：**

1. **增加 JVM 記憶體：**

```bash
# Windows
java -Xms512m -Xmx2048m -jar ATP_re.jar

# macOS
java -Xms512m -Xmx2048m -jar ATP_re.jar
```

2. **修改啟動腳本：**

**ATP_re.bat (Windows):**
```batch
@echo off
set JAVA_OPTS=-Xms512m -Xmx2048m -XX:+UseG1GC
java %JAVA_OPTS% -jar ATP_re.jar
```

**ATP_re.sh (macOS):**
```bash
#!/bin/bash
JAVA_OPTS="-Xms512m -Xmx2048m -XX:+UseG1GC"
java $JAVA_OPTS -jar ATP_re.jar
```

3. **分批處理大量資料：**
   - 將大型檔案分割成較小的批次
   - 減少同時處理的檔案數量
   - 使用批次處理功能

4. **監控記憶體使用：**
```bash
# 啟動時加入記憶體監控
java -Xmx2048m -XX:+PrintGCDetails -XX:+PrintGCTimeStamps -jar ATP_re.jar
```

---

### 問題 3：無法建立視窗 - AWT Error

**症狀：**
```
java.awt.HeadlessException
```

**原因：**
- 在無圖形介面環境執行
- X11 未正確設定（Linux/macOS）

**解決方案：**

1. **確認圖形環境：**

```bash
# Linux/macOS
echo $DISPLAY

# 如果未設定
export DISPLAY=:0
```

2. **使用無 GUI 模式：**

```bash
# 僅解碼模式，不啟動 GUI
java -jar ATP_re.jar --nogui --decode /path/to/file.RU
```

3. **macOS 特定問題：**

```bash
# 加入 macOS 特定參數
java -Dapple.awt.UIElement=false -jar ATP_re.jar
```

---

### 問題 4：埠號已被佔用

**症狀：**
```
java.net.BindException: Address already in use: bind
```

**原因：**
- 預設埠號已被其他程式使用
- 另一個 ATP_re 實例正在執行

**解決方案：**

1. **檢查執行中的實例：**

**Windows:**
```cmd
# 查找使用埠號的程序
netstat -ano | findstr :8080

# 終止程序（替換 PID）
taskkill /PID 1234 /F
```

**macOS:**
```bash
# 查找使用埠號的程序
lsof -i :8080

# 終止程序
kill -9 <PID>
```

2. **更改應用程式埠號：**

編輯 `config/application.properties`:
```properties
server.port=8081
```

---

## 解碼問題

### 問題 5：檔案格式錯誤

**症狀：**
```
DecoderException: Invalid file format
Error Code: 1002
```

**原因：**
- 檔案格式不正確
- 檔案損毀
- 檔案版本不支援

**解決方案：**

1. **驗證檔案簽名：**

```bash
# Windows
certutil -hashfile file.RU MD5

# macOS/Linux
md5 file.RU
```

2. **檢查檔案頭：**

```bash
# 查看檔案前 16 個位元組（十六進位）
# Windows
powershell "Get-Content file.RU -Encoding Byte -TotalCount 16 | Format-Hex"

# macOS/Linux
hexdump -C file.RU | head -n 1
```

RU 檔案應該以 `52 55 4C 4F 47` (RULOG) 開頭

3. **檢查檔案版本：**

查看日誌檔案中的版本資訊：
```
[INFO] File version: 1.8
[ERROR] Unsupported version: 2.0
```

4. **嘗試不同的解碼選項：**

```java
RUDecoder decoder = new RUDecoder();
decoder.setStrictMode(false);  // 非嚴格模式
decoder.setValidateChecksum(false);  // 跳過檢查碼驗證
```

---

### 問題 6：檢查碼驗證失敗

**症狀：**
```
ChecksumException: Checksum validation failed
Expected: 0xABCD, Actual: 0x1234
```

**原因：**
- 檔案在傳輸過程中損毀
- 檔案不完整
- 儲存媒體錯誤

**解決方案：**

1. **重新下載/複製檔案**

2. **暫時停用檢查碼驗證：**

編輯 `config/application.properties`:
```properties
decoder.validate.checksum=false
```

**警告：** 停用檢查碼驗證可能導致解碼錯誤的資料。僅用於診斷。

3. **檢查儲存媒體：**

**Windows:**
```cmd
# 檢查磁碟錯誤
chkdsk C: /F
```

**macOS:**
```bash
# 檢查磁碟
diskutil verifyVolume /
```

4. **比對原始檔案：**
```bash
# 計算檔案雜湊值
# Windows
certutil -hashfile file.RU SHA256

# macOS
shasum -a 256 file.RU
```

---

### 問題 7：解碼速度過慢

**症狀：**
- 解碼單一檔案需要數分鐘
- 批次處理進度緩慢

**原因：**
- 硬體資源不足
- 硬碟 I/O 瓶頸
- 資料庫效能問題

**解決方案：**

1. **增加 JVM 記憶體：**
```bash
java -Xmx4096m -jar ATP_re.jar
```

2. **使用 SSD 儲存資料**

3. **停用即時資料庫寫入：**

編輯 `config/application.properties`:
```properties
# 先解碼，後批次寫入資料庫
database.batch.mode=true
database.batch.size=1000
```

4. **關閉不必要的功能：**
```properties
# 關閉自動統計
analysis.auto.statistics=false

# 關閉自動事件偵測
analysis.auto.events=false
```

5. **使用平行處理：**
```properties
# 啟用多執行緒解碼
decoder.parallel.enabled=true
decoder.parallel.threads=4
```

---

## 效能問題

### 問題 8：應用程式回應緩慢

**症狀：**
- UI 操作反應遲鈍
- 點擊按鈕沒有即時反應
- 視窗凍結

**診斷步驟：**

1. **檢查 CPU 使用率：**

**Windows:**
```cmd
# 工作管理員
taskmgr

# 或使用命令列
wmic cpu get loadpercentage
```

**macOS:**
```bash
# 活動監視器
open -a "Activity Monitor"

# 或使用命令列
top -o cpu
```

2. **檢查記憶體使用：**

**Windows:**
```cmd
wmic OS get FreePhysicalMemory,TotalVisibleMemorySize
```

**macOS:**
```bash
vm_stat
```

3. **檢查磁碟 I/O：**

**Windows:**
```cmd
# 使用資源監視器
resmon
```

**macOS:**
```bash
iostat -w 1
```

**解決方案：**

1. **關閉背景任務：**
   - 停用自動備份
   - 停用 FTP 自動下載
   - 停用自動統計計算

2. **優化資料庫查詢：**
```sql
-- 建立索引
CREATE INDEX idx_task_id ON atp_tasks(task_id);
CREATE INDEX idx_timestamp ON speed_records(timestamp);

-- 更新統計資訊
UPDATE STATISTICS;
```

3. **清理暫存資料：**
```bash
# 刪除暫存檔案
# Windows
del /q "C:\ATP_Data\temp\*"

# macOS
rm -rf /Users/Shared/ATP_Data/temp/*
```

4. **增加記憶體配置：**
```bash
java -Xmx4096m -XX:+UseG1GC -jar ATP_re.jar
```

---

### 問題 9：記憶體洩漏

**症狀：**
- 記憶體使用持續增加
- 最終導致 OutOfMemoryError
- 長時間執行後效能下降

**診斷工具：**

1. **使用 JVisualVM：**
```bash
# 啟動 JVisualVM
jvisualvm

# 連接到 ATP_re 進程
# 監控記憶體使用趨勢
```

2. **啟用 GC 日誌：**
```bash
java -Xmx2048m -XX:+PrintGC -XX:+PrintGCDetails -Xloggc:gc.log -jar ATP_re.jar
```

3. **產生 Heap Dump：**
```bash
# 當記憶體使用率高時
jmap -dump:format=b,file=heap.bin <PID>

# 分析 heap dump
jhat heap.bin
```

**解決方案：**

1. **定期重啟應用程式**（臨時方案）

2. **釋放資源：**
   - 關閉不使用的視窗
   - 清除資料快取
   - 執行垃圾回收

3. **聯絡技術支援** 提供 heap dump 分析

---

## 資料庫問題

### 問題 10：無法連線到資料庫

**症狀：**
```
SQLException: Unable to connect to database
Connection refused
```

**診斷步驟：**

1. **檢查資料庫服務狀態：**

**Windows (SQL Server):**
```cmd
# 檢查 SQL Server 服務
sc query MSSQLSERVER

# 啟動服務
net start MSSQLSERVER
```

**macOS (PostgreSQL):**
```bash
# 檢查 PostgreSQL 狀態
brew services list

# 啟動服務
brew services start postgresql@14
```

2. **測試連線：**

**SQL Server:**
```cmd
sqlcmd -S localhost -U atp_user -P password
```

**PostgreSQL:**
```bash
psql -h localhost -U atp_user -d ATP_DB
```

3. **檢查連線設定：**

查看 `config/database.properties`:
```properties
db.host=localhost
db.port=1433
db.name=ATP_DB
db.user=atp_user
db.password=******
```

**解決方案：**

1. **確認服務執行中**

2. **檢查防火牆設定：**

**Windows:**
```cmd
# 允許 SQL Server 埠號
netsh advfirewall firewall add rule name="SQL Server" dir=in action=allow protocol=TCP localport=1433
```

**macOS:**
```bash
# 檢查防火牆狀態
sudo /usr/libexec/ApplicationFirewall/socketfilterfw --getglobalstate
```

3. **驗證使用者權限：**

```sql
-- 檢查使用者權限
SELECT * FROM sys.database_principals WHERE name = 'atp_user';

-- 授予必要權限
GRANT CONNECT ON DATABASE::ATP_DB TO atp_user;
GRANT SELECT, INSERT, UPDATE, DELETE ON SCHEMA::dbo TO atp_user;
```

---

### 問題 11：資料庫查詢緩慢

**症狀：**
- 資料載入需要很長時間
- 報表產生緩慢
- 查詢超時

**診斷：**

1. **啟用查詢日誌：**

編輯 `config/database.properties`:
```properties
db.log.slow.queries=true
db.slow.query.threshold=1000
```

2. **分析慢查詢：**

**SQL Server:**
```sql
-- 查看最慢的查詢
SELECT TOP 10 
    total_elapsed_time,
    execution_count,
    total_elapsed_time / execution_count AS avg_time,
    SUBSTRING(st.text, (qs.statement_start_offset/2) + 1,
        ((CASE statement_end_offset 
            WHEN -1 THEN DATALENGTH(st.text)
            ELSE qs.statement_end_offset END 
            - qs.statement_start_offset)/2) + 1) AS statement_text
FROM sys.dm_exec_query_stats AS qs
CROSS APPLY sys.dm_exec_sql_text(qs.sql_handle) AS st
ORDER BY total_elapsed_time DESC;
```

**解決方案：**

1. **建立索引：**

```sql
-- 在常用欄位建立索引
CREATE INDEX idx_train_number ON atp_tasks(train_number);
CREATE INDEX idx_start_time ON atp_tasks(start_time);
CREATE INDEX idx_event_type ON atp_events(event_type);
```

2. **優化查詢：**

```sql
-- 避免 SELECT *
-- 不好的做法
SELECT * FROM atp_tasks WHERE train_number = '1234';

-- 好的做法
SELECT task_id, train_number, start_time, end_time 
FROM atp_tasks 
WHERE train_number = '1234';
```

3. **增加連線池大小：**

編輯 `config/database.properties`:
```properties
db.pool.min=10
db.pool.max=50
```

4. **定期維護：**

```sql
-- 更新統計資訊
EXEC sp_updatestats;

-- 重建索引
ALTER INDEX ALL ON atp_tasks REBUILD;

-- 清理資料庫
DBCC SHRINKDATABASE(ATP_DB);
```

---

## UI 顯示問題

### 問題 12：字型顯示異常

**症狀：**
- 中文字顯示為方框
- 字型過小或過大
- 字型模糊

**解決方案：**

1. **設定系統字型：**

編輯 `config/application.properties`:
```properties
# Windows
ui.font.name=Microsoft JhengHei
ui.font.size=12

# macOS
ui.font.name=PingFang TC
ui.font.size=13
```

2. **調整 DPI 縮放：**

**Windows:**
```bash
# 加入 JVM 參數
java -Dsun.java2d.uiScale=1.5 -jar ATP_re.jar
```

**macOS:**
```bash
# 啟用 Retina 支援
java -Dapple.awt.graphics.UseQuartz=true -jar ATP_re.jar
```

3. **安裝缺少的字型**

**Windows:**
- 控制台 → 字型 → 安裝新字型

**macOS:**
- 字體簿 → 檔案 → 加入字體

---

### 問題 13：圖表無法顯示

**症狀：**
- 速度曲線不顯示
- 圖表區域空白
- 顯示錯誤訊息

**原因：**
- 資料不足
- 繪圖庫問題
- 顯示設定錯誤

**解決方案：**

1. **檢查資料：**
```java
// 確認有速度記錄
if (task.getSpeedRecords().isEmpty()) {
    System.out.println("無速度資料");
}
```

2. **檢查圖表設定：**

編輯 `config/application.properties`:
```properties
# 啟用硬體加速
graphics.hardware.acceleration=true

# 設定繪圖緩衝區大小
graphics.buffer.size=2048
```

3. **更新圖形驅動程式**

4. **使用軟體渲染：**
```bash
# 停用硬體加速
java -Dsun.java2d.noddraw=true -jar ATP_re.jar
```

---

## 網路連線問題

### 問題 14：FTP 連線失敗

**症狀：**
```
FTPException: Connection timed out
Unable to connect to FTP server
```

**診斷：**

1. **測試 FTP 連線：**

**Windows:**
```cmd
ftp ftp.example.com
```

**macOS:**
```bash
ftp ftp.example.com
# 或使用 telnet
telnet ftp.example.com 21
```

2. **檢查網路連線：**
```bash
# Ping FTP 伺服器
ping ftp.example.com

# 追蹤路由
tracert ftp.example.com  # Windows
traceroute ftp.example.com  # macOS
```

**解決方案：**

1. **檢查 FTP 設定：**

編輯 `config/ftp.properties`:
```properties
ftp.host=ftp.example.com
ftp.port=21
ftp.user=username
ftp.password=password
ftp.passive.mode=true
ftp.timeout=60000
```

2. **切換為被動模式：**
```properties
ftp.passive.mode=true
```

3. **增加超時時間：**
```properties
ftp.timeout=120000
```

4. **檢查防火牆設定：**
- 允許 FTP 埠號（21）
- 允許被動模式埠號範圍（通常 1024-65535）

---

### 問題 15：代理伺服器設定

**症狀：**
- 無法連線外部網路
- FTP/HTTP 請求失敗

**解決方案：**

1. **設定系統代理：**

編輯 `config/application.properties`:
```properties
network.proxy.enabled=true
network.proxy.host=proxy.company.com
network.proxy.port=8080
network.proxy.user=username
network.proxy.password=password
```

2. **使用 JVM 參數：**
```bash
java -Dhttp.proxyHost=proxy.company.com \
     -Dhttp.proxyPort=8080 \
     -Dhttps.proxyHost=proxy.company.com \
     -Dhttps.proxyPort=8080 \
     -jar ATP_re.jar
```

---

## 日誌分析

### 日誌檔案位置

- **Windows:** `C:\Program Files\ATP_re\logs\atp.log`
- **macOS:** `/Applications/ATP_re/logs/atp.log`

### 日誌等級

| 等級 | 描述 | 範例 |
|------|------|------|
| DEBUG | 詳細除錯資訊 | 變數值、方法呼叫 |
| INFO | 一般資訊 | 啟動、完成操作 |
| WARNING | 警告訊息 | 可恢復的錯誤 |
| ERROR | 錯誤訊息 | 操作失敗 |
| FATAL | 嚴重錯誤 | 系統崩潰 |

### 常見日誌模式

**正常啟動：**
```
[INFO] ATP_re starting...
[INFO] Loading configuration from: config/application.properties
[INFO] Database connection established
[INFO] UI initialization complete
[INFO] ATP_re started successfully
```

**記憶體不足：**
```
[ERROR] java.lang.OutOfMemoryError: Java heap space
[ERROR] Failed to allocate memory for operation
[FATAL] Application terminated due to memory error
```

**檔案解碼錯誤：**
```
[WARNING] File checksum mismatch: file.RU
[ERROR] DecoderException: Invalid file format
[ERROR] File: /path/to/file.RU, Position: 1024
```

### 增加日誌詳細程度

編輯 `config/application.properties`:
```properties
# 更改日誌等級為 DEBUG
log.level=DEBUG

# 啟用特定模組的詳細日誌
log.decoder.level=DEBUG
log.database.level=DEBUG
log.ui.level=INFO
```

### 日誌分析工具

**查看最後 100 行日誌：**
```bash
# Windows
powershell "Get-Content 'C:\Program Files\ATP_re\logs\atp.log' -Tail 100"

# macOS
tail -n 100 /Applications/ATP_re/logs/atp.log
```

**即時監控日誌：**
```bash
# Windows
powershell "Get-Content 'C:\Program Files\ATP_re\logs\atp.log' -Wait"

# macOS
tail -f /Applications/ATP_re/logs/atp.log
```

**搜尋錯誤：**
```bash
# Windows
findstr /i "error" "C:\Program Files\ATP_re\logs\atp.log"

# macOS
grep -i "error" /Applications/ATP_re/logs/atp.log
```

---

## 錯誤代碼對照

### 解碼錯誤 (1000-1999)

| 代碼 | 錯誤名稱 | 描述 | 解決方案 |
|------|---------|------|---------|
| 1001 | ERR_FILE_NOT_FOUND | 檔案不存在 | 檢查檔案路徑 |
| 1002 | ERR_FILE_FORMAT | 檔案格式錯誤 | 確認檔案類型 |
| 1003 | ERR_CHECKSUM | 檢查碼錯誤 | 重新下載檔案 |
| 1004 | ERR_UNSUPPORTED_VERSION | 不支援的版本 | 更新解碼器 |
| 1005 | ERR_CORRUPTED_DATA | 資料損毀 | 檢查檔案完整性 |
| 1006 | ERR_INSUFFICIENT_DATA | 資料不完整 | 確認檔案大小 |

### 資料庫錯誤 (2000-2999)

| 代碼 | 錯誤名稱 | 描述 | 解決方案 |
|------|---------|------|---------|
| 2001 | ERR_DB_CONNECTION | 連線失敗 | 檢查資料庫服務 |
| 2002 | ERR_DB_AUTH | 認證失敗 | 確認帳號密碼 |
| 2003 | ERR_DB_QUERY | 查詢失敗 | 檢查 SQL 語法 |
| 2004 | ERR_DB_TIMEOUT | 連線超時 | 增加超時時間 |
| 2005 | ERR_DB_LOCK | 資料庫鎖定 | 等待或終止鎖定 |

### 網路錯誤 (3000-3999)

| 代碼 | 錯誤名稱 | 描述 | 解決方案 |
|------|---------|------|---------|
| 3001 | ERR_FTP_CONNECTION | FTP 連線失敗 | 檢查網路連線 |
| 3002 | ERR_FTP_AUTH | FTP 認證失敗 | 確認帳號密碼 |
| 3003 | ERR_FTP_TIMEOUT | FTP 超時 | 增加超時設定 |
| 3004 | ERR_NETWORK_UNREACHABLE | 網路不可達 | 檢查網路設定 |

### 系統錯誤 (4000-4999)

| 代碼 | 錯誤名稱 | 描述 | 解決方案 |
|------|---------|------|---------|
| 4001 | ERR_OUT_OF_MEMORY | 記憶體不足 | 增加 JVM 記憶體 |
| 4002 | ERR_DISK_FULL | 磁碟空間不足 | 清理磁碟空間 |
| 4003 | ERR_PERMISSION_DENIED | 權限不足 | 以管理員執行 |
| 4004 | ERR_IO_ERROR | I/O 錯誤 | 檢查磁碟狀態 |

---

## 診斷工具

### 系統資訊收集腳本

**Windows (diagnostic.bat):**
```batch
@echo off
echo ========================================
echo ATP_re 診斷資訊收集
echo ========================================
echo.

echo 系統資訊 > diagnostic_report.txt
echo ======================================== >> diagnostic_report.txt
systeminfo >> diagnostic_report.txt
echo. >> diagnostic_report.txt

echo Java 版本 >> diagnostic_report.txt
echo ======================================== >> diagnostic_report.txt
java -version 2>> diagnostic_report.txt
echo. >> diagnostic_report.txt

echo ATP_re 日誌（最後 100 行） >> diagnostic_report.txt
echo ======================================== >> diagnostic_report.txt
powershell "Get-Content 'C:\Program Files\ATP_re\logs\atp.log' -Tail 100" >> diagnostic_report.txt
echo. >> diagnostic_report.txt

echo 診斷報告已儲存至 diagnostic_report.txt
pause
```

**macOS (diagnostic.sh):**
```bash
#!/bin/bash
echo "========================================"
echo "ATP_re 診斷資訊收集"
echo "========================================"
echo

OUTPUT="diagnostic_report.txt"

echo "系統資訊" > $OUTPUT
echo "========================================" >> $OUTPUT
system_profiler SPSoftwareDataType SPHardwareDataType >> $OUTPUT
echo >> $OUTPUT

echo "Java 版本" >> $OUTPUT
echo "========================================" >> $OUTPUT
java -version 2>> $OUTPUT
echo >> $OUTPUT

echo "ATP_re 日誌（最後 100 行）" >> $OUTPUT
echo "========================================" >> $OUTPUT
tail -n 100 /Applications/ATP_re/logs/atp.log >> $OUTPUT
echo >> $OUTPUT

echo "診斷報告已儲存至 $OUTPUT"
```

### 效能分析工具

**使用 JConsole 監控：**
```bash
# 啟動 JConsole
jconsole

# 連接到 ATP_re 進程
# 監控：記憶體、執行緒、CPU 使用率
```

**產生執行緒傾印：**
```bash
# 取得進程 ID
# Windows
jps

# macOS
jps -l

# 產生執行緒傾印
jstack <PID> > thread_dump.txt
```

---

## 聯絡技術支援

### 提交問題前的檢查清單

在聯絡技術支援前，請確認已嘗試：

- [ ] 查閱本故障排除指南
- [ ] 檢查日誌檔案
- [ ] 搜尋已知問題（GitHub Issues）
- [ ] 重新啟動應用程式
- [ ] 驗證系統需求
- [ ] 收集診斷資訊

### 需要提供的資訊

建立問題報告時，請包含：

1. **基本資訊**
   - ATP_re 版本
   - 作業系統版本
   - Java 版本

2. **問題描述**
   - 詳細的問題說明
   - 重現步驟
   - 預期行為 vs. 實際行為

3. **錯誤資訊**
   - 完整的錯誤訊息
   - 錯誤代碼
   - 螢幕截圖

4. **日誌檔案**
   - atp.log（最後 100 行或完整檔案）
   - 錯誤發生前後的日誌

5. **診斷報告**
   - 系統資訊
   - 設定檔案（移除敏感資訊）
   - 診斷腳本輸出

### 提交管道

**GitHub Issues:**
- URL: https://github.com/Lawliet0813/ATP_re/issues
- 選擇適當的標籤（bug / question / help wanted）
- 使用問題範本

**問題報告範本：**
```markdown
## 問題描述
[簡短描述問題]

## 環境資訊
- ATP_re 版本：
- 作業系統：
- Java 版本：

## 重現步驟
1. 
2. 
3. 

## 預期行為
[描述預期的行為]

## 實際行為
[描述實際發生的行為]

## 錯誤訊息
```
[貼上錯誤訊息]
```

## 日誌
```
[貼上相關日誌]
```

## 截圖
[如適用，加入截圖]

## 已嘗試的解決方案
- [ ] 
- [ ] 
```

---

## 附錄

### A. 快速參考

**記憶體問題：**
```bash
java -Xmx2048m -jar ATP_re.jar
```

**啟用除錯日誌：**
```properties
log.level=DEBUG
```

**停用檢查碼驗證：**
```properties
decoder.validate.checksum=false
```

**檢查 Java 版本：**
```bash
java -version
```

**查看日誌：**
```bash
# Windows
type "C:\Program Files\ATP_re\logs\atp.log"

# macOS
cat /Applications/ATP_re/logs/atp.log
```

### B. 常用命令速查

| 任務 | Windows | macOS |
|------|---------|-------|
| 檢查 Java | `java -version` | `java -version` |
| 查看進程 | `tasklist \| findstr java` | `ps aux \| grep java` |
| 終止進程 | `taskkill /F /PID <PID>` | `kill -9 <PID>` |
| 查看埠號使用 | `netstat -ano \| findstr :8080` | `lsof -i :8080` |
| 查看磁碟空間 | `dir` | `df -h` |
| 查看記憶體 | `systeminfo` | `vm_stat` |

---

**版權聲明**  
© 2025 ATP_re Project. All rights reserved.

**文件維護**  
如發現文件錯誤或需要補充，請提交 GitHub Issue 或 Pull Request。

---

*最後更新：2025-11-09*  
*文件版本：1.0*
