# HabitFlow

HabitFlow is an Android app designed to help users build and maintain daily habits through task management, voice journaling, and motivational video riddles. The app leverages Firebase for authentication, data storage, and media hosting to deliver a smooth and engaging user experience.

---

## Features

- **User Authentication**  
  Sign up, login, and password reset powered by Firebase Authentication.

- **Task Management**  
  - Add, view, and mark daily habits/tasks as completed.  
  - Separate tabs for "To Do" and "Finished" tasks.  
  - Track task completion progress and daily streaks.

- **Voice Recorder**  
  Record and upload daily voice notes to Firebase Storage with metadata saved in Firestore. View and listen to recent recordings.

- **Video Player**  
  Browse and play curated motivational and riddle videos via a swipeable video player.

- **Dashboard**  
  Personalized greeting with user name from Firestore.  
  Time-based motivational messages.  
  Overview of tasks completed, streaks, and voice log counts.  
  Quick access buttons for adding tasks and recording voice notes.

- **Modern UI**  
  Implements edge-to-edge design, bottom navigation bar, and tabbed interfaces with ViewPager2.

---

## Tech Stack

- Android Studio (Java)  
- Firebase Authentication, Firestore, Firebase Storage  
- Android Architecture Components: ViewModel, LiveData  
- RecyclerView, ViewPager2, TabLayout  
- MediaRecorder for audio capture and handling runtime permissions

---

## Installation & Setup

1. **Clone the repository:**

   ```bash
   git clone https://github.com/yourusername/habitflow.git
   cd habitflow
Open in Android Studio:
Open the project folder in Android Studio.

Set up Firebase:

Create a Firebase project at Firebase Console.

Add an Android app to your Firebase project and download the google-services.json file.

Place the google-services.json file inside the app/ directory of the project.

Enable Email/Password Authentication in Firebase Authentication.

Set up Cloud Firestore database in test mode or with appropriate security rules.

Build and run:
Connect an Android device or use an emulator and run the app.

Project Structure
Activities:
SplashScreen, LoginActivity, SignUpActivity, MainActivity — handle authentication flow and main navigation.

Fragments:
HomeFragment, DashboardFragment, ToDoListFragment, FinishedListFragment, VoiceRecorderFragment, VideoPlayerFragment — modular UI components managing tasks, recordings, videos, and dashboard.

ViewModels:
TaskViewModel, VoiceLogViewModel — handle data logic, observe Firestore, and provide live updates to UI.

Adapters:
RecyclerView and ViewPager2 adapters for displaying lists and video content.

Classes:
Helper classes including AuthManager for Firebase Authentication management and data models such as Task, VoiceLog, and Video.

Usage
Launch the app and create a new account or log in.

Navigate through the app using the bottom navigation bar.

Add and complete daily tasks under the "Home" tab.

View your daily progress, streaks, and voice recordings in the "Dashboard".

Record daily voice notes and review past recordings under the "Recorder" tab.

Watch motivational and riddle videos in the "Video Player" tab.

Contribution
Contributions, issues, and feature requests are welcome! Feel free to check issues page.

License
This project is licensed under the MIT License — see the LICENSE file for details.

Contact
Developed by [Asma Hasan]
Email: ajhasan4321@gmail.com
GitHub: https://github.com/ajhasan183
