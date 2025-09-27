package com.aeterna.core.data.cache

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.io.File
import java.io.FileOutputStream
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request

@HiltWorker
class DownloadWorker
@AssistedInject
constructor(
        @Assisted context: Context,
        @Assisted workerParams: WorkerParameters,
        private val cacheManager: CacheManager,
        private val okHttpClient: OkHttpClient
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result =
            withContext(Dispatchers.IO) {
                val songId = inputData.getString("song_id") ?: return@withContext Result.failure()
                val videoId = inputData.getString("video_id") ?: return@withContext Result.failure()

                try {
                    cacheManager.updateDownloadProgress(songId, 0f, DownloadStatus.DOWNLOADING)

                    // In a real implementation, you would:
                    // 1. Extract audio stream URL from YouTube video
                    // 2. Download the audio file
                    // 3. Save metadata

                    // For now, simulate download
                    simulateDownload(songId)

                    cacheManager.updateDownloadProgress(songId, 1f, DownloadStatus.COMPLETED)
                    Result.success()
                } catch (e: Exception) {
                    cacheManager.updateDownloadProgress(songId, 0f, DownloadStatus.FAILED)
                    Result.failure()
                }
            }

    private suspend fun simulateDownload(songId: String) {
        // Simulate download progress
        for (i in 1..10) {
            cacheManager.updateDownloadProgress(songId, i * 0.1f, DownloadStatus.DOWNLOADING)
            kotlinx.coroutines.delay(500) // Simulate download time
        }
    }

    private suspend fun downloadAudioFile(url: String, outputFile: File): Boolean {
        return try {
            val request = Request.Builder().url(url).build()

            val response = okHttpClient.newCall(request).execute()

            if (response.isSuccessful) {
                response.body?.let { body ->
                    val contentLength = body.contentLength()
                    var downloadedBytes = 0L

                    FileOutputStream(outputFile).use { output ->
                        body.byteStream().use { input ->
                            val buffer = ByteArray(8192)
                            var bytesRead: Int

                            while (input.read(buffer).also { bytesRead = it } != -1) {
                                output.write(buffer, 0, bytesRead)
                                downloadedBytes += bytesRead

                                if (contentLength > 0) {
                                    val progress = downloadedBytes.toFloat() / contentLength
                                    // Update progress
                                }
                            }
                        }
                    }
                }
                true
            } else {
                false
            }
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}
