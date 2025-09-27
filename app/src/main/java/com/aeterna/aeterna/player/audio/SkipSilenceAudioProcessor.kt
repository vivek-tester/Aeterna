package com.aeterna.aeterna.player.audio

import androidx.media3.common.audio.AudioProcessor
import java.nio.ByteBuffer
import java.nio.ByteOrder

class SkipSilenceAudioProcessor : AudioProcessor {

    companion object {
        private val EMPTY_BUFFER: ByteBuffer = ByteBuffer.allocate(0)
    }

    private var active = false
    private var threshold = 0.01 // Silence threshold (e.g., 1% of max amplitude)
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

        // Simple silence detection: check if max absolute sample value is below threshold
        val byteBuffer = inputBuffer.asReadOnlyBuffer().order(ByteOrder.nativeOrder())
        val shortBuffer = byteBuffer.asShortBuffer()
        var isSilent = true
        val maxAmplitude = Short.MAX_VALUE.toFloat()

        while (shortBuffer.hasRemaining()) {
            val sample = shortBuffer.get().toFloat()
            if (Math.abs(sample / maxAmplitude) > threshold) {
                isSilent = false
                break
            }
        }

        if (!isSilent) {
            outputBuffer = inputBuffer.asReadOnlyBuffer()
        } else {
            // If silent, effectively drop the buffer by not setting outputBuffer
            outputBuffer = EMPTY_BUFFER
        }
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
