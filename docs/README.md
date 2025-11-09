# ATP_re 文件索引

歡迎來到 ATP_re 專案文件中心！本目錄包含完整的專案文件，幫助您快速了解並使用 ATP_re 系統。

---

## 📚 文件清單

### 1. [使用者操作手冊](./USER_MANUAL.md)
**適用對象：** 系統使用者、維運人員

**內容概要：**
- 系統簡介與架構
- 安裝與設定步驟
- 基本操作指南
- 功能模組使用說明
- 資料管理與報表產出
- 常見問題解答
- 鍵盤快捷鍵

**何時使用：**
- 第一次安裝系統
- 學習系統操作
- 查詢特定功能使用方法
- 遇到操作問題

---

### 2. [API 文件](./API_DOCUMENTATION.md)
**適用對象：** 開發者、系統整合人員

**內容概要：**
- API 架構說明
- 核心模組 API（ATPTask, Station, User）
- 解碼器 API（RUDecoder, MMIDecoder, BTMDecoder）
- 資料模型 API（SpeedRecord, Event, BTMMessage）
- 工具類 API（FileUtils, DataConverter, Logger）
- 程式碼範例與最佳實踐
- 錯誤處理指南

**何時使用：**
- 開發新功能
- 整合第三方系統
- 編寫自訂腳本
- API 參考查詢

---

### 3. [部署指南](./DEPLOYMENT_GUIDE.md)
**適用對象：** 系統管理員、DevOps 工程師

**內容概要：**
- 部署架構說明
- 系統需求（硬體、軟體）
- Windows 詳細部署步驟
- macOS 詳細部署步驟
- 資料庫設定（SQL Server, PostgreSQL）
- 網路與 FTP 設定
- 效能調校指南
- 安全性設定
- 備份與還原
- 升級指南

**何時使用：**
- 首次部署系統
- 系統遷移
- 升級到新版本
- 環境配置優化

---

### 4. [故障排除指南](./TROUBLESHOOTING.md)
**適用對象：** 所有使用者、技術支援人員

**內容概要：**
- 故障排除流程
- 啟動問題（Java、記憶體、視窗）
- 解碼問題（檔案格式、檢查碼）
- 效能問題（速度慢、記憶體洩漏）
- 資料庫問題（連線、查詢）
- UI 顯示問題（字型、圖表）
- 網路連線問題（FTP、代理）
- 日誌分析指南
- 錯誤代碼對照表
- 診斷工具使用
- 技術支援聯絡方式

**何時使用：**
- 系統出現錯誤
- 效能異常
- 功能無法正常運作
- 需要診斷問題

---

### 5. [測試指南](./TESTING_GUIDE.md)
**適用對象：** 開發者、QA 工程師

**內容概要：**
- 測試策略與目標
- 測試金字塔與階段
- 測試環境設定
- 單元測試指南（JUnit, pytest）
- 整合測試（資料庫、檔案系統、FTP）
- 系統測試（端對端、對照驗證）
- 效能測試（解碼效能、記憶體、壓力測試）
- 測試資料準備
- 測試自動化（CI/CD）
- 測試報告範本

**何時使用：**
- 撰寫測試案例
- 設定測試環境
- 執行測試與產生報告
- 建立 CI/CD 流程

---

## 🗂️ 文件架構

```
docs/
├── README.md                    # 本檔案 - 文件索引
├── USER_MANUAL.md              # 使用者操作手冊
├── API_DOCUMENTATION.md        # API 文件
├── DEPLOYMENT_GUIDE.md         # 部署指南
├── TROUBLESHOOTING.md          # 故障排除指南
└── TESTING_GUIDE.md            # 測試指南
```

---

## 🎯 快速導航

### 依角色查找文件

| 角色 | 建議閱讀順序 |
|------|-------------|
| **新使用者** | 1. USER_MANUAL.md → 2. TROUBLESHOOTING.md |
| **開發者** | 1. API_DOCUMENTATION.md → 2. TESTING_GUIDE.md → 3. TROUBLESHOOTING.md |
| **系統管理員** | 1. DEPLOYMENT_GUIDE.md → 2. TROUBLESHOOTING.md → 3. USER_MANUAL.md |
| **QA 工程師** | 1. TESTING_GUIDE.md → 2. API_DOCUMENTATION.md → 3. TROUBLESHOOTING.md |

### 依任務查找文件

