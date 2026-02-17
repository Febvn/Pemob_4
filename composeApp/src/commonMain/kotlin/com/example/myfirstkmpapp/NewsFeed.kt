package com.example.myfirstkmpapp

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

// --- 1. Model & Repository ---

// Data class untuk merepresentasikan entitas Berita
data class News(
    val id: Int,
    val title: String,
    val category: NewsCategory,
    val summary: String,
    val timestamp: String,
    val isRead: Boolean = false
)

// Enum untuk kategori berita yang tersedia
enum class NewsCategory {
    ALL, TECHNOLOGY, SPORTS, BUSINESS, CRYPTO, POLITICS
}

// Enum untuk navigasi antar halaman (Tab)
enum class ScreenTab {
    HOME, FEED, PROFILE
}

// Layer Repository untuk menangani simulasi data aliran (Flow)
class NewsRepository {
    // Penghitung ID dipindahkan ke tingkat kelas agar tidak terulang dari 1 saat koneksi 'pulih' (retry)
    private var idCounter = 1

    // Implementasi Flow Builder untuk mensimulasikan sumber data berita
    private val _rawNewsFlow = flow {
        while (true) {
            delay(2000) // Simulasi penundaan jaringan selama 2 detik
            
            // Implementasi Error Handling: Simulasi gangguan jaringan setiap kelipatan 15
            if (idCounter % 15 == 0) {
                idCounter++ // Lewati ID ini agar tidak terjebak dalam loop error yang sama
                throw Exception("Simulasi Gangguan Jaringan") 
            }
            
            // Melepaskan (emit) objek berita baru ke aliran data
            emit(generateRandomNews(idCounter++))
        }
    }

    // Aliran data berita dengan operator Flow (Rubrik: onEach, catch)
    val newsFlow: Flow<News> = _rawNewsFlow
        .onEach { println("Repo: Mengirim berita #${it.id}") }
        .catch { e -> // Menangani kesalahan dalam aliran data (Bonus: catch)
            println("Kesalahan Repo: ${e.message}")
            // Mengirimkan pesan sistem sebagai pengganti saat terjadi error
            emit(News(0, "Peringatan Sistem", NewsCategory.ALL, "Koneksi tidak stabil. Sedang menghubungkan kembali...", "Sekarang", true))
            delay(1000)
            // Error ini akan memicu aliran berhenti, jadi kita butuh .retry() di bawah
            throw e 
        }
        .retry() // Mengulangi aliran data secara otomatis setelah ditangkap oleh .catch

