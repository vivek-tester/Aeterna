package com.aeterna.core.data.cache

import android.content.Context
import androidx.work.Constraints
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.aeterna.core.domain.Song
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

data class DownloadState(
        val songId: String,
        val progress: Float = 0f,
        val status: DownloadStatus = DownloadStatus.PENDING
)

enum class DownloadStatus {
    PENDING,
    DOWNLOADING,
    COMPLETED,
    FAILED,
    PAUSED,
    CANCELLED
}

@Singleton
class CacheManager
@Inject
constructor(
        @ApplicationContext private val context: Context,
        private val workManager: WorkManager
) {
    private val cacheDir = File(context.cacheDir, "music_cache")
    private val downloadDir = File(context.filesDir, "downloads")

    private val _downloadStates = MutableStateFlow<Map<String, DownloadState>>(emptyMap())
    val downloadStates: Flow<Map<String, DownloadState>> = _downloadStates.asStateFlow()

    init {
        // Ensure directories exist
        cacheDir.mkdirs()
        downloadDir.mkdirs()

        // Clean up old cache files on startup
        cleanupOldCache()
    }

    fun downloadSong(song: Song) {
        val constraints =
                Constraints.Builder()
                        .setRequiredNetworkType(NetworkType.CONNECTED)
                        .setRequiresBatteryNotLow(true)
                        .build()

        val downloadData =
                Data.Builder()
                        .putString("song_id", song.id)
                        .putString("song_title", song.title)
                        .putString("song_artist", song.artist)
                        .putString("video_id", song.videoId)
                        .putString("thumbnail_url", song.thumbnailUrl)
                        .build()

        val downloadWorkRequest =
                OneTimeWorkRequestBuilder<DownloadWorker>()
                        .setConstraints(constraints)
                        .setInputData(downloadData)
                        .build()

        workManager.enqueueUniqueWork(
                "download_${song.id}",
                ExistingWorkPolicy.KEEP,
                downloadWorkRequest
        )

        // Update download state
        updateDownloadState(song.id, DownloadState(song.id, 0f, DownloadStatus.PENDING))
    }

    fun cancelDownload(songId: String) {
        workManager.cancelUniqueWork("download_$songId")
        updateDownloadState(songId, DownloadState(songId, 0f, DownloadStatus.CANCELLED))
    }

    fun pauseDownload(songId: String) {
        workManager.cancelUniqueWork("download_$songId")
        updateDownloadState(songId, DownloadState(songId, 0f, DownloadStatus.PAUSED))
    }

    fun isDownloaded(songId: String): Boolean {
        val file = File(downloadDir, "$songId.mp3")
        return file.exists() && file.length() > 0
    }

    fun getDownloadedFile(songId: String): File? {
        val file = File(downloadDir, "$songId.mp3")
        return if (file.exists() && file.length() > 0) file else null
    }

    fun getCachedFile(songId: String): File? {
        val file = File(cacheDir, "$songId.mp3")
        return if (file.exists() && file.length() > 0) file else null
    }

    fun deleteDownload(songId: String): Boolean {
        val file = File(downloadDir, "$songId.mp3")
        val metadataFile = File(downloadDir, "$songId.json")

        var success = true
        if (file.exists()) {
            success = file.delete()
        }
        if (metadataFile.exists()) {
            metadataFile.delete()
        }

        return success
    }

    fun getDownloadedSongs(): List<String> {
        return downloadDir.listFiles { _, name -> name.endsWith(".mp3") }?.map { file ->
            file.nameWithoutExtension
        }
                ?: emptyList()
    }

    fun getCacheSize(): Long {
        return calculateDirectorySize(cacheDir)
    }

    fun getDownloadSize(): Long {
        return calculateDirectorySize(downloadDir)
    }

    fun clearCache() {
        cacheDir.listFiles()?.forEach { file -> file.delete() }
    }

    fun clearAllDownloads() {
        downloadDir.listFiles()?.forEach { file -> file.delete() }
    }

    private fun cleanupOldCache() {
        val maxCacheAge = 7 * 24 * 60 * 60 * 1000L // 7 days
        val currentTime = System.currentTimeMillis()

        cacheDir.listFiles()?.forEach { file ->
            if (currentTime - file.lastModified() > maxCacheAge) {
                file.delete()
            }
        }
    }

    private fun calculateDirectorySize(directory: File): Long {
        var size = 0L
        directory.listFiles()?.forEach { file ->
            size +=
                    if (file.isDirectory) {
                        calculateDirectorySize(file)
                    } else {
                        file.length()
                    }
        }
        return size
    }

    private fun updateDownloadState(songId: String, state: DownloadState) {
        val currentStates = _downloadStates.value.toMutableMap()
        currentStates[songId] = state
        _downloadStates.value = currentStates
    }

    // Called by DownloadWorker to update progress
    fun updateDownloadProgress(songId: String, progress: Float, status: DownloadStatus) {
        updateDownloadState(songId, DownloadState(songId, progress, status))
    }
}
