/**
 * Created by Elena Neacsu on 16/03/2021
 */

object Versions {
    const val compileSdkVersion = 30
    const val buildToolsVersion = "29.0.3"
    const val minSdkVersion = 22
    const val targetSdkVersion = 30
    const val kotlin = "1.3.72"
    const val gradlePlugin = "4.1.2"
    const val androidxAppCompat = "1.2.0"
    const val androidxCardView = "1.0.0"
    const val androidxCore = "1.3.2"
    const val androidxConstraintLayout = "2.0.4"
    const val androidxCoordinatorLayout = "1.1.0"
    const val dagger = "2.33"
    const val fragment = "1.3.0"
    const val googleServices = "4.3.5"
    const val lifecycle = "2.3.0"
    const val material = "1.4.0-alpha01"
    const val multidex = "2.0.1"
    const val navigation = "2.3.4"
    const val okHttpLogginInterceptor = "4.9.0"
    const val recyclerView = "1.1.0"
    const val retrofit = "2.9.0"
    const val room = "2.2.6"
    const val swipeRefreshLayout = "1.0.0"
    const val junit = "4.13"
    const val junitTest = "1.1.1"
    const val espressoTest = "3.2.0"
    const val gradleVersionsPlugin = "0.28.0"
}

object Deps {
    const val kotlinGradlePlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin}"
    const val gradlePlugin = "com.android.tools.build:gradle:${Versions.gradlePlugin}"
    const val kotlinStdLib = "org.jetbrains.kotlin:kotlin-stdlib-jdk7:${Versions.kotlin}"

    // AndroidX

    const val androidxAppCompat = "androidx.appcompat:appcompat:${Versions.androidxAppCompat}"
    const val androidxCardView = "androidx.cardview:cardview:${Versions.androidxCardView}"
    const val androidxCore = "androidx.core:core-ktx:${Versions.androidxCore}"
    const val androidxConstraintLayout = "androidx.constraintlayout:constraintlayout:${Versions.androidxConstraintLayout}"
    const val androidxCoordinatorLayout = "androidx.coordinatorlayout:coordinatorlayout:${Versions.androidxCoordinatorLayout}"
    const val androidxRecyclerView = "androidx.recyclerview:recyclerview:${Versions.recyclerView}"
    const val androidxSwipeRefreshLayout = "androidx.swiperefreshlayout:swiperefreshlayout:${Versions.swipeRefreshLayout}"

    // Dagger

    const val daggerRuntime = "com.google.dagger:dagger:${Versions.dagger}"
    const val daggerAndroid = "com.google.dagger:dagger-android:${Versions.dagger}"
    const val daggerAndroidSupport = "com.google.dagger:dagger-android-support:${Versions.dagger}"
    const val daggerCompiler = "com.google.dagger:dagger-compiler:${Versions.dagger}"
    const val daggerSupportCompiler = "com.google.dagger:dagger-android-processor:${Versions.dagger}"

    // Fragment

    const val fragmentRuntime = "androidx.fragment:fragment:${Versions.fragment}"
    const val fragmentRuntimeKtx = "androidx.fragment:fragment-ktx:${Versions.fragment}"
    const val fragmentTesting = "androidx.fragment:fragment-testing:${Versions.fragment}"

    // Google
    const val googleServices = "com.google.gms:google-services:${Versions.googleServices}"

    // Lifecycle

    const val lifecycleRuntime = "androidx.lifecycle:lifecycle-runtime:${Versions.lifecycle}"
    const val lifecycleJava8 = "androidx.lifecycle:lifecycle-common-java8:${Versions.lifecycle}"
    const val lifecycleCompiler = "androidx.lifecycle:lifecycle-compiler:${Versions.lifecycle}"
    const val lifecycleRuntimeKtx = "androidx.lifecycle:lifecycle-runtime-ktx:${Versions.lifecycle}"
    const val lifecycleViewmodelKtx = "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.lifecycle}"
    const val lifecycleLivedataKtx = "androidx.lifecycle:lifecycle-livedata-ktx:${Versions.lifecycle}"

    const val material = "com.google.android.material:material:${Versions.material}"
    const val multidex = "androidx.multidex:multidex:${Versions.multidex}"


    // Navigation

    const val navigationRuntime = "androidx.navigation:navigation-runtime:${Versions.navigation}"
    const val navigationRuntimeKtx = "androidx.navigation:navigation-runtime-ktx:${Versions.navigation}"
    const val navigationFragment = "androidx.navigation:navigation-fragment:${Versions.navigation}"
    const val navigationFragmentKtx = "androidx.navigation:navigation-fragment-ktx:${Versions.navigation}"
    const val navigationDynamicFeaturesFragment = "androidx.navigation:navigation-dynamic-features-fragment:${Versions.navigation}"
    const val navigationTesting = "androidx.navigation:navigation-testing:${Versions.navigation}"
    const val navigationUi = "androidx.navigation:navigation-ui:${Versions.navigation}"
    const val navigationUiKtx = "androidx.navigation:navigation-ui-ktx:${Versions.navigation}"
    const val navigationSafeArgsPlugin = "androidx.navigation:navigation-safe-args-gradle-plugin:${Versions.navigation}"

    // OkHttp

    const val okhttp = "com.squareup.okhttp3:okhttp:${Versions.okHttpLogginInterceptor}"
    const val okhttpLoggingInterceptor = "com.squareup.okhttp3:logging-interceptor:${Versions.okHttpLogginInterceptor}"

    // Retrofit

    const val retrofitRuntime = "com.squareup.retrofit2:retrofit:${Versions.retrofit}"
    const val retrofitGson = "com.squareup.retrofit2:converter-gson:${Versions.retrofit}"
    const val retrofitConverterScalars = "com.squareup.retrofit2:converter-scalars:${Versions.retrofit}"
    const val retrofitMock = "com.squareup.retrofit2:retrofit-mock:${Versions.retrofit}"

    // Room

    const val roomRuntime = "androidx.room:room-runtime:${Versions.room}"
    const val roomCompiler = "androidx.room:room-compiler:${Versions.room}"
    const val roomKtx = "androidx.room:room-ktx:${Versions.room}"
    const val roomTesting = "androidx.room:room-testing:${Versions.room}"



    const val junit = "junit:junit:${Versions.junit}"
    const val junitTest = "androidx.test.ext:junit:${Versions.junitTest}"
    const val espressoTest = "androidx.test.espresso:espresso-core:${Versions.espressoTest}"
    const val gradleVersionsPlugin = "com.github.ben-manes:gradle-versions-plugin:${Versions.gradleVersionsPlugin}"
}