package com.example.pocketsongbook.domain.tuner.config

/**
 * An interface providing access to audio recording and playback configuration details.
 */
interface AudioConfig {
    /**
     * The sample rate that the Audio Recorder is using, in hertz. Note that this value is device
     * dependent.
     *
     * @return The sample rate of the audio recording.
     */
    val sampleRate: Int

    /**
     * The size of the underlying input waveform buffer array used by the Audio Recorder. This should be
     * larger than the value returned by [.getReadSize]. This value should only be used to
     * create an instance of an Audio Recorder and when reading or performing operations on the
     * waveform buffer array, [.getReadSize] should be used. Note that an implementation of
     * this method should return a valid value for the device.
     *
     * @return The size of the underlying waveform buffer array.
     */
    val inputBufferSize: Int

    /**
     * The size of the underlying output waveform buffer array used by the Audio Recorder. This should be
     * larger than the value returned by [.getReadSize]. This value should only be used to
     * create an instance of an Audio Recorder and when reading or performing operations on the
     * waveform buffer array, [.getReadSize] should be used. Note that an implementation of
     * this method should return a valid value for the device.
     *
     * @return The size of the underlying waveform buffer array.
     */
    val outputBufferSize: Int

    /**
     * The size of the input array of data that should be read from the buffer. Use this value when
     * processing the buffer of waveform data.
     *
     * @return The size of values read from the underlying waveform buffer array.
     */
    val readSize: Int

    /**
     * The size of the array of data that should be written to the output buffer. Use this value when
     * processing the buffer of waveform data.
     *
     * @return The size of values written to the underlying waveform buffer array.
     */
    val writeSize: Int

    /**
     * The input channel to retrieve the input data.
     *
     * @return The input channel
     */
    val inputChannel: Int

    /**
     * The output channel to output the data. For example, a value representing Stereo.
     *
     * @return The output channel.
     */
    val outputChannel: Int

    /**
     * The input audio format of the buffer array (ex: float or short).
     *
     * @return The format of the buffer array.
     */
    val inputFormat: Int

    /**
     * The output audio format of the buffer array (ex: float or short).
     *
     * @return The format of the buffer array.
     */
    val outputFormat: Int

    /**
     * The amount of bytes to represent a single piece of output data. For instance, 16-bit PCM data
     * can be represented by two bytes.
     *
     * @return The amount of bytes needed to represent the output data.
     */
    val outputFormatByteCount: Int

    /**
     * The input source of the audio data.
     *
     * @return The input source.
     */
    val inputSource: Int
}