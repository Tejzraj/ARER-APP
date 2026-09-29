<div align="center">

# 🟠 ARER APP

### Simplifying Monthly MDM Reporting for Schools

A privacy-focused, offline-first Android application designed to make daily student-count entry, MDM rate management, monthly rate confirmations, and formal report generation simpler for Government and general schools.

<p align="center">
  <img src="https://readme-typing-svg.demolab.com?font=Fira+Code&weight=600&size=20&duration=3000&pause=1000&color=F97316&background=0F172A00&center=true&vCenter=true&width=700&lines=Simplifying+School+Mid-Day+Meal+(MDM)+Reporting;Offline-First+Architecture+with+Zero+Cloud+Dependency;Exact+Integer+Minor-Unit+Financial+Arithmetic+(Paise);Automated+Sunday+%26+Government+Holiday+Validation;Immutable+Historical+Rate+Snapshots;Bilingual+Interface%3A+Kannada+(%E0%B2%95%E0%B2%A8%E0%B3%8D%E0%B2%A8%E0%B2%A1)+%2B+English;Engineered+with+Modern+Kotlin+2.0+%2B+Jetpack+Compose" alt="ARER Feature Stream" />
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Platform-Android-3DDC84?style=for-the-badge&logo=android&logoColor=white" alt="Platform" />
  <img src="https://img.shields.io/badge/Kotlin-2.0.21-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white" alt="Kotlin" />
  <img src="https://img.shields.io/badge/Jetpack%20Compose-M3-4285F4?style=for-the-badge&logo=jetpackcompose&logoColor=white" alt="Jetpack Compose" />
  <img src="https://img.shields.io/badge/Room-v2%20SQLite-0052CC?style=for-the-badge&logo=sqlite&logoColor=white" alt="Room v2" />
  <img src="https://img.shields.io/badge/DI-Hilt-FF6F00?style=for-the-badge&logo=dagger&logoColor=white" alt="Hilt" />
  <img src="https://img.shields.io/badge/Status-Phase%204%20Complete-F97316?style=for-the-badge" alt="Status" />
</p>

`Current Version: V1 — Offline Android` &nbsp;|&nbsp; `Current Milestone: Phase 4 Complete`

</div>

<hr>

## 📑 Quick Navigation

