package stan.aac.experience.implementation.module.fandom.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import stan.aac.experience.foundation.entity.Fandom

class FandomSearchViewModel : ViewModel() {
    private val _fandoms: MutableLiveData<List<Fandom>> = MutableLiveData()
    val fandoms: LiveData<List<Fandom>> = _fandoms
    fun requestFandoms() {
        _fandoms.value = (1..10).map {
            object : Fandom {
                override val id: String
                    get() = it.toString()
                override val name: String
                    get() = "name $it"
                override val domain: String
                    get() = "n$it.fandom.com"

                override fun toString(): String {
                    return "Fandom{$id,$domain}"
                }
            }
        }
    }
}
