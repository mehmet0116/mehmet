# 🎨 Mete Eğitici Çocuk Uygulaması

> 3-8 yaş arası çocuklar için tasarlanmış kapsamlı Android eğitim uygulaması

[![Android](https://img.shields.io/badge/Platform-Android-green.svg)](https://developer.android.com)
[![Kotlin](https://img.shields.io/badge/Language-Kotlin-blue.svg)](https://kotlinlang.org)
[![API](https://img.shields.io/badge/API-21%2B-brightgreen.svg)](https://android-arsenal.com/api?level=21)
[![License](https://img.shields.io/badge/License-Proprietary-red.svg)](LICENSE)

## 📱 Genel Bakış

Mete Eğitici Çocuk Uygulaması, çocukların eğlenerek öğrenmesini sağlayan kapsamlı bir Android uygulamasıdır. Dil gelişiminden matematiğe, yaratıcılıktan fen bilgisine kadar geniş bir yelpazede eğitim içeriği sunar.

## ✨ Özellikler

### 📚 Eğitim Modülleri

#### 🗣️ Dil Gelişimi
- Türkçe ve İngilizce alfabe öğrenimi
- 50+ sesli hikaye kitabı
- Kelime dağarcığı geliştirme oyunları
- Telaffuz egzersizleri (mikrofon desteği)
- Cümle kurma alıştırmaları

#### 🔢 Matematik
- Sayı sayma (1-100)
- Basit toplama ve çıkarma
- Geometrik şekiller (2D & 3D)
- Saat okuma öğrenimi
- Problem çözme senaryoları

#### 🧠 Bilişsel Gelişim
- Hafıza geliştirme oyunları
- Mantık ve akıl yürütme
- Dikkat ve konsantrasyon egzersizleri
- Sınıflandırma ve sıralama oyunları
- Görsel-mekansal algı geliştirme

#### 🎨 Yaratıcılık
- 100+ boyama sayfası
- Müzik aletleri (piyano, davul, gitar, ksilofon)
- Çizim araçları
- 500+ sticker koleksiyonu
- Renk karışımı deneyleri

#### 🔬 Fen ve Doğa Bilgisi
- 200+ hayvan tanıtımı
- Bitkiler ve çiçekler
- Vücudumuz ve organlar
- Gezegenler ve uzay
- Basit bilim deneyleri

#### 👥 Sosyal ve Duygusal Gelişim
- Duygu ifadelerini tanıma
- Empati geliştirme senaryoları
- Sosyal beceriler (paylaşma, özür dileme)
- Günlük rutinler
- Arkadaşlık ve iletişim becerileri

### 🎮 Oyun Çeşitleri
- Eşleştirme oyunları
- Puzzle yapbozlar (4-100 parça)
- Sesli quizler
- Hafıza kart oyunları
- Kelime avı ve bulmacalar
- Labirent oyunları
- Farkı bulma oyunları

### 🏆 Ödül ve Motivasyon Sistemi
- Yıldız puanlama
- 100+ farklı başarı rozeti
- 500+ çıkartma koleksiyonu
- Seviye atlama sistemi
- Günlük ve haftalık ödüller
- Dijital sertifikalar

### 👨‍👩‍👧 Ebeveyn Kontrol Paneli
- Detaylı ilerleme raporları
- Kullanım istatistikleri
- Süre sınırlaması
- İçerik kısıtlama
- Çoklu çocuk profili yönetimi
- E-posta rapor gönderimi

### 🎨 Kişiselleştirme
- 5 farklı tema (Doğa, Uzay, Deniz, Orman, Şehir)
- Avatar seçimi
- Karanlık/Aydınlık mod
- Zorluk seviyesi ayarları
- Dil tercihi (Türkçe/İngilizce)
- Renk körlüğü modu

### ♿ Erişilebilirlik
- Görme engellilere uyumlu mod
- İşitme engelliler için görsel ipuçları
- Otizm dostu mod
- Dikkat eksikliği için özel oyunlar
- Motor beceri gelişimi oyunları

## 🛠️ Teknik Detaylar

### Gereksinimler
- **Minimum Android Versiyonu:** Android 5.0 (API 21)
- **Hedef Android Versiyonu:** Android 14 (API 34)
- **Derleme SDK:** API 34
- **JDK:** Temurin 17.0.11
- **Gradle:** 8.2
- **Kotlin:** 1.9.0

### Teknoloji Yığını
```
- Language: Kotlin
- Architecture: MVVM (Model-View-ViewModel)
- UI Framework: Material Design 3
- Database: Room
- Animations: Lottie
- Audio: AndroidX Media
- Camera: CameraX
- Navigation: Navigation Component
- Lifecycle: ViewModel & LiveData
```

### Bağımlılıklar
```kotlin
// Core
androidx.core:core-ktx:1.12.0
androidx.appcompat:appcompat:1.6.1
com.google.android.material:material:1.10.0

// Lifecycle
androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2
androidx.lifecycle:lifecycle-livedata-ktx:2.6.2

// Navigation
androidx.navigation:navigation-fragment-ktx:2.7.4
androidx.navigation:navigation-ui-ktx:2.7.4

// Room Database
androidx.room:room-runtime:2.6.0
androidx.room:room-ktx:2.6.0

// Lottie Animations
com.airbnb.android:lottie:6.1.0

// Media
androidx.media:media:1.6.0

// CameraX
androidx.camera:camera-*:1.3.0
```

## 📦 Kurulum

### Android Studio'da Açma
1. Android Studio'yu açın
2. "Open an Existing Project" seçin
3. Proje klasörünü seçin
4. Gradle senkronizasyonunu bekleyin

### Derleme
```bash
./gradlew build
```

### Çalıştırma
```bash
./gradlew installDebug
```

## 📁 Proje Yapısı

```
MeteEgiticiCocukUygulamasi/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/mete/egitici/
│   │   │   │   ├── activities/          # Tüm aktiviteler
│   │   │   │   ├── adapters/            # RecyclerView adaptörleri
│   │   │   │   ├── models/              # Veri modelleri
│   │   │   │   ├── viewmodels/          # ViewModeller
│   │   │   │   ├── database/            # Room database
│   │   │   │   ├── services/            # Arka plan servisleri
│   │   │   │   ├── utils/               # Yardımcı sınıflar
│   │   │   │   └── receivers/           # Broadcast receivers
│   │   │   ├── res/
│   │   │   │   ├── layout/              # XML layout dosyaları
│   │   │   │   ├── drawable/            # Çizim kaynakları
│   │   │   │   ├── values/              # Renkler, stringler, vb.
│   │   │   │   ├── anim/                # Animasyonlar
│   │   │   │   ├── menu/                # Menü dosyaları
│   │   │   │   └── xml/                 # XML konfigürasyonları
│   │   │   ├── assets/
│   │   │   │   ├── images/              # Resim dosyaları
│   │   │   │   ├── sounds/              # Ses dosyaları
│   │   │   │   ├── stories/             # Hikaye JSON dosyaları
│   │   │   │   └── data/                # Veri JSON dosyaları
│   │   │   └── AndroidManifest.xml
│   │   └── test/
│   ├── build.gradle.kts
│   └── proguard-rules.pro
├── gradle/
├── build.gradle.kts
├── settings.gradle.kts
├── gradle.properties
└── index.html
```

Detaylı proje yapısı için [PROJECT_STRUCTURE.md](PROJECT_STRUCTURE.md) dosyasına bakın.

## 🎯 Kullanım

### İlk Çalıştırma
1. Uygulamayı açın
2. Splash ekranı gösterilir
3. Hoş geldin ekranında "Başla" butonuna tıklayın
4. Çocuk profili oluşturun
5. Ana ekranda modüller arasında gezinin

### Modül Seçimi
Ana ekranda 8 ana modül bulunur:
- 🗣️ Dil Gelişimi
- 🔢 Matematik
- 🧠 Bilişsel Gelişim
- 🎨 Yaratıcılık
- 🔬 Fen Bilgisi
- 👥 Sosyal Gelişim
- 🎮 Oyunlar
- 👨‍👩‍👧 Ebeveyn Kontrol

### Ebeveyn Kontrolü
1. Ayarlar menüsünden "Ebeveyn Kontrol"e girin
2. Şifre oluşturun
3. İlerleme raporlarını görüntüleyin
4. Kullanım sınırlamaları ayarlayın

## 🔒 Güvenlik ve Gizlilik

- ✅ Reklamsız deneyim
- ✅ İnternet olmadan çalışma
- ✅ Kişisel veri toplanmaması
- ✅ COPPA ve GDPR uyumluluğu
- ✅ Ebeveyn onayı gerektiren işlemler
- ✅ Güvenli içerik filtresi
- ✅ Veri şifreleme

## 📝 Lisans

Bu proje özel lisans altındadır. Tüm hakları saklıdır.

## 👥 Katkıda Bulunanlar

- **Geliştirici:** Mete Egitici Ekibi
- **Tasarım:** UI/UX Tasarım Ekibi
- **Seslendirme:** Profesyonel Seslendirme Sanatçıları
- **İçerik:** Eğitim Uzmanları

## 📞 İletişim

Sorular, öneriler veya destek için:
- Email: info@meteegitici.com
- Website: https://meteegitici.com

## 🚀 Gelecek Özellikler

- [ ] Çevrimiçi çoklu oyuncu desteği
- [ ] Yapay zeka destekli öğrenme asistanı
- [ ] Arttırılmış gerçeklik (AR) modülleri
- [ ] Daha fazla dil desteği
- [ ] Tablet optimizasyonu
- [ ] Giyilebilir cihaz entegrasyonu

## 📊 Versiyon Geçmişi

### v1.0.0 (2024)
- ✨ İlk sürüm
- ✨ 8 ana eğitim modülü
- ✨ 100+ oyun ve aktivite
- ✨ Ebeveyn kontrol paneli
- ✨ Çoklu profil desteği

---

**Mete Eğitici Çocuk Uygulaması ile çocuklarınız eğlenerek öğrensin! 🎓**
