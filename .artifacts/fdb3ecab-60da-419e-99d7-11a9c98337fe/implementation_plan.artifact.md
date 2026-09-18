# To-Do List App Implementation Plan

This plan covers the development of an Android To-Do List application that features Google Login, local caching with SQLite (via Room), remote syncing with Firebase Realtime Database, and a clean, animated UI using Jetpack Compose.

## User Review Required

> [!IMPORTANT]
> **Firebase Configuration:** For Firebase Auth and Realtime Database to work, you will need to manually set up a Firebase Project, enable Google Authentication and Realtime Database, and download the `google-services.json` file into the `app/` directory.

> [!NOTE]
> **Credential Manager vs Legacy Google Sign-In:** I plan to use the modern Android Credential Manager API for Google Login as it is the recommended approach.

## Proposed Changes

### 1. Build & Dependencies

#### [MODIFY] `app/build.gradle.kts`
- Add Firebase BOM, Firebase Auth, Firebase Database, and Play Services Auth (for Credential Manager).
- Add Room dependencies (Runtime, KTX, Compiler) and KSP plugin.
- Add Material 3, Compose Animation, and ViewModel Compose dependencies.

#### [MODIFY] `build.gradle.kts` (Project level)
- Add Google Services plugin (for Firebase).
- Add KSP plugin (for Room).

### 2. Data Layer (Room + Firebase)

#### [NEW] `Task.kt`
- Define the data model for a To-Do item (`id`, `title`, `description`, `isCompleted`, `userId`).

#### [NEW] `TaskDao.kt` (Room)
- DAO interface for SQLite operations (insert, update, delete, get all tasks).

#### [NEW] `AppDatabase.kt` (Room)
- Room Database setup.

#### [NEW] `TaskRepository.kt`
- Repository that acts as a single source of truth. It will handle saving tasks to Room locally and syncing them to Firebase Realtime Database when online.

### 3. Auth Layer

#### [NEW] `AuthManager.kt`
- Wrapper for Firebase Authentication and Credential Manager to handle the Google Sign-In flow securely.

### 4. Presentation Layer (UI & ViewModel)

#### [NEW] `ui/theme/Color.kt` & `Theme.kt`
- Set up a clean, high-contrast light theme.

#### [NEW] `TaskViewModel.kt`
- ViewModel to manage UI state, interact with `TaskRepository`, and handle auth state.

#### [NEW] `ui/LoginScreen.kt`
- A clean login screen with a "Sign in with Google" button.

#### [NEW] `ui/TaskListScreen.kt`
- The main screen displaying the list of tasks.
- Includes a Floating Action Button to add new tasks.
- Uses Compose Animations (`AnimatedVisibility` for adding/removing items, `animateColorAsState` for completion status).

#### [MODIFY] `MainActivity.kt`
- Setup navigation between Login and Task List screens based on authentication state.

## Verification Plan

### Automated Tests
- N/A for this initial implementation phase, but unit tests can be added for the Repository later.

### Manual Verification
1. Build and run the app.
2. Ensure the UI displays the Login screen.
3. Attempt Google Sign-In (requires `google-services.json` and SHA-1 configured in Firebase Console).
4. Add tasks offline (verify they appear in the UI and are saved to SQLite).
5. Verify tasks sync to Firebase Realtime Database.
6. Verify smooth UI animations when adding, completing, or deleting tasks.