    // Fungsi pembantu untuk menghasilkan data berita acak dengan variasi lebih tinggi
    private fun generateRandomNews(id: Int): News {
        val categories = NewsCategory.values().filter { it != NewsCategory.ALL }
        val category = categories.random()
        
        // Kumpulan template fragmen berita untuk meningkatkan variasi
        val techTopics = listOf("AI Quantum", "Neural Link", "Metaverse", "Blockchain", "6G Network", "Cyber Security")
        val sportsTopics = listOf("World Cup", "Olympics", "Esports Finals", "Super Bowl", "Grand Slam", "Formula 1")
        val businessTopics = listOf("Stock Market", "Start-up Unicorn", "Global Trade", "Inflation Rate", "Electric Vehicles")
        val cryptoTopics = listOf("Bitcoin", "Ethereum", "NFT", "DeFi", "Web3", "Mining Rig")
        val politicsTopics = listOf("G20 Summit", "Electoral Reform", "Diplomatic Visit", "Social Policy", "Environment Act")

        val actions = listOf("Mencapai Rekor", "Menghadapi Krisis", "Memicu Debat", "Mendapat Investasi Besar", "Transformasi Digital")
        val regions = listOf("Asia-Pasifik", "Eropa Barat", "Jakarta", "Ibu Kota Baru", "Silicon Valley", "Global")

        val (title, summary) = when (category) {
            NewsCategory.TECHNOLOGY -> {
                val topic = techTopics.random()
                val action = actions.random()
                "$topic $action di ${regions.random()}" to "Inovasi terbaru dalam $topic telah mengubah cara industri teknologi beroperasi secara fundamental."
            }
            NewsCategory.SPORTS -> {
                val topic = sportsTopics.random()
                "Update: $topic ${regions.random()} Dimulai" to "Seluruh dunia tertuju pada turnamen $topic yang diprediksi akan memecahkan rekor penonton tahun ini."
            }
            NewsCategory.BUSINESS -> {
                val topic = businessTopics.random()
                "Analisis: $topic dan Masa Depan Ekonomi" to "Para ahli memprediksi bahwa perkembangan $topic akan menjadi penggerak utama ekonomi di kuartal mendatang."
            }
            NewsCategory.CRYPTO -> {
                val topic = cryptoTopics.random()
                "Lonjakan $topic: Apa yang Perlu Diketahui?" to "Evolusi $topic terus berlanjut dengan adopsi institusi yang semakin luas dan regulasi yang semakin jelas."
            }
            NewsCategory.POLITICS -> {
                val topic = politicsTopics.random()
                "Kebijakan Baru $topic Resmi Disahkan" to "Langkah strategis terkait $topic ini diharapkan dapat membawa stabilitas jangka panjang bagi seluruh elemen masyarakat."
            }
            else -> "Breaking News: Kejadian Penting Terdeteksi" to "Informasi mendalam mengenai perkembangan terbaru yang sedang terjadi saat ini."
        }

        // Menambahkan ID dan timestamp acak agar benar-benar unik
        val uniqueTitle = "[$id] $title"
        val timeNow = "Baru Saja (${(1..59).random()}s lalu)"

        return News(id, uniqueTitle, category, summary, timeNow)
    }
}

// --- 2. ViewModel ---

// ViewModel untuk mengelola logika bisnis dan status UI
class NewsViewModel {
    private val repository = NewsRepository()
    private val _allNews = mutableListOf<News>()

    // Implementasi StateFlow untuk menyimpan status UI secara reaktif
    private val _newsState = MutableStateFlow<List<News>>(emptyList())
    val newsState: StateFlow<List<News>> = _newsState

    private val _readCount = MutableStateFlow(0)
    val readCount: StateFlow<Int> = _readCount

    private val _selectedCategory = MutableStateFlow(NewsCategory.ALL)
    val selectedCategory: StateFlow<NewsCategory> = _selectedCategory

    private val _systemAlert = MutableStateFlow<News?>(null)
    val systemAlert: StateFlow<News?> = _systemAlert

    // Coroutine Scope untuk menjalankan operasi asinkron
    private val scope = CoroutineScope(Dispatchers.Main)

    init {
        // Meluncurkan Coroutine untuk mengumpulkan data dari Flow repository
        scope.launch {
            repository.newsFlow
                .map { news -> // Operator .map untuk transformasi data (Rubrik: map)
                    news.copy(summary = news.summary.uppercase()) 
                }
                .collect { newArticle -> // Menerima data dari aliran (collect)
                    if (newArticle.id == 0) {
                        // Menangani peringatan sistem (error handling)
                        _systemAlert.value = newArticle
                        scope.launch {
                            delay(5000)
                            _systemAlert.value = null
                        }
                    } else {
                        // Menambahkan berita baru ke daftar utama
                        _allNews.add(0, newArticle)
                        applyFilter()
                    }
                }
        }
    }

    // Mengatur kategori filter yang dipilih
    fun setCategory(category: NewsCategory) {
        _selectedCategory.value = category
        applyFilter()
    }

    // Logika penyaringan berita berdasarkan kategori (Rubrik: filter)
    private fun applyFilter() {
        val currentCategory = _selectedCategory.value
        _newsState.value = if (currentCategory == NewsCategory.ALL) {
            _allNews.toList()
        } else {
            _allNews.filter { it.category == currentCategory }
        }
    }

