package com.aeterna.aeterna.player.audio

import androidx.media3.common.audio.AudioProcessor
import java.nio.ByteBuffer
import java.nio.ByteOrder
import kotlin.math.abs

class AudioNormalizationAudioProcessor : AudioProcessor {

    companion object {
        private val EMPTY_BUFFER: ByteBuffer = ByteBuffer.allocate(0)
    }

    private var active = false
    private var targetPeakAmplitude = 0.8f // Target peak amplitude (e.g., 80% of max)
    private var inputAudioFormat: AudioProcessor.AudioFormat = AudioProcessor.AudioFormat.NOT_SET
    private var outputAudioFormat: AudioProcessor.AudioFormat = AudioProcessor.AudioFormat.NOT_SET
    private var outputBuffer: ByteBuffer = EMPTY_BUFFER

    fun setEnabled(enabled: Boolean) {
        if (active != enabled) {
            active = enabled
            if (active) {
                configure(inputAudioFormat)
            } else {
                outputAudioFormat = AudioProcessor.AudioFormat.NOT_SET
            }
        }
    }

    override fun configure(
            inputAudioFormat: AudioProcessor.AudioFormat
    ): AudioProcessor.AudioFormat {
        this.inputAudioFormat = inputAudioFormat
        outputAudioFormat = if (active) inputAudioFormat else AudioProcessor.AudioFormat.NOT_SET
        return outputAudioFormat
    }

    override fun isActive(): Boolean = active

    override fun queueInput(inputBuffer: ByteBuffer) {
        if (!inputBuffer.hasRemaining()) {
            return
        }

        if (!active) {
            outputBuffer = inputBuffer.asReadOnlyBuffer()
            return
        }

        val byteBuffer = inputBuffer.asReadOnlyBuffer().order(ByteOrder.nativeOrder())
        val shortBuffer = byteBuffer.asShortBuffer()

        var maxCurrentAmplitude = 0f
        shortBuffer.rewind()
        while (shortBuffer.hasRemaining()) {
            maxCurrentAmplitude = maxOf(maxCurrentAmplitude, abs(shortBuffer.get().toFloat()))
        }

        val scaleFactor =
                if (maxCurrentAmplitude > 0) {
                    (Short.MAX_VALUE * targetPeakAmplitude) / maxCurrentAmplitude
                } else {
                    1.0f
                }

        val outputByteBuffer =
                ByteBuffer.allocate(inputBuffer.remaining()).order(ByteOrder.nativeOrder())
        val outputShortBuffer = outputByteBuffer.asShortBuffer()

        shortBuffer.rewind()
        while (shortBuffer.hasRemaining()) {
            val sample = shortBuffer.get()
            outputShortBuffer.put((sample * scaleFactor).toInt().toShort())
        }

        outputByteBuffer.flip()
        outputBuffer = outputByteBuffer
    }

    override fun getOutput(): ByteBuffer {
        val output = outputBuffer
        outputBuffer = EMPTY_BUFFER
        return output
    }

    override fun queueEndOfStream() {
        // No-op for this simplified processor
    }

    override fun flush() {
        outputBuffer = EMPTY_BUFFER
    }

    override fun reset() {
        flush()
        inputAudioFormat = AudioProcessor.AudioFormat.NOT_SET
        outputAudioFormat = AudioProcessor.AudioFormat.NOT_SET
        active = false
    }
}
