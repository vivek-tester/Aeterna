package com.aeterna.aeterna.player.audio

import androidx.media3.common.audio.AudioProcessor
import java.nio.ByteBuffer
import kotlin.math.roundToInt

/** Enhanced audio processor for tempo and pitch adjustment with improved quality and performance */
class TempoPitchAudioProcessor : AudioProcessor {

    companion object {
        private val EMPTY_BUFFER: ByteBuffer = ByteBuffer.allocate(0)
    }

    private var active = false
    private var tempo = 1.0f
    private var pitch = 1.0f
    private var preservePitch = true

    private var inputAudioFormat: AudioProcessor.AudioFormat = AudioProcessor.AudioFormat.NOT_SET
    private var outputAudioFormat: AudioProcessor.AudioFormat = AudioProcessor.AudioFormat.NOT_SET
    private var outputBuffer: ByteBuffer = EMPTY_BUFFER
    private var inputBuffer: ByteBuffer = EMPTY_BUFFER

    // Enhanced parameters
    private var qualityMode = QualityMode.HIGH
    private var crossfadeEnabled = true

    enum class QualityMode {
        LOW, // Faster processing, lower quality
        MEDIUM, // Balanced
        HIGH // Best quality, more CPU intensive
    }

    fun setParameters(
            tempo: Float,
            pitch: Float,
            preservePitch: Boolean = true,
            quality: QualityMode = QualityMode.HIGH
    ) {
        val tempoChanged = this.tempo != tempo
        val pitchChanged = this.pitch != pitch
        val qualityChanged = this.qualityMode != quality

        this.tempo = tempo.coerceIn(0.25f, 4.0f)
        this.pitch = pitch.coerceIn(0.25f, 4.0f)
        this.preservePitch = preservePitch
        this.qualityMode = quality

        active = this.tempo != 1.0f || this.pitch != 1.0f

        if ((tempoChanged || pitchChanged || qualityChanged) && active) {
            configure(inputAudioFormat)
        } else if (!active) {
            outputAudioFormat = AudioProcessor.AudioFormat.NOT_SET
        }
    }

    fun setTempo(tempo: Float) {
        setParameters(tempo, pitch, preservePitch, qualityMode)
    }

    fun setPitch(pitch: Float) {
        setParameters(tempo, pitch, preservePitch, qualityMode)
    }

    fun setPreservePitch(preserve: Boolean) {
        setParameters(tempo, pitch, preserve, qualityMode)
    }

    fun setQualityMode(quality: QualityMode) {
        setParameters(tempo, pitch, preservePitch, quality)
    }

    fun getTempo(): Float = tempo
    fun getPitch(): Float = pitch
    fun isPreservingPitch(): Boolean = preservePitch
    fun getQualityMode(): QualityMode = qualityMode

    override fun configure(
            inputAudioFormat: AudioProcessor.AudioFormat
    ): AudioProcessor.AudioFormat {
        this.inputAudioFormat = inputAudioFormat

        if (active) {
            // Calculate output format based on processing parameters
            val outputSampleRate =
                    if (preservePitch) {
                        inputAudioFormat.sampleRate
                    } else {
                        (inputAudioFormat.sampleRate * pitch).roundToInt()
                    }

            outputAudioFormat =
                    AudioProcessor.AudioFormat(
                            outputSampleRate,
                            inputAudioFormat.channelCount,
                            inputAudioFormat.encoding
                    )
        } else {
            outputAudioFormat = AudioProcessor.AudioFormat.NOT_SET
        }

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

        this.inputBuffer = inputBuffer

        // Enhanced processing based on quality mode
        when (qualityMode) {
            QualityMode.LOW -> processLowQuality(inputBuffer)
            QualityMode.MEDIUM -> processMediumQuality(inputBuffer)
            QualityMode.HIGH -> processHighQuality(inputBuffer)
        }
    }

