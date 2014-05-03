package run_for_your_life.screens;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder.AudioSource;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

public class DojoActivity extends Activity {
	private static final int AUDIO_SOURCE = AudioSource.DEFAULT;
	private static final int SAMPLE_RATE_HZ = 44100;
	private static final int CHANNEL_CONFIG = AudioFormat.CHANNEL_IN_MONO;
	private static final int AUDIO_FORMAT = AudioFormat.ENCODING_PCM_16BIT;

	private AudioRecord pulseRecorder;
	//	private AudioTrack audioPlayer;

	private DrawingSpace ds;
	private Thread pulseRecordingThread = null;
	private boolean recording = false;

	private int samplesPerSecond = SAMPLE_RATE_HZ*1; // Number of shorts to hold 1 second of audio
	private int bytesInOneSecond = samplesPerSecond*2;
	private short[][] storedPulse = new short[3][samplesPerSecond];
	private double[][] filteredPulse = new double[3][samplesPerSecond];
	private short[] maxVal = {1,1,1};
	private int batch = 0;
	private int bpm = 0;
	private int totalBeats = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dojo);

		RelativeLayout container = (RelativeLayout) findViewById(R.id.canvas);
		ds = new DrawingSpace(this);
		LayoutParams p = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		container.addView(ds, p);

		pulseRecorder = new AudioRecord(AUDIO_SOURCE, SAMPLE_RATE_HZ, CHANNEL_CONFIG, AUDIO_FORMAT, bytesInOneSecond*10);
	}

	@Override
	protected void onDestroy(){
		super.onDestroy();
		pulseRecorder.release();
	}

	public void logPulse(View view) {
		Button button = (Button) findViewById(view.getId());
		System.out.println("Logging pulse");

		if (!recording) {
			button.setText("Stop logging");
			recording = true;

			pulseRecorder.startRecording();
			// Fire off a thread to handle the audio stuff to prevent locking down the main thread
			pulseRecordingThread = new Thread(new Runnable() {
				@Override
				public void run() {
					int totalSamplesRead = 0;
					int seconds = 0;

					// Start recording audio 
					while (recording) {
						if (seconds > 0)
							bpm = totalBeats / seconds * 60;

						short[] data = new short[samplesPerSecond];

						// Read in from the audio buffer
						int samplesRead = pulseRecorder.read(data, 0, samplesPerSecond);

						if (samplesRead > 0) {
							// Read in data to a rotating array of 3 buffers holding 1 second of data each
							// We do this to prevent race conditions of reading and updating the data at the same time
							if (totalSamplesRead + samplesRead < samplesPerSecond) {
								for (int i=0; i<samplesRead; i++) {
									storedPulse[batch][totalSamplesRead+i] = data[i];
									if (data[i] > maxVal[batch])
										maxVal[batch] = data[i];
								}
								totalSamplesRead += samplesRead;
							} else {
								// Filled up the last buffer, rotate to the next one
								int firstSegment = samplesPerSecond - totalSamplesRead;
								int leftOver = samplesRead - firstSegment;
								seconds++;
								for (int i=0; i<firstSegment; i++) {
									storedPulse[batch][totalSamplesRead+i] = data[i];
									if (data[i] > maxVal[batch])
										maxVal[batch] = data[i];
								}

								Thread processPulseData = new Thread(new ProcessPulseData(batch, maxVal[batch]));
								processPulseData.start();

								batch++;
								batch %= 3;

								// Draw the last buffer of data
								ds.postInvalidate();
								
								// Start the next buffer of data
								maxVal[batch] = 1;
								for (int i=0; i<leftOver; i++) {
									storedPulse[batch][i] = data[firstSegment+i];
									if (data[i] > maxVal[batch])
										maxVal[batch] = data[i];
								}
								totalSamplesRead = leftOver;
							}
						}
					}
				}
			}, "Pulse listening thread");	
			
			// Start the thread
			pulseRecordingThread.start();
		} else {
			// Stop recording audio
			button.setText("Log Pulse");
			recording = false;
			pulseRecorder.stop();
		}

	}

	/*
	 * @note Tried a lot of things, the commented areas are parts that didn't work. 
	 */
	protected double[] lowPass( short[] input, double alpha, short inMax ) {
		//		double[] output = new double[input.length];
		//		double a = 100;
		//		double factor = a/inMax;
		//		double outMax = 0;
		//		output[0] = input[0]*factor;
		//		output[1] = input[0]*factor/2+input[1]*factor/2;
		//		if (Math.abs(output[0]) > Math.abs(output[1]))
		//			outMax = Math.abs(output[0]);
		//		else
		//			outMax = Math.abs(output[1]);
		//		
		//		for (int i=2; i<input.length; i++) {
		//			output[i] = factor*(input[i]+input[i-1]+input[i-2])/3;
		//			if (Math.abs(output[i]) > outMax)
		//				outMax = Math.abs(output[i]);
		//		}
		//		short max = 0;
		//		for (int i=0; i<input.length; i++) {
		//			if (input[i] > max)
		//				max = input[i];
		//		}

		double[] output1 = new double[input.length];
		double a1 = 20;
		double factor1 = a1/inMax;
		output1[0] = 0;//input[0]*factor1;
		double outMax1 = output1[0];
		//		double mean = output1[0];
		for ( int i=1; i<input.length; i++ ) {
			output1[i] = (output1[i-1] + alpha * (input[i]*factor1 - output1[i-1]));
			//			mean += output1[i];
			if (Math.abs(output1[i]) > outMax1)
				outMax1 = Math.abs(output1[i]);
		}
		//		mean /= input.length;
		//		double a2 = 1;
		//		double factor2 = a2/outMax1;
		//		for ( int i=0; i<input.length; i++ ) {
		//			output1[i] = (output1[i]/*-mean)*/)*factor2;
		//		}
		double[] output2 = new double[output1.length];
		double a3 = 1;
		double factor2 = a3/outMax1;
		output2[0] = 0;//(output1[0]-mean)*factor2;
		double outMax2 = output2[0];
		double mean1 = output2[0];
		for ( int i=1; i<input.length; i++ ) {
			output2[i] = (output2[i-1] + alpha * ( (output1[i]/*-mean*/)*factor2 - output2[i-1]));
			mean1 += output2[i];
			if (Math.abs(output2[i]) > outMax2)
				outMax2 = Math.abs(output2[i]);
		}
		//		System.out.println("Max val = " + outMax2);
		mean1 /=input.length;
		double a4 = 1;
		double factor3 = a4/outMax2;
		for ( int i=0; i<input.length; i++ ) {
			output2[i] = (output2[i]-mean1)*factor3;
		}
		return output2; 
	}

	/*
	 * This thread (class) will process (ie. amplify/filter) the sound data in 1 second intervals.
	 */
	private class ProcessPulseData implements Runnable {
		private int localBatch;
		private short localMax;

		ProcessPulseData (int localBatch, short localMax) {
			this.localBatch = localBatch;
			this.localMax = localMax;
		}

		@Override
		public void run() {
			System.out.println("Running batch "+batch);
			// Filter/amplify the pulse audio to prepare for beat analysis (NOT WORKING)
			filteredPulse[localBatch] = lowPass(storedPulse[localBatch], .001, localMax);

			// Run through the filtered data and figure out how many beats happened in that one second.
			int beats = 0;
			double average = 0;
			int length = filteredPulse[localBatch].length;
			for (int i=length/5; i<length; i++) {
				average += Math.abs(filteredPulse[localBatch][i]);
			}
			average /= length-length/5;
			for (int i=length/5; i<length; i++) {
				if (filteredPulse[localBatch][i] > 10*average)
					beats++;
			}
			System.out.println("Done running, beats counted = " + beats);

			// Update the global beat count for displaying on the screen
			totalBeats += beats;
		}
	}

	/*
	 * Class used to display/draw a canvas with pulse information
	 * This is all rather poorly setup, but we ran out of time to make it better.
	 */
	private class DrawingSpace extends View {
		Paint paint;
		public DrawingSpace(Context context) {
			super(context);
			paint = new Paint();
		}

		@Override
		protected void onDraw(Canvas canvas) {
			super.onDraw(canvas);
			int x = this.getWidth();
			int y = this.getHeight();

			// Paint background of canvas
			paint.setStyle(Paint.Style.FILL);
			paint.setColor(Color.WHITE);
			canvas.drawPaint(paint);
			// Write some text on canvas
			paint.setColor(Color.BLACK);
			paint.setTextSize(20);
			String text = "BPM : " + bpm;
			canvas.drawText(text, 0, 20, paint);
			// Draw y=0 line
			canvas.drawLine(0, y/2, x, y/2, paint);
			// Draw lines connecting the points of the current batch
			paint.setColor(Color.RED);
			// Draw the most recent waveform
			int lbatch;
			if (batch==0)
				lbatch = 2;
			else 
				lbatch = batch-1;
			for (int i=0; i<filteredPulse[lbatch].length; i+=2) {
				float x1 = (float) (i*x/filteredPulse[lbatch].length);
				float x2 = (float) ((i+1)*x/filteredPulse[lbatch].length);
				float y1 = (float) (filteredPulse[lbatch][i]*y/2.0+y/2.0);
				float y2 = (float) (filteredPulse[lbatch][i+1]*y/2.0+y/2.0);
				canvas.drawLine(x1,y1,x2,y2,paint);
			}
			// TODO: Figure out why this stopped working
			//           for (int i=0; i<storedPulse[batch].length; i+=2) {
			//        	   float x1 = (float) (i*x/storedPulse[batch].length);
			//        	   float x2 = (float) ((i+1)*x/storedPulse[batch].length);
			//        	   float y1 = (float) (storedPulse[batch][i]/maxVal[batch]*y/2.0+y/2.0);
			//        	   float y2 = (float) (storedPulse[batch][i+1]/maxVal[batch]*y/2.0+y/2.0);
			//        	   canvas.drawLine(x1,y1,x2,y2,paint);
			//           }
		}
	}

}
