# 🔧 Sorun Giderme Rehberi (Troubleshooting Guide)

Bu dokümantasyon, Mete Eğitici Çocuk Uygulaması'nda karşılaşılabilecek yaygın sorunlar ve çözümlerini içerir.

## İçindekiler

1. [Kurulum Sorunları](#kurulum-sorunları)
2. [Derleme Hataları](#derleme-hataları)
3. [Çalışma Zamanı Sorunları](#çalışma-zamanı-sorunları)
4. [Performans Sorunları](#performans-sorunları)
5. [Ses ve Görüntü Sorunları](#ses-ve-görüntü-sorunları)
6. [Database Sorunları](#database-sorunları)
7. [Network Sorunları](#network-sorunları)
8. [Cihaz Özel Sorunlar](#cihaz-özel-sorunlar)

---

## Kurulum Sorunları

### Problem: Gradle Sync Başarısız

**Hata Mesajı:**
```
Gradle sync failed: ...
```

**Çözüm 1: Gradle Wrapper Güncellemesi**
```bash
cd /home/runner/work/mehmet/mehmet
./gradlew wrapper --gradle-version=8.2
./gradlew clean
./gradlew build
```

**Çözüm 2: Gradle Cache Temizleme**
```bash
# Linux/Mac
rm -rf ~/.gradle/caches/
rm -rf ~/.gradle/wrapper/

# Windows
rmdir /s %USERPROFILE%\.gradle\caches
rmdir /s %USERPROFILE%\.gradle\wrapper

# Sonra gradle sync tekrar
./gradlew --refresh-dependencies
```

**Çözüm 3: JDK Kontrolü**
```bash
# JDK versiyonunu kontrol et
java -version
# Beklenen: openjdk version "17.0.11"

# JAVA_HOME kontrolü
echo $JAVA_HOME
# Beklenen: /path/to/jdk-17.0.11
```

### Problem: SDK Bulunamadı

**Hata Mesajı:**
```
SDK location not found. Define location with sdk.dir in the local.properties file
```

**Çözüm:**
```bash
# local.properties dosyası oluştur
cat > local.properties << EOF
sdk.dir=/Users/<username>/Library/Android/sdk
# Windows: sdk.dir=C\:\\Users\\<username>\\AppData\\Local\\Android\\Sdk
# Linux: sdk.dir=/home/<username>/Android/Sdk
EOF
```

### Problem: Dependency Resolution Failed

**Hata Mesajı:**
```
Could not resolve all dependencies for configuration ':app:debugRuntimeClasspath'.
```

**Çözüm 1: Repository Kontrolü**
```kotlin
// build.gradle.kts (root)
allprojects {
    repositories {
        google()
        mavenCentral()
        // JCenter kaldırıldı, kullanmayın
    }
}
```

**Çözüm 2: Offline Mode Kapatma**
```bash
# Android Studio'da
# File > Settings > Build, Execution, Deployment > Gradle
# "Offline work" seçeneğini kaldır
```

**Çözüm 3: Gradle Clean**
```bash
./gradlew clean --refresh-dependencies
```

---

## Derleme Hataları

### Problem: Kotlin Compiler Error

**Hata Mesajı:**
```
Kotlin: [version] Incompatible classes were found in dependencies
```

**Çözüm:**
```kotlin
// build.gradle.kts (app)
kotlin {
    jvmToolchain(17)
}

kotlinOptions {
    jvmTarget = "17"
    freeCompilerArgs += listOf(
        "-opt-in=kotlin.RequiresOptIn"
    )
}
```

### Problem: R class generation failed

**Hata Mesajı:**
```
Cannot find symbol R.layout.activity_main
```

**Çözüm 1: Clean and Rebuild**
```bash
./gradlew clean
./gradlew assembleDebug
```

**Çözüm 2: Invalidate Caches**
```
Android Studio > File > Invalidate Caches / Restart
```

**Çözüm 3: XML Hata Kontrolü**
```xml
<!-- Tüm XML dosyalarında syntax hatası olup olmadığını kontrol et -->
<!-- Layout dosyalarını tek tek aç ve hatayı bul -->
```

### Problem: ViewBinding Not Generated

**Hata Mesajı:**
```
Unresolved reference: ActivityMainBinding
```

**Çözüm:**
```kotlin
// build.gradle.kts'de ViewBinding aktif olmalı
android {
    buildFeatures {
        viewBinding = true
    }
}

// Sonra Gradle Sync
// File > Sync Project with Gradle Files
```

### Problem: Annotation Processor Not Working

**Hata Mesajı:**
```
Cannot find implementation for database
```

**Çözüm:**
```kotlin
// build.gradle.kts
plugins {
    id("com.google.devtools.ksp") version "1.9.0-1.0.13"
}

dependencies {
    // kapt yerine ksp kullan
    ksp("androidx.room:room-compiler:2.6.1")
}
```

---

## Çalışma Zamanı Sorunları

### Problem: App Crashes on Launch

**Hata Mesajı (Logcat):**
```
FATAL EXCEPTION: main
Process: com.mete.egitici, PID: 12345
java.lang.RuntimeException: Unable to start activity
```

**Debug Adımları:**
```bash
# Logcat'i filtrele
adb logcat | grep "com.mete.egitici"

# Crash raporunu görüntüle
adb logcat -d > crash_log.txt

# Backtrace'i incele
adb logcat *:E
```

**Yaygın Nedenler ve Çözümler:**

**1. Missing Layout File**
```kotlin
// Hata: 
// android.view.InflateException: Binary XML file line #X: Error inflating class

// Çözüm: Layout dosyasının varlığını kontrol et
// res/layout/activity_main.xml
```

**2. Null Pointer Exception**
```kotlin
// Hata:
// java.lang.NullPointerException

// Çözüm: Null kontrolü ekle
val userName = binding.etName.text?.toString() ?: ""
```

**3. Permission Denied**
```kotlin
// Hata:
// SecurityException: Permission denied

// Çözüm: İzin kontrolü ekle
if (ContextCompat.checkSelfPermission(this, permission) 
    == PackageManager.PERMISSION_GRANTED) {
    // İzin verilmiş
} else {
    // İzin iste
    ActivityCompat.requestPermissions(this, arrayOf(permission), REQUEST_CODE)
}
```

### Problem: Activity Not Found

**Hata Mesajı:**
```
android.content.ActivityNotFoundException: Unable to find explicit activity class
```

**Çözüm:**
```xml
<!-- AndroidManifest.xml'de activity tanımlı olmalı -->
<activity
    android:name=".activities.GameActivity"
    android:exported="false" />
```

### Problem: ClassNotFoundException

**Hata Mesajı:**
```
java.lang.ClassNotFoundException: Didn't find class on path
```

**Çözüm 1: ProGuard Rules**
```
# proguard-rules.pro
-keep class com.mete.egitici.models.** { *; }
-keep class com.mete.egitici.database.** { *; }
```

**Çözüm 2: Multidex Enable**
```kotlin
// build.gradle.kts
android {
    defaultConfig {
        multiDexEnabled = true
    }
}

dependencies {
    implementation("androidx.multidex:multidex:2.0.1")
}
```

---

## Performans Sorunları

### Problem: Slow App Performance

**Semptomlar:**
- UI donmaları
- Yavaş ekran geçişleri
- Gecikmeli animasyonlar

**Çözüm 1: Memory Profiler Kullan**
```
Android Studio > View > Tool Windows > Profiler
Memory kullanımını izle
```

**Çözüm 2: Background İşlemleri Coroutine'e Taşı**
```kotlin
// ❌ BAD - UI thread'de ağır işlem
fun loadGames() {
    val games = database.getGames() // UI thread bloke
    updateUI(games)
}

// ✅ GOOD - Coroutine kullan
fun loadGames() {
    lifecycleScope.launch {
        val games = withContext(Dispatchers.IO) {
            database.getGames() // Background thread
        }
        updateUI(games) // UI thread
    }
}
```

**Çözüm 3: RecyclerView Optimizasyonu**
```kotlin
// ViewHolder pattern kullan
class GameAdapter : RecyclerView.Adapter<GameViewHolder>() {
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameViewHolder {
        val binding = ItemGameBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return GameViewHolder(binding)
    }
    
    override fun onBindViewHolder(holder: GameViewHolder, position: Int) {
        holder.bind(games[position])
    }
    
    // DiffUtil kullan
    private val diffCallback = object : DiffUtil.ItemCallback<Game>() {
        override fun areItemsTheSame(oldItem: Game, newItem: Game) =
            oldItem.id == newItem.id
            
        override fun areContentsTheSame(oldItem: Game, newItem: Game) =
            oldItem == newItem
    }
}
```

### Problem: Memory Leak

**Tanı:**
```
Android Studio > Profiler > Memory > Record
Heap dump al ve analiz et
```

**Yaygın Nedenler ve Çözümler:**

**1. Context Leak**
```kotlin
// ❌ BAD
class MyViewModel(private val context: Context) : ViewModel()

// ✅ GOOD
class MyViewModel(private val application: Application) : ViewModel()
```

**2. Listener Leak**
```kotlin
// ✅ GOOD - Lifecycle aware
class MyFragment : Fragment() {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewLifecycleOwner.lifecycleScope.launch {
            // Otomatik temizlenir
        }
    }
}
```

**3. Static Reference Leak**
```kotlin
// ❌ BAD
companion object {
    var activity: Activity? = null // Leak!
}

// ✅ GOOD - WeakReference kullan
companion object {
    var activityRef: WeakReference<Activity>? = null
}
```

---

## Ses ve Görüntü Sorunları

### Problem: Ses Çalmıyor

**Debug Checklist:**
```kotlin
// 1. Dosya mevcut mu?
val file = File(soundPath)
if (!file.exists()) {
    Log.e(TAG, "Sound file not found: $soundPath")
}

// 2. MediaPlayer durumu?
mediaPlayer?.let {
    Log.d(TAG, "MediaPlayer state: ${it.isPlaying}")
}

// 3. Ses seviyesi?
val audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
val currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
Log.d(TAG, "Current volume: $currentVolume")

// 4. Cihaz sesi kapalı mı?
val isMuted = audioManager.isStreamMute(AudioManager.STREAM_MUSIC)
Log.d(TAG, "Is muted: $isMuted")
```

**Çözüm:**
```kotlin
class SoundManager(private val context: Context) {
    private var mediaPlayer: MediaPlayer? = null
    
    fun playSound(@RawRes soundRes: Int) {
        try {
            mediaPlayer?.release()
            mediaPlayer = MediaPlayer.create(context, soundRes)
            
            mediaPlayer?.setOnCompletionListener {
                it.release()
                mediaPlayer = null
            }
            
            mediaPlayer?.start()
            
        } catch (e: Exception) {
            Log.e(TAG, "Error playing sound", e)
        }
    }
    
    fun stopSound() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
    }
}
```

### Problem: Lottie Animation Görünmüyor

**Çözüm:**
```xml
<!-- Layout'ta Lottie View -->
<com.airbnb.lottie.LottieAnimationView
    android:id="@+id/animationView"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    app:lottie_fileName="animation.json"
    app:lottie_autoPlay="true"
    app:lottie_loop="true" />
```

```kotlin
// Programmatically
binding.animationView.apply {
    setAnimation("animation.json")
    playAnimation()
}

// Debug
binding.animationView.addAnimatorListener(object : Animator.AnimatorListener {
    override fun onAnimationStart(animation: Animator) {
        Log.d(TAG, "Animation started")
    }
    override fun onAnimationEnd(animation: Animator) {
        Log.d(TAG, "Animation ended")
    }
    override fun onAnimationCancel(animation: Animator) {}
    override fun onAnimationRepeat(animation: Animator) {}
})
```

### Problem: Image Loading Issues

**Çözüm (Coil ile):**
```kotlin
// build.gradle.kts
implementation("io.coil-kt:coil:2.5.0")

// Kullanım
binding.imageView.load(imageUrl) {
    crossfade(true)
    placeholder(R.drawable.placeholder)
    error(R.drawable.error)
    transformations(RoundedCornersTransformation(8f))
    listener(
        onStart = { Log.d(TAG, "Loading started") },
        onSuccess = { _, _ -> Log.d(TAG, "Loading success") },
        onError = { _, result -> Log.e(TAG, "Loading error", result.throwable) }
    )
}
```

---

## Database Sorunları

### Problem: Database Migration Failed

**Hata Mesajı:**
```
java.lang.IllegalStateException: Room cannot verify the data integrity
```

**Çözüm 1: Destructive Migration (Development)**
```kotlin
Room.databaseBuilder(context, AppDatabase::class.java, "mete.db")
    .fallbackToDestructiveMigration() // Tüm veri silinir!
    .build()
```

**Çözüm 2: Migration Ekleme (Production)**
```kotlin
val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("ALTER TABLE games ADD COLUMN difficulty INTEGER NOT NULL DEFAULT 1")
    }
}

Room.databaseBuilder(context, AppDatabase::class.java, "mete.db")
    .addMigrations(MIGRATION_1_2)
    .build()
```

### Problem: Database Locked

**Hata Mesajı:**
```
android.database.sqlite.SQLiteDatabaseLockedException: database is locked
```

**Çözüm:**
```kotlin
// Tek Database instance kullan (Singleton)
@Database(entities = [Game::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    
    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null
        
        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "mete.db"
                ).build().also { INSTANCE = it }
            }
        }
    }
}
```

### Problem: Query Too Slow

**Optimizasyon:**
```kotlin
// 1. Index ekle
@Entity(
    tableName = "games",
    indices = [Index(value = ["category"])] // Index
)
data class Game(...)

// 2. Limit kullan
@Query("SELECT * FROM games WHERE category = :category LIMIT 10")
suspend fun getGamesByCategory(category: String): List<Game>

// 3. Paging kullan
@Query("SELECT * FROM games ORDER BY name ASC")
fun getAllGamesPaged(): PagingSource<Int, Game>
```

---

## Network Sorunları

### Problem: Connection Timeout

**Çözüm:**
```kotlin
val okHttpClient = OkHttpClient.Builder()
    .connectTimeout(30, TimeUnit.SECONDS)
    .readTimeout(30, TimeUnit.SECONDS)
    .writeTimeout(30, TimeUnit.SECONDS)
    .addInterceptor(HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    })
    .build()
```

### Problem: SSL Certificate Error

**Debug:**
```kotlin
// Test için SSL bypass (SADECE DEVELOPMENT)
val trustAllCerts = arrayOf<TrustManager>(
    object : X509TrustManager {
        override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) {}
        override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {}
        override fun getAcceptedIssuers() = arrayOf<X509Certificate>()
    }
)

// PRODUCTION'DA KULLANMA!
```

---

## Cihaz Özel Sorunlar

### Samsung Devices

**Problem: Crash on Samsung**
```kotlin
// Samsung Knox kontrol et
try {
    Class.forName("com.samsung.android.knox.SemPersonaManager")
    // Knox aktif
} catch (e: ClassNotFoundException) {
    // Knox yok
}
```

### Xiaomi/MIUI Devices

**Problem: Background Service Killed**
```kotlin
// Autostart izni iste
val intent = Intent()
intent.component = ComponentName(
    "com.miui.securitycenter",
    "com.miui.permcenter.autostart.AutoStartManagementActivity"
)
startActivity(intent)
```

### Huawei Devices

**Problem: HMS yerine GMS**
```kotlin
// Google Play Services kontrolü
val googleApiAvailability = GoogleApiAvailability.getInstance()
val resultCode = googleApiAvailability.isGooglePlayServicesAvailable(this)

if (resultCode != ConnectionResult.SUCCESS) {
    // HMS kullan veya fallback
}
```

---

## Genel Debug İpuçları

### Logcat Filtreleme

```bash
# Sadece uygulama logları
adb logcat | grep "com.mete.egitici"

# Sadece hatalar
adb logcat *:E

# Tag'e göre filtre
adb logcat -s GameViewModel

# Dosyaya kaydet
adb logcat > logcat.txt
```

### ADB Komutları

```bash
# Cihaz bilgisi
adb shell getprop ro.build.version.release  # Android version
adb shell getprop ro.product.model          # Device model

# App bilgisi
adb shell dumpsys package com.mete.egitici

# Database çek
adb exec-out run-as com.mete.egitici cat \
  databases/mete.db > mete.db

# SharedPreferences görüntüle
adb shell run-as com.mete.egitici cat \
  shared_prefs/user_prefs.xml
```

---

## Yardım Al

**Sorun çözülmediyse:**

1. **GitHub Issue Aç**
   - https://github.com/mehmet0116/mehmet/issues
   - Tüm hata mesajlarını ekle
   - Logcat çıktısını paylaş
   - Cihaz/Android versiyonunu belirt

2. **Email Gönder**
   - support@meteegitici.com
   - Detaylı açıklama yap
   - Ekran görüntüsü ekle

3. **Stack Overflow**
   - `[android]` ve `[kotlin]` tag'leriyle soru sor

---

**Son Güncelleme:** 15 Aralık 2024
**Versiyon:** 1.0
