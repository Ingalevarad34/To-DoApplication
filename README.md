# To-Do Application (UpTodo Clone)

A high-fidelity, production-ready Android Todo application built with **Jetpack Compose**, **Material 3**, **Firebase Authentication**, and **Firebase Realtime Database**. Designed to mirror the sleek dark-mode aesthetics and workflows of the **UpTodo** design system.

---

## 📱 Complete Screen & Component Breakdown

### 1. Onboarding & Authentication Flow

#### 🟢 Splash Screen (`ui/onboarding/IntroScreen.kt`)
- **Location**: `com.example.to_doapplication.ui.onboarding.IntroScreen`
- **Contents**: Centered UpTodo checkmark logo and app title.
- **Workflow**: Displays for 2 seconds upon launch before automatically transitioning to the Onboarding slider (if unauthenticated).

#### 🟢 Onboarding Slider (`ui/onboarding/OnboardingScreen.kt`)
- **Location**: `com.example.to_doapplication.ui.onboarding.OnboardingScreen`
- **Contents**:
  - Top "SKIP" button.
  - 3-step feature slider displaying high-resolution illustration cards (`frame_161.png`, `frame_162.png`, `frame_182.png`).
  - Titles and descriptions: "Manage your tasks", "Create daily routine", "Organize your tasks".
  - Navigation controls: "BACK" and "NEXT" / "GET STARTED" buttons.

#### 🟢 Start Screen (`ui/auth/StartScreen.kt`)
- **Location**: `com.example.to_doapplication.ui.auth.StartScreen`
- **Contents**: Top back arrow, "Welcome to UpTodo" headline, subtitle text, and dual call-to-action buttons: **LOGIN** (primary button) and **CREATE ACCOUNT** (outlined button).

#### 🟢 Login Screen (`ui/auth/LoginScreen.kt`)
- **Location**: `com.example.to_doapplication.ui.auth.LoginScreen`
- **Contents**:
  - Email field with validation.
  - Password field with password visual transformation.
  - Red error message display area for Firebase authentication errors.
  - **Login** button.
  - **Login with Google** button powered by `AuthManager`.
  - Link to switch to the Register screen.

#### 🟢 Register Screen (`ui/auth/RegisterScreen.kt`)
- **Location**: `com.example.to_doapplication.ui.auth.RegisterScreen`
- **Contents**:
  - Email field.
  - Password and Confirm Password fields.
  - Input validation (checking empty fields and password match).
  - Red error feedback text for Firebase creation errors.
  - **Register** and **Register with Google** buttons.

---

### 2. Main Application & Bottom Navigation

#### 🟢 Main Container & Navigation Drawer (`ui/main/MainScreen.kt`)
- **Location**: `com.example.to_doapplication.ui.main.MainScreen`
- **Contents**:
  - **ModalNavigationDrawer**: Animated slide-out drawer accessible by tapping the hamburger menu (`☰`) on the Home screen. Contains user avatar (`baked_goods_1`), display name, live task stats ("Task left" vs "Task done"), settings menu items, and Log out.
  - **Scaffold**: Hosts the 4 bottom tabs (**Index**, **Calendar**, **Focus**, **Profile**).
  - **Floating Action Button (FAB)**: Prominent central `+` button positioned over the bottom bar to launch the Add Task bottom sheet.

#### 🟢 Home / Index Screen (`ui/home/IndexScreen.kt`)
- **Location**: `com.example.to_doapplication.ui.home.IndexScreen`
- **Contents**:
  - Top header with hamburger menu button, "Index" title, and user profile icon.
  - **Search Bar**: Live global search field filtering tasks across titles and descriptions.
  - **Filter Dropdown Button**: Toggle between **Today (Active)**, **Completed**, and **All Tasks**.
  - **Task Cards**: Each card displays a radio button for instant completion toggle, title, subtitle time ("Today At 16:45"), colored category pill, and priority flag badge.
  - **Empty States**: Shows "What do you want to do today?" illustration if no tasks exist, or "No task pending" info icon if current search/filter returns no results.

