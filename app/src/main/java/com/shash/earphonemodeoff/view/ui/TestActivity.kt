package com.shash.earphonemodeoff.view.ui

import android.content.Context
import android.media.AudioManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.shash.earphonemodeoff.databinding.ActivityTestBinding


class TestActivity : AppCompatActivity() {
    private var _binding: ActivityTestBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityTestBinding.inflate(layoutInflater, null, false)
        setContentView(binding.root)
//        audioSwitch = AudioSwitch(this, loggingEnabled = true)
//        audioSwitch.start { audioDevices, selectedDevice ->
//            // TODO update UI with audio devices
//            Log.d("adfadf","Devices=${audioDevices.size},$selectedDevice")
//        }

        var mAudioMgr = getSystemService(Context.AUDIO_SERVICE) as AudioManager
        binding.button.setOnClickListener {
//            val devices: List<AudioDevice> = audioSwitch.availableAudioDevices
//            for (d in devices)
//            {
//                Log.d("adfadf","Device=${d.name}")
//            }
//            devices.find { it is AudioDevice.Speakerphone }
//                ?.let {
//                    audioSwitch.selectDevice(it)
//                    audioSwitch.activate()
//                    showToast("Speaker activated")
//                }
            if(mAudioMgr.isWiredHeadsetOn){
                mAudioMgr = getSystemService(Context.AUDIO_SERVICE) as AudioManager
                mAudioMgr.isWiredHeadsetOn = false;
                mAudioMgr.isSpeakerphoneOn = true;
                mAudioMgr.mode = AudioManager.MODE_IN_COMMUNICATION

                Toast.makeText(this, "SpeakerPhone On", Toast.LENGTH_LONG).show()
            }else{
                mAudioMgr.mode = AudioManager.MODE_IN_COMMUNICATION
                mAudioMgr.isSpeakerphoneOn = false;
                mAudioMgr.isWiredHeadsetOn = true;
                Toast.makeText(this, "Wired Headset On", Toast.LENGTH_LONG).show()
            }
        }

    }

    override fun onDestroy() {
        Log.d("adfadf","Destroyed")
       // audioSwitch.deactivate()
        super.onDestroy()

    }
}

//https://stackoverflow.com/questions/60859938/how-to-switch-audio-output-programatically-in-android-10
//https://stackoverflow.com/questions/2917184/forcing-sound-output-through-speaker-in-android
//https://stackoverflow.com/questions/31397934/how-to-play-audio-through-speaker-even-when-headset-is-plugged-in