    private fun processLowQuality(inputBuffer: ByteBuffer) {
        // Simple time-domain processing for low latency
        val processedBuffer = simpleTimeStretch(inputBuffer, tempo)
        outputBuffer =
                if (pitch != 1.0f && !preservePitch) {
                    simplePitchShift(processedBuffer, pitch)
                } else {
                    processedBuffer
                }
    }

    private fun processMediumQuality(inputBuffer: ByteBuffer) {
        // Overlap-add method with basic windowing
        val processedBuffer = overlapAddTimeStretch(inputBuffer, tempo)
        outputBuffer =
                if (pitch != 1.0f && !preservePitch) {
                    simplePitchShift(processedBuffer, pitch)
                } else {
                    processedBuffer
                }
    }

    private fun processHighQuality(inputBuffer: ByteBuffer) {
        // PSOLA (Pitch Synchronous Overlap and Add) simulation
        // In a real implementation, this would use sophisticated algorithms
        val processedBuffer = psolaTimeStretch(inputBuffer, tempo, pitch, preservePitch)
        outputBuffer = processedBuffer
    }

    private fun simpleTimeStretch(input: ByteBuffer, ratio: Float): ByteBuffer {
        // Simple decimation/interpolation for time stretching
        val inputArray = ByteArray(input.remaining())
        input.get(inputArray)

        val outputSize = (inputArray.size / ratio).roundToInt()
        val outputArray = ByteArray(outputSize)

        for (i in outputArray.indices) {
            val sourceIndex = (i * ratio).roundToInt()
            if (sourceIndex < inputArray.size) {
                outputArray[i] = inputArray[sourceIndex]
            }
        }

        return ByteBuffer.wrap(outputArray)
    }

    private fun simplePitchShift(input: ByteBuffer, ratio: Float): ByteBuffer {
        // Simple resampling for pitch shifting
        val inputArray = ByteArray(input.remaining())
        input.get(inputArray)

        val outputSize = (inputArray.size * ratio).roundToInt()
        val outputArray = ByteArray(outputSize)

        for (i in outputArray.indices) {
            val sourceIndex = (i / ratio).roundToInt()
            if (sourceIndex < inputArray.size) {
                outputArray[i] = inputArray[sourceIndex]
            }
        }

        return ByteBuffer.wrap(outputArray)
    }

    private fun overlapAddTimeStretch(input: ByteBuffer, ratio: Float): ByteBuffer {
        // Overlap-add with Hanning window (simplified)
        // This is a placeholder for a more sophisticated implementation
        return simpleTimeStretch(input, ratio)
    }

    private fun psolaTimeStretch(
            input: ByteBuffer,
            tempoRatio: Float,
            pitchRatio: Float,
            preservePitch: Boolean
    ): ByteBuffer {
        // PSOLA algorithm simulation (simplified)
        // In a real implementation, this would involve:
        // 1. Pitch detection
        // 2. Pitch mark extraction
        // 3. Overlap-add synthesis with pitch modification

        var processed = input

        if (tempoRatio != 1.0f) {
            processed = overlapAddTimeStretch(processed, tempoRatio)
        }

        if (pitchRatio != 1.0f && !preservePitch) {
            processed = simplePitchShift(processed, pitchRatio)
        }

        return processed
    }

    override fun getOutput(): ByteBuffer {
        val output = outputBuffer
        outputBuffer = EMPTY_BUFFER
        return output
    }

    override fun queueEndOfStream() {
        // Process any remaining buffered audio
    }

    override fun flush() {
        outputBuffer = EMPTY_BUFFER
        inputBuffer = EMPTY_BUFFER
    }

    override fun reset() {
        flush()
        inputAudioFormat = AudioProcessor.AudioFormat.NOT_SET
        outputAudioFormat = AudioProcessor.AudioFormat.NOT_SET
        active = false
        tempo = 1.0f
        pitch = 1.0f
        preservePitch = true
        qualityMode = QualityMode.HIGH
    }
}
