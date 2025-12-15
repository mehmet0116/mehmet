# 🏗️ Mete Eğitici - Teknik Mimari Dokümantasyonu

> Kapsamlı mimari açıklama ve tasarım kararları

## İçindekiler

1. [Mimari Genel Bakış](#mimari-genel-bakış)
2. [MVVM Mimarisi](#mvvm-mimarisi)
3. [Katmanlar](#katmanlar)
4. [Veri Akışı](#veri-akışı)
5. [Dependency Management](#dependency-management)
6. [Design Patterns](#design-patterns)
7. [Threading Model](#threading-model)
8. [State Management](#state-management)

---

## Mimari Genel Bakış

### Mimari Prensipleri

Mete Eğitici uygulaması, **SOLID** prensiplerine uygun, **MVVM (Model-View-ViewModel)** mimarisi kullanılarak geliştirilmiştir.

```
┌──────────────────────────────────────────────────────────┐
│                    PRESENTATION LAYER                     │
│  ┌────────────┐  ┌────────────┐  ┌────────────┐         │
│  │  Activity  │  │ Fragment   │  │   Adapter  │         │
│  └──────┬─────┘  └──────┬─────┘  └──────┬─────┘         │
│         │                │                │               │
│         └────────────────┴────────────────┘               │
│                          │                                │
├──────────────────────────┼────────────────────────────────┤
│                    VIEWMODEL LAYER                        │
│                    ┌─────┴──────┐                         │
│                    │ ViewModel  │                         │
│                    └─────┬──────┘                         │
│                          │                                │
├──────────────────────────┼────────────────────────────────┤
│                      DOMAIN LAYER                         │
│         ┌────────────────┼────────────────┐               │
│    ┌────┴───┐      ┌────┴────┐      ┌────┴────┐          │
│    │UseCase │      │UseCase  │      │UseCase  │          │
│    └────┬───┘      └────┬────┘      └────┬────┘          │
│         └───────────────┴─────────────────┘               │
│                          │                                │
├──────────────────────────┼────────────────────────────────┤
│                       DATA LAYER                          │
│         ┌────────────────┼────────────────┐               │
│    ┌────┴──────┐    ┌────┴────┐    ┌─────┴─────┐         │
│    │Repository │    │Database │    │   API     │         │
│    └───────────┘    └─────────┘    └───────────┘         │
└──────────────────────────────────────────────────────────┘
```

### Temel Katmanlar

#### 1. **Presentation Layer (UI)**
- **Sorumluluk:** Kullanıcı arayüzü ve kullanıcı etkileşimleri
- **Bileşenler:**
  - Activities (11 adet)
  - Fragments (3 adet)
  - Adapters (2 adet)
  - Custom Views
- **Özellikler:**
  - ViewBinding ile type-safe view erişimi
  - DataBinding ile reactive UI
  - Material Design 3 components
  - Custom animations

#### 2. **ViewModel Layer**
- **Sorumluluk:** UI logic ve state management
- **Bileşenler:**
  - GameViewModel
  - UserViewModel
  - SharedViewModel (ebeveyn-çocuk iletişimi)
- **Özellikler:**
  - LiveData for observable data
  - Coroutines for async operations
  - Lifecycle awareness
  - SavedStateHandle for process death

#### 3. **Domain Layer (Business Logic)**
- **Sorumluluk:** İş mantığı ve use cases
- **Bileşenler:**
  - Use Cases (her özellik için)
  - Business rules
  - Validators
  - Mappers
- **Özellikler:**
  - Platform independent
  - Testable
  - Reusable

#### 4. **Data Layer**
- **Sorumluluk:** Veri yönetimi ve persistence
- **Bileşenler:**
  - Repositories
  - Data sources (Local, Remote)
  - Database (Room)
  - Network (Retrofit)
  - Cache management
- **Özellikler:**
  - Single source of truth
  - Offline-first architecture
  - Data synchronization
  - Caching strategies

---

## MVVM Mimarisi

### Model-View-ViewModel Pattern

```kotlin
// Example: Game Module Architecture

┌─────────────────────────────────────────────────────────┐
│                       VIEW                              │
│  ┌──────────────────────────────────────────────────┐  │
│  │  GameActivity / GameFragment                     │  │
│  │  - observe(viewModel.gameState)                  │  │
│  │  - viewModel.startGame()                         │  │
│  │  - viewModel.submitAnswer(answer)                │  │
│  └──────────────────────────────────────────────────┘  │
└───────────────────────┬─────────────────────────────────┘
                        │ (LiveData/StateFlow)
┌───────────────────────┴─────────────────────────────────┐
│                    VIEWMODEL                            │
│  ┌──────────────────────────────────────────────────┐  │
│  │  GameViewModel                                    │  │
│  │  - val gameState: LiveData<GameState>            │  │
│  │  - fun startGame()                                │  │
│  │  - fun submitAnswer(answer: String)               │  │
│  │  - fun calculateScore()                           │  │
│  └──────────────────────────────────────────────────┘  │
└───────────────────────┬─────────────────────────────────┘
                        │ (Repository Pattern)
┌───────────────────────┴─────────────────────────────────┐
│                      MODEL                              │
│  ┌──────────────────────────────────────────────────┐  │
│  │  GameRepository                                   │  │
│  │  - fun getGames(): Flow<List<Game>>              │  │
│  │  - fun saveProgress(progress: UserProgress)      │  │
│  │                                                    │  │
│  │  Data Classes:                                    │  │
│  │  - Game                                           │  │
│  │  - Question                                       │  │
│  │  - UserProgress                                   │  │
│  └──────────────────────────────────────────────────┘  │
└─────────────────────────────────────────────────────────┘
```

### ViewModel Implementation Example

```kotlin
class GameViewModel(
    private val gameRepository: GameRepository,
    private val userRepository: UserRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    
    // UI State
    private val _gameState = MutableLiveData<GameState>()
    val gameState: LiveData<GameState> = _gameState
    
    // User progress
    private val _userProgress = MutableLiveData<UserProgress>()
    val userProgress: LiveData<UserProgress> = _userProgress
    
    // Loading state
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
    
    // Error handling
    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error
    
    init {
        loadGames()
    }
    
    fun loadGames() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                gameRepository.getGames()
                    .catch { e ->
                        _error.value = e.message
                    }
                    .collect { games ->
                        _gameState.value = GameState.Success(games)
                    }
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun startGame(gameId: String) {
        viewModelScope.launch {
            val game = gameRepository.getGameById(gameId)
            _gameState.value = GameState.Playing(game)
        }
    }
    
    fun submitAnswer(answer: String) {
        viewModelScope.launch {
            val currentGame = (_gameState.value as? GameState.Playing)?.game
            currentGame?.let { game ->
                val isCorrect = game.checkAnswer(answer)
                if (isCorrect) {
                    updateProgress(game.id, game.difficulty)
                    _gameState.value = GameState.CorrectAnswer
                } else {
                    _gameState.value = GameState.WrongAnswer
                }
            }
        }
    }
    
    private suspend fun updateProgress(gameId: String, difficulty: Int) {
        userRepository.updateProgress(
            gameId = gameId,
            score = calculateScore(difficulty),
            timestamp = System.currentTimeMillis()
        )
    }
    
    private fun calculateScore(difficulty: Int): Int {
        return when (difficulty) {
            1 -> 10
            2 -> 20
            3 -> 30
            else -> 5
        }
    }
}

sealed class GameState {
    object Idle : GameState()
    object Loading : GameState()
    data class Success(val games: List<Game>) : GameState()
    data class Playing(val game: Game) : GameState()
    object CorrectAnswer : GameState()
    object WrongAnswer : GameState()
    data class Error(val message: String) : GameState()
}
```

---

## Katmanlar

### 1. Presentation Layer (Detaylı)

#### Activities

**Splash Activity**
```kotlin
/**
 * Açılış ekranı - Uygulama başlangıcında gösterilir
 * 
 * Sorumluluklar:
 * - Uygulama başlangıç animasyonu
 * - İlk veri yüklemesi kontrolü
 * - Kullanıcı oturumu kontrolü
 * - WelcomeActivity veya MainActivity'ye yönlendirme
 */
class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        // Animation
        startSplashAnimation()
        
        // Check user session
        checkUserSession()
    }
    
    private fun startSplashAnimation() {
        binding.logoImageView.animate()
            .alpha(1f)
            .scaleX(1f)
            .scaleY(1f)
            .setDuration(1500)
            .start()
    }
    
    private fun checkUserSession() {
        lifecycleScope.launch {
            delay(2000)
            val hasUser = PreferencesHelper.hasActiveUser()
            navigateToNextScreen(hasUser)
        }
    }
    
    private fun navigateToNextScreen(hasUser: Boolean) {
        val intent = if (hasUser) {
            Intent(this, MainActivity::class.java)
        } else {
            Intent(this, WelcomeActivity::class.java)
        }
        startActivity(intent)
        finish()
    }
}
```

**Welcome Activity**
```kotlin
/**
 * Karşılama ekranı - İlk kullanımda profil oluşturma
 * 
 * Sorumluluklar:
 * - Hoş geldin animasyonu
 * - Profil oluşturma formu
 * - Avatar seçimi
 * - Yaş ve isim girişi
 * - İlk kullanım tutorial'ı
 */
class WelcomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWelcomeBinding
    private val viewModel: UserViewModel by viewModels()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        setupUI()
        observeViewModel()
    }
    
    private fun setupUI() {
        binding.btnStart.setOnClickListener {
            val name = binding.etName.text.toString()
            val age = binding.etAge.text.toString().toIntOrNull() ?: 0
            val avatar = selectedAvatar
            
            if (validateInput(name, age)) {
                createUserProfile(name, age, avatar)
            }
        }
    }
    
    private fun validateInput(name: String, age: Int): Boolean {
        return when {
            name.isBlank() -> {
                showError("Lütfen isim giriniz")
                false
            }
            age !in 3..8 -> {
                showError("Yaş 3-8 arasında olmalıdır")
                false
            }
            else -> true
        }
    }
    
    private fun createUserProfile(name: String, age: Int, avatar: String) {
        viewModel.createProfile(name, age, avatar)
    }
    
    private fun observeViewModel() {
        viewModel.profileCreated.observe(this) { success ->
            if (success) {
                navigateToMainActivity()
            }
        }
    }
}
```

**Main Activity**
```kotlin
/**
 * Ana ekran - Modül seçimi ve navigasyon
 * 
 * Sorumluluklar:
 * - Bottom navigation
 * - Fragment yönetimi
 * - Home, Profile, Settings fragments
 * - Background music service kontrolü
 * - Notification handling
 */
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: UserViewModel by viewModels()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        setupBottomNavigation()
        setupModuleCards()
        startBackgroundMusic()
    }
    
    private fun setupBottomNavigation() {
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> showHomeFragment()
                R.id.nav_profile -> showProfileFragment()
                R.id.nav_settings -> showSettingsFragment()
            }
            true
        }
    }
    
    private fun setupModuleCards() {
        binding.cardLanguage.setOnClickListener {
            navigateToModule(DilGelisimActivity::class.java)
        }
        binding.cardMath.setOnClickListener {
            navigateToModule(MatematikActivity::class.java)
        }
        // ... diğer modüller
    }
    
    private fun startBackgroundMusic() {
        if (PreferencesHelper.isMusicEnabled()) {
            val intent = Intent(this, BackgroundMusicService::class.java)
            startService(intent)
        }
    }
}
```

#### Fragments

**Home Fragment**
```kotlin
/**
 * Ana sayfa fragment - Günlük aktiviteler ve öneriler
 */
class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: GameViewModel by viewModels()
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        setupRecyclerView()
        observeViewModel()
        loadDailyActivities()
    }
    
    private fun setupRecyclerView() {
        binding.rvActivities.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = GameAdapter { game ->
                navigateToGame(game)
            }
        }
    }
    
    private fun observeViewModel() {
        viewModel.gameState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is GameState.Success -> {
                    (binding.rvActivities.adapter as GameAdapter).submitList(state.games)
                }
                is GameState.Error -> showError(state.message)
                else -> { /* handle other states */ }
            }
        }
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
```

**Profile Fragment**
```kotlin
/**
 * Profil fragment - Kullanıcı bilgileri ve istatistikler
 */
class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private val viewModel: UserViewModel by viewModels()
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        observeUserProfile()
        observeUserProgress()
        setupAchievements()
    }
    
    private fun observeUserProfile() {
        viewModel.userProfile.observe(viewLifecycleOwner) { profile ->
            binding.tvName.text = profile.name
            binding.tvAge.text = "${profile.age} yaş"
            binding.ivAvatar.setImageResource(profile.avatarResId)
        }
    }
    
    private fun observeUserProgress() {
        viewModel.userProgress.observe(viewLifecycleOwner) { progress ->
            binding.tvTotalScore.text = progress.totalScore.toString()
            binding.tvGamesPlayed.text = progress.gamesPlayed.toString()
            binding.pbLevel.progress = progress.levelProgress
        }
    }
    
    private fun setupAchievements() {
        binding.rvAchievements.apply {
            layoutManager = GridLayoutManager(context, 3)
            adapter = AchievementAdapter()
        }
    }
}
```

**Settings Fragment**
```kotlin
/**
 * Ayarlar fragment - Uygulama ayarları ve tercihler
 */
class SettingsFragment : Fragment() {
    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        setupSwitches()
        setupSeekBars()
        setupButtons()
    }
    
    private fun setupSwitches() {
        // Music toggle
        binding.switchMusic.apply {
            isChecked = PreferencesHelper.isMusicEnabled()
            setOnCheckedChangeListener { _, isChecked ->
                PreferencesHelper.setMusicEnabled(isChecked)
                toggleBackgroundMusic(isChecked)
            }
        }
        
        // Sound effects toggle
        binding.switchSounds.apply {
            isChecked = PreferencesHelper.areSoundsEnabled()
            setOnCheckedChangeListener { _, isChecked ->
                PreferencesHelper.setSoundsEnabled(isChecked)
            }
        }
        
        // Dark mode toggle
        binding.switchDarkMode.apply {
            isChecked = PreferencesHelper.isDarkModeEnabled()
            setOnCheckedChangeListener { _, isChecked ->
                PreferencesHelper.setDarkModeEnabled(isChecked)
                applyTheme(isChecked)
            }
        }
    }
    
    private fun setupSeekBars() {
        // Volume control
        binding.seekBarVolume.apply {
            progress = PreferencesHelper.getVolume()
            setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                    PreferencesHelper.setVolume(progress)
                    updateVolumeLevel(progress)
                }
                override fun onStartTrackingTouch(seekBar: SeekBar) {}
                override fun onStopTrackingTouch(seekBar: SeekBar) {}
            })
        }
        
        // Difficulty level
        binding.seekBarDifficulty.apply {
            progress = PreferencesHelper.getDifficultyLevel()
            setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                    PreferencesHelper.setDifficultyLevel(progress)
                    binding.tvDifficultyText.text = getDifficultyText(progress)
                }
                override fun onStartTrackingTouch(seekBar: SeekBar) {}
                override fun onStopTrackingTouch(seekBar: SeekBar) {}
            })
        }
    }
    
    private fun setupButtons() {
        binding.btnParentalControl.setOnClickListener {
            navigateToParentalControl()
        }
        
        binding.btnLanguage.setOnClickListener {
            showLanguageDialog()
        }
        
        binding.btnAbout.setOnClickListener {
            showAboutDialog()
        }
    }
}
```

### 2. ViewModel Layer (Detaylı)

#### GameViewModel - Oyun Mantığı Yönetimi

```kotlin
/**
 * Oyun durumu ve mantığını yöneten ViewModel
 * 
 * Sorumluluklar:
 * - Oyun listesi yönetimi
 * - Oyun başlatma/durdurma
 * - Skor hesaplama
 * - İlerleme kaydetme
 * - Oyun istatistikleri
 */
class GameViewModel(
    private val gameRepository: GameRepository,
    private val userRepository: UserRepository
) : ViewModel() {
    
    // ===== STATE MANAGEMENT =====
    
    private val _gameList = MutableStateFlow<List<Game>>(emptyList())
    val gameList: StateFlow<List<Game>> = _gameList.asStateFlow()
    
    private val _currentGame = MutableStateFlow<Game?>(null)
    val currentGame: StateFlow<Game?> = _currentGame.asStateFlow()
    
    private val _currentQuestion = MutableStateFlow<Question?>(null)
    val currentQuestion: StateFlow<Question?> = _currentQuestion.asStateFlow()
    
    private val _score = MutableStateFlow(0)
    val score: StateFlow<Int> = _score.asStateFlow()
    
    private val _timeRemaining = MutableStateFlow(0L)
    val timeRemaining: StateFlow<Long> = _timeRemaining.asStateFlow()
    
    private val _gameStatus = MutableStateFlow<GameStatus>(GameStatus.Idle)
    val gameStatus: StateFlow<GameStatus> = _gameStatus.asStateFlow()
    
    // ===== GAME OPERATIONS =====
    
    fun loadGames(category: String) {
        viewModelScope.launch {
            try {
                gameRepository.getGamesByCategory(category)
                    .catch { e ->
                        emitError(e.message ?: "Unknown error")
                    }
                    .collect { games ->
                        _gameList.value = games
                    }
            } catch (e: Exception) {
                emitError(e.message ?: "Failed to load games")
            }
        }
    }
    
    fun startGame(gameId: String) {
        viewModelScope.launch {
            try {
                val game = gameRepository.getGameById(gameId)
                _currentGame.value = game
                _score.value = 0
                _gameStatus.value = GameStatus.Playing
                
                loadNextQuestion()
                startTimer(game.timeLimit)
                
            } catch (e: Exception) {
                emitError(e.message ?: "Failed to start game")
            }
        }
    }
    
    fun submitAnswer(answer: String) {
        viewModelScope.launch {
            val question = _currentQuestion.value ?: return@launch
            val isCorrect = question.checkAnswer(answer)
            
            if (isCorrect) {
                handleCorrectAnswer()
            } else {
                handleWrongAnswer()
            }
            
            delay(1500) // Show feedback
            loadNextQuestion()
        }
    }
    
    private fun handleCorrectAnswer() {
        val currentQuestion = _currentQuestion.value ?: return
        val points = calculatePoints(currentQuestion.difficulty, _timeRemaining.value)
        
        _score.value += points
        _gameStatus.value = GameStatus.CorrectAnswer
        
        SoundManager.playCorrectSound()
        showCelebrationAnimation()
    }
    
    private fun handleWrongAnswer() {
        _gameStatus.value = GameStatus.WrongAnswer
        SoundManager.playWrongSound()
    }
    
    private suspend fun loadNextQuestion() {
        val game = _currentGame.value ?: return
        val nextQuestion = game.getNextQuestion()
        
        if (nextQuestion != null) {
            _currentQuestion.value = nextQuestion
            _gameStatus.value = GameStatus.Playing
        } else {
            endGame()
        }
    }
    
    private suspend fun endGame() {
        _gameStatus.value = GameStatus.Completed
        
        val finalScore = _score.value
        val game = _currentGame.value ?: return
        
        // Save progress
        saveGameProgress(game.id, finalScore)
        
        // Update achievements
        checkAndUnlockAchievements(finalScore)
        
        // Show completion screen
        showGameCompletionDialog(finalScore)
    }
    
    private fun startTimer(duration: Long) {
        viewModelScope.launch {
            var remainingTime = duration
            while (remainingTime > 0 && _gameStatus.value == GameStatus.Playing) {
                _timeRemaining.value = remainingTime
                delay(1000)
                remainingTime -= 1000
            }
            
            if (remainingTime <= 0) {
                handleTimeUp()
            }
        }
    }
    
    private fun calculatePoints(difficulty: Int, timeRemaining: Long): Int {
        val basePoints = when (difficulty) {
            1 -> 10
            2 -> 20
            3 -> 30
            else -> 5
        }
        
        val timeBonus = (timeRemaining / 1000).toInt() / 2
        return basePoints + timeBonus
    }
    
    // ===== DATA PERSISTENCE =====
    
    private suspend fun saveGameProgress(gameId: String, score: Int) {
        val progress = UserProgress(
            gameId = gameId,
            score = score,
            timestamp = System.currentTimeMillis(),
            completed = true
        )
        
        userRepository.saveProgress(progress)
    }
    
    private suspend fun checkAndUnlockAchievements(score: Int) {
        when {
            score >= 1000 -> unlockAchievement("master_player")
            score >= 500 -> unlockAchievement("expert_player")
            score >= 100 -> unlockAchievement("good_player")
        }
    }
    
    private suspend fun unlockAchievement(achievementId: String) {
        userRepository.unlockAchievement(achievementId)
    }
    
    // ===== HELPER FUNCTIONS =====
    
    private fun emitError(message: String) {
        _gameStatus.value = GameStatus.Error(message)
    }
    
    private fun showCelebrationAnimation() {
        // Trigger celebration animation in UI
    }
    
    private fun showGameCompletionDialog(score: Int) {
        // Trigger completion dialog in UI
    }
    
    private fun handleTimeUp() {
        _gameStatus.value = GameStatus.TimeUp
        viewModelScope.launch {
            delay(2000)
            endGame()
        }
    }
}

sealed class GameStatus {
    object Idle : GameStatus()
    object Loading : GameStatus()
    object Playing : GameStatus()
    object CorrectAnswer : GameStatus()
    object WrongAnswer : GameStatus()
    object TimeUp : GameStatus()
    object Completed : GameStatus()
    data class Error(val message: String) : GameStatus()
}
```

#### UserViewModel - Kullanıcı Yönetimi

```kotlin
/**
 * Kullanıcı profili ve ilerleme yönetimi
 */
class UserViewModel(
    private val userRepository: UserRepository
) : ViewModel() {
    
    private val _userProfile = MutableLiveData<UserProfile>()
    val userProfile: LiveData<UserProfile> = _userProfile
    
    private val _userProgress = MutableLiveData<UserProgress>()
    val userProgress: LiveData<UserProgress> = _userProgress
    
    private val _achievements = MutableLiveData<List<Achievement>>()
    val achievements: LiveData<List<Achievement>> = _achievements
    
    private val _profileCreated = MutableLiveData<Boolean>()
    val profileCreated: LiveData<Boolean> = _profileCreated
    
    init {
        loadUserProfile()
    }
    
    fun createProfile(name: String, age: Int, avatarId: String) {
        viewModelScope.launch {
            try {
                val profile = UserProfile(
                    id = generateUserId(),
                    name = name,
                    age = age,
                    avatarId = avatarId,
                    createdAt = System.currentTimeMillis()
                )
                
                userRepository.createProfile(profile)
                _profileCreated.value = true
                
            } catch (e: Exception) {
                _profileCreated.value = false
            }
        }
    }
    
    fun loadUserProfile() {
        viewModelScope.launch {
            userRepository.getUserProfile()
                .collect { profile ->
                    _userProfile.value = profile
                }
        }
    }
    
    fun updateProfile(profile: UserProfile) {
        viewModelScope.launch {
            userRepository.updateProfile(profile)
        }
    }
    
    fun loadUserProgress() {
        viewModelScope.launch {
            userRepository.getUserProgress()
                .collect { progress ->
                    _userProgress.value = progress
                }
        }
    }
    
    fun loadAchievements() {
        viewModelScope.launch {
            userRepository.getAchievements()
                .collect { achievements ->
                    _achievements.value = achievements
                }
        }
    }
    
    private fun generateUserId(): String {
        return UUID.randomUUID().toString()
    }
}
```

### 3. Data Layer (Detaylı)

#### Repository Pattern

```kotlin
/**
 * GameRepository - Oyun verilerini yönetir
 */
class GameRepository(
    private val gameDao: GameDao,
    private val assetManager: AssetManager
) {
    
    suspend fun getGamesByCategory(category: String): Flow<List<Game>> = flow {
        // Try to get from local database first
        val localGames = gameDao.getGamesByCategory(category)
        
        if (localGames.isNotEmpty()) {
            emit(localGames)
        } else {
            // Load from assets if not in database
            val games = loadGamesFromAssets(category)
            gameDao.insertAll(games)
            emit(games)
        }
    }.flowOn(Dispatchers.IO)
    
    suspend fun getGameById(gameId: String): Game {
        return withContext(Dispatchers.IO) {
            gameDao.getGameById(gameId)
        }
    }
    
    private fun loadGamesFromAssets(category: String): List<Game> {
        val json = assetManager.open("data/games.json")
            .bufferedReader()
            .use { it.readText() }
        
        val gamesJson = JSONObject(json).getJSONArray("games")
        val games = mutableListOf<Game>()
        
        for (i in 0 until gamesJson.length()) {
            val gameJson = gamesJson.getJSONObject(i)
            if (gameJson.getString("category") == category) {
                games.add(parseGame(gameJson))
            }
        }
        
        return games
    }
    
    private fun parseGame(json: JSONObject): Game {
        return Game(
            id = json.getString("id"),
            name = json.getString("name"),
            category = json.getString("category"),
            difficulty = json.getInt("difficulty"),
            description = json.getString("description"),
            imageUrl = json.getString("imageUrl"),
            timeLimit = json.getLong("timeLimit"),
            questions = parseQuestions(json.getJSONArray("questions"))
        )
    }
    
    private fun parseQuestions(jsonArray: JSONArray): List<Question> {
        val questions = mutableListOf<Question>()
        for (i in 0 until jsonArray.length()) {
            val questionJson = jsonArray.getJSONObject(i)
            questions.add(parseQuestion(questionJson))
        }
        return questions
    }
    
    private fun parseQuestion(json: JSONObject): Question {
        return Question(
            id = json.getString("id"),
            text = json.getString("text"),
            type = QuestionType.valueOf(json.getString("type")),
            options = parseOptions(json.getJSONArray("options")),
            correctAnswer = json.getString("correctAnswer"),
            difficulty = json.getInt("difficulty"),
            hint = json.optString("hint", null)
        )
    }
}
```

```kotlin
/**
 * UserRepository - Kullanıcı verilerini yönetir
 */
class UserRepository(
    private val userDao: UserProgressDao,
    private val preferencesHelper: PreferencesHelper
) {
    
    fun getUserProfile(): Flow<UserProfile> = flow {
        val profileJson = preferencesHelper.getUserProfile()
        if (profileJson.isNotEmpty()) {
            emit(parseUserProfile(profileJson))
        }
    }.flowOn(Dispatchers.IO)
    
    suspend fun createProfile(profile: UserProfile) {
        withContext(Dispatchers.IO) {
            val json = serializeUserProfile(profile)
            preferencesHelper.saveUserProfile(json)
        }
    }
    
    suspend fun updateProfile(profile: UserProfile) {
        withContext(Dispatchers.IO) {
            val json = serializeUserProfile(profile)
            preferencesHelper.saveUserProfile(json)
        }
    }
    
    fun getUserProgress(): Flow<UserProgress> = flow {
        val progress = userDao.getUserProgress()
        emit(progress)
    }.flowOn(Dispatchers.IO)
    
    suspend fun saveProgress(progress: UserProgress) {
        withContext(Dispatchers.IO) {
            userDao.insertProgress(progress.toEntity())
        }
    }
    
    fun getAchievements(): Flow<List<Achievement>> = flow {
        val achievements = userDao.getAchievements()
        emit(achievements.map { it.toAchievement() })
    }.flowOn(Dispatchers.IO)
    
    suspend fun unlockAchievement(achievementId: String) {
        withContext(Dispatchers.IO) {
            userDao.unlockAchievement(achievementId, System.currentTimeMillis())
        }
    }
}
```

---

## Threading Model

### Coroutines ve Dispatchers

```kotlin
/**
 * Threading Strategy
 */

// Main/UI Thread - UI operations
viewModelScope.launch(Dispatchers.Main) {
    binding.progressBar.visibility = View.VISIBLE
}

// IO Thread - Network, Database, File operations
viewModelScope.launch(Dispatchers.IO) {
    val data = database.getData()
    val file = readFromFile()
}

// Default Thread - CPU-intensive operations
viewModelScope.launch(Dispatchers.Default) {
    val result = complexCalculation()
}

// Example: Combining dispatchers
fun loadGameData() {
    viewModelScope.launch {
        // UI thread
        showLoading()
        
        // Switch to IO
        val games = withContext(Dispatchers.IO) {
            database.getGames()
        }
        
        // Switch to Default for processing
        val processedGames = withContext(Dispatchers.Default) {
            games.map { processGame(it) }
        }
        
        // Back to UI thread
        updateUI(processedGames)
        hideLoading()
    }
}
```

### Flow and StateFlow

```kotlin
/**
 * Reactive Data Streams
 */

// StateFlow - Hot stream, always has a value
private val _uiState = MutableStateFlow<UiState>(UiState.Idle)
val uiState: StateFlow<UiState> = _uiState.asStateFlow()

// Flow - Cold stream, emits only when collected
fun getGames(): Flow<List<Game>> = flow {
    emit(database.getGames())
}.flowOn(Dispatchers.IO)

// Combining flows
val combinedData = combine(
    userProfile,
    userProgress,
    achievements
) { profile, progress, achievements ->
    UserDashboard(profile, progress, achievements)
}.stateIn(
    scope = viewModelScope,
    started = SharingStarted.WhileSubscribed(5000),
    initialValue = UserDashboard.Empty
)
```

---

## Design Patterns

### 1. Repository Pattern
- Single source of truth
- Data abstraction
- Testability

### 2. Observer Pattern
- LiveData/StateFlow
- Reactive UI updates
- Event handling

### 3. Factory Pattern
- ViewModel creation
- Object instantiation

### 4. Singleton Pattern
- Database instance
- Preferences helper
- Sound manager

### 5. Strategy Pattern
- Game algorithms
- Difficulty levels

### 6. Adapter Pattern
- RecyclerView adapters
- Data transformation

---

## State Management

### UI State Handling

```kotlin
data class UiState(
    val isLoading: Boolean = false,
    val data: List<Game> = emptyList(),
    val error: String? = null
)

// In ViewModel
private val _uiState = MutableStateFlow(UiState())
val uiState: StateFlow<UiState> = _uiState.asStateFlow()

// In Activity/Fragment
lifecycleScope.launch {
    viewModel.uiState.collect { state ->
        binding.progressBar.isVisible = state.isLoading
        if (state.error != null) {
            showError(state.error)
        }
        adapter.submitList(state.data)
    }
}
```

### SavedStateHandle for Process Death

```kotlin
class GameViewModel(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    
    var currentScore: Int
        get() = savedStateHandle.get<Int>("score") ?: 0
        set(value) {
            savedStateHandle["score"] = value
        }
    
    var currentLevel: Int
        get() = savedStateHandle.get<Int>("level") ?: 1
        set(value) {
            savedStateHandle["level"] = value
        }
}
```

---

## Kesişim Noktaları ve İletişim

### Activity <-> Fragment Communication

```kotlin
// SharedViewModel approach
class SharedViewModel : ViewModel() {
    private val _selectedGame = MutableLiveData<Game>()
    val selectedGame: LiveData<Game> = _selectedGame
    
    fun selectGame(game: Game) {
        _selectedGame.value = game
    }
}

// In Activity
val sharedViewModel: SharedViewModel by viewModels()

// In Fragment
val sharedViewModel: SharedViewModel by activityViewModels()
```

### Service <-> Activity Communication

```kotlin
// Using LocalBroadcastManager
class BackgroundMusicService : Service() {
    fun sendUpdateBroadcast() {
        val intent = Intent("MUSIC_UPDATE")
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
    }
}

// In Activity
override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    LocalBroadcastManager.getInstance(this).registerReceiver(
        musicReceiver,
        IntentFilter("MUSIC_UPDATE")
    )
}
```

---

## Sonuç

Bu mimari yapı, Mete Eğitici uygulamasının:
- ✅ Bakım yapılabilir
- ✅ Test edilebilir
- ✅ Genişletilebilir
- ✅ Performanslı

olmasını sağlar.

Daha fazla bilgi için:
- [Kotlin Coroutines Guide](https://kotlinlang.org/docs/coroutines-guide.html)
- [Android Architecture Components](https://developer.android.com/topic/architecture)
- [MVVM Pattern](https://developer.android.com/topic/libraries/architecture/viewmodel)
