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

    //Picasso - loading and catching download from internet
    implementation("com.squareup.picasso:picasso:2.71828")

    //lifecycle extension
    implementation("androidx.lifecycle:lifecycle-extensions:2.2.0")

    //room and RxJava support
    implementation("androidx.room:room-runtime:2.6.1")
    annotationProcessor("androidx.room:room-compiler:2.6.1")
    implementation("androidx.room:room-rxjava2:2.6.1")

    //RxJava
    implementation("io.reactivex.rxjava2:rxandroid:2.1.1")

    //scalable size Units
    implementation("com.intuit.sdp:sdp-android:1.1.0")
    implementation("com.intuit.ssp:ssp-android:1.1.0")

    //Rounded image View
    implementation("com.makeramen:roundedimageview:2.3.0")

    //handle phone number
    implementation("com.googlecode.libphonenumber:libphonenumber:8.13.27")

}

