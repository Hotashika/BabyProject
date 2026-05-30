# BabyGuard - Smart Baby Monitoring Application

**Developed for Teknofest**

BabyGuard is a comprehensive Android application designed to help parents monitor and track their baby's health, activities, and safety. Built with modern Android development practices, it provides a seamless experience for managing baby-related data and monitoring via advanced features like thermal imaging.

## 🚀 Features

- **User Authentication:** Secure login and signup system for parents.
- **Baby Profile Management:** Register and manage baby information including name and physical stats.
- **Dashboard (Home):** A central hub for quick access to all monitoring tools.
- **Thermal Camera Integration:** Real-time monitoring using thermal imaging technology for enhanced safety.
- **Smart Chatbot:** An integrated assistant to answer parenting questions and provide support.
- **Detailed Reports:** Visualized data and charts (sleep, feeding, health) using advanced charting libraries.
- **Calendar & Notes:** Keep track of important milestones, vaccinations, and daily notes.
- **Admin Panel:** Administrative interface for system management.
- **Profile Management:** Manage user settings and application preferences.

## 🛠️ Tech Stack

- **UI:** [Jetpack Compose](https://developer.android.com/jetpack/compose) (100% Kotlin)
- **Architecture:** MVVM (Model-View-ViewModel)
- **Database:** [Room Persistence Library](https://developer.android.com/training/data-storage/room)
- **Networking:** [Ktor Client](https://ktor.io/)
- **Image Loading:** [Coil](https://coil-kt.github.io/coil/)
- **Data Persistence:** [DataStore Preferences](https://developer.android.com/topic/libraries/architecture/datastore)
- **Navigation:** Jetpack Compose Navigation
- **Visuals:** Material Design 3, Composable-Graphs, yCharts, and cmpcharts.

## 📁 Project Structure

- `auth/`: ViewModels and logic for user authentication.
- `data/`: Data source and repository implementations.
- `room/`: Local database configuration and DAOs.
- `navigation/`: App routing and screen navigation logic.
- `screens/`: UI components for each feature (Home, Thermal, Chatbot, etc.).
- `viewModel/`: Business logic management for UI components.
- `model/`: Data models used across the app.

## 👥 Team Members

This project was developed by a team for **Teknofest**.
- **[Hotashika]** 
- **[burakguducu]** 
- **[iremmiy]**
- **[EmreYigitOzturk]** 

## 📦 Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/your-username/BabyProject.git
   ```
2. Open the project in **Android Studio (Ladybug or newer)**.
3. Sync the project with Gradle files.
4. Ensure you have the necessary SDKs installed (Compile SDK 36, Min SDK 26).
5. Build and run the app on an emulator or physical device.

## 🤝 Contributing

Contributions are welcome! Feel free to open issues or submit pull requests to improve the project.

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
