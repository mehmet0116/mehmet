# Mete Eğitici - Kullanım Kılavuzu

## Uygulama İçeriği

Bu Android uygulaması 3-8 yaş arası çocuklar için tasarlanmış kapsamlı bir eğitim platformudur.

### Ana Özellikler

#### 🏠 Ana Sayfa (Home)
- 8 farklı eğitim modülüne erişim
- Renkli ve çocuk dostu tasarım
- Her modül için kart tabanlı navigasyon

#### 📚 Eğitim Modülleri

1. **🗣️ Dil Gelişimi**
   - Türkçe ve İngilizce alfabe
   - Harf tanıma ve yazma
   - Kelime öğrenme
   
2. **🔢 Matematik**
   - Sayı öğrenme (1-100)
   - Toplama ve çıkarma
   - Geometrik şekiller
   - Saat okuma
   
3. **🧠 Bilişsel Gelişim**
   - Hafıza oyunları
   - Mantık bulmacaları
   - Dikkat egzersizleri
   - Örüntü tamamlama
   
4. **🎨 Yaratıcılık**
   - Boyama kitabı
   - Müzik aletleri
   - Serbest çizim
   - Sticker albümü
   
5. **🔬 Fen Bilgisi**
   - Hayvanlar ve sesleri
   - Mevsimler
   - Gezegenler
   - Basit deneyler
   
6. **👥 Sosyal Gelişim**
   - Duygu tanıma
   - Empati senaryoları
   - Sosyal beceriler
   - Trafik kuralları
   
7. **🎮 Oyunlar**
   - Quiz yarışması (Aktif!)
   - Hafıza eşleştirme
   - Puzzle yapboz
   - Boyama oyunları
   
8. **👨‍👩‍👧 Ebeveyn Paneli**
   - Kullanım istatistikleri
   - İlerleme raporları
   - Ayarlar ve kontroller

#### 👤 Profil Sayfası
- Kullanıcı bilgileri
- Toplam puan ve rozetler
- Tamamlanan dersler
- İlerleme yüzdeleri

#### ⚙️ Ayarlar
- Ses efektleri kontrolü
- Arka plan müziği
- Karanlık mod
- Erişilebilirlik seçenekleri
- Zorluk seviyesi
- Ebeveyn kontrolü ayarları

### Kullanım Adımları

1. **Uygulama Açılışı**
   - Uygulama splash ekranı ile açılır
   - Hoş geldin ekranında "Başla" butonuna tıklayın

2. **Ana Sayfa**
   - Alt navigasyondan "Ana Sayfa" sekmesi açıktır
   - 8 modül kartından birine tıklayarak o bölüme gidin

3. **Modül İçeriği**
   - Her modülde ilgili dersler ve aktiviteler listelenir
   - Kart üzerine tıklayarak ders/aktivite başlatabilirsiniz
   - Geri ok ile ana sayfaya dönebilirsiniz

4. **Quiz Oyunu** (🎯 Özellikle Önerilir)
   - Oyunlar modülüne gidin
   - "Bilgi Yarışması Oyna" kartına tıklayın
   - Sorulara cevap vererek puan kazanın
   - Quiz sonunda sonuçlarınızı görün
   - "Tekrar Oyna" ile yeniden başlayın

5. **Navigasyon**
   - Alt menüden Ana Sayfa, Profil, Ayarlar arasında geçiş yapın
   - Her ekranın üst kısmında başlık ve geri tuşu vardır

### Veri Dosyaları

Uygulama aşağıdaki JSON veri dosyalarını kullanır:

- `assets/data/lessons.json` - 10 farklı ders içeriği
- `assets/data/games.json` - 10 farklı oyun tanımı
- `assets/data/questions.json` - 10 quiz sorusu

### Teknik Bilgiler

- **Platform**: Android 5.0+ (API 21+)
- **Dil**: Kotlin
- **Mimari**: MVVM
- **Minimum SDK**: 21
- **Target SDK**: 34

### Not

Bu uygulama iskelet halindedir ve gelecekte:
- Gerçek oyun implementasyonları
- Ses efektleri
- Animasyonlar
- Veritabanı entegrasyonu
- Daha fazla içerik

eklenecektir.

## Kullanılabilir Özellikler

✅ **Şu Anda Çalışan:**
- Ana sayfa navigasyonu
- Tüm modüllere erişim
- Ders ve oyun listelerinin görüntülenmesi
- Tam fonksiyonel Quiz oyunu
- Profil sayfası
- Ayarlar sayfası
- Ebeveyn paneli görüntüleme

📝 **Geliştirme Aşamasında:**
- Gerçek oyun mekanikleri
- Ses efektleri ve müzik
- İlerleme takibi
- Rozet sistemi
- Veritabanı kayıtları