#### 🟢 Add Task Bottom Sheet (`ui/home/AddTaskBottomSheet.kt`)
- **Location**: `com.example.to_doapplication.ui.home.AddTaskBottomSheet`
- **Contents**:
  - Text fields for Task Title and Description.
  - **Date Picker Icon**: Launches Material 3's native `DatePickerDialog` showing the current year/month/day.
  - **Category Icon**: Launches a 3-column grid dialog displaying 10 colored category options (Grocery, Work, Sport, Design, University, Social, Music, Health, Movie, Home).
  - **Priority Icon**: Launches a 4x3 flag grid dialog for selecting priorities 1 through 10.
  - **Send Button**: Saves the task directly to Firebase Realtime Database under `users/{userId}/tasks/{taskId}`.

#### 🟢 Task Detail & Edit Screen (`ui/home/TaskDetailScreen.kt`)
- **Location**: `com.example.to_doapplication.ui.home.TaskDetailScreen`
- **Contents**:
  - Top bar with back arrow and red **Delete** trash icon.
  - Editable fields for Task Title and Description.
  - Checkbox toggle for completion status.
  - **Save Changes** button updating the task in Firebase.

---

### 3. Feature Tabs

#### 🟢 Calendar Screen (`ui/calendar/CalendarScreen.kt`)
- **Location**: `com.example.to_doapplication.ui.calendar.CalendarScreen`
- **Contents**:
  - Top header "Calendar".
  - **Interactive Calendar Strip**: Horizontal `LazyRow` allowing scrolling and tapping across 30 days in the past and 30 days in the future to filter tasks by date.
  - **Today / Completed Tabs**: Switch between active and finished tasks.
  - Smart fallback displaying completed tasks and "No task pending" empty state.

#### 🟢 Focus Screen (`ui/focus/FocusScreen.kt`)
- **Location**: `com.example.to_doapplication.ui.focus.FocusScreen`
- **Contents**:
  - "Focus Mode" title and description.
  - Large circular countdown timer (25-minute Pomodoro mode).
  - **Start Focus / Pause Focus** button and **Reset Timer** button.

#### 🟢 Profile Screen (`ui/profile/ProfileScreen.kt`)
- **Location**: `com.example.to_doapplication.ui.profile.ProfileScreen`
- **Contents**:
  - Header, circular avatar image (`baked_goods_1`), and user display name.
  - Dynamic task counters (**X Task left**, **Y Task done**).
  - Categorized settings options: **Settings** (App Settings), **Account** (Change name, password, image), **Uptodo** (About US, FAQ, Help & Feedback, Support US).
  - Red **Log out** button invoking `AuthManager.signOut()`.

---

## 🛠️ Data Layer & Backend

- **`data/Task.kt`**: Model class containing `id`, `title`, `description`, `isCompleted` (mapped via `@PropertyName("completed")` for Firebase JSON compatibility), `userId`, `timestamp`, `categoryId`, `priority`, `dueDate`, `dueTime`.
- **`data/TaskRepository.kt`**: Connects directly to Firebase Realtime Database path `users/{userId}/tasks`. Uses `ValueEventListener` to stream real-time task updates via Kotlin `callbackFlow`.
- **`auth/AuthManager.kt`**: Manages `FirebaseAuth` instance, email/password creation/sign-in, and Google Identity Credential Manager API interactions.
- **`ui/TaskViewModel.kt`**: Exposes `StateFlow<List<Task>>` and `StateFlow<Boolean>` for UI consumption, executing coroutine tasks for task insertion, updates, toggles, and deletions.

---

## 🔥 Step-by-Step Firebase Setup Guide

To connect your own Firebase backend:

1. **Create Firebase Project**: Go to [Firebase Console](https://console.firebase.google.com/), create a project, and add an Android app with package name `com.example.to_doapplication`.
2. **Download Config**: Download `google-services.json` and place it in `ToDoApplication2/app/google-services.json`.
3. **Enable Auth**: Under **Authentication** -> **Sign-in method**, enable **Email/Password** and **Google**.
4. **Set Realtime Database Rules**: Under **Realtime Database** -> **Rules**, set:
   ```json
   {
     "rules": {
       "users": {
         "$uid": {
           ".read": "$uid === auth.uid",
           ".write": "$uid === auth.uid"
         }
       }
     }
   }
   ```

---

## 🚀 How to Run
1. Open the project in Android Studio.
2. Sync with Gradle Files.
3. Select an emulator or connected device (API 24+).
4. Run `app` (`Shift + F10`).
