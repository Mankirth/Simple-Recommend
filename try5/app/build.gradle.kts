plugins {
    id("com.android.application")
}

android {
    namespace = "com.example.try5"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.try5"
        minSdk = 24
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    buildFeatures {
        viewBinding = true
    }
}

//repositories {
//    mavenCentral()
//    maven("https://jitpack.io")
//    // OR another notation:
//    // maven {
//    //     url = uri("https://jitpack.io")
//    // }
//}

dependencies {
        // your app dependencies

    implementation ("androidx.browser:browser:1.7.0")
    implementation ("com.google.code.gson:gson:2.10.1")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.10.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    //implementation(files("C:\\Users\\fireb\\android-sdk-0.8.0-appremote_v2.1.0-auth\\android-sdk-0.8.0-appremote_v2.1.0-auth\\app-remote-lib\\spotify-app-remote-release-0.8.0.aar"))
    implementation("androidx.navigation:navigation-fragment:2.7.5")
    implementation("androidx.navigation:navigation-ui:2.7.5")
    implementation("com.spotify.android:auth:1.2.5")
    implementation("com.github.kaaes:spotify-web-api-android:0.4.1")
    //implementation("androidx.appcompat:appcompat:1.7.0-alpha03")
    //implementation("com.google.android.gms:play-services-auth:20.7.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}