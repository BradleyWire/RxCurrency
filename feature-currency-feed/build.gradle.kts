plugins {
    id(BuildPlugins.Android.library)
    id(BuildPlugins.Kotlin.android)
    id(BuildPlugins.Kotlin.androidExtensions)
    id(BuildPlugins.Kotlin.kapt)
    id(ScriptPlugins.buildConfigLibrary)
}

android {
    compileSdkVersion(AndroidSdk.compile)

    defaultConfig {
        minSdkVersion(AndroidSdk.min)
        targetSdkVersion(AndroidSdk.target)
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = TestLibraries.testRunner
    }
}

dependencies {
    implementation(project(Modules.Core.network))
    implementation(project(Modules.Core.storage))

    implementation(Libraries.kotlinStdLib)
    implementation(Libraries.appCompat)
    implementation(Libraries.constraint)
    implementation(Libraries.coroutines)
    implementation(Libraries.Retrofit.core)
    implementation(Libraries.Retrofit.gsonConverter)
    implementation(Libraries.Ktx.core)
    implementation(Libraries.Koin.core)
    implementation(Libraries.Koin.viewModel)
    implementation(Libraries.Lifecycle.runtime)
    implementation(Libraries.Lifecycle.liveData)
    implementation(Libraries.Lifecycle.viewModel)
    implementation(Libraries.Lifecycle.extensions)
    implementation(Libraries.recyclerView)

    testImplementation(TestLibraries.junit4)
    testImplementation(TestLibraries.coroutines)
    testImplementation(TestLibraries.Mockito.core)
    testImplementation(TestLibraries.room)
}
