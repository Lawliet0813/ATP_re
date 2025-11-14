# ATP_re 部署指南

**版本：** 1.0  
**更新日期：** 2025-11-09  
**適用系統：** ATP_re Java 版本

---

## 📖 目錄

1. [部署概述](#部署概述)
2. [系統需求](#系統需求)
3. [Windows 部署](#windows-部署)
4. [macOS 部署](#macos-部署)
5. [資料庫設定](#資料庫設定)
6. [網路設定](#網路設定)
7. [效能調校](#效能調校)
8. [安全性設定](#安全性設定)
9. [備份與還原](#備份與還原)
10. [升級指南](#升級指南)

---

## 部署概述

### 部署架構

ATP_re 系統可以部署為單機版或客戶端-伺服器架構：

#### 單機版部署

```
┌─────────────────────────────┐
│   單機工作站                 │
│  ┌─────────────────────┐    │
│  │   ATP_re 應用程式   │    │
│  └─────────────────────┘    │
│  ┌─────────────────────┐    │
│  │   本地資料庫        │    │
│  │   (選用)            │    │
│  └─────────────────────┘    │
│  ┌─────────────────────┐    │
│  │   本地檔案系統      │    │
│  └─────────────────────┘    │
└─────────────────────────────┘
```

#### 客戶端-伺服器部署

```
┌─────────────────┐         ┌──────────────────┐
│  客戶端 1       │         │   資料庫伺服器    │
│  ATP_re 應用    │────────▶│   SQL Server     │
└─────────────────┘         └──────────────────┘
                                     ▲
┌─────────────────┐                 │
│  客戶端 2       │                 │
│  ATP_re 應用    │─────────────────┘
└─────────────────┘                 
                                     
┌─────────────────┐         ┌──────────────────┐
│  客戶端 3       │         │   檔案伺服器      │
│  ATP_re 應用    │────────▶│   FTP Server     │
└─────────────────┘         └──────────────────┘
```

### 部署清單

在開始部署前，請準備以下項目：

- [ ] ATP_re 安裝套件（.zip 或 .tar.gz）
- [ ] Java Runtime Environment (JRE 8+)
- [ ] 資料庫伺服器（如使用資料庫模式）
- [ ] 網路連線設定資訊
- [ ] 管理員權限
- [ ] 授權檔案（如適用）

---

## 系統需求

### 硬體需求

#### 最低需求（單機版）

| 項目 | 規格 |
|------|------|
| 處理器 | Intel Core i3 或同級 |
| 記憶體 | 4 GB RAM |
| 硬碟空間 | 500 MB（程式） + 10 GB（資料） |
| 顯示器 | 1280 x 720 |
| 網路 | 100 Mbps（如需 FTP） |

#### 建議需求（生產環境）

| 項目 | 規格 |
|------|------|
| 處理器 | Intel Core i5/i7 或同級（4 核心以上） |
| 記憶體 | 8-16 GB RAM |
| 硬碟空間 | 1 GB（程式） + 100 GB（資料） |
| 顯示器 | 1920 x 1080 或更高 |
| 網路 | 1 Gbps |

#### 伺服器需求（客戶端-伺服器模式）

**資料庫伺服器**
- 處理器：4 核心以上
- 記憶體：16 GB RAM 以上
- 硬碟：SSD，200 GB 以上
- RAID 配置建議

**檔案伺服器**
- 處理器：2 核心以上
- 記憶體：8 GB RAM
- 硬碟：500 GB 以上
- 備份系統

### 軟體需求

#### Windows

| 項目 | 版本 |
|------|------|
| 作業系統 | Windows 7 SP1 / 8.1 / 10 / 11 |
| Java Runtime | JRE 8 Update 211 或更新版本 |
| .NET Framework | 4.5.2 或更新（部分功能） |
| 資料庫 | SQL Server 2008 R2 或更新 |

#### macOS

| 項目 | 版本 |
|------|------|
| 作業系統 | macOS 10.12 (Sierra) 或更新 |
| Java Runtime | JRE 8 或更新版本 |
| Xcode Command Line Tools | 最新版本 |

---

## Windows 部署

### 步驟 1：安裝 Java Runtime Environment

1. **下載 JRE**
   
   訪問 Oracle 官網或使用 OpenJDK：
   - Oracle JRE: https://www.java.com/
   - OpenJDK: https://adoptium.net/

2. **安裝 JRE**
   
   ```cmd
   # 執行安裝程式
   jre-8u211-windows-x64.exe
   
   # 或使用靜默安裝（需要管理員權限）
   jre-8u211-windows-x64.exe /s INSTALLDIR=C:\Java\jre8
   ```

3. **驗證安裝**
   
   ```cmd
   # 開啟命令提示字元
   cmd
   
   # 檢查 Java 版本
   java -version
   
   # 應該顯示類似輸出：
   # java version "1.8.0_211"
   # Java(TM) SE Runtime Environment (build 1.8.0_211-b12)
   ```

4. **設定環境變數**（選用，但建議）
   
   ```cmd
   # 設定 JAVA_HOME
   setx JAVA_HOME "C:\Program Files\Java\jre1.8.0_211"
   
   # 將 Java bin 目錄加入 PATH
   setx PATH "%PATH%;%JAVA_HOME%\bin"
   ```

### 步驟 2：解壓縮 ATP_re

1. **建立安裝目錄**
   
   ```cmd
   # 建議安裝路徑
   mkdir "C:\Program Files\ATP_re"
   ```

2. **解壓縮檔案**
   
   ```cmd
   # 使用 PowerShell 解壓縮
   powershell Expand-Archive -Path ATP_re_v1.0.zip -DestinationPath "C:\Program Files\ATP_re"
   
   # 或使用 7-Zip
   "C:\Program Files\7-Zip\7z.exe" x ATP_re_v1.0.zip -o"C:\Program Files\ATP_re"
   ```

3. **驗證目錄結構**
   
   ```
   C:\Program Files\ATP_re\
   ├── ATP_re.jar          # 主程式
   ├── ATP_re.bat          # Windows 啟動腳本
   ├── config\             # 設定檔目錄
   │   ├── application.properties
   │   ├── database.properties
   │   └── ftp.properties
   ├── lib\                # 相依函式庫
   ├── data\               # 資料目錄
   ├── logs\               # 日誌目錄
   └── README.txt          # 說明文件
   ```

### 步驟 3：設定應用程式

1. **編輯主設定檔**
   
   開啟 `config/application.properties`：
   
   ```properties
   # 應用程式設定
   app.name=ATP_re
   app.version=1.0
   app.language=zh_TW
   
   # 資料路徑
   data.ru.path=C:/ATP_Data/ru
   data.mmi.path=C:/ATP_Data/mmi
   data.output.path=C:/ATP_Data/output
   
   # 日誌設定
   log.level=INFO
   log.path=C:/Program Files/ATP_re/logs
   log.max.size=10MB
   log.max.history=30
   
   # UI 設定
   ui.theme=default
   ui.font.size=12
   ```

2. **設定資料庫連線**（如使用資料庫）
   
   開啟 `config/database.properties`：
   
   ```properties
   # 資料庫連線設定
   db.enabled=true
   db.type=sqlserver
   db.host=localhost
   db.port=1433
   db.name=ATP_DB
   db.user=atp_user
   db.password=YOUR_PASSWORD_HERE
   
   # 連線池設定
   db.pool.min=5
   db.pool.max=20
   db.pool.timeout=30000
   
   # JDBC 驅動
   db.driver=com.microsoft.sqlserver.jdbc.SQLServerDriver
   db.url=jdbc:sqlserver://${db.host}:${db.port};databaseName=${db.name}
   ```

3. **設定 FTP**（如需要）
   
   開啟 `config/ftp.properties`：
   
   ```properties
   # FTP 設定
   ftp.enabled=false
   ftp.host=ftp.example.com
   ftp.port=21
   ftp.user=atp_ftp_user
   ftp.password=YOUR_FTP_PASSWORD
   ftp.passive.mode=true
   ftp.timeout=30000
   
   # 自動下載設定
   ftp.auto.download=false
   ftp.download.interval=3600
   ftp.download.path=/atp/data
   ```

### 步驟 4：建立資料目錄

```cmd
# 建立必要的目錄
mkdir "C:\ATP_Data"
mkdir "C:\ATP_Data\ru"
mkdir "C:\ATP_Data\mmi"
mkdir "C:\ATP_Data\output"
mkdir "C:\ATP_Data\reports"
mkdir "C:\ATP_Data\backup"

# 設定目錄權限（需要管理員權限）
icacls "C:\ATP_Data" /grant Users:(OI)(CI)M
```

### 步驟 5：建立啟動腳本

編輯或驗證 `ATP_re.bat`：

```batch
@echo off
REM ATP_re Windows 啟動腳本

REM 設定 Java 路徑（如果沒有在 PATH 中）
REM set JAVA_HOME=C:\Program Files\Java\jre1.8.0_211
REM set PATH=%JAVA_HOME%\bin;%PATH%

REM 設定應用程式路徑
set APP_HOME=%~dp0
cd /d %APP_HOME%

REM 設定 JVM 參數
set JAVA_OPTS=-Xms512m -Xmx2048m -XX:+UseG1GC

REM 啟動應用程式
echo 正在啟動 ATP_re...
java %JAVA_OPTS% -jar ATP_re.jar

REM 如果程式異常結束，暫停以查看錯誤訊息
if errorlevel 1 pause
```

### 步驟 6：建立桌面捷徑

1. 在桌面右鍵 → 新增 → 捷徑
2. 位置：`"C:\Program Files\ATP_re\ATP_re.bat"`
3. 名稱：`ATP_re`
4. 圖示：選擇 `ATP_re.ico`（如有）

### 步驟 7：設定 Windows 服務（選用）

如果需要讓 ATP_re 作為 Windows 服務執行：

1. **下載 NSSM**（Non-Sucking Service Manager）
   
   https://nssm.cc/download

2. **安裝服務**
   
   ```cmd
   # 使用管理員權限執行
   nssm install ATP_re
   
   # 在 GUI 中設定：
   # - Path: C:\Program Files\Java\jre1.8.0_211\bin\java.exe
   # - Startup directory: C:\Program Files\ATP_re
   # - Arguments: -Xmx2048m -jar ATP_re.jar
   ```

3. **啟動服務**
   
   ```cmd
   # 啟動服務
   net start ATP_re
   
   # 設定自動啟動
   sc config ATP_re start=auto
   ```

### 步驟 8：防火牆設定

```cmd
# 允許 ATP_re 通過防火牆（需要管理員權限）
netsh advfirewall firewall add rule name="ATP_re" dir=in action=allow program="C:\Program Files\ATP_re\ATP_re.bat" enable=yes

# 如果使用特定埠號（例如：8080）
netsh advfirewall firewall add rule name="ATP_re Port" dir=in action=allow protocol=TCP localport=8080
```

### 步驟 9：驗證安裝

1. **啟動應用程式**
   
   雙擊桌面捷徑或執行 `ATP_re.bat`

2. **檢查日誌**
   
   ```cmd
   # 查看啟動日誌
   type "C:\Program Files\ATP_re\logs\atp.log"
   ```

3. **測試功能**
   
   - 開啟範例檔案
   - 檢視資料
   - 產生報表

---

## macOS 部署

### 步驟 1：安裝 Java Runtime Environment

1. **使用 Homebrew 安裝（建議）**
   
   ```bash
   # 安裝 Homebrew（如果尚未安裝）
   /bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)"
   
   # 安裝 OpenJDK
   brew install openjdk@8
   
   # 建立符號連結
   sudo ln -sfn /usr/local/opt/openjdk@8/libexec/openjdk.jdk /Library/Java/JavaVirtualMachines/openjdk-8.jdk
   ```

2. **手動安裝**
   
   - 下載 JRE for macOS：https://www.java.com/
   - 執行 .dmg 安裝程式
   - 依照安裝精靈完成安裝

3. **驗證安裝**
   
   ```bash
   # 檢查 Java 版本
   java -version
   
   # 檢查 JAVA_HOME
   /usr/libexec/java_home -V
   ```

4. **設定環境變數**
   
   編輯 `~/.bash_profile` 或 `~/.zshrc`：
   
   ```bash
   # 加入以下內容
   export JAVA_HOME=$(/usr/libexec/java_home -v 1.8)
   export PATH=$JAVA_HOME/bin:$PATH
   
   # 套用設定
   source ~/.bash_profile  # 或 source ~/.zshrc
   ```

### 步驟 2：解壓縮 ATP_re

1. **建立安裝目錄**
   
   ```bash
   # 建議安裝路徑
   sudo mkdir -p /Applications/ATP_re
   sudo chown $USER:staff /Applications/ATP_re
   ```

2. **解壓縮檔案**
   
   ```bash
   # 解壓縮
   unzip ATP_re_v1.0.zip -d /Applications/ATP_re
   
   # 或使用 tar（如果是 .tar.gz）
   tar -xzf ATP_re_v1.0.tar.gz -C /Applications/ATP_re
   ```

3. **設定執行權限**
   
   ```bash
   # 設定執行權限
   chmod +x /Applications/ATP_re/ATP_re.sh
   chmod +x /Applications/ATP_re/*.sh
   
   # 設定目錄權限
   chmod -R 755 /Applications/ATP_re
   ```

### 步驟 3：設定應用程式

1. **編輯主設定檔**
   
   ```bash
   # 使用文字編輯器開啟設定檔
   nano /Applications/ATP_re/config/application.properties
   # 或
   vim /Applications/ATP_re/config/application.properties
   ```
   
   設定內容：
   
   ```properties
   # 應用程式設定
   app.name=ATP_re
   app.version=1.0
   app.language=zh_TW
   
   # 資料路徑（macOS 使用絕對路徑）
   data.ru.path=/Users/Shared/ATP_Data/ru
   data.mmi.path=/Users/Shared/ATP_Data/mmi
   data.output.path=/Users/Shared/ATP_Data/output
   
   # 日誌設定
   log.level=INFO
   log.path=/Applications/ATP_re/logs
   log.max.size=10MB
   log.max.history=30
   ```

2. **設定資料庫**（同 Windows，調整路徑）

3. **設定 FTP**（同 Windows）

### 步驟 4：建立資料目錄

```bash
# 建立共享資料目錄
sudo mkdir -p /Users/Shared/ATP_Data/{ru,mmi,output,reports,backup}

# 設定權限
sudo chown -R $USER:staff /Users/Shared/ATP_Data
chmod -R 755 /Users/Shared/ATP_Data
```

### 步驟 5：建立啟動腳本

編輯或驗證 `ATP_re.sh`：

```bash
#!/bin/bash
# ATP_re macOS 啟動腳本

# 取得腳本目錄
APP_HOME="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
cd "$APP_HOME"

# 設定 Java 路徑
export JAVA_HOME=$(/usr/libexec/java_home -v 1.8)
export PATH=$JAVA_HOME/bin:$PATH

# 檢查 Java 是否已安裝
if ! command -v java &> /dev/null; then
    echo "錯誤：未找到 Java。請安裝 Java 8 或更新版本。"
    exit 1
fi

# 設定 JVM 參數
JAVA_OPTS="-Xms512m -Xmx2048m -XX:+UseG1GC"
JAVA_OPTS="$JAVA_OPTS -Dapple.awt.UIElement=false"
JAVA_OPTS="$JAVA_OPTS -Dfile.encoding=UTF-8"

# 啟動應用程式
echo "正在啟動 ATP_re..."
java $JAVA_OPTS -jar ATP_re.jar

# 檢查退出狀態
if [ $? -ne 0 ]; then
    echo "應用程式異常結束"
    read -p "按 Enter 鍵繼續..."
fi
```

### 步驟 6：建立 macOS 應用程式捆綁包（選用）

建立 `.app` 格式的應用程式：

1. **建立目錄結構**
   
   ```bash
   mkdir -p ATP_re.app/Contents/MacOS
   mkdir -p ATP_re.app/Contents/Resources
   ```

2. **建立 Info.plist**
   
   ```bash
   cat > ATP_re.app/Contents/Info.plist << 'EOF'
   <?xml version="1.0" encoding="UTF-8"?>
   <!DOCTYPE plist PUBLIC "-//Apple//DTD PLIST 1.0//EN" "http://www.apple.com/DTDs/PropertyList-1.0.dtd">
   <plist version="1.0">
   <dict>
       <key>CFBundleName</key>
       <string>ATP_re</string>
       <key>CFBundleDisplayName</key>
       <string>ATP_re</string>
       <key>CFBundleIdentifier</key>
       <string>com.atp.atp_re</string>
       <key>CFBundleVersion</key>
       <string>1.0</string>
       <key>CFBundlePackageType</key>
       <string>APPL</string>
       <key>CFBundleExecutable</key>
       <string>ATP_re</string>
       <key>CFBundleIconFile</key>
       <string>ATP_re.icns</string>
   </dict>
   </plist>
   EOF
   ```

3. **複製檔案**
   
   ```bash
   # 複製啟動腳本
   cp /Applications/ATP_re/ATP_re.sh ATP_re.app/Contents/MacOS/ATP_re
   chmod +x ATP_re.app/Contents/MacOS/ATP_re
   
   # 複製圖示（如有）
   cp ATP_re.icns ATP_re.app/Contents/Resources/
   
   # 移動到 Applications
   mv ATP_re.app /Applications/
   ```

### 步驟 7：設定 LaunchAgent（開機自動啟動）

如需開機自動啟動：

```bash
# 建立 LaunchAgent plist
cat > ~/Library/LaunchAgents/com.atp.atp_re.plist << 'EOF'
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE plist PUBLIC "-//Apple//DTD PLIST 1.0//EN" "http://www.apple.com/DTDs/PropertyList-1.0.dtd">
<plist version="1.0">
<dict>
    <key>Label</key>
    <string>com.atp.atp_re</string>
    <key>ProgramArguments</key>
    <array>
        <string>/Applications/ATP_re/ATP_re.sh</string>
    </array>
    <key>RunAtLoad</key>
    <true/>
    <key>KeepAlive</key>
    <false/>
</dict>
</plist>
EOF

# 載入 LaunchAgent
launchctl load ~/Library/LaunchAgents/com.atp.atp_re.plist

# 立即啟動
launchctl start com.atp.atp_re
```

### 步驟 8：防火牆設定

```bash
# macOS 防火牆設定通常在「系統偏好設定」→「安全性與隱私」→「防火牆」
# 如需命令列設定：

# 啟用防火牆
sudo /usr/libexec/ApplicationFirewall/socketfilterfw --setglobalstate on

# 允許 ATP_re
sudo /usr/libexec/ApplicationFirewall/socketfilterfw --add /Applications/ATP_re/ATP_re.sh
sudo /usr/libexec/ApplicationFirewall/socketfilterfw --unblockapp /Applications/ATP_re/ATP_re.sh
```

### 步驟 9：驗證安裝

```bash
# 啟動應用程式
/Applications/ATP_re/ATP_re.sh

# 查看日誌
tail -f /Applications/ATP_re/logs/atp.log

# 檢查進程
ps aux | grep ATP_re
```

---

## 資料庫設定

### SQL Server 設定（Windows）

1. **安裝 SQL Server**
   
   - 下載 SQL Server Express（免費版）
   - 執行安裝程式
   - 選擇「基本」安裝模式

2. **建立資料庫**
   
   ```sql
   -- 使用 SQL Server Management Studio 或 sqlcmd
   
   -- 建立資料庫
   CREATE DATABASE ATP_DB;
   GO
   
   -- 切換到新資料庫
   USE ATP_DB;
   GO
   
   -- 建立使用者
   CREATE LOGIN atp_user WITH PASSWORD = 'YourStrongPassword123!';
   CREATE USER atp_user FOR LOGIN atp_user;
   GO
   
   -- 授予權限
   EXEC sp_addrolemember 'db_datareader', 'atp_user';
   EXEC sp_addrolemember 'db_datawriter', 'atp_user';
   GO
   ```

3. **執行初始化腳本**
   
   ```cmd
   # 執行 ATP_re 提供的資料庫初始化腳本
   sqlcmd -S localhost -U atp_user -P YourStrongPassword123! -d ATP_DB -i "C:\Program Files\ATP_re\sql\init.sql"
   ```

4. **啟用 TCP/IP 連線**
   
   - 開啟「SQL Server Configuration Manager」
   - 展開「SQL Server 網路組態」
   - 選擇「MSSQLSERVER 的通訊協定」
   - 啟用「TCP/IP」
   - 重新啟動 SQL Server 服務

### PostgreSQL 設定（跨平台）

如果偏好使用 PostgreSQL：

1. **安裝 PostgreSQL**
   
   **Windows:**
   ```cmd
   # 下載並安裝 PostgreSQL
   # https://www.postgresql.org/download/windows/
   ```
   
   **macOS:**
   ```bash
   brew install postgresql@14
   brew services start postgresql@14
   ```

2. **建立資料庫**
   
   ```bash
   # 建立使用者
   createuser -P atp_user
   # 輸入密碼: YourStrongPassword123!
   
   # 建立資料庫
   createdb -O atp_user ATP_DB
   ```

3. **執行初始化腳本**
   
   ```bash
   psql -U atp_user -d ATP_DB -f /Applications/ATP_re/sql/init.sql
   ```

4. **設定遠端存取**（如需要）
   
   編輯 `postgresql.conf`:
   ```
   listen_addresses = '*'
   ```
   
   編輯 `pg_hba.conf`:
   ```
   host    all    all    0.0.0.0/0    md5
   ```

---

## 網路設定

### FTP 伺服器設定

如果需要自動從 FTP 伺服器下載資料：

1. **測試 FTP 連線**
   
   ```bash
   # Windows
   ftp ftp.example.com
   
   # macOS/Linux
   ftp ftp.example.com
   # 或使用
   lftp ftp://username:password@ftp.example.com
   ```

2. **設定被動模式**
   
   在 `config/ftp.properties` 中：
   ```properties
   ftp.passive.mode=true
   ```

3. **設定防火牆規則**
   
   確保允許 FTP 使用的埠號（通常是 20-21 和被動模式的高位埠號）

### 網路共享設定

如果多台電腦需要存取同一資料：

**Windows 網路共享：**

```cmd
# 共享資料夾
net share ATPData=C:\ATP_Data /grant:everyone,FULL

# 從其他電腦存取
net use Z: \\ComputerName\ATPData
```

**macOS 檔案共享：**

1. 系統偏好設定 → 共享
2. 啟用「檔案共享」
3. 加入要共享的資料夾
4. 設定權限

---

## 效能調校

### JVM 記憶體調校

根據系統資源調整 JVM 參數：

```bash
# 小型系統（4GB RAM）
java -Xms256m -Xmx1024m -jar ATP_re.jar

# 中型系統（8GB RAM）
java -Xms512m -Xmx2048m -jar ATP_re.jar

# 大型系統（16GB+ RAM）
java -Xms1024m -Xmx4096m -jar ATP_re.jar
```

### 垃圾回收調校

```bash
# 使用 G1 垃圾回收器（建議）
java -XX:+UseG1GC -XX:MaxGCPauseMillis=200 -jar ATP_re.jar

# 或使用 CMS（較舊但穩定）
java -XX:+UseConcMarkSweepGC -XX:+CMSParallelRemarkEnabled -jar ATP_re.jar
```

### 資料庫效能調校

**建立索引：**

```sql
-- 在常用查詢欄位建立索引
CREATE INDEX idx_task_train_number ON atp_tasks(train_number);
CREATE INDEX idx_task_start_time ON atp_tasks(start_time);
CREATE INDEX idx_event_timestamp ON atp_events(timestamp);
CREATE INDEX idx_speed_timestamp ON speed_records(timestamp);
```

**查詢優化：**

```sql
-- 定期更新統計資訊
UPDATE STATISTICS atp_tasks;
UPDATE STATISTICS atp_events;
UPDATE STATISTICS speed_records;

-- 重建索引
ALTER INDEX ALL ON atp_tasks REBUILD;
```

---

## 安全性設定

### 1. 檔案系統權限

**Windows:**
```cmd
# 限制程式目錄只有管理員可寫入
icacls "C:\Program Files\ATP_re" /inheritance:r
icacls "C:\Program Files\ATP_re" /grant:r Administrators:(OI)(CI)F
icacls "C:\Program Files\ATP_re" /grant:r Users:(OI)(CI)RX

# 資料目錄允許寫入
icacls "C:\ATP_Data" /grant Users:(OI)(CI)M
```

**macOS:**
```bash
# 限制程式目錄
sudo chown -R root:wheel /Applications/ATP_re
sudo chmod -R 755 /Applications/ATP_re

# 資料目錄允許寫入
sudo chown -R $USER:staff /Users/Shared/ATP_Data
chmod -R 755 /Users/Shared/ATP_Data
```

### 2. 資料庫安全

```sql
-- 使用強密碼
ALTER LOGIN atp_user WITH PASSWORD = 'YourV3ryStr0ngP@ssw0rd!';

-- 限制權限
REVOKE ALL ON DATABASE ATP_DB FROM PUBLIC;
GRANT CONNECT ON DATABASE ATP_DB TO atp_user;

-- 啟用連線加密
-- 在 SQL Server Configuration Manager 中設定 SSL
```

### 3. 加密敏感資料

在 `config/database.properties` 中使用加密密碼：

```properties
# 不要明文儲存密碼！
# 使用加密工具生成加密密碼
db.password.encrypted=true
db.password=ENC(encryptedPasswordHere)
```

### 4. 日誌安全

```properties
# 不要在日誌中記錄敏感資訊
log.mask.sensitive=true
log.exclude.patterns=password,secret,token
```

---

## 備份與還原

### 自動備份設定

建立備份腳本：

**Windows (backup.bat):**
```batch
@echo off
set BACKUP_DIR=D:\ATP_Backup\%date:~0,4%%date:~5,2%%date:~8,2%
mkdir %BACKUP_DIR%

REM 備份資料庫
sqlcmd -S localhost -U sa -P YourPassword -Q "BACKUP DATABASE ATP_DB TO DISK='%BACKUP_DIR%\ATP_DB.bak'"

REM 備份資料檔案
xcopy "C:\ATP_Data" "%BACKUP_DIR%\data\" /E /I /Y

REM 備份設定檔
xcopy "C:\Program Files\ATP_re\config" "%BACKUP_DIR%\config\" /E /I /Y

echo 備份完成: %BACKUP_DIR%
```

**macOS (backup.sh):**
```bash
#!/bin/bash
BACKUP_DIR="/Users/Shared/ATP_Backup/$(date +%Y%m%d)"
mkdir -p "$BACKUP_DIR"

# 備份資料庫
pg_dump -U atp_user ATP_DB > "$BACKUP_DIR/ATP_DB.sql"

# 備份資料檔案
cp -r /Users/Shared/ATP_Data "$BACKUP_DIR/data"

# 備份設定檔
cp -r /Applications/ATP_re/config "$BACKUP_DIR/config"

echo "備份完成: $BACKUP_DIR"
```

### 排程自動備份

**Windows（使用工作排程器）:**
```cmd
# 建立每日凌晨 2:00 執行的備份任務
schtasks /create /tn "ATP_re Backup" /tr "D:\Scripts\backup.bat" /sc daily /st 02:00
```

**macOS（使用 cron）:**
```bash
# 編輯 crontab
crontab -e

# 加入以下內容（每日凌晨 2:00 執行）
0 2 * * * /Users/Shared/Scripts/backup.sh
```

### 還原資料

**Windows:**
```batch
REM 還原資料庫
sqlcmd -S localhost -U sa -P YourPassword -Q "RESTORE DATABASE ATP_DB FROM DISK='D:\ATP_Backup\20231201\ATP_DB.bak' WITH REPLACE"

REM 還原資料檔案
xcopy "D:\ATP_Backup\20231201\data" "C:\ATP_Data\" /E /I /Y

REM 還原設定檔
xcopy "D:\ATP_Backup\20231201\config" "C:\Program Files\ATP_re\config\" /E /I /Y
```

**macOS:**
```bash
# 還原資料庫
psql -U atp_user ATP_DB < /Users/Shared/ATP_Backup/20231201/ATP_DB.sql

# 還原資料檔案
cp -r /Users/Shared/ATP_Backup/20231201/data/* /Users/Shared/ATP_Data/

# 還原設定檔
cp -r /Users/Shared/ATP_Backup/20231201/config/* /Applications/ATP_re/config/
```

---

## 升級指南

### 升級前準備

1. **備份現有系統**
   ```bash
   # 執行完整備份
   ./backup.sh  # 或 backup.bat
   ```

2. **記錄目前版本**
   ```bash
   java -jar ATP_re.jar --version
   ```

3. **閱讀版本說明**
   - 查看 CHANGELOG.md
   - 注意不相容的變更

### 升級步驟

1. **停止應用程式**
   
   **Windows:**
   ```cmd
   # 如果作為服務執行
   net stop ATP_re
   
   # 或關閉應用程式視窗
   ```
   
   **macOS:**
   ```bash
   # 如果使用 LaunchAgent
   launchctl stop com.atp.atp_re
   
   # 或使用 kill
   pkill -f ATP_re.jar
   ```

2. **備份舊版本**
   
   ```bash
   # Windows
   move "C:\Program Files\ATP_re" "C:\Program Files\ATP_re_old"
   
   # macOS
   mv /Applications/ATP_re /Applications/ATP_re_old
   ```

3. **安裝新版本**
   
   按照正常安裝步驟安裝新版本

4. **遷移設定檔**
   
   ```bash
   # Windows
   xcopy "C:\Program Files\ATP_re_old\config\*" "C:\Program Files\ATP_re\config\" /Y
   
   # macOS
   cp /Applications/ATP_re_old/config/* /Applications/ATP_re/config/
   ```

5. **執行資料庫升級腳本**（如有）
   
   ```bash
   # 執行版本升級腳本
   sqlcmd -S localhost -U atp_user -P Password -d ATP_DB -i "upgrade_v1.0_to_v1.1.sql"
   ```

6. **啟動新版本**
   
   ```bash
   # 啟動並檢查日誌
   java -jar ATP_re.jar
   ```

7. **驗證升級**
   
   - 檢查版本號
   - 測試主要功能
   - 查看日誌檔

8. **清理舊版本**（確認新版本正常後）
   
   ```bash
   # Windows
   rmdir /s /q "C:\Program Files\ATP_re_old"
   
   # macOS
   rm -rf /Applications/ATP_re_old
   ```

---

## 故障排除

### 常見問題

**問題 1：無法啟動 - 找不到 Java**

解決方法：
```bash
# 檢查 Java 安裝
java -version

# 設定 JAVA_HOME
# Windows
setx JAVA_HOME "C:\Program Files\Java\jre1.8.0_211"

# macOS
export JAVA_HOME=$(/usr/libexec/java_home)
```

**問題 2：記憶體不足錯誤**

解決方法：
```bash
# 增加最大記憶體
java -Xmx4096m -jar ATP_re.jar
```

**問題 3：資料庫連線失敗**

檢查項目：
1. 資料庫服務是否執行
2. 連線資訊是否正確
3. 防火牆是否阻擋
4. 使用者權限是否足夠

**問題 4：埠號衝突**

解決方法：
```properties
# 修改 application.properties
server.port=8081
```

---

## 附錄

### A. 設定檔範本

完整的 `application.properties` 範本：

```properties
# ===========================================
# ATP_re 應用程式設定檔
# ===========================================

# 應用程式基本資訊
app.name=ATP_re
app.version=1.0
app.language=zh_TW
app.timezone=Asia/Taipei

# 資料路徑設定
data.ru.path=/path/to/ru/data
data.mmi.path=/path/to/mmi/data
data.output.path=/path/to/output
data.reports.path=/path/to/reports
data.temp.path=/path/to/temp

# 日誌設定
log.level=INFO
log.path=/path/to/logs
log.max.size=10MB
log.max.history=30
log.pattern=%d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %logger{36} - %msg%n

# UI 設定
ui.theme=default
ui.font.name=Microsoft JhengHei
ui.font.size=12
ui.window.width=1280
ui.window.height=800
ui.window.maximized=false

# 效能設定
performance.thread.pool.size=4
performance.batch.size=100
performance.cache.enabled=true
performance.cache.size=1000

# 解碼器設定
decoder.strict.mode=false
decoder.validate.checksum=true
decoder.auto.retry=true
decoder.retry.count=3

# 自動儲存設定
autosave.enabled=true
autosave.interval=300

# 網路設定
network.timeout=30000
network.retry=3
network.proxy.enabled=false
network.proxy.host=
network.proxy.port=
```

### B. 系統需求檢查腳本

**Windows (check_system.bat):**
```batch
@echo off
echo ========================================
echo ATP_re 系統需求檢查
echo ========================================
echo.

echo 檢查 Java...
java -version 2>&1 | find "version" > nul
if %errorlevel% == 0 (
    echo [OK] Java 已安裝
    java -version
) else (
    echo [FAIL] 未找到 Java
)
echo.

echo 檢查磁碟空間...
for /f "tokens=3" %%a in ('dir C:\ ^| find "bytes free"') do set FREE=%%a
echo C:\ 可用空間: %FREE% bytes
echo.

echo 檢查記憶體...
wmic OS get FreePhysicalMemory
echo.

echo ========================================
echo 檢查完成
echo ========================================
pause
```

**macOS (check_system.sh):**
```bash
#!/bin/bash
echo "========================================"
echo "ATP_re 系統需求檢查"
echo "========================================"
echo

echo "檢查 Java..."
if command -v java &> /dev/null; then
    echo "[OK] Java 已安裝"
    java -version
else
    echo "[FAIL] 未找到 Java"
fi
echo

echo "檢查磁碟空間..."
df -h /
echo

echo "檢查記憶體..."
vm_stat | grep "Pages free" | awk '{print $3}'
echo

echo "========================================"
echo "檢查完成"
echo "========================================"
```

---

**版權聲明**  
© 2025 ATP_re Project. All rights reserved.

**文件維護**  
如發現文件錯誤或需要補充，請提交 GitHub Issue 或 Pull Request。

---

*最後更新：2025-11-09*  
*文件版本：1.0*
