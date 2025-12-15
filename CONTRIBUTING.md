# 🤝 Katkıda Bulunma Rehberi (Contributing Guide)

Mete Eğitici Çocuk Uygulaması'na katkıda bulunmak istediğiniz için teşekkürler! Bu belge, projeye nasıl katkıda bulunabileceğinizi açıklar.

## İçindekiler

1. [Başlamadan Önce](#başlamadan-önce)
2. [Geliştirme Ortamı Kurulumu](#geliştirme-ortamı-kurulumu)
3. [Katkı Türleri](#katkı-türleri)
4. [Kod Standartları](#kod-standartları)
5. [Pull Request Süreci](#pull-request-süreci)
6. [Issue Yönetimi](#issue-yönetimi)
7. [Commit Mesajları](#commit-mesajları)
8. [Test Yazma](#test-yazma)

---

## Başlamadan Önce

### Davranış Kuralları

- ✅ Saygılı ve profesyonel olun
- ✅ Yapıcı eleştiri yapın
- ✅ Başkalarının fikirlerine açık olun
- ✅ İşbirliğine ve yardımlaşmaya önem verin
- ❌ Hakaret, ayrımcılık veya taciz yasaktır

### Gereksinimler

- Android Studio Giraffe veya üzeri
- JDK Temurin 17.0.11
- Git bilgisi
- Kotlin deneyimi
- Android geliştirme bilgisi

---

## Geliştirme Ortamı Kurulumu

### 1. Repository'yi Fork Edin

```bash
# GitHub'da "Fork" butonuna tıklayın
# Kendi hesabınızda bir kopya oluşturun
```

### 2. Klonlayın

```bash
git clone https://github.com/<kullanıcı-adınız>/mehmet.git
cd mehmet
```

### 3. Upstream Ekleyin

```bash
git remote add upstream https://github.com/mehmet0116/mehmet.git
git fetch upstream
```

### 4. Branch Oluşturun

```bash
git checkout -b feature/yeni-ozellik
# veya
git checkout -b bugfix/hata-duzeltmesi
```

### 5. Android Studio'da Açın

```
File > Open > mehmet klasörü
Gradle sync bekleyin
```

---

## Katkı Türleri

### 1. Bug Raporları

**Bug bulduğunuzda:**
- Issue açın
- Hata açıklaması yazın
- Adım adım tekrar etme yöntemi paylaşın
- Beklenen ve gerçek davranışı belirtin
- Ekran görüntüsü ekleyin

**Şablon:**
```markdown
**Hata Açıklaması**
Kısa ve açık hata tanımı

**Tekrar Etme Adımları**
1. '...' sayfasına git
2. '....' butonuna tıkla
3. Scroll down to '....'
4. Hatayı gör

**Beklenen Davranış**
Ne olması gerektiğini açıklayın

**Ekran Görüntüleri**
Varsa ekran görüntüleri ekleyin

**Ortam:**
 - Cihaz: [örn. Pixel 6]
 - Android Versiyonu: [örn. 13]
 - Uygulama Versiyonu: [örn. 1.0.0]
```

### 2. Özellik Önerileri

**Yeni özellik önermek için:**
- Issue açın
- Özelliğin amacını açıklayın
- Kullanım senaryoları verin
- Mockup veya wireframe ekleyin (opsiyonel)

### 3. Kod Katkıları

**Kod katkısı yapmak için:**
- Issue kontrol edin veya yeni oluşturun
- Branch oluşturun
- Kod yazın
- Test ekleyin
- Pull Request açın

### 4. Dokümantasyon

**Dokümantasyon geliştirme:**
- Typo düzeltmeleri
- Eksik döküman ekleme
- Örnek kod iyileştirmeleri
- Çeviri katkıları

---

## Kod Standartları

### Kotlin Style Guide

**Genel Kurallar:**
```kotlin
// Class isimlendirme: PascalCase
class GameViewModel { }

// Function isimlendirme: camelCase
fun calculateScore() { }

// Constant: UPPER_SNAKE_CASE
const val MAX_SCORE = 100

// Variable: camelCase
val userName = "Mete"

// Private property: camelCase with underscore
private val _gameState = MutableLiveData<GameState>()
val gameState: LiveData<GameState> = _gameState
```

**Formatting:**
```kotlin
// Indent: 4 spaces
class Example {
    fun method() {
        if (condition) {
            doSomething()
        }
    }
}

// Line length: 120 characters max

// Import organization:
import android.widget.* // Android
import androidx.* // AndroidX
import com.mete.egitici.* // Project
import java.* // Java
import kotlin.* // Kotlin
```

### XML Style Guide

**Layout Dosyaları:**
```xml
<!-- Naming: snake_case -->
<!-- activity_main.xml, fragment_home.xml, item_game.xml -->

<!-- Attribute order: -->
<TextView
    android:id="@+id/tvTitle"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:layout_margin="16dp"
    android:padding="8dp"
    android:text="@string/title"
    android:textColor="@color/primary"
    android:textSize="18sp"
    android:textStyle="bold" />
```

**Resource Isimlendirme:**
```xml
<!-- IDs: type_name -->
android:id="@+id/btnStart"        <!-- Button -->
android:id="@+id/tvTitle"         <!-- TextView -->
android:id="@+id/etName"          <!-- EditText -->
android:id="@+id/ivLogo"          <!-- ImageView -->

<!-- Colors: description -->
<color name="primary">#6200EE</color>
<color name="text_dark">#000000</color>

<!-- Strings: module_description -->
<string name="welcome_title">Hoş Geldiniz</string>
<string name="game_start">Oyunu Başlat</string>

<!-- Dimensions: size_description -->
<dimen name="text_large">24sp</dimen>
<dimen name="margin_standard">16dp</dimen>
```

### Mimari Kurallar

**MVVM Pattern:**
```kotlin
// View Layer: Activity/Fragment
class GameActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGameBinding
    private val viewModel: GameViewModel by viewModels()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        observeViewModel()
        setupUI()
    }
    
    private fun observeViewModel() {
        viewModel.gameState.observe(this) { state ->
            updateUI(state)
        }
    }
}

// ViewModel Layer
class GameViewModel(
    private val repository: GameRepository
) : ViewModel() {
    
    private val _gameState = MutableLiveData<GameState>()
    val gameState: LiveData<GameState> = _gameState
    
    fun loadGames() {
        viewModelScope.launch {
            try {
                val games = repository.getGames()
                _gameState.value = GameState.Success(games)
            } catch (e: Exception) {
                _gameState.value = GameState.Error(e.message)
            }
        }
    }
}

// Repository Layer
class GameRepository(
    private val dao: GameDao,
    private val assetManager: AssetManager
) {
    suspend fun getGames(): List<Game> {
        return withContext(Dispatchers.IO) {
            dao.getAllGames()
        }
    }
}
```

### Yorum Yazma

**Kodun ne yaptığını değil, neden yaptığını açıklayın:**

```kotlin
// ❌ Kötü yorum
// User name'i al
val userName = getUserName()

// ✅ İyi yorum
// Cache invalidation için user name gerekiyor
val userName = getUserName()

// ✅ Function documentation
/**
 * Calculates the final score based on difficulty and time.
 *
 * @param difficulty The difficulty level (1-3)
 * @param timeRemaining Time remaining in milliseconds
 * @return Calculated score (base score + time bonus)
 */
fun calculateScore(difficulty: Int, timeRemaining: Long): Int {
    val baseScore = when (difficulty) {
        1 -> 10
        2 -> 20
        3 -> 30
        else -> 5
    }
    val timeBonus = (timeRemaining / 1000).toInt() / 2
    return baseScore + timeBonus
}
```

---

## Pull Request Süreci

### 1. Kodu Güncel Tutun

```bash
git fetch upstream
git rebase upstream/main
```

### 2. Testleri Çalıştırın

```bash
./gradlew test
./gradlew connectedAndroidTest
```

### 3. Lint Kontrol

```bash
./gradlew lint
```

### 4. Commit ve Push

```bash
git add .
git commit -m "feat: yeni özellik eklendi"
git push origin feature/yeni-ozellik
```

### 5. Pull Request Açın

**PR Başlığı:**
```
[Type] Short description

Örnekler:
[Feature] Add voice recognition for language module
[Bugfix] Fix memory leak in game activity
[Docs] Update architecture documentation
```

**PR Açıklaması Şablonu:**
```markdown
## Değişiklik Açıklaması
Yapılan değişikliklerin kısa açıklaması

## İlgili Issue
Closes #123

## Değişiklik Türü
- [ ] Bug fix
- [ ] Yeni özellik
- [ ] Breaking change
- [ ] Dokümantasyon

## Test Edildi mi?
- [ ] Unit testler yazıldı
- [ ] UI testler eklendi
- [ ] Manuel test yapıldı

## Ekran Görüntüleri
Varsa ekran görüntüleri ekleyin

## Checklist
- [ ] Kod style guide'a uygun
- [ ] Tüm testler geçiyor
- [ ] Lint hataları yok
- [ ] Dokümantasyon güncellendi
```

### 6. Code Review

**Review süreci:**
1. Maintainer'lar kodu inceler
2. Geri bildirim ve değişiklik istekleri
3. Değişiklikler yapılır
4. Onaylanır
5. Merge edilir

**Review beklenirken:**
- Sabırlı olun
- Geri bildirimlere açık olun
- Sorularınız varsa sorun

---

## Issue Yönetimi

### Issue Etiketleri

```
bug - Hata raporları
enhancement - Yeni özellik önerileri
documentation - Dokümantasyon iyileştirmeleri
good first issue - Yeni katkıcılar için uygun
help wanted - Yardım aranan konular
question - Soru ve tartışma
wontfix - Yapılmayacak değişiklikler
duplicate - Duplicate issue
invalid - Geçersiz issue
```

### Issue Template

**Bug Report:**
```markdown
**Describe the bug**
A clear and concise description of what the bug is.

**To Reproduce**
Steps to reproduce the behavior

**Expected behavior**
What you expected to happen

**Screenshots**
If applicable, add screenshots

**Environment:**
 - Device: [e.g. Pixel 6]
 - OS: [e.g. Android 13]
 - App Version: [e.g. 1.0.0]
```

---

## Commit Mesajları

### Conventional Commits

**Format:**
```
<type>(<scope>): <subject>

<body>

<footer>
```

**Types:**
```
feat: Yeni özellik
fix: Bug düzeltmesi
docs: Dokümantasyon değişikliği
style: Kod formatı (whitespace, formatting)
refactor: Kod değişikliği (ne bug fix ne feature)
perf: Performance iyileştirmesi
test: Test ekleme veya düzeltme
chore: Build, CI, dependencies güncellemesi
```

**Örnekler:**
```bash
# Yeni özellik
git commit -m "feat(language): add voice recognition support"

# Bug düzeltmesi
git commit -m "fix(math): correct score calculation logic"

# Dokümantasyon
git commit -m "docs(readme): update installation instructions"

# Refactoring
git commit -m "refactor(viewmodel): simplify state management"

# Test
git commit -m "test(game): add unit tests for GameViewModel"
```

### Detaylı Commit Mesajı

```bash
git commit -m "feat(language): add voice recognition support

- Integrate Android Speech Recognition API
- Add microphone permission handling
- Implement pronunciation accuracy checking
- Add visual feedback for correct/incorrect pronunciation

Closes #42"
```

---

## Test Yazma

### Unit Test Örneği

```kotlin
class GameViewModelTest {
    
    private lateinit var viewModel: GameViewModel
    private lateinit var mockRepository: GameRepository
    
    @Before
    fun setup() {
        mockRepository = mock()
        viewModel = GameViewModel(mockRepository)
    }
    
    @Test
    fun `loadGames updates state to Success when repository returns games`() = runTest {
        // Given
        val games = listOf(
            Game(id = "1", name = "Test Game 1"),
            Game(id = "2", name = "Test Game 2")
        )
        whenever(mockRepository.getGames()).thenReturn(games)
        
        // When
        viewModel.loadGames()
        
        // Then
        val state = viewModel.gameState.value
        assertTrue(state is GameState.Success)
        assertEquals(games, (state as GameState.Success).games)
    }
    
    @Test
    fun `calculateScore returns correct score for difficulty 1`() {
        // Given
        val difficulty = 1
        val timeRemaining = 10000L
        
        // When
        val score = viewModel.calculateScore(difficulty, timeRemaining)
        
        // Then
        assertEquals(15, score) // 10 base + 5 time bonus
    }
}
```

### UI Test Örneği

```kotlin
@RunWith(AndroidJUnit4::class)
class MainActivityTest {
    
    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)
    
    @Test
    fun clickLanguageModule_opensLanguageActivity() {
        // Click on language module
        onView(withId(R.id.cardLanguage))
            .perform(click())
        
        // Verify language activity is displayed
        onView(withText("Dil Gelişimi"))
            .check(matches(isDisplayed()))
    }
    
    @Test
    fun bottomNavigation_switchesBetweenFragments() {
        // Click on profile tab
        onView(withId(R.id.nav_profile))
            .perform(click())
        
        // Verify profile fragment is displayed
        onView(withId(R.id.profileLayout))
            .check(matches(isDisplayed()))
        
        // Click on settings tab
        onView(withId(R.id.nav_settings))
            .perform(click())
        
        // Verify settings fragment is displayed
        onView(withId(R.id.settingsLayout))
            .check(matches(isDisplayed()))
    }
}
```

---

## Best Practices

### Do's ✅

- Kodunuzu test edin
- Dokümantasyonu güncelleyin
- Küçük, odaklı PR'lar açın
- Açıklayıcı commit mesajları yazın
- Code review'lara katılın
- Issue'ları tartışın
- Yardım isteyin

### Don'ts ❌

- Büyük, monolithic PR'lar
- Test olmadan kod
- Dokümantasyon güncellemeden
- Uygun olmayan commit mesajları
- Style guide'ı ihlal etmek
- Hard-coded değerler
- TODO yorumları bırakmak

---

## Yardım Alın

**Sorularınız varsa:**
- Issue açın
- Discussions kullanın
- E-posta: dev@meteegitici.com
- Discord: (yakında)

**Kaynaklar:**
- [Android Developer Guide](https://developer.android.com/guide)
- [Kotlin Documentation](https://kotlinlang.org/docs/home.html)
- [Material Design Guidelines](https://m3.material.io)

---

## Teşekkürler!

Mete Eğitici projesine katkıda bulunduğunuz için teşekkür ederiz! 🎉

Katkılarınız bu projeyi daha iyi hale getiriyor ve çocukların eğitimine katkı sağlıyor.

---

**Son Güncelleme:** Aralık 2024
**Versiyon:** 1.0
