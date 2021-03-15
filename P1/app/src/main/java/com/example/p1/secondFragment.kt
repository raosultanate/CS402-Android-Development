package com.example.p1

import android.os.Bundle
import android.util.Log
import android.view.Display
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import com.example.p1.MessageEvent.MessageEvent
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private lateinit var messageTextView: TextView
private var messageToDisplay: String? = null

/**
 * A simple [Fragment] subclass.
 * Use the [secondFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class secondFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    //listetning to event on main thread
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: MessageEvent){
        messageToDisplay = event.message
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_second, container, false)

        messageTextView = view.findViewById(R.id.tempTextView)
        val radioGroup = view.findViewById<RadioGroup>(R.id.rdioGrp)

        val sb = StringBuilder()
        sb.append(messageToDisplay).append(" K")
        messageTextView.text = sb

        if(messageToDisplay != null) {
       // messageTextView.text = messageToDisplay


            radioGroup.setOnCheckedChangeListener { group, i ->
                    var rb = view.findViewById<RadioButton>(i)
                    if (rb.id == R.id.radioKelvin) {
                        val sb = StringBuilder()
                        sb.append(messageToDisplay).append(" K")
                        messageTextView.text = sb
                    } else if (rb.id == R.id.radioFarenheit) {
                        val sb = StringBuilder()
                        sb.append(toFarenheitConvertor(messageToDisplay!!)).append(" F")
                        messageTextView.text = sb
                    } else {
                        //Celcius
                        val sb = StringBuilder()
                        sb.append(toCelciusConvertor(messageToDisplay!!)).append(" C")
                        messageTextView.text = sb
                    }

                }
        }
        return view
    }

    fun toCelciusConvertor(numberStringKelvin: String): String {
        val numberDoubleKelvin: Double = numberStringKelvin.toDouble()
        val celcius: Double = numberDoubleKelvin - 273.15
        val celciusStr = String.format("%.2f", celcius)
        return celciusStr
    }

    fun toFarenheitConvertor(numberStringKelvin: String): String {
        val numberDoubleKelvin: Double = numberStringKelvin.toDouble()
        val farenheit: Double = (((numberDoubleKelvin - 273.15)*9)/5) +32
        val farenheitStr = String.format("%.2f", farenheit)
        return farenheitStr

    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment secondFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            secondFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }


}