| 任務 | 相關文件 |
|------|---------|
| 安裝系統 | DEPLOYMENT_GUIDE.md, USER_MANUAL.md |
| 學習操作 | USER_MANUAL.md |
| 開發功能 | API_DOCUMENTATION.md, TESTING_GUIDE.md |
| 解決問題 | TROUBLESHOOTING.md |
| 撰寫測試 | TESTING_GUIDE.md, API_DOCUMENTATION.md |
| 系統維護 | DEPLOYMENT_GUIDE.md, TROUBLESHOOTING.md |
| 效能調校 | DEPLOYMENT_GUIDE.md, TROUBLESHOOTING.md |

---

## 📖 閱讀建議

### 第一次使用 ATP_re？

建議按照以下順序閱讀：

1. **了解系統**
   - 閱讀主專案 [README.md](../README.md)
   - 瀏覽 [EXECUTIVE_SUMMARY.md](../EXECUTIVE_SUMMARY.md)

2. **安裝與設定**
   - 跟隨 [DEPLOYMENT_GUIDE.md](./DEPLOYMENT_GUIDE.md) 安裝系統
   - 參考 [USER_MANUAL.md](./USER_MANUAL.md) 進行初次設定

3. **開始使用**
   - 學習 [USER_MANUAL.md](./USER_MANUAL.md) 的基本操作
   - 遇到問題查閱 [TROUBLESHOOTING.md](./TROUBLESHOOTING.md)

### 準備開發？

建議按照以下順序閱讀：

1. **了解架構**
   - 閱讀 [00_Project_Summary.md](../00_Project_Summary.md)
   - 查看 [ROADMAP.md](../ROADMAP.md)

2. **API 學習**
   - 熟悉 [API_DOCUMENTATION.md](./API_DOCUMENTATION.md)
   - 運行範例程式碼

3. **測試開發**
   - 閱讀 [TESTING_GUIDE.md](./TESTING_GUIDE.md)
   - 設定測試環境

4. **問題排查**
   - 遇到問題查閱 [TROUBLESHOOTING.md](./TROUBLESHOOTING.md)

---

## 🔄 文件更新

### 版本資訊

| 文件 | 版本 | 更新日期 |
|------|------|---------|
| USER_MANUAL.md | 1.0 | 2025-11-09 |
| API_DOCUMENTATION.md | 1.0 | 2025-11-09 |
| DEPLOYMENT_GUIDE.md | 1.0 | 2025-11-09 |
| TROUBLESHOOTING.md | 1.0 | 2025-11-09 |
| TESTING_GUIDE.md | 1.0 | 2025-11-09 |

### 貢獻指南

發現文件錯誤或需要補充？

1. **提交 Issue**
   - 前往 [GitHub Issues](https://github.com/Lawliet0813/ATP_re/issues)
   - 使用標籤 `documentation`
   - 描述問題或建議

2. **提交 Pull Request**
   - Fork 專案
   - 修改文件
   - 提交 PR 並說明變更內容

3. **文件標準**
   - 使用 Markdown 格式
   - 保持清晰的結構
   - 加入實用的範例
   - 更新版本號與日期

---

## 📞 支援與聯絡

### 技術支援

- **GitHub Issues：** https://github.com/Lawliet0813/ATP_re/issues
- **專案負責人：** @Lawliet0813
- **文件問題：** 使用 `documentation` 標籤

### 相關資源

- **主專案 README：** [../README.md](../README.md)
- **快速入門：** [../QUICK_START.md](../QUICK_START.md)
- **開發路線圖：** [../ROADMAP.md](../ROADMAP.md)
- **技術規格書：** [../ATP 行車紀錄分析系統 - 完整技術規格書 v2 0 29855714413f81b1b980eedb85bea559.md](../ATP%20行車紀錄分析系統%20-%20完整技術規格書%20v2%200%2029855714413f81b1b980eedb85bea559.md)

---

## 📝 文件統計

| 項目 | 數量 |
|------|------|
| 文件總數 | 5 |
| 總頁數（約） | 300+ |
| 程式碼範例 | 100+ |
| 圖表/表格 | 50+ |
| 常見問題解答 | 50+ |

---

## 🎉 致謝

感謝所有為 ATP_re 專案文件做出貢獻的人員！

---

**最後更新：** 2025-11-09  
**維護者：** ATP_re Documentation Team  
**版權所有：** © 2025 ATP_re Project

---

<div align="center">
  <strong>📚 持續改進，不斷完善！</strong>
</div>
