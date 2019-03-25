package com.softwarefactory.musicplayerts

import android.app.Activity
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.view.View

import android.widget.Button
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import java.util.concurrent.TimeUnit


class MainActivity : Activity() {
    private var b1: Button? = null
    private var b2: Button? = null
    private var b3: Button? = null
    private var b4: Button? = null
    private var iv: ImageView? = null
    private var mediaPlayer: MediaPlayer? = null

    private var startTime = 0.0
    private var finalTime = 0.0

    private val myHandler = Handler()
    private val forwardTime = 5000
    private val backwardTime = 5000
    private var seekbar: SeekBar? = null
    private var tx1: TextView? = null
    private var tx2: TextView? = null
    private var tx3: TextView? = null

    private val UpdateSongTime = object : Runnable {
        override fun run() {
            startTime = mediaPlayer!!.currentPosition.toDouble()
            tx1!!.text = String.format("%d min, %d sec",
                    TimeUnit.MILLISECONDS.toMinutes(startTime.toLong()),
                    TimeUnit.MILLISECONDS.toSeconds(startTime.toLong()) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(startTime.toLong())))
            seekbar!!.progress = startTime.toInt()
            myHandler.postDelayed(this, 100)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        b1 = findViewById<View>(R.id.button) as Button
        b2 = findViewById<View>(R.id.button2) as Button
        b3 = findViewById<View>(R.id.button3) as Button
        b4 = findViewById<View>(R.id.button4) as Button
        iv = findViewById<View>(R.id.imageView) as ImageView

        tx1 = findViewById<View>(R.id.textView2) as TextView
        tx2 = findViewById<View>(R.id.textView3) as TextView
        tx3 = findViewById<View>(R.id.textView4) as TextView
        tx3!!.text = "Song.mp3"

        mediaPlayer = MediaPlayer.create(this, R.raw.aa)
        seekbar = findViewById<View>(R.id.seekBar) as SeekBar
        seekbar!!.isClickable = true
        seekbar!!.setOnSeekBarChangeListener(object :SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                mediaPlayer!!.seekTo(p1)
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {

            }

            override fun onStopTrackingTouch(p0: SeekBar?) {

            }

        })
        b2!!.isEnabled = false

        b3!!.setOnClickListener {
            Toast.makeText(applicationContext, "Playing sound", Toast.LENGTH_SHORT).show()
            mediaPlayer!!.start()

            finalTime = mediaPlayer!!.duration.toDouble()
            startTime = mediaPlayer!!.currentPosition.toDouble()

            if (oneTimeOnly == 0) {
                seekbar!!.max = finalTime.toInt()
                oneTimeOnly = 1
            }

            tx2!!.text = String.format("%d min, %d sec",
                    TimeUnit.MILLISECONDS.toMinutes(finalTime.toLong()),
                    TimeUnit.MILLISECONDS.toSeconds(finalTime.toLong()) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(finalTime.toLong())))

            tx1!!.text = String.format("%d min, %d sec",
                    TimeUnit.MILLISECONDS.toMinutes(startTime.toLong()),
                    TimeUnit.MILLISECONDS.toSeconds(startTime.toLong()) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(startTime.toLong())))

            seekbar!!.progress = startTime.toInt()
            myHandler.postDelayed(UpdateSongTime, 100)
            b2!!.isEnabled = true
            b3!!.isEnabled = false
        }

        b2!!.setOnClickListener {
            Toast.makeText(applicationContext, "Pausing sound", Toast.LENGTH_SHORT).show()
            mediaPlayer!!.pause()
            b2!!.isEnabled = false
            b3!!.isEnabled = true
        }

        b1!!.setOnClickListener {
            val temp = startTime.toInt()

            if (temp + forwardTime <= finalTime) {
                startTime += forwardTime
                mediaPlayer!!.seekTo(startTime.toInt())
                Toast.makeText(applicationContext, "You have Jumped forward 5 seconds", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(applicationContext, "Cannot jump forward 5 seconds", Toast.LENGTH_SHORT).show()
            }
        }

        b4!!.setOnClickListener {
            val temp = startTime.toInt()

            if (temp - backwardTime > 0) {
                startTime -= backwardTime
                mediaPlayer!!.seekTo(startTime.toInt())
                Toast.makeText(applicationContext, "You have Jumped backward 5 seconds", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(applicationContext, "Cannot jump backward 5 seconds", Toast.LENGTH_SHORT).show()
            }
        }
    }

    companion object {

        var oneTimeOnly = 0
    }
}