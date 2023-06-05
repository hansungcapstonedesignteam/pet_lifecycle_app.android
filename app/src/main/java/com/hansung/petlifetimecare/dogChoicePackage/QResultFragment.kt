package com.hansung.petlifetimecare.dogChoicePackage

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hansung.petlifetimecare.R

data class Dog(
    val id: Int,
    val breed: String,
    val activityLevel: String,
    val houseSize: String,
    val hairLoss: String,
    val training: String,
    val personality: String,
    val walkFrequency: String,
    val loneliness: String,
    val otherPets: String,
    val vetCosts: String,
    val ownerPresenceRequired: Boolean,
    val suitableForSmallHouses: Boolean,
    val yardRequired: Boolean,
)
class DogAdapter(private val dogs: List<Dog>) : RecyclerView.Adapter<DogAdapter.ViewHolder>() {

    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val breedTextView: TextView = view.findViewById(R.id.textView8)
        val breedImageView: ImageView = view.findViewById(R.id.imageView3)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_qresult_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dog = dogs[position]
        holder.breedTextView.text = dog.breed

        val imageResource = getImageResource(dog.breed)
        holder.breedImageView.setImageResource(imageResource)
    }

    override fun getItemCount() = dogs.size

    private fun getImageResource(dogName: String): Int {
        return when (dogName) {
            "진돗개" -> R.drawable.jindo
            "골든 리트리버" -> R.drawable.golden_retriever
            "시바견" -> R.drawable.shiba
            "웰시코기 펨브로크" -> R.drawable.welsh_corgi_pembroke
            "프렌치 불독" -> R.drawable.french_bulldog
            "푸들" -> R.drawable.poodle
            "시추" -> R.drawable.shih_tzu
            "비숑 프리제" -> R.drawable.bichon_frise
            "치와와" -> R.drawable.chihuahua
            "보더콜리" -> R.drawable.border_collie
            "스피치" -> R.drawable.spitz
            "말티즈" -> R.drawable.maltese
            "레브라도 리트리버" -> R.drawable.labrador_retriever
            "요크셔 테리어" -> R.drawable.yorkshire_terrier
            "비글" -> R.drawable.beagle
            else -> R.drawable.shih_tzu
        }
    }
}

class QResultFragment : Fragment() {

    private lateinit var dogPreference: DogPreference
    private var dogs: List<Dog> = listOf() // Initialize your list of Dogs here

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dogPreference = arguments?.getParcelable<DogPreference>("dogPreference") ?: DogPreference()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val dogBreeds = listOf(
            Dog(1, "진돗개", "많음",  "중간", "많음", "중간", "활동적, 충성스러움", "자주", "많음", "보통", "많음", true, false, true),
            Dog(2, "골든 리트리버", "많음",  "중간", "많음", "쉬움", "친절, 온순", "자주", "많음", "잘지냄", "많음", true, false, true),
            Dog(3, "시바견", "많음", "작음", "적음", "쉬움", "독립적, 충성스러움", "자주", "많음", "잘지냄", "보통", true, false, false),
            Dog(4, "웰시코기 펨브로크", "많음", "중간", "많음", "쉬움", "활동적, 영리함", "덜 자주", "적음", "잘지냄", "적음", true, false, false),
            Dog(5, "프렌치 불독", "낮음", "작음", "많음", "쉬움", "친절, 온순", "덜 자주", "보통", "잘지냄", "보통", false, true, false),
            Dog(6, "푸들", "높음", "중간", "적음", "쉬움", "영리함, 활동적", "자주", "많음", "잘지냄", "보통", true, true, false),
            Dog(7, "시추", "중간", "작음", "많음", "쉬움", "친절, 온순", "자주", "많음", "잘지냄", "보통", true, true, false),
            Dog(8, "비숑 프리제", "중간", "작음", "적음", "쉬움", "친절, 온순", "자주", "보통", "잘지냄", "보통", true, true, false),
            Dog(9, "치와와", "중간", "작음", "적음", "어려움", "장난꾸러기, 활동적", "자주", "많음", "보통", "보통", true,false,true),
            Dog(10, "보더콜리", "높음", "중간", "많음", "쉬움", "영리함, 충성스러움", "매우 자주", "많음", "잘지냄", "보통", true, false, true),
            Dog(11, "스피치", "높음", "중간", "적음", "어려움", "활발함, 영리함", "매우 자주", "많음", "보통", "보통", true, false, true),
            Dog(12, "말티즈", "낮음", "작음", "적음", "쉬움", "친절함, 온순함", "적게", "적음", "잘지냄", "보통", true, true, false),
            Dog(13, "레브라도 리트리버", "높음", "큼", "많음", "쉬움", "친절함, 충성스러움", "매우 자주", "적음", "잘지냄", "많음", true, false, true),
            Dog(14, "요크셔 테리어", "낮음", "작음", "많음", "쉬움", "영리함, 장난꾸러기", "적게", "적음", "잘지냄", "적음", true, true, false),
            Dog(15, "비글", "높음", "중간", "많음", "쉬움", "친절함, 온순함", "매우 자주", "적음", "잘지냄", "많음", true, false, true)
        )

        val view = inflater.inflate(R.layout.fragment_qresult, container, false)

        val matchingDogs = findMatchingDogs(dogPreference,dogBreeds) // dogPreference 전달
        val recyclerView: RecyclerView = view.findViewById(R.id.dogRecyclerView)
        recyclerView.adapter = DogAdapter(matchingDogs)
        recyclerView.layoutManager = LinearLayoutManager(context)

        return view
    }


    private fun findMatchingDogs(dogPreference: DogPreference, dogBreeds: List<Dog>): List<Dog> {
        val scoredDogs = dogBreeds.map { dog ->
            val score = calculateMatchScore(dogPreference,dog )
            Pair(dog, score)
        }

        // score 순으로 정렬하고, score가 가장 높은 강아지를 반환합니다.
        val sortedDogs = scoredDogs.sortedByDescending { it.second }
        return sortedDogs.map { it.first }
    }

    private fun calculateMatchScore(dogPreference: DogPreference, dog: Dog): Int {
        var score = 0

        if (dogPreference.activityLevel == dog.activityLevel) score++
        if (dogPreference.houseSize == dog.houseSize) score++
        if (dogPreference.hairLoss == dog.hairLoss) score++
        if (dogPreference.training == dog.training) score++
        if (dogPreference.personality == dog.personality) score++
        if (dogPreference.walkFrequency == dog.walkFrequency) score++
        if (dogPreference.loneliness == dog.loneliness) score++
        if (dogPreference.otherPets == dog.otherPets) score++
        if (dogPreference.vetCosts == dog.vetCosts) score++
        if (dogPreference.ownerPresenceRequired == dog.ownerPresenceRequired) score++
        if (dogPreference.suitableForSmallHouses == dog.suitableForSmallHouses) score++
        if (dogPreference.yardRequired == dog.yardRequired) score++

        return score
    }

}
