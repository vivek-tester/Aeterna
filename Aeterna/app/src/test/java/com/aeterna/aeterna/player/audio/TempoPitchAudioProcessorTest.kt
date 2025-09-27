package com.aeterna.aeterna.player.audio

import java.nio.ByteBuffer
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class TempoPitchAudioProcessorTest {

    private lateinit var processor: TempoPitchAudioProcessor

    @Before
    fun setup() {
        processor = TempoPitchAudioProcessor()
    }

    @Test
    fun `processor is inactive by default`() {
        assertFalse(processor.isActive())
    }

    @Test
    fun `setting tempo activates processor`() {
        processor.setTempo(1.5f)
        assertTrue(processor.isActive())
    }

    @Test
    fun `setting pitch activates processor`() {
        processor.setPitch(1.2f)
        assertTrue(processor.isActive())
    }

    @Test
    fun `tempo is clamped to valid range`() {
        processor.setTempo(5.0f)
        assertEquals(4.0f, processor.getTempo(), 0.001f)

        processor.setTempo(0.1f)
        assertEquals(0.25f, processor.getTempo(), 0.001f)
    }

    @Test
    fun `pitch is clamped to valid range`() {
        processor.setPitch(5.0f)
        assertEquals(4.0f, processor.getPitch(), 0.001f)

        processor.setPitch(0.1f)
        assertEquals(0.25f, processor.getPitch(), 0.001f)
    }

    @Test
    fun `quality mode can be set and retrieved`() {
        processor.setQualityMode(TempoPitchAudioProcessor.QualityMode.HIGH)
        assertEquals(TempoPitchAudioProcessor.QualityMode.HIGH, processor.getQualityMode())

        processor.setQualityMode(TempoPitchAudioProcessor.QualityMode.LOW)
        assertEquals(TempoPitchAudioProcessor.QualityMode.LOW, processor.getQualityMode())
    }

    @Test
    fun `preserve pitch setting can be toggled`() {
        assertTrue(processor.isPreservingPitch()) // Default should be true

        processor.setPreservePitch(false)
        assertFalse(processor.isPreservingPitch())

        processor.setPreservePitch(true)
        assertTrue(processor.isPreservingPitch())
    }

    @Test
    fun `processor handles empty input buffer`() {
        val emptyBuffer = ByteBuffer.allocate(0)
        processor.queueInput(emptyBuffer)

        val output = processor.getOutput()
        assertFalse(output.hasRemaining())
    }

    @Test
    fun `reset deactivates processor and resets parameters`() {
        processor.setTempo(2.0f)
        processor.setPitch(1.5f)
        assertTrue(processor.isActive())

        processor.reset()

        assertFalse(processor.isActive())
        assertEquals(1.0f, processor.getTempo(), 0.001f)
        assertEquals(1.0f, processor.getPitch(), 0.001f)
        assertTrue(processor.isPreservingPitch())
        assertEquals(TempoPitchAudioProcessor.QualityMode.HIGH, processor.getQualityMode())
    }
}
