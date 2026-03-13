# My Profile App: Skeuomorphic Design

[![Kotlin](https://img.shields.io/badge/Kotlin-1.9.20-blue.svg?style=flat&logo=kotlin)](https://kotlinlang.org/)
[![Compose Multiplatform](https://img.shields.io/badge/Compose%20Multiplatform-1.5.10-orange.svg?style=flat&logo=jetpack-compose)](https://www.jetbrains.com/lp/compose-multiplatform/)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

**My Profile App** adalah aplikasi profil interaktif berbasis **Kotlin Multiplatform (KMP)** dan **Compose Multiplatform** yang menampilkan desain antarmuka **Skeuomorphic**. Aplikasi ini memungkinkan pengguna untuk melihat profil dengan gaya visual yang kaya (bertekstur, bayangan mendalam, gradiasi) serta dapat diedit dan dikustomisasi warnanya secara dinamis.

---

## Fitur Utama

- **Skeuomorphic UI:** Antarmuka dengan elemen visual realistis, bayangan inset/outset, dan tekstur border yang menyerupai jahitan kulit (leather stitching).
- **Interactive Edit Mode:** Pengguna dapat mengubah data profil (Nama, Title, Bio, Kontak, Pendidikan, Keahlian) secara langsung di dalam aplikasi.
- **Dynamic Color Theme:** Dilengkapi dengan *Color Wheel* yang memungkinkan pengguna meracik tema warna (SkeuPalette) secara custom menggunakan algoritma HSL.
- **Profile Picture Upload:** Memungkinkan pengguna untuk mengunggah dan menghapus foto profil kustom khusus di platform Desktop/JVM.

---

## Screenshot & Demo

Berikut adalah tampilan antarmuka aplikasi:

| Contact Information & Profile Overview | Education & Skills | Edit Profile Page & Color Wheel |
| :---: | :---: | :---: |
| ![Contact Information](1.JPG) | ![Education skills](2.JPG) | ![Edit profile page](3.JPG) |

### Galeri Fitur Lainnya

| Tampilan 1 | Tampilan 2 | Tampilan 3 |
| :---: | :---: | :---: |
| ![Screenshot 4](4.JPG) | ![Screenshot 5](5.JPG) | ![Screenshot 7](7.JPG) |

> *Aplikasi ini menggunakan ikon-ikon Lucid/Outlined bawaan Material 3 dan mendemonstrasikan kapabilitas styling tingkat lanjut dari Jetpack Compose.*

---

## Tech Stack

Komponen dan teknologi utama yang digunakan:
*   **Core:** [Kotlin Multiplatform (KMP)](https://kotlinlang.org/docs/multiplatform.html)
*   **UI Framework:** Compose Multiplatform
*   **Graphics & Drawing:** Canvas, SweepGradient, ImageBitmap (Skia)
*   **State Management:** Jetpack Compose `MutableState`, `remember`
*   **Build Tool:** Gradle (Kotlin DSL)

---

## Struktur Project

```text
├── composeApp/
│   ├── src/
│   │   ├── commonMain/
│   │   │   ├── kotlin/com/example/myfirstkmpapp/
│   │   │   │   ├── model/         # Data class ProfileData
│   │   │   │   ├── ui/
│   │   │   │   │   ├── components/ # ColorWheel, ProfileCard, InfoItem, dll
│   │   │   │   │   ├── screen/     # ProfileScreen (UI Utama)
│   │   │   │   │   └── theme/      # SkeuomorphicTheme & Palette Generator
│   │   │   │   └── util/          # Utilitas ImagePicker
│   │   │   └── App.kt             # Root / Main App
│   │   └── jvmMain/               # Implementasi ImagePicker Desktop (AWT)
│   └── build.gradle.kts
├── gradle/
└── gradlew.bat
```

---

## Instalasi & Penggunaan

### Prasyarat
- **JDK 11** atau lebih baru.
- **Android Studio** atau **IntelliJ IDEA**.

### Langkah-langkah Menjalankan
1. Clone repository:
   ```bash
   git clone https://github.com/Febvn/Pemob_3.git
   ```
2. Masuk ke direktori project:
   ```bash
   cd Pemob_3
   ```
3. Jalankan aplikasi (Desktop / JVM target):
   ```bash
   ./gradlew :composeApp:run
   ```

---

## Author

**Febrian Valentino Nugroho**
*   **GitHub:** [@Febvn](https://github.com/Febvn)

---

## License

Project ini dilisensikan di bawah **MIT License**.
