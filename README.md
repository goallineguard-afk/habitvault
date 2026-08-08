# HabitVault

A minimalistic, discipline-first habit tracker for Android.

## Description

HabitVault is a production-quality Android habit tracker built for discipline, not gamification.

Unlike apps that reward you with streaks and confetti, HabitVault uses a **Discipline Score** — a weighted, rolling measure of consistency that punishes patterns of failure exponentially while rewarding sustained effort. Below 70? You're failing. No participation trophies.

## Core Principles

- **Offline-first** — works without internet, ever
- **Privacy-first** — no accounts, no cloud, no ads, no tracking
- **Discipline over streaks** — consistency matters more than perfect runs
- **Minimal UI** — one-tap completion, no clutter, no distractions

## Features

| Feature | Status |
|---------|--------|
| One-tap habit completion | ✅ |
| Discipline score engine | ✅ |
| Streak tracking | ✅ |
| Dark mode | ✅ |
| Local persistence (Room/SQLite) | ✅ |
| Habit reordering | ✅ |
| Daily journal | ✅ |
| Goal setting | ✅ |
| Statistics dashboard | ✅ |
| Data export | ✅ |

## Tech Stack

| Layer | Technology |
|-------|------------|
| Language | Kotlin 1.9.22 |
| UI | Jetpack Compose + Material3 |
| Architecture | MVVM + Clean Architecture |
| Database | Room (SQLite) |
| DI | Hilt |
| Async | Kotlin Coroutines + Flow |
| Build | Gradle 8.4 |

## Build Locally

### Prerequisites

- Java JDK 17+
- Android SDK (API 34)

### Commands

```bash
# Debug APK
./gradlew assembleDebug

# Release APK
./gradlew assembleRelease

# Run tests
./gradlew test
```

APK output:
- Debug: `app/build/outputs/apk/debug/app-debug.apk`
- Release: `app/build/outputs/apk/release/app-release.apk`

## License

Distributed under the MIT License.

Built with discipline. No compromises.
