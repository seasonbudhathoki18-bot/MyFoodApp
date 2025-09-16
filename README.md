MyFoodApp 🍽️

Android app for NIT3213 Final Assignment — Login → Dashboard (Food) → Details using MVVM + Hilt + Retrofit.

Student: Sijan
Student ID: 8195011
Topic: Food

✨ Features

Login with first name + student ID digits (no leading “s”)

Dashboard lists food entities (shows only property1, property2)

Details screen shows full entity data (including description)

Clean architecture: MVVM + Repository + DI (Hilt)

Networking: Retrofit + OkHttp + Gson

🧪 API

Base URL:

https://nit3213api.onrender.com/

Auth (choose campus path)

Use one of the following (default is sydney/auth):

POST /sydney/auth
POST /footscray/auth
POST /br/auth


Body

{
  "username": "Sijan",
  "password": "8195011"
}

🧱 Tech Stack

Language: Kotlin

Min SDK: 23

Compile/Target SDK: 34

Architecture: MVVM (ViewModel + Repository)

DI: Hilt

HTTP: Retrofit, OkHttp, Gson

UI: RecyclerView, ViewBinding

Build System: Gradle (KTS)

🗂 Project Structure
app/
 ├─ src/main/
 │   ├─ AndroidManifest.xml
 │   ├─ java/com/example/myfoodapp/
 │   │  ├─ MyApp.kt
 │   │  ├─ NetworkModule.kt
 │   │  ├─ ApiService.kt
 │   │  ├─ LoginRequest.kt / LoginResponse.kt
 │   │  ├─ MainActivity.kt
 │   │  ├─ DashboardActivity.kt
 │   │  ├─ DetailsActivity.kt
 │   │  └─ DynamicEntityAdapter.kt
 │   └─ res/layout/
 │       ├─ activity_main.xml
 │       ├─ activity_dashboard.xml
 │       ├─ activity_details.xml
 │       └─ row_dynamic_entity.xml
 └─ build.gradle.kts

⚙️ Setup
Prerequisites

Android Studio Hedgehog or newer

Gradle JDK: 17

Clone
git clone https://github.com/seasonbudhathoki18-bot/MyFoodApp.git
cd MyFoodApp

Open & Sync

Open in Android Studio

Click Sync Project with Gradle Files

Wait for dependencies to download

▶️ Build & Run

Connect an Android device or start an emulator

Click Run (▶️)

Login credentials for marking:

Username: Sijan

Password: 8195011

📸 Screenshots
Login Screen

Dashboard Screen

Details Screen

📑 Verification Checklist

Dependencies declared and project syncs on fresh clone

All required libraries are in app/build.gradle.kts.

App demonstrates Login → Dashboard → Details per spec

Login → Dashboard → Details working with API.

Code organized with MVVM + Hilt + Retrofit

MVVM pattern, Hilt DI, Retrofit API calls implemented.
