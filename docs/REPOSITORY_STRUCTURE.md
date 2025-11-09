# ATP_re 倉庫結構說明

**最後更新：** 2025-11-09  
**版本：** 2.0

---

## 📁 目錄結構總覽

```
ATP_re/
├── docs/                    # 📚 所有專案文件
│   ├── issues/             # 議題追蹤與規劃
│   ├── 00_Project_Summary.md
│   ├── EXECUTIVE_SUMMARY.md
│   ├── QUICK_START.md
│   ├── ROADMAP.md
│   ├── *_Analysis.md       # 各模組詳細分析文件
│   └── ...                 # 其他技術文件
├── src/                     # 💻 所有原始碼
│   ├── Tools_re/           # 工具集合層（備份、複製、使用者驗證）
│   ├── connect_re/         # 資料存取層（資料庫、FTP）
│   ├── core_re/            # 核心資料模型層（ATP任務、車站）
│   ├── decode_re/          # 基礎解碼層（MMI封包）
│   ├── decoder_re/         # 進階解碼引擎（BTM、VDX、RU）
│   ├── drawGraphics_re/    # 圖形繪製層（曲線、直方圖）
│   ├── ui_re/              # 使用者介面層（Swing GUI）
│   └── com/                # Java package 結構
├── .github/                 # GitHub Actions 和 CI/CD workflows
├── README.md               # 專案主文件
└── .gitignore              # Git 忽略規則
```

---

## 🔄 重組歷史

### 2025-11-09：主要重組

**改變前的問題：**
- ❌ 文件散落在根目錄（25+ 個 markdown 檔案）
- ❌ 重複的資料夾結構（`core/` 和 `core_re/`）
- ❌ 編譯過的 `.class` 檔案被版本控制追蹤
- ❌ 根目錄混亂，難以瀏覽

**執行的改變：**
1. ✅ 建立 `docs/` 資料夾，集中所有文件
2. ✅ 建立 `src/` 資料夾，集中所有原始碼
3. ✅ 移除重複的 `core/`、`decode/`、`decoder/`、`drawGraphics/` 資料夾（僅包含 .class 檔案）
4. ✅ 從版本控制中移除所有 `.class` 檔案
5. ✅ 更新 `README.md` 中的所有文件連結

**改變後的優點：**
- ✅ 清晰的根目錄（僅 3 個資料夾 + 2 個檔案）
- ✅ 文件集中管理，易於查找
- ✅ 原始碼組織清楚
- ✅ 無編譯產物污染版本控制
- ✅ 符合標準專案結構慣例

---

## 📚 docs/ 資料夾內容

### 核心文件
- **README.md** (根目錄) - 專案入口文件
- **00_Project_Summary.md** - 專案總覽與系統架構
- **EXECUTIVE_SUMMARY.md** - 管理層執行摘要
- **QUICK_START.md** - 開發者快速入門
- **ROADMAP.md** - 18 個月開發路線圖
- **PLANNING_SUMMARY.md** - 規劃階段總結
- **IMPLEMENTATION_SUMMARY.md** - 實作階段總結

### 技術文件
- **ATP 行車紀錄分析系統 - 完整技術規格書 v2 0.md** - 完整的 Python 重構技術規格
- **RU_DECODER_UPDATE_NOTES.md** - RU 解碼器更新說明（英文）
- **RU解碼器更新說明.md** - RU 解碼器更新說明（中文）
- **TEST_README.md** - 測試相關說明

### 模組分析文件
每個主要模組都有詳細的分析文件：
- **core_re_Analysis.md** - 核心資料模型層分析
- **decode_re_Analysis.md** - 基礎解碼層分析
- **decoder_re_Analysis.md** - 進階解碼引擎分析
- **drawGraphics_re_Analysis.md** - 圖形繪製層分析
- **ui_re_Analysis.md** - 使用者介面層分析
- **connect_re_Analysis.md** - 資料存取層分析
- **Tools_re_Analysis.md** - 工具集合層分析