    // Fungsi untuk menandai berita telah dibaca menggunakan Coroutines (Rubrik: async/await)
    fun markAsRead(news: News) {
        if (!news.isRead) {
            scope.launch {
                // Mensimulasikan panggilan API asinkron menggunakan async dan await
                val updateSuccess = async {
                    delay(500) // Simulasi latensi server (Coroutines)
                    true
                }.await()

                if (updateSuccess) {
                    val index = _allNews.indexOfFirst { it.id == news.id }
                    if (index != -1) {
                        _allNews[index] = _allNews[index].copy(isRead = true)
                        _readCount.value += 1
                        applyFilter()
                    }
                }
            }
        }
    }
}

// --- 3. Antarmuka Pengguna (Tema Brutalist) ---

// Definisi warna untuk tema Brutalist
val BruBg = Color(0xFFF0F0F0)
val BruYellow = Color(0xFFFFD600)
val BruBlack = Color(0xFF000000)
val BruWhite = Color(0xFFFFFFFF)
val BruAccent = Color(0xFFFF4D00)

// Layar utama aplikasi
@Composable
fun NewsScreen() {
    val viewModel = remember { NewsViewModel() }
    val newsList by viewModel.newsState.collectAsState()
    val readCount by viewModel.readCount.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val systemAlert by viewModel.systemAlert.collectAsState()
    
    // Status tab aktif saat ini
    var currentTab by remember { mutableStateOf(ScreenTab.FEED) }

    MaterialTheme {
        Scaffold(
            bottomBar = { 
                BottomNavBar(
                    currentTab = currentTab, 
                    onTabSelected = { currentTab = it }
                ) 
            },
            containerColor = BruBg
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(BruBg)
            ) {
                // Konten utama berdasarkan tab yang dipilih
                Column(modifier = Modifier.padding(16.dp)) {
                    when (currentTab) {
                        ScreenTab.FEED -> {
                            FeedContent(
                                newsList = newsList,
                                readCount = readCount,
                                selectedCategory = selectedCategory,
                                onCategorySelected = { viewModel.setCategory(it) },
                                onNewsClick = { viewModel.markAsRead(it) }
                            )
                        }
                        ScreenTab.HOME -> {
                            HomeContent(readCount)
                        }
                        ScreenTab.PROFILE -> {
                            ProfileContent()
                        }
                    }
                }

                // Notifikasi peringatan sistem mengambang
                systemAlert?.let { alert ->
                    SystemAlertNotif(alert.summary)
                }
            }
        }
    }
}

// Konten untuk tab Feed Berita
@Composable
fun FeedContent(
    newsList: List<News>,
    readCount: Int,
    selectedCategory: NewsCategory,
    onCategorySelected: (NewsCategory) -> Unit,
    onNewsClick: (News) -> Unit
) {
    // State untuk kontrol scroll daftar
    val listState = androidx.compose.foundation.lazy.rememberLazyListState()
    
    // Auto-scroll ke atas setiap kali ada berita baru yang masuk
    // LaunchedEffect akan dipicu ketika ukuran newsList berubah
    LaunchedEffect(newsList.size) {
        if (newsList.isNotEmpty()) {
            listState.animateScrollToItem(0)
        }
    }

    Column {
        // Bagian Header
        BrutalistCard(
            modifier = Modifier.fillMaxWidth().padding(bottom = 20.dp),
            backgroundColor = BruYellow
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text("RAW FEED", fontSize = 28.sp, fontWeight = FontWeight.Black, color = BruBlack)
                    Text("BRUTALIST EDITION", fontSize = 12.sp, color = BruBlack, fontWeight = FontWeight.Bold)
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text("JUMLAH", fontSize = 10.sp, color = BruBlack, fontWeight = FontWeight.Bold)
                    Text("$readCount", fontSize = 32.sp, fontWeight = FontWeight.Black, color = BruBlack)
                }
            }
        }

        // Jalur gulir horizontal untuk filter kategori
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.padding(bottom = 20.dp)
        ) {
            items(NewsCategory.values()) { category ->
                BrutalistChip(
                    text = category.name,
                    isSelected = selectedCategory == category,
                    onClick = { onCategorySelected(category) }
                )
            }
        }

        // Daftar gulir vertikal untuk item berita dengan state scroll
        LazyColumn(
            state = listState,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            items(newsList, key = { it.id }) { news ->
                NewsItem(news, onClick = { onNewsClick(news) })
            }
        }
    }
}

