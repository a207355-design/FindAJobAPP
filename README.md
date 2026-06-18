# Find a Job App - Project 2

## Project Description
The "Find a Job App" is an Android mobile application developed using Kotlin and Jetpack Compose. It aims to connect job seekers with potential employers, providing real-time opportunities to secure stable incomes.

## SDG Theme
**SDG 1: No Poverty**
This app addresses the problem of unemployment and underemployment, which are major drivers of poverty, by reducing information barriers in the job market.

## Key Features
* **Minimum 7 Screens**: Includes Find, Community, Favorite, Message, Profile, Job Detail, and Chat screens.
* **Sensor Integration**: Uses the device's Location/GPS sensor to detect the user's current location (e.g., Bangi) for filtering job opportunities.
* **External Web API**: Integrates the News API via Retrofit to fetch and display real-time news articles about specific companies.
* **Local Data Persistence**: Utilizes a Room Database to allow users to save and view their favorite jobs locally on the device.
* **Cloud Data Integration**: Employs Firebase Firestore to enable a shared "Community" feed where users can remotely store and view shared opportunities.

## Setup Instructions
1. Clone this repository to your local machine.
2. Open the project in Android Studio (Giraffe or newer recommended).
3. Ensure you have the `google-services.json` file placed in the `app/` directory for Firebase functionality.
4. Sync the Gradle files to download all necessary dependencies (Jetpack Compose, Room, Retrofit, Firebase).
5. Build and run the application on an Android Emulator or a physical Android device.
