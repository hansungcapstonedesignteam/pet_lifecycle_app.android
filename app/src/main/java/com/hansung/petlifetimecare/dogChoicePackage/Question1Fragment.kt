import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.hansung.petlifetimecare.R
import com.hansung.petlifetimecare.dogChoicePackage.DogPreference


class Question1Fragment : Fragment() {

    private lateinit var dogPreference: DogPreference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_question1, container, false)

        dogPreference = DogPreference()

        val buttonActive = view.findViewById<Button>(R.id.button)
        val buttonMedium = view.findViewById<Button>(R.id.button2)
        val buttonLow = view.findViewById<Button>(R.id.button3)

        buttonActive.setOnClickListener {
            dogPreference.activityLevel = "많음"
            navigateToQuestion2Fragment()
        }

        buttonMedium.setOnClickListener {
            dogPreference.activityLevel = "중간"
            navigateToQuestion2Fragment()
        }

        buttonLow.setOnClickListener {
            dogPreference.activityLevel = "낮음"
            navigateToQuestion2Fragment()
        }

        return view
    }

    private fun navigateToQuestion2Fragment() {
        val bundle = Bundle().apply {
            putParcelable("dogPreference", dogPreference)
        }

        val question2Fragment = Question2Fragment()
        question2Fragment.arguments = bundle

        parentFragmentManager.beginTransaction()
            .replace(R.id.mainFrame, question2Fragment)  // container is your Fragment's container id
            .hide(this)
            .commit()
    }
}
