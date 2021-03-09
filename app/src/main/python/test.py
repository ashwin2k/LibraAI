import numpy as np
from pydub import AudioSegment
import matplotlib.pyplot as plt
import matplotlib.mlab as mlab
from scipy.io import wavfile
from scipy.io.wavfile import write
import os
import tensorflow as tf
from os.path import dirname, join
import sys


fs=44100
chunk_duration = 0.5 # Each read length in seconds from mic.
feed_duration = 10
feed_samples = int(fs * feed_duration)
silence_threshold = 100

def main(x):
    print(type(x))
    return "999"
def graph_spectrogram(wav_file):
    rate, data = get_wav_info(wav_file)
    nfft = 200 # Length of each window segment
    fs = 8000 # Sampling frequencies
    noverlap = 120 # Overlap between windows
    nchannels = data.ndim
    if nchannels == 1:
        pxx, freqs, bins, im = plt.specgram(data, nfft, fs, noverlap = noverlap)
    elif nchannels == 2:
        pxx, freqs, bins, im = plt.specgram(data[:,0], nfft, fs, noverlap = noverlap)
    return pxx

def get_spectrogram(data):
    """
    Function to compute a spectrogram.
    
    Argument:
    predictions -- one channel / dual channel audio data as numpy array

    Returns:
    pxx -- spectrogram, 2-D array, columns are the periodograms of successive segments.
    """
    nfft = 200 # Length of each window segment
    fs = 8000 # Sampling frequencies
    noverlap = 120 # Overlap between windows
    nchannels = data.ndim
    if nchannels == 1:
        pxx, _, _ = mlab.specgram(data, nfft, fs, noverlap = noverlap)
    elif nchannels == 2:
        pxx, _, _ = mlab.specgram(data[:,0], nfft, fs, noverlap = noverlap)
    print(pxx.shape)
    return pxx

def processData(in_data):
    data = np.zeros(feed_samples, dtype='int16')
    data0 = in_data
    if np.abs(data0).mean() < silence_threshold:
        return -1

    data = np.append(data,data0)
    if len(data) > feed_samples:
        data = data[-feed_samples:]
        #process Data
    try:
      #  print(data.shape)
        spectrum = get_spectrogram(data)
       # print(spectrum.shape)
        preds = detect_triggerword_spectrum(spectrum)
        new_trigger = has_new_triggerword(preds, chunk_duration, feed_duration)
        if new_trigger:
            print("DEtected")
            return 1
        print("Running")

    except (KeyboardInterrupt, SystemExit):
            return -1

interpreter = tf.lite.Interpreter(model_path=join(dirname(__file__), "final_mod_2.tflite"))


def detect_triggerword_spectrum(x):
    """
    Function to predict the location of the trigger word.

    Argument:
    x -- spectrum of shape (freqs, Tx)
    i.e. (Number of frequencies, The number time steps)

    Returns:
    predictions -- flattened numpy array to shape (number of output time steps)
    """
    # the spectogram outputs  and we want (Tx, freqs) to input into the model
    x  = x.swapaxes(0,1)
    x = np.expand_dims(x, axis=0).astype('float32')
    interpreter.allocate_tensors()
    input_details = interpreter.get_input_details()
    output_details = interpreter.get_output_details()
    interpreter.set_tensor(input_details[0]['index'], x)
    interpreter.invoke()
    predictions = interpreter.get_tensor(output_details[0]['index'])

#     predictions = model.predict(x)
    return predictions.reshape(-1)
def has_new_triggerword(predictions, chunk_duration, feed_duration, threshold=0.5):
    """
    Function to detect new trigger word in the latest chunk of input audio.
    It is looking for the rising edge of the predictions data belongs to the
    last/latest chunk.

    Argument:
    predictions -- predicted labels from model
    chunk_duration -- time in second of a chunk
    feed_duration -- time in second of the input to model
    threshold -- threshold for probability above a certain to be considered positive

    Returns:
    True if new trigger word detected in the latest chunk
    """
    predictions = predictions > threshold
    chunk_predictions_samples = int(len(predictions) * chunk_duration / feed_duration)
    chunk_predictions = predictions[-chunk_predictions_samples:]
    level = chunk_predictions[0]
    for pred in chunk_predictions:
        if pred > level:
            return True
        else:
            level = pred
    return False