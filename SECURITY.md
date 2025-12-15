# 🔒 Güvenlik Politikası (Security Policy)

## Güvenlik Taahhüdümüz

Mete Eğitici Çocuk Uygulaması, çocukların güvenliğini en üst öncelik olarak ele alır. Bu belge, uygulamamızın güvenlik politikalarını, raporlama prosedürlerini ve en iyi güvenlik uygulamalarını açıklar.

---

## İçindekiler

1. [Desteklenen Versiyonlar](#desteklenen-versiyonlar)
2. [Güvenlik Önlemleri](#güvenlik-önlemleri)
3. [Güvenlik Açığı Bildirimi](#güvenlik-açığı-bildirimi)
4. [Veri Gizliliği](#veri-gizliliği)
5. [Çocuk Güvenliği](#çocuk-güvenliği)
6. [Uyumluluk](#uyumluluk)
7. [En İyi Uygulamalar](#en-iyi-uygulamalar)

---

## Desteklenen Versiyonlar

Aşağıdaki tabloda hangi versiyonların aktif güvenlik desteği aldığı belirtilmiştir:

| Versiyon | Destekleniyor          |
| -------- | ---------------------- |
| 1.0.x    | :white_check_mark: Evet |
| < 1.0    | :x: Hayır              |

**Not:** Güvenlik güncellemelerini almak için her zaman en son sürümü kullanmanızı öneririz.

---

## Güvenlik Önlemleri

### 🔐 Veri Koruma

#### Yerel Veri Depolama
```kotlin
// Tüm hassas veriler şifrelenir
implementation("androidx.security:security-crypto:1.1.0-alpha06")

val masterKey = MasterKey.Builder(context)
    .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
    .build()

val sharedPreferences = EncryptedSharedPreferences.create(
    context,
    "user_prefs",
    masterKey,
    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
)
```

#### Database Şifreleme
```kotlin
// Room Database SQLCipher ile şifrelenir
val passphrase = SQLiteDatabase.getBytes(BuildConfig.DB_KEY.toCharArray())
val factory = SupportFactory(passphrase)

Room.databaseBuilder(context, AppDatabase::class.java, "mete.db")
    .openHelperFactory(factory)
    .build()
```

### 🚫 Veri Toplama POLİTİKASI

**Toplanan Veriler:**
- ✅ Kullanıcı profili (sadece yerel)
  - İsim (yerel cihazda)
  - Yaş (yerel cihazda)
  - Avatar tercihi (yerel cihazda)
  
- ✅ İlerleme verileri (sadece yerel)
  - Oyun skorları
  - Tamamlanan aktiviteler
  - Kazanılan rozetler

**ASLA Toplanmayan Veriler:**
- ❌ Kişisel kimlik bilgileri
- ❌ Konum verileri
- ❌ Kamera/Mikrofon kayıtları (cihaz dışına gönderilmez)
- ❌ Kişisel fotoğraflar
- ❌ İletişim bilgileri
- ❌ Cihaz tanımlayıcıları
- ❌ Kullanım alışkanlıkları (analytics)

### 🔒 İzin Yönetimi

#### Gerekli İzinler
```xml
<!-- AndroidManifest.xml -->

<!-- Offline çalışma için gerekli DEĞİL -->
<!-- <uses-permission android:name="android.permission.INTERNET" /> -->

<!-- Sadece yerel ses oynatma için -->
<uses-permission android:name="android.permission.RECORD_AUDIO" />

<!-- Titreşim geri bildirimi için -->
<uses-permission android:name="android.permission.VIBRATE" />

<!-- AR özellikler için (opsiyonel) -->
<uses-permission android:name="android.permission.CAMERA" />
<uses-feature android:name="android.hardware.camera" android:required="false" />
```

#### İzin İsteği Best Practices
```kotlin
// İzin istemeden önce açıklama göster
private fun requestMicrophonePermission() {
    when {
        ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.RECORD_AUDIO
        ) == PackageManager.PERMISSION_GRANTED -> {
            // İzin zaten verilmiş
            startVoiceRecognition()
        }
        shouldShowRequestPermissionRationale(Manifest.permission.RECORD_AUDIO) -> {
            // Kullanıcıya neden gerekli olduğunu açıkla
            showPermissionRationaleDialog()
        }
        else -> {
            // İzin iste
            requestPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
        }
    }
}
```

### 🛡️ Network Güvenliği

#### SSL Pinning (Gelecek Özellik)
```xml
<!-- res/xml/network_security_config.xml -->
<network-security-config>
    <domain-config cleartextTrafficPermitted="false">
        <domain includeSubdomains="true">meteegitici.com</domain>
        <pin-set expiration="2025-12-31">
            <pin digest="SHA-256"><!-- Certificate Hash --></pin>
        </pin-set>
    </domain-config>
</network-security-config>
```

#### HTTPS Only
```kotlin
// Tüm network istekleri HTTPS üzerinden
val client = OkHttpClient.Builder()
    .addInterceptor { chain ->
        val request = chain.request()
        if (request.url.scheme != "https") {
            throw SecurityException("Only HTTPS connections allowed")
        }
        chain.proceed(request)
    }
    .build()
```

### 🔐 Ebeveyn Kontrolü Güvenliği

#### PIN Koruma
```kotlin
class ParentalControlManager {
    private val PIN_KEY = "parental_pin"
    
    fun setPIN(pin: String) {
        require(pin.length in 4..6) { "PIN must be 4-6 digits" }
        require(pin.all { it.isDigit() }) { "PIN must contain only digits" }
        
        val hashedPIN = hashPIN(pin)
        securePreferences.edit {
            putString(PIN_KEY, hashedPIN)
        }
    }
    
    private fun hashPIN(pin: String): String {
        val digest = MessageDigest.getInstance("SHA-256")
        val hash = digest.digest(pin.toByteArray())
        return Base64.encodeToString(hash, Base64.NO_WRAP)
    }
    
    fun verifyPIN(inputPIN: String): Boolean {
        val storedHash = securePreferences.getString(PIN_KEY, null) ?: return false
        val inputHash = hashPIN(inputPIN)
        return storedHash == inputHash
    }
}
```

#### Brute Force Protection
```kotlin
class BruteForceProtection {
    private var failedAttempts = 0
    private var lockoutTime: Long? = null
    
    fun checkAttempt(pin: String): Boolean {
        if (isLockedOut()) {
            showLockoutMessage()
            return false
        }
        
        val isCorrect = parentalControl.verifyPIN(pin)
        
        if (isCorrect) {
            resetAttempts()
        } else {
            failedAttempts++
            
            if (failedAttempts >= MAX_ATTEMPTS) {
                lockoutTime = System.currentTimeMillis() + LOCKOUT_DURATION
                showLockoutMessage()
            }
        }
        
        return isCorrect
    }
    
    private fun isLockedOut(): Boolean {
        val lockout = lockoutTime ?: return false
        if (System.currentTimeMillis() < lockout) {
            return true
        }
        resetAttempts()
        return false
    }
    
    companion object {
        private const val MAX_ATTEMPTS = 5
        private const val LOCKOUT_DURATION = 30 * 60 * 1000L // 30 dakika
    }
}
```

### 🔒 Input Validation

#### SQL Injection Prevention
```kotlin
// Room kullanımı otomatik olarak SQL injection'ı önler
@Query("SELECT * FROM games WHERE category = :category")
suspend fun getGamesByCategory(category: String): List<Game>

// Asla string concatenation kullanma
// BAD: @Query("SELECT * FROM games WHERE category = '$category'")
```

#### XSS Prevention
```kotlin
// User input'ları her zaman escape et
fun sanitizeInput(input: String): String {
    return input
        .replace("<", "&lt;")
        .replace(">", "&gt;")
        .replace("\"", "&quot;")
        .replace("'", "&#39;")
        .trim()
}
```

---

## Güvenlik Açığı Bildirimi

### 🚨 Güvenlik Açığı Bulursanız

**Lütfen HEMEN BİLDİRİN!**

#### Bildirme Yöntemi

**Email:** security@meteegitici.com

**Şablon:**
```
Konu: [SECURITY] Güvenlik Açığı Raporu

1. Açıklama:
   [Güvenlik açığının detaylı açıklaması]

2. Etki:
   [Hangi kullanıcılar etkilenir, ne kadar ciddi]

3. Tekrar Etme Adımları:
   [Adım adım nasıl tekrar edilir]

4. Önerilen Çözüm:
   [Varsa çözüm öneriniz]

5. Keşif Tarihi:
   [Ne zaman keşfettiniz]

6. İletişim:
   [İsim ve iletişim bilgileriniz - opsiyonel]
```

### ⏱️ Yanıt Süresi

| Aşama | Süre |
|-------|------|
| İlk yanıt | 24 saat içinde |
| Doğrulama | 48 saat içinde |
| Düzeltme planı | 7 gün içinde |
| Patch yayını | Kritiklik seviyesine göre |

### 🏆 Ödül Programı

**Responsible Disclosure Ödülleri:**

| Kritiklik | Ödül |
|-----------|------|
| Kritik | Teşekkür + Special Badge |
| Yüksek | Teşekkür + Contributors Listesi |
| Orta | Teşekkür |
| Düşük | Teşekkür |

---

## Veri Gizliliği

### 📋 Gizlilik İlkeleri

#### 1. Veri Minimizasyonu
- Sadece gerekli verileri toplarız
- Tüm veriler yerel cihazda kalır
- Üçüncü taraflarla veri paylaşımı YOK

#### 2. Şeffaflık
- Hangi verilerin toplandığı açıkça belirtilir
- Verilerin nasıl kullanıldığı açıklanır
- Ebeveynler veri erişimi talep edebilir

#### 3. Kullanıcı Kontrolü
- Ebeveynler çocuk verilerini silebilir
- Profil ve ilerleme verisi dışa aktarılabilir
- İstediği zaman veri silinebilir

### 🗑️ Veri Silme

```kotlin
class DataManager {
    /**
     * Tüm kullanıcı verilerini siler
     * COPPA uyumluluğu için zorunlu
     */
    suspend fun deleteAllUserData(userId: String) {
        withContext(Dispatchers.IO) {
            // Database'den sil
            database.userProgressDao().deleteAllForUser(userId)
            database.achievementDao().deleteAllForUser(userId)
            
            // SharedPreferences'dan sil
            securePreferences.edit {
                remove("user_profile_$userId")
                remove("user_settings_$userId")
            }
            
            // Cache dosyalarını sil
            clearUserCache(userId)
            
            // Log
            Log.i("DataManager", "All data deleted for user: $userId")
        }
    }
}
```

---

## Çocuk Güvenliği

### 👶 COPPA Uyumluluğu

**Children's Online Privacy Protection Act** gereksinimlerine uyumlu:

#### ✅ Uyumluluk Kontrol Listesi

- [x] 13 yaş altı çocuklardan kişisel bilgi toplanmaz
- [x] Ebeveyn izni sistemi
- [x] Veri toplama şeffaflığı
- [x] Üçüncü taraf paylaşımı YOK
- [x] Veri silme hakkı
- [x] Güvenli içerik politikası

### 🌍 GDPR Uyumluluğu

**General Data Protection Regulation** gereksinimlerine uyumlu:

#### ✅ GDPR Hakları

- [x] **Right to Access**: Ebeveynler veri erişimi talep edebilir
- [x] **Right to Rectification**: Veriler düzeltilebilir
- [x] **Right to Erasure**: Veriler silinebilir
- [x] **Right to Data Portability**: Veriler dışa aktarılabilir
- [x] **Right to Object**: Veri işlemeye itiraz edilebilir
- [x] **Right to Restriction**: Veri işleme sınırlanabilir

### 🔞 İçerik Güvenliği

#### İçerik Filtreleme
```kotlin
class ContentFilter {
    private val inappropriateWords = loadBlacklist()
    
    fun validateContent(content: String): Boolean {
        return !inappropriateWords.any { word ->
            content.contains(word, ignoreCase = true)
        }
    }
    
    fun filterContent(content: String): String {
        var filtered = content
        inappropriateWords.forEach { word ->
            filtered = filtered.replace(
                word,
                "*".repeat(word.length),
                ignoreCase = true
            )
        }
        return filtered
    }
}
```

#### Yaş Uygunluğu
- Tüm içerik 3+ yaş için uygun
- Şiddet içeriği YOK
- Korku unsurları YOK
- Uygunsuz dil YOK

---

## Uyumluluk

### 📱 Platform Güvenliği

#### Android Security Best Practices

**1. ProGuard/R8 Obfuscation**
```
# proguard-rules.pro
-keepattributes *Annotation*
-keep class com.mete.egitici.models.** { *; }
-dontwarn okhttp3.**
```

**2. Code Signing**
```bash
# Release build için zorunlu
jarsigner -verbose -sigalg SHA256withRSA \
  -digestalg SHA256 \
  -keystore mete-release.keystore \
  app-release-unsigned.apk alias_name
```

**3. SafetyNet Attestation** (Gelecek)
```kotlin
fun checkDeviceSecurity() {
    SafetyNet.getClient(this)
        .attest(nonce, API_KEY)
        .addOnSuccessListener { response ->
            val jwsResult = response.jwsResult
            // Verify device integrity
        }
}
```

### 🔐 Penetration Testing

**Düzenli Güvenlik Testleri:**
- [ ] Quarterly penetration testing
- [ ] Automated vulnerability scanning
- [ ] Code security review
- [ ] Dependency vulnerability check

**Test Alanları:**
- Authentication/Authorization
- Data encryption
- Input validation
- Session management
- API security

---

## En İyi Uygulamalar

### Geliştiriciler İçin

#### 1. Secure Coding
```kotlin
// ✅ GOOD
val password = securePreferences.getString("password", "")

// ❌ BAD
val password = "hardcoded_password"
```

#### 2. Dependency Updates
```kotlin
// Düzenli olarak güncelle
implementation("androidx.security:security-crypto:1.1.0-alpha06")
```

#### 3. Logging
```kotlin
// ✅ GOOD - No sensitive data
Log.d(TAG, "User logged in successfully")

// ❌ BAD - Exposes sensitive data
Log.d(TAG, "User password: $password")
```

### Kullanıcılar İçin

#### Ebeveyn Tavsiyeleri

**Güvenli Kullanım:**
1. ✅ Güçlü PIN kodu seçin (6 haneli)
2. ✅ PIN'i çocuklarla paylaşmayın
3. ✅ Düzenli ilerleme kontrolleri
4. ✅ Cihaz güvenliğini sağlayın
5. ✅ Güncellemeleri yükleyin

**Cihaz Güvenliği:**
1. ✅ Cihazın ekran kilidini aktif edin
2. ✅ Uygulamalardan bilinmeyen kaynaklara izin vermeyin
3. ✅ Antivirus yazılımı kullanın
4. ✅ İşletim sistemini güncel tutun

---

## Güvenlik İletişim

**Güvenlik Ekibi:**
- Email: security@meteegitici.com
- PGP Key: (yakında eklenecek)
- Response Time: 24 saat

**Genel Destek:**
- Email: support@meteegitici.com
- Website: https://meteegitici.com/security

---

## Güvenlik Güncellemeleri

**Güvenlik güncellemeleri için:**
- [ ] GitHub Security Advisories'i takip edin
- [ ] Email newsletter'a kayıt olun
- [ ] Uygulama içi bildirimleri aktif edin

---

## Sorumluluk Reddi

Bu güvenlik politikası, bilinen en iyi uygulamaları yansıtır ancak %100 güvenlik garantisi vermez. Kullanıcılar, uygulamayı kullanırken kendi sorumluluklarını da yerine getirmelidir.

---

**Son Güncelleme:** 15 Aralık 2024
**Versiyon:** 1.0
**Sonraki Gözden Geçirme:** 15 Mart 2025
