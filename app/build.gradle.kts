plugins {
    id ("com.android.application")
    id ("kotlin-android")
    id ("kotlin-kapt")
    id ("dagger.hilt.android.plugin")
}

android {
    compileSdk = 31
    buildToolsVersion = "30.0.3"

    defaultConfig {
        applicationId = "me.rerere.zhiwang"
        minSdk = 26
        targetSdk = 31
        versionCode = 12
        versionName = "2.0.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles (getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility (JavaVersion.VERSION_1_8)
        targetCompatibility (JavaVersion.VERSION_1_8)
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = composeVersion
    }
}

dependencies {
    implementation ("androidx.core:core-ktx:1.7.0")
    implementation ("androidx.appcompat:appcompat:1.3.1")
    implementation ("com.google.android.material:material:1.4.0")

    // Compose Lib
    implementation ("androidx.compose.ui:ui:$composeVersion")
    implementation ("androidx.compose.material:material:$composeVersion")
    implementation("androidx.compose.material3:material3:1.0.0-alpha01")
    implementation ("androidx.compose.ui:ui-tooling:$composeVersion")
    implementation ("androidx.lifecycle:lifecycle-runtime-ktx:2.4.0")
    implementation ("androidx.activity:activity-compose:1.4.0")
    implementation ("androidx.compose.runtime:runtime-livedata:$composeVersion")

    // splash
    implementation("androidx.core:core-splashscreen:1.0.0-alpha02")

    // Dialog
    implementation ("io.github.vanpra.compose-material-dialogs:core:0.6.1")

    // Coil
    implementation("io.coil-kt:coil-compose:1.3.2")

    // Hilt
    implementation ("com.google.dagger:hilt-android:$hiltVersion")
    kapt ("com.google.dagger:hilt-compiler:$hiltVersion")
    implementation ("androidx.hilt:hilt-navigation-compose:1.0.0-alpha03")

    // Paging3
    implementation ("androidx.paging:paging-runtime-ktx:3.1.0-beta01")
    implementation ("androidx.paging:paging-compose:1.0.0-alpha14")

    // 图标扩展
    implementation ("androidx.compose.material:material-icons-extended:$composeVersion")

    // Navigation for JetpackCompose
    implementation ("androidx.navigation:navigation-compose:2.4.0-beta01")

    // accompanist
    // Pager
    implementation ("com.google.accompanist:accompanist-pager:$accVersion")
    // Swipe to refresh
    implementation ("com.google.accompanist:accompanist-swiperefresh:$accVersion")
    // 状态栏颜色
    implementation ("com.google.accompanist:accompanist-systemuicontroller:$accVersion")
    // Insets
    implementation ("com.google.accompanist:accompanist-insets:$accVersion")
    implementation ("com.google.accompanist:accompanist-insets-ui:$accVersion")
    // Flow
    implementation ("com.google.accompanist:accompanist-flowlayout:$accVersion")
    // Placeholder
    implementation ("com.google.accompanist:accompanist-placeholder-material:$accVersion")

    // Retrofit
    implementation ("com.squareup.okhttp3:okhttp:5.0.0-alpha.2")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation ("com.squareup.okhttp3:logging-interceptor:5.0.0-alpha.2")

    // JSOUP
    implementation ("org.jsoup:jsoup:1.13.1")

    // 约束布局
    implementation ("androidx.constraintlayout:constraintlayout:2.1.1")
    implementation ("androidx.constraintlayout:constraintlayout-compose:1.0.0-rc01")


    testImplementation ("junit:junit:4.+")
    androidTestImplementation ("androidx.test.ext:junit:1.1.3")
    androidTestImplementation ("androidx.test.espresso:espresso-core:3.4.0")
    androidTestImplementation ("androidx.compose.ui:ui-test-junit4:$composeVersion")
}