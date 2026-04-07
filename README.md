# My Profile App - Pemob_4 (MVVM Edition)

[![Kotlin](https://img.shields.io/badge/Kotlin-2.0.0-blue.svg?style=flat&logo=kotlin)](https://kotlinlang.org/)
[![Compose Multiplatform](https://img.shields.io/badge/Compose%20Multiplatform-1.6.10-orange.svg?style=flat&logo=jetpack-compose)](https://www.jetbrains.com/lp/compose-multiplatform/)
[![Architecture](https://img.shields.io/badge/Architecture-MVVM-brightgreen.svg?style=flat)](https://developer.android.com/topic/architecture)

**My Profile App - Pemob_4** merupakan aplikasi Multiplatform berbasis Kotlin yang mengimplementasikan arsitektur MVVM (Model-View-ViewModel) secara penuh. Aplikasi ini menawarkan fitur manajemen profil yang dinamis dan sistem pencatatan cerdas yang efisien.

---

## What's New in Pemob_4?

*   **Integrated Note-Taking System:** Fitur pembuatan, pengeditan, dan penghapusan catatan yang tersimpan secara lokal.
*   **Professional MVVM Architecture:** Pemisahan tegas antara lapisan UI, ViewModel, dan Data untuk meningkatkan skalabilitas aplikasi.
*   **Dark Monochrome aesthetic:** Tampilan antarmuka berbasis monokrom yang elegan dengan prinsip desain skeuomorphic yang modern.
*   **Real-time State Management:** sinkronisasi data yang reaktif menggunakan StateFlow untuk pengalaman pengguna yang mulus.
*   **Optimized Performance:** Fokus pada manajemen teks dan kecepatan build yang maksimal.

---

## Screenshot & Comparison (Pemob_3 vs Pemob_4)

Aplikasi ini menunjukkan perkembangan signifikan dari aspek fungsionalitas dan desain:

### Versi Terbaru (Pemob_4)
| Profile View (Main) | Note List (Empty) | Note Detail |
| :---: | :---: | :---: |
| ![Profile View](Profile%20View.JPG) | ![Empty Notes](NoteListEmpty.JPG) | ![Note Detail](NoteDetail.JPG) |

| Add Note Form | Note List View |
| :---: | :---: |
| ![Add Note](AddNoteForm.JPG) | ![Note List](NoteListView.JPG) |

### Perbandingan Fase Pengembangan
Evaluasi dari tahap Pemob_3 menuju Pemob_4 menunjukkan transisi dari aplikasi profil statis menjadi sistem pencatatan yang modular.

---

## Struktur Proyek

Navigasi paket aplikasi mengikuti standar MVVM yang modular:

```text
├── composeApp/
│   ├── src/commonMain/kotlin/com/example/myfirstkmpapp/
│   │   ├── data/           # Lapisan Data: Note, ProfileData
│   │   ├── viewmodel/      # Lapisan Logika: NoteViewModel, ProfileViewModel
│   │   ├── ui/             # Lapisan Presentasi
│   │   │   ├── components/ # Komponen UI: NoteItem, ColorWheel, Navbar
│   │   │   ├── screen/     # Layar Utama: NoteListScreen, ProfileScreen
│   │   │   └── theme/      # Skeuomorphic Theme & Palette Generator
│   │   ├── util/           # Utilitas Gambar & File Picker
│   │   └── App.kt          # Titik Masuk Utama Aplikasi
```

---

## Instalasi & Cara Menjalankan

Langkah-langkah untuk menjalankan aplikasi pada platform Desktop (JVM):

1. **Clone Repository:**
   ```bash
   git clone https://github.com/Febvn/Pemob_4.git
   ```
2. **Konfigurasi Lingkungan (PENTING):**
   Gunakan JDK 17 atau versi yang lebih baru (Disarankan JBR dari Android Studio).
3. **Run Perintah Berikut:**
   ```powershell
   $env:JAVA_HOME = "C:\Program Files\Android\Android Studio\jbr"
   ./gradlew :composeApp:run --no-daemon
   ```

---

## Author

**Febrian Valentino Nugroho**
*   **GitHub:** [@Febvn](https://github.com/Febvn)
*   **Branch Repo (WEEK_5):** [github.com/Febvn/Pemob_4/tree/WEEK_5](https://github.com/Febvn/Pemob_4/tree/WEEK_5)
*   **Kelas:** Pemrograman Mobile (Pemob)

---

## Lisensi

Proyek ini dilisensikan di bawah **MIT License**.