### UI/UX 文件
- **UI_UX_GUIDELINES.md** - UI/UX 設計指南
- **UI_UX_VISUAL_EXAMPLES.md** - 視覺化範例

### 其他
- **ATP_System_Flow.mermaid** - 系統流程圖（Mermaid 格式）
- **issues/** - 議題追蹤資料夾

---

## 💻 src/ 資料夾內容

### 分層架構（由低到高）

#### 1. connect_re/ - 資料存取層
- **ConnectDB.java** - 資料庫連線與操作
- **ConnectFTP.java** - FTP 伺服器連線與檔案傳輸

#### 2. core_re/ - 核心資料模型層
- ATP 任務相關類別（ATPMission, ATPMissionDetail 等）
- 車站資訊管理（Station）
- 使用者資料（User）
- 列車資料建立工具

#### 3. decode_re/ - 基礎解碼層
- MMI 封包解碼（40+ 種封包類型）
- ATP 資料解碼
- TS 資料解碼

#### 4. decoder_re/ - 進階解碼引擎
- 解碼緩衝區管理（4096 任務佇列）
- BTM 解碼器（5 片段重組）
- VDX 解碼器
- RU 解碼器
- 路側電報解碼（19 種封包類型）

#### 5. drawGraphics_re/ - 圖形繪製層
- 繪圖基礎類別與介面
- 各種繪圖器（曲線、坡度、故障訊息等）
- 雙模式支援（時間/距離）

#### 6. ui_re/ - 使用者介面層
- **frames/** - 140 個視窗類別
- **panels/** - 145 個面板類別
- **dialogs/** - 9 個對話框
- **adapters/** - 245 個事件適配器
- **utils/** - 工具類別

#### 7. Tools_re/ - 工具集合層
- 檔案備份與刪除管理
- 複製任務處理（USB/FTP/MO/HD）
- 使用者驗證與權限管理（4 級權限）
- 磁碟工具
- 檔案過濾器
- 可排序表格

### Java Package 結構
- **com/MiTAC/TRA/ATP/** - 標準 Java package 結構

---

## 🔍 如何查找檔案

### 查找文件
所有文件都在 `docs/` 資料夾中：
```bash
# 查找所有 markdown 文件
find docs/ -name "*.md"

# 搜尋特定主題
grep -r "解碼器" docs/
```

### 查找原始碼
所有原始碼都在 `src/` 資料夾中：
```bash
# 查找特定類別
find src/ -name "ATPDecoder.java"

# 搜尋特定功能
grep -r "BTM" src/decoder_re/
```

---

## 🚫 .gitignore 規則

編譯產物和暫存檔案會被自動忽略：
```
# 編譯產物
*.class

# IDE 檔案
.idea/
.vscode/
*.iml

# 測試資料
test_data/
test_results/

# OS 檔案
.DS_Store
Thumbs.db

# 暫存檔案
*.tmp
*.swp
*~
```

---

## 📝 貢獻指南

### 新增文件
1. 所有新文件應放在 `docs/` 資料夾
2. 使用描述性的檔案名稱
3. 在主 `README.md` 中新增連結（如果是重要文件）

### 新增原始碼
1. 根據功能層級放入適當的資料夾
2. 遵循現有的命名慣例
3. 確保 `.class` 檔案不會被提交（已被 .gitignore 排除）

### 維護資料夾結構
- ✅ 保持 `docs/` 和 `src/` 的清晰分離
- ✅ 不要在根目錄新增新檔案（除非是配置檔）
- ✅ 使用子資料夾組織相關檔案
- ❌ 不要提交編譯產物
- ❌ 不要建立重複的資料夾結構

---

## 🔗 相關資源

- [README.md](../README.md) - 專案主文件
- [00_Project_Summary.md](./00_Project_Summary.md) - 專案總覽
- [QUICK_START.md](./QUICK_START.md) - 快速入門指南

---

**維護者：** ATP_re 團隊  
**聯絡方式：** [GitHub Issues](https://github.com/Lawliet0813/ATP_re/issues)
