import java.util.*

val properties = Properties().apply {
    load(rootProject.file("local.properties").reader())
}
val baseUrl = properties["base_url"]


plugins {
    id("com.android.application")
}

android {
    namespace = "com.tech.cybercars"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.tech.cybercars"
        minSdk = 28
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        debug {
            buildConfigField("String", "BASE_URL", "\"$baseUrl\"")
        }
        release {
            buildConfigField("String", "BASE_URL", "\"$baseUrl\"")
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    buildFeatures {
        buildConfig = true
        dataBinding = true
        viewBinding = true
    }
}

dependencies {
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.legacy:legacy-support-v4:1.0.0")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.6.2")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2")
    implementation("androidx.navigation:navigation-fragment:2.7.6")
    implementation("androidx.navigation:navigation-ui:2.7.6")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    //country picker
    implementation("com.hbb20:ccp:2.7.3")

    //Retrofit & Gson - call API and convert data to java object
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    //lifecycle extension
    implementation("androidx.lifecycle:lifecycle-extensions:2.2.0")

    //room database and RxJava support
    val room_version = "2.6.1"
    implementation("androidx.room:room-runtime:$room_version")
    annotationProcessor("androidx.room:room-compiler:$room_version")
    implementation("androidx.room:room-rxjava2:$room_version")

    //RxJava
    implementation("io.reactivex.rxjava2:rxandroid:2.1.1")

    //scalable size Units
    implementation("com.intuit.sdp:sdp-android:1.1.0")
    implementation("com.intuit.ssp:ssp-android:1.1.0")

    //Rounded image View
    implementation("com.makeramen:roundedimageview:2.3.0")

    //handle phone number
    implementation("com.googlecode.libphonenumber:libphonenumber:8.13.27")

    //mapbox
    implementation("com.mapbox.mapboxsdk:mapbox-android-sdk:9.7.0")
    implementation("com.mapbox.mapboxsdk:mapbox-android-plugin-places-v9:0.12.0")
    implementation("com.mapbox.mapboxsdk:mapbox-sdk-services:5.5.0")

    //load load img online
    implementation("com.github.bumptech.glide:glide:4.16.0")

    //gif
    implementation("pl.droidsonroids.gif:android-gif-drawable:1.2.28")

    //pull-to-refresh
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")

    //skeleton loading
    implementation("com.facebook.shimmer:shimmer:0.1.0@aar")

    //request permission
    implementation("io.github.ParkSangGwon:tedpermission-normal:3.3.0")

}

