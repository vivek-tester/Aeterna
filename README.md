# 🎵 Aeterna

**A beautifully crafted, privacy-respecting YouTube Music client for Android**  
Built with Jetpack Compose, Material 3, and a deep love for music.

> No ads. No tracking. No bloat. Just a clean, fast, and intuitive experience that puts your music first.  
> *Made by Eternal.*

![Material 3 UI](https://via.placeholder.com/800x400/212121/FFFFFF?text=Aeterna+UI+Preview) <!-- Replace with real screenshot later -->

---

## ✨ Features

- 🎧 Play any song, album, artist, playlist, or video from YouTube Music  
- 🔐 Secure login with full library sync (liked songs, playlists, subscriptions)  
- 📱 **Material 3** design with **Dynamic Color**, Light, Dark, and Pure Black themes  
- 🔍 Universal search + personalized “Quick Picks” (recently/most played)  
- 📥 Offline playback with intelligent caching  
- 🎤 Synced live lyrics, skip silence, tempo/pitch control, audio normalization  
- 🎛️ Full queue management with drag-and-drop reordering  
- 📲 Background playback, lockscreen controls, sleep timer with fade-out  
- 📁 Import playlists from `.m3u`/`.pls` files or URLs  
- 🖥️ Adaptive layouts for phones, tablets, landscape, and split-screen  

---

## 📲 Download

> ⚠️ **Note**: Aeterna is **not affiliated with Google or YouTube Music**. It uses public endpoints and requires an internet connection. It does **not** host or redistribute music.

### Latest Debug APK
You can download the latest debug build from **GitHub Actions**:
1. Go to the [**Actions** tab](https://github.com/your-username/aeterna/actions)
2. Click the latest workflow run titled **“Build Debug APK”**
3. Under **Artifacts**, download `app-debug`
4. Install the APK on your Android device (enable “Install unknown apps” if needed)

> 💡 No powerful PC required — builds happen in the cloud!

---

## ⚙️ Build from Source

### Prerequisites
- Android Studio **(optional)** – you can also build via terminal
- JDK 17+
- Git

### Steps
```bash
git clone https://github.com/your-username/aeterna.git
cd aeterna
./gradlew assembleDebug
