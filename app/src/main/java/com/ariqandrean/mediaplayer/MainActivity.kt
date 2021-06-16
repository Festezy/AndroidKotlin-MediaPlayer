package com.ariqandrean.mediaplayer

import android.annotation.SuppressLint
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.View
import android.widget.SeekBar
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var mediaPutar: MediaPlayer
    private var waktuTotal: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mediaPutar = MediaPlayer.create(this, R.raw.music)
        mediaPutar.isLooping = false
        mediaPutar.setVolume(0.5f, 0.5f)
        waktuTotal = mediaPutar.duration

        suaraSeekBar.setOnSeekBarChangeListener( @SuppressLint("AppCompatCustomView")
        object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser){
                    val jumlahVolume = progress / 100.0f
                    mediaPutar.setVolume(jumlahVolume, jumlahVolume)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }

        })

        posisiSeekBarLagu.max = waktuTotal
        posisiSeekBarLagu.setOnSeekBarChangeListener( object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser){
                    mediaPutar.seekTo(progress)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })

        Thread(Runnable {
            while (mediaPutar != null){
                try {
                    val pesan = Message()
                    pesan.what = mediaPutar.currentPosition
                    penanganan.sendMessage(pesan)
                    Thread.sleep(1000)
                } catch (e: InterruptedException){

                }
            }
        })

    }

    private val penanganan = @SuppressLint("HandlerLeak")
    object : Handler(){
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)

            val posisiSaatIni = msg.what
            posisiSeekBarLagu.progress = posisiSaatIni

            val PerkiraanWaktu = buatLabelWaktu(posisiSaatIni)
            labelPerkiraanWaktu.text = PerkiraanWaktu.toString()

            val pengingatWaktu = buatLabelWaktu(waktuTotal - posisiSaatIni)
            labelPengingatWaktu.text = "-$pengingatWaktu"
        }
    }

    private fun buatLabelWaktu(waktu: Int): Any {
        var labelWaktu = ""
        val menit = waktu / 1000 / 60
        val detik = waktu / 1000 % 60

        labelWaktu = "$menit: "
        if (detik < 10) labelWaktu += "0"
        labelWaktu += detik

        return labelWaktu
    }

    fun playBtnClick(view: View) {
        if (mediaPutar.isPlaying){
            mediaPutar.pause()
            Btn_PlayPause.setBackgroundResource(R.drawable.ic_play_arrow_24)
        } else {
            mediaPutar.start()
            Btn_PlayPause.setBackgroundColor(R.drawable.ic_pause_24)
        }
    }
}