// Konten untuk tab Beranda (Landing Page)
@Composable
fun HomeContent(readCount: Int) {
    Column(
        modifier = Modifier.fillMaxSize().padding(horizontal = 24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        BrutalistCard(backgroundColor = BruYellow) {
            Column(
                modifier = Modifier.padding(24.dp), 
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    Icons.Default.Home, 
                    contentDescription = null, 
                    modifier = Modifier.size(64.dp).padding(bottom = 8.dp),
                    tint = BruBlack
                )
                Text("SELAMAT DATANG", fontSize = 32.sp, fontWeight = FontWeight.Black, textAlign = TextAlign.Center)
                Text("DI SIMULATOR BERITA", fontSize = 14.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
                Spacer(Modifier.height(24.dp))
                Text("ANDA TELAH MEMBACA", fontSize = 12.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
                Text("$readCount ARTIKEL", fontSize = 24.sp, fontWeight = FontWeight.Black, textAlign = TextAlign.Center)
            }
        }
    }
}

// Konten untuk tab Profil Pengguna
@Composable
fun ProfileContent() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        BrutalistCard(modifier = Modifier.size(120.dp), backgroundColor = BruWhite) {
            Icon(Icons.Default.Person, null, modifier = Modifier.fillMaxSize().padding(20.dp), tint = BruBlack)
        }
        Spacer(Modifier.height(20.dp))
        BrutalistCard(backgroundColor = BruYellow, modifier = Modifier.fillMaxWidth()) {
            Column(Modifier.padding(16.dp)) {
                Text("DETAIL PENGGUNA", fontWeight = FontWeight.Black, fontSize = 18.sp)
                Divider(color = BruBlack, thickness = 2.dp, modifier = Modifier.padding(vertical = 8.dp))
                Text("STATUS: AKTIF", fontWeight = FontWeight.Bold)
                Text("PERAN: PENGEMBANG ELITE", fontWeight = FontWeight.Bold)
                Divider(color = BruBlack, thickness = 1.dp, modifier = Modifier.padding(vertical = 8.dp))
                Text(
                    "CREATED BY FEBVN", 
                    fontWeight = FontWeight.Black, 
                    fontSize = 10.sp, 
                    color = BruAccent,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.End
                )
            }
        }
    }
}

// Komponen notifikasi peringatan mengambang
@Composable
fun SystemAlertNotif(message: String) {
    Box(
        modifier = Modifier.fillMaxWidth().padding(16.dp)
    ) {
        BrutalistCard(
            backgroundColor = BruAccent,
            shadowOffset = 4.dp
        ) {
            Row(
                modifier = Modifier.padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("PERINGATAN", fontSize = 14.sp, fontWeight = FontWeight.Black, color = BruWhite)
                Spacer(Modifier.width(12.dp))
                Text(message, fontSize = 12.sp, fontWeight = FontWeight.Black, color = BruWhite)
            }
        }
    }
}

// Komponen bilah navigasi bawah
@Composable
fun BottomNavBar(currentTab: ScreenTab, onTabSelected: (ScreenTab) -> Unit) {
    BrutalistCard(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp).height(64.dp),
        backgroundColor = BruWhite,
        shadowOffset = 4.dp
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            NavBarItem(Icons.Default.Home, "HOME", currentTab == ScreenTab.HOME) { 
                onTabSelected(ScreenTab.HOME) 
            }
            NavBarItem(Icons.Default.List, "FEED", currentTab == ScreenTab.FEED) { 
                onTabSelected(ScreenTab.FEED) 
            }
            NavBarItem(Icons.Default.Person, "PROFIL", currentTab == ScreenTab.PROFILE) { 
                onTabSelected(ScreenTab.PROFILE) 
            }
        }
    }
}

