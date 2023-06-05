import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.hansung.petlifetimecare.R
import com.hansung.petlifetimecare.dogChoicePackage.DogPreference
import com.hansung.petlifetimecare.dogChoicePackage.Question3Fragment

class Question2Fragment : Fragment() {

    private lateinit var dogPreference: DogPreference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dogPreference = arguments?.getParcelable<DogPreference>("dogPreference") ?: DogPreference()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_question2, container, false)

        val buttonLarge = view.findViewById<Button>(R.id.button)
        val buttonMedium = view.findViewById<Button>(R.id.button2)
        val buttonSmall = view.findViewById<Button>(R.id.button3)

        buttonLarge.setOnClickListener {
            dogPreference.houseSize = "큼"
            navigateToNextFragment()
        }

        buttonMedium.setOnClickListener {
            dogPreference.houseSize = "중간"
            navigateToNextFragment()
        }

        buttonSmall.setOnClickListener {
            dogPreference.houseSize = "작음"
            navigateToNextFragment()
        }

        return view
    }

    private fun navigateToNextFragment() {
        val bundle = Bundle().apply {
            putParcelable("dogPreference", dogPreference)
        }

        // Update the below line to navigate to your next Fragment
        val question3Fragment = Question3Fragment()
        question3Fragment.arguments = bundle

        parentFragmentManager.beginTransaction()
            .replace(R.id.mainFrame, question3Fragment)  // container is your Fragment's container id
            .hide(this)
            .commit()
    }
}
