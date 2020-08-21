package stan.aac.experience.implementation.util.platform.android.lifecycle

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore

inline fun <reified T : ViewModel> viewModel(
    viewModelStore: ViewModelStore,
    viewModelProviderFactory: ViewModelProvider.Factory
): T {
    return ViewModelProvider(viewModelStore, viewModelProviderFactory).get(T::class.java)
}

inline fun <reified T : ViewModel> Fragment.viewModel(): T {
    return viewModel(viewModelStore, defaultViewModelProviderFactory)
}