// Detail item untuk bilah navigasi bawah
@Composable
fun NavBarItem(icon: ImageVector, label: String, isSelected: Boolean, onClick: () -> Unit) {
    val color = if (isSelected) BruAccent else BruBlack
    val bgColor = if (isSelected) BruYellow else Color.Transparent
    
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .background(bgColor)
            .clickable { onClick() }
            .padding(horizontal = 12.dp, vertical = 4.dp)
    ) {
        Icon(icon, contentDescription = null, tint = BruBlack, modifier = Modifier.size(24.dp))
        Text(label, fontSize = 10.sp, fontWeight = FontWeight.Black, color = color)
    }
}

// --- Komponen Dasar Brutalist ---

// Komponen kartu dengan gaya Brutalist (border tebal dan bayangan kaku)
@Composable
fun BrutalistCard(
    modifier: Modifier = Modifier,
    backgroundColor: Color = BruWhite,
    shadowColor: Color = BruBlack,
    shadowOffset: androidx.compose.ui.unit.Dp = 8.dp,
    onClick: (() -> Unit)? = null,
    content: @Composable BoxScope.() -> Unit
) {
    Box(modifier = modifier) {
        // Lapisan bayangan
        Box(
            modifier = Modifier
                .matchParentSize()
                .offset(shadowOffset, shadowOffset)
                .background(shadowColor)
                .border(2.dp, BruBlack)
        )
        // Lapisan utama
        Box(
            modifier = Modifier
                .background(backgroundColor)
                .border(2.dp, BruBlack)
                .then(if (onClick != null) Modifier.clickable { onClick() } else Modifier),
            content = content
        )
    }
}

// Komponen chip filter dengan gaya Brutalist
@Composable
fun BrutalistChip(text: String, isSelected: Boolean, onClick: () -> Unit) {
    val bgColor = if (isSelected) BruYellow else BruWhite
    val offset = if (isSelected) 2.dp else 4.dp
    
    Box(modifier = Modifier.clickable { onClick() }) {
        Box(
            modifier = Modifier
                .matchParentSize()
                .offset(offset, offset)
                .background(BruBlack)
        )
        Box(
            modifier = Modifier
                .background(bgColor)
                .border(2.dp, BruBlack)
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Text(text, color = BruBlack, fontWeight = FontWeight.Black, fontSize = 12.sp)
        }
    }
}

// Representasi visual setiap item berita dalam daftar
@Composable
fun NewsItem(news: News, onClick: () -> Unit) {
    BrutalistCard(
        modifier = Modifier.fillMaxWidth(),
        backgroundColor = if (news.id == 0) BruAccent else BruWhite,
        shadowOffset = 6.dp,
        onClick = onClick
    ) {
        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            // Kotak kategori samping
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(BruYellow)
                    .border(2.dp, BruBlack),
                contentAlignment = Alignment.Center
            ) {
                Text(news.category.name.take(1), color = BruBlack, fontWeight = FontWeight.Black, fontSize = 20.sp)
            }
            
            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        news.category.name,
                        fontSize = 11.sp,
                        color = BruBlack,
                        fontWeight = FontWeight.Black,
                        modifier = Modifier.background(BruYellow).padding(horizontal = 4.dp)
                    )
                    if (news.isRead) {
                        Text("DIBACA ✓", fontSize = 11.sp, color = BruBlack, fontWeight = FontWeight.Black)
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(news.title, fontSize = 16.sp, fontWeight = FontWeight.Black, color = BruBlack)
                Spacer(modifier = Modifier.height(2.dp))
                Text(news.summary, fontSize = 12.sp, color = BruBlack, fontWeight = FontWeight.Medium, maxLines = 2)
            }
        }
    }
}