- [Overview](#-overview)
- [Development Status](#-development-status)
- [The Problem vs. Solution](#-the-problem-vs--solution)
- [Design Philosophy](#-design-philosophy)
- [Current Features](#-current-features)
- [System Workflow](#-system-workflow)
- [Architecture & Clean Design](#-architecture--clean-design)
- [Database & Offline-First Architecture](#-database--offline-first-architecture)
- [Privacy & Security](#-privacy--security)
- [Tech Stack](#-tech-stack)
- [Project Structure](#-project-structure)
- [Testing & Quality Assurance](#-testing--quality-assurance)
- [Development Roadmap](#-development-roadmap)
- [Getting Started](#-getting-started)
- [License & Contributions](#-license--contributions)

<hr>

## 📖 Overview

School Head Masters (HMs) and Assistant Teachers traditionally maintain daily student attendance in registers or Excel spreadsheets, followed by manual monthly calculations of item-wise expenditures for Mid-Day Meal (MDM) bills. This manual workflow is prone to calculation errors, rate mismatches, and tedious paperwork.

**ARER APP** digitizes and streamlines this exact workflow into a lightning-fast, offline-first Android application designed specifically for Indian government and general schools.

---

## 🟠 Development Status

| Phase | Milestone | Status | Description |
| :---: | :--- | :---: | :--- |
| **Phase 1** | Architecture + Offline Database Foundation | ✅ **Completed** | Clean Architecture, MVVM, Room v2, Hilt DI, 10 Entities, DAOs. |
| **Phase 2** | School Setup + Local Profile + Auth | ✅ **Completed** | First-time onboarding, School/HM profile, default MDM item seeding, DataStore. |
| **Phase 3** | Daily MDM Entry + Working Day Engine | ✅ **Completed** | Month navigation, Sunday/holiday auto-detection, overrides, null vs zero distinction. |
| **Phase 4** | Rate Management + Monthly Rate Confirmation | ✅ **Completed** | Editable item rates, effective-date rate resolution, monthly rate review & confirmation. |
| **Phase 5** | MDM Calculation Engine (Paise Precision) | ⏳ **Next** | Exact minor-currency unit calculations and monthly totals. |
| **Phase 6** | Formal Report Exporter (PDF/CSV) | ⏳ **Planned** | Government-compliant monthly bill generation with Kannada Unicode. |
| **Phase 7** | History Archive & Analytics | ⏳ **Planned** | Archived report browser and contingency analysis. |
| **V2** | Cloud Sync & Stock Management | 🔮 **Future** | Encrypted cloud backups and multi-device synchronization. |

---

## 🛑 The Problem vs. 🚀 Solution

<table width="100%">
<tr>
<td width="50%" valign="top">

### 🔴 Traditional Manual Process
* **Paper Registers**: Daily attendance recorded across handwritten registers.
* **Manual Arithmetic**: Monthly item totals and fractional quantities computed manually.
* **Error Prone**: Off-by-one paise discrepancies lead to audit rejections and delayed grants.
* **Rate Drift**: Mid-year rate changes often corrupt past bill records.
* **Clerical Fatigue**: Hours of repetitive paperwork every month.

</td>
<td width="50%" valign="top">

### 🟢 ARER APP Solution
* **5-Second Daily Entry**: Single aggregate student count entered daily.
* **Smart Calendar Engine**: Automatically recognizes Sundays and government holidays; supports emergency overrides.
* **Paise-Exact Precision**: Integer arithmetic (`Long paise`) eliminates floating-point rounding bugs.
* **Historical Rate Snapshots**: Past bills are locked to the rates confirmed for that specific month.
* **Instant Export**: Ready-form audit reports without manual calculation.

</td>
</tr>
</table>

---

## 🎯 Design Philosophy

ARER is intentionally engineered around **"Simple enough for a first-time user."**

* **Minimal Screens**: No complex menus or deep navigation hierarchies.
* **Large Touch Targets**: Button sizes ($\ge 56\,\text{dp}$) optimized for everyday use.
* **Clear Status Indicators**: Immediate visual feedback (`✓ Saved`, `Missing Entry`, `Locked`).
* **Orange Visual Identity**: Professional, high-contrast, government-office appropriate palette (`#F97316`).
* **Bilingual Support**: Native English and Kannada (`ಕನ್ನಡ`) localization.
* **Offline-First**: Zero reliance on internet connectivity.

---

## ✨ Current Features

### 🏫 School & HM Setup
* Full school profile capture (Name, Code, UDISE, KGID, District, Taluk, Cluster, Address, PIN Code).
* Head Master profile management with persistent onboarding flags.

### 📅 Daily MDM Entry
* Month navigation selector (`<` Month `>`).
* Date-wise student count entry with safe auto-save.
* Strict distinction between **Missing Entry (`null`)** and **Explicit Zero (`0`)**.

### 🗓 Working Day Engine
* Automatic Sunday and local government holiday detection.
* Controlled emergency working-day overrides with reason logging and audit trail.

### 💰 Rate Management & Confirmation
* Manage item-wise MDM rates across the 11 default seeded items (`Vegetables`, `Sambar Items`, `Salt`, `Sugar`, `Dal`, `Oil`, `Gas`, `Egg`, `Milk`, `Girini`, `Banana`).
* Effective-date rate resolution ensuring historical rate preservation.
* Monthly Rate Confirmation workflow ensuring rates are reviewed and locked before reporting.

### 🗄 Offline Storage & Security
* Room database v2 with safe non-destructive migration (`MIGRATION_1_2`).
* 100% offline application-private storage with zero internet permissions requested.

---

## 🔄 System Workflow

```mermaid
sequenceDiagram
    autonumber
    actor Teacher as 👨‍🏫 Head Master / Teacher
    participant UI as 📱 Compose Presentation
    participant VM as 🧠 ViewModel / StateFlow
    participant Repo as 📦 Repository Layer
    participant DB as 💾 Room v2 SQLite Database

    Teacher->>UI: Launch ARER APP
    UI->>VM: Request App State
    VM->>Repo: Check Setup Completion (DataStore)
    Repo-->>VM: isSetupComplete = true
    VM-->>UI: Display Dashboard Screen

    Teacher->>UI: Open Monthly Entry & Select Month
    UI->>VM: Fetch Days & Rate Confirmation Status
    VM->>Repo: Query MonthlyRateConfirmationEntity
    alt Rates Not Confirmed
        Repo-->>VM: isConfirmed = false
        VM-->>UI: Show Monthly Rate Confirmation Dialog
        Teacher->>UI: Review Rates & Tap Confirm
        UI->>VM: Save Rate Confirmation & Log Audit
        VM->>Repo: Insert Confirmation Entity
    end
    Teacher->>UI: Enter Daily Student Count
    UI->>VM: Save Daily MDM Entry (Nullable Int?)
    VM->>Repo: Save to DailyMDMEntryEntity
    Repo->>DB: Persist via Room v2 DAO
    DB-->>UI: Show "✓ Saved" Status
```

---

## 🏛 Architecture & Clean Design

ARER APP follows strict **Clean Architecture** and **MVVM** principles:

```mermaid
graph TD
    UI[Presentation Layer<br>Jetpack Compose & ViewModels] --> Domain[Domain Layer<br>Use Cases & Business Rules]
    Domain --> Data[Data Layer<br>Repositories & Room DAOs]
    Data --> DB[(SQLite / Room Database v2<br>App-Private Storage)]
    Hilt[Hilt DI Container] --> UI
    Hilt --> Domain
    Hilt --> Data

    style UI fill:#E65100,stroke:#3E2723,stroke-width:2px,color:#fff
    style Domain fill:#BF360C,stroke:#261A18,stroke-width:2px,color:#fff
    style Data fill:#0F172A,stroke:#F97316,stroke-width:2px,color:#fff
    style DB fill:#334155,stroke:#F97316,stroke-width:2px,color:#fff
    style Hilt fill:#7F52FF,stroke:#3E2723,stroke-width:2px,color:#fff
```

---

## 🗄 Database & Offline-First Architecture

The Room v2 database manages 10 core entities with non-destructive migrations (`MIGRATION_1_2`):

* `SchoolProfileEntity`
* `HMProfileEntity`
* `MDMItemEntity`
* `ItemRateEntity` (Effective-date rate history)
* `HolidayEntity` (Local holiday calendar)
* `DailyMDMEntryEntity` (Nullable `studentCount` for missing vs zero distinction)
* `MonthlyRateConfirmationEntity` (Monthly rate lock)
* `MonthlyReportEntity`
* `MonthlyReportItemEntity` (Historical snapshots)
* `AuditLogEntity` (Action tracing)

---

## 🔐 Privacy & Security

* **100% Offline**: Zero network calls or external cloud dependencies in V1 core workflows.
* **App-Private Storage**: Databases and preferences are securely sandboxed inside internal app storage.
* **No Plaintext Secrets**: Authentication is abstracted via `AuthRepository`.
* **Audit Logging**: Important administrative rate changes and overrides are recorded locally via `AuditLogEntity`.

---

## 🛠 Tech Stack

| Technology | Purpose |
| :--- | :--- |
| **Kotlin 2.0.21** | Application programming language |
| **Jetpack Compose** | Declarative UI framework |
| **Material 3** | Design system & theming (Light/Dark/System) |
| **Room v2 (2.8.5)** | Local SQLite database with migration safety |
| **Dagger Hilt (2.60.1)** | Compile-time dependency injection |
| **Kotlin Coroutines** | Asynchronous operations & background threading |
| **StateFlow** | Reactive UI state propagation |
| **Navigation Compose** | Type-safe composable screen routing |
| **DataStore Preferences** | Local session and onboarding state persistence |
| **Android SDK** | Min SDK 26, Target SDK 37, Compile SDK 37 |

---

## 📂 Project Structure

```text
app/src/main/java/com/arer/app/
├── data/
│   ├── local/
│   │   ├── ArerDatabase.kt (v2 + MIGRATION_1_2)
│   │   ├── converter/
│   │   ├── dao/          # SchoolDao, MdmDao, HolidayDao, ReportDao
│   │   └── entity/       # 10 Room Entities
│   └── preferences/      # AppPreferences.kt (DataStore)
├── domain/
│   ├── model/            # Money.kt, Enums.kt
│   ├── repository/       # Repository Interfaces
│   └── usecase/          # Business Use Cases (ResolveRateForDate, etc.)
├── data/repository/      # MdmRepositoryImpl, SchoolRepositoryImpl
├── di/                   # Hilt Modules (DatabaseModule, AuthModule)
└── ui/
    ├── auth/             # LoginScreen, LoginViewModel
    ├── dashboard/        # DashboardScreen, DashboardViewModel
    ├── entry/            # MonthlyEntryScreen, MonthlyEntryViewModel
    ├── rates/            # RateManagementScreen, RateManagementViewModel, Dialogs
    ├── history/          # HistoryScreen
    ├── reports/          # ReportsScreen
    ├── settings/         # SettingsScreen, SettingsViewModel
    ├── setup/            # SchoolSetup, HmSetup, MdmSetup
    ├── splash/           # SplashScreen, SplashViewModel
    ├── navigation/       # ArerNavGraph.kt, Screen.kt
    └── theme/            # Color.kt, Theme.kt, Type.kt
```

---

## 🧪 Testing & Quality Assurance

The test suite validates money arithmetic, date calculations, and Room migration safety:

```bash
# Execute local JVM unit tests
./gradlew test
```

* **Unit Tests**: 15 tests passed successfully (`MoneyTest`, `DateUtilsTest`, `MdmDomainTest`, `RateResolutionTest`, `SetupValidationTest`).
* **Build Verification**: `./gradlew assembleDebug` builds successfully without warnings.

---

## 🗺 Development Roadmap

### Completed
* **Phase 1**: Architecture & Database Foundation
* **Phase 2**: School Setup + Auth + MDM Item Seeding
* **Phase 3**: Daily MDM Entry + Working Day Engine
* **Phase 4**: Rate Management + Monthly Rate Confirmation

### Next
* **Phase 5**: MDM Calculation Engine (Paise Precision)

### Planned (V1 Finalization)
* **Phase 6**: Formal PDF & CSV Report Exporter
* **Phase 7**: History Archive & Analytics

### V2 Vision
* Encrypted Cloud Backup & Multi-School Synchronization

---

## 🚀 Getting Started

### Prerequisites
* Android Studio (Koala / Ladybug or newer)
* JDK 17
* Android SDK (Min 26, Target 37)

### Clone & Build
```bash
git clone https://github.com/YOUR_GITHUB_USERNAME/ARER-APP.git
cd ARER-APP

# Build debug APK
./gradlew assembleDebug

# Run test suite
./gradlew test
```

---

## 📌 Project Status

ARER APP is currently under active development.  
**Current Milestone**: Phase 4 Complete & Verified.  
**Next Milestone**: Phase 5 — MDM Calculation Engine.

---

## 📜 License & Contributions

License: **Not yet specified.**  
Contributions, feedback, and architectural suggestions are welcome via Pull Requests.
